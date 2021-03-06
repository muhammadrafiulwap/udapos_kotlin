package com.udacoding.pos.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udacoding.pos.SessionManager
import com.udacoding.pos.databinding.FragmentProfileBinding
import com.udacoding.pos.ui.home.viewmodel.HomeViewModel
import com.udacoding.pos.ui.login.LoginActivity
import com.udacoding.pos.utils.moveActivity

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding

    private lateinit var viewModelHome: HomeViewModel

    lateinit var session: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelHome = ViewModelProvider(this).get(HomeViewModel::class.java)

        session = SessionManager(view.context)

        initView()
    }

    private fun initView() {

        with(binding) {
            textFullName.text = session.full_name
            textEmail.text = session.email_user
            buttonLogout.setOnClickListener {

                viewModelHome.deleteCartAll()

                session.logout()

                activity?.moveActivity(LoginActivity::class.java)
            }
        }
    }

}