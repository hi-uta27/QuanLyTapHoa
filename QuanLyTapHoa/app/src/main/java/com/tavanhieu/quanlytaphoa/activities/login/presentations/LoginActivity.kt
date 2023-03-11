package com.tavanhieu.quanlytaphoa.activities.login.presentations

import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.activities.login.domain.infra.AuthenticationUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.login.domain.use_case.AuthenticationUseCase
import java.util.logging.SimpleFormatter
import java.util.regex.Pattern

class LoginActivity : BaseActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var rememberAccountCheckBox: CheckBox
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView

    private val authenticationUseCase: AuthenticationUseCase by lazy { AuthenticationUseCaseImpl() }

    override fun setContentView() {
        setContentView(R.layout.activity_login)
    }

    override fun mappingViewId() {
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        rememberAccountCheckBox = findViewById(R.id.rememberPasswordCheckBox)
        registerTextView = findViewById(R.id.registerTextView)
        loginButton = findViewById(R.id.loginButton)
    }

    override fun configLayout() {
        handleClickOnView()
    }

    private fun handleClickOnView() {
        loginButton.setOnClickListener { handleLogin() }
    }

    private fun handleLogin() {
        if (checkNullOrEmptyWith(emailEditText, "Chưa nhập email")
            && checkNullOrEmptyWith(passwordEditText, "Chưa nhập mật khẩu")
        ) {
            val email = emailEditText.text.trim().toString()
            val password = passwordEditText.text.trim().toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.error = "Định dạng email chưa đúng"
                emailEditText.requestFocus()
            } else {
                authenticationUseCase.loginWith(
                    email,
                    password,
                    { loginSuccess() },
                    { loginFailure() })
            }
        }

    }

    private fun loginSuccess() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
    }

    private fun loginFailure() {
        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
    }
}