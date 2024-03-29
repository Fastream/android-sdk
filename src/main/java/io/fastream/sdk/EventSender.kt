package io.fastream.sdk

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class EventSender (
    url: String,
    private val token: String
) {

    private val service = Retrofit.Builder().baseUrl(url.let { if (it.startsWith("http://") || it.startsWith("https://")) it else "https://$url" }).build().create(ApiService::class.java)
    private val mediaType = MediaType.parse("application/json")
    private var laseSentTime = 0L

    fun send(events: List<String>, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        if (events.isEmpty()) return
        addSendingTime(events)
        val requestBody = RequestBody.create(mediaType, "[${events.joinToString(",")}]")
        val call = service.sendEvents(token = token, events = requestBody)
        call.enqueue(SendEventsCallback(
            onSuccess = { onSuccess() },
            onError = { onError(it) }
        ))
    }

    private fun addSendingTime(events: List<String>) {
        events.forEach { event ->
            Gson().fromJson(event, JsonObject::class.java).getAsJsonObject("properties").addProperty(
                "sending_time",
                System.currentTimeMillis() / 1000
            )
        }
    }

    private fun addMessageIndex(events: List<String>) {
        events.forEachIndexed { index, event ->
            Gson().fromJson(event, JsonObject::class.java).getAsJsonObject("properties").addProperty(
                "message_index",
                index + 1
            )
        }
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
        onError(FastreamException("Cannot send events to fastream", t))
    }

}

