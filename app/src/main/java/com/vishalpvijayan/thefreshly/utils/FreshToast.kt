package com.vishalpvijayan.thefreshly.utils

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.vishalpvijayan.thefreshly.R

object FreshToast {
    fun show(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            background = ContextCompat.getDrawable(context, R.drawable.bg_fresh_toast)
            setPadding(28, 18, 28, 18)
        }

        val textView = TextView(context).apply {
            text = message
            setTextColor(Color.WHITE)
            textSize = 14f
            maxLines = 3
        }

        container.addView(
            textView,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        Toast(context).apply {
            this.duration = duration
            view = container
            setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 120)
            show()
        }
    }
}

fun Context.showFreshToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    FreshToast.show(this, message, duration)
}
