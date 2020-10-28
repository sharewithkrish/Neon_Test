package com.aos.test.views.data_model

import androidx.databinding.BaseObservable
import com.aos.test.utilities.Utility
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/*

* @author Krishna

* Created on Oct 27, 2020

*/


/********************************
* DATA MODEL CLASS FOR CATEGORY DATA RESPONSE ELEMENTS
* ******************************
*/


data class ResponseCategoryData(
    @SerializedName("results") var categoryList: List<CategoryItems> = mutableListOf(),
    @SerializedName("count") var count: Int,
    @SerializedName("next") var next: String?
) : Serializable, BaseObservable()

data class CategoryItems(
    @SerializedName("id")
    @Expose
    var id: String = "",
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("setts_url")
    @Expose
    var settsUrl: String = "",

    var subSeriesList: MutableList<SeriesItems> = mutableListOf()

) : Serializable, BaseObservable()





/***************************************
* DATA MODEL CLASS FOR SERIES DATA RESPONSE ELEMENTS
* ***************************************
*/
data class ResponseSeriesData(
    @SerializedName("results") var seriesList: MutableList<SeriesItems> = mutableListOf(),
    @SerializedName("count") var count: Int
) : Serializable, BaseObservable()

data class SeriesItems(
    @SerializedName("id")
    @Expose
    var id: String = "",
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("preview_0")
    @Expose
    var previewUrl: String = "",
    @SerializedName("sett_assets")
    @Expose
    var sett_assets: SettAsset,
    @SerializedName("creator")
    @Expose
    var creator: Creator,

    val progressValue:Int

) : Serializable, BaseObservable(){
    // GENERATE RANDOM NUMBER BETWEEN 1 TO 100 FOR PROGRESS BAR DISPLAY
    fun getRandomNumber():Int{
        return (0..100).random()
    }
}

// CHILD OBJECT
data class Creator(
    @SerializedName("name")
    @Expose
    var name: String
): Serializable, BaseObservable()

// CHILD OBJECT
data class SettAsset(
    @SerializedName("large")
    @Expose
    var large: LargeImg
) : Serializable, BaseObservable()

// CHILD OBJECT
data class LargeImg(

    @SerializedName("url")
    @Expose
    var url: String

) : Serializable, BaseObservable(){

    // FORMAT IMAGE THUMBNAIL URL
    fun getValidImageUrl() : String? {
        return Utility.getFormatImageUrl(url)
    }

}

