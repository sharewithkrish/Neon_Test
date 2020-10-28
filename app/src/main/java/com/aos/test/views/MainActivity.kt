package com.aos.test.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aos.test.R
import com.aos.test.databinding.ActivityMainBinding
import com.aos.test.views.adapter.CategoryListAdapter
import com.aos.test.views.data_model.CategoryItems
import com.aos.test.views.view_model.MainActivityVM

/*

* @author Krishna

* Created on Oct 27, 2020

*/


class MainActivity : AppCompatActivity(), LifecycleOwner {

    private lateinit var activityMainBinding: ActivityMainBinding
    lateinit var mainActivityVM: MainActivityVM
    lateinit var categoryListAdapter: CategoryListAdapter

    var nextUrl:String? = null
    var loading = false

    var pastVisibleItems: Int? = null
    var visibleItemCount: Int? = null
    var totalItemCount: Int? = null

    var fetchedCategoryItems: MutableList<CategoryItems> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        mainActivityVM = MainActivityVM(this@MainActivity)
        activityMainBinding.mainActivityVm = mainActivityVM

        // INITIALIZE THE RECYCLE LIST VIEW AND ASSIGN ADAPTER
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this@MainActivity)
        activityMainBinding.rvCategoryListView.layoutManager = layoutManager
        categoryListAdapter = CategoryListAdapter(this@MainActivity)
        activityMainBinding.rvCategoryListView.adapter = categoryListAdapter

        // ADD RECYCLE LIST SCROLL LISTENER TO LOAD MORE DATA ON DEMAND - PAGINATION CONCEPT
        activityMainBinding.rvCategoryListView
            .addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                        if ((visibleItemCount!! + pastVisibleItems!!) >= totalItemCount!!) {
                            if (!nextUrl.isNullOrEmpty() && !loading) {
                                mainActivityVM.categoryListAPICall(nextUrl!!)
                            }
                        }
                }
            })
    }


}
