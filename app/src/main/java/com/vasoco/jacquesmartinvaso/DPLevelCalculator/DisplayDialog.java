package com.vasoco.jacquesmartinvaso.DPLevelCalculator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class DisplayDialog extends BaseActivity{


    private String pressure_unit;
    private String measurement_unit;
    private Button dialogButton;
    private Dialog dialog;
    private double height4mA;
    private double height20mA;
    private double pressure4mA;
    private double pressure20mA;
    public TextView height4ma;
    public TextView height4matv;


    public String getPressure_unit() {
        return pressure_unit;
    }

    public void setPressure_unit(String pressure_unit) {
        this.pressure_unit = pressure_unit;
    }

    public String getMeasurement_unit() {
        return measurement_unit;
    }

    public void setMeasurement_unit(String measurement_unit) {
        this.measurement_unit = measurement_unit;
    }


    public double getHeight4mA() {
        return height4mA;
    }

    public void setHeight4mA(double height4mA) {
        this.height4mA = height4mA;
    }

    public double getHeight20mA() {
        return height20mA;
    }

    public void setHeight20mA(double height20mA) {
        this.height20mA = height20mA;
    }

    public double getPressure4mA() {
        return pressure4mA;
    }

    public void setPressure4mA(double pressure4mA) {
        this.pressure4mA = pressure4mA;
    }

    public double getPressure20mA() {
        return pressure20mA;
    }

    public void setPressure20mA(double pressure20mA) {
        this.pressure20mA = pressure20mA;
    }

    void showDialog(Activity activity,String Key) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        switch (Key){
            case "Impulse_Wet_Leg":
                dialog.setContentView(R.layout.dialog_ot_impulse_wet_leg_description);
                break;
            case "OpenTank_Capillary_Zero_Elevation":
                dialog.setContentView(R.layout.dialog_ot_capillary_zero_elevation);
                break;
            case "OpenTank_Capillary_Zero_Suppression":
                dialog.setContentView(R.layout.dialog_ot_capillary_sero_suppression_description);
                break;
            case "OpenTank_Bubbler_System":
                dialog.setContentView(R.layout.dialog_ot_bubbler_system);
                break;
            case "ClosedTank_Impulse_WetLeg":
                dialog.setContentView(R.layout.dialog_ct_impulse_wet_leg);
                break;
            case "ClosedTank_Impulse_DryLeg":
                dialog.setContentView(R.layout.dialog_ct_impulse_dry_leg);
                break;
            case "ClosedTank_Capillary_TwoSeal_System":
                dialog.setContentView(R.layout.dialog_ct_capillary_two_seal);
                break;
            case "ClosedTank_Capillary_Interface":
                dialog.setContentView(R.layout.dialog_ct_interface);
                break;

        }


        Button dialogButtonclose = dialog.findViewById(R.id.dialog_button);
        dialogButtonclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
    @SuppressLint("SetTextI18n")
    void showDialogTable(Activity activity){

        dialog = new Dialog(activity,android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.tablelayout);
        dialog.setCancelable(true);

        TextView height8ma = dialog.findViewById(R.id.dialog_height_8ma);
        TextView height12ma = dialog.findViewById(R.id.dialog_height_12ma);
        TextView height16ma = dialog.findViewById(R.id.dialog_height_16ma);
        TextView height20ma = dialog.findViewById(R.id.dialog_height_20ma);
        TextView pressure4ma = dialog.findViewById(R.id.dialog_pressure_4ma);
        TextView pressure8ma = dialog.findViewById(R.id.dialog_pressure_8ma);
        TextView pressure12ma = dialog.findViewById(R.id.dialog_pressure_12ma);
        TextView pressure16ma = dialog.findViewById(R.id.dialog_pressure_16ma);
        TextView pressure20ma = dialog.findViewById(R.id.dialog_pressure_20ma);


        height8ma.setText(String.valueOf(Math.round((((getHeight20mA())*.25)+getHeight4mA())*1000d)/1000d)+" "+measurement_unit);
        height12ma.setText(String.valueOf(Math.round((((getHeight20mA())*.50)+getHeight4mA())*1000d)/1000d)+" "+measurement_unit);
        height16ma.setText(String.valueOf(Math.round((((getHeight20mA())*.75)+getHeight4mA())*1000d)/1000d)+" "+measurement_unit);
        height20ma.setText(String.valueOf(Math.round((getHeight20mA()+getHeight4mA())*1000d)/1000d)+" "+measurement_unit);
        height4matv = dialog.findViewById(R.id.dialog_height_4ma);
        height4matv.setText(String.valueOf(Math.round((getHeight4mA())*1000d)/1000d)+" "+measurement_unit);

        pressure4ma.setText(String.valueOf(Math.round(getPressure4mA()*1000d)/1000d+" "+pressure_unit));
        pressure8ma.setText(String.valueOf(Math.round((((pressure20mA-pressure4mA)*.25)+pressure4mA)*1000d)/1000d)+" "+pressure_unit);
        pressure12ma.setText(String.valueOf(Math.round((((pressure20mA-pressure4mA)*.50)+pressure4mA)*1000d)/1000d+" "+pressure_unit));
        pressure16ma.setText(String.valueOf(Math.round((((pressure20mA-pressure4mA)*.75)+pressure4mA)*1000d)/1000d+" "+pressure_unit));
        pressure20ma.setText(String.valueOf(Math.round((pressure20mA)*1000d)/1000d+" "+pressure_unit));

        dialogButton = dialog.findViewById(R.id.table_button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }
}
