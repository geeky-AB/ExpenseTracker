package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    //Drawer layout
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FrameLayout frameLayout;
    ExpenseFragment expenseFragment;
    IncomeFragment incomeFragment;
    HomeFragment homeFragment;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ImageView navigationDrawer;
    Toolbar toolbar;
    TextView monthsRep;
    Button barChartRep;
    Button pieChartRep;
    String month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        frameLayout = findViewById(R.id.frameLyt);
//        toolbar = findViewById(R.id.toolbarTL);

        navigationDrawer = findViewById(R.id.navigationMenuImg);
        incomeFragment = new IncomeFragment();
        expenseFragment = new ExpenseFragment();
        homeFragment = new HomeFragment();
        applyFragment(homeFragment);
        navigationDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationDrawerMenu();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

    }

    private void navigationDrawerMenu() {
        navigationView.bringToFront();
        Log.d("navigate", "navigationDrawerMenu: khul gya");
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);


    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START);
        else{

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do You Want To Exit?");
            builder.setCancelable(false);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HomeActivity.super.onBackPressed();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("EXIT", "exit");
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
//            super.onBackPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:{
                applyFragment(homeFragment);
                break;
            }
            case R.id.nav_expenses:{
                applyFragment(expenseFragment);
                break;
            }
            case R.id.nav_income:{
                applyFragment(incomeFragment);
                break;
            }
            case R.id.nav_reports:{
                openDialogReports();
                break;
            }
            case R.id.nav_logout:{
                signoutUser();
                break;
            }
            case R.id.nav_dev:{
                AboutDeveloper();
                break;
            }
            case R.id.jan:
                monthsRep.setText("JANUARY");
                break;
            case R.id.feb:
                monthsRep.setText("FEBRUARY");
                break;
            case R.id.mar:
                monthsRep.setText("MARCH");
                break;
            case R.id.apr:
                monthsRep.setText("APRIL");
                break;
            case R.id.may:
                monthsRep.setText("MAY");
                break;
            case R.id.jun:
                monthsRep.setText("JUNE");
                break;
            case R.id.jul:
                monthsRep.setText("JULY");
                break;
            case R.id.aug:
                monthsRep.setText("AUGUST");
                break;
            case R.id.sep:
                monthsRep.setText("SEPTEMBER");
                break;
            case R.id.oct:
                monthsRep.setText("OCTOBER");
                break;
            case R.id.nov:
                monthsRep.setText("NOVEMBER");
                break;
            case R.id.dec:
                monthsRep.setText("DECEMBER");
                break;
            default: break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void AboutDeveloper() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View mView = inflater.inflate(R.layout.about_developer_dailog,null);
        builder.setView(mView);
        FloatingActionButton igProf = mView.findViewById(R.id.igProfile);
        FloatingActionButton linkedInProf = mView.findViewById(R.id.linkedInProfile);
        FloatingActionButton gitHubProf = mView.findViewById(R.id.gitHubProfile);
        TextView aboutDev = mView.findViewById(R.id.aboutDevTxt);
        aboutDev.setText("Hello guys! Myself Abhijeet. I am an android development enthusiast currently pursuing B.Tech. from JCBUST, YMCA, Faridabad. " +
                "I always try to identify problems and find optimal solutions in more than one way. I would like to effectively put my skills into use " +
                "so that I can make a change into the lives of people. ");
        AlertDialog dialog = builder.create();
        dialog.show();
        igProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://instagram.com/abhi_rana_1602?igshid=YmMyMTA2M2Y=");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://instagram.com/abhi_rana_1602?igshid=YmMyMTA2M2Y=")));
                }
            }
        });

        linkedInProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.linkedin.com/in/abhijeet-rana-1a4407200");
                Intent linkLinkedIn = new Intent(Intent.ACTION_VIEW,uri);
                linkLinkedIn.setPackage("com.linkedin.android");
                try{
                    startActivity(linkLinkedIn);
                } catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.linkedin.com/in/abhijeet-rana-1a4407200")));
                }
            }
        });

        gitHubProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/geeky-AB");
                Intent linkGithub = new Intent(Intent.ACTION_VIEW,uri);
                linkGithub.setPackage("com.github.android");
                try{
                    startActivity(linkGithub);
                } catch (ActivityNotFoundException e){
                    startActivity( new Intent(Intent.ACTION_VIEW,Uri.parse("https://github.com/geeky-AB")));
                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openDialogReports() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.reports_dialog,null);
        builder.setView(view);

        monthsRep = view.findViewById(R.id.monthSelect);
        barChartRep = view.findViewById(R.id.barChart);
        pieChartRep = view.findViewById(R.id.pieChart);
        monthsRep.setText(LocalDate.now().getMonth().toString());
        monthsRep.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ShowPopUp(v);
                return true;
            }
        });

        AlertDialog dialog = builder.create();

        barChartRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month = monthsRep.getText().toString();
                Intent intent = new Intent(HomeActivity.this,ReportsActivity.class);
                intent.putExtra("MONTH",month);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        pieChartRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                month = monthsRep.getText().toString();
                Intent intent = new Intent(HomeActivity.this, PieChartActivity.class);
                intent.putExtra("MONTH",month);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ShowPopUp(View v){
        PopupMenu popupMenu = new PopupMenu(this,v);
        popupMenu.setOnMenuItemClickListener(this::onNavigationItemSelected);
        popupMenu.inflate(R.menu.months_item);
        popupMenu.show();
    }

    private void signoutUser() {
        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        mAuth.signOut();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void applyFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLyt,fragment);
        fragmentTransaction.commit();
    }
}