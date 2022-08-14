package com.jitzimoto.fragments.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.jitzimoto.R
import com.jitzimoto.dataModels.bookingModel
import kotlinx.android.synthetic.main.bookingcard.view.*
//import com.jitzimoto.dataModels.createServiceModel
import kotlinx.android.synthetic.main.listingcard.view.*

class homeAdapter: RecyclerView.Adapter<homeAdapter.MyViewHolder>() {

    private var myList = emptyList<bookingModel>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): homeAdapter.MyViewHolder {
        val abc = MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bookingcard, parent, false))
        return abc
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: homeAdapter.MyViewHolder, position: Int) {
        val abc = myList[position]

        holder.itemView.bookername.text = myList[position].booker
        holder.itemView.booking_service_provider.text = myList[position].serviceProvider
        holder.itemView.bookingservicename.text = myList[position].serviceName
        holder.itemView.location.text = myList[position].location
        holder.itemView.bookingcomment.text = myList[position].comments
        holder.itemView.bookingprice.text = myList[position].price
        holder.itemView.day.text = myList[position].day
        holder.itemView.month.text = myList[position].month
        holder.itemView.year.text = myList[position].year
        holder.itemView.hour.text = myList[position].hour
        holder.itemView.minute.text = myList[position].minute

        holder.itemView.bookingtime.text = "${myList[position].hour}:${myList[position].minute}"

        holder.itemView.bookingcard.setOnClickListener {
            val action = Home1Directions.actionHome1ToBookings(abc)
            holder.itemView.findNavController().navigate(action)
        }

    }

    fun setData(newList: List<bookingModel>){
        myList = newList
        notifyDataSetChanged()
    }

}