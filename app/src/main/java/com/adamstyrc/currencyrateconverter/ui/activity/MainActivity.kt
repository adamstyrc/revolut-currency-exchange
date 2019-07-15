package com.adamstyrc.currencyrateconverter.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.adamstyrc.currencyrateconverter.R
import com.adamstyrc.currencyrateconverter.dagger.InjectionGraph
import com.adamstyrc.currencyrateconverter.model.EstimatedCurrencyExchange
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
        viewModel.orderedCurrencies = arrayListOf(
            Currency.EUR,
            Currency.USD,
            Currency.GBP,
            Currency.AUD,
            Currency.PLN,
            Currency.JPY,
            Currency.CZK
        )

        rvCurrencyRates.layoutManager = LinearLayoutManager(this)
        rvCurrencyRates.adapter = currenciesRateAdapter

        viewModel.estimatedCurrenciesExchange.observe(this, Observer { currencyRates ->
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
        viewModel.setBaseCurrency(currency)
    }

    private fun updateExchangedCurrencies(currencyExchangeRates: ArrayList<EstimatedCurrencyExchange>) {
        currenciesRateAdapter.items = currencyExchangeRates
        currenciesRateAdapter.notifyItemRangeChanged(1, currenciesRateAdapter.itemCount - 1)
    }
}
