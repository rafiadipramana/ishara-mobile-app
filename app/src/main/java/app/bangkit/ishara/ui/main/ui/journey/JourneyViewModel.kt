package app.bangkit.ishara.ui.main.ui.journey

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JourneyViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is journey Fragment"
    }
    val text: LiveData<String> = _text
}