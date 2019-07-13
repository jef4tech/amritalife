package com.kraftlabs.crm_new.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kraftlabs.crm_new.Db.ExpenseDB;
import com.kraftlabs.crm_new.Fragments.ExpenseFormFragment;
import com.kraftlabs.crm_new.Models.Expense;
import com.kraftlabs.crm_new.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by ajith on 29/7/16.
 */

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.MyViewHolder> {

    private final String TAG = "TASK ADAPTER";
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Context context;
    private ArrayList<Expense> expenses = new ArrayList<>();
    private ExpenseDB expenseDB;

    public ExpenseAdapter(Context context, ArrayList<Expense> expenses) {
        this.context = context;
        this.expenses = expenses;
        expenseDB = new ExpenseDB(context);
    }

    public void update(ArrayList<Expense> expenses) {
        this.expenses.clear();
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Expense expense = expenses.get(position);
        holder.txtTownVisited.setText(expense.getTownVisited());
        holder.txtDate.setText(expense.getDate());
        holder.txtTotal.setText("" + expense.getTotal());
        holder.txtStatus.setText(expense.getStatus());

        holder.expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = ExpenseFormFragment.newInstance(expense.getExpenseId());
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
               FragmentManager fragmentManager = activity.getSupportFragmentManager();
               FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_main, fragment);
                String backStateName = fragment.getClass().getName();
                ft.addToBackStack(backStateName);
                ft.commit();
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTownVisited, txtDate, txtTotal, txtStatus;
        public RelativeLayout expense;

        public MyViewHolder(View view) {
            super(view);
            txtTownVisited = (TextView) view.findViewById(R.id.txtTownVisited);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            txtTotal = (TextView) view.findViewById(R.id.txtTotal);
            txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            expense = (RelativeLayout) view.findViewById(R.id.expense);
        }
    }
}
