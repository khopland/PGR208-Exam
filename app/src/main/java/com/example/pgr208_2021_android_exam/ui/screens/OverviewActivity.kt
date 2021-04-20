package com.example.pgr208_2021_android_exam.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pgr208_2021_android_exam.database.viewModel.PointsViewModel
import com.example.pgr208_2021_android_exam.databinding.ActivityOverviewBinding
import com.example.pgr208_2021_android_exam.ui.recyclerview.CurrencyAdapter
import com.example.pgr208_2021_android_exam.ui.viewmodels.OverViewModel

class OverviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOverviewBinding
    private lateinit var viewModel: OverViewModel

    private lateinit var pointsViewModel: PointsViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Creating the static layout class and inflating the content into the contentView -
        // (middle-part between top and bottom navbar etc.)
        binding = ActivityOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Instantiate the viewModel
        viewModel = OverViewModel(application)


        val currencyRecycleView = binding.currencyList



        viewModel.cryptoCurrencies.observe(this, { cryptoList ->
            currencyRecycleView.adapter = CurrencyAdapter(
                this,
                cryptoList,
                CurrencyAdapter.OnClickListener { clickedCurrency ->

                    // Set the selectedCurrency to the clicked-currency
                    viewModel.setSelectedCurrency(clickedCurrency)

                    // Clear the content of the fragmentContainer before replacing it...
                    binding.fragmentContainer.removeAllViewsInLayout()
                })
            // LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            currencyRecycleView.layoutManager = GridLayoutManager(this, 1)
        })


        pointsViewModel = ViewModelProvider(this).get(PointsViewModel::class.java)

        pointsViewModel.pointsLiveData.observe(this, { d ->
            val res = d.toString() + "00"
            binding.TWUserPoint.text = "Points : ${
                res.substring(0, res.indexOf('.') + 3)
            } USD"
        })


        viewModel.cryptoCurrencies.observe(this, { cryptoList ->
            currencyRecycleView.adapter = CurrencyAdapter(
                this,
                cryptoList,
                CurrencyAdapter.OnClickListener { clickedCurrency ->

                    // Set the selectedCurrency to the clicked-currency
                    viewModel.setSelectedCurrency(clickedCurrency)

                    // Clear the content of the fragmentContainer before replacing it...
                    binding.fragmentContainer.removeAllViewsInLayout()
                })
            // LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            currencyRecycleView.layoutManager = GridLayoutManager(this, 1)
        })


        // TODO: Make this start the CurrencyFragment and give it info about the (selected)cryptoCurrency
        viewModel.selectedCryptoCurrency.observe(this, { cryptoCurrency ->
            //Log.d(this::class.java.simpleName, cryptoCurrency.toString())
            renderFragment(CurrencyFragment.newInstance(cryptoCurrency))
        })

        // TODO: Move this somewhere else later, or find a better solution for handling the possible fetch-error
        viewModel.error.observe(this, { ex ->
            showError(this, ex)
        })


        // TODO: Remove this after setting up navigation...
//        binding.btnPortFolio.setOnClickListener {
//
//            // TODO: Remove this and remove the visibility-attribute in the XML-file after setting up RecyclerView...
//            // Make the fragmentContainer visible, and hide the RecyclerView in the XML-file...
//            binding.fragmentContainer.visibility = View.VISIBLE
//            binding.currencyList.visibility = View.GONE
//
//            supportFragmentManager.apply {
//                beginTransaction()
//                    .replace(binding.fragmentContainer.id, PortfolioFragment())
//                    .commit()
//            }
//        }
    }

    // TODO: Maybe move this into some kind of shared file later?
    private fun showError(context: Context, error: Exception) {
        Toast.makeText(context, error.message.toString(), Toast.LENGTH_SHORT).show()
    }

    //        renderFragment(PortFolioFragment())
//        renderFragment(CurrencyFragment(selected))
    private fun renderFragment(fragment: Fragment) {
        supportFragmentManager.apply {
            beginTransaction()
                .replace(binding.fragmentContainer.id, fragment)
                .commit()
        }
    }
}