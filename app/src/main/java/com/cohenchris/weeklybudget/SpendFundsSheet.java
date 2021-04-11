package com.cohenchris.weeklybudget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class SpendFundsSheet extends BottomSheetDialogFragment {
    private TextInputEditText fundsToRemove;
    private TextView amount;
    private Button spendFunds;
    private TextView budget;

    private String curr_balance;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String CURR_BALANCE = "currBalance";
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NORMAL, R.style.BottomSheet);
        View view = inflater.inflate(R.layout.spend_funds_bottomsheet, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        spendFunds = view.findViewById(R.id.button4);
        amount = getActivity().findViewById(R.id.textView);
        budget = getActivity().findViewById(R.id.textView7);
        fundsToRemove = view.findViewById(R.id.inputText);

        // Create US currency locale
        Locale usa = new Locale("en", "US");
        final NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(usa);

        spendFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fundsToRemove.getText().toString().length() == 0){
                    fundsToRemove.setText("0");
                }
                try {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    double toRemove = Double.parseDouble(fundsToRemove.getText().toString().replaceAll(",", ".").replaceAll("$", ""));
                    double curr = Double.longBitsToDouble(sharedPreferences.getLong(CURR_BALANCE, (long) 0.00));
                    double newBalance = curr - toRemove;
                    amount.setText(dollarFormat.format(newBalance));

                    editor.putLong(CURR_BALANCE, Double.doubleToRawLongBits(newBalance));
                    editor.apply();
                } catch(NumberFormatException exception) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Error")
                            .setMessage("Invalid number was entered.")
                            .setNeutralButton("OK", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
        return view;
    }
}
