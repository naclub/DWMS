package com.example.android.dwms

import android.os.StrictMode
import android.util.Log
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.lang.ClassNotFoundException as ClassNotFoundException

class ConnectionClass {
//   private val ip = "172.30.1.38"  // your Database IP Address + Port
//   private val db = "isbfdb" // your Database Name
//   private val username = "sa" // your server username
//   private val password = "sql@2014"  // your server password

//    금성침대 실서버 연결 테스트 by lapiss 2020.03.20
    private val ip = "121.156.116.190:14233"  // your Database IP Address + Port
    private val db = "k1dwmsdb_test1" // your Database Name  // 금성침대 1공장 Test db by lapiss 2020.04.08
    private val username = "Bizsi" // your server username
    private val password = "Wms209##"  // your server password

//    듀오백 실서버 연결 테스트 by lapiss 2020.03.20
//    private val ip = "220.121.17.231:7080"  // your Database IP Address + Port
//    private val db = "uwmsdb" // your Database Name
//    private val username = "duouwms" // your server username
//    private val password = "duouwms"  // your server password

    fun dbConn() : Connection? {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var conn : Connection? = null
        var connString : String? = null
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            connString = "jdbc:jtds:sqlserver://$ip;databaseName=$db;user=$username;password=$password;"
            conn = DriverManager.getConnection(connString)
        } catch (ex :SQLException) {
            Log.e("Error : ", ex.message)
        } catch (ex1 : ClassNotFoundException) {
            Log.e("Error : ", ex1.message)
        } catch (ex2 : Exception){
            Log.e("Error : ", ex2.message)
        }
        return conn
    }
}