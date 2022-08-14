package com.jitzimoto.fragments.bookings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.jitzimoto.BuildConfig
import com.jitzimoto.R
import com.jitzimoto.api.API
import com.jitzimoto.fragments.home.homeAdapter
import com.jitzimoto.utils.ViewModel
import kotlinx.android.synthetic.main.fragment_home1.*
import kotlinx.android.synthetic.main.fragment_home1.home1_recyclerView
import kotlinx.android.synthetic.main.fragment_mybookings.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class mybookings : Fragment() {

    private val myAdapter by lazy { mybookingsAdapter() }

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
        val view = inflater.inflate(R.layout.fragment_mybookings, container, false)

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
                    val response = retrofit.personalBookingsApi(selector)
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
        my_bookings_recycler_view.adapter = myAdapter
        my_bookings_recycler_view.layoutManager = LinearLayoutManager(this.context)
    }
}