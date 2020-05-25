package com.example.android.dwms

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UnloadingwavelistActivity : AppCompatActivity() {

    companion object {
        val db2 = DatabaseHelper_sp()
        val dbupate = DBupdateHelper()  ///// update 처리 Process
    }

    var ls_window: String? = ""

    lateinit var rv : RecyclerView

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unloadingwavelist)

        rv = findViewById(R.id.rvUnloadingwavelist)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        db2.ctx = this
        db2.getUnloadingwavelist(rv)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        ls_window = "MainActivity"

        var result = SearchLocationActivity.dbupate.update_rf_window(ls_window!!)

        if (result < 0 ) {
        }else{
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }

    }
}