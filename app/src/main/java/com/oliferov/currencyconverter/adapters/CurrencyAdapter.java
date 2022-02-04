package com.oliferov.currencyconverter.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oliferov.currencyconverter.R;
import com.oliferov.currencyconverter.pojo.Currency;

import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

    private List<Currency> currencies;
    private OnCurrencyClickListener onCurrencyClickListener;


    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
        notifyDataSetChanged();
    }

    public interface OnCurrencyClickListener {
        void onCurrencyClick(int position);
    }

    public void setOnCurrencyClickListener(OnCurrencyClickListener onCurrencyClickListener) {
        this.onCurrencyClickListener = onCurrencyClickListener;
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.currency_item, parent, false);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        Currency currency = currencies.get(position);
        holder.textViewCurrencyName.setText(currency.getName());
        holder.textViewCurrencyAbbreviation.setText(currency.getCharCode());
        holder.textViewCurrencyValue
                .setText(Double.toString(currency.getValue() / currency.getNominal()));
        holder.textViewCurrencyPrevious
                .setText(Double.toString(currency.getPrevious() / currency.getNominal()));
    }

    @Override
    public int getItemCount() {
        if (currencies == null) {
            return 0;
        }
        return currencies.size();
    }

    class CurrencyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewCurrencyName;
        private TextView textViewCurrencyAbbreviation;
        private TextView textViewCurrencyValue;
        private TextView textViewCurrencyPrevious;


        public CurrencyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCurrencyName = itemView.findViewById(R.id.textViewCurrencyName);
            textViewCurrencyAbbreviation = itemView.findViewById(R.id.textViewCurrencyAbbreviation);
            textViewCurrencyValue = itemView.findViewById(R.id.textViewCurrencyValue);
            textViewCurrencyPrevious = itemView.findViewById(R.id.textViewCurrencyPrevious);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onCurrencyClickListener != null) {
                        onCurrencyClickListener.onCurrencyClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
