package com.vishalpvijayan.thefreshly.utils
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.ProgressBar
import com.vishalpvijayan.thefreshly.R

object ProgressDialogUtil {

    private var progressDialog: Dialog? = null

    fun show(context: Context) {
        if (progressDialog?.isShowing == true) return

        progressDialog = Dialog(context).apply {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.dialog_progress, null)
            setContentView(view)
            setCancelable(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
    }

    fun hide() {
        progressDialog?.dismiss()
        progressDialog = null
    }
}