package com.altayyar.app.presentation.ui.view

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.altayyar.app.R
import com.altayyar.app.databinding.ViewBackgroundMessageBinding
import com.altayyar.app.util.addDrawables
import com.altayyar.app.util.getDrawableRes
import com.altayyar.app.util.getErrorString
import com.altayyar.app.util.hide
import com.altayyar.app.util.show
import com.altayyar.app.util.visible

/**
 * This view is used for screens with content which may be empty or might have failed to download.
 */
class BackgroundMessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = ViewBackgroundMessageBinding.inflate(LayoutInflater.from(context), this)

    init {
        gravity = Gravity.CENTER_HORIZONTAL
        orientation = VERTICAL

        if (isInEditMode) {
            setup(R.drawable.errorphant_offline, R.string.error_network) {}
        }
    }

    fun setup(throwable: Throwable, listener: ((v: View) -> Unit)? = null) {
        setup(throwable.getDrawableRes(), throwable.getErrorString(context), listener)
    }

    fun setup(
        @DrawableRes imageRes: Int,
        @StringRes messageRes: Int,
        clickListener: ((v: View) -> Unit)? = null
    ) = setup(imageRes, context.getString(messageRes), clickListener)

    /**
     * Setup image, message and button.
     * If [clickListener] is `null` then the button will be hidden.
     */
    fun setup(
        @DrawableRes imageRes: Int,
        message: String,
        clickListener: ((v: View) -> Unit)? = null
    ) {
        binding.messageTextView.text = message
        binding.messageTextView.movementMethod = LinkMovementMethod.getInstance()
        binding.imageView.setImageResource(imageRes)
        binding.button.setOnClickListener(clickListener)
        binding.button.visible(clickListener != null)
        binding.helpTextCard.hide()
    }

    fun showHelp(@StringRes helpRes: Int) {
        val size: Int = binding.helpText.textSize.toInt() + 2
        val color = binding.helpText.currentTextColor
        val text = context.getText(helpRes)
        val textWithDrawables = addDrawables(text, color, size, context)

        binding.helpText.setText(textWithDrawables, TextView.BufferType.SPANNABLE)

        binding.helpTextCard.show()
    }
}
