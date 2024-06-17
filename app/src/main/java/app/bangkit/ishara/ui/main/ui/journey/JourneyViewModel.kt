package app.bangkit.ishara.ui.main.ui.journey

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.bangkit.ishara.data.models.Item
import app.bangkit.ishara.data.preferences.UserPreference
import app.bangkit.ishara.data.preferences.dataStore
import app.bangkit.ishara.data.requests.LoginRequest
import app.bangkit.ishara.data.responses.journey.DataItem
import app.bangkit.ishara.data.responses.login.error.LoginErrorResponse
import app.bangkit.ishara.data.retrofit.ApiConfig
import app.bangkit.ishara.ui.auth.login.LoginViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch

class JourneyViewModel : ViewModel() {
    private val _journeyList = MutableLiveData<List<Item>>()
    val journeyList: LiveData<List<Item>> = _journeyList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getJourney(jwtAccessToken: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getAllJourney(jwtAccessToken)
                if (response.meta.success) {
                    Log.d(TAG, "Get Journey success")
                    val journeyItems = mutableListOf<Item>()
                    response.data.forEach { stagesData ->
                        Log.d(TAG, "Stages data: $stagesData")
                        journeyItems.add(Item.StageItem(stagesData.copy()))
                        Log.d(TAG, "Journey items: $journeyItems")
                        journeyItems.addAll(stagesData.levels.map { levelData -> Item.LevelItem(levelData.copy()) })
                    }
                    _journeyList.postValue(journeyItems) // Use postValue to update LiveData from a background thread
                    Log.d(TAG, "Journey list: ${_journeyList.value}")
                } else {
                    // Handle API error
                    _errorMessage.postValue(response.meta.message)
                }
            } catch (e: Exception) {
                // Handle network or other errors
                Log.e(TAG, "Error fetching journeys", e)
                _errorMessage.postValue("Error fetching journeys: ${e.message}")
            } finally {
                _isLoading.postValue(false) // Ensure _isLoading is set to false in the finally block
            }

        }
    }

    companion object {
        private const val TAG = "JourneyViewModel"
    }

//    private val _items = MutableLiveData<List<Item>>().apply {
//        value = listOf(
//            Item.JourneyItem("Abjad 1", "Belajar isyarat untuk\nabjad a,b,c,d,e"),
//            Item.LevelItem("Level 1", 3, true),
//            Item.LevelItem("Level 2", 2, true),
//            Item.LevelItem("Level 3", 4, true),
//            Item.LevelItem("Level 4", 3, true),
//            Item.LevelItem("Level 5", 5, false),
//            Item.JourneyItem("Abjad 2", "Belajar isyarat untuk\nabjad e,f,g,h,i"),
//            Item.LevelItem("Level 1", 5, false),
//            Item.LevelItem("Level 2", 3, false),
//            Item.LevelItem("Level 3", 1, false),
//            Item.LevelItem("Level 4", 2, false),
//            Item.LevelItem("Level 5", 4, false),
//        )
//    }
//    val items: LiveData<List<Item>> = _items
}
