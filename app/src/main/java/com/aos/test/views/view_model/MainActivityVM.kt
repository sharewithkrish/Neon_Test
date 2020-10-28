package com.aos.test.views.view_model

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.aos.test.R
import com.aos.test.api.APIConstants.BASE_FETCH
import com.aos.test.api.APIFactory
import com.aos.test.api.APIHandler
import com.aos.test.api.APIRequest
import com.aos.test.enums.SnackBarEnum
import com.aos.test.utilities.MyProgressDialog
import com.aos.test.utilities.Utility
import com.aos.test.views.MainActivity
import com.aos.test.views.data_model.ResponseCategoryData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

/*

* @author Krishna

* Created on Oct 27, 2020

*/

class MainActivityVM(private var mainActivity: MainActivity) : ViewModel() {

    private var myProgressDialog = MyProgressDialog()

    init {
        // START FETCH CATEGORY
        categoryListAPICall(BASE_FETCH)
    }


    // API CALL FOR FETCH THE CATEGORY LIST
    @SuppressLint("CheckResult")
    fun categoryListAPICall(url:String) {
        if (Utility.isInternetAvailable(mainActivity)) {
            mainActivity.loading = true // SET API LOADING STATE "TRUE", TO AVOID SCROLL TO LOAD MORE UNTIL RECEIVES RESPONSE
            myProgressDialog.showDialog(mainActivity, mainActivity.getString(R.string.txt_loading), true)
            APIFactory.createService<APIRequest>().getCategories(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::categoryListSuccessResponse) {
                    //Failure Block
                    myProgressDialog.dismissDialog()
                    mainActivity.loading = false
                    APIHandler.apiFailure(it, mainActivity)
                }
        } else {
            Utility.showSnackBar(mainActivity, mainActivity.getString(R.string.msg_internet_not_available), SnackBarEnum.ERROR)
        }
    }
    // HANDLE CATEGORY LIST API RESPONSE HERE
    private fun categoryListSuccessResponse(response: Response<ResponseCategoryData>) {
        myProgressDialog.dismissDialog()
        mainActivity.loading = false  // SET API LOADING STATE "FALSE", TO ALLOW TO SCROLL TO LOAD MORE
        if (response.code() == 200) {
            mainActivity.nextUrl = response.body()?.next // SAVE NEXT PAGE REQUEST URL IN A VARIABLE
            mainActivity.categoryListAdapter.setData(response.body()!!.categoryList) // LOAD TO THE ADAPTER
        } else {
            Utility.showSnackBar(mainActivity, mainActivity.getString(R.string.msg_data_not_available), SnackBarEnum.ERROR)   // HANDLING API REQUEST FAILURE
        }
    }



}