package com.jitzimoto.fragments.services

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.jitzimoto.R
import com.jitzimoto.dataModels.createServiceModel
import kotlinx.android.synthetic.main.myservicecard.view.*

class myServicesAdapter: RecyclerView.Adapter<myServicesAdapter.MyViewHolder>() {

    private var myList = emptyList<createServiceModel>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val abc = MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.myservicecard, parent, false))
        return abc
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val abc = myList[position]

//        first do the items that the users see's
        holder.itemView.myservice_servicename.text = myList[position].serviceName
        holder.itemView.myservice_price.text = myList[position].price
//        stuff the user doest see until the edit screen
        holder.itemView.myservice_serviceType.text = myList[position].serviceType
        holder.itemView.myservice_mon.text = myList[position].mon
        holder.itemView.myservice_tue.text = myList[position].tue
        holder.itemView.myservice_wed.text = myList[position].wed
        holder.itemView.myservice_wed.text = myList[position].wed
        holder.itemView.myservice_thu.text = myList[position].thu
        holder.itemView.myservice_fri.text = myList[position].fri
        holder.itemView.myservice_sun.text = myList[position].sun
        holder.itemView.myservice_hourStart.text = myList[position].hourStart
        holder.itemView.myservice_minStart.text = myList[position].minStart
        holder.itemView.myservice_hourEnd.text = myList[position].hourEnd
        holder.itemView.myservice_minEnd.text = myList[position].minStart
        holder.itemView.myservice_currency.text = myList[position].currency
        holder.itemView.myservice_name.text = myList[position].serviceProvider

        holder.itemView.myservicecard.setOnClickListener {
            val action = servicesDirections.actionServicesToEditServices(abc)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(newList: List<createServiceModel>){
        myList = newList
        notifyDataSetChanged()
    }
}