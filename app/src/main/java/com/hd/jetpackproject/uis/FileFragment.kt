package com.hd.jetpackproject.uis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hd.jetpackproject.databinding.FragmentFileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentFileBinding.inflate(inflater, container, false)
        return binding.root
    }

}