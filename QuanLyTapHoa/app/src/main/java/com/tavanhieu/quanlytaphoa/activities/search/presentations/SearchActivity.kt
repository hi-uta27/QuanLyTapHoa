package com.tavanhieu.quanlytaphoa.activities.search.presentations

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.depot.adapter.DepotAdapter
import com.tavanhieu.quanlytaphoa.activities.search.domain.infra.SearchUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.search.domain.use_cases.SearchUseCase
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import com.tavanhieu.quanlytaphoa.commons.models.Product

class SearchActivity : BaseActivity() {
    private lateinit var imageBack: ImageView
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val searchUseCase: SearchUseCase by lazy { SearchUseCaseImpl() }
    private val productAdapter: DepotAdapter by lazy { DepotAdapter() }

    override fun setContentView() {
        setContentView(R.layout.activity_search)
    }

    override fun mappingViewId() {
        imageBack = findViewById(R.id.imageBack)
        searchView = findViewById(R.id.searchView)
        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.recyclerView)
    }

    override fun configLayout() {
        imageBack.setOnClickListener { finish() }
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 != null && p0 != "") {
                    searchWithName(p0.trim())
                } else {
                    productAdapter.setData(this@SearchActivity, ArrayList())
                }
                return false
            }
        })
    }

    private fun searchWithName(name: String) {
        progressBar.visibility = View.VISIBLE
        searchUseCase.searchProductByName(name, {
            handleSearchWithNameSuccess(it)
        }, {
            handleSearchFailure()
        })
    }

    private fun handleSearchWithNameSuccess(products: ArrayList<Product>) {
        progressBar.visibility = View.GONE
        productAdapter.setData(this, products)
        recyclerView.adapter = productAdapter
    }

    private fun handleSearchFailure() {
        progressBar.visibility = View.GONE
        showAlertDialog(
            getResourceText(R.string.error),
            getResourceText(R.string.searchFailure),
            getResourceText(R.string.tryAgain)
        ) {
            searchWithName(searchView.query.toString())
        }
    }
}