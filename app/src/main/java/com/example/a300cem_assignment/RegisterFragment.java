package com.example.a300cem_assignment;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentTransaction;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

public class RegisterFragment extends Fragment implements View.OnClickListener{

    private SharedPreferences sharedPrefs;
    private Button regBtn;
    private EditText userName;
    private EditText userPhone;

    public RegisterFragment() {
    }

    static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPrefs = getActivity().getSharedPreferences("userInfor", Context.MODE_PRIVATE);
        regBtn = view.findViewById(R.id.registerSubmit);
        userName = view.findViewById(R.id.userInputRegisterName);
        userPhone = view.findViewById(R.id.userInputRegisterPhone);
        regBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.registerSubmit) {
            throw new IllegalStateException("Unexpected value: " + v.getId());
        } else {
            SharedPreferences.Editor edit = sharedPrefs.edit();
            String tmpUserName, tmpUserPhone;
            tmpUserName = userName.getText().toString().trim();
            tmpUserPhone = userPhone.getText().toString().trim();
            if (isEmptyString(tmpUserName)) {
                Toast.makeText(getActivity(), getString(R.string.toastEmptyName), Toast.LENGTH_SHORT).show();
            } else if (isEmptyString(tmpUserPhone)) {
                Toast.makeText(getActivity(), getString(R.string.toastEmptyPhone), Toast.LENGTH_SHORT).show();
            } else {
                edit.putString("userName", tmpUserName);
                edit.putString("userPhone", tmpUserPhone);
                edit.putBoolean("initialized", true);
                edit.apply();
                openFragment(OrderFragment.newInstance(tmpUserName, tmpUserPhone));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }
}
