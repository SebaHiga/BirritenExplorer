package com.higa.birritenexplorer.database

import androidx.room.*
import com.higa.birritenexplorer.entities.User

@Dao
public interface UserDao {

    @Query("SELECT * FROM users ORDER BY id")
    fun loadAll(): MutableList<User?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User?)

    @Update
    fun update(user: User?)

    @Delete
    fun delete(user: User?)

    @Query("SELECT * FROM users WHERE id = :id")
    fun loadById(id: Int): User?

    @Query("SELECT * FROM users WHERE name = :name")
    fun loadByName(name: String): User?
}