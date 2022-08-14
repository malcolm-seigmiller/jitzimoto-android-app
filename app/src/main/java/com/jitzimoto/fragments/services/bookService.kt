package com.jitzimoto.fragments.services

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.GsonBuilder
import com.jitzimoto.BuildConfig
import com.jitzimoto.R
import com.jitzimoto.api.API
import com.jitzimoto.fragments.listings.listingsArgs
import com.jitzimoto.utils.ViewModel
import kotlinx.android.synthetic.main.fragment_book_service.view.*
import kotlinx.android.synthetic.main.myservicecard.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class bookService : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private val args by navArgs<bookServiceArgs>()

    private lateinit var mUserViewModel: ViewModel

    val gson = GsonBuilder()
        .setLenient()
        .create()

    val BASE_URL = "http://10.0.2.2:80/"


    var saveDay = 0
    var saveMonth = 0
    var saveYear = 0
    var saveHour = 0
    var saveMinute = 0

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_book_service, container, false)

        view.book_btn.setOnClickListener {
            mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
            mUserViewModel.userRowCount.observe(viewLifecycleOwner, { user ->
                if (user >= 1){
                    mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
                    mUserViewModel.readUserDetails.observe(viewLifecycleOwner, { user ->
                        val name = user.name
                        val location = user.city
                        val serviceProvider = args.serviceprams.serviceProvider
                        val price = args.serviceprams.price
//                        val location = args.serviceprams.city
                        val serviceName = args.serviceprams.serviceName
                        val comment = view.commentText.text.toString()
                        //Todo : add logic to stop you from booking when the biz owner doesnt want it
//                      //Todo : add ending times as well
                        val abc = arrayOf(name, serviceProvider, serviceName, price, location, comment, saveDay.toString(), saveMonth.toString(), saveYear.toString(), saveHour.toString(), saveMinute.toString())

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
                                val response = retrofit.bookingApi(abc)
                                try {
                                    Log.i("RESPONSE", "RAW: " + response)
                                    if (response.code() == 200) {
                                        println("booked")
                                        findNavController().navigate(R.id.action_bookService_to_home1)

                                    }else{
                                        println("not booked")
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
                    mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
                    mUserViewModel.consumerRowCount.observe(viewLifecycleOwner, { user ->
                        if (user >= 1){
                            mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
                            mUserViewModel.readConsumerData.observe(viewLifecycleOwner, { user ->
                                val name = user.name
                                val location = user.region
                                val serviceProvider = args.serviceprams.serviceProvider
                                val price = args.serviceprams.price
//                        val location = args.serviceprams.city
                                val serviceName = args.serviceprams.serviceName
                                val comment = view.commentText.text.toString()
                                //Todo : add logic to stop you from booking when the biz owner doesnt want it
//                              //Todo : add ending times as well
                                val abc = arrayOf(name, serviceProvider, serviceName, price, location, comment, saveDay.toString(), saveMonth.toString(), saveYear.toString(), saveHour.toString(), saveMinute.toString())

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
                                        val response = retrofit.bookingApi(abc)
                                        try {
                                            if (response.code() == 200) {
                                                println("booked")
                                                findNavController().navigate(R.id.action_bookService_to_home2)
                                            }else{
                                                println("not booked")
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
        }
        pickdate(view)
        return view
    }

    private fun getDate() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickdate(view: View) {
        view.date_btn?.setOnClickListener {
            getDate()
            view.let { DatePickerDialog(it.context,this,year,month,day).show() }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        saveDay = dayOfMonth
        saveMonth = month
        saveYear = year
        getDate()
        view?.let { TimePickerDialog(it.context,this,hour,minute,true).show() }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        saveHour = hourOfDay
        saveMinute = minute

    }

}