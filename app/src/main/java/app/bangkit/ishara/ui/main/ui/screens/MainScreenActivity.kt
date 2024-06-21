package app.bangkit.ishara.ui.main.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import app.bangkit.ishara.R
import app.bangkit.ishara.data.preferences.UserPreference
import app.bangkit.ishara.data.preferences.dataStore
import app.bangkit.ishara.data.retrofit.ApiConfig
import app.bangkit.ishara.databinding.ActivityMainScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var userPreference: UserPreference

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            refreshAccessToken()
        }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main2)
        navView.setupWithNavController(navController)
    }

    private suspend fun refreshAccessToken() {
        userPreference = UserPreference.getInstance(application.dataStore)

        try {
            // Collect the current access token from the Flow
            val currentAccessToken = userPreference.getJwtAccessToken().first()

            // Making the network request to refresh the token
            val response = ApiConfig.getApiService().refreshToken("Bearer $currentAccessToken")
            if (response.meta?.success == true) {
                val body = response.data
                if (body != null) {
                    Log.e(TAG, "refreshAccessToken: ${body.token}")
                    body.token?.let { userPreference.saveJwtAccessToken(it) }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "MainScreenActivity"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}