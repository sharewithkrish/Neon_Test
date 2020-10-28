package com.aos.test.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aos.test.databinding.SeriesListItemBinding
import com.aos.test.utilities.Utility
import com.aos.test.views.MainActivity
import com.aos.test.views.data_model.LargeImg
import com.aos.test.views.data_model.SeriesItems

/*

* @author Krishna

* Created on Oct 27, 2020

*/

class SeriesListAdapter(private var mainActivity: MainActivity) :
    RecyclerView.Adapter<SeriesListAdapter.SeriesListHolder>() {

    private var fetchedSeriesItems: MutableList<SeriesItems> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesListHolder {
        val binding =
            SeriesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeriesListHolder(binding)
    }

    override fun onBindViewHolder(holder: SeriesListHolder, position: Int) {
        holder.bind(
            fetchedSeriesItems[position],
            position
        )

        // LOAD SERIES THUMBNAIL IMAGE FROM API, BEFORE THAT URL HAS FORMATTED AS HTTP TO SUPPORT GLIDE HANDLING
        if (fetchedSeriesItems[position].sett_assets.large.url.isNotEmpty()) {
            val largeImage  = LargeImg(fetchedSeriesItems[position].sett_assets.large.url)
            largeImage.getValidImageUrl()?.let {
                Utility.loadImage(
                    mainActivity, it,
                    holder.seriesListItemBinding.ivSeriesPic, null, null
                )
            }
        }


    }

    override fun getItemCount(): Int {
        return fetchedSeriesItems.size
    }

    fun setDataPosition(position:Int){
        // CLEAR AND UPDATE SERIES LIST FOR EACH CATEGORY
        fetchedSeriesItems.clear()
        // LOAD SERIES LIST FROM MAIN CATEGORY LIST
        fetchedSeriesItems.addAll(mainActivity.fetchedCategoryItems[position].subSeriesList)
        notifyDataSetChanged()
    }

    fun clearAdapter(){
        fetchedSeriesItems.clear()
        notifyDataSetChanged()
    }


    class SeriesListHolder(
        var seriesListItemBinding: SeriesListItemBinding
    ) :
        RecyclerView.ViewHolder(seriesListItemBinding.root) {

        fun bind(seriesDetailData: SeriesItems, position: Int) {
            seriesListItemBinding.getSeriesItem = seriesDetailData
            seriesListItemBinding.position = position
        }
    }


}