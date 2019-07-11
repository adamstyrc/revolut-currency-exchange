package com.adamstyrc.currencyrateconverter.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.adamstyrc.currencyrateconverter.CurrencyRate
import com.adamstyrc.currencyrateconverter.R
import com.adamstyrc.currencyrateconverter.ui.adapter.CurrenciesRateAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvCurrencyRates.layoutManager = LinearLayoutManager(this)
        rvCurrencyRates.adapter = CurrenciesRateAdapter(
            ArrayList<CurrencyRate>()
                .apply {
                    add(CurrencyRate()
                        .apply { currency = "GBR"; rate = 1.10 })
                }
                .apply {
                    add(CurrencyRate()
                        .apply { currency = "USD"; rate = 0.90 })
                }
        )
    }
}
