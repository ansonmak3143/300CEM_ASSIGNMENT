package com.example.a300cem_assignment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewOrderFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vieworder, container, false);
    }
}
