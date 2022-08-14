package com.jitzimoto.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.jitzimoto.BuildConfig
import com.jitzimoto.R
import com.jitzimoto.api.API
import com.jitzimoto.fragments.listings.listingAdapter
import com.jitzimoto.utils.ViewModel
import kotlinx.android.synthetic.main.fragment_home1.*
import kotlinx.android.synthetic.main.fragment_home1.view.*
import kotlinx.android.synthetic.main.fragment_listings.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Home1 : Fragment() {
    private val myAdapter by lazy { homeAdapter() }

    val gson = GsonBuilder()
            .setLenient()
            .create()

    val BASE_URL = "http://10.0.2.2:80/"

    private lateinit var mUserViewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home1, container, false)

//        this checks if the user is looged in
        mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        mUserViewModel.checkUserInfo.observe(viewLifecycleOwner, Observer{ user ->
            val email = user.email
            val password = user.password

            val abc = arrayOf(email, password)

            val builder = OkHttpClient().newBuilder()
            builder.readTimeout(120, TimeUnit.SECONDS)
            builder.connectTimeout(5, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(interceptor)
            }

            val client = builder.build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build()
                    .create(API::class.java)

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = retrofit.loginAuthenticate(abc)
                    try {
                        if (response.code() == 200) {
                            println("logged in")
                        } else {
                            findNavController().navigate(R.id.action_home1_to_splash2)
                        }
                    } catch (e: Exception) {
                        println("you messed up the connection some how")
                        println(e)
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        println(e)
                        Toast.makeText(requireContext(), "something went wrong!", Toast.LENGTH_LONG).show()
                    }
                }
            }
            //push comes to shove I can put most of the code in here ... I think
        })
        println("you are in home 1")

        val cunt = resources.getStringArray(R.array.broadtypes)
        val arrayadapters = ArrayAdapter(requireContext(), R.layout.country_dropdown, cunt)
        view.search_auto_complete.setAdapter(arrayadapters)

        view.search_auto_complete.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                // TODO: 2021-05-16 add country to the selector
                val abc = search_auto_complete.text.toString()
                val action = Home1Directions.actionHome1ToListings(abc)
                Navigation.findNavController(view).navigate(action)
//                findNavController().navigate(R.id.action_home1_to_listings)
            }
            false
        }

        view.bookings_btn.setOnClickListener {
            findNavController().navigate(R.id.action_home1_to_mybookings)
        }

        view.services_btn.setOnClickListener {
            findNavController().navigate(R.id.action_home1_to_services)
        }

        view.profile_btn.setOnClickListener {
            findNavController().navigate(R.id.action_home1_to_profile)
        }

//        do recyclerview stuff
        mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        mUserViewModel.readUserDetails.observe(viewLifecycleOwner, Observer{ user ->
            val selector = user.name

            val builder = OkHttpClient().newBuilder()
            builder.readTimeout(120, TimeUnit.SECONDS)
            builder.connectTimeout(5, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(interceptor)
            }

            val client = builder.build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build()
                    .create(API::class.java)

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = retrofit.myBookingsApi(selector)
                    try {
                        if (response.code() == 200) {
                            Log.i("RESPONSE", "RAW: " + response)
                            for (Mlist in response.body()?.bookingModel!!) {
                                Log.d("MainActivity", "Result + $Mlist")
                                response.body()?.bookingModel!!.let { myAdapter.setData(it) }
                            }
                            withContext(Dispatchers.Main) {
                                setUpRecyclerView()
                            }

                        } else {
                            findNavController().navigate(R.id.action_home1_to_splash2)
                        }
                    } catch (e: Exception) {
                        println("you messed up the connection some how")
                        println(e)
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        println(e)
                        Toast.makeText(requireContext(), "something went wrong!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
        return view
    }

    private fun setUpRecyclerView() {
        home1_recyclerView.adapter = myAdapter
        home1_recyclerView.layoutManager = LinearLayoutManager(this.context)
    }
}