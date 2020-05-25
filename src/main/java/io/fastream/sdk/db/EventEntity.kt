package io.fastream.sdk.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class EventEntity (
    @PrimaryKey val id: Int?,
    @ColumnInfo(name = "payload") val payload: String
)
