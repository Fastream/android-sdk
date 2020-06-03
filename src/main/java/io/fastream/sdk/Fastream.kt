package io.fastream.sdk

import android.app.Application
import android.content.Context
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class Fastream(
    url: String,
    private val token: String,
    private val context: Context
) {

    private val service = Retrofit.Builder().baseUrl(url.let { if (it.startsWith("http://") || it.startsWith("https://")) it else "https://$url" }).build().create(ApiService::class.java)
    private val mediaType = MediaType.parse("application/json")
    private val eventStore = EventStore(context)
    private val eventFactory = EventFactory(context)

    fun flush() {
        eventStore.findAll { events ->
            if (events.isEmpty()) { return@findAll }
            val requestBody = RequestBody.create(mediaType, "[${events.joinToString(",") { it.payload }}]")
            val call = service.sendEvents(token = token, events = requestBody)
            call.enqueue(SendEventsCallback(
                onSuccess = { eventStore.deleteAll(events) },
                onError = {  }
            ))
        }
    }

    fun track(eventName: String, properties: JsonObject) {
        eventStore.add(eventFactory.create(eventName, properties))
    }

    companion object {
        fun init(url: String, token: String, context: Context) = Fastream(url, token, context)
    }

}

private class SendEventsCallback(
    private val onSuccess: () -> Unit,
    private val onError: (Throwable) -> Unit
) : Callback<Unit> {

    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
        if (response.isSuccessful) {
            onSuccess()
        } else {
            onError(FastreamException("Cannot send events to fastream (code=${response.code()})"))
        }
    }

    override fun onFailure(call: Call<Unit>, t: Throwable) {
        onError(FastreamException("Cannot send events to fastream",t))
    }

}

class FastreamException(message: String, throwable: Throwable? = null) : RuntimeException(message, throwable)
