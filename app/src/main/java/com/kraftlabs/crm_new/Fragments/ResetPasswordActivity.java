package com.kraftlabs.crm_new.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kraftlabs.crm_new.Config.Config;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.Util.PrefUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;



import static com.kraftlabs.crm_new.R.id.edtTxt_newPassword;


public class ResetPasswordActivity extends Fragment {
    EditText edtTxtCurrentPassword, edtTxtNewPassword, edtTxtConfirmPassword;
    TextView content;
    Button btn_changePassword;
    private String TAG = "LoginActivity";
    private Context context;

    public ResetPasswordActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        edtTxtCurrentPassword = (EditText) view.findViewById(R.id.edtTxt_currentPassword);
        edtTxtNewPassword = (EditText) view.findViewById(edtTxt_newPassword);
        edtTxtConfirmPassword = (EditText) view.findViewById(R.id.edtTxt_confirmPassword);
        content = (TextView) view.findViewById(R.id.content);
        btn_changePassword = (Button) view.findViewById(R.id.btn_changePassword);
        btn_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtTxtNewPassword.getText().toString().equals(edtTxtConfirmPassword.getText().toString())) {

                    attemptResetPass();

                } else {
                   // Toast.makeText(getActivity().getApplicationContext(), context.getResources().getString( R.string.password_not_match), Toast.LENGTH_SHORT, true).show();

             Toast.makeText(context, R.string.password_not_match, Toast.LENGTH_LONG).show();

                }
            }
        });
        hideKeyboard(view);
        return view;
    }


    public void attemptResetPass() {
        edtTxtCurrentPassword.setError(null);
        edtTxtNewPassword.setError(null);
        edtTxtConfirmPassword.setError(null);
        String strCurrentPassword = edtTxtCurrentPassword.getText().toString().trim();
        String strNewPassword = edtTxtNewPassword.getText().toString().trim();
        String strConfirmPassword = edtTxtConfirmPassword.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(strCurrentPassword)) {
            edtTxtCurrentPassword.setError(content.getResources().getString(R.string.error_field_required));
            focusView = edtTxtCurrentPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(strNewPassword)) {
            edtTxtNewPassword.setError(content.getResources().getString(R.string.error_field_required));
            focusView = edtTxtNewPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(strConfirmPassword)) {
            edtTxtConfirmPassword.setError(content.getResources().getString(R.string.error_field_required));
            focusView = edtTxtConfirmPassword;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            //  showProgress(true);
            resetPass(strCurrentPassword, strNewPassword);
        }

    }

    private void resetPass(final String strCurrentPassword, final String strNewPassword) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.RESET_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONObject object = new JSONObject(response);
                    String status = object.get("Status").toString();
                    if (status.equals("success")) {
                        Toast.makeText(context, R.string.password_reset_success, Toast.LENGTH_SHORT).show();
                      //  Toasty.success(getActivity().getApplicationContext(), context.getResources().getString( R.string.password_reset_success), Toast.LENGTH_SHORT, true).show();


                      /* Fragment fragment = null;
                       fragment = new ProfileFragment();

                       FragmentManager manager = getActivity().getSupportFragmentManager();
                       FragmentTransaction transaction = manager.beginTransaction();
                       transaction.replace(R.id.reset_password_layuot, fragment);
                       transaction.commit();*/
                    } else {


                        edtTxtCurrentPassword.setError(getString(R.string.error_incorrect_password));
                        edtTxtCurrentPassword.requestFocus();
                    }

                } catch (JSONException e) {
                //    Toasty.error(getActivity().getApplicationContext(), context.getResources().getString( R.string.server_error), Toast.LENGTH_SHORT, true).show();
    Toast.makeText(context, R.string.server_error, Toast.LENGTH_LONG).show();

                } catch (NullPointerException e) {
                    Log.i(TAG, "onResponse: ");
                } catch (NumberFormatException e) {
                    Log.i(TAG, "onResponse: ");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, R.string.network_error, Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("user_id", PrefUtils.getCurrentUser(context).getUserId() + "");
                params.put("old_password", strCurrentPassword);
                params.put("password", strNewPassword);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }


    public void GetText() throws IOException {
        String data = URLEncoder.encode("name", "UTF-8")
                + "=" + URLEncoder.encode(String.valueOf(edtTxtCurrentPassword), "UTF-8");

        data += "&" + URLEncoder.encode("user", "UTF-8")
                + "=" + URLEncoder.encode(String.valueOf(edtTxtNewPassword), "UTF-8");
        String text = "";
        BufferedReader reader = null;
        try {

            URL url = new URL("");

            // Send POST data request
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            // Get the server response
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }
            text = sb.toString();
        } catch (Exception ex) {

        } finally {
            try {

                reader.close();
            } catch (Exception ex) {
            }
        }
    Toast.makeText(context, getString(R.string.password_reset_success), Toast.LENGTH_LONG);
    //    Toasty.success(getActivity().getApplicationContext(), context.getResources().getString( R.string.password_reset_success), Toast.LENGTH_SHORT, true).show();

        content.setText(text);
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
