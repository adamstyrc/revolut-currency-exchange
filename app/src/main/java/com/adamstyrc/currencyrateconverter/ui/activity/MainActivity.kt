package com.adamstyrc.currencyrateconverter.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.adamstyrc.currencyrateconverter.R
import com.adamstyrc.currencyrateconverter.dagger.InjectionGraph
import com.adamstyrc.currencyrateconverter.model.EstimatedCurrency
import com.adamstyrc.currencyrateconverter.model.Currency
import com.adamstyrc.currencyrateconverter.ui.adapter.CurrenciesExchangeAdapter
import com.adamstyrc.currencyrateconverter.viewmodel.CurrencyRateViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: CurrencyRateViewModel
    private var currenciesRateAdapter = CurrenciesExchangeAdapter(this, ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        InjectionGraph.Manager.init(this).inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrencyRateViewModel::class.java)

        rvCurrencyRates.layoutManager = LinearLayoutManager(this)
        rvCurrencyRates.adapter = currenciesRateAdapter

        viewModel.estimatedCurrencies.observe(this, Observer { currencyRates ->
            updateExchangedCurrencies(currencyRates)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.startUpdatingCurrencyRates()
    }

    override fun onPause() {
        super.onPause()
        viewModel.cancelUpdatingCurrencyRates()
    }

    fun setBaseCurrencyAmount(amount: Float) {
        viewModel.setBaseCurrencyAmount(amount)
    }

    fun setBaseCurrency(currency: Currency) {
        viewModel.baseCurrency = currency
    }

    private fun updateExchangedCurrencies(currencyRates: ArrayList<EstimatedCurrency>) {
        currenciesRateAdapter.items = currencyRates
        currenciesRateAdapter.notifyItemRangeChanged(1, currenciesRateAdapter.itemCount - 1)
    }
}
