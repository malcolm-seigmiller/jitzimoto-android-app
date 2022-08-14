package com.jitzimoto.fragments.signup

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.gson.GsonBuilder
import com.jitzimoto.BuildConfig
import com.jitzimoto.R
import com.jitzimoto.api.API
import kotlinx.android.synthetic.main.fragment_verifiy.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Verifiy : Fragment() {
    val gson = GsonBuilder()
            .setLenient()
            .create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_verifiy, container, false)

        view.verify_btn.setOnClickListener {
            inputcheck()
        }
        return view
    }

    private fun inputcheck() {
        val vkey = view?.vkey_text?.text.toString()

        if (checkvkey(vkey)){
            verify(vkey)
        }else{
            println("enter a vkey")
        }
    }

    val BASE_URL = "http://10.0.2.2:80/"


    private fun verify(vkey: String) {
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
                val response = retrofit.verifyPost(vkey)
                try {
                    println(response)
                    if (response.code() == 200) {
                        findNavController().navigate(R.id.action_verifiy_to_login2)
                    } else {
                        println("invalid vkey")
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

    private fun checkvkey(vkey: String): Boolean {
        return !(TextUtils.isEmpty(vkey))
    }

}