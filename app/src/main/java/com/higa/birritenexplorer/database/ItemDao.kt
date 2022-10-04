//package com.ort.roomdatabaseexample.database
//
//import androidx.room.*
//import com.higa.birritenexplorer.entities.Item
//
//@Dao
//public interface userDao {
//
//    @Query("SELECT * FROM users ORDER BY id")
//    fun loadAllPersons(): MutableList<Item?>?
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertPerson(user: Item?)
//
//    @Update
//    fun updatePerson(user: Item?)
//
//    @Delete
//    fun delete(user: Item?)
//
//    @Query("SELECT * FROM users WHERE id = :id")
//    fun loadPersonById(id: Int): Item?
//
//}