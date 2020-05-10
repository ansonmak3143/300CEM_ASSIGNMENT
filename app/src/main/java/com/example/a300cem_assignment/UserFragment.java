package com.example.a300cem_assignment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "userName";
    private String userName;
    private EditText userEditName;
    private TextView userNameTxt;
    private LinearLayout userEditInforLayout;
    private SharedPreferences sharedPrefs;
    private  Button submit,cancel,edit;


    public UserFragment() {
    }

    static UserFragment newInstance(String param1) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPrefs = getActivity().getSharedPreferences("userInfor", Context.MODE_PRIVATE);
        userNameTxt = view.findViewById(R.id.userNameTxt);
        userEditName = view.findViewById(R.id.userInputEditName);
        submit = view.findViewById(R.id.userEditInforSubmitBtn);
        cancel = view.findViewById(R.id.userInforCancel);
        edit = view.findViewById(R.id.userInforEdit);
        userEditInforLayout = view.findViewById(R.id.userEditInforLayout);
        userNameTxt.setText(userName);
        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userEditInforSubmitBtn:
                SharedPreferences.Editor editer = sharedPrefs.edit();
                String tmpUserName, tmpUserPhone;
                tmpUserName = userEditName.getText().toString().trim();
                if (isEmptyString(tmpUserName)) {
                    Toast.makeText(getActivity(), getString(R.string.toastEmptyName), Toast.LENGTH_SHORT).show();
                } else {
                    editer.putString("userName", tmpUserName);
                    editer.putBoolean("initialized", true);
                    editer.apply();
                    userNameTxt.setText(tmpUserName);
                    cancel.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);
                    userEditInforLayout.setVisibility(View.GONE);
                    edit.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.userInforCancel:
                cancel.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
                userEditInforLayout.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
                break;
            case R.id.userInforEdit:
                edit.setVisibility(View.GONE);
                userEditInforLayout.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    private static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }
}
