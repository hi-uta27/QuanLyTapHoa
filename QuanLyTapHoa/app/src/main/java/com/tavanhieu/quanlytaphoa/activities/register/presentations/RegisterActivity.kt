package com.tavanhieu.quanlytaphoa.activities.register.presentations

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.util.Patterns
import android.view.View
import android.widget.*
import com.tavanhieu.quanlytaphoa.R
import com.tavanhieu.quanlytaphoa.activities.logout.domain.infra.LogoutUseCaseImpl
import com.tavanhieu.quanlytaphoa.activities.logout.domain.use_case.LogoutUseCase
import com.tavanhieu.quanlytaphoa.activities.register.domain.infra.RegisterUseCaseImpl
import com.tavanhieu.quanlytaphoa.commons.models.Employee
import com.tavanhieu.quanlytaphoa.activities.register.domain.use_cases.RegisterUseCase
import com.tavanhieu.quanlytaphoa.commons.base.BaseActivity
import com.tavanhieu.quanlytaphoa.commons.base.showAlertDialog
import com.tavanhieu.quanlytaphoa.data_network_layer.FirebaseNetworkLayer
import java.text.SimpleDateFormat
import java.util.Calendar

class RegisterActivity : BaseActivity() {
    private lateinit var backImage: ImageView
    private lateinit var emailEditText: EditText
    private lateinit var userNameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var sendAgainButton: Button
    private lateinit var countTimerTextView: TextView
    private lateinit var backgroundSendAgainLinearLayout: LinearLayout

    private val registerUseCase: RegisterUseCase by lazy { RegisterUseCaseImpl() }
    private val logoutUseCase: LogoutUseCase by lazy { LogoutUseCaseImpl() }
    private val timer: CountDownTimer by lazy { object: CountDownTimer(60000, 1000) {
        @SuppressLint("SimpleDateFormat")
        override fun onTick(p0: Long) {
            countTimerTextView.text = SimpleDateFormat("mm:ss").format(p0)
        }

        @SuppressLint("SetTextI18n")
        override fun onFinish() {
            countTimerTextView.text = "00:00"
            sendAgainButton.isEnabled = true
        }
    }}

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
        sendAgainButton = findViewById(R.id.sendAgainButton)
        countTimerTextView = findViewById(R.id.countTimerTextView)
        backgroundSendAgainLinearLayout = findViewById(R.id.backgroundSendAgainLinearLayout)
    }

    override fun configLayout() {
        handleClickOnView()
    }

    // MARK: - Function created

    private fun handleClickOnView() {
        backImage.setOnClickListener { finish() }
        registerButton.setOnClickListener { handleRegister() }
        sendAgainButton.setOnClickListener { sendVerifiedEmail() }
    }

    private fun handleRegister() {
        if (checkNullOrEmptyWithText(emailEditText)) {
            showErrorWithEditText(emailEditText, getResourceText(R.string.noEmail))
        } else if (checkNullOrEmptyWithText(userNameEditText)) {
            showErrorWithEditText(userNameEditText, getResourceText(R.string.noName))
        } else if (checkNullOrEmptyWithText(passwordEditText)) {
            showErrorWithEditText(passwordEditText, getResourceText(R.string.noPassword))
        } else if (checkNullOrEmptyWithText(confirmPasswordEditText)) {
            showErrorWithEditText(confirmPasswordEditText, getResourceText(R.string.noPassword))
        } else {
            val email = emailEditText.text.trim().toString()
            val userName = userNameEditText.text.trim().toString()
            val password = passwordEditText.text.trim().toString()
            val confirmPassword = confirmPasswordEditText.text.trim().toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showErrorWithEditText(emailEditText, getResourceText(R.string.IncorectEmailFormat))
            } else if (password != confirmPassword) {
                showErrorWithEditText(confirmPasswordEditText, getResourceText(R.string.passwordDontMatch))
            } else {
                progressBar.visibility = View.VISIBLE
                registerUseCase.registerWith(email, password, {
                        val employee = Employee(
                            FirebaseNetworkLayer.instance.requestCurrentUserUID(),
                            email,
                            userName,
                            Calendar.getInstance().time
                        )
                        registerSuccess(employee)
                    }, {
                        registerFailure()
                    })
            }
        }
    }

    private fun registerSuccess(employee: Employee) {
        progressBar.visibility = View.GONE
        deleteInput()
        sendVerifiedEmail()
        registerUseCase.addToDatabase(employee, {}, { registerFailure() })
    }

    private fun sendVerifiedEmail() {
        progressBar.visibility = View.VISIBLE
        registerUseCase.sendVerifiedEmail({
            progressBar.visibility = View.GONE
            showAlertDialog(getResourceText(R.string.notification), getResourceText(R.string.weJustSendVerifiedToYourEmail)) {
                backgroundSendAgainLinearLayout.visibility = View.VISIBLE
                sendAgainButton.isEnabled = false
                timer.start()
            }
        }, {
            progressBar.visibility = View.GONE
        })
    }

    private fun registerFailure() {
        progressBar.visibility = View.GONE
        showAlertDialog(getResourceText(R.string.error), getResourceText(R.string.registerFailed), getResourceText(R.string.tryAgain)) {
            handleRegister()
        }
    }

    private fun deleteInput() {
        emailEditText.setText("")
        userNameEditText.setText("")
        passwordEditText.setText("")
        confirmPasswordEditText.setText("")
        emailEditText.requestFocus()
    }
}