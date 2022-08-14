package com.jitzimoto.fragments.bookings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.jitzimoto.R
import com.jitzimoto.dataModels.bookingModel
import com.jitzimoto.fragments.home.Home1Directions
import kotlinx.android.synthetic.main.bookingcard.view.*
import kotlinx.android.synthetic.main.mybookingcard.view.*

class mybookingsAdapter: RecyclerView.Adapter<mybookingsAdapter.MyViewHolder>(){

    private var myList = emptyList<bookingModel>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val abc = MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.mybookingcard, parent, false))
        return abc
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val abc = myList[position]

        holder.itemView.mybookings_booker.text = myList[position].booker
        holder.itemView.mybooking_service_provider.text = myList[position].serviceProvider
        holder.itemView.booking_myservice_servicename.text = myList[position].serviceName
        holder.itemView.mybooking_location.text = myList[position].location
        holder.itemView.mybookings_comment.text = myList[position].comments
        holder.itemView.my_booking_price.text = myList[position].price

        holder.itemView.mybooking_timedate.text = "ON: ${myList[position].month} : ${myList[position].day}  AT ${myList[position].hour}:${myList[position].minute}"
        holder.itemView.mybookings_year.text = myList[position].year

        holder.itemView.mybookingcard.setOnClickListener {
            val action = mybookingsDirections.actionMybookingsToEditbookings(abc)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(newList: List<bookingModel>) {
        myList = newList
        notifyDataSetChanged()
    }
}