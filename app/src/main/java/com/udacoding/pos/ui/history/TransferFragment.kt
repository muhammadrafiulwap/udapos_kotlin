package com.udacoding.pos.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.udacoding.pos.R
import com.udacoding.pos.databinding.FragmentTransferBinding

class TransferFragment : Fragment() {

    lateinit var binding: FragmentTransferBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

}