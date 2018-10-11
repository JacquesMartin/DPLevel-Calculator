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

public class ClosedTank_Capillary_TwoSeal_System extends BaseActivity implements AdapterView.OnItemSelectedListener {



    private EditText height_Max_editText, height_Min_editText, distancetx_editText, sg_process_editText, sg_fill_editText;
    public double heightMax_value;
    public double heightMin_value;
    public double calculatedURV_value;
    public double calculatedLRV_value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ct_capillary_two_seal);

        if(savedInstanceState != null){
            urv_show_textView = (TextView) findViewById(R.id.urv_textview);
            lrv_show_textView = (TextView) findViewById(R.id.lrv_textview);
            urv_show_textView.setText(savedInstanceState.getString("urv"));
            lrv_show_textView.setText(savedInstanceState.getString("lrv"));
        }


        height_Max_editText = (EditText) findViewById(R.id.height_max_edittext);
        height_Min_editText = (EditText) findViewById(R.id.height_min_edittext);
        distancetx_editText = (EditText) findViewById(R.id.href_edittext);
        sg_process_editText = (EditText) findViewById(R.id.sg_proces_edittext);
        sg_fill_editText = (EditText) findViewById(R.id.sg_fill_edittext);

        spinnerMeasurement = (Spinner) findViewById(R.id.spinner_measurement_units);
        spinnerPressure = (Spinner) findViewById(R.id.spinner_pressure_units);

        spinnerPressure.setOnItemSelectedListener(this);
        spinnerMeasurement.setOnItemSelectedListener(this);

        height_Max_editText.addTextChangedListener(new MyTextWatcher(height_Max_editText));
        height_Min_editText.addTextChangedListener(new MyTextWatcher(height_Min_editText));
        distancetx_editText.addTextChangedListener(new MyTextWatcher(distancetx_editText));
        sg_process_editText.addTextChangedListener(new MyTextWatcher(sg_process_editText));
        sg_fill_editText.addTextChangedListener(new MyTextWatcher(sg_fill_editText));

        spinnerinit();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



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
                    collapsingToolbarLayout.setTitle("CT-Capillary Two Seal");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle("CT-Capillary Two Seal");
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
                alert.showDialog(ClosedTank_Capillary_TwoSeal_System.this,"ClosedTank_Capillary_TwoSeal_System");
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

        final AlertDialog.Builder builder = setDialogofSGmenu();
        Button fabDialog = (Button)findViewById(R.id.dialog_icon_button);
        fabDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        findViewById(R.id.showtable_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyboard(view);
                if (validate()) {
                    calculate();

                    DisplayDialog proceed = new DisplayDialog();
                    proceed.setHeight4mA(heightMin_value);
                    proceed.setHeight20mA(heightMax_value);
                    proceed.setPressure4mA(calculatedLRV_value);
                    proceed.setPressure20mA(calculatedURV_value);
                    proceed.setPressure_unit(selectedPressureUnit);
                    proceed.setMeasurement_unit(selectedMeasurementUnit);
                    proceed.showDialogTable(ClosedTank_Capillary_TwoSeal_System.this);
                }
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
        distancetx_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });
        sg_process_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){hideKeyboard(view);}
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

        heightMax_value = Double.parseDouble(height_Max_editText.getText().toString().trim());
        heightMin_value = Double.parseDouble(height_Min_editText.getText().toString().trim());
        double distanceTx_zeroSuppression_Value = Double.parseDouble(distancetx_editText.getText().toString().trim());
        double spProcess_Value = Double.parseDouble(sg_process_editText.getText().toString().trim());
        double sp_fill_value = Double.parseDouble(sg_fill_editText.getText().toString().trim());

        double raw_urv = (heightMax_value * spProcess_Value)+(heightMin_value * spProcess_Value)-(distanceTx_zeroSuppression_Value *sp_fill_value);
        double raw_lrv = (heightMin_value * spProcess_Value)-(distanceTx_zeroSuppression_Value *sp_fill_value);

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

        if (distancetx_editText.getText().toString().trim().length() == 0) {
            textInputLayout_distanceTx_zeroSupp.setError("Please enter a value");
            textInputLayout_distanceTx_zeroSupp.setErrorEnabled(true);
            requestFocus(distancetx_editText);
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

        boolean valid = true;

        if (!validateHeightMax()) {
            return false;
        }
        if (!validateHeightMin()) {
            return false;
        }
        if (!validateDistanceTxZeroSupp()) {
            return false;
        }
        if (!validateSGProcess()) {
            return false;
        }
        if(!validateSGFill()){
            return false;
        }
        calculate();

        return valid;
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
                case R.id.href_edittext:
                    validateDistanceTxZeroSupp();

                    break;
                case R.id.sg_proces_edittext:
                    validateSGProcess();

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

            startActivity(new Intent(this, ClosedTank_Capillary_TwoSeal_System.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
