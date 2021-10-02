package com.udacoding.pos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import com.udacoding.pos.databinding.ActivityLoginBinding
import com.udacoding.pos.utils.emailPattern

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        validateEmail()

    }

    private fun validateEmail() {
        binding.inputEmail.doAfterTextChanged {
            if (!it.toString().matches(emailPattern().toRegex())){
                binding.textInputLayoutEmail.error = getString(R.string.message_email_invalid)
            } else {
                binding.textInputLayoutEmail.isErrorEnabled = false
            }
        }
    }

}