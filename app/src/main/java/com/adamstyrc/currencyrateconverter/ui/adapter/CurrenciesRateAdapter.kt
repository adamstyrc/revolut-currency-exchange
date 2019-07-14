package com.adamstyrc.currencyrateconverter.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adamstyrc.currencyrateconverter.model.CalculatedCurrency
import com.adamstyrc.currencyrateconverter.R

class CurrenciesRateAdapter(
    var calculatedCurrencies: List<CalculatedCurrency>
) : RecyclerView.Adapter<CurrencyRateViewHolder>() {

    override fun getItemCount() = calculatedCurrencies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyRateViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_currency_rate, parent, false)
        return CurrencyRateViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CurrencyRateViewHolder, position: Int) {
        val currencyRate = calculatedCurrencies[position]
        holder.bindCurrencyRate(currencyRate)
    }
}

class CurrencyRateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var tvCurrencyName: TextView = itemView.findViewById(R.id.tvCurrencyName)
    private var etRateConverter: EditText = itemView.findViewById(R.id.etRate)

    fun bindCurrencyRate(calculatedCurrency: CalculatedCurrency) {
        tvCurrencyName.text = calculatedCurrency.currency.name
        val formattedValue = "%.2f".format(calculatedCurrency.value)
        etRateConverter.setText(formattedValue)
    }

}
