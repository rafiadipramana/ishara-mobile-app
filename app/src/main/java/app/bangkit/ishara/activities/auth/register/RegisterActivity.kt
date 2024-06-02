package app.bangkit.ishara.activities.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.bangkit.ishara.R
import app.bangkit.ishara.activities.auth.login.LoginActivity
import app.bangkit.ishara.activities.main.MainActivity
import app.bangkit.ishara.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel.isRegistered.observe(this) { isRegistered ->
            if (isRegistered) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        registerViewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonRegister.setOnClickListener(this)
        binding.textViewButtonLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.buttonRegister -> {
                if (binding.editTextUsername.text.toString().isEmpty()) {
                    binding.editTextUsername.error = "Username tidak boleh kosong"
                    return
                }

                if (binding.editTextEmail.text.toString().isEmpty()) {
                    binding.editTextEmail.error = "Email tidak boleh kosong"
                    return
                }

                if (binding.editTextPassword.text.toString().isEmpty()) {
                    binding.editTextPassword.error = "Password tidak boleh kosong"
                    return
                }

                if (binding.editTextConfirmPassword.text.toString().isEmpty()) {
                    binding.editTextConfirmPassword.error = "Konfirmasi password tidak boleh kosong"
                    return
                }

                if (binding.editTextPassword.text.toString() != binding.editTextConfirmPassword.text.toString()) {
                    binding.editTextConfirmPassword.error = "Password tidak cocok"
                    return
                }

                registerViewModel.register(
                    binding.editTextUsername.text.toString(),
                    binding.editTextEmail.text.toString(),
                    binding.editTextPassword.text.toString(),
                    binding.editTextConfirmPassword.text.toString()
                )
            }

            R.id.textViewButtonLogin -> {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}