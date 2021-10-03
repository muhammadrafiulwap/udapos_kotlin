package com.udacoding.pos.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.udacoding.pos.R
import com.udacoding.pos.databinding.FragmentHomeBinding
import com.udacoding.pos.ui.home.adapter.ProdukAdapter
import com.udacoding.pos.ui.home.model.DataProduk
import com.udacoding.pos.ui.home.model.responseProduk
import kotlin.math.log

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = responseProduk()

        Log.d("TAG", "onViewCreated: ${data.toString()}")

        binding.listProduct.adapter = ProdukAdapter(data)

    }

}