package io.fastream.sdk

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.google.gson.JsonObject
import io.fastream.sdk.db.EventDb
import io.fastream.sdk.db.EventEntity
import java.util.concurrent.Executor
import java.util.concurrent.Executors


internal class EventStore(
    private val context: Context
) {

    private val executor: Executor by lazy {
        Executors.newSingleThreadExecutor()
    }

    private val db: EventDb by lazy {
        Room.databaseBuilder(context, EventDb::class.java, "fastream-event-database")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun add(eventPayload: JsonObject) {
        executor.execute {
            db.eventDao().insertAll(listOf(EventEntity(null, eventPayload.asString)))
        }
    }

    fun findAll(callback: (List<EventEntity>) -> Unit) {
        executor.execute {
            callback(db.eventDao().findAllEvents())
        }
    }

    fun deleteAll(events: List<EventEntity>) {
        executor.execute {
            db.eventDao().deleteAll(events)
        }
    }

}
