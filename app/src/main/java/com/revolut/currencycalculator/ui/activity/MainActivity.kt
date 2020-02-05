package com.revolut.currencycalculator.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.revolut.currencycalculator.R
import com.revolut.currencycalculator.dagger.InjectionGraph
import com.revolut.currencycalculator.ui.adapter.CurrenciesExchangeAdapter
import com.revolut.currencycalculator.viewmodel.CurrencyRateViewModel
import com.revolut.domain.Money
import com.revolut.domain.model.Currency
import com.revolut.domain.model.EstimatedCurrencyExchange
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
        (rvCurrencyRates.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        viewModel.getEstimatedCurrencyExchange().observe(this, Observer { currencyRates ->
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

    fun setBaseCurrencyAmount(amount: Money) {
        viewModel.setBaseCurrencyAmount(amount)
    }

    fun setBaseCurrency(currency: Currency) {
        viewModel.setBaseCurrency(currency)
    }

    private fun updateExchangedCurrencies(
        currencyExchangeRates: MutableList<EstimatedCurrencyExchange>
    ) {
        currenciesRateAdapter.items = currencyExchangeRates

        currenciesRateAdapter.notifyItemRangeChanged(
            1,
            currenciesRateAdapter.itemCount - 1)
    }
}
