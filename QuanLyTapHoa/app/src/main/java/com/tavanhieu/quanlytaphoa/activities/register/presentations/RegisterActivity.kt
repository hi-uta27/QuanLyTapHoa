package com.tavanhieu.quanlytaphoa.activities.register.presentations

import android.util.Patterns
import android.view.View
import android.widget.*
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.logout.infra.LogoutUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.logout.use_case.LogoutUseCase
import com.tavanhieu.quanlytaphoa.activities.register.domain.infra.RegisterUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.register.domain.models.Employee
import com.tavanhieu.quanlytaphoa.activities.register.domain.use_cases.RegisterUseCase
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showErrorDialog
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer
import java.util.Calendar

class RegisterActivity : BaseActivity() {
    private lateinit var backImage: ImageView
    private lateinit var emailEditText: EditText
    private lateinit var userNameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var progressBar: ProgressBar

    private val registerUseCase: RegisterUseCase by lazy { RegisterUseCaseImpl() }
    private val logoutUseCase: LogoutUseCase by lazy { LogoutUseCaseImpl() }

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
                progressBar.visibility = View.VISIBLE
                registerUseCase.registerWith(email, password,
                    {
                        val employee = Employee(
                            FirebaseNetworkLayer.instance.requestCurrentUserUID(),
                            email,
                            userName,
                            Calendar.getInstance().time
                        )
                        registerSuccess(employee)
                    }, {
                        progressBar.visibility = View.GONE
                        registerFailure()
                    })
            }
        }
    }

    private fun registerSuccess(employee: Employee) {
        registerUseCase.addToDatabase(employee, "Employee",
            { // add user to firebase success
                progressBar.visibility = View.GONE
                showToast("Đăng ký thành công")
                logoutUseCase.logoutCurrentUser()
                finish()
            }, {})
    }

    private fun registerFailure() {
        showErrorDialog("Lỗi", "Đăng ký không thành công", "Thử lại") {
            handleRegister()
        }
    }
}