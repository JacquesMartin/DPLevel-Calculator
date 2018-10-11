package com.vasoco.jacquesmartinvaso.DPLevelCalculator;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ClosedTank_Capillary_Interface extends BaseActivity implements AdapterView.OnItemSelectedListener{



    private EditText height_Max_editText, height_Min_editText, href_editText;
    private EditText sg_fill_editText;
    public EditText sg_process1_editText;
    public EditText sg_process2_editText;
    private EditText overflow_height_editText;
    public double lmax_value_;
    public double lmin_value_1;
    public double calculatedLRV_value;
    public double calculatedURV_value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ct_capillary_interface);

        if(savedInstanceState != null){
            urv_show_textView = (TextView) findViewById(R.id.urv_textview);
            lrv_show_textView = (TextView) findViewById(R.id.lrv_textview);
            urv_show_textView.setText(savedInstanceState.getString("urv"));
            lrv_show_textView.setText(savedInstanceState.getString("lrv"));
        }

        height_Max_editText = (EditText) findViewById(R.id.height_max_edittext);
        height_Min_editText = (EditText) findViewById(R.id.height_min_edittext);
        href_editText = (EditText) findViewById(R.id.href_edittext);
        overflow_height_editText= (EditText) findViewById(R.id.overflow_height_edittext);
        sg_process1_editText = (EditText) findViewById(R.id.sg_proces1_edittext);
        sg_process2_editText = (EditText) findViewById(R.id.sg_process2_edittext);
        sg_fill_editText = (EditText) findViewById(R.id.sg_fill_edittext);
        spinnerMeasurement = (Spinner) findViewById(R.id.spinner_measurement_units);
        spinnerPressure = (Spinner) findViewById(R.id.spinner_pressure_units);
        spinnerPressure.setOnItemSelectedListener(this);
        spinnerMeasurement.setOnItemSelectedListener(this);
        height_Max_editText.addTextChangedListener(new MyTextWatcher(height_Max_editText));
        height_Min_editText.addTextChangedListener(new MyTextWatcher(height_Min_editText));
        href_editText.addTextChangedListener(new MyTextWatcher(href_editText));
        sg_process1_editText.addTextChangedListener(new MyTextWatcher(sg_process1_editText));
        sg_process2_editText.addTextChangedListener(new MyTextWatcher(sg_process2_editText));
        sg_fill_editText.addTextChangedListener(new MyTextWatcher(sg_fill_editText));
        spinnerinit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

                hideKeyboard(view);
                if (validate()) {
                    calculate();

                    DisplayDialog proceed = new DisplayDialog();
                    proceed.setHeight4mA(lmin_value_1);
                    proceed.setHeight20mA(lmax_value_);
                    proceed.setPressure4mA(calculatedLRV_value);
                    proceed.setPressure20mA(calculatedURV_value);
                    proceed.setPressure_unit(selectedPressureUnit);
                    proceed.setMeasurement_unit(selectedMeasurementUnit);
                    proceed.showDialogTable(ClosedTank_Capillary_Interface.this);
                }
            }
        });

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
                    collapsingToolbarLayout.setTitle("Capillary Interface");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle("Capillary Interface");
                    collapsingToolbarLayout.setExpandedTitleMargin(20,110,110,20);
                    collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedTitleAppearance);
                    isShow = false;
                }
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DisplayDialog alert = new DisplayDialog();
                alert.showDialog(ClosedTank_Capillary_Interface.this,"ClosedTank_Capillary_Interface");
            }
        });

        final AlertDialog.Builder builder = setDialogofSGmenu();

        Button fabDialog = (Button)findViewById(R.id.dialog_icon_button);
        fabDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        sg_fill_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });
        href_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });
        sg_process1_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });
        sg_process2_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
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


    @NonNull
    private AlertDialog.Builder setDialogofSGmenu() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SG Fill Fluids")
                .setItems(R.array.SG_Fill_Fluids, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        switch(which){
                            case 0: sg_fill_editText.setText(R.string.Silicone_200);
                                break;
                            case 1: sg_fill_editText.setText(R.string.Silicone_704);
                                break;
                            case 2: sg_fill_editText.setText(R.string.Silicone_705);
                                break;
                            case 3: sg_fill_editText.setText(R.string.Sytherm_XLT);
                                break;
                            case 4: sg_fill_editText.setText(R.string.Inert_Halocarbon);
                                break;
                            case 5: sg_fill_editText.setText(R.string.Neobee_M_20);
                                break;
                            case 6: sg_fill_editText.setText(R.string.Glycerin_and_water);
                                break;
                            case 7: sg_fill_editText.setText(R.string.Propylen_Glycol_and_water);
                                break;
                        }
                    }
                });
        return builder;
    }

    private void calculate() {

        lmax_value_ = Double.parseDouble(height_Max_editText.getText().toString().trim());
        lmin_value_1 = Double.parseDouble(height_Min_editText.getText().toString().trim());
        double overflow_height_value = Double.parseDouble(overflow_height_editText.getText().toString().trim());
        double Href_value_ = Double.parseDouble(href_editText.getText().toString().trim());
        double sg1_value_ =Double.parseDouble(sg_process1_editText.getText().toString().trim());
        double sg2_value_ =Double.parseDouble(sg_process2_editText.getText().toString().trim());
        double sgfill_value_ = Double.parseDouble(sg_fill_editText.getText().toString().trim());

        double raw_urv = (overflow_height_value)*(sg1_value_)+(lmax_value_ + lmin_value_1)*(sg2_value_)-(Href_value_*sgfill_value_);
        double raw_lrv = (overflow_height_value+lmax_value_ )*(sg1_value_)+(lmin_value_1 *sg2_value_)-(Href_value_*sgfill_value_);

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


        textInputLayout_heightMin = (TextInputLayout) findViewById(R.id.heightmin_textInputLayout);

        if (height_Min_editText.getText().toString().trim().length() == 0) {
            textInputLayout_heightMin.setError("Please enter a value");
            textInputLayout_heightMin.setErrorEnabled(true);
            requestFocus(height_Min_editText);
            return false;
        } else {
            textInputLayout_heightMin.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateDistanceTxZeroSupp() {


        textInputLayout_distanceTx_zeroSupp = (TextInputLayout) findViewById(R.id.distanceTx_zeroSuppression_textInputLayout);

        if (href_editText.getText().toString().trim().length() == 0) {
            textInputLayout_distanceTx_zeroSupp.setError("Please enter a value");
            textInputLayout_distanceTx_zeroSupp.setErrorEnabled(true);
            requestFocus(href_editText);
            return false;
        } else {
            textInputLayout_distanceTx_zeroSupp.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateSGProcess1() {

        TextInputLayout sgrocess1_textinputlayout = (TextInputLayout) findViewById(R.id.sg_process1_textInputLayout);

        if (sg_process1_editText.getText().toString().trim().length() == 0) {
            sgrocess1_textinputlayout.setError("Please enter a value");
            sgrocess1_textinputlayout.setErrorEnabled(true);
            requestFocus(sg_process1_editText);
            return false;
        } else {
            sgrocess1_textinputlayout.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateSGProcess2() {

        TextInputLayout sgrocess2_textinputlayout = (TextInputLayout) findViewById(R.id.sg_process2_textInputLayout);

        if (sg_process2_editText.getText().toString().trim().length() == 0) {
            sgrocess2_textinputlayout.setError("Please enter a value");
            sgrocess2_textinputlayout.setErrorEnabled(true);
            requestFocus(sg_process2_editText);
            return false;
        } else {
            sgrocess2_textinputlayout.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateSGFill() {



        TextInputLayout textInputLayout_sgFill = (TextInputLayout) findViewById(R.id.sg_fill_textInputLayout);

        if (sg_fill_editText.getText().toString().trim().length() == 0) {
            textInputLayout_sgFill.setError("Please enter a value");
            textInputLayout_sgFill.setErrorEnabled(true);
            requestFocus(sg_fill_editText);
            return false;
        } else {
            textInputLayout_sgFill.setErrorEnabled(false);
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
            return false;
        }
        if (!validateDistanceTxZeroSupp()) {
            return false;
        }
        if (!validateSGProcess1()) {
            return false;
        }
        if (!validateSGProcess2()) {
            return false;
        }
        if(!validateSGFill()){
            return false;
        }

        calculate();

        return true;

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    class MyTextWatcher implements TextWatcher {
        private View view;


        MyTextWatcher(View view) {
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
                case R.id.height_min_edittext:
                    validateHeightMin();
                    break;
                case R.id.href_edittext:
                    validateDistanceTxZeroSupp();
                    break;
                case R.id.sg_proces1_edittext:
                    validateSGProcess1();
                    break;
                case R.id.sg_process2_edittext:
                    validateSGProcess2();
                    break;

                case R.id.sg_fill_edittext:
                    validateSGFill();
                    break;
            }
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==R.id.clear){

            startActivity(new Intent(this, ClosedTank_Capillary_Interface.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
