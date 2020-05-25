package com.example.android.dwms

import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer

class MainActivity : AppCompatActivity() {

    companion object {
        val dbupate = DBupdateHelper()  ///// update 처리 Process
        val connectionClass = ConnectionClass()
    }

    var ls_rfno: String? = ""
    var ls_ware: String? = ""
    var ls_owner: String? = ""
    var ls_window: String? = ""
    lateinit var i : Intent
    var result: Int? = 0

    val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 300)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ls_rfno = App.prefs.g_rfid
        ls_ware = App.prefs.g_warehouse
        ls_owner = App.prefs.g_owner

        get_lastactivity()

  /*      Toast.makeText(this, "$ls_window", Toast.LENGTH_SHORT).show()*/

        /// rf단말기에 저장된 Activity 로 자동 화면전환 by lapiss 2020.04.29
        if (ls_window == null){

        }else{
            if(ls_window != "MainActivity") {
                try {
                    when (ls_window) {
                        "ReceiptActivity" -> i = Intent(this, ReceiptActivity::class.java)  // 입하
                        "ProductsActivity" -> i = Intent(this, ProductsActivity::class.java) // 입고
                        "CodeActivity" -> i = Intent(this, CodeActivity::class.java) // 이동
                        "ItemActivity" -> i = Intent(this, ItemActivity::class.java) // 피킹
                        "UnloadingwavelistActivity" -> i = Intent(this, UnloadingwavelistActivity::class.java) // 상차
                        "SearchItemActivity" -> i = Intent(this, SearchItemActivity::class.java) // Item조회
                        "SearchLocationActivity" -> i = Intent(this, SearchLocationActivity::class.java) //로케이션 조회
                        "RfconfigActivity" -> i = Intent(this, RfconfigActivity::class.java) // RF설정
                        "CustomersActivity" -> i = Intent(this, CustomersActivity::class.java)

                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                startActivity(i)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun ReceiptsOnClick(view: View) {
        i = Intent(this, ReceiptActivity::class.java)
        result = dbupate.update_rf_window("ReceiptActivity")

        //toneGen1.startTone(ToneGenerator.TONE_SUP_ERROR, 300)

        if (result!! < 0 ) {
            Toast.makeText(this, "RF단말기 정보변경처리 중 오류!", Toast.LENGTH_SHORT).show()
        }else{
            startActivity(i)
        }
    }

    fun PutawayOnClick(view: View) {
        i = Intent(this, ProductsActivity::class.java)
        result = dbupate.update_rf_window("ProductsActivity")


        if (result!! < 0 ) {
            Toast.makeText(this, "RF단말기 정보변경처리 중 오류!", Toast.LENGTH_SHORT).show()
        }else{
            startActivity(i)
        }
    }

    fun MoveitemOnClick(view: View) {
        i = Intent(this, CodeActivity::class.java)
        result = dbupate.update_rf_window("CodeActivity")

        if (result!! < 0 ) {
            Toast.makeText(this, "RF단말기 정보변경처리 중 오류!", Toast.LENGTH_SHORT).show()
        }else{
            startActivity(i)
        }
    }

    fun PickingOnClick(view: View) {
        i = Intent(this, ItemActivity::class.java)
        result = dbupate.update_rf_window("ItemActivity")

        if (result!! < 0 ) {
            Toast.makeText(this, "RF단말기 정보변경처리 중 오류!", Toast.LENGTH_SHORT).show()
        }else{
            startActivity(i)
        }
    }

    fun LoadingOnClick(view: View) {
        i = Intent(this, UnloadingwavelistActivity::class.java)
        result = dbupate.update_rf_window("UnloadingwavelistActivity")

        if (result!! < 0 ) {
            Toast.makeText(this, "RF단말기 정보변경처리 중 오류!", Toast.LENGTH_SHORT).show()
        }else{
            startActivity(i)
        }
    }

    fun CyclecountOnClick(view: View) {
        i = Intent(this, UnloadingwavelistActivity::class.java)
        result = dbupate.update_rf_window("UnloadingwavelistActivity")

        if (result!! < 0 ) {
            Toast.makeText(this, "RF단말기 정보변경처리 중 오류!", Toast.LENGTH_SHORT).show()
        }else{
            startActivity(i)
        }
    }

    fun SearchitemOnClick(view: View) {
        i = Intent(this, SearchItemActivity::class.java)
        result = dbupate.update_rf_window("SearchItemActivity")

        if (result!! < 0 ) {
            Toast.makeText(this, "RF단말기 정보변경처리 중 오류!", Toast.LENGTH_SHORT).show()
        }else{
            startActivity(i)
        }
    }

    fun SearchlocationOnClick(view: View) {
        i = Intent(this, SearchLocationActivity::class.java)
        result = dbupate.update_rf_window("SearchLocationActivity")

        if (result!! < 0 ) {
            Toast.makeText(this, "RF단말기 정보변경처리 중 오류!", Toast.LENGTH_SHORT).show()
        }else{
            startActivity(i)
        }
    }

    fun ConfigOnClick(view: View) {
        i = Intent(this, RfconfigActivity::class.java)
        result = dbupate.update_rf_window("RfconfigActivity")

        if (result!! < 0 ) {
            Toast.makeText(this, "RF단말기 정보변경처리 중 오류!", Toast.LENGTH_SHORT).show()
        }else{
            startActivity(i)
        }
    }

    fun ExitOnClick(view: View) {
/*        ActivityCompat.finishAffinity(this) //해당 앱의 루트 액티비티를 종료시킨다. (API  16미만은 ActivityCompat.finishAffinity())
        System.runFinalization() //현재 작업중인 쓰레드가 다 종료되면, 종료 시키라는 명령어이다.
        System.exit(0) // 현재 액티비티를 종료시킨다.*/
        result = dbupate.update_rf_window("MainActivity")

        val i = Intent(this, LoginActivity::class.java)
        if (result!! < 0 ) {
            Toast.makeText(this, "RF단말기 정보변경처리 중 오류!", Toast.LENGTH_SHORT).show()
        }else{
            startActivity(i)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        result = dbupate.update_rf_window("MainActivity")

        val i = Intent(this, LoginActivity::class.java)
        if (result!! < 0 ) {
            Toast.makeText(this, "RF단말기 정보변경처리 중 오류!", Toast.LENGTH_SHORT).show()
        }else{
            startActivity(i)
        }
    }

    fun get_lastactivity() {
        try {
            var Con = connectionClass?.dbConn()
            if (Con == null) {
            } else {
                var query = "SELECT window_id from rf_$ls_ware"+"_"+"$ls_rfno where warehouse_id='$ls_ware' and rf_id ='$ls_rfno' "
                val stmt = Con!!.createStatement()
                val rs = stmt.executeQuery(query)
                if (rs != null) {
                    while (rs.next()) {
                        try {
                            ls_window = (rs.getString("window_id"))
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


}