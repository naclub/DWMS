
package com.example.android.dwms

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.io.PrintWriter
import java.io.StringWriter

class DatabaseHelper_sp {

    companion object {
        val connectionClass = ConnectionClass()
    }

    lateinit var ctx : Context

    private var isDone = false
    private lateinit var rv : RecyclerView
    private lateinit var query : String
    private lateinit var adapter : RecyclerView.Adapter<*>
    private var recordCount : Int = 0
    private var functionType : Int = 0
    private lateinit var records : ArrayList<Any>

    inner class SyncData2 : AsyncTask<String, String, String>(){
        private var message = "No Connection or Windows FireWall, not enough permission Error!"
        lateinit var prog : ProgressDialog

        override fun onPreExecute() {
            records.clear()
            recordCount = 0
            prog = ProgressDialog.show(ctx, "Reading Data....", "Loading.. Please Wati", true)
        }

        override fun doInBackground(vararg params: String?): String {
            try {
            var myConn = connectionClass?.dbConn()
                if (myConn == null) {
                    isDone = false
                } else {
                    val cstmt = myConn.prepareCall(query)
                    val cursor= cstmt.executeQuery()
                    if (cursor != null){
                        while   (cursor!!.next()) {
                            try {
                                when (functionType) {
                                    1 -> records?.add(Unloadingwavelist(cursor!!.getString("wave_no"),cursor!!.getString("wave_nm"),cursor!!.getInt("pqty"),cursor!!.getInt("oqty")))
                                }
                                recordCount++
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        }
                        message = "Found $recordCount"
                        isDone = true
                    } else {
                        message = "There is No Records"
                        isDone = false
                    }
                    myConn.close()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                    val writer = StringWriter()
                    ex.printStackTrace(PrintWriter(writer))
                    message = writer.toString()
                    isDone = false
            }
            return message
        }

        override  fun onPostExecute(result: String?) {
            prog.dismiss()
            Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
            if (isDone == false) {

            } else {
                try {
                    rv.adapter = adapter
                } catch (ex: Exception) {

                }
            }
        }
    }

   fun getUnloadingwavelist(rv: RecyclerView) {
       this.rv = rv
       query = "{call unloading_wave_list_ksbed_mobile()}"
       records = ArrayList<Unloadingwavelist>() as ArrayList<Any>
       adapter = Unloadingwavelistadapter(records as ArrayList<Unloadingwavelist>)
       functionType = 1
       SyncData2().execute("")
   }

}