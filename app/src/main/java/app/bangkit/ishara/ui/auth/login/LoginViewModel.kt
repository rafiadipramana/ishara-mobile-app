package app.bangkit.ishara.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.bangkit.ishara.data.preferences.UserPreference
import app.bangkit.ishara.data.requests.LoginRequest
import app.bangkit.ishara.data.responses.login.error.LoginErrorResponse
import app.bangkit.ishara.data.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: UserPreference) : ViewModel() {
    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun login(username: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(username, password)
                val response = ApiConfig.getApiService().login(loginRequest)
                if (response.meta.success) {
                    pref.saveUserLoginStatus(true)
                    _isLoggedIn.value = true
                }
                _isLoading.value = false
            } catch (e: Exception) {
                if (e is retrofit2.HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, LoginErrorResponse::class.java)
                    _errorMessage.value = "Login failed: ${errorResponse.meta.error}"
                } else {
                    _errorMessage.value = "Login failed: ${e.message}"
                }
                _isLoading.value = false
            }
        }
    }
}
