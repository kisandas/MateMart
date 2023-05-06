package com.matemart.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.matemart.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun setupMainStoreAdapter() {
//        todo mainstore Adapter initialize here
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding!!.rcMainStore.layoutManager = linearLayoutManager
        //       todo uncomment this line after setting upAdapter binding.rcMainStore.setAdapter(adapter);
    }
}