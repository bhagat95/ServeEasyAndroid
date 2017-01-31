package layout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bhagat.finalyear.ListData;
import com.example.bhagat.finalyear.ProviderHome;
import com.example.bhagat.finalyear.R;
import com.example.bhagat.finalyear.RequestDetails;
import com.example.bhagat.finalyear.RequestsAdapter;
import com.example.bhagat.finalyear.UserDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bhagat on 10/9/16.
 */
public class ActiveRequests extends Fragment implements RequestDetails.RequestDialogResponse {

    ListView requestsList;
    ArrayList<ListData> arrayOfItems;
    RequestsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_active_requests, container, false);

        requestsList = (ListView) v.findViewById(R.id.list);

        //  ListView
        arrayOfItems = new ArrayList<ListData>();
        //temporary add
        //for(int i =0; i<10;i++)
        //  arrayOfItems.add(new ListData());
        arrayOfItems = new ArrayList<ListData>();

        getRequests();

        adapter = new RequestsAdapter(getContext(), 0, arrayOfItems);
        requestsList.setAdapter(adapter);

        //  To open the dialog for details
        requestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView consumerNameView = (TextView)view.findViewById(R.id.consumer);
                String consumerName = consumerNameView.getText().toString();

                TextView categoryNameView = (TextView)view.findViewById(R.id.category);
                String categoryName = categoryNameView.getText().toString();

                TextView distanceView = (TextView)view.findViewById(R.id.distance);
                String distance = distanceView.getText().toString();

                TextView quantityView = (TextView)view.findViewById(R.id.quantity);
                String quantity = quantityView.getText().toString();

                String request_id = "";

                try {
                    request_id = arrayOfItems.get(i).jOb.getString("request_id");
                }
                catch (Exception e){

                }




                showDetails(i,consumerName,categoryName,distance,quantity,request_id);

                //showDetails(i);
            }
        });


        return v;

    }
    /*private void showDetails(int position)
    {
        RequestDetails dialog = new RequestDetails();
        dialog.show(getFragmentManager(), "dialogTag");
    }*/
    private void showDetails(int position,String consumerName,String categoryName,String distance, String quantity,String request_id) {

        FragmentManager fm = getFragmentManager();

        Bundle args = new Bundle();
        args.putInt("position",position);
        args.putString("consumerName",consumerName);
        args.putString("categoryName",categoryName);
        args.putString("quantity",quantity);
        args.putString("distance",distance);
        args.putString("request_id",request_id);

        RequestDetails details = RequestDetails.newInstance();
        details.setTargetFragment(this, 0);
        details.setArguments(args);

        details.show(fm, "dialogTag");
    }

    @Override
    public void onDialogResponse(String response) {
        if(response.equals("accept")){
            Toast.makeText(getActivity(), "Accept ho gaya re bhai", Toast.LENGTH_SHORT).show();
        }
        else if (response.equals("decline")){
            Toast.makeText(getActivity(), "lawde lelo", Toast.LENGTH_SHORT).show();
        }
    }

/*
    void onRequestDecline(int position){
        //notify server about decline
        //add changes to transactions
        arrayOfItems.remove(position);
        adapter.notifyDataSetChanged();
    }
*/
    void getRequests(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        String url = UserDetails.getInstance().url + "fetch_requests.php";
        //->GET REQUEST USING VOLLEY
        StringRequest request = new StringRequest( Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ActiveRequests response", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jOb = jsonArray.getJSONObject(i);
                                arrayOfItems.add(new ListData(jOb));
                            }

                            } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        RequestsAdapter adapter = new RequestsAdapter(getActivity(), 0, arrayOfItems);
                        requestsList.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("fetch_requests error",error.toString());
                    }
                }
        )

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("provider_id", UserDetails.getInstance().providerId);
                params.put("type", "active");
                //params.put("consumer_locy", "3");
                return params;
            }
        } ;
        requestQueue.add(request);

    }


}






















//this is to multiple delete to be pasted in onCreateView
/*
        // define Choice mode for multiple  delete
        requestsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        requestsList.setMultiChoiceModeListener(new  AbsListView.MultiChoiceModeListener() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.getMenuInflater().inflate(R.menu.multiple_delete, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.selectAll:
                        //
                        final int checkedCount = arrayOfItems.size();
                        // If item  is already selected or checked then remove or
                        // unchecked  and again select all
                        adapter.removeSelection();
                        for (int i = 0; i < checkedCount; i++) {
                            requestsList.setItemChecked(i, true);
                            //  listviewadapter.toggleSelection(i);
                        }
                        // Set the  CAB title according to total checked items
                        // Calls  toggleSelection method from ListViewAdapter Class
                        // Count no.  of selected item and print it
                        actionMode.setTitle(checkedCount + "  Selected");

                        return true;

                    case R.id.delete:
                        // Add  dialog for confirmation to delete selected item record.
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        builder.setMessage("Do you  want to delete selected record(s)?");


                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO  Auto-generated method stub
                            }
                        });

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override

                            public void onClick(DialogInterface dialog, int which) {

                                // TODO  Auto-generated method stub

                                SparseBooleanArray selected = adapter.getSelectedIds();
                                for (int i = (selected.size() - 1); i >= 0; i--) {
                                    if (selected.valueAt(i)) {
                                        ListData selecteditem = adapter.getItem(selected.keyAt(i));
                                        // Remove  selected items following the ids
                                        adapter.remove(selecteditem);
                                    }
                                }
                                // Close CAB
                                actionMode.finish();
                                selected.clear();
                            }

                        });

                        AlertDialog alert = builder.create();

                        alert.setIcon(R.drawable.ic_question_answer);// dialog  Icon

                        alert.setTitle("Confirmation"); // dialog  Title

                        alert.show();

                        return true;

                    default:
                    return false;
                }
            }
            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }

            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

            }

        });
        */
