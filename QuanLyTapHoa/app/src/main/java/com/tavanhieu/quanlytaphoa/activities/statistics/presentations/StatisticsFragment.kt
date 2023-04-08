package com.tavanhieu.quanlytaphoa.activities.statistics.presentations

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.statistics.domain.enum.TimeUnit
import com.tavanhieu.quanlytaphoa.activities.statistics.domain.infra.StatisticUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.statistics.domain.use_cases.StatisticUseCase
import com.tavanhieu.quanlytaphoa.commons.*
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
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
    private val timeUnits = listOf("Ngày", "Tuần", "Tháng")

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
        val timeUnit: TimeUnit = when (timeUnitSpinner.selectedItemId) {
            0L -> TimeUnit.day
            1L -> TimeUnit.weak
            2L -> TimeUnit.month
//            3L -> TimeUnit.year
            else -> TimeUnit.day
        }

        statisticUseCase.filterOrderListBy(timeLineDate.time, timeUnit, {
            handleFilterOrderListSuccess(it, timeUnit)
        }, {
            handleFilterOrderListFailure()
        })
    }

    private fun handleFilterOrderListSuccess(orders: ArrayList<Order>, timeUnit: TimeUnit) {
        context.showToast(timeUnit.toString())
        val barEntriesList: ArrayList<BarEntry> = ArrayList()
        val labels: ArrayList<String> = ArrayList()
        var i = 0f
        orders.forEach {
            barEntriesList.add(BarEntry(i++, it.totalPrice()))
            when (timeUnit) {
                TimeUnit.day -> labels.add(it.date.formatTime())
                TimeUnit.weak -> labels.add(it.date.getNameOfDayInWeek())
                TimeUnit.month -> labels.add(it.date.getNameOfDayAndMonth())
                TimeUnit.year -> labels.add(it.date.getNameOfMonth())
                else -> labels.add(it.date.formatTime())
            }
        }

        progressBar.visibility = View.GONE
        val barDataSet = BarDataSet(barEntriesList, context.getResourceText(R.string.revenueStatistics))
        val barData = BarData(barDataSet)
        barChart.data = barData
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.setCenterAxisLabels(true)
        barChart.xAxis.enableGridDashedLine(0f, 5f, 0f)
        barChart.description.isEnabled = false
        barChart.animateY(1500)
        barChart.setFitBars(true)
    }

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
        timeUnitSpinner.onItemSelectedListener = object: OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                loadDataForStatistic()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun openDatePicker() {
        DatePickerDialog(requireContext(), { _, i, i2, i3 ->
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