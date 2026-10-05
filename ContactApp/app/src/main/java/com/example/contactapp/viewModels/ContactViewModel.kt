package com.example.contactapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactapp.data.models.Contact
import com.example.contactapp.data.source.ContactDAO
import com.example.contactapp.data.source.ContactDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ContactViewModel(application: Application): AndroidViewModel(application) {
    private val dao: ContactDAO =
        ContactDB.getInstance(application.applicationContext).contactDoa()

    val contacts : StateFlow<List<Contact>> = dao.getAllContacts().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addContact(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        dao.insert(contact)
    }

    fun updateContact(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        dao.update(contact)
    }

    fun deleteContact(contact: Contact) = viewModelScope.launch(Dispatchers.IO) {
        dao.delete(contact)
    }

    fun getContact(contactId: Int) = viewModelScope.launch(Dispatchers.IO) {
        dao.getContactById(contactId)
    }
}