package app.bangkit.ishara.ui.main.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.bangkit.ishara.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _completedAlphabet = MutableLiveData<String>()
    val completedAlphabet: LiveData<String> = _completedAlphabet

    init {
        _completedAlphabet.value = "This is home fragment"
    }

    fun refreshAccessToken(accessToken: String) {
        viewModelScope.launch {
            val response = ApiConfig.getApiService().refreshToken(accessToken)
            if (response.meta?.success == true) {
                Log.d(TAG, "refrehAccessToken: ${response.data?.token}")
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}