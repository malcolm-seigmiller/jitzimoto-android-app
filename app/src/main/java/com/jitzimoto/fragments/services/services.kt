package com.jitzimoto.fragments.services

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
import com.jitzimoto.utils.ViewModel
import kotlinx.android.synthetic.main.fragment_services.*
import kotlinx.android.synthetic.main.fragment_services.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class services : Fragment() {

    private lateinit var mUserViewModel: ViewModel
    private val myAdapter by lazy { myServicesAdapter() }

    val gson = GsonBuilder()
            .setLenient()
            .create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_services, container, false)

        view.addService.setOnClickListener {
            findNavController().navigate(R.id.action_services_to_createService)
        }

        dbread()
        return view
    }

    private fun dbread() {
        mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        mUserViewModel.readUserDetails.observe(viewLifecycleOwner, Observer { user ->
            val name = user.name
            getServices(name)
        })

    }

    val BASE_URL = "http://10.0.2.2:80/"

    private fun getServices(name: String) {

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
//                val response = api.postit(test)
            try {
                val response = retrofit.myServicesApi(name)//it wants a JSONarray
                //the above wants some kind of JSON to work we were passing Strings instead
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
                    Toast.makeText(requireContext(), "something went wrong!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun setUpRecyclerView() {
        myServiceRecyclerView.adapter = myAdapter
        myServiceRecyclerView.layoutManager = LinearLayoutManager(this.context)
    }
}