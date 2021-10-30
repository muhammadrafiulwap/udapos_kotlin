package com.udacoding.pos.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.udacoding.pos.MainActivity
import com.udacoding.pos.R
import com.udacoding.pos.SessionManager
import com.udacoding.pos.databinding.ActivityLoginBinding
import com.udacoding.pos.ui.login.model.ResponseLogin
import com.udacoding.pos.ui.login.viewmodel.LoginViewModel
import com.udacoding.pos.utils.*

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    lateinit var viewModel: LoginViewModel

    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        initView()

        attachObserve()

    }

    private fun attachObserve() {
        with(viewModel) {
            login.observe(this@LoginActivity, { actionLogin(it) })

            require.observe(this@LoginActivity, { actionRequire(it) })

            email_invalid.observe(this@LoginActivity, { actionEmailInvalid(it) })

            require_email.observe(this@LoginActivity, { actionRequireEmail(it) })

            require_password.observe(this@LoginActivity, { actionRequirePassword(it) })

            loading.observe(
                this@LoginActivity, {
                    actionLoading(it)
                })

            error.observe(this@LoginActivity, {
                    showError(this@LoginActivity, it)
            })
        }
    }

    private fun initView() {

        validateEmail()

        with(binding) {
            button.setOnClickListener {
                viewModel.login(inputEmail.text.toString(), inputPassword.text.toString())
            }
        }

    }

    private fun actionLoading(it: Boolean?) {
        if (it == true) {
            binding.progressBar.show()
            binding.button.hide()
        } else {
            binding.progressBar.hide()
            binding.button.show()
        }
    }

    private fun actionRequire(it: Boolean?) {
        if (it == true) {
            binding.textInputLayoutEmail.error = getString(R.string.email_required)
            binding.textInputLayoutPassword.error = getString(R.string.password_required)
        } else {
            binding.textInputLayoutEmail.isErrorEnabled = false
            binding.textInputLayoutPassword.isErrorEnabled = false
        }
    }

    private fun actionRequireEmail(it: Boolean?) {
        if (it == true) {
            binding.textInputLayoutEmail.error = getString(R.string.email_required)
        } else {
            binding.textInputLayoutEmail.isErrorEnabled = false
        }
    }

    private fun actionEmailInvalid(it: Boolean?) {
        if (it == true) {
            binding.textInputLayoutEmail.error = getString(R.string.message_email_invalid)
        } else {
            binding.textInputLayoutEmail.isErrorEnabled = false
        }
    }

    private fun actionRequirePassword(it: Boolean?) {
        if (it == true) {
            binding.textInputLayoutPassword.error = getString(R.string.password_required)
        } else {
            binding.textInputLayoutPassword.isErrorEnabled = false
        }
    }

    private fun actionLogin(it: ResponseLogin?) {
        if (it?.isSuccess == true) {
            session.apply {
                createLoginSession()
                id_user = it.data?.id ?: 0
                full_name = it.data?.fullName ?: ""
                email_user = it.data?.email ?: ""
                phone_number = it.data?.phoneNumber ?: ""
            }
            moveActivity(MainActivity::class.java)
        } else {
            showToast(this, it?.message.toString())
//            Toast.makeText(this, it?.message.toString(), Toast.LENGTH_SHORT).show()
        }

    }


    private fun validateEmail() {
        binding.inputEmail.doAfterTextChanged {
            if (!it.toString().matches(emailPattern().toRegex())) {
                binding.textInputLayoutEmail.error = getString(R.string.message_email_invalid)
            } else {
                binding.textInputLayoutEmail.isErrorEnabled = false
            }
        }
    }

}