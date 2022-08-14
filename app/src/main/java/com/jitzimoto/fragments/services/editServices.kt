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
import androidx.navigation.fragment.navArgs
import com.google.gson.GsonBuilder
import com.jitzimoto.BuildConfig
import com.jitzimoto.R
import com.jitzimoto.api.API
import com.jitzimoto.fragments.listings.listingsArgs
import com.jitzimoto.utils.ViewModel
import kotlinx.android.synthetic.main.fragment_create_service.view.*
import kotlinx.android.synthetic.main.fragment_create_service.view.FRI_switch
import kotlinx.android.synthetic.main.fragment_create_service.view.MON_switch
import kotlinx.android.synthetic.main.fragment_create_service.view.SAT_switch
import kotlinx.android.synthetic.main.fragment_create_service.view.SUN_switch
import kotlinx.android.synthetic.main.fragment_create_service.view.THU_switch
import kotlinx.android.synthetic.main.fragment_create_service.view.TUE_switch
import kotlinx.android.synthetic.main.fragment_create_service.view.WED_switch
import kotlinx.android.synthetic.main.fragment_create_service.view.createService_btn
import kotlinx.android.synthetic.main.fragment_create_service.view.priceedittext
import kotlinx.android.synthetic.main.fragment_create_service.view.service_auto_complete_textView
import kotlinx.android.synthetic.main.fragment_create_service.view.service_desc
import kotlinx.android.synthetic.main.fragment_create_service.view.servicename
import kotlinx.android.synthetic.main.fragment_create_service.view.timePicker1
import kotlinx.android.synthetic.main.fragment_create_service.view.timePicker2
import kotlinx.android.synthetic.main.fragment_edit_services.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class editServices : Fragment() {
    private val args by navArgs<editServicesArgs>()

    private lateinit var mUserViewModel: ViewModel

    val gson = GsonBuilder()
        .setLenient()
        .create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_services, container, false)
        view.edit_servicename.setText(args.myservice.serviceName)
        view.edit_service_auto_complete_textView.setText(args.myservice.serviceType)
        view.edit_service_desc.setText(args.myservice.serviceDescription)
        view.edit_priceedittext.setText(args.myservice.price)

        val premon = args.myservice.mon
        val pretue = args.myservice.tue
        val prewed = args.myservice.wed
        val prethu = args.myservice.thu
        val prefri = args.myservice.fri
        val presat = args.myservice.sat
        val presun = args.myservice.sun

        if (premon == "1"){
            view.edit_MON_switch.setChecked(true)
        }else{
            view.edit_MON_switch.setChecked(false)
        }

        if (pretue == "1"){
            view.edit_TUE_switch.setChecked(true)
        }else{
            view.edit_TUE_switch.setChecked(false)
        }

        if (prewed == "1"){
            view.edit_WED_switch.setChecked(true)
        }else{
            view.edit_WED_switch.setChecked(false)
        }

        if (prethu == "1"){
            view.edit_THU_switch.setChecked(true)
        }else{
            view.edit_THU_switch.setChecked(false)
        }

        if (prefri == "1"){
            view.edit_FRI_switch.setChecked(true)
        }else{
            view.edit_FRI_switch.setChecked(false)
        }

        if (presat == "1"){
            view.edit_SAT_switch.setChecked(true)
        }else{
            view.edit_SAT_switch.setChecked(false)
        }

        if (presun == "1"){
            view.edit_SUN_switch.setChecked(true)
        }else{
            view.edit_SUN_switch.setChecked(false)
        }

        val prehourStart = args.myservice.hourStart
        val preminStart = args.myservice.minStart
        val prehourEnd = args.myservice.hourEnd
        val preminEnd = args.myservice.minEnd

//        view.edit_timePicker1.hour.s


        val cunt = resources.getStringArray(R.array.broadtypes)
        val arrayadapters = ArrayAdapter(requireContext(), R.layout.country_dropdown, cunt)
        view.edit_service_auto_complete_textView.setAdapter(arrayadapters)

        view.edit_delete_btn.setOnClickListener {
            deletefun()
        }

        view.edit_createService_btn.setOnClickListener {
            val serviceName = view.edit_servicename.text.toString()
            val serviceType = view.edit_service_auto_complete_textView.text.toString()
            val serviceDescription = view.edit_service_desc.text.toString()

            val mon = if(view.edit_MON_switch.isChecked) 1 else 0
            val tue = if(view.edit_TUE_switch.isChecked) 1 else 0
            val wed = if(view.edit_WED_switch.isChecked) 1 else 0
            val thu = if(view.edit_THU_switch.isChecked) 1 else 0
            val fri = if(view.edit_FRI_switch.isChecked) 1 else 0
            val sat = if(view.edit_SAT_switch.isChecked) 1 else 0
            val sun = if(view.edit_SUN_switch.isChecked) 1 else 0

            val hourStart = view.edit_timePicker1.hour
            val minStart = view.edit_timePicker1.minute
            val hourEnd = view.edit_timePicker2.hour
            val minEnd = view.edit_timePicker2.minute

            val price = view.edit_priceedittext.text.toString()

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
                                val creatservice = arrayOf(serviceProvider,serviceName,serviceType,serviceDescription,mon.toString(),tue.toString(),wed.toString(),thu.toString(),fri.toString(),sat.toString(),sun.toString(),hourStart.toString(),minStart.toString(),hourEnd.toString(),minEnd.toString(),price,currency, args.myservice.serviceName, city)
                                createservicefun(creatservice)
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
                val response = retrofit.editServiceApi(creatservice)
                try {
                    if (response.code() == 200) {
                        findNavController().navigate(R.id.action_editServices_to_services)
                    } else {
                        println("service edit failure")
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

    private fun deletefun() {

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
                val response = retrofit.deleteServiceApi(args.myservice.serviceName)
                try {
                    if (response.code() == 200) {
                        findNavController().navigate(R.id.action_editServices_to_services)
                    } else {
                        println("service deletion failure")
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