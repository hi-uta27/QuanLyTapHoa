package com.tavanhieu.quanlytaphoa.activities.setting.presentations

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.logout.domain.infra.LogoutUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.logout.domain.use_case.LogoutUseCase
import com.tavanhieu.quanlytaphoa.activities.logout.presentations.LogoutActivity
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity

class SettingsFragment(val context: BaseActivity) : Fragment() {
    private lateinit var userInfoLinearLayout: LinearLayout
    private lateinit var logoutLinearLayout: LinearLayout
    private lateinit var userNameTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var emailTextView: TextView

    private val logoutUseCase: LogoutUseCase by lazy { LogoutUseCaseImpl() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_setting, container, false)
        mappingViewId(view)
        readUserInfo()
        handleClickOnView()

        return view
    }

    private fun handleClickOnView() {
        userInfoLinearLayout.setOnClickListener {
            // open user info
        }

        logoutLinearLayout.setOnClickListener {
            logoutUseCase.logoutCurrentUser()
            context.startActivity(Intent(context, LogoutActivity::class.java))
            context.finish()
        }
    }

    private fun readUserInfo() {
        //
    }

    private fun mappingViewId(view: View) {
        userInfoLinearLayout = view.findViewById(R.id.userInfoLinearLayout)
        logoutLinearLayout = view.findViewById(R.id.logoutLinearLayout)
        userNameTextView = view.findViewById(R.id.userNameTextView)
        progressBar = view.findViewById(R.id.progressBar)
        emailTextView = view.findViewById(R.id.emailTextView)
    }
}