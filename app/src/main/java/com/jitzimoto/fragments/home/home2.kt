package com.jitzimoto.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.GsonBuilder
import com.jitzimoto.R
import com.jitzimoto.utils.ViewModel

class home2 : Fragment() {

    private lateinit var mUserViewModel: ViewModel

    val gson = GsonBuilder()
            .setLenient()
            .create()

    val BASE_URL = "http://10.0.2.2:80/"

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home2, container, false)

        //get fun

        return view
    }

}