package com.vasoco.jacquesmartinvaso.DPLevelCalculator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemSelectedListener{

    public String selectedPressureUnit;
    public String selectedMeasurementUnit;

    public Spinner spinnerMeasurement;
    public Spinner spinnerPressure;
    public TextInputLayout textInputlayout_heightMax;
    public TextInputLayout textInputLayout_heightMin;
    public TextInputLayout textInputLayout_distanceTx_zeroSupp;
    public TextInputLayout textInputLayout_sgProces;

    public TextView urv_show_textView;
    public TextView lrv_show_textView;

    public EditText sg_fill_edittext;


    public EditText height_min_editText;
    public EditText height_max_editText;




    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


        spinnerMeasurement = (Spinner) findViewById(R.id.spinner_measurement_units);
        spinnerPressure = (Spinner) findViewById(R.id.spinner_pressure_units);

        height_max_editText = (EditText) findViewById(R.id.height_max_edittext);
        height_min_editText = (EditText) findViewById(R.id.height_min_edittext);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                BaseActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        height_max_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                        hideKeyboard(view);
                                    }
            }
        });
        height_min_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });



    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinner_measurement_units:
                selectedMeasurementUnit = (String) spinnerMeasurement.getSelectedItem();
                break;
            case R.id.spinner_pressure_units:
                selectedPressureUnit = (String) spinnerPressure.getSelectedItem();
                break;
        }
    }
    //on nothing selected on the spinner
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_impulse__wet__leg, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.action_settings:

                String mailId = "calevelvasoco@gmail.com";
                Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri.fromParts("mailto", mailId, null));

                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mailId});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Contact Us");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Sent Message");
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, "Send Mail..."));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.opentTankimpulse_wetleg_single) {

            startActivity(new Intent(BaseActivity.this, Impulse_Wet_Leg.class));
            finish();

        } else if (id == R.id.opentTankCapp_Single_seal_below_tap) {
            startActivity(new Intent(BaseActivity.this,OpenTank_Capillary_Zero_Suppression.class));
            finish();

        } else if (id == R.id.openTankCapp_singleSeal_above_tap) {
            startActivity(new Intent(BaseActivity.this,OpenTank_Capillary_Zero_Elevation.class));
            finish();

        } else if (id == R.id.closedTankCapp_two_seal_system) {
            startActivity(new Intent(BaseActivity.this,ClosedTank_Capillary_TwoSeal_System.class));
            finish();

        } else if (id == R.id.closedTankImpulse_dry_leg) {
            startActivity(new Intent(BaseActivity.this,ClosedTank_Impulse_DryLeg.class));
            finish();

        } else if (id == R.id.closedTankImpulse_wet_leg) {
            startActivity(new Intent(BaseActivity.this,ClosedTank_Impulse_WetLeg.class));
            finish();

        } else if (id == R.id.openTank_bubbler_system){
            startActivity(new Intent(BaseActivity.this,OpenTank_Bubbler_System.class));
            finish();
        } else if (id == R.id.closedTankcapillary_interface){
            startActivity(new Intent(BaseActivity.this,ClosedTank_Capillary_Interface.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void spinnerinit() {

        spinnerMeasurement = (Spinner) findViewById(R.id.spinner_measurement_units);
        ArrayAdapter<CharSequence> adapterMeasurement = ArrayAdapter.createFromResource(this, R.array.measurement_units, android.R.layout.simple_spinner_item);
        adapterMeasurement.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerMeasurement.setAdapter(adapterMeasurement);

        spinnerPressure = (Spinner) findViewById(R.id.spinner_pressure_units);
        ArrayAdapter<CharSequence> adapterPressure = ArrayAdapter.createFromResource(this, R.array.pressure_units, android.R.layout.simple_spinner_item);
        adapterMeasurement.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerPressure.setAdapter(adapterPressure);
    }

    public void printData(Double URV, Double LRV, String unit) {



        urv_show_textView = (TextView) findViewById(R.id.urv_textview);
        lrv_show_textView = (TextView) findViewById(R.id.lrv_textview);

        urv_show_textView.setText("  "+String.valueOf(URV) + "  " + unit);
        lrv_show_textView.setText("  "+String.valueOf(LRV) + "  " + unit);

    }


}
