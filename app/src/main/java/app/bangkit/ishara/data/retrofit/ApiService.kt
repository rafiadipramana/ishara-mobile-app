package app.bangkit.ishara.data.retrofit

import app.bangkit.ishara.data.requests.LoginRequest
import app.bangkit.ishara.data.requests.RegisterRequest
import app.bangkit.ishara.data.responses.login.LoginResponse
import app.bangkit.ishara.data.responses.register.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("api/v1/auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse
}