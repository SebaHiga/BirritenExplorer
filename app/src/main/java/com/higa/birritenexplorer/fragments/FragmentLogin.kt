package com.higa.birritenexplorer.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.higa.birritenexplorer.controllers.UserController
import com.higa.birritenexplorer.database.AppDatabase
import com.higa.birritenexplorer.database.UserDao
import com.higa.birritenexplorer.entities.User
import java.util.*


class FragmentLogin: Fragment() {

    companion object {
        fun newInstance() = FragmentMain()
    }

    private val PREF_NAME = "myPreferences"

    lateinit var v : View
    lateinit var buttonLogin : Button
    lateinit var buttonGotoCreateUser : Button
    lateinit var editTextUserName : EditText
    lateinit var editTextUserPassword : EditText
    lateinit var callbackManager : com.facebook.CallbackManager
    lateinit var loginButton : LoginButton

    private var db: AppDatabase? = null
    private var userDao: UserDao? = null
    lateinit var userController: UserController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(com.higa.birritenexplorer.R.layout.fragment_login, container, false)

        buttonLogin = v.findViewById(com.higa.birritenexplorer.R.id.buttonLogin)
        buttonGotoCreateUser = v.findViewById(com.higa.birritenexplorer.R.id.buttonGotoUserCreation)
        editTextUserName = v.findViewById(com.higa.birritenexplorer.R.id.editTextUserName)
        editTextUserPassword = v.findViewById(com.higa.birritenexplorer.R.id.editTextUserPassword)


        val EMAIL = "email"

        loginButton = v.findViewById(com.higa.birritenexplorer.R.id.login_button) as LoginButton
        loginButton.setReadPermissions(Arrays.asList(EMAIL))
        // If you are using in a fragment, call loginButton.setFragment(this);    

        // Callback registration
        callbackManager = CallbackManager.Factory.create();
        // If you are using in a fragment, call loginButton.setFragment(this);



        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
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

        userController.insert(User("asdf", "asdf", "seb@higa.com"))
        userController.insert(User("qwer", "qwer", "seb@higa.com"))

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }

            override fun onSuccess(result: LoginResult?) {
                Log.d("FEISBUIK", "facebook:onSuccess:")
                val action = FragmentLoginDirections.actionFragmentLogin2ToMainActivity()
                v.findNavController().navigate(action)
                activity?.finish()
            }
        })

        buttonLogin.setOnClickListener {
            val name = editTextUserName.text.toString()
            val pass = editTextUserPassword.text.toString()

            if (!userController.isValid(User(name, pass, ""))){
              // TODO: Alert notification, red wriggles and whatever
                return@setOnClickListener
            }

            val userInfo = userDao?.loadByName(name)
            val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()

            editor.putString("USER", userInfo?.name)
            editor.putString("EMAIL", userInfo?.email)
            userInfo?.id?.let { it1 -> editor.putInt("USERID", it1) }
            editor.apply()

            val action = FragmentLoginDirections.actionFragmentLogin2ToMainActivity()
            v.findNavController().navigate(action)
            activity?.finish()
        }

        buttonGotoCreateUser.setOnClickListener {
            val action = FragmentLoginDirections.actionFragmentLoginToFragmentCreateUser()
            v.findNavController().navigate(action)
        }
    }
}