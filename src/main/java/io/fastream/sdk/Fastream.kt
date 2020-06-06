package io.fastream.sdk

import android.app.Application
import android.content.Context
import android.util.Log
import android.util.LogPrinter
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

private const val LOGTAG = "Fastream"

class Fastream(
    url: String,
    private val token: String,
    private val context: Context,
    private val flushOnStart: Boolean
) {

    private val eventFactory = EventFactory(context)
    private val eventStore = EventStore(context)
    private val eventSender = EventSender(url = url, token = token)

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
        eventStore.add(eventFactory.create(eventName, properties))
    }

    companion object {
        fun init(url: String, token: String, context: Context, flushOnStart: Boolean = true) = Fastream(url, token, context, flushOnStart)
    }

}

