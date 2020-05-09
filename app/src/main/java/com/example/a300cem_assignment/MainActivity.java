package com.example.a300cem_assignment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPrefs;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        sharedPrefs = getSharedPreferences("userInfor", MODE_PRIVATE);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        if(!sharedPrefs.contains("initialized")){
            openFragment(RegisterFragment.newInstance());
        }else{
            String tmpUserName, tmpUserPhone;
            tmpUserName = sharedPrefs.getString("userName","Error");
            tmpUserPhone = sharedPrefs.getString("userPhone","999");
            openFragment(OrderFragment.newInstance(tmpUserName, tmpUserPhone));
        }
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    String tmpUserName, tmpUserPhone;
                    tmpUserName = sharedPrefs.getString("userName","Error");
                    tmpUserPhone = sharedPrefs.getString("userPhone","999");
                    switch (item.getItemId()) {
                        case R.id.navigation_order:
                            openFragment(OrderFragment.newInstance(tmpUserName, tmpUserPhone));
                            return true;
                        case R.id.navigation_pickOrder:
                            openFragment(PickOrderFragment.newInstance());
                            return true;
                        case R.id.navigation_viewOrder:
                            openFragment(ViewOrderFragment.newInstance());
                            return true;
                        case R.id.navigation_user:
                            openFragment(UserFragment.newInstance(tmpUserName, tmpUserPhone));
                            return true;
                    }
                    return false;
                }
            };

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
