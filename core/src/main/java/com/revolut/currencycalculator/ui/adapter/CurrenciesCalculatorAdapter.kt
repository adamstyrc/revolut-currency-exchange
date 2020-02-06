package com.revolut.currencycalculator.ui.adapter

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
import com.revolut.currencycalculator.model.CurrenciesInfo
import com.revolut.currencycalculator.utils.Logger
import com.revolut.domain.Price
import com.revolut.domain.formatter.PriceFormatter
import com.revolut.domain.model.Currency
import com.revolut.domain.model.EstimatedCurrencyExchange

class CurrenciesCalculatorAdapter(
    var items: MutableList<EstimatedCurrencyExchange>
) : RecyclerView.Adapter<CurrenciesCalculatorAdapter.ViewHolder>() {

    var onBaseCurrencyChanged: OnBaseCurrencyChanged? = null

    private val textChangedListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(input: CharSequence?, start: Int, before: Int, count: Int) {
            input.toString().toBigDecimalOrNull()?.let { amount ->
                Logger.log("onBaseCurrencyAmountChanged(${amount})")
                onBaseCurrencyChanged?.onBaseCurrencyAmountChanged(amount)
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
        holder.bindCurrencyRate(position, currencyRate)
    }


    fun updateItems(currencyExchangeRates: MutableList<EstimatedCurrencyExchange>) {
        items = currencyExchangeRates
        notifyItemRangeChanged(1, itemCount - 1)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var ivCurrencyIcon: ImageView = itemView.findViewById(R.id.ivCurrencyIcon)
        private var tvCurrencyName: TextView = itemView.findViewById(R.id.tvCurrencyName)
        private var tvCurrencySymbol: TextView = itemView.findViewById(R.id.tvCurrencySymbol)
        private var etRateConverter: EditText = itemView.findViewById(R.id.etRate)

        fun bindCurrencyRate(
            position: Int,
            estimatedCurrencyExchange: EstimatedCurrencyExchange
        ) {
            setCurrency(estimatedCurrencyExchange.currency)

            //TODO decide about rounding to 2 last digits
            val price = estimatedCurrencyExchange.value
            val formattedValue = PriceFormatter.displayPriceUpTo2Decimals(price)

            etRateConverter.removeTextChangedListener(textChangedListener)

            etRateConverter.setText(formattedValue)

            val isBaseCurrency = position == 0
            setItemBehavior(position, isBaseCurrency, estimatedCurrencyExchange)
        }

        private fun setCurrency(currency: Currency) {
            val icon = CurrenciesInfo.getIcon(currency)
                ?: R.drawable.ic_currency_unknown
            ivCurrencyIcon.setImageResource(icon)

            val stringRes = CurrenciesInfo.getNameRes(currency)
            if (stringRes != null) {
                tvCurrencyName.setText(stringRes)
            } else {
                tvCurrencyName.text = ""
            }
            tvCurrencySymbol.text = currency.name
        }

        private fun setItemBehavior(
            position: Int,
            isBaseCurrency: Boolean,
            estimatedCurrencyExchange: EstimatedCurrencyExchange
        ) {
            if (isBaseCurrency) {
                etRateConverter.onFocusChangeListener = null
                etRateConverter.addTextChangedListener(textChangedListener)
            } else {
                etRateConverter.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        Logger.log("Position $position focus $hasFocus")
                        onFocusIntercepted(position, estimatedCurrencyExchange)
                    }
                }
                etRateConverter.removeTextChangedListener(textChangedListener)
            }
        }

        private fun onFocusIntercepted(
            position: Int,
            estimatedCurrencyExchange: EstimatedCurrencyExchange
        ) {
            etRateConverter.removeTextChangedListener(textChangedListener)
            moveItemToTop()
            setItemBehavior(position, true, estimatedCurrencyExchange)

            Logger.log("onBaseCurrencyChanged(${estimatedCurrencyExchange.currency})")
            onBaseCurrencyChanged?.onBaseCurrencyChanged(estimatedCurrencyExchange.currency)
        }

        private fun moveItemToTop() {
            layoutPosition.takeIf { it > 0 }?.also { currentPosition ->
                items.removeAt(currentPosition).also {
                    items.add(0, it)
                }
                notifyItemMoved(currentPosition, 0)
                notifyItemChanged(1)
            }
        }
    }

    interface OnBaseCurrencyChanged {
        fun onBaseCurrencyAmountChanged(amount: Price)
        fun onBaseCurrencyChanged(currency: Currency)
    }
}


