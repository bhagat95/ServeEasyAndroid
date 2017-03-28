package com.example.bhagat.finalyear;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**]
 * Created by Shashank on 09-10-2016.
 * Improved by bhagat on 10/28/16.
 */
public class AccountSettings extends Fragment {
    ArrayList<String> categoryName;
    EditText textIn;
    Button buttonAdd, buttonSave;
    LinearLayout container;
    EditText service_name;
    TextView tvcategoryName;
    ProgressDialog pDialog;
    static int PROVIDER_ID = Integer.parseInt(UserDetails.getInstance().providerId);
    static String SERVICE_NAME = "";
    String TAG = "CancelRequests";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_settings, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        categoryName = new ArrayList<>();
        service_name = (EditText) getActivity().findViewById(R.id.etservice_name);
        tvcategoryName = (TextView) getActivity().findViewById(R.id.tvcat_name);
        buttonSave = (Button) getActivity().findViewById(R.id.buttonSave);
        textIn = (EditText) getActivity().findViewById(R.id.textin);
        buttonAdd = (Button) getActivity().findViewById(R.id.add);
        container = (LinearLayout) getActivity().findViewById(R.id.container);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.remove_category, null);
                TextView textOut = (TextView) addView.findViewById(R.id.textout);
                textOut.setText(textIn.getText().toString());
                Button buttonRemove = (Button) addView.findViewById(R.id.remove);
                buttonRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout) addView.getParent()).removeView(addView);
                    }
                });
                container.addView(addView);
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SERVICE_NAME = service_name.getText().toString();
                for (int i = 0; i < container.getChildCount(); i++) {
                    RelativeLayout temp = (RelativeLayout) container.getChildAt(i);
                    TextView tvCategory = (TextView) temp.findViewById(R.id.textout);
                    categoryName.add(tvCategory.getText().toString() + "");
                }
                postRequest();
            }
        });
    }
    public void postRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("providerID", PROVIDER_ID + "");
        params.put("serviceName", SERVICE_NAME);
        params.put("noOfCategories", categoryName.size() + "");
        for (int i = 0; i < categoryName.size(); i++) {
            params.put("category" + i, categoryName.get(i));
            Log.d("category", categoryName.get(i));
        }
        categoryName.clear();
        VolleyNetworkManager.getInstance(getContext()).makeRequest(params,
                "http://192.168.109.41/se_addService.php", new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("RESPONSE", response);
                    }
                });
    }
}