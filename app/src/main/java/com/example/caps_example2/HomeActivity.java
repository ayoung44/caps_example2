package com.example.caps_example2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    // 지도, 뉴스, 게시판, 마이페이지 fragment
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private mapFragment mapFrg = new mapFragment();
    private newsFragment newsFrg = new newsFragment();
    private boardFragment boardFrg = new boardFragment();
    private myPageFragment myPageFrg = new myPageFragment();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 맨 처음 화면이 나타날 때 FrameLayout 에 표시할 fragment 를 추가함
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, mapFrg).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.mapItem:
                    transaction.replace(R.id.frameLayout, mapFrg).commitAllowingStateLoss();
                    break;

                case R.id.newsItem:
                    transaction.replace(R.id.frameLayout, newsFrg).commitAllowingStateLoss();
                    break;

                case R.id.boardItem:
                    transaction.replace(R.id.frameLayout, boardFrg).commitAllowingStateLoss();
                    break;

                case R.id.myPageItem:
                    transaction.replace(R.id.frameLayout, myPageFrg).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}
