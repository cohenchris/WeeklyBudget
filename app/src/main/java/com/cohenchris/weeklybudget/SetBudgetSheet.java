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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

public class SetBudgetSheet extends BottomSheetDialogFragment {
    private TextInputEditText budgetEditText;
    private TextView budget;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String CURR_BALANCE = "currBalance";
    public static final String CURR_BUDGET = "currBudget";
    public static final String WEEK = "weekOfBudget";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NORMAL, R.style.BottomSheet);
        View view = inflater.inflate(R.layout.budget_bottomsheet, container, false);
        budgetEditText = view.findViewById(R.id.inputtext2);
        Button setBudget = view.findViewById(R.id.button6);
        budget = requireActivity().findViewById(R.id.textView7);

        // Create US currency locale
        Locale usa = new Locale("en", "US");
        final NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(usa);

        setBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Objects.requireNonNull(budgetEditText.getText()).toString().length() == 0) {
                    budgetEditText.setText("0");
                }
                try {
                    // replace old budget with newly entered one
                    double newBudget = Double.parseDouble(budgetEditText.getText().toString().replaceAll(",", "."));
                    budget.setText(dollarFormat.format(newBudget));

                    // Get the current week number
                    Calendar now = Calendar.getInstance();
                    int week_name = now.get(Calendar.WEEK_OF_YEAR);

                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    // update budget in the sharedPreferences
                    editor.putLong(CURR_BUDGET, Double.doubleToRawLongBits(newBudget));

                    editor.putInt(WEEK, week_name);
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
