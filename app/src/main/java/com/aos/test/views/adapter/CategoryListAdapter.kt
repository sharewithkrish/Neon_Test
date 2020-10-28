package com.aos.test.views.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aos.test.R
import com.aos.test.api.APIFactory
import com.aos.test.api.APIHandler
import com.aos.test.api.APIRequest
import com.aos.test.databinding.CategoryListItemBinding
import com.aos.test.enums.SnackBarEnum
import com.aos.test.utilities.Utility
import com.aos.test.views.MainActivity
import com.aos.test.views.data_model.CategoryItems
import com.aos.test.views.data_model.ResponseSeriesData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response

/*

* @author Krishna

* Created on Oct 27, 2020

*/

class CategoryListAdapter(private var mainActivity: MainActivity) :
    RecyclerView.Adapter<CategoryListAdapter.CategoryListHolder>() {

    private var seriesAdapter: SeriesListAdapter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListHolder {
        val binding =
            CategoryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // INITIALIZE CHILD RECYCLE ADAPTER AND VIEW MANAGER
        val layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSeriesList.layoutManager = layoutManager
        seriesAdapter = SeriesListAdapter(mainActivity)
        binding.rvSeriesList.adapter = seriesAdapter
        return CategoryListHolder(mainActivity, binding, seriesAdapter!!)
    }

    override fun onBindViewHolder(holder: CategoryListHolder, position: Int) {
        holder.bind(mainActivity.fetchedCategoryItems[position], position)
    }

    override fun getItemCount(): Int {
        return mainActivity.fetchedCategoryItems.size
    }

    fun setData(getCategoryItems: List<CategoryItems>) {
        val last = mainActivity.fetchedCategoryItems.size - 1
        mainActivity.fetchedCategoryItems.addAll(getCategoryItems)
        if (last >= 0) {
            // REFRESH VIEW BASED ON LAST INDEX AND ADDITIONAL ITEM
            notifyItemRangeChanged(last, getCategoryItems.size)
        } else {
            // INITIALLY UPDATE THE LIST VIEW
            notifyDataSetChanged()
        }
    }

    class CategoryListHolder(
        private var mainActivity: MainActivity,
        private var categoryListItemBinding: CategoryListItemBinding,
        private var seriesAdapter: SeriesListAdapter
    ) :
        RecyclerView.ViewHolder(categoryListItemBinding.root) {

        fun bind(categoryDetailData: CategoryItems, position: Int) {
            categoryListItemBinding.getCategoryItem = categoryDetailData
            categoryListItemBinding.position = position

            //MAKE REQUEST BASED ON CATEGORY'S CHILD SERIES LIST AVAILABILITY
            if (mainActivity.fetchedCategoryItems[position].subSeriesList.isNullOrEmpty()) {
                seriesListAPICall(categoryDetailData.settsUrl, position)
            } else {
                // IF ALREADY SERIES LIST AVAILABLE, JUST TELL THE POSITION TO CHILD RECYCLE ADAPTER
                seriesAdapter.setDataPosition(position)
            }
        }


        // API CALL TO FETCH ALL SERIES LIST FROM EACH CATEGORY
        // ASYNCHRONOUS REQUEST TO FETCH SERIES LIST ITEM WITHOUT UI BLOCKING
        @SuppressLint("CheckResult")
        fun seriesListAPICall(getSuffixUrl: String, position: Int) {
            if (Utility.isInternetAvailable(mainActivity)) {
                seriesAdapter.clearAdapter() // CLEAR ADAPTER TO AVOID PREVIOUS VIEW HOLDER ITEM DISPLAY
                APIFactory.createService<APIRequest>().getSeries(getSuffixUrl)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ seriesListSuccessResponse(it, position) }) {
                        //FAILURE BLOCK
                        APIHandler.apiFailure(it, mainActivity)
                    }
            } else {
                Utility.showSnackBar(
                    mainActivity,
                    mainActivity.getString(R.string.msg_internet_not_available),
                    SnackBarEnum.ERROR
                )
            }
        }

        // HANDLE SERIES LIST API RESPONSE HERE
        private fun seriesListSuccessResponse(response: Response<ResponseSeriesData>, position: Int) {
            if (response.code() == 200) {

                // LOAD BANNER IMAGE FROM CATEGORY'S FIRST OBJECT THUMBNAIL URL
                if(position == 0){
                    Utility.loadImage(
                        mainActivity, Utility.getFormatImageUrl(response.body()!!.seriesList[0].sett_assets.large.url)!!,
                        mainActivity.iv_banner, null, null
                    )
                }

                // ADD SERIES LIST TO MAIN CATEGORY LIST TO EACH CATEGORY POSITION
                mainActivity.fetchedCategoryItems[position].subSeriesList.addAll(response.body()!!.seriesList)
                // LOAD SERIES LIST TO THE CHILD ADAPTER
                seriesAdapter.setDataPosition(position)
            } else {
                // HANDLING API REQUEST FAILURE
                Utility.showSnackBar(mainActivity, mainActivity.getString(R.string.msg_data_not_available), SnackBarEnum.ERROR)
            }
        }

    }


}