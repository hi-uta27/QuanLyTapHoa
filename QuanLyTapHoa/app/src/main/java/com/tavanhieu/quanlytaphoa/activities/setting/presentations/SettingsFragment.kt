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
import com.tavanhieu.quanlytaphoa.activities.login.presentations.LoginActivity
import com.tavanhieu.quanlytaphoa.activities.logout.domain.infra.LogoutUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.logout.domain.use_case.LogoutUseCase
import com.tavanhieu.quanlytaphoa.activities.logout.presentations.LogoutActivity
import com.tavanhieu.quanlytaphoa.activities.user_info.domain.infra.UserInfoUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.user_info.domain.use_case.UserInfoUseCase
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import com.tavanhieu.quanlytaphoa.commons.models.Employee

class SettingsFragment(val context: BaseActivity) : Fragment() {
    private lateinit var userInfoLinearLayout: LinearLayout
    private lateinit var logoutLinearLayout: LinearLayout
    private lateinit var userNameTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var emailTextView: TextView

    private val logoutUseCase: LogoutUseCase by lazy { LogoutUseCaseImpl() }
    private val userInfoUseCase: UserInfoUseCase by lazy { UserInfoUseCaseImpl() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_setting, container, false)
        mappingViewId(view)
        requestUserInfo()
        handleClickOnView()

        return view
    }

    private fun mappingViewId(view: View) {
        userInfoLinearLayout = view.findViewById(R.id.userInfoLinearLayout)
        logoutLinearLayout = view.findViewById(R.id.logoutLinearLayout)
        userNameTextView = view.findViewById(R.id.userNameTextView)
        progressBar = view.findViewById(R.id.progressBar)
        emailTextView = view.findViewById(R.id.emailTextView)
    }

    private fun handleClickOnView() {
        userInfoLinearLayout.setOnClickListener {
            // open user info
        }

        logoutLinearLayout.setOnClickListener {
            logoutUseCase.logoutCurrentUser()
            context.startActivity(Intent(context, LoginActivity::class.java))
            context.finish()
        }
    }

    // --------------------------------------------------------------------------

    private fun requestUserInfo() {
        progressBar.visibility = View.VISIBLE
        userInfoUseCase.requestUseInfo({
            requestUserInfoSuccess(it)
        }) {
            requestUserInfoFailure()
        }
    }

    private fun requestUserInfoSuccess(employee: Employee?) {
        progressBar.visibility = View.GONE
        if (employee != null) {
            userNameTextView.text = employee.name
            emailTextView.text = employee.email
        }
    }

    private fun requestUserInfoFailure() {
        progressBar.visibility = View.GONE
        context.showAlertDialog(context.getResourceText(R.string.error),
            context.getResourceText(R.string.readDepotFailure),
            context.getResourceText(R.string.tryAgain)
        ) {
            requestUserInfo()
        }
    }
}