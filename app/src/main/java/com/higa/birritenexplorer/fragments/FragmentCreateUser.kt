package com.higa.birritenexplorer.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.higa.birritenexplorer.R
import com.higa.birritenexplorer.controllers.UserController
import com.higa.birritenexplorer.database.AppDatabase
import com.higa.birritenexplorer.database.UserDao
import com.higa.birritenexplorer.entities.User

class FragmentCreateUser: Fragment() {

    companion object {
        fun newInstance() = FragmentMain()
    }

    private val PREF_NAME = "myPreferences"

    lateinit var v : View
    lateinit var buttonCreateUser : Button
    lateinit var editTextCreateUser : EditText
    lateinit var editTextCreatePassword : EditText
    lateinit var editTextCreateEmail : EditText

    private var db: AppDatabase? = null
    private var userDao: UserDao? = null
    lateinit var userController: UserController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_create_user, container, false)

        buttonCreateUser = v.findViewById(R.id.buttonCreateUser)
        editTextCreateUser = v.findViewById(R.id.editTextCreateUser)
        editTextCreatePassword = v.findViewById(R.id.editTextCreatePassword)
        editTextCreateEmail = v.findViewById(R.id.editTextCreateEmail)

        return v
    }


    override fun onResume() {
        super.onResume()
        onStart()
    }

    override fun onStart() {
        super.onStart()

        db = AppDatabase.getAppDataBase(v.context)
        userDao = db?.UserDao()
        userController = UserController(userDao)

        val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        buttonCreateUser.setOnClickListener {
            val name = editTextCreateUser.text.toString()
            val pass = editTextCreatePassword.text.toString()
            val email = editTextCreateEmail.text.toString()

            userController.insert(User(name, pass, email))

            val userInfo = userDao?.loadByName(name)
            val editor = sharedPref.edit()

            editor.putString("USER", name)
            editor.putString("EMAIL", email)
            userInfo?.id?.let { it1 -> editor.putInt("USERID", it1) }
            editor.apply()

            val action = FragmentCreateUserDirections.actionFragmentCreateUserToMainActivity2()
            v.findNavController().navigate(action)
            activity?.finish()
        }
    }
}