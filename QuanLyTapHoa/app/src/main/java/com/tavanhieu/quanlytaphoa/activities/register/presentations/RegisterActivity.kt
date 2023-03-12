package com.tavanhieu.quanlytaphoa.activities.register.presentations

import android.util.Patterns
import android.widget.*
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.register.domain.infra.RegisterUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.register.domain.use_cases.RegisterUseCase
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity

class RegisterActivity : BaseActivity() {
    private lateinit var backImage: ImageView
    private lateinit var emailEditText: EditText
    private lateinit var userNameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var progressBar: ProgressBar

    private val registerUseCase: RegisterUseCase by lazy { RegisterUseCaseImpl() }

    // MARK: - Function in base
    override fun setContentView() {
        setContentView(R.layout.activity_register)
    }

    override fun mappingViewId() {
        backImage = findViewById(R.id.backImage)
        emailEditText = findViewById(R.id.emailEditText)
        userNameEditText = findViewById(R.id.userNameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        registerButton = findViewById(R.id.registerButton)
        progressBar = findViewById(R.id.progressBar)
    }

    override fun configLayout() {
        handleClickOnView()
    }

    // MARK: - Function created

    private fun handleClickOnView() {
        backImage.setOnClickListener { finish() }
        registerButton.setOnClickListener { handleRegister() }
    }

    private fun handleRegister() {
        if (checkNullOrEmptyWithText(emailEditText)) {
            showErrorWithText(emailEditText, "Chưa nhập email")
        } else if (checkNullOrEmptyWithText(userNameEditText)) {
            showErrorWithText(userNameEditText, "Chưa nhập họ tên")
        } else if (checkNullOrEmptyWithText(passwordEditText)) {
            showErrorWithText(passwordEditText, "Mật khẩu trống")
        } else if (checkNullOrEmptyWithText(confirmPasswordEditText)) {
            showErrorWithText(confirmPasswordEditText, "Xác nhận mật khẩu trống")
        } else {
            val email = emailEditText.text.trim().toString()
            val userName = userNameEditText.text.trim().toString()
            val password = passwordEditText.text.trim().toString()
            val confirmPassword = confirmPasswordEditText.text.trim().toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showErrorWithText(emailEditText, "Định dạng email chưa đúng")
            } else if (password != confirmPassword) {
                showErrorWithText(confirmPasswordEditText, "Mật khẩu không khớp")
            } else {
                registerUseCase.registerWith("", "", { registerSuccess() }, { registerFailure() })
            }
        }
    }

    private fun registerSuccess() {
    }

    private fun registerFailure() {
    }
}