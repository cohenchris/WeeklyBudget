package com.cohenchris.weeklybudget.ui.home;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cohenchris.weeklybudget.AddFundsSheet;
import com.cohenchris.weeklybudget.R;
import com.cohenchris.weeklybudget.SpendFundsSheet;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView currentBalanceView;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String CURR_BALANCE = "currBalance";
    public static final String CURR_BUDGET = "currBudget";
    public static final String WEEK = "weekOfBudget";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        currentBalanceView = root.findViewById(R.id.textView);
        final Button addFundsButton = root.findViewById(R.id.button2);
        final Button spendFundsButton = root.findViewById(R.id.button3);
        addFundsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFundsSheet addFundsDialog = new AddFundsSheet();
                addFundsDialog.show(requireActivity().getSupportFragmentManager(), "addFundsDialogBox");
            }
        });
        spendFundsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpendFundsSheet  spendFundsDialog = new SpendFundsSheet();
                spendFundsDialog.show(requireActivity().getSupportFragmentManager(), "spendFundsDialogBox");
            }
        });

        loadData();

        return root;
    }

    public void loadData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // Determine whether or not to increase budget (if it's a new week)
        int lastWeek = sharedPreferences.getInt(WEEK, -1);

        System.out.println("lastWeek = " + lastWeek);

        // Get the current week number
        Calendar now = Calendar.getInstance();
        int currWeek = now.get(Calendar.WEEK_OF_YEAR);

        System.out.println("currWeek = " + currWeek);


        double curr_balance = Double.longBitsToDouble(sharedPreferences.getLong(CURR_BALANCE, (long) 0.00));
        double curr_budget = Double.longBitsToDouble(sharedPreferences.getLong(CURR_BUDGET, (long) 0.00));

        System.out.println("old balance = " + curr_balance);

        // add (budget * num weeks passed) to available funds
        curr_balance += (curr_budget * (currWeek - lastWeek));

        System.out.println("new balance = " + curr_balance);

        // Create US currency locale
        Locale usa = new Locale("en", "US");
        final NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(usa);

        // update current balance
        currentBalanceView.setText(dollarFormat.format(curr_balance));

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(CURR_BALANCE, Double.doubleToRawLongBits(curr_balance));
        editor.putInt(WEEK, currWeek);
        editor.apply();
    }
}
