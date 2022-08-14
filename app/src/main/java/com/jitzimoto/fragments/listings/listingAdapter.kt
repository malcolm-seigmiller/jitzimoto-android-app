package com.jitzimoto.fragments.listings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.jitzimoto.R
import com.jitzimoto.dataModels.createServiceModel
import kotlinx.android.synthetic.main.listingcard.view.*

class listingAdapter: RecyclerView.Adapter<listingAdapter.MyViewHolder>() {
    private var myList = emptyList<createServiceModel>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listingAdapter.MyViewHolder {
        val abc = MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.listingcard, parent, false))
        return abc
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: listingAdapter.MyViewHolder, position: Int) {
        val abc = myList[position]

        holder.itemView.listing_myservice_servicename.text = myList[position].serviceName
        holder.itemView.listing_biz.text = myList[position].serviceProvider
        holder.itemView.listing_myservice_price.text = myList[position].price
        holder.itemView.listing_myservice_serviceDescription.text =myList[position].serviceDescription
        holder.itemView.listing_myservice_currency.text = myList[position].serviceDescription
//        holder.itemView.listing_myservice_location.text = myList[position].city

        holder.itemView.listingcard.setOnClickListener {
            val action = listingsDirections.actionListingsToBookService(abc)
            holder.itemView.findNavController().navigate(action)
        }

        if (myList[position].mon == "1"){
            println("mon = one")
        }else{
            holder.itemView.listing_mon.text = ""
        }

        if (myList[position].tue == "1"){
            println("mon = one")
        }else{
            holder.itemView.listing_tue.text = ""
        }

        if (myList[position].wed == "1"){
            println("mon = one")
        }else{
            holder.itemView.listing_wed.text = ""
        }

        if (myList[position].thu == "1"){
            println("mon = one")
        }else{
            holder.itemView.listing_thr.text = ""
        }
        if (myList[position].fri == "1"){
            println("mon = one")
        }else{
            holder.itemView.listing_fri.text = ""
        }

        if (myList[position].sat == "1"){
            println("mon = one")
        }else{
            holder.itemView.listing_sat.text = ""
        }

        if (myList[position].sun == "1"){
            println("mon = one")
        }else{
            holder.itemView.listing_sun.text = ""
        }

    }
    fun setData(newList: List<createServiceModel>){
        myList = newList
        notifyDataSetChanged()
    }
}