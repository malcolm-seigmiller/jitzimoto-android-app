package com.jitzimoto.fragments.services

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.GsonBuilder
import com.jitzimoto.BuildConfig
import com.jitzimoto.R
import com.jitzimoto.api.API
import com.jitzimoto.utils.ViewModel
import kotlinx.android.synthetic.main.fragment_create_service.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class createService : Fragment() {

    private lateinit var mUserViewModel: ViewModel

    val gson = GsonBuilder()
            .setLenient()
            .create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_service, container, false)

        val cunt = resources.getStringArray(R.array.broadtypes)
        val arrayadapters = ArrayAdapter(requireContext(), R.layout.country_dropdown, cunt)
        view.service_auto_complete_textView.setAdapter(arrayadapters)


        view.createService_btn.setOnClickListener {
            val serviceName = view.servicename.text.toString()
            val serviceType = view.service_auto_complete_textView.text.toString()
            val serviceDescription = view.service_desc.text.toString()

            val mon = if(view.MON_switch.isChecked) 1 else 0
            val tue = if(view.TUE_switch.isChecked) 1 else 0
            val wed = if(view.WED_switch.isChecked) 1 else 0
            val thu = if(view.THU_switch.isChecked) 1 else 0
            val fri = if(view.FRI_switch.isChecked) 1 else 0
            val sat = if(view.SAT_switch.isChecked) 1 else 0
            val sun = if(view.SUN_switch.isChecked) 1 else 0

            val hourStart = view.timePicker1.hour
            val minStart = view.timePicker1.minute
            val hourEnd = view.timePicker2.hour
            val minEnd = view.timePicker2.minute

            val price = view.priceedittext.text.toString()

//            println(price)
//            if(hour > 12){
//                val hour = hour - 12
//                println(hour)
//            }else{
//                println(hour)
//            }
//            println(hourStart)
//            println(minStart)
            if (serviceName.isNotEmpty()){
                if (serviceDescription.isNotEmpty()){
                    if (serviceType.isNotEmpty()){
                        if (price.isNotEmpty()){
                            mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
                            mUserViewModel.readUserDetails.observe(viewLifecycleOwner, Observer{ user ->
                                val currency = user.country
                                val serviceProvider = user.name
                                val city = user.city
//                                val creatservice = createServiceModel(serviceName,serviceType,serviceDescription,mon.toString(),tue.toString(),wed.toString(),thu.toString(),fri.toString(),sat.toString(),sun.toString(),hourStart.toString(),minStart.toString(),hourEnd.toString(),minEnd.toString(),price,currency,name)
//                                val creatservice = createServiceModel(serviceName,serviceType,serviceDescription,mon,tue,wed,thu,fri,sat,sun,hourStart,minStart,hourEnd,minEnd,price,currency,name)
                                val creatservice = arrayOf(serviceProvider,serviceName,serviceType,serviceDescription,mon.toString(),tue.toString(),wed.toString(),thu.toString(),fri.toString(),sat.toString(),sun.toString(),hourStart.toString(),minStart.toString(),hourEnd.toString(),minEnd.toString(),price,currency,city.toString())
                                createservicefun(creatservice)
//                                println(creatservice)
                            })
                        }else{
                            println("price is empty")
                        }
                    }else{
                        println("serviceType is empty")
                    }
                }else{
                    println("serviceDescription is empty")
                }
            }else{
                println("service name is empty")
            }
        }
        return view
    }
    val BASE_URL = "http://10.0.2.2:80/"

    private fun createservicefun(creatservice: Array<String>) {
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
                val response = retrofit.createServiceApi(creatservice)
                try {
                    if (response.code() == 200) {
                        findNavController().navigate(R.id.action_createService_to_services)
                    } else {
                        println("service creation failure")
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
    }
}