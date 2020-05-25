package com.example.android.dwms

import java.io.PrintWriter
import java.io.StringWriter

class DddwDbHelper {

    companion object {
        val connectionClass = ConnectionClass()
    }

    private lateinit var warehouse : String
    private lateinit var query : String
    private lateinit var records : ArrayList<String>

    fun getreclocation() : ArrayList<String> {
        warehouse = App.prefs.g_warehouse.toString()
        query = "select location_id from location " +
                "where warehouse_id = '$warehouse' " +
                "and location_used ='RECEIPT' " +
                "order by location_id"
        records = ArrayList<String>()
        records.clear()
        var recordCount : Int = 0
        try {
            var myConn = connectionClass.dbConn()
            if (myConn == null) {
            } else {
                val statement = myConn!!.createStatement()
                val cursor = statement.executeQuery(query)
                if (cursor != null) {
                    while (cursor!!.next()) {
                        records?.add(cursor!!.getString("location_id"))
                        recordCount++
                    }
                } else {
                }
                myConn.close()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            val writer = StringWriter()
            ex.printStackTrace(PrintWriter(writer))
        }
        return return records
    }

    fun getretlocation() : ArrayList<String>{
        warehouse = App.prefs.g_warehouse.toString()
        query = "select location_id from location " +
                "where warehouse_id = '$warehouse' " +
                "and location_used ='RETURN' " +
                "order by location_id"
        records = ArrayList<String>()
        records.clear()
        var recordCount : Int = 0

        try {
            var myConn = connectionClass?.dbConn()
            if (myConn == null) {
            } else {
                val statement = myConn!!.createStatement()
                val cursor = statement.executeQuery(query)
                if (cursor != null) {
                    while (cursor!!.next()) {
                        records?.add(cursor!!.getString("location_id"))
                        recordCount++
                    }
                } else {
                }
                myConn.close()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            val writer = StringWriter()
            ex.printStackTrace(PrintWriter(writer))
        }
        return return records
    }

    fun getpickdivision() : ArrayList<String>{
        warehouse = App.prefs.g_warehouse.toString()
        query = "select division_id from warehouse_division " +
                "where warehouse_id = '$warehouse' "

        var records = ArrayList<String>()
        records.clear()
        var recordCount : Int = 0

        try {
            var myConn = connectionClass?.dbConn()
            if (myConn == null) {
            } else {
                val statement = myConn!!.createStatement()
                val cursor = statement.executeQuery(query)
                if (cursor != null) {
                    while (cursor!!.next()) {
                        records?.add(cursor!!.getString("division_id"))
                        recordCount++
                    }
                } else {
                }
                myConn.close()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            val writer = StringWriter()
            ex.printStackTrace(PrintWriter(writer))
        }
        return return records
    }

}