package com.example.android.dwms

import java.io.PrintWriter
import java.io.StringWriter

class ReceiptDBHelper {

    companion object {
        val connectionClass = ConnectionClass()
    }

    private lateinit var warehouse : String
    private lateinit var query : String
    private lateinit var query2 : String
    private lateinit var query3 : String
    private lateinit var query4 : String
    private lateinit var query5 : String
    private lateinit var sysUsr : String
    var result : Int = 0
    var ls_return : String = ""

    fun receipt_process_execute(ll_loc : Int, ls_owner : String, receipt_no :String, receipt_seqno : String, confirm_yn : String, rl_no : String, item_id : String,
                                receipt_type : String, rec_location : String, seq : Int, serl : Int, com_goodcd : String) : String {
        warehouse = App.prefs.g_warehouse.toString()
        sysUsr = App.prefs.g_loginid.toString()
        query =  "update receipt_line set confirm_qty = confirm_qty + 1, confirm_yn = '$confirm_yn' " +
                 "where warehouse_id='$warehouse' and owner_id ='$ls_owner' " +
                 "and receipt_no = '$receipt_no' and receipt_seqno = '$receipt_seqno' "
        try {
            var myConn = connectionClass.dbConn()
            if (myConn == null) {
                ls_return = "Network 접속오류 발생!"
                return ls_return
            } else {
                myConn.setAutoCommit(false)      /* 자동 커밋 모드를 끈다 */
                val statement = myConn!!.createStatement()
                result = statement.executeUpdate(query)
                if (result < 0) {
                    myConn.rollback()
                    myConn.setAutoCommit(true)
                    myConn.close()
                    ls_return = "receipt_line update 처리 중 오류발생!"
                    return ls_return
                } else {
                    query2 = "insert into receipt_line_history values('$warehouse', '$ls_owner', '$receipt_no', '$receipt_seqno', '$rl_no', getdate()," +
                             "'$sysUsr', '$item_id', '$receipt_type', 1, '$rec_location', '', '$seq', '$serl', '$com_goodcd')"
                    try {
                        val statement2 = myConn!!.createStatement()
                        result = statement2.executeUpdate(query2)
                        if (result < 0) {
                            myConn.rollback()
                            myConn.setAutoCommit(true)
                            myConn.close()
                            ls_return = "receipt_line_history insert 처리 중 오류발생!"
                            return ls_return
                        } else {
                            query3 = "insert into receipt_line_history_if values('$warehouse', '$ls_owner', '$receipt_no', '$receipt_seqno', getdate()," +
                                     "'$item_id', '$receipt_type', 1, 'N', null, null, '$seq', '$serl', '$com_goodcd')"
                            try {
                                val statement3 = myConn!!.createStatement()
                                result = statement3.executeUpdate(query3)
                                if (result < 0) {
                                    myConn.rollback()
                                    myConn.setAutoCommit(true)
                                    myConn.close()
                                    ls_return = "receipt_line_history_if insert 처리 중 오류발생!"
                                    return ls_return
                                } else {
                                    if (ll_loc == 0) {
                                        query4 = "insert into item_location_detail values( '$warehouse', '$rec_location', '$ls_owner', '$item_id', '', '$receipt_no', '$receipt_seqno', " +
                                                 "null, 'N', null, 1, 0 )"
                                        try {
                                            val statement4 = myConn!!.createStatement()
                                            result = statement4.executeUpdate(query4)
                                            if (result < 0) {
                                                myConn.rollback()
                                                myConn.setAutoCommit(true)
                                                myConn.close()
                                                ls_return = "item_location_detail insert 처리 중 오류발생!"
                                                return ls_return
                                            } else {
                                            }
                                        } catch (ex: Exception) {
                                            ex.printStackTrace()
                                            val writer = StringWriter()
                                            ex.printStackTrace(PrintWriter(writer))
                                            myConn.rollback()
                                            myConn.setAutoCommit(true)
                                            myConn.close()
                                            ls_return = "item_location_detail insert 처리 중 오류발생!"
                                            return ls_return
                                        }

                                    } else {
                                        query5 = "update item_location_detail " +
                                                "set stock_qty = stock_qty + 1 " +
                                                "where warehouse_id = '$warehouse' and  location_id = '$rec_location' and owner_id =  '$ls_owner' " +
                                                "and item_id = '$item_id' and pallet_id = '' and receipt_no = '$receipt_no' and receipt_seqno = '$receipt_seqno'"
                                        try {
                                            val statement5 = myConn!!.createStatement()
                                            result = statement5.executeUpdate(query5)
                                            if (result < 0) {
                                                myConn.rollback()
                                                myConn.setAutoCommit(true)
                                                myConn.close()
                                                ls_return = "item_location_detail update 처리 중 오류발생!"
                                                return ls_return
                                            } else {
                                            }
                                        } catch (ex: Exception) {
                                            ex.printStackTrace()
                                            val writer = StringWriter()
                                            ex.printStackTrace(PrintWriter(writer))
                                            myConn.rollback()
                                            myConn.setAutoCommit(true)
                                            myConn.close()
                                            ls_return = "item_location_detail update 처리 중 오류발생!"
                                            return ls_return
                                        }
                                    }
                                }
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                val writer = StringWriter()
                                ex.printStackTrace(PrintWriter(writer))
                                myConn.rollback()
                                myConn.setAutoCommit(true)
                                myConn.close()
                                ls_return = "receipt_line_history_if insert 처리 중 오류발생!"
                                return ls_return
                            }

                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        val writer = StringWriter()
                        ex.printStackTrace(PrintWriter(writer))
                        myConn.rollback()
                        myConn.setAutoCommit(true)
                        myConn.close()
                        ls_return = "receipt_line_history insert 처리 중 오류발생!"
                        return ls_return
                    }

                    myConn.commit()
                    myConn.setAutoCommit(true)      /* 자동 커밋 모드를 켠다 */
                    myConn.close()
                    ls_return = "OK"
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            val writer = StringWriter()
            ex.printStackTrace(PrintWriter(writer))
        }
        return ls_return
    }
}
