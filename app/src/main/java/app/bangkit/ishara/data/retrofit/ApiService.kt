package app.bangkit.ishara.data.retrofit

import app.bangkit.ishara.data.requests.LoginRequest
import app.bangkit.ishara.data.responses.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse
}