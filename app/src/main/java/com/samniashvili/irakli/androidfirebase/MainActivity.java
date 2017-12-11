package com.samniashvili.irakli.androidfirebase;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private  Toolbar mToolbar;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
       mToolbar = findViewById(R.id.main_page_toolbar);
      setSupportActionBar(mToolbar);
      getSupportActionBar().setTitle("Lapin Chat");

      mViewPager = findViewById(R.id.tabPager);
      mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
      mViewPager.setAdapter(mSectionsPagerAdapter);

      mTabLayout = findViewById(R.id.main_tabs);
      mTabLayout.setupWithViewPager(mViewPager);

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null) {
           sendToStart();
        }
    }

    public  void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
      if(item.getItemId() == R.id.main_logout){
          FirebaseAuth.getInstance().signOut();
          sendToStart();
      }
      if(item.getItemId() == R.id.main_settings_btn) {
          Intent settingsIntent = new Intent(MainActivity.this,SettingsActivity.class);
          startActivity(settingsIntent);

      }
      return true;

    }
}
