package com.tavanhieu.quanlytaphoa.activities.statistics.domain.use_cases

import com.tavanhieu.quanlytaphoa.activities.statistics.domain.enum.TimeUnit
import com.tavanhieu.quanlytaphoa.commons.models.Order
import java.util.Date

interface StatisticUseCase {
    fun filterOrderListBy(timeLine: Date, timeUnit: TimeUnit, complete: (ArrayList<Order>) -> Unit, failure: () -> Unit)
}