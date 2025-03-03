package com.shopfun

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

class CPContentProvider : ContentProvider() {
    companion object {
        const val AUTHORITY = "com.shopfun.ContentProvider"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/quotes")
    }

    private lateinit var database: SQLiteDatabase

    override fun onCreate(): Boolean {
        val dbHelper = SQLLiteDBHelperQuote(context!!)
        database = dbHelper.writableDatabase
        return true
    }

    // Handle query requests
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return database.query("tblquotes", projection, selection, selectionArgs, null, null, sortOrder)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = database.insert("tblquotes", null, values)
        return Uri.withAppendedPath(CONTENT_URI, id.toString())
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        return database.update("tblquotes", values, selection, selectionArgs)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return database.delete("tblquotes", selection, selectionArgs)
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.dir/vnd.$AUTHORITY.tblquotes"
    }
}

