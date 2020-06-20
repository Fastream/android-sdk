package io.fastream.sdk.db

import androidx.room.*

@Dao
internal interface SuperPropertyDao {
    @Query("SELECT * FROM SuperPropertyEntity")
    fun findAll(): List<SuperPropertyEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(superProperties: List<SuperPropertyEntity>)
    @Query("DELETE FROM SuperPropertyEntity")
    fun deleteAll()
    @Query("DELETE FROM SuperPropertyEntity WHERE `key` = :key")
    fun deleteByKey(key: String)
}