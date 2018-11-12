package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bigkoo.pickerview.MyOptionsPickerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RehireJob extends Activity implements View.OnClickListener,SimpleGestureFilter.SimpleGestureListener{

    Spinner list;
    LinearLayout layout,category_layout;
    String id, address, zipcode, state, city, name,job_category,description,date,start_time,end_time,amount,st_time,en_time, type;
    private static final String GET_JOB = Constant.SERVER_URL+"job_detail_view";
    Button next;
    EditText job_name, job_description,payamount;
    static TextView date_text;
    TextView start_time_text;
    TextView end_time_text;
    TextView job_amount,symbol;
    TextView pay_text;
    TextView amount_text;
    public static TextView textview;
    static String category="0",categoryId="0";
    private int mHour, mMinute;
    ImageView logo,arrow,arrow1;
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String JOB_ID = "job_id";
    String value = "HandzForHire@~";
    String job_id,jobId,paytext,pay_amount,flexible_status,job_estimated,hourr,latitude,longitude,employeeId;
    ProgressDialog progress_dialog;
    RelativeLayout pay_lay,payment_layout,date_layout,time_layout,estimate_layout,duration_layout;
    CheckBox checkBox;
    Activity activity;
    MyOptionsPickerView threePicker;
    public static String date_format;
    TextView category_name,username_text,hour;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    String header,sub_category,job_category_color,job_expire,expected_hours,post_address;
    Dialog dialog;
    private SimpleGestureFilter detector;
    ImageView main_category_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rehire_job);

        dialog = new Dialog(RehireJob.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        layout = (LinearLayout)findViewById(R.id.relay);
        category_layout = (LinearLayout)findViewById(R.id.linear);
        duration_layout = (RelativeLayout)findViewById(R.id.linear3);
        arrow1 = (ImageView) findViewById(R.id.arrow1);
        category_name = (TextView)findViewById(R.id.cat_name);
        next = (Button) findViewById(R.id.next);
        job_name = (EditText) findViewById(R.id.descrip);
        job_description = (EditText) findViewById(R.id.detail);
        date_text = (TextView) findViewById(R.id.date_text);
        start_time_text = (TextView) findViewById(R.id.start_time_text);
        end_time_text = (TextView) findViewById(R.id.end_time_text);
        job_amount = (TextView) findViewById(R.id.amount);
        amount_text = (TextView) findViewById(R.id.pay_type);
        pay_text = (TextView) findViewById(R.id.payment_details);
        logo = (ImageView) findViewById(R.id.logo);
        arrow = (ImageView) findViewById(R.id.arrow);
        list = (Spinner)findViewById(R.id.listview);
        pay_lay = (RelativeLayout) findViewById(R.id.linear4);
        payment_layout = (RelativeLayout) findViewById(R.id.relay1);
        date_layout = (RelativeLayout) findViewById(R.id.linear1);
        time_layout = (RelativeLayout) findViewById(R.id.linear2);
        estimate_layout = (RelativeLayout) findViewById(R.id.linear3);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        symbol = (TextView) findViewById(R.id.symbol);
        username_text = (TextView) findViewById(R.id.username_text);
        main_category_image =(ImageView)findViewById(R.id.main_category);
        hour = (TextView) findViewById(R.id.hour);

        Intent i = getIntent();
        id = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");
        jobId = i.getStringExtra("jobId");
        employeeId = i.getStringExtra("employeeId");

        detector = new SimpleGestureFilter(this,this);

        String pattern2 = "hh:mm:ss";
        st_time = new SimpleDateFormat(pattern2).format(new Date());
        en_time = new SimpleDateFormat(pattern2).format(new Date());
        System.out.println("777777777:time::::" + st_time+",,,,"+ en_time);
        activity=this;

        getJobDetails();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        date_layout.setOnClickListener(this);
        time_layout.setOnClickListener(this);

        duration_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threePicker.show();
            }
        });


        //Three Options PickerView
        threePicker = new MyOptionsPickerView(RehireJob.this);
        final ArrayList<Integer> numbers = new ArrayList<Integer>(100);

        for (int j = 0; j <= 100; j++)
        {
            numbers.add(j+1);
            System.out.println(numbers.get(j));
        }

        final ArrayList<String> threeItemsOptions2 = new ArrayList<String>();
        threeItemsOptions2.add("");
        threeItemsOptions2.add("0.25");
        threeItemsOptions2.add("0.50");
        threeItemsOptions2.add("0.75");

        final ArrayList<String> threeItemsOptions3 = new ArrayList<String>();
        threeItemsOptions3.add("Hours");
        threeItemsOptions3.add("Minutes");

        threePicker.setPicker(numbers, threeItemsOptions2, threeItemsOptions3, false);
        //threePicker.setTitle("Picker");
        threePicker.setCyclic(false, false, false);
        threePicker.setSelectOptions(0, 0, 0);
        threePicker.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                String a = String.valueOf(numbers.get(options1));
                String b = String.valueOf(threeItemsOptions2.get(option2));
                if(b.equals(""))
                {
                    String c = a + b;
                    System.out.println("aaaaaaaaaaa::cccc:ifcondition:::"+"..."+c+"..."+a+"...."+b);
                    end_time_text.setText(String.valueOf(c));
                    String option = threeItemsOptions3.get(options3);
                    if(c.equals("1"))
                    {
                        String s = option.substring(0, option.length() - 1);
                        System.out.println("aaaaaaaaaaa::sssss:if:::"+"..."+s);
                        hour.setText(s);
                    }
                    else
                    {
                        System.out.println("aaaaaaaaaaa::sssss:else:option::"+"..."+option);
                        hour.setText(option);
                    }

                }
                else
                {
                    float numa = Float.parseFloat(a);
                    float numb = Float.parseFloat(b);
                    System.out.println("aaaaaaaaaaa::elsecondition:"+numa+"..."+numb+"..."+a+"...."+b);
                    float c = numa + numb;
                    System.out.println("aaaaaaaaaaa:elsecondition:cccc:"+c);
                    end_time_text.setText(String.valueOf(c));
                    String option = threeItemsOptions3.get(options3);
                    hour.setText(option);
                }
            }
        });


        pay_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog=new Dialog(RehireJob.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.payment_details);

                ImageView close = (ImageView) dialog.findViewById(R.id.close_btn);
                ImageView logo = (ImageView) dialog.findViewById(R.id.logo);
                payamount = (EditText) dialog.findViewById(R.id.amount);
                Button update = (Button) dialog.findViewById(R.id.update_btn);
                final TextView text = (TextView) dialog.findViewById(R.id.text);

                payamount.addTextChangedListener(tw);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                logo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pay_amount = payamount.getText().toString().trim();
                        paytext = text.getText().toString().trim();
                        payment_layout.setVisibility(View.VISIBLE);
                        job_amount.setText(pay_amount);
                        amount_text.setText(paytext);
                        pay_text.setVisibility(View.GONE);
                        arrow1.setVisibility(View.GONE);
                        arrow.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                dialog.getWindow().

                        setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                return;

            }
        });

        category_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(RehireJob.this);
                dialog.setContentView(R.layout.category_popup);

                expListView = (ExpandableListView) dialog.findViewById(R.id.lvExp);

                // preparing list data
                prepareListData();

                listAdapter = new ExpandableListAdapter(RehireJob.this, listDataHeader, listDataChild);

                // setting list adapter
                expListView.setAdapter(listAdapter);

                // Listview Group click listener
                expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v,
                                                int groupPosition, long id) {
                        // Toast.makeText(getApplicationContext(),
                        // "Group Clicked " + listDataHeader.get(groupPosition),
                        // Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                // Listview Group expanded listener
                expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        // Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " Expanded", Toast.LENGTH_SHORT).show();
                    }
                });

                // Listview Group collasped listener
                expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                    @Override
                    public void onGroupCollapse(int groupPosition) {
                        //Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " Collapsed", Toast.LENGTH_SHORT).show();

                    }
                });

                // Listview on child click listener
                expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) {
                        // TODO Auto-generated method stub
                        //Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " : " + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                        int pos = groupPosition+1;
                        categoryId = String.valueOf(pos);
                        header =  listDataHeader.get(groupPosition);
                        sub_category =  listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                        System.out.println("ccccccccc:"+header+",,"+sub_category+",,"+pos+"id:"+categoryId);
                       // category_name.setText(header+" - "+sub_category);

                        if(header.equals("CARE GIVING"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.care_giving);
                            category_name.setText(sub_category);
                        }
                        if(header.equals("COACHING"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.coaching);
                            category_name.setText(sub_category);
                        }
                        if(header.equals("HOLIDAYS"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.holidays);
                            category_name.setText(sub_category);
                        }
                        if(header.equals("INSIDE THE HOME"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.inside_home);
                            category_name.setText(sub_category);
                        }
                        if(header.equals("OUTSIDE THE HOME"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.outside_home);
                            category_name.setText(sub_category);
                        }
                        if(header.equals("PERSONAL SERVICES"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.personal_services);
                            category_name.setText(sub_category);
                        }
                        if(header.equals("PETCARE"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.petcare);
                            category_name.setText(sub_category);
                        }
                        if(header.equals("TUTORING"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.tutoring);
                            category_name.setText(sub_category);
                        }


                        dialog.dismiss();
                        return false;
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                return;

            }
        });


    }

    public void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("CARE GIVING");
        listDataHeader.add("COACHING");
        listDataHeader.add("HOLIDAYS");
        listDataHeader.add("INSIDE THE HOME");
        listDataHeader.add("OUTSIDE THE HOME");
        listDataHeader.add("PERSONAL SERVICES");
        listDataHeader.add("PETCARE");
        listDataHeader.add("TUTORING");

        // Adding child data
        List<String> care = new ArrayList<String>();
        care.add("Babysitting");
        care.add("Elder Care");
        care.add("Other");
        care.add("Respite Care");

        List<String> coach = new ArrayList<String>();
        coach.add("Baseball");
        coach.add("Basketball");
        coach.add("Dance");
        coach.add("Field Hockey");
        coach.add("Football");
        coach.add("Gymnastics");
        coach.add("Ice Hockey");
        coach.add("Lacrosse");
        coach.add("Life Coach/Mentor");
        coach.add("Other");
        coach.add("Running");
        coach.add("Soccer");
        coach.add("Surfing");
        coach.add("Swimming");
        coach.add("Tennis");
        coach.add("Track & Field");
        coach.add("Volleyball");
        coach.add("Wrestling");


        List<String> holiday = new ArrayList<String>();
        holiday.add("Decorations (Setup or Cleanup)");
        holiday.add("Gift Wrapping");
        holiday.add("Holiday Baking");
        holiday.add("Holiday Shopping");
        holiday.add("Light Hanging");
        holiday.add("Other");
        holiday.add("Party Help (Setup, Cleanup, Serving)");
        holiday.add("Tree Delivery");
        holiday.add("Tree (Setup or Cleanup)");

        List<String> inside = new ArrayList<String>();
        inside.add("Box Packing / Unpacking");
        inside.add("Cleaning");
        inside.add("Electronics Setup");
        inside.add("Furniture Assembling");
        inside.add("Heavy Lifting / Moving");
        inside.add("House Sitting");
        inside.add("Organizing");
        inside.add("Other");
        inside.add("Painting");
        inside.add("Plant Care");
        inside.add("Smart Home Setup");
        inside.add("Wall Hanging");
        inside.add("Window Washing");

        List<String> outside = new ArrayList<String>();
        outside.add("Car Washing / Detailing");
        outside.add("Cleaning");
        outside.add("Gutters");
        outside.add("Digging / Shoveling");
        outside.add("Garage Organizing");
        outside.add("Heavy Lifting / Moving");
        outside.add("Landscaping");
        outside.add("Lawn Mowing");
        outside.add("Mulching");
        outside.add("Other");
        outside.add("Painting");
        outside.add("Planting");
        outside.add("Pool Cleaning");
        outside.add("Power Washing");
        outside.add("Snow Removal");
        outside.add("Window Washing");
        outside.add("Yard Cleanup");

        List<String> personal = new ArrayList<String>();
        personal.add("Cooking / Baking");
        personal.add("Data Entry");
        personal.add("Drop off / Pick up Driver");
        personal.add("Errands");
        personal.add("Event Server / Bartender");
        personal.add("Exercise Buddy");
        personal.add("Grocery Shopper");
        personal.add("Heavy Lifting / Moving");
        personal.add("Meal Preparation");
        personal.add("Other");
        personal.add("Party Help (Setup, Cleanup, Serving)");
        personal.add("Tax Preparation");
        personal.add("Wait in Line");

        List<String> pet = new ArrayList<String>();
        pet.add("Other");
        pet.add("Pet Bathing");
        pet.add("Pet Sitting");
        pet.add("Pet Walking");

        List<String> tutor = new ArrayList<String>();
        tutor.add("Computer Coding");
        tutor.add("Computer / Software");
        tutor.add("English");
        tutor.add("Foreign Language");
        tutor.add("Google Apps Training");
        tutor.add("Graphic Design (Photoshop/Illustrator)");
        tutor.add("History");
        tutor.add("Language Arts");
        tutor.add("Math");
        tutor.add("Microsoft Office");
        tutor.add("Musical Instrument");
        tutor.add("Other");
        tutor.add("Reading");
        tutor.add("Science");
        tutor.add("Singing / Voice");

        listDataChild.put(listDataHeader.get(0), care); // Header, Child data
        listDataChild.put(listDataHeader.get(1), coach);
        listDataChild.put(listDataHeader.get(2), holiday);
        listDataChild.put(listDataHeader.get(3), inside);
        listDataChild.put(listDataHeader.get(4), outside);
        listDataChild.put(listDataHeader.get(5), personal);
        listDataChild.put(listDataHeader.get(6), pet);
        listDataChild.put(listDataHeader.get(7), tutor);
    }

    @Override
    public void onClick(View v) {

        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

        if (v == date_layout) {

            DialogFragment dialogfragment = new datepickerClass();

            dialogfragment.show(getFragmentManager(), "DatePickerDialog");

        }
        if (v == time_layout) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            int second = 00;
                            int hour = hourOfDay;
                            int minutes = minute;
                            String sec = (second < 10) ? "0" + second : "" + second;
                            String min = (minutes < 10) ? "0" + minutes : "" + minutes;
                            String hour_day = (hour < 10) ? "0" + hour : "" + hour;
                            start_time = hour_day + ":" + min + ":" + sec;
                            System.out.println("77777777:start_time::::::"+start_time);

                            hourr = hour_day;
                            System.out.println("77777777:hourr::::::"+hourr);

                            String timeSet = "";
                            if (hour > 12) {
                                hour -= 12;
                                timeSet = "PM";
                            } else if (hour == 0) {
                                hour += 12;
                                timeSet = "AM";
                            }
                            else if (hour == 12){
                                timeSet = "PM";
                            }else{
                                timeSet = "AM";
                            }

                            String min1 = "";
                            if (minutes < 10)
                            {
                                min1 = "0" + minutes ;
                            } else {
                                min1 = String.valueOf(minutes);
                            }
                            if (minutes > 00) {
                                min1 = "00";
                                hour = hour + 1;
                            }
                            if(hour == 13)
                            {
                                String hour_new = "1";
                                start_time_text.setText(hour_new + ":" + min1 + " " + timeSet);
                            }
                            else
                            {
                                start_time_text.setText(hour + ":" + min1 + " " + timeSet);
                            }

                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    public void validate() {
        name = job_name.getText().toString().trim();
        description = job_description.getText().toString().trim();
        date = date_text.getText().toString().trim();
        start_time = start_time_text.getText().toString();
        end_time = end_time_text.getText().toString();
        amount = job_amount.getText().toString().trim();
        type = amount_text.getText().toString().trim();

        if(header.equals("CARE GIVING"))
        {
            job_category_color = "#FF87FA";
            System.out.println("cccccccccccccc:colorcode:::"+job_category_color);
        }
        if(header.equals("COACHING"))
        {
            job_category_color = "#BED2EA";
            System.out.println("cccccccccccccc:colorcode:::"+job_category_color);

        }
        if(header.equals("HOLIDAYS"))
        {
            job_category_color = "#FF4B13";
            System.out.println("cccccccccccccc:colorcode:::"+job_category_color);
        }
        if(header.equals("INSIDE THE HOME"))
        {
            job_category_color = "#FFFB86";
            System.out.println("cccccccccccccc:colorcode:::"+job_category_color);
        }
        if(header.equals("OUTSIDE THE HOME"))
        {
            job_category_color = "#00D034";
            System.out.println("cccccccccccccc:colorcode:::"+job_category_color);
        }
        if(header.equals("PERSONAL SERVICES"))
        {
            job_category_color = "#FFC834";
            System.out.println("cccccccccccccc:colorcode:::"+job_category_color);
        }
        if(header.equals("PETCARE"))
        {
            job_category_color = "#AA84FA";
            System.out.println("cccccccccccccc:colorcode:::"+job_category_color);
        }
        if(header.equals("TUTORING"))
        {
            job_category_color = "#6BAEFB";
            System.out.println("cccccccccccccc:colorcode:::"+job_category_color);
        }

        if (checkBox.isChecked())
        {
            flexible_status = "yes";
        }
        else
        {
            flexible_status = "no";
        }

        expected_hours = end_time_text.getText().toString();
        System.out.println("eeeeeeeee:expected_hours:::"+expected_hours);
        job_estimated = String.valueOf(Float.valueOf(expected_hours)*Float.valueOf(amount));
        System.out.println("eeeeeeeee:estimated:::"+job_estimated);
        job_expire = date_format + " " + st_time ;
        System.out.println("eeeeeeeee:job_expire:::"+job_expire);
        String hours = hour.getText().toString();
        String duration = expected_hours+" "+hours;
        System.out.println("eeeeeeeee:duration:::"+duration);

        Intent i = new Intent(RehireJob.this, RehireMultiply.class);
        i.putExtra("userId", id);
        i.putExtra("job_name",name);
        i.putExtra("job_category",categoryId);
        i.putExtra("job_id",job_id);
        i.putExtra("job_category_color", job_category_color);
        i.putExtra("sub_category", sub_category);
        i.putExtra("job_decription",description);
        i.putExtra("job_date", date_format);
        i.putExtra("start_time",st_time);
        i.putExtra("expected_hours",expected_hours);
        i.putExtra("payment_type", expected_hours);
        i.putExtra("payment_amount",amount);
        i.putExtra("flexible_status", flexible_status);
        i.putExtra("estimated_amount", job_estimated);
        i.putExtra("job_expire", job_expire);
        i.putExtra("duration", duration);
        i.putExtra("employeeId", employeeId);
        startActivity(i);
    }

    public void getJobDetails()
    {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_JOB,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:new:get:job:" + response);
                        onResponserecieved(response, 2);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(),"Not Connected",Toast.LENGTH_LONG).show();
                        }else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"Authentication Failure while performing the request",Toast.LENGTH_LONG).show();
                        }else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"Network error while performing the request",Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String status = jsonObject.getString("msg");
                                if (!status.equals("")) {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(RehireJob.this);
                                    dialog.setContentView(R.layout.custom_dialog);

                                    // set the custom dialog components - text, image and button
                                    TextView text = (TextView) dialog.findViewById(R.id.text);
                                    text.setText(status);
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
                            } catch (JSONException e) {
                                //Handle a malformed json response
                            } catch (UnsupportedEncodingException error1) {

                            }
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(JOB_ID, jobId);
                params.put(Constant.DEVICE, Constant.ANDROID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int i) {
        System.out.println("response from interface" + jsonobject);

        String status = null;
        String job_data = null;

        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            if (status.equals("success")) {
                job_data = jResult.getString("job_data");
                System.out.println("jjjjjjjjjjjjjjjob:::job_data:::" + job_data);
                JSONObject object = new JSONObject(job_data);
                String get_name = object.getString("job_name");

                job_category = object.getString("job_category");

                String get_description = object.getString("description");

                String get_date = object.getString("job_date");

                String get_start_time = object.getString("start_time");

                String get_amount = object.getString("job_payment_amount");

                String get_type = object.getString("job_payment_type");

                String flexible = object.getString("job_date_time_flexible");

                String sub_cat = object.getString("sub_category");

                String cat_color = object.getString("job_category_color");

                String job_estimated_payment = object.getString("job_estimated_payment");

                String job_expire_date_time = object.getString("job_expire_date_time");

                job_id = object.getString("job_id");

                String username = object.getString("username");

                String profilename = object.getString("profile_name");

                String firstname = object.getString("firstname");

                if(profilename.equals(""))
                {
                    username_text.setText(username);
                }
                if(profilename.equals("")&&username.equals(""))
                {
                    username_text.setText(firstname);
                }
                else
                {
                    username_text.setText(profilename);
                }

                name = get_name;
                categoryId = job_category;
                job_category_color = cat_color;
                sub_category = sub_cat;
                description = get_description;
                date_format = get_date;
                st_time = get_start_time;
                expected_hours = get_type;
                amount = get_amount;
                type = get_type;
                flexible_status = flexible;
                job_estimated = job_estimated_payment;
                job_expire = job_expire_date_time;

                String date = get_date;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd,yyyy");
                try {
                    Date startEntryDate = formatter.parse(date);
                    String new_date_format = dateFormatter.format(startEntryDate);
                    System.out.println("dddddddd:date:::"+new_date_format);
                    date_text.setText(new_date_format);

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                String time = get_start_time;
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                SimpleDateFormat sdfs = new SimpleDateFormat("h:mm a");
                Date dt;
                try {
                    dt = sdf.parse(time);
                    String new_time_format = sdfs.format(dt).toUpperCase().replace(".","");
                    System.out.println("ddddddd:new_time_format: " + new_time_format);
                    start_time_text.setText(new_time_format);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                job_name.setText(get_name);
                job_description.setText(get_description);
                end_time_text.setText(get_type);
                pay_text.setVisibility(View.GONE);
                arrow.setVisibility(View.VISIBLE);
                arrow1.setVisibility(View.GONE);
                payment_layout.setVisibility(View.VISIBLE);
                job_amount.setText(get_amount);
                amount_text.setText("Hourly Wage");
                if(flexible.equals("yes"))
                {
                    checkBox.setChecked(true);
                }

                if(job_category.equals("1"))
                {
                    main_category_image.setVisibility(View.VISIBLE);
                    main_category_image.setImageResource(R.drawable.care_giving);
                    header = "CARE GIVING";
                }
                if(job_category.equals("2"))
                {
                    main_category_image.setVisibility(View.VISIBLE);
                    main_category_image.setImageResource(R.drawable.coaching);
                    header = "COACHING";
                }
                if(job_category.equals("3"))
                {
                    main_category_image.setVisibility(View.VISIBLE);
                    main_category_image.setImageResource(R.drawable.holidays);
                    header = "HOLIDAYS";
                }
                if(job_category.equals("4"))
                {
                    main_category_image.setVisibility(View.VISIBLE);
                    main_category_image.setImageResource(R.drawable.inside_home);
                    header = "INSIDE THE HOME";
                }
                if(job_category.equals("5"))
                {
                    main_category_image.setVisibility(View.VISIBLE);
                    main_category_image.setImageResource(R.drawable.outside_home);
                    header = "OUTSIDE THE HOME";
                }
                if(job_category.equals("6"))
                {
                    main_category_image.setVisibility(View.VISIBLE);
                    main_category_image.setImageResource(R.drawable.personal_services);
                    header = "PERSONAL SERVICES";
                }
                if(job_category.equals("7"))
                {
                    main_category_image.setVisibility(View.VISIBLE);
                    main_category_image.setImageResource(R.drawable.petcare);
                    header = "PETCARE";
                }
                if(job_category.equals("8"))
                {
                    main_category_image.setVisibility(View.VISIBLE);
                    main_category_image.setImageResource(R.drawable.tutoring);
                    header = "TUTORING";
                }
                category_name.setText(sub_cat);

                dialog.dismiss();

            } else {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class datepickerClass extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();

            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,this,year,month,day);
            datepickerdialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return datepickerdialog;

        }


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub




            Calendar calander2 = Calendar.getInstance();

            calander2.setTimeInMillis(0);

            calander2.set(year, monthOfYear, dayOfMonth, 0, 0, 0);

            Date SelectedDate = calander2.getTime();

            int mm = monthOfYear + 1;
            String month = (mm < 10) ? "0" + mm : "" + mm;
            date_format = year + "-" + month + "-" + dayOfMonth;
            System.out.println("dddddddddd:date_format:::"+date_format);

            DateFormat dateformat_US = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
            String StringDateformat_US = dateformat_US.format(SelectedDate);
            System.out.println("dddddddddd:StringDateformat_US:::"+StringDateformat_US);
            date_text.setText(StringDateformat_US);

            /*DateFormat dateformat_UK = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
            String StringDateformat_UK = dateformat_UK.format(SelectedDate);
            date_text.setText(date_text.getText() + StringDateformat_UK + "\n");*/

        }
    }

    TextWatcher tw = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!s.toString().matches("^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {
                String userInput = "" + s.toString().replaceAll("[^\\d]", "");
                StringBuilder cashAmountBuilder = new StringBuilder(userInput);
                while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                    cashAmountBuilder.deleteCharAt(0);
                }
                while (cashAmountBuilder.length() < 3) {
                    cashAmountBuilder.insert(0, '0');
                }
                cashAmountBuilder.insert(cashAmountBuilder.length() - 2, '.');

                payamount.removeTextChangedListener(this);
                payamount.setText(cashAmountBuilder.toString());

                payamount.setTextKeepState(cashAmountBuilder.toString());
                Selection.setSelection(payamount.getText(), cashAmountBuilder.toString().length());

                payamount.addTextChangedListener(this);
            }
        }
    };
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
                Intent i;
                if(Profilevalues.usertype.equals("1")) {
                    i = new Intent(getApplicationContext(), ProfilePage.class);
                }else{
                    i = new Intent(getApplicationContext(), LendProfilePage.class);
                }
                i.putExtra("userId", Profilevalues.user_id);
                i.putExtra("address", Profilevalues.address);
                i.putExtra("city", Profilevalues.city);
                i.putExtra("state", Profilevalues.state);
                i.putExtra("zipcode", Profilevalues.zipcode);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
                break;
            /*case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                break;*/

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
