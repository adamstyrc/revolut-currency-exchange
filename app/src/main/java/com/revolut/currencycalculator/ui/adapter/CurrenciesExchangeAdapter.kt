package com.revolut.currencycalculator.ui.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.revolut.currencycalculator.R
import com.revolut.currencycalculator.model.CurrencyIcons
import com.revolut.currencycalculator.ui.activity.MainActivity
import com.revolut.domain.model.EstimatedCurrencyExchange

class CurrenciesExchangeAdapter(
    val context: Context,
    var items: MutableList<EstimatedCurrencyExchange>
) : RecyclerView.Adapter<CurrenciesExchangeAdapter.ViewHolder>() {

    private val textChangedListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(input: CharSequence?, start: Int, before: Int, count: Int) {
            input.toString().toBigDecimalOrNull()?.let { amount ->
                if (context is MainActivity) {
                    context.setBaseCurrencyAmount(amount)
                }
            }
        }
    }

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



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivCurrencyIcon: ImageView = itemView.findViewById(R.id.ivCurrencyIcon)
        var tvCurrencyName: TextView = itemView.findViewById(R.id.tvCurrencyName)
        var etRateConverter: EditText = itemView.findViewById(R.id.etRate)

        fun bindCurrencyRate(
            estimatedCurrencyExchange: EstimatedCurrencyExchange,
            selectedAsBaseCurrency: Boolean
        ) {
            val icon = CurrencyIcons.getIcon(estimatedCurrencyExchange.currency)
            ivCurrencyIcon.setImageResource(icon)
            tvCurrencyName.text = estimatedCurrencyExchange.currency.name

            //TODO decide about rounding to 2 last digits
            val formattedValue = "%.2f".format(estimatedCurrencyExchange.value)
                .replace(",", ".")

            etRateConverter.removeTextChangedListener(textChangedListener)
            etRateConverter.setText(formattedValue)

            setViewActions(estimatedCurrencyExchange, selectedAsBaseCurrency)
        }

        private fun setViewActions(
            estimatedCurrencyExchange: EstimatedCurrencyExchange,
            selectedAsBase: Boolean
        ) {
            if (selectedAsBase) {
                enableEditText(true)
                etRateConverter.addTextChangedListener(textChangedListener)
                etRateConverter.setOnClickListener(null)
            } else {
                enableEditText(true)
                etRateConverter.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        onFocusIntercepted(estimatedCurrencyExchange, context)
                    }
                }
            }
        }

        private fun enableEditText(enabled: Boolean) {
            etRateConverter.isFocusable = enabled
        }

        private fun onFocusIntercepted(
            estimatedCurrencyExchange: EstimatedCurrencyExchange,
            context: Context
        ) {
            etRateConverter.removeTextChangedListener(textChangedListener)
            if (context is MainActivity) {
                moveItemToTop()
                setViewActions(estimatedCurrencyExchange, true)
                context.setBaseCurrency(estimatedCurrencyExchange.currency)
            }
        }

        private fun moveItemToTop() {
            layoutPosition.takeIf { it > 0 }?.also { currentPosition ->
                items.removeAt(currentPosition).also {
                    items.add(0, it)
                }
                notifyItemMoved(currentPosition, 0)
            }
        }
    }
}


