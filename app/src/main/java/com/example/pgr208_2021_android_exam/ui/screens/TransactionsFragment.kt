package com.example.pgr208_2021_android_exam.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.pgr208_2021_android_exam.R
import com.example.pgr208_2021_android_exam.database.viewModel.TransactionsViewModel


class TransactionsFragment : Fragment() {

    private lateinit var transitionsViewModel: TransactionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        transitionsViewModel = ViewModelProvider(this).get(TransactionsViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transactions, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TransactionsFragment()
    }
}