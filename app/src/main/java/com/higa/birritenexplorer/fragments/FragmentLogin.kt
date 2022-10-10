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

class FragmentLogin: Fragment() {

    companion object {
        fun newInstance() = FragmentMain()
    }

    private val PREF_NAME = "myPreferences"

    lateinit var v : View
    lateinit var buttonLogin : Button
    lateinit var editTextUserName : EditText
    lateinit var editTextUserPassword : EditText

    private var db: AppDatabase? = null
    private var userDao: UserDao? = null
    lateinit var userController: UserController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_login, container, false)

        buttonLogin = v.findViewById(R.id.buttonLogin)
        editTextUserName = v.findViewById(R.id.editTextUserName)
        editTextUserPassword = v.findViewById(R.id.editTextUserPassword)

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

        userDao?.insert(User(1, "asdf", "asdf", "seb@higa.com"))

        buttonLogin.setOnClickListener {
            val name = editTextUserName.text.toString()
            val pass = editTextUserPassword.text.toString()

            if (!userController.isValid(User(1, name, pass, ""))){
              // TODO: Alert notification, red wriggles and whatever
                return@setOnClickListener
            }

            val userInfo = userDao?.loadByName(name)
            val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()

            editor.putString("USER", userInfo?.name)
            editor.putString("EMAIL", userInfo?.email)
            editor.apply()

            val action = FragmentLoginDirections.actionFragmentLogin2ToMainActivity()
            v.findNavController().navigate(action)
            activity?.finish()
        }
    }
}