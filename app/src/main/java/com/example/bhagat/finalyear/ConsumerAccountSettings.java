package com.example.bhagat.finalyear;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConsumerAccountSettings extends Fragment {
    TextView seekBarDescription,seekBarProgress;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_consumer_account_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPreferences.edit();
        seekBarDescription = (TextView)getActivity().findViewById(R.id.seekbar_description);
        seekBarProgress = (TextView) getActivity().findViewById(R.id.seekbar_progress);
        seekBarDescription.setText("Set distance upto which you would like to view services near you.");
        SeekBar seekBar = (SeekBar) getActivity().findViewById(R.id.seekbar);
        seekBarProgress.setText(sharedPreferences.getString("radial_distance","50")+" KM");
        int curRadialDistance = Integer.parseInt(sharedPreferences.getString("radial_distance","50"));
        seekBar.setProgress(curRadialDistance);

        /*seekBar.setOnTouchListener(new SeekBar.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View parent = (View)v.getParent();
                TextView textView = (TextView)parent.findViewById(R.id.seekbar_progress);
                if (textView != null) {
                    SeekBar seekBar = (SeekBar)v;
                    textView.setText(seekBar.getProgress() + "KM");
                    //textView.setText(seekBar.getProgress() + "/" + seekBar.getMax());
                }
                return false;
            }
        });*/

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarProgress.setText(seekBar.getProgress() + "KM");
                editor.putString("radial_distance",seekBar.getProgress()+"");
                editor.apply();
                updateRadialDistance(seekBar.getProgress()+"");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    public void updateRadialDistance(String radialDistance){
        Map<String, String> params = new HashMap<>();
        //Toast.makeText(getActivity(),UserDetails.getInstance().userId,Toast.LENGTH_LONG).show();
        Log.d("USERID",UserDetails.getInstance().userId);
        params.put("consumer_id", UserDetails.getInstance().userId);
        params.put("radial_distance", radialDistance);
        String url = UserDetails.getInstance().url + "update_radial_distance.php";
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        VolleyNetworkManager.getInstance(getContext()).makeRequest(params,
                url, new VolleyNetworkManager.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("RESPONSE12", response);
                        pDialog.hide();
                    }
                    @Override
                    public void onError(String error) {
                        Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
                        pDialog.hide();
                    }
                });
    }
}
