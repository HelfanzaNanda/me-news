package com.elfan.mynews.ui.login

import androidx.lifecycle.ViewModel
import com.elfan.mynews.models.User
import com.elfan.mynews.repositories.UserRepository
import com.elfan.mynews.utils.Constants
import com.elfan.mynews.utils.SingleLiveEvent
import com.elfan.mynews.utils.SingleResponse

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val state: SingleLiveEvent<LoginState> = SingleLiveEvent()
    private fun isLoading(b : Boolean) { state.value = LoginState.Loading(b) }
    private fun toast(message: String){ state.value = LoginState.ShowToast(message) }
    private fun success(token: String){ state.value = LoginState.Success(token) }

    fun login(email: String, password: String) {
        isLoading(true)
        userRepository.login(email, password, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                isLoading(false)
                data?.let { success(it.token!!) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun validate(email: String, password: String): Boolean{
        state.value = LoginState.Reset

        if(email.isEmpty() ){
            state.value = LoginState.Validate(email = "email tidak boleh kosong")
            return false
        }
        if (!Constants.isValidEmail(email)){
            state.value = LoginState.Validate(email = "email tidak valid")
            return false
        }

        if (password.isEmpty()){
            state.value = LoginState.Validate(password = "password tidak boleh kosong")
            return false
        }

        if (!Constants.isValidPassword(password)){
            state.value = LoginState.Validate(password = "password minimal 8 karakter")
            return false
        }

        return true
    }

    fun listenToState() = state
}

sealed class LoginState{
    data class Loading(var state : Boolean = false) : LoginState()
    data class ShowToast(var message : String) : LoginState()
    data class Success(var token : String) : LoginState()
    object Reset : LoginState()
    data class Validate(
        var email : String? = null,
        var password : String? = null
    ) : LoginState()
}