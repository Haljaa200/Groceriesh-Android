package com.haljaa200.groceriesh.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.haljaa200.groceriesh.db.daos.BasketDao
import com.haljaa200.groceriesh.models.OrderItem
import com.haljaa200.groceriesh.util.Constants

@Database(
    entities = [OrderItem::class, ],
    version = 1
)
abstract class GrocerieshDatabase : RoomDatabase() {

    abstract fun getBasketDao(): BasketDao

    companion object {
        @Volatile
        private var instance: GrocerieshDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                GrocerieshDatabase::class.java,
                Constants.DB_FULL_NAME
            ).setJournalMode(JournalMode.TRUNCATE).build()
    }
}