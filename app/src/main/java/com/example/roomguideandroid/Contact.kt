package com.example.roomguideandroid

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact (
    @PrimaryKey(autoGenerate = true)
    val id :Int? = null,
    val firstName:String,
    val lastName:String,
    val PhoneNumber:String
)