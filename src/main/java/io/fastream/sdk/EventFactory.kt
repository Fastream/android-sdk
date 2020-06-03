package io.fastream.sdk

import android.content.Context
import com.google.gson.JsonObject

internal class EventFactory(context: Context? = null) {

    fun create(eventName: String, properties: JsonObject): JsonObject {
        val enhancedEvent = properties.deepCopy()
        enhancedEvent.addProperty("event", eventName)
        val metadata = enhancedEvent.getOrCreateJsonObject("_metadata")
        enhancedEvent.add("_metadata", metadata)
        return enhancedEvent
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
