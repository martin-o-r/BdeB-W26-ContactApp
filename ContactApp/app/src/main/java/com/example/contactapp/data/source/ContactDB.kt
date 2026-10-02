package com.example.contactapp.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.contactapp.data.models.Contact

@Database(
    entities = [Contact::class],
    version = 1,
    exportSchema = false
)
abstract class ContactDB : RoomDatabase() {
    abstract fun contactDoa(): ContactDAO

    companion object {
        @Volatile
        private var Instance: ContactDB? = null

        fun getInstance(context: Context): ContactDB =
            Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    ContactDB::class.java,
                    "contact_db"
                ).build().also {Instance = it}
            }
    }
}