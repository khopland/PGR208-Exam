package com.example.pgr208_2021_android_exam.ui.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pgr208_2021_android_exam.database.viewModel.PointsViewModel
import com.example.pgr208_2021_android_exam.databinding.FragmentOverviewBinding
import com.example.pgr208_2021_android_exam.ui.recyclerview.CurrencyAdapter
import com.example.pgr208_2021_android_exam.ui.viewmodels.OverViewModel

class OverviewFragment : Fragment() {
    private lateinit var binding: FragmentOverviewBinding
    private lateinit var viewModel: OverViewModel
    private lateinit var pointsViewModel: PointsViewModel

    companion object {
        @JvmStatic
        fun newInstance() = OverviewFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentOverviewBinding.inflate(inflater, container, false);

        // Instantiate the viewModel(s)
        viewModel = ViewModelProvider(this).get(OverViewModel::class.java)
        pointsViewModel = ViewModelProvider(this).get(PointsViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe when the formatted userPoints-text is populated by the "pointsLiveData"
        pointsViewModel.userPoints.observe(viewLifecycleOwner, { pointsText ->
            binding.TWUserPoint.text = pointsText
        })

        val currencyRecycleView = binding.currencyList

        viewModel.cryptoCurrencies.observe(viewLifecycleOwner, { cryptoList ->
            currencyRecycleView.adapter =
                CurrencyAdapter(cryptoList, CurrencyAdapter.OnClickListener { clickedCurrency ->

                    findNavController().navigate(
                        OverviewFragmentDirections.actionOverviewFragmentToCurrencyFragment(
                            clickedCurrency
                        )
                    )
                })
            // LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            currencyRecycleView.layoutManager = GridLayoutManager(requireContext(), 1)
        })

        // TODO: Move this somewhere else later, or find a better solution for handling the possible fetch-error
        viewModel.error.observe(viewLifecycleOwner, { ex ->
            showError(requireContext(), ex)
        })

        binding.TWUserPoint.setOnClickListener {
            findNavController().navigate(OverviewFragmentDirections.actionOverviewFragmentToPortfolioFragment())
        }
    }

    override fun onResume() {
        super.onResume()

        pointsViewModel.refresh()
        viewModel.fetchAllCryptoCurrency()
    }
}

// TODO: Maybe move this into some kind of shared file later?
private fun showError(context: Context, error: Exception) {
    Toast.makeText(context, error.message.toString(), Toast.LENGTH_SHORT).show()
}