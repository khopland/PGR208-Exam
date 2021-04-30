package com.example.pgr208_2021_android_exam.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.lifecycle.ViewModelProvider
import com.example.pgr208_2021_android_exam.R
import com.example.pgr208_2021_android_exam.databinding.FragmentPortfolioBinding
import com.example.pgr208_2021_android_exam.database.viewModel.AllWalletsViewModel

class PortfolioFragment : Fragment() {
    private lateinit var allWalletsViewModel: AllWalletsViewModel
    private lateinit var binding: FragmentPortfolioBinding

    companion object {
        @JvmStatic
        fun newInstance() = PortfolioFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        allWalletsViewModel = ViewModelProvider(this).get(AllWalletsViewModel::class.java)

        // Inflate the layout for this fragment
        binding = FragmentPortfolioBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTransactions.setOnClickListener {
            it.findNavController().navigate(PortfolioFragmentDirections.actionPortfolioFragmentToTransactionsFragment())
        }
    }
}
