package io.fastream.sdk

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.google.gson.JsonObject
import io.fastream.sdk.db.FastreamDb
import java.util.concurrent.Executor
import java.util.concurrent.Executors

private const val LOGTAG = "Fastream"

class Fastream(
    url: String,
    private val token: String,
    private val context: Context,
    private val flushOnStart: Boolean
) {

    private val executor: Executor = Executors.newSingleThreadExecutor()

    private val db: FastreamDb = Room.databaseBuilder(context, FastreamDb::class.java, "fastream-event-database")
        .fallbackToDestructiveMigration()
        .build()

    private val eventFactory = EventFactory(context)
    private val eventStore = EventStore(db, executor)
    private val eventSender = EventSender(url = url, token = token)

    private val superPropertiesStore = SuperPropertyStore(db, executor)

    init { if (flushOnStart) { flush() } }

    fun flush() {
        eventStore.findAll { events ->
            eventSender.send(
                events = events.map { it.payload },
                onSuccess = { eventStore.deleteAll(events) },
                onError = { Log.w(LOGTAG, "Cannot flush events", it) }
            )
        }
    }

    fun track(eventName: String, properties: JsonObject) {
        superPropertiesStore.findAll { superProperties ->
            eventStore.add(eventFactory.create(eventName, properties, superProperties))
        }
    }

    fun setSuperProperty(key: String, value: String) {
        superPropertiesStore.add(key, value)
    }

    fun removeSuperProperty(key: String) {
        superPropertiesStore.remove(key)
    }

    fun clearSuperProperties() {
        superPropertiesStore.clear()
    }

    companion object {
        fun init(url: String, token: String, context: Context, flushOnStart: Boolean = true) = Fastream(url, token, context, flushOnStart)
    }

}

