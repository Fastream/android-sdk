package io.fastream.sdk

import android.content.Context
import com.google.gson.JsonObject

internal class EventFactory(context: Context) {

    private val defaultPropertiesFactory = DefaultPropertiesFactory(context)

    fun create(eventName: String, properties: JsonObject): JsonObject {
        val eventObject = JsonObject()
        val eventProperties = JsonObject()

        val defaultEventProperties = defaultPropertiesFactory.createDefaultEventProperties()
        defaultEventProperties.entrySet().forEach { (k, v) -> eventProperties.add(k, v) }
        properties.entrySet().forEach { (k, v) -> eventProperties.add(k, v) }

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
