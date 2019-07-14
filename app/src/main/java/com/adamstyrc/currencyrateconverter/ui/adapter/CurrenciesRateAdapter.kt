package com.adamstyrc.currencyrateconverter.ui.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adamstyrc.currencyrateconverter.model.CalculatedCurrency
import com.adamstyrc.currencyrateconverter.R
import com.adamstyrc.currencyrateconverter.ui.activity.MainActivity

class CurrenciesRateAdapter(
    val context: Context,
    var items: ArrayList<CalculatedCurrency>
) : RecyclerView.Adapter<CurrenciesRateAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_currency_rate, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currencyRate = items[position]
        holder.bindCurrencyRate(currencyRate, position == 0)
    }

    private val textChangedListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(input: CharSequence?, start: Int, before: Int, count: Int) {
            input.toString().toFloatOrNull()?.let { amount ->
                if (context is MainActivity) {
                    context.setBaseCurrencyAmount(amount)
                }
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCurrencyName: TextView = itemView.findViewById(R.id.tvCurrencyName)
        var etRateConverter: EditText = itemView.findViewById(R.id.etRate)

        fun bindCurrencyRate(calculatedCurrency: CalculatedCurrency, selectedAsBase: Boolean) {
            etRateConverter.removeTextChangedListener(textChangedListener)

            tvCurrencyName.text = calculatedCurrency.currency.name
            //TODO decide about rounding to 2 last digits
            val formattedValue = "%.2f".format(calculatedCurrency.value).replace(",", ".")
            etRateConverter.setText(formattedValue)

            setViewActions(calculatedCurrency, selectedAsBase)
        }

        private fun setViewActions(
            calculatedCurrency: CalculatedCurrency,
            selectedAsBase: Boolean
        ) {
            if (selectedAsBase) {
                etRateConverter.addTextChangedListener(textChangedListener)
                etRateConverter.setOnClickListener(null)
            } else {
                etRateConverter.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        if (context is MainActivity) {
                            moveToTop()
                            setViewActions(calculatedCurrency, true)
                            context.setBaseCurrency(calculatedCurrency.currency)
                        }
                    }
                }
            }
        }

        private fun moveToTop() {
            layoutPosition.takeIf { it > 0 }?.also { currentPosition ->
                items.removeAt(currentPosition).also {
                    items.add(0, it)
                }
                notifyItemMoved(currentPosition, 0)
            }
        }
    }
}

