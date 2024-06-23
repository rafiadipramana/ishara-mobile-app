package app.bangkit.ishara.ui.game

import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import app.bangkit.ishara.databinding.ActivityGameBinding
import app.bangkit.ishara.domain.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat
import java.util.concurrent.Executors

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var isHandDetected: Boolean = false
    private lateinit var quizLetter: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizLetter = intent.getStringExtra(QUIZ_LETTER_EXTRA_KEY) ?: ""
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun startCamera() {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            isHandDetectedListener = object : ImageClassifierHelper.IsHandDetectedListener {
                override fun onHandDetected() {
                    isHandDetected = true
                }

                override fun onNoHandDetected() {
                    isHandDetected = false
                }
            },
            imageClassifierListener = object : ImageClassifierHelper.ImageClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@GameActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        if (isHandDetected && !results.isNullOrEmpty() && results[0].categories.isNotEmpty()) {
                            val sortedCategories = results[0].categories.sortedByDescending { it.score }
                            checkLetter(sortedCategories[0].label[0])
                            val topThreeCategories = sortedCategories.take(3)
                            val displayResult = topThreeCategories.joinToString("\n") {
                                "${it.label} " + NumberFormat.getPercentInstance().format(it.score).trim()
                            }
                            binding.tvResult.text = displayResult
                            binding.tvInferenceTime.text = "$inferenceTime ms"
                        } else {
                            binding.tvResult.text = ""
                            binding.tvInferenceTime.text = ""
                        }
                    }
                }
            }
        )

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        val displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val display = displayManager.getDisplay(Display.DEFAULT_DISPLAY)
        val rotation = display?.rotation

        cameraProviderFuture.addListener({
            val resolutionSelector = ResolutionSelector.Builder()
                .setAspectRatioStrategy(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)
                .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .setResolutionSelector(resolutionSelector)
                .setTargetRotation(rotation ?: 0)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()

            imageAnalyzer.setAnalyzer(Executors.newSingleThreadExecutor()) { image ->
                imageClassifierHelper.classifyImage(image)
                image.close()
            }

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
            } catch (exc: Exception) {
                Toast.makeText(
                    this@GameActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private var isTrue: Boolean = false
    private fun checkLetter(letter: Char?) {
        val currentTime = System.currentTimeMillis()

        runOnUiThread {
            if (quizLetter.isNotEmpty() && letter == quizLetter[0]) {
                if (!isTrue) {
                    isTrue = true
                    showToast("Benar!")
                }
            }
        }
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "GameActivity"
        const val QUIZ_LETTER_EXTRA_KEY = "QUIZ_LETTER_EXTRA"
        const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
        const val CAMERAX_RESULT = 200
    }
}
