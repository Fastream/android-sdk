package io.fastream.sdk.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
internal interface EventDao {
    @Query("SELECT * FROM EventEntity")
    fun findAllEvents(): List<EventEntity>
    @Insert
    fun insertAll(events: List<EventEntity>)
    @Delete
    fun deleteAll(events: List<EventEntity>)
}
