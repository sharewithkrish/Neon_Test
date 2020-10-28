package com.aos.test.api

import com.aos.test.views.data_model.ResponseCategoryData
import com.aos.test.views.data_model.ResponseSeriesData
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface APIRequest {

    @GET
    fun getCategories(@Url loadMoreUrl : String): Observable<Response<ResponseCategoryData>>

    @GET
    fun getSeries(@Url suffixUrl : String): Observable<Response<ResponseSeriesData>>

}