package com.example.android.dwms

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer

class RfconfigActivity() : AppCompatActivity() {

    companion object {
        val ddlb_db = DddwDbHelper()  //// dropdown list 데이터 db select class by lapiss 2020.04.28
        val dbupate = DBupdateHelper()  ///// update 처리 Process
        val connectionClass = ConnectionClass()
    }

    var ls_rfno: String? = ""
    var ls_ware: String? = ""
    var ls_owner: String? = ""
    var ls_recloc: String? = ""
    var ls_retloc: String? = ""
    var ls_divi: String? = ""
    var ls_window: String? = ""
    var li_shplabel: Int? = 0
    var li_trklabel: Int? = 0

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rfconfig)

        ls_rfno = App.prefs.g_rfid
        ls_ware = App.prefs.g_warehouse
        ls_owner = App.prefs.g_owner

        Getrfinfo()  //// 기존 RF단말기의 정보 취득

        var array_owner = resources.getStringArray(R.array.OWNER)
        var array_prtlist1 = resources.getStringArray(R.array.PRTCNT)
        var array_prtlist2 = resources.getStringArray(R.array.PRTCNT)
        var array_recloc = ddlb_db.getreclocation()
        var array_retloc = ddlb_db.getretlocation()
        var array_pickdivi = ddlb_db.getpickdivision()

        val owner_spinner = findViewById<AppCompatSpinner>(R.id.ownerspinner)
        val recloc_spinner = findViewById<AppCompatSpinner>(R.id.reclocspinner)
        val retloc_spinner = findViewById<AppCompatSpinner>(R.id.retlocspinner)
        val pickdivi_spinner = findViewById<AppCompatSpinner>(R.id.pickdivispinner)
        val shiplabel_spinner = findViewById<AppCompatSpinner>(R.id.shiplabelpinner)
        val trklabel_spinner = findViewById<AppCompatSpinner>(R.id.trklabelspinner)

        if (array_owner!=null) {
            owner_spinner.spinnerAdapterArray(array_owner.indexOf(ls_owner), array_owner)
        }
        if (array_recloc!=null) {
            recloc_spinner.spinnerAdapterArray(array_recloc.indexOf(ls_recloc), array_recloc)
        }
        if (array_retloc!=null) {
            retloc_spinner.spinnerAdapterArray(array_retloc.indexOf(ls_retloc), array_retloc)
        }
        if (array_pickdivi!=null) {
            pickdivi_spinner.spinnerAdapterArray(array_pickdivi.indexOf(ls_divi), array_pickdivi)
        }
        if (array_prtlist1!=null) {
        shiplabel_spinner.spinnerAdapterArray(array_prtlist1.indexOf(li_shplabel.toString()), array_prtlist1)
        }
        if (array_prtlist2!=null) {
        trklabel_spinner.spinnerAdapterArray(array_prtlist2.indexOf(li_trklabel.toString()), array_prtlist2)
        }

     }

    fun CloseOnClick(view: View) {

        ls_window = "MainActivity"

        var result = dbupate.update_rf_window(ls_window!!)

        if (result < 0 ) {
            Toast.makeText(this@RfconfigActivity, "RF단말기 정보변경처리 중 오류!", Toast.LENGTH_SHORT).show()
        }else{
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

    fun ChangeconfigOnClick(view: View) {

        var owner_spin = findViewById<AppCompatSpinner>(R.id.ownerspinner)
        var recloc_spin = findViewById<AppCompatSpinner>(R.id.reclocspinner)
        var retloc_spin = findViewById<AppCompatSpinner>(R.id.retlocspinner)
        var pickdivi_spin = findViewById<AppCompatSpinner>(R.id.pickdivispinner)
        var shiplabel_spin = findViewById<AppCompatSpinner>(R.id.shiplabelpinner)
        var trklabel_spin = findViewById<AppCompatSpinner>(R.id.trklabelspinner)

        ls_owner = owner_spin.getSelectedItem().toString()
        ls_recloc = recloc_spin.getSelectedItem().toString()
        ls_retloc = retloc_spin.getSelectedItem().toString()
        ls_divi = pickdivi_spin.getSelectedItem().toString()
        li_shplabel = shiplabel_spin.getSelectedItem().toString().toInt()
        li_trklabel = trklabel_spin.getSelectedItem().toString().toInt()

        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
        val dialogTitle = dialogView.findViewById<TextView>(R.id.dialogTitle)
        val dialogText = dialogView.findViewById<TextView>(R.id.dialogEt)

        dialogTitle.text = "저장확인"
        dialogText.text = "RF단말기의 셋팅값을 저장하시겠습니까?"

        builder.setView(dialogView)
            .setPositiveButton("확인") { dialogInterface, i ->
                /* 확인일 때 main의 View의 값에 dialog View에 있는 값을 적용 */
                var result = dbupate.update_rf_config_info(ls_owner!!, ls_recloc!!, ls_retloc!!, ls_divi!!,li_shplabel!!, li_trklabel!!)

                if (result < 0 ) {
                }else{
                    Toast.makeText(this@RfconfigActivity, "정상처리완료!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("취소") { dialogInterface, i ->
                /* 취소일 때 아무 액션이 없으므로 빈칸 */
            }
            .show()


         /* Toast.makeText(this@RfconfigActivity, "$ls_owner, $ls_recloc, $ls_retloc,$ls_divi, $li_shplabel, $li_trklabel", Toast.LENGTH_SHORT).show()*/

    }


    fun Getrfinfo() {
        try {
            var Con = connectionClass?.dbConn()
            if (Con == null) {
            } else {
                var query =
                    "SELECT owner_id, rec_location, ret_location, division_id, ship_label_number, truck_label_number " +
                            "FROM rf_$ls_ware"+"_"+"$ls_rfno where warehouse_id='$ls_ware' and rf_id ='$ls_rfno' "
                val stmt = Con!!.createStatement()
                val rs = stmt.executeQuery(query)
                if (rs != null) {
                    while (rs.next()) {
                        try {
                            ls_owner = (rs.getString("owner_id"))
                            ls_recloc = (rs.getString("rec_location"))
                            ls_retloc = (rs.getString("ret_location"))
                            ls_divi = (rs.getString("division_id"))
                            li_shplabel = (rs.getInt("ship_label_number"))
                            li_trklabel = (rs.getInt("truck_label_number"))
                        }catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                }else {
                }
                Con.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val writer: Writer = StringWriter()
            e.printStackTrace(PrintWriter(writer))
        }
    }

    fun AppCompatSpinner.spinnerAdapterArray(position: Int, arrayList: ArrayList<String>): String {
        var selectedItem: String = ""
        val adapter = ArrayAdapter(context, R.layout.custom_simple_dropdown_item_1line, arrayList)
        adapter.setDropDownViewResource(R.layout.custom_simple_dropdown_item_1line)
        this.adapter = adapter
        this.setSelection(position)
        this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItem = arrayList[position]
/*                Toast.makeText(this@RfconfigActivity, "$id, $position, $selectedItem", Toast.LENGTH_SHORT).show()*/
            }
        }
        return selectedItem

    }

    fun AppCompatSpinner.spinnerAdapterArray(position: Int, array: Array<String>): String {
        var selectedItem: String = ""
        val adapter = ArrayAdapter(context, R.layout.custom_simple_dropdown_item_1line, array)
        adapter.setDropDownViewResource(R.layout.custom_simple_dropdown_item_1line)
        this.adapter = adapter
        this.setSelection(position)
        this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItem = array[position]
/*                Toast.makeText(this@RfconfigActivity, "$id, $position, $selectedItem", Toast.LENGTH_SHORT).show()*/
            }
        }
        return selectedItem

    }

    override fun onBackPressed() {
        super.onBackPressed()

        ls_window = "MainActivity"

        var result = dbupate.update_rf_window(ls_window!!)

        if (result < 0 ) {
        }else{
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }

    }

}


