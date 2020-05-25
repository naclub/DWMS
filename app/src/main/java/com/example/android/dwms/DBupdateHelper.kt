package com.example.android.dwms

import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

class DBupdateHelper {

    companion object {
        val connectionClass = ConnectionClass()
    }

    private lateinit var warehouse : String
    private lateinit var ls_rfno : String
    private lateinit var query : String
    private lateinit var sys_usr : String
    private var result = 0

    fun update_rf_config_info(ls_owner : String, ls_recloc :String, ls_retloc : String, ls_divi : String, li_shplabel : Int, li_trklabel : Int) : Int {
        warehouse = App.prefs.g_warehouse.toString()
        ls_rfno = App.prefs.g_rfid.toString()
        query = "update rf_$warehouse"+"_"+"$ls_rfno " +
                "set owner_id = '$ls_owner', rec_location = '$ls_recloc', ret_location = '$ls_retloc', " +
                "division_id = '$ls_divi', ship_label_number = $li_shplabel, truck_label_number = $li_trklabel " +
                "where warehouse_id='$warehouse' and rf_id ='$ls_rfno' "
        try {
            var myConn = connectionClass.dbConn()
            if (myConn == null) {
            } else {
                val statement = myConn!!.createStatement()
                result = statement.executeUpdate(query)
                if (result < 0) {
                } else {

                }
                myConn.close()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            val writer = StringWriter()
            ex.printStackTrace(PrintWriter(writer))
        }
        return result
    }

    fun update_rf_window(ls_window : String) : Int {
        warehouse = App.prefs.g_warehouse.toString()
        ls_rfno = App.prefs.g_rfid.toString()
        query = "update rf_$warehouse"+"_"+"$ls_rfno " +
                "set window_id = '$ls_window' " +
                "where warehouse_id='$warehouse' and rf_id ='$ls_rfno' "
        try {
            var myConn = connectionClass.dbConn()
            if (myConn == null) {
            } else {
                val statement = myConn!!.createStatement()
                result = statement.executeUpdate(query)
                if (result < 0) {
                } else {

                }
                myConn.close()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            val writer = StringWriter()
            ex.printStackTrace(PrintWriter(writer))
        }
        return result
    }

    fun update_receipt_line(ls_owner : String, receipt_no :String, receipt_seqno : String, confirm_yn : String) : Int {
        warehouse = App.prefs.g_warehouse.toString()

        query = "update receipt_line set confirm_qty = confirm_qty + 1, confirm_yn = '$confirm_yn' " +
                "where warehouse_id='$warehouse' and owner_id ='$ls_owner' " +
                "and receipt_no = '$receipt_no' and receipt_seqno = '$receipt_seqno' "
        try {
            var myConn = connectionClass.dbConn()
            if (myConn == null) {
            } else {
                val statement = myConn!!.createStatement()
                result = statement.executeUpdate(query)
                if (result < 0) {
                } else {

                }
                myConn.close()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            val writer = StringWriter()
            ex.printStackTrace(PrintWriter(writer))
        }
        return result
    }

    fun insert_receipt_line_history(ls_owner : String, receipt_no :String, receipt_seqno : String, rl_no : String, item_id : String,
                                    receipt_type : String, rec_location : String, seq : Int, serl : Int, com_goodcd : String) : Int {

        warehouse = App.prefs.g_warehouse.toString()
        sys_usr = App.prefs.g_loginid.toString()
        query = "insert into receipt_line_history values( '$warehouse', '$ls_owner', '$receipt_no', '$receipt_seqno', $rl_no', getdate()," +
                "'$sys_usr', '$item_id', '$receipt_type', 1, '$rec_location', '', '$seq', '$serl', '$com_goodcd') "
        try {
            var myConn = connectionClass.dbConn()
            if (myConn == null) {
            } else {
                val statement = myConn!!.createStatement()
                result = statement.executeUpdate(query)
                if (result < 0) {
                } else {

                }
                myConn.close()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            val writer = StringWriter()
            ex.printStackTrace(PrintWriter(writer))
        }
        return result
    }

/*    insert into receipt_line_history
    values(:ls_wh,:owner_id,:receipt_no,:receipt_seqno,:rl_no,:sys_dt,:sys_usr,
    :item_id,:receipt_type,1,:rec_location,'',:ll_workseq,:ll_workserl,:ls_com_goodcd)
    using sqlca;
    if sqlca.sqlcode <> 0 then
    sle_err.text = sqlca.sqlerrtext
    rollback using sqlca;
    setpointer(Arrow!)
    return -1
    end if
    insert into receipt_line_history_if
    values(:ls_wh,:owner_id,:receipt_no,:receipt_seqno,:sys_dt,
    :item_id,:receipt_type,1,'N',null,null, :ll_workseq,:ll_workserl,:ls_com_goodcd)
    using sqlca;*/

    fun insert_receipt_line_history_if(ls_owner : String, receipt_no :String, receipt_seqno : String, item_id : String,
                                    receipt_type : String, rec_location : String, seq : Int, serl : Int, com_goodcd : String) : Int {

        warehouse = App.prefs.g_warehouse.toString()
        sys_usr = App.prefs.g_loginid.toString()
        query = "insert into receipt_line_history_if values( '$warehouse', '$ls_owner', '$receipt_no', '$receipt_seqno', getdate()," +
                "'$sys_usr', '$item_id', '$receipt_type', 1, null, null, '$seq', '$serl', '$com_goodcd') "
        try {
            var myConn = connectionClass.dbConn()
            if (myConn == null) {
            } else {
                val statement = myConn!!.createStatement()
                result = statement.executeUpdate(query)
                if (result < 0) {
                } else {

                }
                myConn.close()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            val writer = StringWriter()
            ex.printStackTrace(PrintWriter(writer))
        }
        return result
    }

    fun insert_item_location_detail(ls_owner : String, receipt_no :String, receipt_seqno : String, item_id : String, rec_location : String) : Int {
        warehouse = App.prefs.g_warehouse.toString()
        query = "insert into item_location_detail values( '$warehouse', '$rec_location', '$ls_owner', '$item_id', '', '$receipt_no', '$receipt_seqno', " +
                "null, 'N', null, 1, 0 )"
        try {
            var myConn = connectionClass.dbConn()
            if (myConn == null) {
            } else {
                val statement = myConn!!.createStatement()
                result = statement.executeUpdate(query)
                if (result < 0) {
                } else {

                }
                myConn.close()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            val writer = StringWriter()
            ex.printStackTrace(PrintWriter(writer))
        }
        return result
    }

    fun update_item_location_detail(ls_owner : String, receipt_no :String, receipt_seqno : String, item_id : String, rec_location : String) : Int {
        warehouse = App.prefs.g_warehouse.toString()
        query = "update item_location_detail " +
                "set stock_qty = stock_qty + 1 " +
                "where warehouse_id = '$warehouse' and  location_id = '$rec_location' and owner_id =  '$ls_owner' " +
                "and item_id = '$item_id' and pallet_id = '' and receipt_no = '$receipt_no' and receipt_seqno = '$receipt_seqno' "
        try {
            var myConn = connectionClass.dbConn()
            if (myConn == null) {
            } else {
                val statement = myConn!!.createStatement()
                result = statement.executeUpdate(query)
                if (result < 0) {
                } else {

                }
                myConn.close()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            val writer = StringWriter()
            ex.printStackTrace(PrintWriter(writer))
        }
        return result
    }

}

/*
update item_location_detail set stock_qty = stock_qty + 1
where warehouse_id = :ls_wh
and	location_id = :rec_location
and	owner_id = :owner_id
and	item_id = :item_id
and	pallet_id = ''
and	receipt_no = :receipt_no
and	receipt_seqno = :receipt_seqno
using sqlca;
if sqlca.sqlcode <> 0 then
sle_err.text = sqlca.sqlerrtext
rollback using sqlca;
setpointer(Arrow!)
return -1
end if
//--------------------------------------------------------
if loc_cnt = 0 then
insert into item_location_detail
values(:ls_wh,:rec_location,:owner_id,:item_id,'',
:receipt_no,:receipt_seqno,:lot_no,'N',null,1,0)
using sqlca;
if sqlca.sqlcode <> 0 then
sle_err.text = sqlca.sqlerrtext
rollback using sqlca;
setpointer(Arrow!)
return -1
end if
else
end if

insert into receipt_line_history
values(:ls_wh,:owner_id,:receipt_no,:receipt_seqno,:rl_no,:sys_dt,:sys_usr,
:item_id,:receipt_type,1,:rec_location,'',:ll_workseq,:ll_workserl,:ls_com_goodcd)
using sqlca;
if sqlca.sqlcode <> 0 then
sle_err.text = sqlca.sqlerrtext
rollback using sqlca;
setpointer(Arrow!)
return -1
end if
insert into receipt_line_history_if
values(:ls_wh,:owner_id,:receipt_no,:receipt_seqno,:sys_dt,
:item_id,:receipt_type,1,'N',null,null, :ll_workseq,:ll_workserl,:ls_com_goodcd)
using sqlca;
if sqlca.sqlcode <> 0 then
sle_err.text = sqlca.sqlerrtext
rollback using sqlca;
setpointer(Arrow!)
return -1
end if

*/
