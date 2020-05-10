package com.example.a300cem_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPrefs;

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
            String tmpUserName;
            tmpUserName = sharedPrefs.getString("userName","Error");
            openFragment(OrderFragment.newInstance(tmpUserName));
        }
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    String tmpUserName;
                    tmpUserName = sharedPrefs.getString("userName","Error");
                    switch (item.getItemId()) {
                        case R.id.navigation_order:
                            openFragment(OrderFragment.newInstance(tmpUserName));
                            return true;
                        case R.id.navigation_viewOrder:
                            openFragment(ViewOrderFragment.newInstance());
                            return true;
                        case R.id.navigation_user:
                            openFragment(UserFragment.newInstance(tmpUserName));
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
