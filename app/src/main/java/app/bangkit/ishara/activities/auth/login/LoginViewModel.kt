package app.bangkit.ishara.activities.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.bangkit.ishara.data.preferences.UserPreference
import app.bangkit.ishara.data.requests.LoginRequest
import app.bangkit.ishara.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: UserPreference) : ViewModel() {
    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(username, password)
                val response = ApiConfig.getApiService().login(loginRequest)
            } catch (e: Exception) {

            }
        }
    }
}
