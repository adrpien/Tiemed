package com.adrpien.tiemed.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adrpien.tiemed.R
import com.adrpien.tiemed.databinding.FragmentEditRepairBinding
import com.adrpien.tiemed.databinding.FragmentInspectionsListBinding

// ViewBinding
private var _binding: FragmentInspectionsListBinding? = null
private val binding
get() = _binding!!

class InspectionsListFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentInspectionsListBinding.inflate(layoutInflater)
        return binding.root
    }

}