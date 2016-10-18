package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bhagat.finalyear.ListData;
import com.example.bhagat.finalyear.MainActivity;
import com.example.bhagat.finalyear.R;
import com.example.bhagat.finalyear.RequestDetails;
import com.example.bhagat.finalyear.RequestsAdapter;

import java.util.ArrayList;

/**
 * Created by bhagat on 10/9/16.
 */
public class ActiveRequests extends Fragment {

    ListView requestsList;
    ArrayList<ListData> arrayOfItems;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_active_requests, container, false);

        requestsList = (ListView) v.findViewById(R.id.list);

        //  ListView
        arrayOfItems = new ArrayList<ListData>();
        //temporary add
        for(int i =0; i<10;i++)
            arrayOfItems.add(new ListData());

        RequestsAdapter adapter = new RequestsAdapter(getContext(), 0, arrayOfItems);
        requestsList.setAdapter(adapter);



//  To open the dialog for details
/*
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDetails();
            }
        });
*/
        requestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDetails(i);
            }
        });




    return v;
    }

    private void showDetails(int position)
    {
        RequestDetails dialog = new RequestDetails();
        dialog.show(getFragmentManager(), "dialogTag");
    }

}
