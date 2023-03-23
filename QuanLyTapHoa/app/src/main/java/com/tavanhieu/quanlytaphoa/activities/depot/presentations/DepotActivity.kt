package com.tavanhieu.quanlytaphoa.activities.depot.presentations

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.depot.domain.adapter.DepotAdapter
import com.tavanhieu.quanlytaphoa.activities.depot.domain.infra.ReadDepotUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.depot.domain.use_cases.ReadDepotUseCase
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import com.tavanhieu.quanlytaphoa.commons.models.Product

class DepotActivity : BaseActivity() {
    private lateinit var imageBack: ImageView
    private lateinit var emptyTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var floatingActionButton: FloatingActionButton

    private val readDepotUseCase: ReadDepotUseCase by lazy { ReadDepotUseCaseImpl() }
    private val adapter: DepotAdapter by lazy { DepotAdapter() }
//    private val arr: ArrayList<Product> by lazy { ArrayList() }

    override fun setContentView() {
        setContentView(R.layout.activity_depot)
    }

    override fun mappingViewId() {
        imageBack = findViewById(R.id.imageBack)
        emptyTextView = findViewById(R.id.emptyTextView)
        recyclerView = findViewById(R.id.recycleView)
        progressBar = findViewById(R.id.progressBar)
        floatingActionButton = findViewById(R.id.floatingActionButton)
    }

    override fun configLayout() {
        refreshDepot()
        handleClickOnView()
    }

    private fun refreshDepot() {
        progressBar.visibility = View.VISIBLE
        readDepotUseCase.refresh({
            progressBar.visibility = View.GONE
            if(it.isEmpty()) {
                emptyTextView.visibility = View.VISIBLE
            } else {
                emptyTextView.visibility = View.GONE
//                arr.clear()
//                arr.addAll(it)
                adapter.setData(it)
                recyclerView.adapter = adapter
            }
        }, {
            showAlertDialog("Lỗi", "Đọc kho hàng thất bại", "Thử lại") {
                refreshDepot()
            }
        })
    }

    private fun handleClickOnView() {
        floatingActionButton.setOnClickListener {
            // open add product
        }
    }
}