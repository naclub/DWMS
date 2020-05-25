package com.example.android.dwms

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import org.ini4j.Ini

class LoginActivity : AppCompatActivity() {

    companion object {
        val connectionClass = ConnectionClass()
    }

    protected var ini: Ini? = null
    protected var s_rfno: String? = null

    var login: Button? = null
    var id: EditText? = null
    var rfno: EditText? = null
    var password: EditText? = null
    var progressBar: ProgressBar? = null

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login = findViewById<View>(R.id.btnLogin) as Button
        id = findViewById<View>(R.id.id) as EditText
        password = findViewById<View>(R.id.password) as EditText
        rfno = findViewById<View>(R.id.rfno) as EditText

        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        progressBar!!.visibility = View.INVISIBLE

        password!!.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
        password!!.setTransformationMethod(PasswordTransformationMethod.getInstance())

        ///// 기존에 저장된 로그인 id와 rf단말기 정보
        id!!.setText(App.prefs.g_loginid)
        rfno!!.setText(App.prefs.g_rfid)

        login!!.setOnClickListener {
            val checkLogin = CheckLogin()
            checkLogin.execute("")

        }
    }

    inner class CheckLogin : AsyncTask<String?, String?, String?>() {
        var z: String? = ""
        var isSuccess = false
        override fun onPreExecute() {
            progressBar!!.visibility = View.VISIBLE
        }

        @SuppressLint("WrongThread")
        override fun doInBackground(vararg params: String?): String? {
            var sid = id!!.text.toString()
            var spassword = password!!.text.toString()
            if (sid.trim { it <= ' ' } == "") {
                z = "아이디를 입력하세요!"
            }else if(spassword.trim { it <= ' ' } == "") {
                z = "비밀번호를 입력하세요!"
            }else{
                try {
//                   con = connectionclass(un, pass, db, ip)
                    var Con = connectionClass?.dbConn()
                    if (Con == null) {
                        z = "Check Your Internet Access!"
                    } else {
                        var query =
                            "select * from users where user_id = '$sid' and user_pwd = '$spassword'"
                        val stmt = Con!!.createStatement()
                        val rs = stmt.executeQuery(query)
                        if (rs.next()) {
                            z = "Login successful"
                            isSuccess = true
                            Con!!.close()
                        } else {
                            z = "등록되지 않은 사용자입니다!"
                            isSuccess = false
                        }
                        Con.close()
                    }
                } catch (ex: Exception) {
                    isSuccess = false
                    z = ex.message
                }
            }
            return z
        }

        override fun onPostExecute(r: String?) {
            progressBar!!.visibility = View.INVISIBLE
            Toast.makeText(this@LoginActivity, r, Toast.LENGTH_SHORT).show()
            if (isSuccess) {
                //// 입력한 id로 저장
                App.prefs.g_loginid = id!!.text.toString()
                val nextintent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(nextintent)
            }
        }
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            ActivityCompat.finishAffinity(this) //해당 앱의 루트 액티비티를 종료시킨다. (API  16미만은 ActivityCompat.finishAffinity())
            System.runFinalization() //현재 작업중인 쓰레드가 다 종료되면, 종료 시키라는 명령어이다.
            System.exit(0) // 현재 액티비티를 종료시킨다.
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "'뒤로' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    fun RfconfigOnClick(view: View) {
        val i = Intent(this, RfnamingActivity::class.java)
        startActivity(i)
    }
}