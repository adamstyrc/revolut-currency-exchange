package com.adamstyrc.currencyrateconverter.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adamstyrc.currencyrateconverter.CurrencyRate
import com.adamstyrc.currencyrateconverter.R

class CurrenciesRateAdapter(
    var currencyRates: List<CurrencyRate>
) : RecyclerView.Adapter<CurrencyRateViewHolder>() {

    override fun getItemCount() = currencyRates.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyRateViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_currency_rate, parent, false)
        return CurrencyRateViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CurrencyRateViewHolder, position: Int) {
        val currencyRate = currencyRates[position]
        holder.bindCurrencyRate(currencyRate)
    }
}

class CurrencyRateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var tvCurrencyName: TextView = itemView.findViewById(R.id.tvCurrencyName)
    private var etRateConverter: EditText = itemView.findViewById(R.id.etRate)

    fun bindCurrencyRate(currencyRate: CurrencyRate) {
        tvCurrencyName.text = currencyRate.currency
        etRateConverter.setText(currencyRate.rate.toString())
    }

}
