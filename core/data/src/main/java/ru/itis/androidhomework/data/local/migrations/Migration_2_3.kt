package ru.itis.androidhomework.data.local.migrations

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.itis.androidhomework.data.local.AppDatabase

class Migration_2_3: Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {
        try {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS push_events (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    event TEXT NOT NULL,
                    city TEXT NOT NULL,
                    timestamp INTEGER NOT NULL
                )
                """
            )
        } catch (ex: Exception) {
            Log.e(AppDatabase.DB_LOG_KEY, "Error during migration 2_3: ${ex.message}")
        }
    }
}
