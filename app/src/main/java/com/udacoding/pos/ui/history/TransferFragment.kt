package com.udacoding.pos.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.udacoding.pos.R
import com.udacoding.pos.databinding.FragmentTransferBinding
import com.udacoding.pos.ui.history.adapter.HistoryAdapter
import com.udacoding.pos.ui.history.model.ResponseGetTransaction
import com.udacoding.pos.ui.history.viewmodel.HistoryViewModel
import com.udacoding.pos.utils.showError

class TransferFragment : Fragment() {

    lateinit var binding: FragmentTransferBinding

    lateinit var viewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        viewModel.getTransaction(null,"TRANSFER")

        attachObserve(view)

    }

    private fun attachObserve(view: View) {
        with(viewModel) {
            history.observe(viewLifecycleOwner, { showHistory(it, view) })
            error.observe(viewLifecycleOwner, { showError(activity?.applicationContext, it) })
        }
    }

    private fun showHistory(it: ResponseGetTransaction?, view: View) {

        binding.listHistory.adapter = HistoryAdapter(view.context, it?.data)

    }

}