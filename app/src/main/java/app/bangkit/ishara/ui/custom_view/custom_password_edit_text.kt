package app.bangkit.ishara.ui.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import app.bangkit.ishara.R

class CustomPasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var passwordButtonImage: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_password_baseline24) as Drawable
    private var hidePasswordButtonImage: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_hide_baseline24) as Drawable
    private var showPasswordButtonImage: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_show_baseline24) as Drawable
    private var isPasswordVisible: Boolean = false

    init {
        setOnTouchListener(this)
        updatePasswordVisibility()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setButtonDrawables(endOfTheText = if (isPasswordVisible) hidePasswordButtonImage else showPasswordButtonImage)
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP && compoundDrawables[2] != null) {
            val togglePasswordButtonStart: Float
            val togglePasswordButtonEnd: Float
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                togglePasswordButtonEnd = (hidePasswordButtonImage.intrinsicWidth + paddingStart).toFloat()
                if (event.x < togglePasswordButtonEnd) {
                    togglePasswordVisibility()
                    return true
                }
            } else {
                togglePasswordButtonStart = (width - paddingEnd - hidePasswordButtonImage.intrinsicWidth).toFloat()
                if (event.x > togglePasswordButtonStart) {
                    togglePasswordVisibility()
                    return true
                }
            }
        }
        return false
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        updatePasswordVisibility()
        invalidate()
    }

    private fun updatePasswordVisibility() {
        if (isPasswordVisible) {
            inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        setSelection(text?.length ?: 0) // To maintain the cursor position
        setButtonDrawables(endOfTheText = if (isPasswordVisible) hidePasswordButtonImage else showPasswordButtonImage)
    }
}
