package com.example.android.dwms

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer


class ReceiptActivity : AppCompatActivity() {

    companion object {
        val dbupate = DBupdateHelper()  ///// update 처리 Process
        val ReceiptDbUpdate = ReceiptDBHelper()  ///// update 처리 Process
        val connectionClass = ConnectionClass()
        val db = DatabaseHelper()
    }

    var ls_rfno: String? = ""
    var ls_ware: String? = ""
    var ls_owner: String? = ""
    var ls_window: String? = ""
    var ls_recloc: String? = ""
    var owner : TextView? = null
    var recloc : TextView? = null
    var barcode : EditText? = null
    lateinit var btnReceipt : Button
    lateinit var rv : RecyclerView
    var item_id : String? = ""
    var com_goodcd : String? = ""
    var i_seq : Int? = 0
    var i_serl : Int? = 0
    var ll_cnt : Int = 0
    var loc_cnt : Int = 0
    var rest : Int = 0
    var receipt_no : String? = ""
    var receipt_seqno : String? = ""
    var receipt_type : String? = ""
    var lot_no : String? = ""
    var receipt_query : String? = ""
    var confirm_yn : String? = "N"
    var ls_return : String? = ""


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)

        ls_rfno = App.prefs.g_rfid
        ls_ware = App.prefs.g_warehouse
        ls_owner = App.prefs.g_owner

        owner = findViewById<View>(R.id.owner_id) as TextView
        recloc = findViewById<View>(R.id.recloc_id) as TextView
        barcode = findViewById<View>(R.id.txtbarcode) as EditText
        btnReceipt = findViewById<View>(R.id.btnReceipt) as Button

        rv = findViewById(R.id.rvReceiptItem)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        // recyclerview 에 선 긋기


        Getrfinfo()  //// 기존 RF단말기의 정보 취득

        owner!!.setText(ls_owner.toString())
        recloc!!.setText(ls_recloc.toString())

        barcode!!.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                ReceiptOnClick()
                return@OnEditorActionListener true
            }
            false
        })

        btnReceipt.setOnClickListener(View.OnClickListener {
            ReceiptOnClick()
        })
    }

    fun ReceiptOnClick() {

        var barcode_txt = barcode?.getText().toString()

        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
        val dialogTitle = dialogView.findViewById<TextView>(R.id.dialogTitle)
        val dialogText = dialogView.findViewById<TextView>(R.id.dialogEt)

        if (barcode_txt != null) {
            if (barcode_txt.trim { it <= ' ' } == "") {
                Toast.makeText(this@ReceiptActivity, "바코드를 입력하세요!", Toast.LENGTH_SHORT).show()
            }else if( barcode_txt.length != 17 ){
                Toast.makeText(this@ReceiptActivity, "유효한 바코드가 아닙니다!", Toast.LENGTH_SHORT).show()
            }else {
                //바코드 입력 시 입하계획 완료여부 확인하여 입하처리 by lapiss 2020.04.17

                BarcodeCheck() //// 바코드 정보 획득

                if (item_id != null){

                    if (com_goodcd?.equals('3')!!) {//// 외주납품인 경우 입고미완료된 건 중에 빠른 순서로 처리 by lapiss 2020.02.10
                        receipt_query = "select count (*) as cnt, isnull(sum(a.plan_qty-a.confirm_qty),0) as rest," +
                                "isnull(max(a.receipt_no),'') as receipt_no, isnull(max(a.receipt_seqno),'') as receipt_seqno, isnull(max(a.receipt_type),'') as receipt_type, " +
                                "isnull(max(a.lot_no),'') as lot_no " +
                                "from receipt_line a where a.warehouse_id='$ls_ware' and a.owner_id ='$ls_owner' " +
                                "and a.workorderseq = '$i_seq' and a.workorderserl = '$i_serl' and a.confirm_yn = 'N' "
                    }else {
                        receipt_query = "select count (*) as cnt, isnull(sum(a.plan_qty-a.confirm_qty),0) as rest," +
                                "isnull(max(a.receipt_no),'') as receipt_no, isnull(max(a.receipt_seqno),'') as receipt_seqno, isnull(max(a.receipt_type),'') as receipt_type, " +
                                "isnull(max(a.lot_no),'') as lot_no " +
                                "from receipt_line a where a.warehouse_id='$ls_ware' and a.owner_id ='$ls_owner' " +
                                "and a.workorderseq = '$i_seq' and a.workorderserl = '$i_serl' "
                    }

                    try {
                        var Con = connectionClass?.dbConn()
                        if (Con == null) {
                        } else {
                            var stmt = Con!!.createStatement()
                            var rs = stmt.executeQuery(receipt_query)
                            if (rs != null) {
                                while (rs.next()) {
                                    try {
                                        ll_cnt = rs.getInt("cnt")
                                        rest = rs.getInt("rest")
                                        receipt_no = rs.getString("receipt_no")
                                        receipt_seqno = rs.getString("receipt_seqno")
                                        receipt_type = rs.getString("receipt_type")
                                        lot_no = rs.getString("lot_no")

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

                    if (ll_cnt == 0) {
                        dialogTitle.text = "입하계획확인"
                        dialogText.text = "ER0072:이관한 입하계획이 없습니다!\n item_id : '$item_id' receipt_no : '$receipt_no'"
                        builder.setView(dialogView)
                            .setPositiveButton("확인") { dialogInterface, i ->
                               //  확인일 때 main의 View의 값에 dialog View에 있는 값을 적용
                            }
                            .show()
/*                        Toast.makeText(this@ReceiptActivity, "ll_cnt : '$ll_cnt' rest : '$rest' receipt_no : '$receipt_no' ", Toast.LENGTH_SHORT).show()*/
                        barcode?.requestFocus()
                        return
                    }


                    if (rest == 0) {
                        dialogTitle.text = "입하수량초과"
                        dialogText.text = "ER0006:입하량 초과!\n item_id : '$item_id' receipt_no : '$receipt_no'"

                        builder.setView(dialogView)
                            .setPositiveButton("확인") { dialogInterface, i ->
                                 //확인일 때 main의 View의 값에 dialog View에 있는 값을 적용
                            }
/*                            .setNegativeButton("취소") { dialogInterface, i ->
                                 //취소일 때 아무 액션이 없으므로 빈칸
                            }*/
                            .show()
/*                        Toast.makeText(this@ReceiptActivity, "ll_cnt : '$ll_cnt' rest : '$rest' receipt_no : '$receipt_no' ", Toast.LENGTH_SHORT).show()*/
                        barcode?.requestFocus()
                        return
                    }

                    //// receipt_line_history에서 해당 바코드 입하여부 확인
                    try {
                        var Con = connectionClass?.dbConn()
                        if (Con == null) {
                        } else {
                            var query =
                                "SELECT count(*) as cnt from receipt_line_history where rl_no = '$barcode_txt' "
                            var stmt = Con!!.createStatement()
                            var rs = stmt.executeQuery(query)
                            if (rs != null) {
                                while (rs.next()) {
                                    try {
                                        ll_cnt = rs.getInt("cnt")

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

                    if (ll_cnt > 0) {
                        dialogTitle.text = "입하완료 바코드"
                        dialogText.text = "ER0074:입하완료된 바코드 번호입니다!"
                        builder.setView(dialogView)
                            .setPositiveButton("확인") { dialogInterface, i ->
                                //  확인일 때 main의 View의 값에 dialog View에 있는 값을 적용
                            }
                            .show()
/*                        Toast.makeText(this@ReceiptActivity, "ll_cnt : '$ll_cnt' rest : '$rest' receipt_no : '$receipt_no' ", Toast.LENGTH_SHORT).show()*/
                        barcode?.requestFocus()
                        return
                    }

                    if (rest == 1) {
                        confirm_yn = "Y"
                    } else{
                        confirm_yn = "N"
                    }

                    //// item_location_detail 에서 해당 입하번호, 순번의 입하여부 확인
                    try {
                        var Con = connectionClass?.dbConn()
                        if (Con == null) {
                        } else {
                            var query =
                                "select count(*) as loc_cnt from item_location_detail " +
                                "where warehouse_id = '$ls_ware' and owner_id = '$ls_owner' and item_id = '$item_id' "+
                                "and pallet_id = '' and receipt_no = '$receipt_no' and receipt_seqno = '$receipt_seqno' "
                            var stmt = Con!!.createStatement()
                            var rs = stmt.executeQuery(query)
                            if (rs != null) {
                                while (rs.next()) {
                                    try {
                                        loc_cnt = rs.getInt("loc_cnt")
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

                    ls_return = ReceiptDbUpdate.receipt_process_execute(loc_cnt!!, ls_owner!!, receipt_no!!, receipt_seqno!!, confirm_yn!!, barcode_txt!!, item_id!!,
                            receipt_type!!, ls_recloc!!, i_seq!!, i_serl!!, com_goodcd!!)

                    if(ls_return != "OK"){
                        dialogTitle.text = "입하오류"
                        dialogText.text = ls_return
                        builder.setView(dialogView)
                            .setPositiveButton("확인") { dialogInterface, i ->
                                //확인일 때 main의 View의 값에 dialog View에 있는 값을 적용
                            }
/*                            .setNegativeButton("취소") { dialogInterface, i ->
                                 //취소일 때 아무 액션이 없으므로 빈칸
                            }*/
                            .show()
/*                        Toast.makeText(this@ReceiptActivity, "ll_cnt : '$ll_cnt' rest : '$rest' receipt_no : '$receipt_no' ", Toast.LENGTH_SHORT).show()*/
                        barcode?.requestFocus()
                        return

                    }else{
                        Toast.makeText(this@ReceiptActivity, "입하처리완료!", Toast.LENGTH_SHORT).show()

                        db.ctx = this@ReceiptActivity
                        db.getReceiptlist(rv, "", "", item_id!!, receipt_no!! )

                        barcode?.requestFocus()
                    }

                }else{
                    dialogTitle.text = "품목정보 확인"
                    dialogText.text = "ER0055:품목정보에 오류가 있습니다!\n item_id : '$item_id' receipt_no : '$receipt_no'"
                    builder.setView(dialogView)
                        .setPositiveButton("확인") { dialogInterface, i ->
                            //  확인일 때 main의 View의 값에 dialog View에 있는 값을 적용
                        }
                        .show()
                    barcode?.requestFocus()
                    return
                }

            }
        }

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

    fun BarcodeCheck() {
        try {
            val conn = connectionClass?.dbConn()
            if (conn == null) {
            } else {
                var v_barcode = barcode?.getText().toString()
                var query = "{call ksbed_barcodeselect('$v_barcode')}"
                var stmt = conn!!.prepareCall(query)
                var rs = stmt.executeQuery()
                if (rs != null) {
                    while (rs.next()) {
                        try {
                            i_seq = rs.getInt("seq")
                            i_serl = rs.getInt("Serl")
                            var query2 =
                                "select  item_id, com_goodcd from receipt_line " +
                                "where workorderseq = '$i_seq' and workorderserl = '$i_serl' " +
                                "and   receipt_type = 'NORMAL' "
                            var statement = conn!!.createStatement()
                            var rs2 = statement.executeQuery(query2)

                            if (rs2 != null) {
                                while (rs2.next()) {
                                    try {
                                        item_id = rs2.getString("item_id")
                                        com_goodcd = rs2.getString("com_goodcd")

                                    } catch (ex: Exception) {
                                        ex.printStackTrace()
                                    }
                                }
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                } else {
                }
                conn.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val writer: Writer = StringWriter()
            e.printStackTrace(PrintWriter(writer))
        }
    }

    fun CloseOnClick(view: View) {

        ls_window = "MainActivity"

        var result = dbupate.update_rf_window(ls_window!!)

        if (result < 0 ) {
            Toast.makeText(this@ReceiptActivity, "RF단말기 정보변경처리 중 오류!", Toast.LENGTH_SHORT).show()
        }else{
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
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

