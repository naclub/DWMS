package com.example.android.dwms

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchLocationActivity : AppCompatActivity() {

    companion object {
        val db = DatabaseHelper()
        val dbupate = DBupdateHelper()  ///// update 처리 Process
    }

    lateinit var rv : RecyclerView
    lateinit var edt_location : EditText
    lateinit var btnsearchloc : Button
    var ls_window: String? = ""

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_location)

        edt_location = findViewById(R.id.txtlocation_id)
        btnsearchloc = findViewById(R.id.btnSearchloc)

        rv = findViewById(R.id.rvSearchLocation)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        // recyclerview 에 선 긋기

        this.btnsearchloc.setOnClickListener {

            var location_id  =  edt_location.getText().toString()
                //location_id  =  "${location_id.replace("-","")}"
                //System.out.println("location_id값 = " +location_id)
            if (location_id != null) {
                if (location_id.trim { it <= ' ' } == "") {
                    Toast.makeText(this@SearchLocationActivity, "로케이션을 입력하세요!", Toast.LENGTH_SHORT).show()
                }else {
                    //로케이션 입력 시 조회하기 by lapiss 2020.04.17

                    Toast.makeText(this@SearchLocationActivity, "로케이션 : $location_id", Toast.LENGTH_SHORT).show()
                    db.ctx = this@SearchLocationActivity
                    db.getSearchLocation(rv, "", "", location_id)

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