package io.fastream.sdk

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

internal interface ApiService {
    @POST("inputs/api/{token}")
    fun sendEvents(@Path("token") token: String, @Body events: RequestBody): Call<Unit>
}
