package com.jitzimoto.fragments.signup

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.jitzimoto.BuildConfig
import com.jitzimoto.R
import com.jitzimoto.api.API
import kotlinx.android.synthetic.main.fragment_biz_signup.*
import kotlinx.android.synthetic.main.fragment_biz_signup.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class bizSignup : Fragment() {

    val gson = GsonBuilder()
            .setLenient()
            .create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_biz_signup, container, false)

        val cunt = resources.getStringArray(R.array.nations)
        val arrayadapters = ArrayAdapter(requireContext(), R.layout.country_dropdown, cunt)
        view.cuntAutoCompleteTextView.setAdapter(arrayadapters)

        view.regAutoCompleteTextView.setOnClickListener {
            val abc = cuntAutoCompleteTextView.text.toString()

            if(abc == "canada"){
                val cunt2 = resources.getStringArray(R.array.canada)
                val arrayadapters2 = ArrayAdapter(requireContext(), R.layout.locality_dropdown, cunt2)
                view.regAutoCompleteTextView.setAdapter(arrayadapters2)
            }

            if (abc == "us"){
                val cunt2 = resources.getStringArray(R.array.american_states)
                val arrayadapters2 = ArrayAdapter(requireContext(), R.layout.locality_dropdown, cunt2)
                view.regAutoCompleteTextView.setAdapter(arrayadapters2)
            }

            if (abc == "uk"){
                val cunt2 = resources.getStringArray(R.array.uk_regions)
                val arrayadapters2 = ArrayAdapter(requireContext(), R.layout.locality_dropdown, cunt2)
                view.regAutoCompleteTextView.setAdapter(arrayadapters2)
            }

            if (abc == "australia"){
                val cunt2 = resources.getStringArray(R.array.australia)
                val arrayadapters2 = ArrayAdapter(requireContext(), R.layout.locality_dropdown, cunt2)
                view.regAutoCompleteTextView.setAdapter(arrayadapters2)
            }

            if (abc == "new zealand"){
                val cunt2 = resources.getStringArray(R.array.new_Zealand)
                val arrayadapters2 = ArrayAdapter(requireContext(), R.layout.locality_dropdown, cunt2)
                view.regAutoCompleteTextView.setAdapter(arrayadapters2)
            }
        }

        //broad biz type
        //maybe remove this, it doesnt really fit.
        //it will do for now
        val broad = resources.getStringArray(R.array.broadtypes)
        val arrayadapters2 = ArrayAdapter(requireContext(), R.layout.country_dropdown, broad)
        view.subAutoCompleteTextView.setAdapter(arrayadapters2)


        //exact business
        val sub = resources.getStringArray(R.array.biztypes)
        val arrayadapters3 = ArrayAdapter(requireContext(), R.layout.country_dropdown, sub)
        view.bizAutoCompleteTextView.setAdapter(arrayadapters3)


        view.button4.setOnClickListener {
            prescreen()
        }

        return view
    }

    private fun prescreen() {
        //this function makes sure their are no irregularities in the system
        val pwd = view?.password?.text.toString()
        val pwdConfirm = view?.confirm_password?.text.toString()
        val country = view?.cuntAutoCompleteTextView?.text.toString()
        val cityAndRegion = view?.regAutoCompleteTextView?.text.toString()
        val broadBiz = view?.subAutoCompleteTextView?.text.toString()
        val bizType = view?.bizAutoCompleteTextView?.text.toString()
        val name = view?.bizName?.text.toString()
        val email = view?.editTextTextEmailAddress?.text.toString()


        //if else pyramid to make sure the user entered in all the data
        if (namecheck(name)){
            if (cuntcheck(country)){
                if (citycheck(cityAndRegion)){
                    if (broadcheck(broadBiz)){
                        if (bizcheck(bizType)){
                            if (emailcheck(email)){
                                if (passwordcheck(pwd)){
                                    if (confimpwdcheck(pwdConfirm)){
                                        if (pwd == pwdConfirm){
                                            //add password restrictions
                                            println("all good nigga")
                                            //i think you should check if the email is already associated with an account here
                                            accuntcheck(email,pwd,country,cityAndRegion,broadBiz,bizType,name)
                                        }else{
                                            println("no match")
                                        }
                                    }else{
                                        println("confirm your password")
                                    }
                                }else{
                                    println("enter a password")
                                }
                            }else{
                                println("enter your email")
                            }
                        }else{
                            println("enter the type of your biz")
                        }
                    }else{
                        println("enter the broad business type of your biz")
                    }
                }else{
                    println("enter your city And Region")
                }
            }else{
                println("enter your country")
            }
        }else{
            println("enter your biz name")
        }

    }

    //172.17.167.192
    val BASE_URL = "http://10.0.2.2:80/"
//    val BASE_URL = "https://run.mocky.io/"
//    http://localhost/jsontest.php
    //http://127.0.0.1/jsontest.php
    //val TAG = "MainActivity"

    private fun accuntcheck(email: String, pwd: String, country: String, cityAndRegion: String, broadBiz: String, bizType: String, name: String) {
        //rn lets just get retrofit up and running

        val builder = OkHttpClient().newBuilder()
        builder.readTimeout(120, TimeUnit.SECONDS)
        builder.connectTimeout(5, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }
        //you're going to want to remove above at launch ... Maybe????
        val client = builder.build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(API::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = retrofit.emailcheck(email)//it wants a JSONarray
                //the above wants some kind of JSON to work we were passing Strings instead
                try {
                    println(response)
                    if (response.code() == 202) {
                        signup(email,pwd,country,cityAndRegion,broadBiz,bizType,name)
                    } else {
                        println("this email is all ready associated with an account:unsexy man")
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

    private fun signup(email: String, pwd: String, country: String, cityAndRegion: String, broadBiz: String, bizType: String, name: String) {
        println("in the clear")

        //make array

        val abc = arrayOf(email, pwd, country, cityAndRegion, broadBiz, bizType, name)
//        val typedArray = abc.split(",").toTypedArray()

        val builder = OkHttpClient().newBuilder()
        builder.readTimeout(120, TimeUnit.SECONDS)
        builder.connectTimeout(5, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }
        //you're going to want to remove above at launch ... Maybe????
        val client = builder.build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(API::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            val response = retrofit.signupPost(abc)//it wants a JSONarray
            try {
//                val response = retrofit.signupPost()//it wants a JSONarray
                //the above wants some kind of JSON to work we were passing Strings instead
                if (response.code() == 201) {
                    findNavController().navigate(R.id.action_bizSignup_to_verifiy)
                } else {
                    println("acc creation failed")

                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "something went wrong!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun confimpwdcheck(pwdConfirm: String): Boolean {
        return !(TextUtils.isEmpty(pwdConfirm))
    }

    private fun passwordcheck(pwd: String): Boolean {
        return !(TextUtils.isEmpty(pwd))
    }

    private fun emailcheck(email: String): Boolean {
        return !(TextUtils.isEmpty(email))
    }

    private fun namecheck(name: String): Boolean {
        return !(TextUtils.isEmpty(name))
    }

    private fun bizcheck(bizType: String): Boolean {
        return !(TextUtils.isEmpty(bizType))
    }

    private fun broadcheck(broadBiz: String): Boolean {
        return !(TextUtils.isEmpty(broadBiz))
    }

    private fun citycheck(cityAndRegion: String): Boolean {
        return !(TextUtils.isEmpty(cityAndRegion))
    }

    private fun cuntcheck(country: String): Boolean {
        return !(TextUtils.isEmpty(country))
    }

}