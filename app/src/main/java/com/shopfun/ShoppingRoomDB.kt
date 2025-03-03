package com.shopfun

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [Product::class, Order::class], version = 2, exportSchema = false)
abstract class ShoppingRoomDB : RoomDatabase() {

    abstract fun daoProduct(): ProductDAO
    abstract fun daoOrder(): OrderDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
//        private var dbinstance: ShoppingRoomDB? = null
        @Volatile
        private var INSTANCE: ShoppingRoomDB? = null

        fun getDatabase(context: Context): ShoppingRoomDB {

            val migration = Migration(1, 2){
                //it
            }

            // if the INSTANCE is not null, then return it,
            // if it is, then create the database

/*
            if(dbinstance == null)
            {
                dbinstance = Room.databaseBuilder(context, ShoppingRoomDB::class.java, "ShoppingRoomDB").addMigrations(migration).build()
            }
            return dbinstance!!
*/

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShoppingRoomDB::class.java,
                    "ShoppingRoomDB"
                ).addMigrations(migration).build()
                INSTANCE = instance
                // return instance
                instance
            }

        }
    }
}