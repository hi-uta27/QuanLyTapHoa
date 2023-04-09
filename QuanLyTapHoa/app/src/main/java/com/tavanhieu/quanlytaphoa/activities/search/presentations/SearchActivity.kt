package com.tavanhieu.quanlytaphoa.activities.search.presentations

import android.widget.ImageView
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity

class SearchActivity : BaseActivity() {
    private lateinit var imageBack: ImageView
    private lateinit var scanBarCodeImageView: ImageView
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView

    override fun setContentView() {
        setContentView(R.layout.activity_search)
    }

    override fun mappingViewId() {
        imageBack = findViewById(R.id.imageBack)
        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerView)
        scanBarCodeImageView = findViewById(R.id.scanBarCodeImageView)
    }

    override fun configLayout() {
        //
    }
}