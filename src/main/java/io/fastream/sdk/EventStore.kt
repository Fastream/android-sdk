package io.fastream.sdk

import com.google.gson.JsonObject
import io.fastream.sdk.db.FastreamDb
import io.fastream.sdk.db.EventEntity
import java.util.concurrent.Executor
import java.util.concurrent.Executors

private const val DEFAULT_BATCH_SIZE = 50

internal class EventStore(
    private val db: FastreamDb,
    private val executor: Executor
) {

    fun add(eventPayload: JsonObject) {
        executor.execute {
            db.eventDao().insertAll(listOf(EventEntity(null, eventPayload.toString())))
        }
    }

    fun findAll(batchSize: Int = DEFAULT_BATCH_SIZE, callback: (List<EventEntity>) -> Unit) {
        executor.execute {
            callback(db.eventDao().findAll(batchSize))
        }
    }

    fun deleteAll(events: List<EventEntity>) {
        executor.execute {
            db.eventDao().deleteAll(events)
        }
    }

}
