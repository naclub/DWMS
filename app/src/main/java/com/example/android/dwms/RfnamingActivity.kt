package com.example.android.dwms

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class RfnamingActivity : AppCompatActivity() {

    var ls_rfno : String? = ""
    var ls_ware : String? = ""
    var syspwd : EditText? = null
    var ls_pwd : String? = "8888"

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rfnaming)

        ls_rfno = App.prefs.g_rfid
        ls_ware = App.prefs.g_warehouse

        syspwd = findViewById<View>(R.id.txtsyspwd) as EditText

        val array_rfno = resources.getStringArray(R.array.RFLIST)
        val array_wareinfo = resources.getStringArray(R.array.WARE)

        val rf_spinner = findViewById<Spinner>(R.id.rfspinner)
        val ware_spinner = findViewById<Spinner>(R.id.rfwarehouse)

        if ( rf_spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, array_rfno)
            rf_spinner.adapter = adapter

            //// 초기값 셋팅
            if (ls_rfno == "RF001") {
                rf_spinner.setSelection(0)
            }else if(ls_rfno == "RF002") {
                rf_spinner.setSelection(1)
            }else if(ls_rfno == "RF003") {
                rf_spinner.setSelection(2)
            }else if(ls_rfno == "RF004") {
                rf_spinner.setSelection(3)
            }else if(ls_rfno == "RF005") {
                rf_spinner.setSelection(4)
            }else if(ls_rfno == "RF006") {
                rf_spinner.setSelection(5)
            }


            rf_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){

                    ls_rfno = array_rfno[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>){

                }
            }
        }

        if ( ware_spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, array_wareinfo)
            ware_spinner.adapter = adapter

            if (ls_ware == "K01") {
                ware_spinner.setSelection(0)
            }else if(ls_ware == "K03") {
                ware_spinner.setSelection(1)
            }

            ware_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){

                    ls_ware = array_wareinfo[position]
                 }
                override fun onNothingSelected(parent: AdapterView<*>){

                }
            }
        }
    }

    fun CloseOnClick(view: View) {
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }

    fun SaveconfigOnClick(view: View) {

        var password_chk : String? = syspwd!!.text.toString()

        if (password_chk != null) {
            if (password_chk.trim { it <= ' ' } == "") {
                Toast.makeText(this@RfnamingActivity, "비밀번호를 입력하고 진행하세요!!", Toast.LENGTH_SHORT).show()
            }

            if(ls_pwd == password_chk){
                App.prefs.g_rfid = ls_rfno
                App.prefs.g_warehouse = ls_ware
                Toast.makeText(this@RfnamingActivity, "rf : $ls_rfno warehouse : $ls_ware", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@RfnamingActivity, "잘못된 비밀번호입니다!", Toast.LENGTH_SHORT).show()
            }
        }

    }


}