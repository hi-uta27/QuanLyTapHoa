package com.tavanhieu.quanlytaphoa.activities.home.presentations

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.MainActivity
import com.tavanhieu.quanlytaphoa.activities.depot.adapter.DepotAdapter
import com.tavanhieu.quanlytaphoa.activities.home.adapters.FunctionItemAdapter
import com.tavanhieu.quanlytaphoa.activities.home.domain.infra.HomeUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.home.domain.use_cases.HomeUseCase
import com.tavanhieu.quanlytaphoa.activities.home.models.FunctionItem
import com.tavanhieu.quanlytaphoa.activities.search.presentations.SearchActivity
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import com.tavanhieu.quanlytaphoa.commons.models.Product

class HomeFragment(val context: MainActivity): Fragment() {
    private lateinit var nameEmployeeTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchImageView: ImageView
    private lateinit var recyclerViewFunction: RecyclerView
    private lateinit var recyclerViewProduct: RecyclerView

    private val arr: ArrayList<FunctionItem> by lazy { ArrayList() }
    private val functionItemAdapter: FunctionItemAdapter by lazy { FunctionItemAdapter() }
    private val homeUseCase: HomeUseCase by lazy { HomeUseCaseImpl() }
    private val productAdapter: DepotAdapter by lazy { DepotAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_home, container, false)
        mappingViewId(view)
        handleClickOnView()
        displayFunctionRecyclerView()
        requestBestSalesProduct()

        return view
    }

    private fun requestBestSalesProduct() {
        progressBar.visibility = View.VISIBLE
        homeUseCase.requestProductsBestSales({
            handleRequestBestSalesProductSuccess(it)
        }, {
            handleRequestBestSalesProductFailure()
        })
    }

    private fun handleRequestBestSalesProductSuccess(products: ArrayList<Product>) {
        progressBar.visibility = View.GONE
        if (products.size == 0) {
            context.showToast("Best sales product empty")
        } else {
            productAdapter.setData(context, products)
            recyclerViewProduct.adapter = productAdapter
        }
    }

    private fun handleRequestBestSalesProductFailure() {
        progressBar.visibility = View.GONE
        context.showAlertDialog(
            context.getResourceText(R.string.notification),
            context.getResourceText(R.string.loginFailed),
            context.getResourceText(R.string.tryAgain)) {
            requestBestSalesProduct()
        }
    }

    private fun mappingViewId(view: View) {
        nameEmployeeTextView = view.findViewById(R.id.nameEmployeeTextView)
        recyclerViewFunction = view.findViewById(R.id.recycleViewFunction)
        recyclerViewProduct = view.findViewById(R.id.recycleViewProduct)
        searchImageView = view.findViewById(R.id.searchImageView)
        progressBar = view.findViewById(R.id.progressBar)
    }

    private fun handleClickOnView() {
        searchImageView.setOnClickListener {
            requireActivity().startActivity(Intent(requireActivity(), SearchActivity::class.java))
        }
    }

    private fun displayFunctionRecyclerView() {
        getArr()
        functionItemAdapter.setData(context, arr)
        recyclerViewFunction.adapter = functionItemAdapter
    }

    private fun getArr() {
        arr.add(FunctionItem(R.drawable.kho_hang, resources.getString(R.string.Depot)))
        arr.add(FunctionItem(R.drawable.don_hang, resources.getString(R.string.Orders)))
        arr.add(FunctionItem(R.drawable.ic_cart, resources.getString(R.string.createOrder)))
        arr.add(FunctionItem(R.drawable.thong_ke, resources.getString(R.string.Statistics)))
        arr.add(FunctionItem(R.drawable.ic_notifications, resources.getString(R.string.notification)))
    }
}