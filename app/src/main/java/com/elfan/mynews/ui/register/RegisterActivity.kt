package com.elfan.mynews.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Observer
import com.elfan.mynews.R
import com.elfan.mynews.ui.login.LoginActivity
import com.elfan.mynews.utils.extensions.gone
import com.elfan.mynews.utils.extensions.visible
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.ed_email
import kotlinx.android.synthetic.main.activity_register.ed_password
import kotlinx.android.synthetic.main.activity_register.til_email
import kotlinx.android.synthetic.main.activity_register.til_password
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val registerViewModel : RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        observer()
        txt_login.setOnClickListener { startActivity(Intent(this@RegisterActivity, LoginActivity::class.java)) }
        register()
    }

    private fun register() {
        btn_register.setOnClickListener {
            val name = ed_name.text.toString().trim()
            val email = ed_email.text.toString().trim()
            val password = ed_password.text.toString().trim()
            val confirmPass = ed_password_confirm.text.toString().trim()
            if (registerViewModel.validate(name, email, password, confirmPass)){
                registerViewModel.register(name, email, password)
            }
        }
    }

    private fun handleUIState(it : RegisterState){
        when(it){
            is RegisterState.Validate -> handleValidate(it)
            is RegisterState.ShowToast -> toast(it.message)
            is RegisterState.Reset -> handleReset()
            is RegisterState.Loading -> handleLoading(it.state)
            is RegisterState.Success -> success()
        }
    }

    private fun handleLoading(state: Boolean) {
        btn_register.isEnabled = !state
        if (state) {
            loading.visible()
        } else {
            loading.gone()
        }
    }

    private fun handleReset() {
        setNameError(null)
        setEmailError(null)
        setPasswordError(null)
        setConfirmPasswordError(null)
    }

    private fun handleValidate(validate: RegisterState.Validate) {
        validate.name?.let { setNameError(it) }
        validate.email?.let { setEmailError(it) }
        validate.password?.let { setPasswordError(it) }
        validate.confirmPassword?.let { setConfirmPasswordError(it) }
    }

    private fun observer() = registerViewModel.listenToState().observe(this, Observer { handleUIState(it) })
    private fun toast(message : String) = Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    private fun setNameError(err : String?){ til_name.error = err }
    private fun setEmailError(err : String?){ til_email.error = err }
    private fun setPasswordError(err : String?){ til_password.error = err }
    private fun setConfirmPasswordError(err : String?){ til_password_confirm.error = err }
    private fun success() {
        AlertDialog.Builder(ContextThemeWrapper(this, R.style.ThemeOverlay_AppCompat_Dialog_Alert)).apply {
            setMessage("berhasil regitser, silahkan login")
            setPositiveButton("Mengerti"){ d, _ ->
                d.cancel()
                finish()
            }.create().show()
        }
    }

}