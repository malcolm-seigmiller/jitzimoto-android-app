package com.jitzimoto.fragments.listings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.jitzimoto.R
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.jitzimoto.BuildConfig
import com.jitzimoto.api.API
import com.jitzimoto.fragments.services.myServicesAdapter
import com.jitzimoto.utils.ViewModel
import kotlinx.android.synthetic.main.fragment_listings.*
import kotlinx.android.synthetic.main.fragment_services.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class listings : Fragment(){
    private val args by navArgs<listingsArgs>()
    private val myAdapter by lazy { listingAdapter() }

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
        val view = inflater.inflate(R.layout.fragment_listings, container, false)

        mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        mUserViewModel.userRowCount.observe(viewLifecycleOwner, { user ->
            if (user >= 1){
                //this counts the if there is a user
                mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
                mUserViewModel.readUserDetails.observe(viewLifecycleOwner, { user ->
                    val selector = args.searchparam
                    val cunt = user.city

                    val abc = arrayOf(selector, cunt)

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
                            val response = retrofit.listingSearch(abc)
                            try {
                                Log.i("RESPONSE", "RAW: " + response)
                                for (Mlist in response.serviceModel) {
                                    Log.d("MainActivity", "Result + $Mlist")
                                    response.serviceModel.let { myAdapter.setData(it) }
                                }
                                withContext(Dispatchers.Main) {
                                    setUpRecyclerView()
                                }

                            } catch (e: Exception) {
                                println("you messed up the connection some how")
                            }

                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                println(e)
                                Toast.makeText(requireContext(), "something went wrong!", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                })
            }else{
//                consumer listings
                mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
                mUserViewModel.consumerRowCount.observe(viewLifecycleOwner, { user ->
                    if (user >= 1){//this counts the if there is a consumer
                        mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
                        mUserViewModel.readConsumerData.observe(viewLifecycleOwner, { user ->
                            val selector = args.searchparam
                            val cunt = user.region
                            val abc = arrayOf(selector, cunt)

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
                                    val response = retrofit.listingSearch(abc)
                                    try {
                                        Log.i("RESPONSE", "RAW: " + response)
                                        for (Mlist in response.serviceModel) {
                                            Log.d("MainActivity", "Result + $Mlist")
                                            response.serviceModel.let { myAdapter.setData(it) }
                                        }
                                        withContext(Dispatchers.Main) {
                                            setUpRecyclerView()
                                        }
                                    } catch (e: Exception) {
                                        println("you messed up the connection some how")
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        println(e)
                                        Toast.makeText(requireContext(), "something went wrong!", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        })
                    }else{
                        println("lol wut")
                    }
                })
            }
        })
        return view
    }

    private fun setUpRecyclerView() {
        listing_recyclerview.adapter = myAdapter
        listing_recyclerview.layoutManager = LinearLayoutManager(this.context)
    }
}