package com.higa.birritenexplorer.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.higa.birritenexplorer.controllers.UserController
import com.higa.birritenexplorer.database.AppDatabase
import com.higa.birritenexplorer.database.UserDao
import com.higa.birritenexplorer.databinding.FragmentLoginBinding
import com.higa.birritenexplorer.entities.User


class FragmentLogin: Fragment() {

    companion object {
        fun newInstance() = FragmentMain()
    }

    private val PREF_NAME = "myPreferences"

    lateinit var binding : FragmentLoginBinding
    lateinit var callbackManager : com.facebook.CallbackManager
    private lateinit var auth: FirebaseAuth
// ...
// Initialize Firebase Auth
    private var db: AppDatabase? = null
    private var userDao: UserDao? = null
    lateinit var userController: UserController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginButton.setReadPermissions("email")
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        callbackManager = CallbackManager.Factory.create();
        // If you are using in a fragment, call loginButton.setFragment(this);
        auth = Firebase.auth

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun askLoginWithPreviousUser(){
        val builder = AlertDialog.Builder(activity as Context)

        builder.setTitle("Confirm")
        builder.setMessage("Are you sure?")

        builder.setPositiveButton(
            "YES",
            DialogInterface.OnClickListener { dialog, which -> // Do nothing but close the dialog
                dialog.dismiss()
                goToMainActivity()
            })

        builder.setNegativeButton(
            "NO",
            DialogInterface.OnClickListener { dialog, which -> // Do nothing
                dialog.dismiss()
            })

        val alert: AlertDialog = builder.create()
        alert.show()
    }

    override fun onResume() {
        super.onResume()
        onStart()
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("TAG", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        activity?.let {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val email = auth.currentUser?.email
                        Log.d("TTAGTAGTAGTAGTAGTAGTAGTAGTAGTAGAG", "signInWithCredential:success user is $email")

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithCredential:failure", task.exception)
//                        Toast.makeText(baseContext, "Authentication failed.",
//                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun goToMainActivity(){
        val action = FragmentLoginDirections.actionFragmentLogin2ToMainActivity()
        binding.root.findNavController().navigate(action)
        activity?.finish()
    }

    override fun onStart() {
        super.onStart()

//        db = AppDatabase.getAppDataBase(binding.root as Context)
        userDao = db?.UserDao()
        userController = UserController(userDao)

        userController.insert(User("asdf", "asdf", "seb@higa.com"))
        userController.insert(User("qwer", "qwer", "seb@higa.com"))

        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        if (isLoggedIn){
            accessToken?.let { handleFacebookAccessToken(it) }
            askLoginWithPreviousUser()
        }

        // Callback registration
        binding.loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }

            override fun onSuccess(result: LoginResult?) {
                result?.let { handleFacebookAccessToken(it.accessToken) }
                goToMainActivity()
            }
        })

        binding.buttonLogin.setOnClickListener {
            val name = binding.editTextUserName.text.toString()
            val pass = binding.editTextUserPassword.text.toString()

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
            binding.root.findNavController().navigate(action)
            activity?.finish()
        }

        binding.buttonGotoUserCreation.setOnClickListener {
            val action = FragmentLoginDirections.actionFragmentLoginToFragmentCreateUser()
            binding.root.findNavController().navigate(action)
        }
    }
}