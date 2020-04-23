package com.example.caps_example2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private homeFragment homeFrg = new homeFragment();
    private myPageFragment myPageFrg = new myPageFragment();
    private newsFragment newsFrg = new newsFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, homeFrg).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.homeItem:
                    transaction.replace(R.id.frameLayout, homeFrg).commitAllowingStateLoss();
                    break;

                case R.id.myPageItem:
                    transaction.replace(R.id.frameLayout, myPageFrg).commitAllowingStateLoss();
                    break;

                case R.id.newsItem:
                    transaction.replace(R.id.frameLayout, newsFrg).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}
