package com.tavanhieu.quanlytaphoa.activities.statistics.domain.infra

import com.tavanhieu.quanlytaphoa.activities.statistics.domain.enum.TimeUnit
import com.tavanhieu.quanlytaphoa.activities.statistics.domain.use_cases.StatisticUseCase
import java.util.*

class StatisticUseCaseImpl: StatisticUseCase {
    override fun filterOrderListBy(
        timeLine: Date,
        timeUnit: TimeUnit,
        complete: () -> Unit,
        failure: () -> Unit
    ) {
        TODO("Not yet implemented")
    }
}