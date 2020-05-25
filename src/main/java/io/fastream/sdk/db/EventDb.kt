package io.fastream.sdk.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EventEntity::class], version = 1)
internal abstract class EventDb : RoomDatabase() {
    abstract fun eventDao(): EventDao
}
