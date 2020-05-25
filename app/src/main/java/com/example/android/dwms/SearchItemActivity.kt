package com.example.android.dwms

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer


class SearchItemActivity : AppCompatActivity() {

    companion object {
        val db = DatabaseHelper()
        val dbupate = DBupdateHelper()  ///// update 처리 Process
        val connectionClass = ConnectionClass()
    }

    lateinit var rv : RecyclerView
    lateinit var edt_barcode : EditText
    lateinit var txt_item_id : TextView
    lateinit var txt_item_nm : TextView
    lateinit var btnsearch : Button

    var item_id : String = ""  //// 바코드로 추출한 단품코드
    var item_nm : String = ""  //// 바코드로 추출한 단품명
    var ls_window: String? = ""

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_item)

        edt_barcode = findViewById(R.id.txtbarcode)
        txt_item_id = findViewById(R.id.txtitem_id)
        txt_item_nm = findViewById(R.id.txtitem_nm)
        btnsearch = findViewById(R.id.btnSearch)

        rv = findViewById(R.id.rvSearchItem)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        // recyclerview 에 선 긋기

        this.btnsearch.setOnClickListener {

            var barcode_txt = edt_barcode.getText().toString()
            txt_item_id.text = ""
            txt_item_nm.text = ""

            if (barcode_txt != null) {
                if (barcode_txt.trim { it <= ' ' } == "") {
                    Toast.makeText(this@SearchItemActivity, "바코드를 입력하세요!", Toast.LENGTH_SHORT).show()
                }else if( barcode_txt.length != 17 ){
                    Toast.makeText(this@SearchItemActivity, "유효한 바코드가 아닙니다!", Toast.LENGTH_SHORT).show()
                }else {
                    //바코드 입력 시 바코르->단품코드, 단품명 불러와서 조회하기 by lapiss 2020.04.17

                    BarcodeSelect().execute("")

                }
            }
        }
    }

    inner class BarcodeSelect : AsyncTask<String?, String?, String>() {
        private var success = false
        var msg = "No Data Found"
 //       var progress: ProgressDialog? = null

        override fun onPreExecute() {
 //           progress = ProgressDialog.show(this@SearchItemActivity, "Synchronising","Please Wait...", true )
        }

        @SuppressLint("WrongThread")
        override fun doInBackground(vararg params: String?): String {
            try {
                val conn = connectionClass?.dbConn()
                if (conn == null) {
                    msg = "Please Check Your Connection"
                    success = false
                } else {
                    var v_barcode = edt_barcode.getText().toString()
                    var query = "{call ksbed_barcodeselect('$v_barcode')}"
                    var stmt = conn!!.prepareCall(query)
                    var rs= stmt.executeQuery()
                    if (rs != null) {
                        while (rs.next()) {
                            try {

                                var itemseq = (rs.getInt("itemseq"))
                                var query2 = "select item_id, item_nm from item where itemseq = '$itemseq' "
                                var statement = conn!!.createStatement()
                                var rs2 = statement.executeQuery(query2)

                                if ( rs2 !=null) {
                                    while (rs2.next()) {
                                        try {
                                            txt_item_id.setText(rs2.getString("item_id"))
                                            txt_item_nm.setText(rs2.getString("item_nm"))
                                            
                                        }catch(ex: Exception) {
                                            ex.printStackTrace()
                                        }
                                    }
                                }
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        }
                        msg = "Found"
                        success = true
                    } else {
                        msg = "No Data found!"
                        success = false
                    }
                    conn.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val writer: Writer = StringWriter()
                e.printStackTrace(PrintWriter(writer))
                msg = writer.toString()
                success = false
            }
            return msg
        }

        override fun onPostExecute(msg: String) {
  //          progress!!.dismiss()
 //           Toast.makeText(this@SearchItemActivity, msg, Toast.LENGTH_LONG).show()
            if (success === false) {
            } else {
                try {

                    item_id = txt_item_id.text.toString()
                    item_nm = txt_item_nm.text.toString()

                    Toast.makeText(this@SearchItemActivity, "변경후 : $item_id", Toast.LENGTH_LONG).show()
                    db.ctx = this@SearchItemActivity
                    db.getSearchItem(rv, "", "", item_id)

                } catch (ex: Exception) {
                }
            }
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

