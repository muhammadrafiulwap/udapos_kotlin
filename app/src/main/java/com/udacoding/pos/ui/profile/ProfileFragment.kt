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
import com.udacoding.pos.ui.profile.model.ResponseProfit
import com.udacoding.pos.ui.profile.viewmodel.ProfileViewModel
import com.udacoding.pos.utils.moveActivity
import com.udacoding.pos.utils.toRupiah

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding

    private lateinit var viewModelHome: HomeViewModel

    private lateinit var viewModel: ProfileViewModel

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

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModelHome = ViewModelProvider(this).get(HomeViewModel::class.java)

        session = SessionManager(view.context)

        initView()
        attachObserve()
    }

    private fun attachObserve() {
        with(viewModel) {
            profit.observe(viewLifecycleOwner, { showProfit(it) })
        }
    }

    private fun showProfit(it: ResponseProfit?) {
        binding.textTotalTransaction.text = it?.data?.totalTransaction.toString()
        binding.textTotalIncome.text = toRupiah(it?.data?.totalIncome?.toDouble())
    }

    private fun initView() {

        with(viewModel) {
            getProfit(session.id_user)
        }

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