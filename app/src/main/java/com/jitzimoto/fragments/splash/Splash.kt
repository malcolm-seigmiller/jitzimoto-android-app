package com.jitzimoto.fragments.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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

class Splash : Fragment() {
    private lateinit var mUserViewModel: ViewModel

    val gson = GsonBuilder()
            .setLenient()
            .create()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        //I guess the first thing it should do Is count if there is an account
        checkfun()
        return view
    }

    private fun checkfun() {
        mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        mUserViewModel.userRowCount.observe(viewLifecycleOwner, { user ->
            if (user >= 1){
                logincheck()
            }else{
                concheck()
            }
        })
    }
    
    private fun logincheck() {
        mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        mUserViewModel.checkUserInfo.observe(viewLifecycleOwner,  { user ->
            val email = user.email
            val password = user.password
            println(user)

            login(email, password)
        })
    }

    private fun concheck() {
        mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        mUserViewModel.consumerRowCount.observe(viewLifecycleOwner, Observer { user ->
            if (user >= 1){
                //send to home 2
                findNavController().navigate(R.id.action_splash_to_home2)
            }else{
                findNavController().navigate(R.id.action_splash_to_login)
            }
        })
    }

    val BASE_URL = "http://10.0.2.2:80/"

    private fun login(email: String, password: String) {

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
                        findNavController().navigate(R.id.action_splash_to_home1)
                    } else {
                        findNavController().navigate(R.id.action_splash_to_login)
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