package app.bangkit.ishara.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import app.bangkit.ishara.R
import app.bangkit.ishara.data.preferences.UserPreference
import app.bangkit.ishara.data.preferences.dataStore
import app.bangkit.ishara.databinding.ActivityMainBinding
import app.bangkit.ishara.ui.auth.login.LoginActivity
import app.bangkit.ishara.ui.main.ui.screens.MainScreenActivity
import app.bangkit.ishara.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // Optional for future use

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        splashScreen.setSplashScreenTheme(R.style.Theme_App_Starting)

        val pref = UserPreference.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]

        mainViewModel.getUserLoginStatus().observe(this) { isLogin: Boolean ->
            if (isLogin) {
                val intent = Intent(this, MainScreenActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
