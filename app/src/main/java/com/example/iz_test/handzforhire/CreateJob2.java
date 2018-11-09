package com.example.iz_test.handzforhire;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class CreateJob2 extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener {

    String id,address,city,state,zipcode,name,category,date,start_time,expected_hours,amount,type,description;
    TextView text,pros,cons;
    EditText add,cit,stat,zip;
    CheckBox check1,check2;
    LocationTrack locationTrack;
    Button next;
    String j_address,j_city,j_state,j_zipcode,post_address,job_id,job_expire;
    RelativeLayout address_layout;
    LinearLayout linear,layout;
    ImageView logo;
    double lat,lon;
    static String latitude="0.0";
    static String longitude="0.0";
    String estimated_amount,flexible_status,job_category_color,sub_category,duration;
    String current_location;
    static String get_lat="0.0";
    static String get_lon="0.0";
    String usertype = "employer";
    String edit_job = "no";
    private SimpleGestureFilter detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_job_2);

        Intent i = getIntent();
        id = i.getStringExtra("userId");
        name = i.getStringExtra("job_name");
        category = i.getStringExtra("job_category");
        description = i.getStringExtra("job_decription");
        date = i.getStringExtra("job_date");
        start_time = i.getStringExtra("start_time");
        expected_hours = i.getStringExtra("expected_hours");
        amount = i.getStringExtra("payment_amount");
        type = i.getStringExtra("payment_type");
        estimated_amount = i.getStringExtra("estimated_amount");
        flexible_status = i.getStringExtra("flexible_status");
        job_expire = i.getStringExtra("job_expire");
        job_category_color = i.getStringExtra("job_category_color");
        sub_category = i.getStringExtra("sub_category");
        duration = i.getStringExtra("duration");
        System.out.println("estimated amount "+estimated_amount);
        System.out.println("777777777:" + id+".."+name+".."+category+".."+date+".."+start_time+".."+expected_hours+".."+amount+".."+type);
        System.out.println("777777777:" +".."+job_category_color+".."+sub_category);

        text = (TextView)findViewById(R.id.bt1);
        add = (EditText)findViewById(R.id.address);
        cit = (EditText)findViewById(R.id.city);
        stat = (EditText)findViewById(R.id.state);
        zip = (EditText)findViewById(R.id.zipcode);
        check1 = (CheckBox)findViewById(R.id.checkBox1);
        check2 = (CheckBox)findViewById(R.id.checkBox2);
        next = (Button)findViewById(R.id.next);
        pros = (TextView)findViewById(R.id.pros);
        cons = (TextView)findViewById(R.id.cons);
        address_layout = (RelativeLayout)findViewById(R.id.layout2);
        linear = (LinearLayout)findViewById(R.id.lay);
        layout = (LinearLayout)findViewById(R.id.layout);
        logo = (ImageView)findViewById(R.id.logo);
        ImageView back = (ImageView)findViewById(R.id.back);

        detector = new SimpleGestureFilter(this,this);
        stat.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        text.setText(name);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(CreateJob2.this);
                dialog.setContentView(R.layout.pros_popup);

                // set the custom dialog components - text, image and button
                ImageView close = (ImageView) dialog.findViewById(R.id.close_btn);
                // if button is clicked, close the custom dialog
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        address_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                j_address = add.getText().toString().trim();
                j_city = cit.getText().toString().trim();
                j_state = stat.getText().toString().trim();
                j_zipcode = zip.getText().toString().trim();
                if(j_address.equals("")&&j_city.equals("")&&j_state.equals("")&&j_zipcode.equals(""))
                {
                    add.clearFocus();
                    cit.clearFocus();
                    stat.clearFocus();
                    zip.clearFocus();
                    linear.setVisibility(View.VISIBLE);
                }
                if(!j_address.equals("")&&!j_city.equals("")&&!j_state.equals("")&&!j_zipcode.equals(""))
                {
                    check1.setChecked(false);
                }

            }
        });

        cons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(CreateJob2.this);
                dialog.setContentView(R.layout.cons_popup);

                // set the custom dialog components - text, image and button
                ImageView close = (ImageView) dialog.findViewById(R.id.close_btn);
                // if button is clicked, close the custom dialog
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check1.isChecked())
                {
                    add.setText("");
                    cit.setText("");
                    stat.setText("");
                    zip.setText("");
                    locationTrack = new LocationTrack(CreateJob2.this);
                    if (locationTrack.canGetLocation()) {
                        lon = locationTrack.getLongitude();
                        lat = locationTrack.getLatitude();
                        latitude = String.valueOf(lat);
                        longitude = String.valueOf(lon);
                        System.out.println("kkkkkkkkkkkkkk:latitude::check::"+latitude);
                        System.out.println("kkkkkkkkkkkkkk:longitude:check::"+longitude);
                        //Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(lon) + "\nLatitude:" + Double.toString(lat), Toast.LENGTH_SHORT).show();
                    } else {
                        locationTrack.showSettingsAlert();
                    }
                }

            }
        });

        add.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    check1.setChecked(false);
                } else {
                    linear.setVisibility(View.VISIBLE);
                }
            }
        });
        cit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    check1.setChecked(false);
                } else {
                    linear.setVisibility(View.VISIBLE);
                }
            }
        });
        stat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    check1.setChecked(false);
                } else {
                    linear.setVisibility(View.VISIBLE);
                }
            }
        });
        zip.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    check1.setChecked(false);
                } else {
                    linear.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void validate() {
        j_address = add.getText().toString().trim();
        j_city = cit.getText().toString().trim();
        j_state = stat.getText().toString().trim();
        j_zipcode = zip.getText().toString().trim();

        if(check2.isChecked()) {
            post_address = "yes";
        }
        else {
            post_address = "no";
            String address = j_address + j_city + j_state + j_zipcode;
            System.out.println("ssssssssss:add::"+ address);
            getGeoCoordsFromAddress(this,address);
            System.out.println("kkkkkkkkkkkkkk:getlatitude:"+get_lat);
            System.out.println("kkkkkkkkkkkkkk:getlatitude:"+get_lon);
            latitude = get_lat;
            longitude = get_lon;
            System.out.println("kkkkkkkkkkkkkk:latitude:"+latitude);
            System.out.println("kkkkkkkkkkkkkk:longitude:"+longitude);
        }
        if(check1.isChecked())
        {
            current_location = "yes";
            locationTrack = new LocationTrack(CreateJob2.this);
            if (locationTrack.canGetLocation()) {
                lon = locationTrack.getLongitude();
                lat = locationTrack.getLatitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(lon);
                System.out.println("kkkkkkkkkkkkkk:latitude::check::"+latitude);
                System.out.println("kkkkkkkkkkkkkk:longitude:check::"+longitude);
                // Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(lon) + "\nLatitude:" + Double.toString(lat), Toast.LENGTH_SHORT).show();
            } else {
                locationTrack.showSettingsAlert();
            }

        }
        else
        {
            current_location = "no";
            String address = j_address + j_city + j_state + j_zipcode;
            getGeoCoordsFromAddress(this,address);
            latitude = get_lat;
            longitude = get_lon;
        }
        if(!check1.isChecked()&&(j_address.equals("")||j_state.equals("")||j_city.equals("")||j_zipcode.equals("")))
        {
            final Dialog dialog = new Dialog(CreateJob2.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must Fill In either\"Address or Current location option\" Box");
            Button dialogButton = (Button) dialog.findViewById(R.id.ok);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
            Window window = dialog.getWindow();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        else
        {
            if(latitude.equals("0.0")||longitude.equals("0.0"))
            {
                final Dialog dialog = new Dialog(CreateJob2.this);
                dialog.setContentView(R.layout.custom_dialog);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Entered Address Could Not Be Located");
                Button dialogButton = (Button) dialog.findViewById(R.id.ok);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
            else
            {
                //registerUser();
                Intent i = new Intent(CreateJob2.this,SummaryMultiply.class);
                i.putExtra("userId", id);
                i.putExtra("job_name",name);
                i.putExtra("job_category", category);
                i.putExtra("job_category_color", job_category_color);
                i.putExtra("sub_category", sub_category);
                i.putExtra("job_decription", description);
                i.putExtra("job_date", date);
                i.putExtra("start_time", start_time);
                i.putExtra("expected_hours",expected_hours);
                i.putExtra("payment_amount", amount);
                i.putExtra("payment_type", type);
                i.putExtra("current_location", current_location);
                i.putExtra("post_address", post_address);
                i.putExtra("latitude",latitude);
                i.putExtra("longitude", longitude);
                i.putExtra("estimated_amount",estimated_amount);
                i.putExtra("flexible_status", flexible_status);
                i.putExtra("job_expire", job_expire);
                i.putExtra("edit_job", edit_job);
                i.putExtra("duration", duration);
                startActivity(i);
            }
        }
    }

    public static LatLng getGeoCoordsFromAddress(Context c, String address)
    {
        Geocoder geocoder = new Geocoder(c);
        List<Address> addresses;
        try
        {
            addresses = geocoder.getFromLocationName(address, 1);
            if(addresses.size() > 0)
            {
                double latitud = addresses.get(0).getLatitude();
                double longitud = addresses.get(0).getLongitude();
                get_lat = String.valueOf(latitud);
                get_lon = String.valueOf(longitud);
                System.out.println("kkkkkkkkkkkkkk:latitude:"+get_lat);
                System.out.println("kkkkkkkkkkkkkk:latitude:"+get_lon);
                return new LatLng(latitud, longitud);
            }
            else
            {
                return null;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void onSwipe(int direction) {
        String str = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
                Intent j = new Intent(getApplicationContext(), SwitchingSide.class);
                startActivity(j);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                finish();
                break;
            case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
                Intent i = new Intent(getApplicationContext(), ProfilePage.class);
                i.putExtra("userId", Profilevalues.user_id);
                i.putExtra("address", Profilevalues.address);
                i.putExtra("city", Profilevalues.city);
                i.putExtra("state", Profilevalues.state);
                i.putExtra("zipcode", Profilevalues.zipcode);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();

                break;
            case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                break;

        }
        //  Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoubleTap() {

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event){

        this.detector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }
}
