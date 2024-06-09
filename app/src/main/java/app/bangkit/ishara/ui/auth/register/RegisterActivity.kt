package app.bangkit.ishara.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import app.bangkit.ishara.R
import app.bangkit.ishara.ui.auth.login.LoginActivity
import app.bangkit.ishara.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(), View.OnClickListener, View.OnTouchListener {

    private lateinit var binding: ActivityRegisterBinding
    private var isPasswordVisible: Boolean = false
    private var isConfirmPasswordVisible: Boolean = false
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
        binding.editTextPassword.setOnTouchListener(this)
        binding.editTextConfirmPassword.setOnTouchListener(this)
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

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v?.id) {
            R.id.editTextPassword -> {
                val drawableEndIndex = 2;
                if (event?.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= binding.editTextPassword.right - binding.editTextPassword.compoundDrawables[drawableEndIndex].bounds.width() - binding.editTextPassword.paddingEnd) {
                        togglePasswordVisibility(binding.editTextPassword)
                        return true
                    }
                }
                return false
            }
            R.id.editTextConfirmPassword -> {
                val drawableEndIndex = 2;
                if (event?.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= binding.editTextPassword.right - binding.editTextPassword.compoundDrawables[drawableEndIndex].bounds.width() - binding.editTextPassword.paddingEnd) {
                        togglePasswordVisibility(binding.editTextConfirmPassword)
                        return true
                    }
                }
                return false
            }
            else -> return false
        }
    }

    private fun togglePasswordVisibility(editText: EditText) {
        when (editText) {
            binding.editTextPassword -> {
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

            binding.editTextConfirmPassword -> {
                val startDrawable = ContextCompat.getDrawable(this, R.drawable.ic_password_baseline24)
                val endDrawable = if (isPasswordVisible) {
                    ContextCompat.getDrawable(this, R.drawable.ic_show_baseline24)
                } else {
                    ContextCompat.getDrawable(this, R.drawable.ic_hide_baseline24)
                }

                if (isPasswordVisible) {
                    // Hide password
                    binding.editTextConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                } else {
                    // Show password
                    binding.editTextConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                }

                isConfirmPasswordVisible = !isConfirmPasswordVisible

                // Set the drawables
                binding.editTextConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(
                    startDrawable,
                    null,
                    endDrawable,
                    null
                )

                // Move the cursor to the end of the text
                binding.editTextConfirmPassword.setSelection(binding.editTextConfirmPassword.text.length)
            }
            else -> return
        }
    }
}