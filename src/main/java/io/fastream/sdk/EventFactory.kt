package io.fastream.sdk

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.util.*


internal class EventFactory(private val context: Context) {

    private val sessionId: String by lazy { UUID.randomUUID().toString() }

    private val defaultPropertiesFactory = DefaultPropertiesFactory(context)

    private val gson = Gson()

    fun create(
        eventName: String,
        properties: Map<String, Any>,
        superProperties: Map<String, String>
    ): JsonObject {
        val eventObject = JsonObject()
        val eventProperties = JsonObject()

        superProperties.entries.forEach { (k, v) -> eventProperties.addProperty(k, v) }
        val defaultEventProperties = defaultPropertiesFactory.createDefaultEventProperties()
        defaultEventProperties.entrySet().forEach { (k, v) -> eventProperties.add(k, v) }
        eventProperties.addProperty("time", System.currentTimeMillis() / 1000)
        eventProperties.addProperty("session_id", sessionId)
        properties.entries.forEach { (k, v) -> eventProperties.add(k, gson.toJsonTree(v)) }
        eventObject.addProperty("event", eventName)
        eventObject.add("properties", eventProperties)
        return eventObject
    }

}

private fun JsonObject.getOrCreateJsonObject(memberName: String): JsonObject {
    if (this.has(memberName)) {
        val element = this.get(memberName)
        if (element.isJsonObject) {
            return element.asJsonObject
        }
    }
    val jsonObject = JsonObject()
    this.add(memberName, jsonObject)
    return jsonObject
}
