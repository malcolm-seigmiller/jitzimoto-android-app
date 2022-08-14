package com.jitzimoto.fragments.bookings

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.GsonBuilder
import com.jitzimoto.BuildConfig
import com.jitzimoto.R
import com.jitzimoto.api.API
import kotlinx.android.synthetic.main.fragment_book_service.view.*
import kotlinx.android.synthetic.main.fragment_editbookings.view.*
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


class editbookings : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
//    todo: make this usesable, for now just put a delete button and leave it for later.

    private val args by navArgs<editbookingsArgs>()

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
        val view = inflater.inflate(R.layout.fragment_editbookings, container, false)

        val booker = args.editBookingsParam.booker
        val serviceProvider = args.editBookingsParam.serviceProvider
        val serviceName = args.editBookingsParam.serviceName
        val location = args.editBookingsParam.location
        val price = args.editBookingsParam.price
        val comments = args.editBookingsParam.comments

        var day = args.editBookingsParam.day
        var month = args.editBookingsParam.month
        var argsyear = args.editBookingsParam.year
        var hour = args.editBookingsParam.hour
        var minute = args.editBookingsParam.minute

        //todo: make it so that the month will display
        view.myedit_edit_bookedtimetext.text = "${hour} : ${minute} | ${month} : ${day}"


        //        view.myedit_bookingdatetextview.setText("")
        view.myedit_servicebooked.setText(serviceName)
        view.myedit_booker_booker.setText(booker)
//        view.myedit_editTextTextMultiLine_booker.setText(args.editBookingsParam.comments)
//        view.myedit_location_textview.setText(location)
        view.myedit_textView_price.setText(price)

//        val commentText = view.myedit_editTextTextMultiLine_booker.text.toString()

        view.edit_booking_btn.setOnClickListener {
//            editfun(commentText)
            deletefun()
        }

        view.edit_delete_bookings_btn.setOnClickListener {
            findNavController().navigate(R.id.action_editbookings_to_mybookings)
        }
        pickdate(view)
        return view
    }

    private fun deletefun() {
//        println(commentText)
        //editable
//        val comments

//        val array = arrayOf(booker,serviceProvider, serviceName, price, location, commentText, saveDay.toString(), saveMonth.toString(), saveYear.toString(), saveHour.toString(), saveMinute.toString())
        val booker = args.editBookingsParam.booker
        val serviceName = args.editBookingsParam.serviceName


        val array = arrayOf(booker, serviceName)
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
                val response = retrofit.deleteBookingsApi(array)
                try {
                    if (response.code() == 200) {
                        println("edited")
                        findNavController().navigate(R.id.action_editbookings_to_mybookings)
                    } else {
                        println("not edited")
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
//        view.myedit_edit_bookedtimetext?.setOnClickListener {
//            getDate()
//            view.let { DatePickerDialog(it.context,this,year,month,day).show() }
//        }
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

//        val savedHour = saveHour.toString()
//        val savedMinute = saveMinute.toString()
//        val savedMonth = saveMonth.toString()
//        val savedDay = saveDay.toString()
        view?.myedit_edit_bookedtimetext?.text = "$saveHour : $saveMinute | $saveMonth : $saveDay"

        println(saveDay)
        println(saveMonth)
        println(saveYear)
        println(saveHour)
        println(saveMinute)
    }

    private fun editfun(commentText: String) {
        //get everything into a variable
        //saveDay
        //saveMonth
        //saveYear
        //saveHour
        //saveMinute

        val booker = args.editBookingsParam.booker
        val serviceProvider = args.editBookingsParam.serviceProvider
        val serviceName = args.editBookingsParam.serviceName
        val location = args.editBookingsParam.location
        val price = args.editBookingsParam.price

        println(commentText)
        //editable
//        val comments

        val array = arrayOf(booker,serviceProvider, serviceName, price, location, commentText, saveDay.toString(), saveMonth.toString(), saveYear.toString(), saveHour.toString(), saveMinute.toString())

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
                val response = retrofit.editBookingsApi(array)
                try {
                    if (response.code() == 200) {
                        println("edited")
                        findNavController().navigate(R.id.action_editbookings_to_mybookings)
                    }else{
                        println("not edited")
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
    }
}