package com.udacoding.pos.ui.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacoding.pos.repository.RepositoryUser
import com.udacoding.pos.ui.login.model.ResponseLogin
import com.udacoding.pos.utils.validationEmail

class LoginViewModel: ViewModel() {

    private val repository = RepositoryUser()

    private val _login = MutableLiveData<ResponseLogin>()
    val login: LiveData<ResponseLogin> = _login

    private val _email_invalid = MutableLiveData<Boolean>()
    val email_invalid: LiveData<Boolean> = _email_invalid

    private val _require_email = MutableLiveData<Boolean>()
    val require_email: LiveData<Boolean> = _require_email

    private val _require_password = MutableLiveData<Boolean>()
    val require_password: LiveData<Boolean> = _require_password

    private val _require = MutableLiveData<Boolean>()
    val require: LiveData<Boolean> = _require

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    fun login(
        email: String?,
        password: String?
    ){
        _loading.value = true
        if (email?.equals("") == true) {
            _require_email.value = true
            _require_password.value = false
            _loading.value = false
        } else if (!validationEmail(email)) {
            _email_invalid.value = true
            _loading.value = false
        } else if (password?.equals("") == true) {
            _require_password.value = true
            _require_email.value = false
            _loading.value = false
        } else if (email?.equals("") == true && password?.equals("") == true) {
            _require.value = true
            _loading.value = false
        } else {
            repository.repoLogin(
                email,
                password,
                {
                    _loading.value = false
                    _login.value = it
                }, {
                    _loading.value = false
                    _error.value = it
                }
            )
        }
    }

}