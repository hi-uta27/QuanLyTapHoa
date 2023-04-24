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
import com.tavanhieu.quanlytaphoa.activities.home.adapters.SalesProductAdapter
import com.tavanhieu.quanlytaphoa.activities.home.domain.infra.HomeUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.home.domain.use_cases.HomeUseCase
import com.tavanhieu.quanlytaphoa.activities.home.models.FunctionItem
import com.tavanhieu.quanlytaphoa.activities.search.presentations.SearchActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import com.tavanhieu.quanlytaphoa.commons.models.Product

class HomeFragment(val context: MainActivity): Fragment() {
    private lateinit var nameEmployeeTextView: TextView
    private lateinit var searchImageView: ImageView
    private lateinit var recyclerViewFunction: RecyclerView
    private lateinit var recyclerViewBestSalesProduct: RecyclerView
    private lateinit var recyclerViewLeastSalesProduct: RecyclerView
    private lateinit var emptyLeastSalesTextView: TextView
    private lateinit var emptyBestSalesTextView: TextView
    private lateinit var loading1: TextView
    private lateinit var loading2: TextView

    private val arr: ArrayList<FunctionItem> by lazy { ArrayList() }
    private val functionItemAdapter: FunctionItemAdapter by lazy { FunctionItemAdapter() }
    private val homeUseCase: HomeUseCase by lazy { HomeUseCaseImpl() }
    private val bestSalesProductAdapter: SalesProductAdapter by lazy { SalesProductAdapter() }
    private val leastSalesProductAdapter: SalesProductAdapter by lazy { SalesProductAdapter() }

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
        requestLeastSalesProduct()

        return view
    }

    private fun mappingViewId(view: View) {
        nameEmployeeTextView = view.findViewById(R.id.nameEmployeeTextView)
        recyclerViewFunction = view.findViewById(R.id.recycleViewFunction)
        recyclerViewBestSalesProduct = view.findViewById(R.id.recycleViewBestSalesProduct)
        recyclerViewLeastSalesProduct = view.findViewById(R.id.recycleViewLeastSalesProduct)
        searchImageView = view.findViewById(R.id.searchImageView)
        emptyLeastSalesTextView = view.findViewById(R.id.emptyLeastSalesTextView)
        emptyBestSalesTextView = view.findViewById(R.id.emptyBestSalesTextView)
        loading1 = view.findViewById(R.id.loading1)
        loading2 = view.findViewById(R.id.loading2)
    }

    private fun handleClickOnView() {
        searchImageView.setOnClickListener {
            requireActivity().startActivity(Intent(requireActivity(), SearchActivity::class.java))
        }
    }

    // MARK: - Least sales ----------------------------------------------------------------

    private fun requestLeastSalesProduct() {
        loading2.visibility = View.VISIBLE
        homeUseCase.requestProductsLeastSales({
            handleRequestLeastSalesProductSuccess(it)
        }, {
            handleRequestLeastSalesProductFailure()
        })
    }

    private fun handleRequestLeastSalesProductSuccess(products: ArrayList<Product>) {
        loading2.visibility = View.GONE
        if (products.size == 0) {
            emptyLeastSalesTextView.visibility = View.VISIBLE
        } else {
            emptyLeastSalesTextView.visibility = View.GONE
            leastSalesProductAdapter.setData(context, products)
            recyclerViewLeastSalesProduct.adapter = leastSalesProductAdapter
        }
    }

    private fun handleRequestLeastSalesProductFailure() {
        loading2.visibility = View.GONE
        context.showAlertDialog(
            context.getResourceText(R.string.notification),
            context.getResourceText(R.string.loginFailed),
            context.getResourceText(R.string.tryAgain)) {
            requestLeastSalesProduct()
        }
    }

    // MARK: - Best sales ----------------------------------------------------------------

    private fun requestBestSalesProduct() {
        loading1.visibility = View.VISIBLE
        homeUseCase.requestProductsBestSales({
            handleRequestBestSalesProductSuccess(it)
        }, {
            handleRequestBestSalesProductFailure()
        })
    }

    private fun handleRequestBestSalesProductSuccess(products: ArrayList<Product>) {
        loading1.visibility = View.GONE
        if (products.size == 0) {
            emptyBestSalesTextView.visibility = View.VISIBLE
        } else {
            emptyBestSalesTextView.visibility = View.GONE
            bestSalesProductAdapter.setData(context, products)
            recyclerViewBestSalesProduct.adapter = bestSalesProductAdapter
        }
    }

    private fun handleRequestBestSalesProductFailure() {
        loading1.visibility = View.GONE
        context.showAlertDialog(
            context.getResourceText(R.string.notification),
            context.getResourceText(R.string.loginFailed),
            context.getResourceText(R.string.tryAgain)) {
            requestBestSalesProduct()
        }
    }

    // Function item ----------------------------------------------------------------------------

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