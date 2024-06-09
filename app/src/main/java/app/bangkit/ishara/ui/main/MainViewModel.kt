package app.bangkit.ishara.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import app.bangkit.ishara.data.preferences.UserPreference
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUserLoginStatus(): LiveData<Boolean> {
        return pref.getUserLoginStatus().asLiveData()
    }

    suspend fun saveUserLoginStatus(isLogin: Boolean) {
        viewModelScope.launch {
            pref.saveUserLoginStatus(isLogin)
        }
    }
}