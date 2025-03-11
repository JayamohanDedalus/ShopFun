package com.shopfun

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLLiteDBHelperQuote (context: android.content.Context) :
    SQLiteOpenHelper(context, "quotes.db", null, 1) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE tblquotes (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "quote TEXT NOT NULL)"
            )

            // Insert sample quotes
            db.execSQL("INSERT INTO tblquotes (quote) VALUES ('The only limit to our realization of tomorrow is our doubts of today.')")
            db.execSQL("INSERT INTO tblquotes (quote) VALUES ('Success is not final, failure is not fatal: It is the courage to continue that counts.')")
            db.execSQL("INSERT INTO tblquotes (quote) VALUES ('Believe you can and you are halfway there.')")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS tblquotes")
            onCreate(db)
        }
}