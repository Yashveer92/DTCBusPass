package com.example.dtcbuspass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.example.dtcbuspass.LoginSignUp.LoginActivity;
import com.example.dtcbuspass.RenewPass.RenewPassNumber;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    ViewFlipper viewFlipper;
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrawerLayout;
    private  LinearLayout buyBusPass,feedback_and_suggestions,history,track_application_status,renew_pass,moreinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
         mDrawerLayout=findViewById(R.id.drawer_layout);

        BottomNavigationView navigation = findViewById(R.id.botton_navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        buyBusPass=findViewById(R.id.buy_bus_pass_ll);
        history=findViewById(R.id.history);
        renew_pass=findViewById(R.id.renew_pass);
        track_application_status=findViewById(R.id.track_application_status_ll);
        feedback_and_suggestions=findViewById(R.id.feedback_and_suggestions);
        feedback_and_suggestions.setOnClickListener(this);

        moreinfo=findViewById(R.id.more_info);
        moreinfo.setOnClickListener(this);

        buyBusPass.setOnClickListener(this);
        history.setOnClickListener(this);
        track_application_status.setOnClickListener(this);
        renew_pass.setOnClickListener(this);

        setNavigationDrawer();
        setBottomNavigation();




    }

    private void setNavigationDrawer() {


        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.setDrawerIndicatorEnabled(true);

        mToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mMenuMyVehicleLayout.setVisibility(View.GONE);
                if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set Navigation Header
        View headerView      =  navigationView.getHeaderView(0);

        ImageView iv_profile =  headerView.findViewById(R.id.nav_profile_pic);
//        TextView tv_name     =  headerView.findViewById(R.id.name);
//        TextView tv_mobile   =  headerView.findViewById(R.id.mobile);

        // need to set a profile pic -
        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,ProfileActivity.class));
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });

    }



    private void setBottomNavigation() {


        BottomNavigationView navigation = findViewById(R.id.botton_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {


                    case R.id.home_page_asset:

                        break;

            /*        case R.id.navigation_digital_pass:
                        Intent intent2=new Intent(HomePage.this,ProfileActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_notification:
                        Intent intent3=new Intent(HomePage.this,ProfileActivity.class);
                        startActivity(intent3);
                        break;*/

                    case R.id.navigation_profile:
                        Intent intent4=new Intent(HomePage.this,ProfileActivity.class);
                        startActivity(intent4);
                        break;

                    default:
                        return false;

                }
                return true;
            }
        });

    }


/*
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem items) {

        switch (items.getItemId()) {

            case R.id.navigation_trips:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.navigation_profile:
                startActivity(new Intent(this, ProfileActivity.class));

                break;

            case R.id.navigation_wallet:
                startActivity(new Intent(this, ProfileActivity.class));
                break;

                default:
                    return false;

        }
        return true;
    };*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        switch (menuItem.getItemId()) {

            case R.id.navigation_drawer_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                break;

            case R.id.logout:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;

        }
        //close navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {


        switch (view.getId())
        {

            case R.id.buy_bus_pass_ll:

            startActivity(new Intent(HomePage.this,BuyBusPassActivity.class));
            //finish();
            break;

            case R.id.feedback_and_suggestions:

                startActivity(new Intent(HomePage.this,Feedback_and_Suggestions.class));
                //finish();
                break;

            case R.id.history:

                startActivity(new Intent(HomePage.this,PassHistory.class));
                //finish();
                break;
            case R.id.track_application_status_ll:

                startActivity(new Intent(HomePage.this,TrackApplicatinStatus.class));
                //finish();
                break;

            case R.id.renew_pass:

                startActivity(new Intent(HomePage.this, RenewPassNumber.class));
                //finish();
                break;

            case R.id.more_info:

                startActivity(new Intent(HomePage.this, MoreInfoActivity.class));
                //finish();
                break;


        }



    }
}


