package com.example.android.dwms

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import androidx.recyclerview.widget.RecyclerView
import java.io.PrintWriter
import java.io.StringWriter

class DatabaseHelper {

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

    inner class SyncData : AsyncTask<String, String, String>(){
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
                    val statement = myConn!!.createStatement()
                    val cursor = statement.executeQuery(query)
                    if (cursor != null) {
                        while (cursor!!.next()) {
                            try {
                                when (functionType) {
                                    1 -> records?.add(Customer(cursor!!.getInt("CustomerNo"),cursor!!.getString("CustomerName")))
                                    2 -> records?.add(Product(cursor!!.getInt("ProductID"),cursor!!.getString("ProductName")))
                                    3 -> records?.add(Code(cursor!!.getString("cdx_cd"),cursor!!.getString("cdx_nm")))
                                    4 -> records?.add(Item(cursor!!.getString("item_id"),cursor!!.getString("item_nm")))
                                    5 -> records?.add(Search_item(cursor!!.getString("location_id"),cursor!!.getString("owner_id"),cursor!!.getInt("stock_qty")))
                                    6 -> records?.add(Search_location(cursor!!.getString("owner_id"),cursor!!.getString("item_id"),cursor!!.getInt("stock_qty"),cursor!!.getString("item_nm")))
                                    7 -> records?.add(Receipt_list(cursor!!.getString("item_id"),cursor!!.getString("item_nm"),cursor!!.getInt("plan_qty"),cursor!!.getInt("confirm_qty")))
                                   /* b.item_id, b.item_nm, a.plan_qty, a.confirm_qty*/
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
 //           Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
            if (isDone == false) {

            } else {
                try {
                    rv.adapter = adapter
                } catch (ex: Exception) {

                }
            }
        }
    }
    fun getCustomers(rv : RecyclerView) {
        this.rv = rv
        query = "Select CustomerNo, CustomerName from Customers"
        records = ArrayList<Customer>() as ArrayList<Any>
        adapter = Customersadapter(records as ArrayList<Customer>)
        functionType = 1
        SyncData().execute("")
    }

    fun getProducts(rv : RecyclerView) {
        this.rv = rv
        query = "Select ProductID, ProductName from Products"
        records = ArrayList<Product>() as ArrayList<Any>
        adapter = Productsadapter(records as ArrayList<Product>)
        functionType = 2
        SyncData().execute("")
    }

    fun getCode(rv : RecyclerView) {
        this.rv = rv
        query = "Select cdx_cd, cdx_nm from code"
        records = ArrayList<Code>() as ArrayList<Any>
        adapter = Codeadapter(records as ArrayList<Code>)
        functionType = 3
        SyncData().execute("")
    }

    fun getItem(rv : RecyclerView) {
        this.rv = rv
        query = "Select item_id, item_nm from item"
        records = ArrayList<Item>() as ArrayList<Any>
        adapter = Itemadapter(records as ArrayList<Item>)
        functionType = 4
        SyncData().execute("")
    }

    fun getSearchItem(rv : RecyclerView, warehouse : String, owner : String, item_id : String ) {
        this.rv = rv
        var warehouse = "K01"
        var owner = "KSBED"

        query = "Select location_id, owner_id, stock_qty FROM item_location " +
                "where warehouse_id = '$warehouse' " +
                "and owner_id LIKE '$owner' " +
                "and item_id = '$item_id' " +
                "order by location_id"
        records = ArrayList<Search_item>() as ArrayList<Any>
        adapter = SearchItemadapter(records as ArrayList<Search_item>)
        //SearchItemadapter는 조회된 데이터를 Adapter로 보냄,ArrayList<Search_item> 의 Search_item은 data type정의하는 Models.kt파일에 등록되어 있음

        functionType = 5
        SyncData().execute("")
    }

    fun getSearchLocation(rv : RecyclerView, warehouse : String, owner : String, location_id : String ) {
        this.rv = rv
        var warehouse = "K01"
        var owner = "KSBED"
        query = "select a.owner_id, a.item_id, a.stock_qty, " +
                "(select item_nm from item where item_id = a.item_id) as item_nm FROM item_location a " +
                "where a.warehouse_id = '$warehouse' " +
                "and a.owner_id LIKE '$owner' " +
                "and a.location_id = '$location_id' " +
                "order by a.item_id"
        records = ArrayList<Search_location>() as ArrayList<Any>
        adapter = SearchLocationadapter(records as ArrayList<Search_location>)
        functionType = 6
        SyncData().execute("")
    }

    fun getReceiptlist(rv : RecyclerView, warehouse : String, owner : String, item_id : String, receipt_no : String ) {
        this.rv = rv
        var warehouse = "K01"
        var owner = "KSBED"
        query = "select b.item_id, b.item_nm, a.plan_qty, a.confirm_qty " +
                "from receipt_line a, item b " +
                "where a.warehouse_id = '$warehouse' " +
                "and a.owner_id like '$owner' " +
                "and a.item_id = '$item_id' " +
                "and a.receipt_no = '$receipt_no' " +
                "and b.owner_id = a.owner_id " +
                "and b.item_id = a.item_id"
        records = ArrayList<Receipt_list>() as ArrayList<Any>
        adapter = Receiptlistadapter(records as ArrayList<Receipt_list>)
        functionType = 7
        SyncData().execute("")
    }

}