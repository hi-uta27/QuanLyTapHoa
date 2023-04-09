package com.tavanhieu.quanlytaphoa.activities.home.presentations

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.home.adapters.FunctionItemAdapter
import com.tavanhieu.quanlytaphoa.activities.home.models.FunctionItem
import com.tavanhieu.quanlytaphoa.activities.search.presentations.SearchActivity

class HomeFragment: Fragment() {
    private lateinit var nameEmployeeTextView: TextView
    private lateinit var searchImageView: ImageView
    private lateinit var recyclerViewFunction: RecyclerView
    private lateinit var recyclerViewProduct: RecyclerView

    private val arr: ArrayList<FunctionItem> by lazy { ArrayList() }
    private val adapter: FunctionItemAdapter by lazy { FunctionItemAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_home, container, false)
        mappingViewId(view)
        handleClickOnView()
        displayFunctionRecyclerView()

        return view
    }

    private fun mappingViewId(view: View) {
        nameEmployeeTextView = view.findViewById(R.id.nameEmployeeTextView)
        recyclerViewFunction = view.findViewById(R.id.recycleViewFunction)
        recyclerViewProduct = view.findViewById(R.id.recycleViewProduct)
        searchImageView = view.findViewById(R.id.searchImageView)
    }

    private fun handleClickOnView() {
        searchImageView.setOnClickListener {
            requireActivity().startActivity(Intent(requireActivity(), SearchActivity::class.java))
        }
    }

    private fun displayFunctionRecyclerView() {
        getArr()
        adapter.setData(arr)
        recyclerViewFunction.adapter = adapter
    }

    private fun getArr() {
        arr.add(FunctionItem(R.drawable.kho_hang, resources.getString(R.string.Depot)))
        arr.add(FunctionItem(R.drawable.don_hang, resources.getString(R.string.Orders)))
        arr.add(FunctionItem(R.drawable.ic_cart, resources.getString(R.string.cart)))
        arr.add(FunctionItem(R.drawable.thong_ke, resources.getString(R.string.Statistics)))
        arr.add(FunctionItem(R.drawable.ic_qr_code, resources.getString(R.string.Qr)))
    }
}