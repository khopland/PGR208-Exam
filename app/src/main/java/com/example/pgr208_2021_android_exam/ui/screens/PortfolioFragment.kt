package com.example.pgr208_2021_android_exam.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pgr208_2021_android_exam.R
import com.example.pgr208_2021_android_exam.databinding.FragmentPortfolioBinding
import com.example.pgr208_2021_android_exam.database.viewModel.PointsViewModel
import com.example.pgr208_2021_android_exam.ui.recyclerview.WalletAdapter
import com.example.pgr208_2021_android_exam.ui.viewmodels.CurrencyViewModel
import com.example.pgr208_2021_android_exam.ui.viewmodels.OwnedWalletsViewModel

class PortfolioFragment : Fragment() {
    private lateinit var binding: FragmentPortfolioBinding
    private lateinit var pointsViewModel: PointsViewModel
    private lateinit var ownedWalletsViewModel: OwnedWalletsViewModel
    private lateinit var currencyViewModel: CurrencyViewModel


    companion object {
        @JvmStatic
        fun newInstance() = PortfolioFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pointsViewModel = ViewModelProvider(this).get(PointsViewModel::class.java)
        currencyViewModel = ViewModelProvider(this).get(CurrencyViewModel::class.java)

        // Inflate the layout for this fragment
        binding = FragmentPortfolioBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show user-points in top-header
        pointsViewModel.userPoints.observe(viewLifecycleOwner, { pointsText ->
            binding.tvUserPoints.text = pointsText
        })

        // TODO: Research other ways to get the result below, without "observe nesting"
        // I know, this does not look too good, but it works for now...
        /* Reason/explanation:
            -> To make PortfolioFragment able to see/react when the currencyRates have been filled, we need to observe it...
            -> We need to have that those values, as they are needed for transforming wallets.
            -> The ownWalletsViewModel needs the rates for the transformation (wallets => ownedWallets) -
               so the cleanest way was to pass it as a dependency.
         */
        currencyViewModel.currencyRates.observe(viewLifecycleOwner, { rates ->
            ownedWalletsViewModel = OwnedWalletsViewModel(requireActivity().application, rates)

            ownedWalletsViewModel.ownedWallets.observe(viewLifecycleOwner, { ownedWallets ->
                // This is to only display OwnedWallets with a total-value above 1 USD
                // We need to filter this before giving it to the walletAdapter to avoid "empty spaces rendered in the recyclerView"
                val wallets = ownedWallets.values.filter { wallet -> wallet.totalInUSD >= 1 }

                binding.ownedCurrencies.apply {
                    adapter = WalletAdapter(wallets)
                    layoutManager = GridLayoutManager(requireContext(), 1)
                }
            })
        })

        // Navigate to transactions-screen when clicking the "Transactions"-button
        binding.btnTransactions.setOnClickListener {
            it.findNavController().navigate(PortfolioFragmentDirections.actionPortfolioFragmentToTransactionsFragment())
        }
    }

    override fun onResume() {
        super.onResume()

        pointsViewModel.refresh()
    }
}
