package com.aos.test.utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.aos.test.R
import com.aos.test.enums.SnackBarEnum
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.android.material.snackbar.Snackbar


object Utility {

    fun loadImage(
        context: Context,
        path: String,
        imageView: ImageView,
        isLoadCompleted: Runnable?,
        isLoadFailed: Runnable?
    ){
        GlideApp.with(context)
            .load(path)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_place_holder)
            .fitCenter() // scale to fit entire image within ImageView
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    isLoadFailed?.run()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    isLoadCompleted?.run()
                    return false
                }
            })
            .into(imageView)
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw      = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    fun getFormatImageUrl(url:String) : String? {
        return if(!url.startsWith("http")) {
            "http://$url"
        } else {
            url
        }
    }

    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    @SuppressLint("InflateParams")
    fun showSnackBar(context: Context, message: String, snackBarEnum: SnackBarEnum) {
        try {
            val customSnackBar = Snackbar.make(
                (context as Activity).findViewById(android.R.id.content),
                "",
                Snackbar.LENGTH_LONG
            )
            val layout = customSnackBar.view as Snackbar.SnackbarLayout
            val customSnackView = context.layoutInflater.inflate(R.layout.custom_snackbar, null)
            val messageTV = customSnackView.findViewById(R.id.message_tv) as TextView
            messageTV.text = message
            messageTV.run {
                when (snackBarEnum) {
                    SnackBarEnum.ERROR -> {
                        messageTV.setTextColor(ContextCompat.getColor(context, R.color.text_white))
                        customSnackView.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.snack_bar_failure
                            )
                        )

                    }
                    SnackBarEnum.SUCCESS -> {
                        messageTV.setTextColor(ContextCompat.getColor(context, R.color.text_white))
                        customSnackView.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.snack_bar_success
                            )
                        )
                    }
                    SnackBarEnum.NOTE -> {
                        messageTV.setTextColor(ContextCompat.getColor(context, R.color.text_white))
                        customSnackView.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.snack_bar_info
                            )
                        )

                    }
                    SnackBarEnum.WARNING -> {
                        messageTV.setTextColor(ContextCompat.getColor(context, R.color.text_white))
                        customSnackView.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.snack_bar_warning
                            )
                        )
                    }
                }
            }

            layout.setPadding(0, 0, 0, 0)
            layout.addView(customSnackView, 0)

            customSnackBar.show()

        } catch (e: Exception) {
            Log.d("Logger", e.message!!)
        }
    }


}