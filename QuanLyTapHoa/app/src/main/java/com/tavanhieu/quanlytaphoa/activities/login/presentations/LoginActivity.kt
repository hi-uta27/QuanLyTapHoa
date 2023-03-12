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
import com.tavanhieu.quanlytaphoa.MainActivity
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.activities.login.domain.infra.AuthenticationUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.login.domain.use_case.AuthenticationUseCase
import com.tavanhieu.quanlytaphoa.commons.base.showErrorDialog

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
        if (!handleNullOrEmptyWithText(emailEditText, "Chưa nhập email")
            && !handleNullOrEmptyWithText(passwordEditText, "Chưa nhập mật khẩu")
        ) {
            val email = emailEditText.text.trim().toString()
            val password = passwordEditText.text.trim().toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.error = "Định dạng email chưa đúng"
                emailEditText.requestFocus()
            } else {
                progressBar.visibility = View.VISIBLE
                authenticationUseCase.loginWith(email, password, {
                    progressBar.visibility = View.GONE
                    loginSuccess()
                }, {
                    progressBar.visibility = View.GONE
                    loginFailure()
                })
            }
        }
    }

    private fun loginSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun loginFailure() {
        showErrorDialog("Thông báo", "Đăng nhập thất bại!", "Thử lại") {
            handleLogin()
        }
    }
}