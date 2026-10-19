/*
https://developer.android.com/training/data-storage/room/migrating-db-versions
    -> lorsqu'on modifie notre table, il faut aussi changer la version de notre DB
    -> par contre, il faut aussi specifier a Room le changemenet avec une migration
        -> soit on transfere vers une nouvelle table
        -> soit on cree une nouvelle table
        -> soit query pour modifier une colone de la table
    -> pour ce petit projet, chaque version creera sa propre nouvelle table
        -> donc la table de l'ancienne version sera effacee et une nouvelle sera produite
* */

package com.example.contactapp.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.contactapp.data.models.Contact

@Database(
    entities = [Contact::class],
    version = 2,
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
                )
                    .fallbackToDestructiveMigration(dropAllTables = true) // missing part for new version of DB
                    .build().also {Instance = it}
            }
    }
}