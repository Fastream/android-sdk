package io.fastream.sdk.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["key"], unique = true)])
internal data class SuperPropertyEntity (
    @PrimaryKey val id: Int?,
    @ColumnInfo(name = "key") val key: String,
    @ColumnInfo(name = "value") val value: String
)