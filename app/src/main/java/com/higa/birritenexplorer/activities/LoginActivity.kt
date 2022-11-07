package com.higa.birritenexplorer.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.navigation.ui.NavigationUI
import com.facebook.CallbackManager
import com.higa.birritenexplorer.R
import com.higa.birritenexplorer.database.AppDatabase
import com.higa.birritenexplorer.database.ItemDao
import com.higa.birritenexplorer.database.UserDao


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}