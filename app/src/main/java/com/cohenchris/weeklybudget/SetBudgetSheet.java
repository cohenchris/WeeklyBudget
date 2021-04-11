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

public class SetBudgetSheet extends BottomSheetDialogFragment {
    private Button setBudget;
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
        setBudget = view.findViewById(R.id.button6);
        budget = getActivity().findViewById(R.id.textView7);

        // Create US currency locale
        Locale usa = new Locale("en", "US");
        final NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(usa);

        setBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(budgetEditText.getText().toString().length() == 0) {
                    budgetEditText.setText("0");
                }
                try {
                    // replace old budget with newly entered one
                    double newBudget = Double.parseDouble(budgetEditText.getText().toString().replaceAll(",", "."));
                    budget.setText(dollarFormat.format(newBudget));

                    // Get the current week number
                    Calendar calendar = new GregorianCalendar();
                    Date trialTime = new Date();
                    calendar.setTime(trialTime);
                    int week_name = Calendar.WEEK_OF_YEAR;

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    // update balance in the sharedPreferences
                    editor.putLong(CURR_BUDGET, Double.doubleToRawLongBits(newBudget));
                    // overwrite current balance with the newly updated budget
                    editor.putLong(CURR_BALANCE, Double.doubleToRawLongBits(newBudget));

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
