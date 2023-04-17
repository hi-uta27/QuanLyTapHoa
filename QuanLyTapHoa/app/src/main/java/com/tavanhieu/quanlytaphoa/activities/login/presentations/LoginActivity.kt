package com.tavanhieu.quanlytaphoa.activities.login.presentations

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.analytics.FirebaseAnalytics
import com.tavanhieu.quanlytaphoa.BuildConfig
import com.tavanhieu.quanlytaphoa.activities.MainActivity
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.activities.login.domain.infra.AuthenticationUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.login.domain.use_case.AuthenticationUseCase
import com.tavanhieu.quanlytaphoa.activities.register.presentations.RegisterActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer

class LoginActivity : BaseActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var rememberAccountCheckBox: CheckBox
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView
    private lateinit var progressBar: ProgressBar

    private val authenticationUseCase: AuthenticationUseCase by lazy { AuthenticationUseCaseImpl() }
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("LoginActivity_Account", MODE_PRIVATE)
    }

    // MARK: - Function System

    override fun setContentView() {
        setContentView(R.layout.activity_login)

        //
        if (BuildConfig.DEBUG) {
            FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(false)
        } else {
            FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true)
        }

        // open main activity if user logged before
        if (FirebaseNetworkLayer.instance.authIsLogged()) {
            FirebaseNetworkLayer.instance.checkVerifiedEmail({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, {})
        }
    }

    override fun mappingViewId() {
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        rememberAccountCheckBox = findViewById(R.id.rememberPasswordCheckBox)
        registerTextView = findViewById(R.id.registerTextView)
        loginButton = findViewById(R.id.loginButton)
        progressBar = findViewById(R.id.progressBar)
    }

    override fun configLayout() {
        handleClickOnView()

        // update account if save account
        emailEditText.setText(sharedPreferences.getString("Email", ""))
        passwordEditText.setText(sharedPreferences.getString("Password", ""))
        val email = emailEditText.text.trim().toString()
        if (!(email == "" || email.isEmpty())) {
            rememberAccountCheckBox.isChecked = true
        }
    }

    private fun handleClickOnView() {
        loginButton.setOnClickListener { handleLogin() }
        rememberAccountCheckBox.setOnClickListener { saveAccountLogin() }
        registerTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onPause() {
        super.onPause()
        saveAccountLogin()
    }

    // MARK: - Function Created

    @SuppressLint("CommitPrefEdits")
    private fun saveAccountLogin() {
        if (rememberAccountCheckBox.isChecked) {
            sharedPreferences.edit().putString("Email", emailEditText.text.trim().toString())
                .apply()
            sharedPreferences.edit().putString("Password", passwordEditText.text.trim().toString())
                .apply()
        } else {
            sharedPreferences.edit().remove("Email").apply()
            sharedPreferences.edit().remove("Password").apply()
        }
    }

    private fun handleLogin() {
        if (checkNullOrEmptyWithText(emailEditText)) {
            showErrorWithEditText(emailEditText, getResourceText(R.string.noEmail))
        } else if (checkNullOrEmptyWithText(passwordEditText)) {
            showErrorWithEditText(passwordEditText, getResourceText(R.string.noPassword))
        } else {
            val email = emailEditText.text.trim().toString()
            val password = passwordEditText.text.trim().toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showErrorWithEditText(emailEditText, getResourceText(R.string.IncorectEmailFormat))
            } else {
                progressBar.visibility = View.VISIBLE
                // TODO: I'll update it with domain after
                authenticationUseCase.loginWith(email, password, {
                    FirebaseNetworkLayer.instance.checkVerifiedEmail({
                        loginSuccess()
                    }, {
                        progressBar.visibility = View.GONE
                        showAlertDialog(
                            getResourceText(R.string.notification),
                            getResourceText(R.string.weJustSendVerifiedToYourEmail),
                            getResourceText(R.string.sendAgain)
                        ) {
                            FirebaseNetworkLayer.instance.sendVerifiedEmail({}, {})
                        }
                    })
                }, {
                    loginFailure()
                })
            }
        }
    }

    private fun loginSuccess() {
        progressBar.visibility = View.GONE
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun loginFailure() {
        progressBar.visibility = View.GONE
        showAlertDialog(getResourceText(R.string.notification), getResourceText(R.string.loginFailed), getResourceText(R.string.tryAgain)) {
            handleLogin()
        }
    }
}