package com.elfan.mynews.ui.login

import android.content.Intent
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.elfan.mynews.R
import com.elfan.mynews.ui.main.MainActivity
import com.elfan.mynews.ui.register.RegisterActivity
import com.elfan.mynews.utils.Constants
import com.elfan.mynews.utils.extensions.gone
import com.elfan.mynews.utils.extensions.visible
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.ed_email
import kotlinx.android.synthetic.main.activity_login.ed_password
import kotlinx.android.synthetic.main.activity_login.loading
import kotlinx.android.synthetic.main.activity_login.til_email
import kotlinx.android.synthetic.main.activity_login.til_password
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        observer()
        doLogin()
        register()
    }

    private fun register() {
        btn_register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun observer() = loginViewModel.listenToState().observer(this, Observer { handleUiState(it) })


    private fun handleUiState(it : LoginState) {
        when(it){
            is LoginState.Loading -> handleLoading(it.state)
            is LoginState.ShowToast -> toast(it.message)
            is LoginState.Success -> handleSuccess(it.token)
            is LoginState.Reset -> handleReset()
            is LoginState.Validate -> handleValidate(it)
        }
    }

    private fun handleValidate(validate: LoginState.Validate) {
        validate.email?.let { setEmailErr(it) }
        validate.password?.let { setPasswordErr(it) }
    }

    private fun handleReset() {
        setEmailErr(null)
        setPasswordErr(null)
    }

    private fun handleSuccess(token  : String) {
        Constants.setToken(this@LoginActivity, "Bearer $token")
        if (getExpectResult()){
            finish()
        }else{
            startActivity(Intent(this@LoginActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }).also { finish() }
        }
    }

    private fun doLogin(){
        btn_login.setOnClickListener {
            val email = ed_email.text.toString().trim()
            val password = ed_password.text.toString().trim()
            if (loginViewModel.validate(email, password)){
                loginViewModel.login(email, password)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        btn_login.isEnabled = !state
        if (state) {
            loading.visible()
        } else {
            loading.gone()
        }
    }
    private fun getExpectResult() = intent.getBooleanExtra("EXPECT_RESULT", false)
    private fun setEmailErr(err : String?) { til_email.error = err }
    private fun setPasswordErr(err : String?) { til_password.error = err }
    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    override fun onResume() {
        super.onResume()
        if (!Constants.getToken(this@LoginActivity).equals("UNDEFINED")){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }
}
