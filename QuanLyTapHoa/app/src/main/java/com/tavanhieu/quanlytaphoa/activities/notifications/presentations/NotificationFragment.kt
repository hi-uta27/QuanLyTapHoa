package com.tavanhieu.quanlytaphoa.activities.notifications.presentations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.notifications.adapter.NotificationAdapter
import com.tavanhieu.quanlytaphoa.activities.notifications.domain.infra.NotificationUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.notifications.domain.use_cases.NotificationsUseCase
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import com.tavanhieu.quanlytaphoa.commons.models.Notification

class NotificationFragment(val context: BaseActivity) : Fragment() {
    private lateinit var recycleView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyTextView: TextView

    private val notificationsUseCase: NotificationsUseCase by lazy { NotificationUseCaseImpl() }
    private val adapter: NotificationAdapter by lazy { NotificationAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_notifications, container, false)
        mappingViewId(view)
        readNotification()

        return view
    }

    private fun mappingViewId(view: View) {
        recycleView = view.findViewById(R.id.recycleView)
        progressBar = view.findViewById(R.id.progressBar)
        emptyTextView = view.findViewById(R.id.emptyTextView)
    }

    private fun readNotification() {
        progressBar.visibility = View.VISIBLE
        notificationsUseCase.readNotification({
            readNotificationSuccess(it)
        }, {
            readNotificationFailure()
        })
    }

    private fun readNotificationSuccess(notifications: ArrayList<Notification>) {
        progressBar.visibility = View.GONE
        if (notifications.size == 0) {
            emptyTextView.visibility = View.VISIBLE
        } else {
            emptyTextView.visibility = View.GONE
        }
        adapter.setData(context, notifications)
        recycleView.adapter = adapter
    }

    private fun readNotificationFailure() {
        progressBar.visibility = View.GONE
        context.showAlertDialog(context.getResourceText(R.string.error),
            context.getResourceText(R.string.readDepotFailure),
            context.getResourceText(R.string.tryAgain)
        ) {
            readNotification()
        }
    }
}