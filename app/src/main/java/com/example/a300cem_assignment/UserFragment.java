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
    private static final String ARG_PARAM2 = "userPhone";
    private String userName, userPhone;
    private EditText userEditName, userEditPhone;
    private TextView userNameTxt, userPhoneTxt;
    private LinearLayout userEditInforLayout;
    private SharedPreferences sharedPrefs;
    private  Button submit,cancel,edit;


    public UserFragment() {
    }

    static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString(ARG_PARAM1);
            userPhone = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPrefs = getActivity().getSharedPreferences("userInfor", Context.MODE_PRIVATE);
        userNameTxt = view.findViewById(R.id.userNameTxt);
        userPhoneTxt = view.findViewById(R.id.userPhoneTxt);
        userEditName = view.findViewById(R.id.userInputEditName);
        userEditPhone = view.findViewById(R.id.userInputEditPhone);
        submit = view.findViewById(R.id.userEditInforSubmitBtn);
        cancel = view.findViewById(R.id.userInforCancel);
        edit = view.findViewById(R.id.userInforEdit);
        userEditInforLayout = view.findViewById(R.id.userEditInforLayout);
        userNameTxt.setText(userName);
        userPhoneTxt.setText(userPhone);
        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        edit.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userEditInforSubmitBtn:
                SharedPreferences.Editor editer = sharedPrefs.edit();
                String tmpUserName, tmpUserPhone;
                tmpUserName = userEditName.getText().toString().trim();
                tmpUserPhone = userEditPhone.getText().toString().trim();
                if (isEmptyString(tmpUserName)) {
                    Toast.makeText(getActivity(), getString(R.string.toastEmptyName), Toast.LENGTH_SHORT).show();
                } else if (isEmptyString(tmpUserPhone)) {
                    Toast.makeText(getActivity(), getString(R.string.toastEmptyPhone), Toast.LENGTH_SHORT).show();
                } else {
                    editer.putString("userName", tmpUserName);
                    editer.putString("userPhone", tmpUserPhone);
                    editer.putBoolean("initialized", true);
                    editer.apply();
                    userNameTxt.setText(tmpUserName);
                    userPhoneTxt.setText(tmpUserPhone);
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
