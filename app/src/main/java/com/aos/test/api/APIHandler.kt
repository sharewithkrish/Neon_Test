package com.aos.test.api

import android.content.Context
import com.aos.test.R
import com.aos.test.enums.SnackBarEnum
import com.aos.test.utilities.Utility
import java.io.EOFException
import java.io.IOException
import java.net.SocketTimeoutException

object APIHandler {

    fun apiFailure(t: Throwable, context: Context) {
        try {
            if (t is SocketTimeoutException) {
                Utility.showSnackBar(
                    context,
                    context.getString(R.string.internet_check_validation),
                    SnackBarEnum.ERROR
                )
                return
            }
            if (t is IOException) {

                Utility.showSnackBar(
                    context,
                    context.getString(R.string.internet_check_validation),
                    SnackBarEnum.ERROR
                )
                return
            }
            if (t is EOFException) {
                Utility.showSnackBar(context, "EOFException", SnackBarEnum.ERROR)
                return
            }
            if (t is APIFactory.ApiFailureException) {
                Utility.showSnackBar(context, "ApiFailureException", SnackBarEnum.ERROR)
                return
            }
            Utility.showSnackBar(context, t.message!!, SnackBarEnum.ERROR)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}