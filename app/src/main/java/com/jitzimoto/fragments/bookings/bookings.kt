package com.jitzimoto.fragments.bookings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.jitzimoto.R
import kotlinx.android.synthetic.main.fragment_bookings.view.*

class bookings : Fragment() {

    private val args by navArgs<bookingsArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bookings, container, false)

        val booker = args.bookingparams.booker
        val serviceProvider = args.bookingparams.serviceProvider
        val serviceName = args.bookingparams.serviceName
        val location = args.bookingparams.location
        val comments = args.bookingparams.comments
        val price = args.bookingparams.price

        val day = args.bookingparams.day
        val month = args.bookingparams.month
        val year = args.bookingparams.year
        val hour = args.bookingparams.hour
        val minute = args.bookingparams.minute

//        Todo: make it so that the month will change to the english version

        view.bookedtimetext.text = "time booked : $hour:$minute"
        view.bookingdatetextview.text = "Date booked : $month:$day"

        view.servicebooked.text = serviceName
        view.booker_booker.text = booker
        view.editTextTextMultiLine_booker.text = comments
        view.textView_price.text = price
        view.location_textview.text = location



        return view
    }
}