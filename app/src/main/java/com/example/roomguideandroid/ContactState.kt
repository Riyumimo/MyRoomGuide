package com.example.roomguideandroid

data class ContactState (
    val contacts: List<Contact> = emptyList(),
    val firstName : String = "",
    val lasttName : String = "",
    val phoneNumber  : String = "",
    val isAddingContact : Boolean = false,
    val sortType : SortType = SortType.FIRST_NAME
        )