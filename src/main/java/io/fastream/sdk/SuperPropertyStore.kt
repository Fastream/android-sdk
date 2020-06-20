package io.fastream.sdk

import io.fastream.sdk.db.FastreamDb
import io.fastream.sdk.db.SuperPropertyEntity
import java.util.concurrent.Executor

internal class SuperPropertyStore (
    private val db: FastreamDb,
    private val executor: Executor
) {

    private val cached = mutableMapOf<String, String>()

    fun findAll(callback: (Map<String, String>) -> Unit) {
        if (cached.isNotEmpty()) {
            callback(cached.toMap())
            return
        }
        executor.execute {
            val superProperties = db.superPropertiesDao().findAll()
            cached.clear()
            superProperties.forEach { cached[it.key] = it.value }
            callback(cached.toMap())
        }
    }

    fun add(key: String, value: String) {
        executor.execute {
            val superProperty = SuperPropertyEntity(id = null, key = key, value = value)
            db.superPropertiesDao().insertAll(listOf(superProperty))
            cached.clear()
        }
    }

    fun remove(key: String) {
        executor.execute {
            db.superPropertiesDao().deleteByKey(key)
            cached.clear()
        }
    }

    fun clear() {
        executor.execute {
            db.superPropertiesDao().deleteAll()
            cached.clear()
        }
    }

}