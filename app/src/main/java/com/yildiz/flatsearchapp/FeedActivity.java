package com.yildiz.flatsearchapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class FeedActivity extends AppCompatActivity {
    FirebaseAuth auth;
    ActionBar actionBar;
    private NoticeFragment notFrag;
    private FragmentTransaction notFragTrans;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_page);
        auth = FirebaseAuth.getInstance();
        actionBar = getSupportActionBar();
        actionBar.setTitle("Duyurular");
        notFrag = new NoticeFragment();
        changeFragment(notFrag);

        NavigationBarView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();
            switch (itemID){
                case R.id.navigate_map:
                    actionBar.setTitle("Harita");
                    SocialFragment socFrag = new SocialFragment();
                    changeFragment(socFrag);
                    break;
                case R.id.navigate_notifications:
                    actionBar.setTitle("Bildirimler");
                    NoticeFragment notFrag = new NoticeFragment();
                    changeFragment(notFrag);
                    break;
                case R.id.navigate_search:
                    actionBar.setTitle("Arama");
                    SearchFragment searchFrag = new SearchFragment();
                    changeFragment(searchFrag);
                    break;

            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.menu_editProfile){
            Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
            startActivity(intent);
            //onPause();
        }else if(id == R.id.menu_logout){
            auth.signOut();
            //View view = item.getActionView();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }


    return true;
    }

    private void changeFragment(Fragment fragment){
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.replace(R.id.feedContainer,fragment,"");
        fragTrans.commit();
    }

}
