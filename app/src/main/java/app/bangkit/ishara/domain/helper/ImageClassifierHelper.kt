package app.bangkit.ishara.domain.helper

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import android.view.Surface
import androidx.camera.core.ImageProxy
import app.bangkit.ishara.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(
    var threshold: Float = 0.8f,
    var multiClassMaxResults: Int = 3,
    val binaryModelName: String = "binary_model.tflite",
    val multiClassModelName: String = "multiclass_model.tflite",
    val context: Context,
    val isHandDetectedListener: IsHandDetectedListener?,
    val imageClassifierListener: ImageClassifierListener?
) {
    private var binaryImageClassifier: ImageClassifier? = null
    private var multiClassImageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val binaryOptionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(1)

        val multiClassOptionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(multiClassMaxResults)

        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)

        binaryOptionsBuilder.setBaseOptions(baseOptionsBuilder.build())
        multiClassOptionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            binaryImageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                binaryModelName,
                binaryOptionsBuilder.build()
            )
            multiClassImageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                multiClassModelName,
                multiClassOptionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            imageClassifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyImage(image: ImageProxy) {
        if (binaryImageClassifier == null || multiClassImageClassifier == null) {
            setupImageClassifier()
        }

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0.0f, 1.0f))
            .add(CastOp(DataType.FLOAT32))
            .build()

        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(toBitmap(image)))

        val imageProcessingOptions = ImageProcessingOptions.builder()
            .setOrientation(getOrientationFromRotation(image.imageInfo.rotationDegrees))
            .build()

        var inferenceTime: Long = SystemClock.uptimeMillis()

        val binaryResults = binaryImageClassifier?.classify(tensorImage, imageProcessingOptions)
        Log.i(TAG, "Binary Results: $binaryResults")

        if (!binaryResults.isNullOrEmpty()) {

            // Check if the binary classifier detected a hand
            if (binaryResults[0].categories.isNullOrEmpty()) {
                isHandDetectedListener?.onHandDetected()
                val multiClassResults =
                    multiClassImageClassifier?.classify(tensorImage, imageProcessingOptions)
                inferenceTime = SystemClock.uptimeMillis() - inferenceTime
                imageClassifierListener?.onResults(
                    multiClassResults,
                    inferenceTime
                )
            } else if (binaryResults[0].categories[0].label == "0" && binaryResults[0].categories[0].score < 0.4) {
                isHandDetectedListener?.onHandDetected()
                val multiClassResults =
                    multiClassImageClassifier?.classify(tensorImage, imageProcessingOptions)
                inferenceTime = SystemClock.uptimeMillis() - inferenceTime
                imageClassifierListener?.onResults(
                    multiClassResults,
                    inferenceTime
                )
            } else {
                isHandDetectedListener?.onNoHandDetected()
            }
        } else {
            imageClassifierListener?.onError(context.getString(R.string.image_classifier_failed))
        }
    }

    private fun toBitmap(image: ImageProxy): Bitmap {
        val bitmapBuffer = Bitmap.createBitmap(
            image.width,
            image.height,
            Bitmap.Config.ARGB_8888
        )
        image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }
        image.close()
        return bitmapBuffer
    }

    private fun getOrientationFromRotation(rotation: Int): ImageProcessingOptions.Orientation {
        return when (rotation) {
            Surface.ROTATION_270 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
            Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
            Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
            else -> ImageProcessingOptions.Orientation.RIGHT_TOP
        }
    }

    interface IsHandDetectedListener {
        fun onHandDetected()
        fun onNoHandDetected()
    }

    interface ImageClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<Classifications>?,
            inferenceTime: Long
        )
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}