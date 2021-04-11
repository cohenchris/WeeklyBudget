package com.cohenchris.weeklybudget.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.cohenchris.weeklybudget.R;
import com.cohenchris.weeklybudget.SetBudgetSheet;

import java.text.NumberFormat;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private Button setBudget;
    private TextView currentBudget;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String CURR_BUDGET = "currBudget";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        currentBudget = root.findViewById(R.id.textView7);
        setBudget = root.findViewById(R.id.button5);

        setBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetBudgetSheet setBudgetSheet = new SetBudgetSheet();
                setBudgetSheet.show(getActivity().getSupportFragmentManager(), "setBudgetSheetLog");
            }
        });

        // load budget into view
        loadBudget();

        // Change text view text
        TextView message = root.findViewById(R.id.textView8);
        message.setText("Current Weekly Budget");

        TextView comment = root.findViewById(R.id.textView9);
        comment.setText("This will be added to the available amount every Sunday");

        return root;
    }

    public void loadBudget(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // Create US currency locale
        Locale usa = new Locale("en", "US");
        final NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(usa);

        long currBudget = sharedPreferences.getLong(CURR_BUDGET, (long) 0.00);
        currentBudget.setText(dollarFormat.format(currBudget));
    }
}
