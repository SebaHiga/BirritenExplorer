package com.higa.birritenexplorer.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.higa.birritenexplorer.entities.Item
import com.higa.birritenexplorer.entities.User


@Database(entities = [User::class, Item::class], version = 1, exportSchema = false)

public  abstract class AppDatabase : RoomDatabase() {

    abstract fun UserDao(): UserDao
    abstract fun ItemDao(): ItemDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "myDB"
                    ).allowMainThreadQueries().build() // No es lo mas recomendable que se ejecute en el mainthread
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}