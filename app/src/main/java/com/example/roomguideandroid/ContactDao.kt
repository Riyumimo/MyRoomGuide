package com.example.roomguideandroid

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Upsert
    suspend fun upsertContact(contact : Contact)

    @Delete
    suspend fun deleteContact(contact : Contact)

    @Query("SELECT * FROM contact ORDER BY firstName ASC")
    fun getContactsOrderByfirstName(): Flow<List<Contact>>

    @Query("SELECT * FROM contact ORDER BY lastName ASC")
    fun getContactsOrderByLastName(): Flow<List<Contact>>


    @Query("SELECT * FROM contact ORDER BY PhoneNumber ASC")
    fun getContactsOrderByPhoneNumber(): Flow<List<Contact>>
}
