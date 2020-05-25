package com.example.android.dwms

class Customer (val CustomerNo : Int, val CustomerName : String)
class Product (val ProductID : Int, val ProductName : String)

class Code (val cdx_cd : String, val cdx_nm : String)

class Item (val item_id : String, val item_nm : String)

class Unloadingwavelist(val wave_no : String, val wave_nm : String, val pqty : Int, val oqty : Int)

class Search_item(val location_id : String, val owner_id : String, val stock_qty : Int)

class Search_location(val owner_id : String, val item_id : String, val stock_qty : Int, val item_nm : String)

class barcode_info(val barcode : String, val itemseq : Int, val seq : Int, val serl : Int, val no : String)

class Receipt_list(val item_id : String, val item_nm : String, val plan_qty : Int, val confirm_qty : Int)
