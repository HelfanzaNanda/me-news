package com.elfan.mynews.ui.register

import androidx.lifecycle.ViewModel
import com.elfan.mynews.models.User
import com.elfan.mynews.repositories.UserRepository
import com.elfan.mynews.utils.Constants
import com.elfan.mynews.utils.SingleLiveEvent
import com.elfan.mynews.utils.SingleResponse

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel(){

    private var state : SingleLiveEvent<RegisterState> = SingleLiveEvent()
    private fun isLoading(b : Boolean) { state.value = RegisterState.Loading(b) }
    private fun toast(message: String) { state.value = RegisterState.ShowToast(message) }
    private fun success() { state.value = RegisterState.Success }

    fun register(name: String, email: String, password: String){
        isLoading(true)
        userRepository.register(name, email, password, object : SingleResponse<User> {
            override fun onSuccess(data: User?) {
                isLoading(false)
                data?.let { success() }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { error ->  toast(error.message.toString()) }
            }
        })
    }

    fun validate(name: String, email: String, password: String, confirmPassword : String): Boolean{
        state.value = RegisterState.Reset
        if (name.isEmpty()){
            state.value = RegisterState.Validate(name = "nama tidak boleh kosong")
            return false
        }

        if (!Constants.isAlpha(name)){
            state.value = RegisterState.Validate(name = "nama hanya mengandung huruf saja")
            return false
        }

        if (email.isEmpty()){
            state.value = RegisterState.Validate(email = "email tidak boleh kosong")
            return false
        }

        if (!Constants.isValidEmail(email)){
            state.value = RegisterState.Validate(email = "email tidak valid")
            return false
        }

        if (password.isEmpty()){
            state.value = RegisterState.Validate(password ="password tidak boleh kosong")
            return false
        }
        if (!Constants.isValidPassword(password)){
            state.value = RegisterState.Validate(password = "password minimal 8 karakter")
            return false
        }
        if (confirmPassword.isEmpty()){
            state.value = RegisterState.Validate(confirmPassword = "konfirmasi password tidak boleh kosong")
            return false
        }
        if(confirmPassword != password){
            state.value = RegisterState.Validate(confirmPassword = "Konfirmasi password tidak cocok")
            return false
        }
        return true
    }

    fun listenToState() = state
}

sealed class RegisterState{
    object Reset : RegisterState()
    data class Loading (var state : Boolean = false) : RegisterState()
    data class ShowToast(var message : String) : RegisterState()
    data class Validate(
        var name : String? = null,
        var email : String? = null,
        var password : String? = null,
        var confirmPassword : String? = null
    ) : RegisterState()
    object Success : RegisterState()
}