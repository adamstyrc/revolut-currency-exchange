package com.adamstyrc.currencyrateconverter.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.adamstyrc.currencyrateconverter.R
import com.adamstyrc.currencyrateconverter.ui.adapter.CurrenciesRateAdapter
import com.adamstyrc.currencyrateconverter.viewmodel.CurrencyRateViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CurrencyRateViewModel
    private var currenciesRateAdapter = CurrenciesRateAdapter(emptyList())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(CurrencyRateViewModel::class.java)

        rvCurrencyRates.layoutManager = LinearLayoutManager(this)
        currenciesRateAdapter = CurrenciesRateAdapter(emptyList())
        rvCurrencyRates.adapter = currenciesRateAdapter

        viewModel.currencyRateLiveData.observe(this, Observer { currencyRates ->
            currenciesRateAdapter.currencyRates = currencyRates
            currenciesRateAdapter.notifyDataSetChanged()
        })
    }
}
