package com.adamstyrc.currencyrateconverter.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.adamstyrc.currencyrateconverter.R
import com.adamstyrc.currencyrateconverter.dagger.InjectionGraph
import com.adamstyrc.currencyrateconverter.ui.adapter.CurrenciesRateAdapter
import com.adamstyrc.currencyrateconverter.viewmodel.CurrencyRateViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: CurrencyRateViewModel

    private var currenciesRateAdapter = CurrenciesRateAdapter(emptyList())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        InjectionGraph.Manager.init(this).inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrencyRateViewModel::class.java)

        rvCurrencyRates.layoutManager = LinearLayoutManager(this)
        currenciesRateAdapter = CurrenciesRateAdapter(emptyList())
        rvCurrencyRates.adapter = currenciesRateAdapter

        viewModel.currencyRateLiveData.observe(this, Observer { currencyRates ->
            currenciesRateAdapter.currencyRates = currencyRates
            currenciesRateAdapter.notifyDataSetChanged()
        })
    }

    override fun onResume() {
        super.onResume()

        viewModel.updateCurrencyRates()
    }
}
