package ru.itis.androidhomework.data.local.migrations

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.itis.androidhomework.data.local.AppDatabase

class Migration_1_2: Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {
        try {
            db.execSQL(
                " CREATE TABLE IF NOT EXISTS users (id TEXT NOT NULL," +
                        "                user_name TEXT NOT NULL,\n" +
                        "                email TEXT NOT NULL,\n" +
                        "                password TEXT NOT NULL,\n" +
                        "                PRIMARY KEY(id)\n" +
                        "            )"
            )
        } catch (ex: Exception) {
            Log.e(AppDatabase.DB_LOG_KEY, "Error while 1_2 migration: ${ex.message}")
        }
    }
}