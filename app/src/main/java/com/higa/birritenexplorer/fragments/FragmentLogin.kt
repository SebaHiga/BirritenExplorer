package com.higa.birritenexplorer.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.higa.birritenexplorer.R
import com.higa.birritenexplorer.database.AppDatabase
import com.higa.birritenexplorer.database.ItemDao
import com.higa.birritenexplorer.database.UserDao

class FragmentLogin: Fragment() {

    companion object {
        fun newInstance() = FragmentMain()
    }

    private val PREF_NAME = "myPreferences"

    lateinit var v : View
    lateinit var buttonLogin : Button

    private var db: AppDatabase? = null
    private var userDao: UserDao? = null
//    private var itemDao: ItemDao? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_login, container, false)

        buttonLogin = v.findViewById(R.id.buttonLogin)

        return v
    }

    override fun onResume() {
        super.onResume()
        onStart()
    }

    override fun onStart() {
        super.onStart()

//        val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//        val editor = sharedPref.edit()

//        editor.putString("USER", "Serbastian")
//        editor.putString("EMAIL", "sebastian@higa.com")
//        editor.apply()

        db = AppDatabase.getAppDataBase(v.context)
        userDao = db?.UserDao()

//        adapter = ItemAdapter(itemList) { item ->
//            val action = FragmentMainDirections.actionFragmentMainToFragmentCreation(item.id)
//            v.findNavController().navigate(action)
//        }
//
//        userDao?.insert(User(1, "Sebastian","pass","sebastian@higa.com"))
//
//        recycleView.layoutManager = LinearLayoutManager(requireContext())
//        recycleView.adapter = adapter
//
//        itemList = itemDao?.loadAll() as MutableList<Item>

        buttonLogin.setOnClickListener {
            Log.d("asdf", "aaaa")
            val action = FragmentLoginDirections.actionFragmentLogin2ToMainActivity()
            v.findNavController().navigate(action)
            activity?.finish()
        }
    }
}