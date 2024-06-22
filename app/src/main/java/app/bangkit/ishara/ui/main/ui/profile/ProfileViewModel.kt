package app.bangkit.ishara.ui.main.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.bangkit.ishara.data.responses.profile.ProfileResponse
import app.bangkit.ishara.data.retrofit.ApiConfig
import app.bangkit.ishara.data.retrofit.ApiService
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _profileData = MutableLiveData<ProfileResponse>()
    val profileData: LiveData<ProfileResponse> get() = _profileData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _text = MutableLiveData<String>().apply {
        value = "This is profile Fragment"
    }
    val text: LiveData<String> = _text

    fun fetchUserProfile(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService().getProfile("Bearer $token")
                _profileData.value = response
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

}