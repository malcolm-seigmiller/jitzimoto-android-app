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
import com.google.gson.GsonBuilder
import com.jitzimoto.BuildConfig
import com.jitzimoto.R
import com.jitzimoto.api.API
import com.jitzimoto.utils.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class bizListings : Fragment() {
    private lateinit var mUserViewModel: ViewModel
    private val args by navArgs<bizListingsArgs>()

    private val myAdapter by lazy { bizListingsAdapter() }

    val gson = GsonBuilder()
        .setLenient()
        .create()

    val BASE_URL = "http://10.0.2.2:80/"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_biz_listings, container, false)
        mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        mUserViewModel.userRowCount.observe(viewLifecycleOwner, { user ->
            if (user >= 1){
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
                            val response = retrofit.bizListingSearch(abc)
                            try {
                                Log.i("RESPONSE", "RAW: " + response)
//                                for (Mlist in response.mybizlistingModel) {
//                                    Log.d("MainActivity", "Result + $Mlist")
//                                    response.mybizlistingModel.let { myAdapter.setData(it) }
//                                }
                                withContext(Dispatchers.Main) {
//                                    setUpRecyclerView()
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
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            val response = retrofit.listingSearch(abc)
                            try {

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

            }
        })
        return view
    }

}