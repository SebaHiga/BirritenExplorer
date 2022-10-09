package com.higa.birritenexplorer.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.higa.birritenexplorer.R
import com.higa.birritenexplorer.adapters.ItemAdapter
import com.higa.birritenexplorer.database.AppDatabase
import com.higa.birritenexplorer.database.UserDao
import com.higa.birritenexplorer.entities.Item
import com.higa.birritenexplorer.entities.User

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentProfile.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentProfile : Fragment() {
    // TODO: Rename and change types of parameters

    private val PREF_NAME = "myPreferences"

    lateinit var buttonSetup : Button
    lateinit var textName : TextView
    lateinit var textEmail : TextView
    lateinit var v : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_profile, container, false)
        buttonSetup = v.findViewById(R.id.buttonSettings)
        textName = v.findViewById(R.id.textProfileName)
        textEmail = v.findViewById(R.id.textProfileEmail)

        return v
    }

    override fun onStart() {
        super.onStart()

        val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val name = sharedPref.getString("USER","User was not found")!!
        val email = sharedPref.getString("EMAIL","Email was not found")!!

        textName.setText(name)
        textEmail.setText(email)

        buttonSetup.setOnClickListener {
            val action = FragmentProfileDirections.actionFragmentProfileToSettingsActivity()
            v.findNavController().navigate(action)
        }
    }


}