package com.example.roomguideandroid

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
class ContactViewModel(private val dao : ContactDao) : ViewModel() {
    private val _state = MutableStateFlow(ContactState())
    private val _sorttype = MutableStateFlow(SortType.FIRST_NAME)
    private val _contacts = _sorttype.flatMapLatest { sortType ->
        when (sortType) {
            SortType.FIRST_NAME -> dao.getContactsOrderByfirstName()
            SortType.LAST_NAME -> dao.getContactsOrderByLastName()
            SortType.PHONE_NUMBER -> dao.getContactsOrderByPhoneNumber()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _sorttype, _contacts) { state, sortType, contacts ->
        state.copy(contacts = contacts, sortType = sortType)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ContactState())


    fun onEvent(event : ContactEvent) {
        when (event) {
            is ContactEvent.DeleteContacts -> {
                viewModelScope.launch {

                    dao.deleteContact(event.contact)
                }
            }
            ContactEvent.HideDialog -> {
                _state.update {
                    it.copy(isAddingContact = false)
                }
            }
            ContactEvent.SaveContact -> {
                val fisrtName = state.value.firstName
                val lastName = state.value.lasttName
                val phoneNumber = state.value.phoneNumber

                if(fisrtName.isBlank() || lastName.isBlank() || phoneNumber.isBlank()) {
                    return
                }
                val contact =
                    Contact(firstName = fisrtName, lastName = lastName, PhoneNumber = phoneNumber)
                viewModelScope.launch {
                    dao.upsertContact(contact)
                }
                _state.update {
                    it.copy(
                        isAddingContact = false,
                        firstName = "",
                        lasttName = "",
                        phoneNumber = ""
                    )
                }
            }
            is ContactEvent.SetFirstName -> {
                _state.update {
                    it.copy(firstName = event.firstName)
                }
            }
            is ContactEvent.SetLastName -> {
                _state.update {
                    it.copy(lasttName = event.lastName)
                }
            }
            is ContactEvent.SetPhoneNumber -> {
                _state.update {
                    it.copy(phoneNumber = event.phoneNumber)
                }
            }
            ContactEvent.ShowDialog -> {
                _state.update {
                    it.copy(isAddingContact = true)
                }
            }
            is ContactEvent.SortContacts -> {
                _sorttype.value = event.sortType
            }
        }
    }

}