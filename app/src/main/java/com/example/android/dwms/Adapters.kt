package com.example.android.dwms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class Customersadapter (val customers : ArrayList<Customer>) : RecyclerView.Adapter<Customersadapter.ViewHolder>()
{
    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtCustomerNo = itemView.findViewById(R.id.txtCustomerNo) as TextView
        val txtCustomerName = itemView.findViewById(R.id.txtCustomerName) as TextView
    }

    override fun onCreateViewHolder(pO: ViewGroup, p1: Int ): Customersadapter.ViewHolder {
        val v = LayoutInflater.from(pO.context).inflate(R.layout.lo_customers,pO,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return customers.size
    }

    override fun onBindViewHolder(pO: Customersadapter.ViewHolder, p1: Int) {
        val customer : Customer = customers[p1]

        pO?.txtCustomerNo.text = customer.CustomerNo.toString()
        pO?.txtCustomerName.text = customer.CustomerName
    }

}

class Productsadapter (val products : ArrayList<Product>) : RecyclerView.Adapter<Productsadapter.ViewHolder>()
{
    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtBarcode = itemView.findViewById(R.id.txtBarcode) as TextView
        val txtProductName = itemView.findViewById(R.id.txtProductName) as TextView
    }

    override fun onCreateViewHolder(pO: ViewGroup, p1: Int): Productsadapter.ViewHolder {
        val v = LayoutInflater.from(pO.context).inflate(R.layout.lo_products,pO,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(pO: Productsadapter.ViewHolder, p1: Int) {
        val products : Product = products[p1]

        pO?.txtBarcode.text = products.ProductID.toString()
        pO?.txtProductName.text = products.ProductName
    }

}

class Codeadapter (val codes : ArrayList<Code>) : RecyclerView.Adapter<Codeadapter.ViewHolder>()
{
    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtcdxcd = itemView.findViewById(R.id.txtcdxcd) as TextView
        val txtcdxnm = itemView.findViewById(R.id.txtcdxnm) as TextView
    }

    override fun onCreateViewHolder(pO: ViewGroup, p1: Int): Codeadapter.ViewHolder {
        val v = LayoutInflater.from(pO.context).inflate(R.layout.lo_code,pO,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return codes.size
    }

    override fun onBindViewHolder(pO: Codeadapter.ViewHolder, p1: Int) {
        val codes : Code = codes[p1]

        pO?.txtcdxcd.text = codes.cdx_cd
        pO?.txtcdxnm.text = codes.cdx_nm
    }

}

class Itemadapter (val items : ArrayList<Item>) : RecyclerView.Adapter<Itemadapter.ViewHolder>()
{
    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtitemid = itemView.findViewById(R.id.txtitemid) as TextView
        val txtitemnm = itemView.findViewById(R.id.txtitemnm) as TextView
    }

    override fun onCreateViewHolder(pO: ViewGroup, p1: Int): Itemadapter.ViewHolder {
        val v = LayoutInflater.from(pO.context).inflate(R.layout.lo_item,pO,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(pO: Itemadapter.ViewHolder, p1: Int) {
        val items : Item = items[p1]

        pO?.txtitemid.text = items.item_id
        pO?.txtitemnm.text = items.item_nm
    }

}

class Unloadingwavelistadapter (val Unloadingwavelists : ArrayList<Unloadingwavelist>) : RecyclerView.Adapter<Unloadingwavelistadapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtwaveno = itemView.findViewById(R.id.txtwaveno) as TextView
        val txtwavenm = itemView.findViewById(R.id.txtwavenm) as TextView
        val txtpqty: TextView = itemView.findViewById(R.id.txtpqty) as TextView
        val txtoqty: TextView = itemView.findViewById(R.id.txtoqty) as TextView
    }

    override fun onCreateViewHolder(pO: ViewGroup, p1: Int): Unloadingwavelistadapter.ViewHolder {
        val v = LayoutInflater.from(pO.context).inflate(R.layout.lo_unloadingwavelist, pO, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return Unloadingwavelists.size
    }

    override fun onBindViewHolder(pO: Unloadingwavelistadapter.ViewHolder, p1: Int) {
        val Unloadingwavelists: Unloadingwavelist = Unloadingwavelists[p1]

        pO?.txtwaveno.text = Unloadingwavelists.wave_no
        pO?.txtwavenm.text = Unloadingwavelists.wave_nm
        pO?.txtpqty.text = Unloadingwavelists.pqty.toString()
        pO?.txtoqty.text = Unloadingwavelists.oqty.toString()
    }
}

class SearchItemadapter (val Search_item : ArrayList<Search_item>) : RecyclerView.Adapter<SearchItemadapter.ViewHolder>() {
    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtloacation_id = itemView.findViewById(R.id.txtlocation_id) as TextView
        val txtowner_id = itemView.findViewById(R.id.txtowner_id) as TextView
        val txtstock_qty : TextView = itemView.findViewById(R.id.txtstock_qty) as TextView
    }

    override fun onCreateViewHolder(pO: ViewGroup, p1: Int): SearchItemadapter.ViewHolder {
        val v = LayoutInflater.from(pO.context).inflate(R.layout.lo_search_item,pO,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return Search_item.size
    }

    override fun onBindViewHolder(pO: SearchItemadapter.ViewHolder, p1: Int) {
        val Search_item : Search_item = Search_item[p1]

        pO?.txtloacation_id.text = Search_item.location_id
        pO?.txtowner_id.text = Search_item.owner_id
        pO?.txtstock_qty.text = makeCommaNumber(Search_item.stock_qty)
    }
}


class SearchLocationadapter (val Search_location : ArrayList<Search_location>) : RecyclerView.Adapter<SearchLocationadapter.ViewHolder>() {
    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtowner_id = itemView.findViewById(R.id.txtowner_id) as TextView
        val txtitem_id = itemView.findViewById(R.id.txtitem_id) as TextView
        val txtitem_nm = itemView.findViewById(R.id.txtitem_nm) as TextView
        val txtstock_qty = itemView.findViewById(R.id.txtstock_qty) as TextView
    }

    override fun onCreateViewHolder(pO: ViewGroup, p1: Int): SearchLocationadapter.ViewHolder {
        val v = LayoutInflater.from(pO.context).inflate(R.layout.lo_search_location,pO,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return Search_location.size
    }

    override fun onBindViewHolder(pO: SearchLocationadapter.ViewHolder, p1: Int) {
        val Search_location : Search_location = Search_location[p1]

        pO?.txtowner_id.text = Search_location.owner_id
        pO?.txtitem_id.text = Search_location.item_id
        pO?.txtitem_nm.text = Search_location.item_nm
        pO?.txtstock_qty.text = makeCommaNumber(Search_location.stock_qty)
    }
}

class Receiptlistadapter (val Receipt_list : ArrayList<Receipt_list>) : RecyclerView.Adapter<Receiptlistadapter.ViewHolder>() {
    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtitem_id = itemView.findViewById(R.id.txtitem_id) as TextView
        val txtitem_nm = itemView.findViewById(R.id.txtitem_nm) as TextView
        val txtplan_qty = itemView.findViewById(R.id.txtplan_qty) as TextView
        val txtconfirm_qty : TextView = itemView.findViewById(R.id.txtconfirm_qty) as TextView
    }

    override fun onCreateViewHolder(pO: ViewGroup, p1: Int): Receiptlistadapter.ViewHolder {
        val v = LayoutInflater.from(pO.context).inflate(R.layout.lo_receipt_list,pO,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return Receipt_list.size
    }

    override fun onBindViewHolder(pO: Receiptlistadapter.ViewHolder, p1: Int) {
        val Receipt_list : Receipt_list = Receipt_list[p1]

        pO?.txtitem_id.text = Receipt_list.item_id
        pO?.txtitem_nm.text = Receipt_list.item_nm
        pO?.txtplan_qty.text = makeCommaNumber(Receipt_list.plan_qty)
        pO?.txtconfirm_qty.text = makeCommaNumber(Receipt_list.confirm_qty)
    }
}


