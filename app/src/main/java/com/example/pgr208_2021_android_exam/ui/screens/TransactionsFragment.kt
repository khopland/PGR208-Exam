package com.example.pgr208_2021_android_exam.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pgr208_2021_android_exam.database.viewModel.TransactionsViewModel
import com.example.pgr208_2021_android_exam.databinding.FragmentTransactionsBinding
import com.example.pgr208_2021_android_exam.ui.recyclerview.TransactionsAdapter


class TransactionsFragment : Fragment() {
    private lateinit var binding: FragmentTransactionsBinding
    private lateinit var transactionsViewModel: TransactionsViewModel

    companion object {
        @JvmStatic
        fun newInstance() = TransactionsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        transactionsViewModel = ViewModelProvider(this).get(TransactionsViewModel::class.java)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionsViewModel.userTransactions.observe(viewLifecycleOwner) { transactions ->
            // Preparing RecyclerView with list of content
            binding.transactions.apply {
                adapter = TransactionsAdapter(transactions)
                layoutManager = GridLayoutManager(requireContext(), 1)
            }
        }
    }
}