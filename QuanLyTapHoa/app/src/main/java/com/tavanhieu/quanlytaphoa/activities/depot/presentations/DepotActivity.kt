package com.tavanhieu.quanlytaphoa.activities.depot.presentations

import android.content.Intent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.add_product.presentations.AddProductActivity
import com.tavanhieu.quanlytaphoa.activities.depot.adapter.DepotAdapter
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
    private lateinit var floatingActionButton: ExtendedFloatingActionButton

    private val depotUseCase: ReadDepotUseCase by lazy { ReadDepotUseCaseImpl() }
    private val adapter: DepotAdapter by lazy { DepotAdapter() }

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

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }
        imageBack.setOnClickListener { finish() }
    }

    private fun refreshDepot() {
        progressBar.visibility = View.VISIBLE
        depotUseCase.refresh({
            refreshDepotSuccess(it)
        }, {
            refreshDepotFailure()
        })
    }

    private fun refreshDepotSuccess(products: ArrayList<Product>) {
        progressBar.visibility = View.GONE
        adapter.setData(this, products)
        recyclerView.adapter = adapter

        if(products.isEmpty()) {
            emptyTextView.visibility = View.VISIBLE
        } else {
            emptyTextView.visibility = View.GONE
        }
    }

    private fun refreshDepotFailure() {
        progressBar.visibility = View.GONE
        showAlertDialog(getResourceText(R.string.error),
            getResourceText(R.string.readDepotFailure),
            getResourceText(R.string.tryAgain)
        ) {
            refreshDepot()
        }
    }
}