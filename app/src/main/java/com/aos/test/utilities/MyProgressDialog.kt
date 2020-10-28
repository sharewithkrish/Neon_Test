package com.aos.test.utilities

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView
import com.aos.test.R

class MyProgressDialog {
    private var customDialog : Dialog? = null
    private lateinit var message: TextView

    fun dismissDialog() {
        try {
            if (customDialog != null && customDialog!!.isShowing)
                customDialog!!.dismiss()
            customDialog = null
        } catch (e: Exception) {
        }
    }
    fun showDialog(activity: Activity, getMessage: String, setShow: Boolean) {
        activity.runOnUiThread {
            if (setShow) {
                customDialog = Dialog(activity)
                customDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                customDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                customDialog!!.setCancelable(false)
                customDialog!!.setCanceledOnTouchOutside(false)
                customDialog!!.setContentView(R.layout.dialog_progress)
                message = customDialog!!.findViewById(R.id.dialog_custom_msg)
                message.text = getMessage
                customDialog!!.show()
            } else {
                customDialog!!.dismiss()
            }
        }
    }








}