package io.fastream.sdk.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EventEntity::class, SuperPropertyEntity::class], version = 1)
internal abstract class FastreamDb : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun superPropertiesDao(): SuperPropertyDao
}
