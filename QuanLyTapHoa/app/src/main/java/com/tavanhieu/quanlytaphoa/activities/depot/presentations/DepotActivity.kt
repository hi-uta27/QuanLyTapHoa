package com.tavanhieu.quanlytaphoa.activities.depot.presentations

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.add_product.presentations.AddProductActivity
import com.tavanhieu.quanlytaphoa.activities.depot.adapter.DepotAdapter
import com.tavanhieu.quanlytaphoa.activities.depot.domain.infra.ReadDepotUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.depot.domain.use_cases.ReadDepotUseCase
import com.tavanhieu.quanlytaphoa.activities.register.presentations.RegisterActivity
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import com.tavanhieu.quanlytaphoa.commons.models.Product

class DepotActivity : BaseActivity() {
    private lateinit var imageBack: ImageView
    private lateinit var emptyTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var floatingActionButton: ExtendedFloatingActionButton

    private val readDepotUseCase: ReadDepotUseCase by lazy { ReadDepotUseCaseImpl() }
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
        handleClickOnView()
    }

    private fun refreshDepot() {
        progressBar.visibility = View.VISIBLE
        readDepotUseCase.refresh({
            progressBar.visibility = View.GONE
            adapter.setData(this, it)
            recyclerView.adapter = adapter
            if(it.isEmpty()) {
                emptyTextView.visibility = View.VISIBLE
            } else {
                emptyTextView.visibility = View.GONE
            }
        }, {
            showAlertDialog("Lỗi", "Đọc kho hàng thất bại", "Thử lại") {
                refreshDepot()
            }
        })
    }

    private fun handleClickOnView() {
        floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }

        imageBack.setOnClickListener { finish() }
    }
}