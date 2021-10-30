package com.udacoding.pos.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val ARG_OBJECT = "object"
    private val tab_name = arrayListOf("CASH", "TRANSFER")

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        var fragment = Fragment()

        when(position) {
            0 -> {
                fragment = CashFragment()
                fragment.arguments = Bundle().apply {
                    putStringArrayList(ARG_OBJECT, tab_name)
                }
                return fragment
            }
            1 -> {
                fragment = TransferFragment()
                fragment.arguments = Bundle().apply {
                    putStringArrayList(ARG_OBJECT, tab_name)
                }
                return fragment
            }
        }
        return fragment
    }
}