package com.tavanhieu.quanlytaphoa.activities.statistics.presentations

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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
    private lateinit var totalPriceTextView: TextView
    private lateinit var emptyOrderTextView: TextView
    private lateinit var calendarImageButton: ImageButton
    private lateinit var timeUnitSpinner: Spinner
    private lateinit var lineChart: LineChart

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
        totalPriceTextView = view.findViewById(R.id.totalPriceTextView)
        emptyOrderTextView = view.findViewById(R.id.emptyOrderTextView)
        calendarImageButton = view.findViewById(R.id.calenderImageButton)
        timeUnitSpinner = view.findViewById(R.id.timeUnitSpinner)
        lineChart = view.findViewById(R.id.lineChart)
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

    @SuppressLint("SetTextI18n")
    private fun handleFilterOrderListSuccess(orders: ArrayList<Order>, timeUnit: TimeUnit) {
        progressBar.visibility = View.GONE
        var totalPrice = 0f
        if (orders.size == 0) {
            lineChart.visibility = View.GONE
            totalPriceTextView.visibility = View.GONE
            emptyOrderTextView.visibility = View.VISIBLE
        } else {
            lineChart.visibility = View.VISIBLE
            totalPriceTextView.visibility = View.VISIBLE
            emptyOrderTextView.visibility = View.GONE

            val entries: ArrayList<Entry> = ArrayList()
            val labels: ArrayList<String> = ArrayList()

            var i = 0f
            orders.forEach {
                entries.add(Entry(i++, it.totalPrice(), ""))
                totalPrice += it.totalPrice()
                when (timeUnit) {
                    TimeUnit.day -> labels.add(it.date.formatTime())
                    TimeUnit.weak -> labels.add(it.date.getNameOfDayInWeek())
                    TimeUnit.month -> labels.add(it.date.getNameOfDayAndMonth())
                    TimeUnit.year -> labels.add(it.date.getNameOfMonth())
                    else -> labels.add(it.date.formatTime())
                }
            }

            // set total price with a type selection:
            totalPriceTextView.text = "${context.getResourceText(R.string.totalPrice)}: ${totalPrice.formatCurrency()}"

            // set line chart
            val set1 = LineDataSet(entries, context.getResourceText(R.string.revenueStatistics))
            set1.color = Color.BLUE
            set1.setCircleColor(Color.BLUE)
            set1.lineWidth = 1f
            set1.circleRadius = 3f
            set1.setDrawCircleHole(false)
            set1.valueTextSize = 0f
            set1.setDrawFilled(false)
            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1)
            val data = LineData(dataSets)

            lineChart.data = data
            lineChart.description.isEnabled = false
            lineChart.xAxis.enableGridDashedLine(5f, 5f, 0f)
            lineChart.axisRight.enableGridDashedLine(5f, 5f, 0f)
            lineChart.axisLeft.enableGridDashedLine(5f, 5f, 0f)
            lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            lineChart.animateY(1500)
        }
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