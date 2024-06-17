package app.bangkit.ishara.data.retrofit

import app.bangkit.ishara.data.requests.LoginRequest
import app.bangkit.ishara.data.requests.RegisterRequest
import app.bangkit.ishara.data.responses.auth.RefreshTokenResponse
import app.bangkit.ishara.data.responses.journey.JourneyResponse
import app.bangkit.ishara.data.responses.login.LoginResponse
import app.bangkit.ishara.data.responses.register.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
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

    @GET("api/v1/auth/refresh-token")
    suspend fun refreshToken(
        @Header("Authorization") token: String
    ): RefreshTokenResponse

    @GET("api/v1/user/journey/stages")
    suspend fun getAllJourney(
        @Header("Authorization") token: String
    ): JourneyResponse


}