package com.kraftlabs.crm_new.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.kraftlabs.crm_new.Activities.SalesRepActivity;
import com.kraftlabs.crm_new.Adapters.SpinnerRouteAdapter;
import com.kraftlabs.crm_new.Config.Config;
import com.kraftlabs.crm_new.Db.ExpenseDB;
import com.kraftlabs.crm_new.Db.RouteDB;
import com.kraftlabs.crm_new.Helper.DateHelper;
import com.kraftlabs.crm_new.Models.Expense;
import com.kraftlabs.crm_new.Models.Route;
import com.kraftlabs.crm_new.Models.User;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.Util.PrefUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class ExpenseFormFragment extends Fragment implements TextWatcher {

  private static final String EXPENSE_ID = "EXPENSE_ID";
  private static final String TAG = "ExpenseFormFragment";
  private static final String[] travelTypes = { "By Bike", "By Bus" };
  private double chargePerKM = 0;
  private double userDa = 0.0;
  private static final double maxDistance = 160.0;
  private static TextView txtDate;
  private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private int expenseId;
  private Spinner spnRoute;
  private TextView txtTownVisited;
  private TextView txtDa;
  private Spinner spnTaType;
  private TextView txtTaBus;
  private TextView txtTaBikeKm;
  private TextView txtTaBikeAmount;
  private TextView txtLodge;
  private TextView txtCourier;
  private TextView txtSundries;
  private TextView txtTotal;
  private TextView txtFreight;
  private TextInputLayout taBus;
  private TextInputLayout taBikeKm;
  private TextInputLayout taBikeAmount;
  private Button btnSubmit;
  private Button btnDate;
  private Context context;
  private ExpenseDB expenseDB;
  private Expense expense;
  private ArrayList<Expense> expenses = new ArrayList<>();

  private Boolean editMode = false;
  private ArrayList<Route> routes;
  private OnFragmentInteractionListener mListener;

  private Double da = 0.0;
  private Double ta = 0.0;
  private Double lodge = 0.0;
  private Double courier = 0.0;
  private Double sundries = 0.0;
  private Double total = 0.0;
  private Double bikeKM = 0.0;
  private Double bikeAmount = 0.0;
  private Double bus = 0.0;
  private Double freight = 0.0;
  private DateHelper dateHelper;
  private Route route;
  private RouteDB routeDB;
  private User user;

  public ExpenseFormFragment() {
    // Required empty public constructor
  }

  @SuppressLint("ValidFragment")
  public ExpenseFormFragment(Context context) {
    this.context = context;
  }

  public static ExpenseFormFragment newInstance(int expenseId) {
    ExpenseFormFragment fragment = new ExpenseFormFragment();
    Bundle args = new Bundle();
    args.putInt(EXPENSE_ID, expenseId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = getActivity();
    expenseDB = new ExpenseDB(context);
    user = PrefUtils.getCurrentUser(context);
    if (getArguments() != null) {
      expenseId = getArguments().getInt(EXPENSE_ID);
      editMode = true;
      expense = expenseDB.getExpense(expenseId);
    } else {
      expense = new Expense();
    }
    dateHelper = new DateHelper();
    routeDB = new RouteDB(context);
    configExpenses();
  }

  private void configExpenses() {
    if (user != null) {
      chargePerKM = user.getTa() == 0 ? 1.5 : user.getTa();
      userDa = user.getDa() == 0 ? 130 : user.getDa();
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_expense_form, container, false);
    spnRoute = view.findViewById(R.id.spnRoute);
    btnDate = view.findViewById(R.id.btnDate);
    txtTownVisited = view.findViewById(R.id.txtTownVisited);
    txtDate = view.findViewById(R.id.txtDate);
    txtDa = view.findViewById(R.id.txtDa);

    txtTaBus = view.findViewById(R.id.txtTaBus);
    txtTaBikeKm = view.findViewById(R.id.txtTaBikeKm);
    txtTaBikeAmount = view.findViewById(R.id.txtTaBikeAmount);
    txtLodge = view.findViewById(R.id.txtLodge);
    txtCourier = view.findViewById(R.id.txtCourier);
    txtSundries = view.findViewById(R.id.txtSundries);
    txtTotal = view.findViewById(R.id.txtTotal);
    txtFreight = view.findViewById(R.id.txtFreight);
    spnTaType = view.findViewById(R.id.spnTaType);

    txtDa.addTextChangedListener(this);
    txtTaBus.addTextChangedListener(this);
    txtLodge.addTextChangedListener(this);
    txtCourier.addTextChangedListener(this);
    txtSundries.addTextChangedListener(this);
    txtFreight.addTextChangedListener(this);

    taBus = view.findViewById(R.id.taBus);
    taBikeKm = view.findViewById(R.id.taBikeKm);
    taBikeAmount = view.findViewById(R.id.taBikeAmount);
    btnSubmit = view.findViewById(R.id.btnSubmit);
    txtDa.setText(userDa + "");
    btnSubmit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //  int dateDiff = dateHelper.dateDiff(txtDate.getText().toString());
        int j[] = DateHelper.dateDiff(txtDate.getText().toString());     //eg:8-11=-1
        int dateDiff = j[0];
        int limit = -1;
        if ((dateDiff > 0)) {
          Toast.makeText(context, context.getResources().getString(R.string.pls_enter_valid_date),
              Toast.LENGTH_SHORT).show();
        } else if ((dateDiff) >= (limit)) {
          saveExpense();
        } else {
          Toast.makeText(context, "you can't add expenses for this day", Toast.LENGTH_SHORT).show();
        }
        hideKeyboard(view);
      }
    });

    RouteDB routeDB = new RouteDB(context);
    routes = routeDB.getRoutes(0, 50);
    SpinnerRouteAdapter spinnerRouteAdapter = new SpinnerRouteAdapter(context, routes);
    spnRoute.setAdapter(spinnerRouteAdapter);

    ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
        android.R.layout.simple_spinner_item, travelTypes
    );

    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spnTaType.setAdapter(adapter);

    spnTaType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
          expense.setTaType(context.getResources().getString(R.string.by_bike));
          taBus.setVisibility(View.GONE);
          taBikeKm.requestFocus();
          taBikeKm.setVisibility(View.VISIBLE);
          taBikeAmount.setVisibility(View.VISIBLE);

          /*expense.setTaType("");
          taBus.setVisibility(View.GONE);
          taBikeKm.setVisibility(View.GONE);
          taBikeAmount.setVisibility(View.GONE);*/
        } else if (i == 1) {
          expense.setTaType(context.getResources().getString(R.string.by_bus));
          taBus.setVisibility(View.VISIBLE);
          txtTaBus.requestFocus();
          taBikeKm.setVisibility(View.GONE);
          taBikeAmount.setVisibility(View.GONE);
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
        expense.setTaType("");
      }
    });

    btnDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showDatePickerDialog(view);
      }
    });
    spnRoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        expense.setRouteId(routes.get(i).getRouteId());
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
        expense.setRouteId(routes.get(0).getRouteId());
      }
    });

    txtTaBikeKm.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void afterTextChanged(Editable editable) {

        Double distance = 0.0;
        try {
          distance = Double.parseDouble(txtTaBikeKm.getText().toString());
        } catch (ParseException e) {

        } catch (NumberFormatException e) {

        }
        if (distance > maxDistance) {
          Toast.makeText(context, context.getResources().getString(R.string.max_km_exceed),
              Toast.LENGTH_SHORT).show();
        }
        txtTaBikeAmount.setText("" + (distance * chargePerKM));
        setTotal();
      }
    });
    hideKeyboard(view);
    return view;
  }

  private void saveExpense() {

    if (txtTownVisited.getText().toString().trim().equals("")) {
      Toast.makeText(context, context.getResources().getString(R.string.enter_town_visited),
          Toast.LENGTH_SHORT).show();
    } else if (txtDate.getText().toString().trim().equals("")) {
      Toast.makeText(context, context.getResources().getString(R.string.enter_date),
          Toast.LENGTH_SHORT).show();
    } else {
      expense.setTownVisited(txtTownVisited.getText().toString());
      expense.setDate(txtDate.getText().toString());
      expense.setDa(da);
      expense.setFreight(freight);

      if (expense.getTaType().equals("")) {
        expense.setTaBus(0.0);
        expense.setTaBikeAmount(0.0);
        expense.setTaBikeKM(0.0);
        expense.setTa(0.0);
      } else if (expense.getTaType().equals(getString(R.string.by_bus))) {
        expense.setTaBus(bus);
        expense.setTaBikeAmount(0.0);
        expense.setTaBikeKM(0.0);
        expense.setTa(ta);
      } else {
        expense.setTaBus(0.0);
        expense.setTaBikeAmount(bikeAmount);
        expense.setTaBikeKM(bikeKM);
        expense.setTa(ta);
      }

      expense.setLodge(lodge);
      expense.setCourier(courier);
      expense.setSundries(sundries);

      String today = formatter.format(new Date());
      expense.setCreatedDate(today);
      expense.setStatus(getString(R.string.saved));

      if (editMode) {
        expense.setServerExpenseId(0);
        expenseDB.update(expense);
        Toast.makeText(context, context.getResources().getString(R.string.updated1),
            Toast.LENGTH_SHORT).show();
      } else {
        expenseId = expenseDB.insert(expense);
        expense.setExpenseId(expenseId);
        editMode = true;
        Toast.makeText(context, context.getResources().getString(R.string.inserted),
            Toast.LENGTH_SHORT).show();
        //Toast.makeText(context, context.getResources().getString(R.string.inserted), Toast.LENGTH_SHORT).show();
      }
      //   saveToServer();
      sync();

      final Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {

          Fragment fragment = new ExpenseFragment();
          // Toast.makeText(context, "leadId" + leadId, Toast.LENGTH_SHORT).show();
          FragmentManager fragmentManager =
              getActivity().getSupportFragmentManager();
          FragmentTransaction ft = fragmentManager.beginTransaction();
          ft.replace(R.id.content_main, fragment);
                  /*  String backStateName = fragment.getClass().getName();
                    ft.addToBackStack(null);
                    ft.detach(fragment);
                    ft.attach(fragment);*/
          ft.commit();
        }
      }, 600);
    }
  }

  protected void hideKeyboard(View view) {
    InputMethodManager in =
        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
  }

  public void sync() {

    try {
      expenseDB = new ExpenseDB(context);
    } catch (NullPointerException e) {
      expenseDB = new ExpenseDB(getActivity().getApplicationContext());
      context = getActivity().getApplicationContext();
    }
    expenses = expenseDB.getUnsentData();
    if (expenses.size() > 0) {
      for (int i = 0; i < expenses.size(); i++) {
        expense = expenses.get(i);
        saveToServer(expense.getExpenseId());
      }
    }
    Log.i(TAG, "sync: " + expenses.size());
  }

  public int syncCount() {
    try {
      expenseDB = new ExpenseDB(context);
    } catch (NullPointerException e) {
      expenseDB = new ExpenseDB(getActivity().getApplicationContext());
      context = getActivity().getApplicationContext();
    }
    expenses = expenseDB.getUnsentData();

    Log.i(TAG, "sync: " + expenses.size());
    return expenses.size();
  }

  public void saveToServer(final int id) {
    //        ((SalesRepActivity) context).showProgress(true);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SAVE_EXPENSE,
        s -> {
          Log.i(TAG, s);
          try {
            JSONObject object = new JSONObject(s);
            String status = object.get("status").toString();

            if (status.equals("success")) {
              /* expense = expenseDB.getExpense(id);*/

              int serverExpenseId = object.getInt("expense_id");

              expense = expenseDB.getExpense(id);
              expense.setServerExpenseId(serverExpenseId);
              expenseDB.update(expense);
            }
          } catch (JSONException e) {
            Log.i(TAG, e.getMessage());
          } catch (NullPointerException e) {
            Log.i(TAG, e.getMessage());
          }
          //      ((SalesRepActivity) context).showProgress(false);

        },
        volleyError -> {
          try {
            ((SalesRepActivity) context).showProgress(false);
          } catch (ClassCastException e) {
            e.printStackTrace();
          }
        }
    ) {
      @Override
      protected Map<String, String> getParams() {

        int userId = PrefUtils.getCurrentUser(context).getUserId();
        Map<String, String> params = new Hashtable<String, String>();

        expense = expenseDB.getExpense(id);
        Log.i(TAG, "getParams: " + expense);

        params.put("route_id", expense.getRouteId() + "");
        params.put("town_visited", expense.getTownVisited() + "");
        params.put("date", expense.getDate() + "");
        params.put("da", expense.getDa() + "");
        params.put("ta", expense.getTa() + "");
        params.put("ta_type", expense.getTaType() + "");
        params.put("ta_bus", expense.getTaBus() + "");
        params.put("ta_bike_km", expense.getTaBikeKM() + "");
        params.put("ta_bike_amount", expense.getTaBikeAmount() + "");
        params.put("lodge", expense.getLodge() + "");
        params.put("courier", expense.getCourier() + "");
        params.put("sundries", expense.getSundries() + "");
        params.put("total", expense.getTotal() + "");
        params.put("created_date", expense.getCreatedDate() + "");
        params.put("status", expense.getStatus() + "");
        params.put("user_id", Integer.toString(userId));
        params.put("freight", expense.getFreight() + "");

        return params;
      }
    };
    RequestQueue requestQueue = Volley.newRequestQueue(context);
    requestQueue.add(stringRequest);
  }

  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onResume() {
    super.onResume();
    getActivity().setTitle(getActivity().getResources().getString(R.string.expense));
    if (editMode) {
      route = routeDB.getRouteById(expense.getRouteId());
      //    spnRoute.setSelection(routes.indexOf(expense.getRouteId()));
      spnRoute.setPrompt(route.getRouteName());
      txtDate.setText(expense.getDate());
      txtTownVisited.setText(expense.getTownVisited());
      txtDa.setText(expense.getDa() + "");
      txtTaBus.setText(expense.getTaBus() + "");
      txtTaBikeKm.setText(expense.getTaBikeKM() + "");
      txtTaBikeAmount.setText(expense.getTaBikeAmount() + "");
      txtLodge.setText(expense.getLodge() + "");
      txtCourier.setText(expense.getCourier() + "");
      txtSundries.setText(expense.getSundries() + "");
      txtFreight.setText(expense.getFreight() + "");
      txtTotal.setText(expense.getTotal() + "");
      if (expense.getTaType() == null || expense.getTaType().equals("")) {
        spnTaType.setSelection(0);
        expense.setTaType("");
        taBus.setVisibility(View.GONE);
        taBikeKm.setVisibility(View.GONE);
        taBikeAmount.setVisibility(View.GONE);
      } else if (expense.getTaType().equalsIgnoreCase("by_bus")) {
        spnTaType.setSelection(1);
        taBus.setVisibility(View.GONE);
        taBikeKm.setVisibility(View.VISIBLE);
        taBikeAmount.setVisibility(View.VISIBLE);
      } else {
        spnTaType.setSelection(0);
        taBus.setVisibility(View.VISIBLE);
        taBikeKm.setVisibility(View.GONE);
        taBikeAmount.setVisibility(View.GONE);
      }
      setTotal();
    }
    if (!editMode) {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

      String today = formatter.format(new Date());
      txtDate.setText(today);
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  public void showDatePickerDialog(View v) {
    DialogFragment newFragment = new DatePickerFragment();
    newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
  }

  public void setTotal() {

    try {
      freight = Double.valueOf(txtFreight.getText().toString());
    } catch (NumberFormatException e) {

    }

    try {
      bikeKM = Double.parseDouble(txtTaBikeKm.getText().toString());
    } catch (NumberFormatException e) {

    }
    try {
      bikeAmount = Double.parseDouble(txtTaBikeAmount.getText().toString());
    } catch (NumberFormatException e) {

    }
    try {
      bus = Double.parseDouble(txtTaBus.getText().toString());
    } catch (NumberFormatException e) {

    }
    try {
      da = Double.parseDouble(txtDa.getText().toString());
    } catch (NumberFormatException e) {

    }

    try {
      lodge = Double.parseDouble(txtLodge.getText().toString());
    } catch (NumberFormatException e) {

    }

    try {
      courier = Double.parseDouble(txtCourier.getText().toString());
    } catch (NumberFormatException e) {

    }

    try {
      sundries = Double.parseDouble(txtSundries.getText().toString());
    } catch (NumberFormatException e) {

    }
    if (expense.getTaType() != null) {
      if (expense.getTaType()
          .equals(context.getResources().getString(R.string.by_bus))) {
        ta = bus;
      } else if (expense.getTaType().equals((context.getResources().getString(R.string.by_bike)))) {
        ta = bikeAmount;
      }
    }
    total = ta + da + lodge + courier + sundries + freight;
    expense.setTotal(total);
    txtTotal.setText(total + "");
  }

  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void afterTextChanged(Editable editable) {
    setTotal();
  }

  public interface OnFragmentInteractionListener {
    void onFragmentInteraction(Uri uri);
  }

  public static class DatePickerFragment extends DialogFragment
      implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      // Use the current date as the default date in the picker
      final Calendar c = Calendar.getInstance();
      int year = c.get(Calendar.YEAR);
      int month = c.get(Calendar.MONTH);
      int day = c.get(Calendar.DAY_OF_MONTH);
      // Create a new instance of DatePickerDialog and return it
      return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
      txtDate.setText(
          "" + year + (month < 9 ? "-0" : "-") + (month + 1) + (day < 10 ? "-0" : "-") + day);
    }
  }

  private int getIndex(Spinner spinner, String myString) {

    int index = 0;

    for (int i = 0; i < spinner.getCount(); i++) {
      if (spinner.getItemAtPosition(i).equals(myString)) {
        index = i;
      }
    }
    return index;
  }
}
