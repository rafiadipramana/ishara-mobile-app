package app.bangkit.ishara.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import app.bangkit.ishara.R
import app.bangkit.ishara.ui.auth.register.RegisterActivity
import app.bangkit.ishara.ui.main.MainActivity
import app.bangkit.ishara.data.preferences.UserPreference
import app.bangkit.ishara.data.preferences.dataStore
import app.bangkit.ishara.databinding.ActivityLoginBinding
import app.bangkit.ishara.utils.ViewModelFactory

class LoginActivity : AppCompatActivity(), View.OnClickListener, View.OnTouchListener {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private var isPasswordVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreference.getInstance(application.dataStore)
        loginViewModel = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]

        loginViewModel.isLoggedIn.observe(this) { isLoggedIn ->
            if (isLoggedIn) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        loginViewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonLogin.setOnClickListener(this)
        binding.textViewButtonRegister.setOnClickListener(this)
        binding.editTextPassword.setOnTouchListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonLogin -> {
                val email = binding.editTextEmail.text.toString()
                val password = binding.editTextPassword.text.toString()

                if (email.isEmpty()) {
                    binding.editTextEmail.error = "Email tidak boleh kosong"
                    return
                }

                if (password.isEmpty()) {
                    binding.editTextPassword.error = "Password tidak boleh kosong"
                    return
                }

                if (password.length < 8) {
                    binding.editTextPassword.error = "Password minimal 8 karakter"
                    return
                }

                loginViewModel.login(email, password)
            }

            R.id.textViewButtonRegister -> {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val drawableEndIndex = 2;
        if (event?.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= binding.editTextPassword.right - binding.editTextPassword.compoundDrawables[drawableEndIndex].bounds.width() - binding.editTextPassword.paddingEnd) {
                togglePasswordVisibility()
                return true
            }
        }
        return false
    }

    private fun togglePasswordVisibility() {
        val startDrawable = ContextCompat.getDrawable(this, R.drawable.ic_password_baseline24)
        val endDrawable = if (isPasswordVisible) {
            ContextCompat.getDrawable(this, R.drawable.ic_show_baseline24)
        } else {
            ContextCompat.getDrawable(this, R.drawable.ic_hide_baseline24)
        }

        if (isPasswordVisible) {
            // Hide password
            binding.editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            // Show password
            binding.editTextPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }

        isPasswordVisible = !isPasswordVisible

        // Set the drawables
        binding.editTextPassword.setCompoundDrawablesWithIntrinsicBounds(
            startDrawable,
            null,
            endDrawable,
            null
        )

        // Move the cursor to the end of the text
        binding.editTextPassword.setSelection(binding.editTextPassword.text.length)
    }
}