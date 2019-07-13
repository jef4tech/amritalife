package com.kraftlabs.crm_new.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.kraftlabs.crm_new.Adapters.ProductAdapter;
import com.kraftlabs.crm_new.Db.CategoryDB;
import com.kraftlabs.crm_new.Db.ProductsDB;
import com.kraftlabs.crm_new.Models.Category;
import com.kraftlabs.crm_new.Models.Product;
import com.kraftlabs.crm_new.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {

  private static final String TAG = "ProductFragment";
  private static final String IS_LISTNG = "is_listing";
  private static boolean isListing = true;
  private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private ArrayList<Category> categories;
  private List<String> list;
  private Context context;
  private ProductsDB productDB;
  private Spinner spnCategory;
  private EditText txtProductHint;
  private ProductAdapter productAdapter;
  private RecyclerView lstProduct;
  private ArrayList<Product> products;
  private CategoryDB categoryDB;
  private TextView txtProductEmpty;
  private View v1;
  private ProductFragment.OnFragmentInteractionListener mListener;
  private Menu menu;
  private Animator spruceAnimator;

  public ProductFragment() {
    // Required empty public constructor
  }

  public static ProductFragment newInstance(Boolean isListing) {
    ProductFragment fragment = new ProductFragment();
    Bundle args = new Bundle();
    args.putBoolean(IS_LISTNG, isListing);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      isListing = getArguments().getBoolean(IS_LISTNG);
    }
    context = getActivity();
    categoryDB = new CategoryDB(context);
    categories = categoryDB.getAllCategories();

    list = new ArrayList<String>();
    list.add(getString(R.string.all_categoies));
    for (int i = 0; i < categories.size(); i++) {
      list.add(categories.get(i).mName);
    }
    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_product, container, false);
    txtProductEmpty = (TextView) view.findViewById(R.id.txtProductEmpty);

    lstProduct = (RecyclerView) view.findViewById(R.id.lstProduct);

    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
    lstProduct.setLayoutManager(mLayoutManager);
    lstProduct.setItemAnimator(new DefaultItemAnimator());
    lstProduct.addItemDecoration(
        new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    lstProduct.setHasFixedSize(false);
    lstProduct.setItemViewCacheSize(20);
    lstProduct.setDrawingCacheEnabled(true);
    lstProduct.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

    lstProduct.invalidate();

    //initSpruce();

    txtProductHint = (EditText) view.findViewById(R.id.txtProductHint);
    spnCategory = (Spinner) view.findViewById(R.id.spnCategory);

    ArrayAdapter<String> categoryAdapter =
        new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
            list);

    spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        filterItemList();
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }
    });
    spnCategory.setAdapter(categoryAdapter);
    txtProductHint.addTextChangedListener(new TextWatcher() {
      public void afterTextChanged(Editable s) {

      }

      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      public void onTextChanged(CharSequence s, int start, int before, int count) {
        filterItemList();
      }
    });
    setHasOptionsMenu(true);
      hideKeyboard(view);
    return view;
  }

  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    this.menu = menu;
    if (!isListing) {
      inflater.inflate(R.menu.item_menu, menu);
    }
    showOverflowMenu();
    super.onCreateOptionsMenu(menu, inflater);
  }

  public void showOverflowMenu() {
    if (menu == null) {
      return;
    }
    menu.setGroupVisible(R.id.done_menu_group, true);
    menu.setGroupVisible(R.id.sync_menu_group, false);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_done) {
      v1 = getActivity().getCurrentFocus();
      if (v1 != null) {
        InputMethodManager imm =
            (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v1.getWindowToken(), 0);
      }

      Fragment fragment = new OrderCreationFragment();
      String backStateName = fragment.getClass().getName();
         FragmentTransaction fragmentManager =
          getActivity().getSupportFragmentManager().beginTransaction();
      fragmentManager.replace(R.id.content_main, fragment);
      fragmentManager.addToBackStack(backStateName);
      fragmentManager.commit();
      getActivity().getSupportFragmentManager().executePendingTransactions();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
  private void showProgress(final boolean show) {
    final View mProgressView = getActivity().findViewById(R.id.login_progress);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

      mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
      mProgressView.animate().setDuration(shortAnimTime).alpha(
          show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
      });
    } else {
      mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if (isListing) {
      getActivity().setTitle(getActivity().getResources().getString(R.string.products));
    } else {
      getActivity().setTitle(getActivity().getResources().getString(R.string.select_products));
    }
    productDB = new ProductsDB(context);
    products = productDB.getProducts("", "", 0, 50);
    productAdapter = new ProductAdapter(context, products, isListing);
    lstProduct.setAdapter(productAdapter);

    if (products.size() == 0) {
      txtProductEmpty.setVisibility(View.VISIBLE);
    } else {
      txtProductEmpty.setVisibility(View.GONE);
    }
    if (spruceAnimator != null) {
      spruceAnimator.start();
    }
  }

  public void filterItemList() {
    String category = String.valueOf(spnCategory.getSelectedItem());
    if (category == getString(R.string.all_categoies)) {
      category = "";
    }
    String hint = txtProductHint.getText().toString();
    products = productDB.getProducts(hint, category, 0, 50);
    productAdapter = new ProductAdapter(context, products, isListing);
    lstProduct.setAdapter(productAdapter);

    if (products.size() == 0) {
      txtProductEmpty.setVisibility(View.VISIBLE);
    } else {
      txtProductEmpty.setVisibility(View.GONE);
    }
  }



  public interface OnFragmentInteractionListener {
    void onFragmentInteraction(Uri uri);
  }

    protected void hideKeyboard(View view) {
        InputMethodManager in =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
