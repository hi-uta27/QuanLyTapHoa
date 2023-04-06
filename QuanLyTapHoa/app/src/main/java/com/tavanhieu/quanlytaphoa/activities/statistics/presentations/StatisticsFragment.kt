package com.tavanhieu.quanlytaphoa.activities.statistics.presentations

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.statistics.domain.enum.TimeUnit
import com.tavanhieu.quanlytaphoa.activities.statistics.domain.infra.StatisticUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.statistics.domain.use_cases.StatisticUseCase
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import com.tavanhieu.quanlytaphoa.commons.formatDate
import com.tavanhieu.quanlytaphoa.commons.models.Order
import java.util.Calendar

class StatisticsFragment(val context: BaseActivity): Fragment() {
    private lateinit var progressBar: ProgressBar
    private lateinit var timeLineDateTextView: TextView
    private lateinit var calendarImageButton: ImageButton
    private lateinit var timeUnitSpinner: Spinner
    private lateinit var barChart: BarChart

    private val timeLineDate: Calendar by lazy { Calendar.getInstance() }
    private val statisticUseCase: StatisticUseCase by lazy { StatisticUseCaseImpl() }
    private var barEntriesList: ArrayList<BarEntry> = ArrayList()
    private val timeUnits = listOf("Ngày", "Tuần", "Tháng", "Năm")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_statistics, container, false)
        mappingViewId(view)
        configLayout()
        handleClickOnView()

        return view
    }

    private fun mappingViewId(view: View) {
        progressBar = view.findViewById(R.id.progressBar)
        timeLineDateTextView = view.findViewById(R.id.timeLineDateTextView)
        calendarImageButton = view.findViewById(R.id.calenderImageButton)
        timeUnitSpinner = view.findViewById(R.id.timeUnitSpinner)
        barChart = view.findViewById(R.id.barChart)
    }

    private fun configLayout() {
        timeLineDateTextView.text = timeLineDate.time.formatDate()
        timeUnitSpinner.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_list_item_1,
            timeUnits
        )
        loadDataForStatistic()
    }

    private fun loadDataForStatistic() {
        progressBar.visibility = View.VISIBLE
        statisticUseCase.filterOrderListBy(timeLineDate.time, TimeUnit.day, {
            handleFilterOrderListSuccess(it)
        }, {
            handleFilterOrderListFailure()
        })
    }

    private fun handleFilterOrderListSuccess(orders: ArrayList<Order>) {
        progressBar.visibility = View.GONE
//        barEntriesList = orders.flatMap { order -> BarEntry(order.totalPrice(), "1") }
        orders.forEach {
            barEntriesList.add(BarEntry(it.totalPrice(), it.id.toFloat()))
        }
        val barDataSet = BarDataSet(barEntriesList, "Bar Chart Data")
        val barData = BarData(barDataSet)
        barChart.data = barData
    }

//    // on below line we are setting colors for our bar chart text
//    barDataSet.valueTextColor = Color.BLACK
//    // on below line we are setting color for our bar data set
//    barDataSet.setColor(resources.getColor(R.color.purple_200))
//    // on below line we are setting text size
//    barDataSet.valueTextSize = 16f
//    // on below line we are enabling description as false
//    barChart.description.isEnabled = false

    private fun handleFilterOrderListFailure() {
        progressBar.visibility = View.GONE
        context.showAlertDialog(
            context.getResourceText(R.string.notification),
            context.getResourceText(R.string.addToCartFailure),
            context.getResourceText(R.string.tryAgain)
        ) {
            loadDataForStatistic()
        }
    }

    private fun handleClickOnView() {
        calendarImageButton.setOnClickListener { openDatePicker()  }
    }

    @SuppressLint("SimpleDateFormat")
    private fun openDatePicker() {
        DatePickerDialog(
            requireContext(),
            { _, i, i2, i3 ->
                timeLineDate.set(Calendar.YEAR, i)
                timeLineDate.set(Calendar.MONTH, i2)
                timeLineDate.set(Calendar.DAY_OF_MONTH, i3)
                timeLineDateTextView.text = timeLineDate.time.formatDate()
                loadDataForStatistic()
            },
            timeLineDate.get(Calendar.YEAR),
            timeLineDate.get(Calendar.MONTH),
            timeLineDate.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}