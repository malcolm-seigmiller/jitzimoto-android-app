package com.jitzimoto.fragments.login

import android.os.Bundle
import android.text.TextUtils
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
import com.jitzimoto.dataModels.user
import com.jitzimoto.dataModels.userdetails
import com.jitzimoto.utils.ViewModel
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Login : Fragment() {

    private lateinit var mUserViewModel: ViewModel

    val gson = GsonBuilder()
            .setLenient()
            .create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_login, container, false)

        clearfun()

        view.login_btn.setOnClickListener{
            inputcheck()
        }

        view.sign_up_btn.setOnClickListener {
            signupfun()
//            readcheck()
        }

        view.consumer_signup_btn.setOnClickListener {
            customerfun()
        }

//        add function that destroys all login details
        return view
    }

    private fun clearfun() {
        mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        mUserViewModel.deleteUser()
        mUserViewModel.deleteDetails()
    }

//    fun getAdminArea(): String!

    private fun customerfun() {
        findNavController().navigate(R.id.action_login_to_consumerSignup)

    }

    private fun signupfun() {
        findNavController().navigate(R.id.action_login_to_bizSignup2)
    }

    private fun inputcheck() {
        val email = view?.emailtext?.text.toString()
        val password = view?.passwordtext?.text.toString()

        if(emailcheck(email)){
            if (passwordcheck(password)){
                login(email,password)
            }else{
                println("please enter a password")
            }
        }else{
            println("please enter your email")
        }
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
                val response = retrofit.loginPost(abc)
                try {
                    if (response.code() == 200) {
                        println(response.body())
                        val deetes = response.body()?.userdetails
                        val eAp = response.body()?.user
                        println(eAp)
                        println(deetes)
                        save(eAp, deetes)
                        println("updated")
                    } else {
                        println("please enter a correct email and password")
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

    private fun save(eAp: user?, deetes: userdetails?) {
        if (eAp != null) {
            mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
            mUserViewModel.adduser(eAp)
            println("entered user")
            if (deetes != null) {

                mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
                mUserViewModel.addLogindetails(deetes)
                println("entered in user details")

                findNavController().navigate(R.id.action_login_to_home1)

            }else{
                println("failed to enter in user details")
            }

        }else{
            println("failed to enter user")
        }
    }

    private fun passwordcheck(password: String): Boolean {
        return !(TextUtils.isEmpty(password))
    }

    private fun emailcheck(email: String): Boolean {
        return !(TextUtils.isEmpty(email))
    }

    private fun readfun(){
        mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        mUserViewModel.readUserDetails.observe(viewLifecycleOwner, Observer{ user ->
            println(user)
        })

        mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        mUserViewModel.userRowCount.observe(viewLifecycleOwner, Observer { user ->
            if (user >= 1){
                //send to home 2
                println("user found")
            }else{
//                findNavController().navigate(R.id.action_splash_to_login)
                //keep redirect for now
                println("no user found")
                println(user)
            }
        })

        mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        mUserViewModel.consumerRowCount.observe(viewLifecycleOwner, Observer { user ->
            if (user >= 1){
                //send to home 2
                println("consumer found")
            }else{
//                findNavController().navigate(R.id.action_splash_to_login)
                //keep redirect for now
                println("no consumer found")
                println(user)
            }
        })
    }

}