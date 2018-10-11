package com.vasoco.jacquesmartinvaso.DPLevelCalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Impulse_Wet_Leg extends BaseActivity implements OnItemSelectedListener {

    private EditText height_Max_editText, height_Min_editText, distancetx_zerosupp_editText, sg_process_editText;
    public double heightMax_value;
    public double heightMin_value;


    public double calculatedURV_value;
    public double calculatedLRV_value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ot_impulse__wet__leg);

        if(savedInstanceState != null){
            urv_show_textView = (TextView) findViewById(R.id.urv_textview);
            lrv_show_textView = (TextView) findViewById(R.id.lrv_textview);
            urv_show_textView.setText(savedInstanceState.getString("urv"));
            lrv_show_textView.setText(savedInstanceState.getString("lrv"));
        }

        spinnerMeasurement = (Spinner) findViewById(R.id.spinner_measurement_units);
        spinnerPressure = (Spinner) findViewById(R.id.spinner_pressure_units);

        spinnerPressure.setOnItemSelectedListener(Impulse_Wet_Leg.this);
        spinnerMeasurement.setOnItemSelectedListener(Impulse_Wet_Leg.this);

        height_Max_editText = (EditText) findViewById(R.id.height_max_edittext);
        height_Min_editText = (EditText) findViewById(R.id.height_min_edittext);
        distancetx_zerosupp_editText = (EditText) findViewById(R.id.href_edittext);
        sg_process_editText = (EditText) findViewById(R.id.sg_proces_edittext);

        height_Max_editText.addTextChangedListener(new MyTextWatcher(height_Max_editText));
        height_Min_editText.addTextChangedListener(new MyTextWatcher(height_Min_editText));
        distancetx_zerosupp_editText.addTextChangedListener(new MyTextWatcher(distancetx_zerosupp_editText));
        sg_process_editText.addTextChangedListener(new MyTextWatcher(sg_process_editText));

        distancetx_zerosupp_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {hideKeyboard(view);}
            }
        });
        sg_process_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){hideKeyboard(view);}
            }
        });
        spinnerinit();
        collapsingToolbar();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayDialog alert = new DisplayDialog();
                alert.showDialog(Impulse_Wet_Leg.this,"Impulse_Wet_Leg");
            }
        });

        findViewById(R.id.fab_equals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
                if (validate()) {
                    calculate();

                }
            }
        });
        findViewById(R.id.showtable_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    calculate();
                    hideKeyboard(view);
                    DisplayDialog proceed = new DisplayDialog();
                    proceed.setHeight4mA(heightMin_value);
                    proceed.setHeight20mA(heightMax_value);
                    proceed.setPressure4mA(calculatedLRV_value);
                    proceed.setPressure20mA(calculatedURV_value);
                    proceed.setPressure_unit(selectedPressureUnit);
                    proceed.setMeasurement_unit(selectedMeasurementUnit);
                    proceed.showDialogTable(Impulse_Wet_Leg.this);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        urv_show_textView = (TextView) findViewById(R.id.urv_textview);
        lrv_show_textView = (TextView) findViewById(R.id.lrv_textview);
        savedInstanceState.putString("urv",urv_show_textView.getText().toString());
        savedInstanceState.putString("lrv",lrv_show_textView.getText().toString());

    }

    private void collapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbarlayout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("OT-Impulse Wet Leg");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle("OT-Impulse Wet Leg");
                    collapsingToolbarLayout.setExpandedTitleMargin(20,110,110,20);
                    collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedTitleAppearance);
                    isShow = false;

            }
            }
        });
    }
    private void calculate() {

        heightMax_value = Double.parseDouble(height_Max_editText.getText().toString().trim());
        heightMin_value = Double.parseDouble(height_Min_editText.getText().toString().trim());
        double distanceTx_zeroSuppression_Value = Double.parseDouble(distancetx_zerosupp_editText.getText().toString().trim());
        double spProcess_Value = Double.parseDouble(sg_process_editText.getText().toString().trim());

        double raw_urv = (heightMax_value * spProcess_Value) + (heightMin_value * spProcess_Value) + (distanceTx_zeroSuppression_Value * spProcess_Value);
        double raw_lrv = (heightMin_value * spProcess_Value)+(distanceTx_zeroSuppression_Value * spProcess_Value);

        switch (selectedMeasurementUnit) {

            case "in":

                switch (selectedPressureUnit) {
                    case "bar":
                        calculatedLRV_value = Math.round((raw_lrv * 0.00249082) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.00249082) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "bar");
                        break;
                    case "mmH20":
                        calculatedLRV_value = Math.round((raw_lrv * 25.3999998) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 25.3999998) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "mmH20");
                        break;
                    case "psi":
                        calculatedLRV_value = Math.round((raw_lrv * 0.036126) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.036126) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "psi");
                        break;
                    case "kPa":
                        calculatedLRV_value = Math.round((raw_lrv * 0.249089) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.249089) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "kPa");
                        break;
                    case "MPa":
                        calculatedLRV_value = Math.round((raw_lrv * 0.000249089) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.000249089) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "MPa");
                        break;

                    case "mbar":

                        calculatedLRV_value = Math.round((raw_lrv * 2.4908) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 2.4908) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "mbar");
                        break;
                    case "inH20":
                        calculatedLRV_value = Math.round(raw_lrv * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round(raw_urv * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "inH20");
                        break;

                    case "kg/cm2":
                        calculatedLRV_value = Math.round((raw_lrv * 0.00254) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.00254) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "kg/cm2");

                        break;
                }
                break;

            case "cm":

                switch (selectedPressureUnit) {
                    case "bar":
                        calculatedLRV_value = Math.round((raw_lrv * 0.000980665) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.000980665) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "bar");
                        break;
                    case "mmH20":
                        calculatedLRV_value = Math.round((raw_lrv * 10) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 10) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "mmH20");
                        break;
                    case "psi":
                        calculatedLRV_value = Math.round((raw_lrv * 0.0142233) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.0142233) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "psi");
                        break;
                    case "kPa":
                        calculatedLRV_value = Math.round((raw_lrv * 0.0980665) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.0980665) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "kPa");
                        break;
                    case "MPa":
                        calculatedLRV_value = Math.round((raw_lrv * 0.0000980665) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.0000980665) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "MPa");
                        break;

                    case "mbar":

                        calculatedLRV_value = Math.round((raw_lrv * 0.980665) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.980665) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "mbar");
                        break;
                    case "inH20":
                        calculatedLRV_value = Math.round((raw_lrv *0.393701) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv *0.393701) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "inH20");
                        break;

                    case "kg/cm2":
                        calculatedLRV_value = Math.round((raw_lrv * 0.001) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.001) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "kg/cm2");

                        break;
                }
                break;

            case "mm":

                switch (selectedPressureUnit) {
                    case "bar":
                        calculatedLRV_value = Math.round((raw_lrv * 0.0000980665) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.0000980665) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "bar");
                        break;
                    case "mmH20":
                        calculatedLRV_value = Math.round((raw_lrv * 1) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 1) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "mmH20");
                        break;
                    case "psi":
                        calculatedLRV_value = Math.round((raw_lrv * 0.00142233) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.00142233) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "psi");
                        break;
                    case "kPa":
                        calculatedLRV_value = Math.round((raw_lrv * 0.00980665) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.00980665) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "kPa");
                        break;
                    case "MPa":
                        calculatedLRV_value = Math.round((raw_lrv * 0.00000980665) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.00000980665) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "MPa");
                        break;

                    case "mbar":

                        calculatedLRV_value = Math.round((raw_lrv * 0.0980665) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.0980665) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "mbar");
                        break;
                    case "inH20":
                        calculatedLRV_value = Math.round((raw_lrv *0.0393701) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv *0.0393701) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "inH20");
                        break;

                    case "kg/cm2":
                        calculatedLRV_value = Math.round((raw_lrv * 0.0001) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.0001) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "kg/cm2");

                        break;
                }
                break;

            case "ft":

                switch (selectedPressureUnit) {
                    case "bar":
                        calculatedLRV_value = Math.round((raw_lrv * 0.0298906692) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.0298906692) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "bar");
                        break;
                    case "mmH20":
                        calculatedLRV_value = Math.round((raw_lrv * 304.8) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 304.8) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "mmH20");
                        break;
                    case "psi":
                        calculatedLRV_value = Math.round((raw_lrv * 0.433527504) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.433527504) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "psi");
                        break;
                    case "kPa":
                        calculatedLRV_value = Math.round((raw_lrv * 2.98906692) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 2.98906692) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "kPa");
                        break;
                    case "MPa":
                        calculatedLRV_value = Math.round((raw_lrv * 0.00298906692) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.00298906692) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "MPa");
                        break;

                    case "mbar":

                        calculatedLRV_value = Math.round((raw_lrv * 29.8906692) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 29.8906692) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "mbar");
                        break;
                    case "inH20":
                        calculatedLRV_value = Math.round((raw_lrv *12) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv *12) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "inH20");
                        break;

                    case "kg/cm2":
                        calculatedLRV_value = Math.round((raw_lrv * 0.03048) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.03048) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "kg/cm2");

                        break;
                }
                break;

            case "m":

                switch (selectedPressureUnit) {
                    case "bar":
                        calculatedLRV_value = Math.round((raw_lrv * 0.0980665) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.0980665) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "bar");
                        break;
                    case "mmH20":
                        calculatedLRV_value = Math.round((raw_lrv * 1000) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 1000) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "mmH20");
                        break;
                    case "psi":
                        calculatedLRV_value = Math.round((raw_lrv * 1.42233433) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 1.42233433) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "psi");
                        break;
                    case "kPa":
                        calculatedLRV_value = Math.round((raw_lrv * 9.80665) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 9.80665) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "kPa");
                        break;
                    case "MPa":
                        calculatedLRV_value = Math.round((raw_lrv * 0.00980665) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.00980665) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "MPa");
                        break;

                    case "mbar":

                        calculatedLRV_value = Math.round((raw_lrv * 98.0665) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 98.0665) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "mbar");
                        break;
                    case "inH20":
                        calculatedLRV_value = Math.round((raw_lrv *39.3700787) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv *39.3700787) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "inH20");
                        break;

                    case "kg/cm2":
                        calculatedLRV_value = Math.round((raw_lrv * 0.1) * 1000.0) / 1000.0;
                        calculatedURV_value = Math.round((raw_urv * 0.1) * 1000.0) / 1000.0;
                        printData(calculatedURV_value, calculatedLRV_value, "kg/cm2");

                        break;
                }
                break;
        }
    }


    private boolean validateHeightMax() {
        textInputlayout_heightMax = (TextInputLayout) findViewById(R.id.heightmax_textInputLayout);

        if (height_Max_editText.getText().toString().trim().length() == 0) {
            textInputlayout_heightMax.setError("Please enter a value");
            textInputlayout_heightMax.setErrorEnabled(true);
            requestFocus(height_Max_editText);
            return false;
        } else {
            textInputlayout_heightMax.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateHeightMin() {


        if (height_Min_editText.getText().toString().trim().length() == 0) {
            height_Min_editText.setText("0");
        }
        return true;
    }
    private boolean validateDistanceTxZeroSupp() {

        textInputLayout_distanceTx_zeroSupp = (TextInputLayout) findViewById(R.id.distanceTx_zeroSuppression_textInputLayout);

        if (distancetx_zerosupp_editText.getText().toString().trim().length() == 0) {
            textInputLayout_distanceTx_zeroSupp.setError("Please enter a value");
            textInputLayout_distanceTx_zeroSupp.setErrorEnabled(true);
            requestFocus(distancetx_zerosupp_editText);
            return false;
        } else {
            textInputLayout_distanceTx_zeroSupp.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateSGProcess() {

        textInputLayout_sgProces = (TextInputLayout) findViewById(R.id.ssg_process_textInputLayout);

        if (sg_process_editText.getText().toString().trim().length() == 0) {
            textInputLayout_sgProces.setError("Please enter a value");
            textInputLayout_sgProces.setErrorEnabled(true);
            requestFocus(sg_process_editText);
            return false;
        } else {
            textInputLayout_sgProces.setErrorEnabled(false);
        }
        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validate() {


        if (!validateHeightMax()) {
            return false;
        }
        if (!validateHeightMin()) {
            return false ;
        }
        if (!validateDistanceTxZeroSupp()) {
            return false;
        }
        if (!validateSGProcess()) {
            return false;
        }
        calculate();
        return true;
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void afterTextChanged(Editable editable) {

                switch (view.getId()) {
                    case R.id.height_max_edittext:
                        validateHeightMax();
                        break;
                    case R.id.href_edittext:
                        validateDistanceTxZeroSupp();
                        break;
                    case R.id.sg_proces_edittext:
                        validateSGProcess();
                }

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==R.id.clear){

            startActivity(new Intent(this, Impulse_Wet_Leg.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}

