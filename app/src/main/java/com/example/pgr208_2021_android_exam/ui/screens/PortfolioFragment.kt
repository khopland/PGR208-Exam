package com.example.pgr208_2021_android_exam.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pgr208_2021_android_exam.databinding.FragmentPortfolioBinding
import com.example.pgr208_2021_android_exam.database.viewModel.AllWalletsViewModel
import com.example.pgr208_2021_android_exam.database.viewModel.PointsViewModel
import com.example.pgr208_2021_android_exam.ui.recyclerview.WalletAdapter
import com.example.pgr208_2021_android_exam.ui.viewmodels.PortfolioViewModel

/**
 * Layout:
 *  -> Shows user-points in "top header" (X)
 *  -> Info about how the user-points are calculated. (X)
 *  -> My portfolio-text. (X)
 *  -> Scrollable-list of user-owned crypto-currencies (RecyclerView). (X)
 *  --> getAllWallets from AllWalletsViewModel(?).
 *  --> Correct assumption :D.
 *  -> Button for navigation to Transactions. (X)
 *
 *  ViewModel refresh(es):
 *  -> User points refresh: PointsViewModel(?).
 *  -> User owned wallets refresh: AllWalletsViewModel(?).
 */

class PortfolioFragment : Fragment() {
    private lateinit var binding: FragmentPortfolioBinding
    private lateinit var viewModel: PortfolioViewModel
    private lateinit var allWalletsViewModel: AllWalletsViewModel
    private lateinit var pointsViewModel: PointsViewModel


    companion object {
        @JvmStatic
        fun newInstance() = PortfolioFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(PortfolioViewModel::class.java)
        allWalletsViewModel = ViewModelProvider(this).get(AllWalletsViewModel::class.java)
        pointsViewModel = ViewModelProvider(this).get(PointsViewModel::class.java)


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
            -> To make PortfolioFragment able to see/react when the currencyRates-list has been filled, we need to observe it...
            -> We need to have that list filled with values, as it is needed for transforming wallets.
            -> The PortfolioViewModel needs a list of wallets to transform, so we have to observe that list also...
         */
        viewModel.currencyRates.observe(viewLifecycleOwner, {
            allWalletsViewModel.walletsLiveData.observe(viewLifecycleOwner, { wallets ->
                wallets?.let { existingWallets ->
                    // Give wallets to PortfolioViewModel when we got them
                    binding.ownedCurrencies.apply {
                        adapter = WalletAdapter(requireContext(), viewModel.transformIntoOwnedWallets(existingWallets))
                        layoutManager = GridLayoutManager(requireContext(), 1)
                    }
                }
            })
        })

        // Navigate to transactions-screen when clicking the "Transactions"-button
        binding.btnTransactions.setOnClickListener {
            it.findNavController().navigate(PortfolioFragmentDirections.actionPortfolioFragmentToTransactionsFragment())
        }
    }
}
