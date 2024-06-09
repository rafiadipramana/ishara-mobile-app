package app.bangkit.ishara.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.bangkit.ishara.data.requests.RegisterRequest
import app.bangkit.ishara.data.responses.register.error.RegisterErrorResponse
import app.bangkit.ishara.data.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _isRegistered = MutableLiveData<Boolean>()
    val isRegistered: LiveData<Boolean> = _isRegistered

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun register(username: String, email: String, password: String, confirmPassword: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val registerRequest = RegisterRequest(username, email, password, confirmPassword)
                val response = ApiConfig.getApiService().register(registerRequest)
                if (response.meta.success) {
                    _isRegistered.value = true
                }
                _isLoading.value = false
            } catch (e: Exception) {
                if (e is retrofit2.HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, RegisterErrorResponse::class.java)
                    _errorMessage.value = "Register failed: ${errorResponse.meta.error[0]}"
                } else {
                    _errorMessage.value = "Register failed: ${e.message}"
                }
                _isLoading.value = false
            }
        }
    }
}