package com.hd.jetpackproject.uis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hd.jetpackproject.R
import com.hd.jetpackproject.databinding.FragmentMainBinding
import com.hd.jetpackproject.utils.LOG
import com.hd.jetpackproject.viewmodels.UnsplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    private val unsplashViewModel by viewModels<UnsplashViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        subscribe()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            unsplashViewModel.reqBanner()
        }
    }

    private fun subscribe() {
        unsplashViewModel.liveBanner.observe(viewLifecycleOwner, {
            LOG.d("MainFragment", "liveBanner $it")
            Toast.makeText(activity, "" + it.errorCode + it.errorMsg, Toast.LENGTH_SHORT).show()
        })
        unsplashViewModel.liveQuery.observe(viewLifecycleOwner, {
            LOG.d("MainFragment", "liveQuery $it")
//            Toast.makeText(activity, "" + it.errorCode + it.errorMsg, Toast.LENGTH_SHORT).show()
        })
    }


    private fun navigateFileFragment() {
        findNavController().navigate(R.id.fragment_file)
    }
}