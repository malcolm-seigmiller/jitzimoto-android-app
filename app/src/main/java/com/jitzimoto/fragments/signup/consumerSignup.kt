package com.jitzimoto.fragments.signup

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jitzimoto.R
import com.jitzimoto.dataModels.consumer
import com.jitzimoto.utils.ViewModel
import kotlinx.android.synthetic.main.fragment_consumer_signup.*
import kotlinx.android.synthetic.main.fragment_consumer_signup.view.*

class consumerSignup : Fragment() {

    private lateinit var mUserViewModel: ViewModel

    //add it so that on selecting a new cunt it will erase or change the region

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_consumer_signup, container, false)

//        val list1 = arrayListOf<String>("canada","america","australia")
//        val arrayadapters = ArrayAdapter(requireContext(), R.layout.country_dropdown, list1)
//        view.autoCompleteTextView.setAdapter(arrayadapters)


//        country input
        val cunt = resources.getStringArray(R.array.nations)
        val arrayadapters = ArrayAdapter(requireContext(), R.layout.country_dropdown, cunt)
        view.autoCompleteTextView.setAdapter(arrayadapters)

        view.autoCompleteTextView2.setOnClickListener {
            val abc = autoCompleteTextView.text.toString()

            if(abc == "canada"){
                val cunt2 = resources.getStringArray(R.array.canada)
                val arrayadapters2 = ArrayAdapter(requireContext(), R.layout.locality_dropdown, cunt2)
                view.autoCompleteTextView2.setAdapter(arrayadapters2)
            }

            if (abc == "us"){
                val cunt2 = resources.getStringArray(R.array.american_states)
                val arrayadapters2 = ArrayAdapter(requireContext(), R.layout.locality_dropdown, cunt2)
                view.autoCompleteTextView2.setAdapter(arrayadapters2)
            }

            if (abc == "uk"){
                val cunt2 = resources.getStringArray(R.array.uk_regions)
                val arrayadapters2 = ArrayAdapter(requireContext(), R.layout.locality_dropdown, cunt2)
                view.autoCompleteTextView2.setAdapter(arrayadapters2)
            }

            if (abc == "australia"){
                val cunt2 = resources.getStringArray(R.array.australia)
                val arrayadapters2 = ArrayAdapter(requireContext(), R.layout.locality_dropdown, cunt2)
                view.autoCompleteTextView2.setAdapter(arrayadapters2)
            }

            if (abc == "new zealand"){
                val cunt2 = resources.getStringArray(R.array.new_Zealand)
                val arrayadapters2 = ArrayAdapter(requireContext(), R.layout.locality_dropdown, cunt2)
                view.autoCompleteTextView2.setAdapter(arrayadapters2)
            }
        }


        view.button3.setOnClickListener {

            enterfun()
        }

        return view
    }

    private fun enterfun() {
        val cunt = autoCompleteTextView.text.toString()
        val local = autoCompleteTextView2.text.toString()
        val name = editTextTextPersonName2.text.toString()

        println(name)
        println(cunt)
        println(local)

        println(name + cunt + local)

//        val entry = consumer(0,name,cunt,local)
//        mUserViewModel.addconsumer(consumer(0,name,cunt,local))
//        mUserViewModel.addconsumer(entry)

        if (namecheck(name)) {
            if (cuntcheck(cunt)){
                if (localcheck(local)){
                    val entry = consumer(0,name,cunt,local)
                    mUserViewModel = ViewModelProvider(this).get(ViewModel::class.java)
                    mUserViewModel.addconsumer(entry)
                    findNavController().navigate(R.id.action_consumerSignup_to_splash)
                }else{
                    println("would not enter")
                }
            }else{
                println("would not enter")
            }
        }else{
            println("would not enter")
        }
    }

    private fun localcheck(local: String): Boolean {
        return !(TextUtils.isEmpty(local))
    }

    private fun cuntcheck(cunt: String): Boolean {
        return !(TextUtils.isEmpty(cunt))
    }

    private fun namecheck(name: String): Boolean {
        return !(TextUtils.isEmpty(name))
    }

}