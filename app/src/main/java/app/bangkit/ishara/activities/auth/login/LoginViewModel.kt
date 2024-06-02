package app.bangkit.ishara.activities.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.bangkit.ishara.data.preferences.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: UserPreference) : ViewModel() {
    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    fun login(username: String, password: String) {
        if (username == "admin" && password == "password") {
            viewModelScope.launch {
                pref.saveUserLoginStatus(true)
                _isLoggedIn.postValue(true)
            }
        } else {
            _isLoggedIn.value = false
        }
    }
}
