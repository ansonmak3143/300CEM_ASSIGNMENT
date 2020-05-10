package com.example.a300cem_assignment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewOrderFragment extends Fragment {

    private DBHelper DH = null;

    public ViewOrderFragment() {
        // Required empty public constructor
    }

    static ViewOrderFragment newInstance() {
        return new ViewOrderFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        openDB();
        final ListView list = view.findViewById(R.id.listHistoryListView);
        ArrayList<RecordObject> arrayList = DH.getAllRecord();
        CustomAdapter customAdapter = new CustomAdapter(getContext(), arrayList);
        if(!arrayList.isEmpty()) list.setAdapter(customAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        closeDB();
    }

    private void openDB(){
        DH = new DBHelper(getContext());
    }
    private void closeDB(){
        DH.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vieworder, container, false);
    }
}
