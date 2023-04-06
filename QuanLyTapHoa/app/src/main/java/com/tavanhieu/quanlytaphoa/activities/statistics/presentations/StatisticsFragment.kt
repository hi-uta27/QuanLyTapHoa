package com.tavanhieu.quanlytaphoa.activities.statistics.presentations

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.commons.formatDate
import java.util.Calendar

class StatisticsFragment: Fragment() {
    private lateinit var timeLineDateTextView: TextView
    private lateinit var calendarImageButton: ImageButton
    private lateinit var timeUnitSpinner: Spinner
    private lateinit var barChart: BarChart

    private val timeLineDate: Calendar by lazy { Calendar.getInstance() }
    private val timeUnits = listOf<String>("Ngày", "Tuần", "Tháng", "Năm")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_statistics, container, false)
        mappingViewId(view)
        handleClickOnView()
        timeLineDateTextView.text = timeLineDate.time.formatDate()
        timeUnitSpinner.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_list_item_1,
            timeUnits
        )

        return view
    }

    private fun mappingViewId(view: View) {
        timeLineDateTextView = view.findViewById(R.id.timeLineDateTextView)
        calendarImageButton = view.findViewById(R.id.calenderImageButton)
        timeUnitSpinner = view.findViewById(R.id.timeUnitSpinner)
        barChart = view.findViewById(R.id.barChart)
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
            },
            timeLineDate.get(Calendar.YEAR),
            timeLineDate.get(Calendar.MONTH),
            timeLineDate.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}