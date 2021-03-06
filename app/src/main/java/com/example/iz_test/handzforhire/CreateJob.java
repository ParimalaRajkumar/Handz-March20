package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bigkoo.pickerview.MyOptionsPickerView;
import com.example.iz_test.handzforhire.DateTimeWheel.DateWheel.DatePickerPopWin;
import com.example.iz_test.handzforhire.DateTimeWheel.TimeWheel.TimePickerPopWin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateJob extends Activity implements View.OnClickListener,SimpleGestureFilter.SimpleGestureListener{

    Spinner list;
    LinearLayout layout,category_layout;
    String id,address,zipcode,state,city,name,description,date,start_time,end_time,amount,type,st_time,en_time;
    private static final String URL = Constant.SERVER_URL+"job_category_lists";
    Button next;
    String categoryId,job_category_color,sub_category,header,child;
    EditText job_name,job_description;
    static TextView date_text;
    TextView start_time_text;
    TextView end_time_text;
    TextView job_amount;
    TextView pay_text;
    TextView amount_text,hour;
    public static TextView textview;
    public static ImageView img_paint;
    private int mHour, mMinute;
    ImageView logo,arrow,img_arrow;
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    static ArrayList<HashMap<String, String>> job_title = new ArrayList<HashMap<String, String>>();
    String job_category_name,job_id,payment_type,pay_amount,flexible_status,job_estimated,paytext,hourr,time_value;
    RelativeLayout pay_lay,payment_layout,date_layout,time_layout,estimate_layout;
    Integer cat;
    CheckBox checkBox;
    ImageView main_category_image,arrow1;
    EditText payamount;
    Activity activity;
    RelativeLayout duration_layout;
    CustomJobListAdapter adapter;
    public static PopupWindow popupWindowDogs;

    //MyOptionsPickerView threePicker;
    MyPicker threePicker;
    public static String date_format;
    TextView select_category;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    Dialog dialog;
    private SimpleGestureFilter detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_job);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        dialog = new Dialog(CreateJob.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        detector = new SimpleGestureFilter(this,this);

        layout = (LinearLayout)findViewById(R.id.relay);
        category_layout = (LinearLayout)findViewById(R.id.linear);
        duration_layout = (RelativeLayout)findViewById(R.id.linear3);
        select_category = (TextView)findViewById(R.id.cat_name);
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
        arrow1 = (ImageView) findViewById(R.id.arrow1);
        main_category_image =(ImageView)findViewById(R.id.main_category);

        pay_lay = (RelativeLayout) findViewById(R.id.linear4);
        payment_layout = (RelativeLayout) findViewById(R.id.relay1);
        date_layout = (RelativeLayout) findViewById(R.id.linear1);
        time_layout = (RelativeLayout) findViewById(R.id.linear2);
        estimate_layout = (RelativeLayout) findViewById(R.id.linear3);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        hour = (TextView) findViewById(R.id.hour);



        Intent i = getIntent();
        id = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");
        System.out.println("iiiiiiiiiiiiiiiiiiiii:" + id);

        String pattern1 = "hh:mm a";
        String timeFormat = new SimpleDateFormat(pattern1).format(new Date());

        String pattern2 = "hh:mm:ss";
        st_time = new SimpleDateFormat(pattern2).format(new Date());
        en_time = new SimpleDateFormat(pattern2).format(new Date());
        System.out.println("777777777:time::::" + st_time+",,,,"+ en_time);
        activity=this;

        listCategory();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(30);
        job_name.setFilters(FilterArray);

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

        estimate_layout.setOnClickListener(this);
        duration_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threePicker.show();
            }
        });

        //Three Options PickerView
        threePicker = new MyPicker(CreateJob.this);
        final ArrayList<Integer> numbers = new ArrayList<Integer>(100);

        for (int j = 0; j <100; j++)
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
                // Toast.makeText(CreateJob.this, "" + numbers.get(options1) + " " + threeItemsOptions2.get(option2) + " " + threeItemsOptions3.get(options3), Toast.LENGTH_SHORT).show();
            }
        });

        pay_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(CreateJob.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.payment_details);
                String fontPath = "fonts/LibreFranklin-SemiBoldItalic.ttf";
                Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
                ImageView close = (ImageView) dialog.findViewById(R.id.close_btn);
                ImageView logo = (ImageView) dialog.findViewById(R.id.logo);
                payamount = (EditText) dialog.findViewById(R.id.amount);
                Button update = (Button) dialog.findViewById(R.id.update_btn);
                final TextView text = (TextView) dialog.findViewById(R.id.text);
                final TextView text1 = (TextView) dialog.findViewById(R.id.text1);
                final TextView text2 = (TextView) dialog. findViewById(R.id.text2);
                final TextView text3 = (TextView) dialog.findViewById(R.id.text3);
                payamount.setTypeface (tf);
                text.setTypeface(tf);
                text1.setTypeface(tf);
                text2.setTypeface(tf);
                text3.setTypeface(tf);
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
                        System.out.println("sssssssssssss::pay_amount:"+pay_amount);
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
                dialog.getWindow().setDimAmount(0);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                return;

            }
        });

        category_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(CreateJob.this);
                dialog.setContentView(R.layout.category_popup);

                expListView = (ExpandableListView) dialog.findViewById(R.id.lvExp);

                // preparing list data
                prepareListData();

                listAdapter = new ExpandableListAdapter(CreateJob.this, listDataHeader, listDataChild)
                {
                    @Override
                    public void OnIndicatorClick(boolean isExpanded, int position) {
                        if(isExpanded){
                            expListView.collapseGroup(position);
                        }else{
                            expListView.expandGroup(position);
                        }
                    }

                    public void OnTextClick() {
                        //Do whatever you want to do on text click
                    }
                };

                expListView.setAdapter(listAdapter);

                expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        int pos = groupPosition+1;
                        categoryId = String.valueOf(pos);
                        header = listDataHeader.get(groupPosition);
                        sub_category = header;
                        System.out.println("fffffff:::::setOnGroupClickListener:::"+header+"..."+sub_category);
                        if(header.equals("CARE GIVING"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.care_giving);
                            select_category.setText(header);
                            dialog.dismiss();
                        }
                        if(header.equals("COACHING"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.coaching);
                            select_category.setText(header);
                            dialog.dismiss();
                        }
                        if(header.equals("HOLIDAYS"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.holidays);
                            select_category.setText(header);
                            dialog.dismiss();
                        }
                        if(header.equals("INSIDE THE HOME"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.inside_home);
                            select_category.setText(header);
                            dialog.dismiss();
                        }
                        if(header.equals("OUTSIDE THE HOME"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.outside_home);
                            select_category.setText(header);
                            dialog.dismiss();
                        }
                        if(header.equals("PERSONAL SERVICES"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.personal_services);
                            select_category.setText(header);
                            dialog.dismiss();
                        }
                        if(header.equals("PETCARE"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.petcare);
                            select_category.setText(header);
                            dialog.dismiss();
                        }
                        if(header.equals("TUTORING"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.tutoring);
                            select_category.setText(header);
                            dialog.dismiss();
                        }
                        System.out.println("fffffff:::::categoryId:::"+categoryId);
                        return true;
                    }
                });

                // Listview on child click listener
                expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) {
                        // TODO Auto-generated method stub
                        System.out.println("ffffff::::setOnChildClickListener");

                        int pos = groupPosition+1;
                        categoryId = String.valueOf(pos);
                        header =  listDataHeader.get(groupPosition);
                        sub_category =  listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                        System.out.println("ccccccccc:"+header+",,"+sub_category+",,"+pos+"id:"+categoryId);
                        if(header.equals("CARE GIVING"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.care_giving);
                            select_category.setText(sub_category);
                        }
                        if(header.equals("COACHING"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.coaching);
                            select_category.setText(sub_category);
                        }
                        if(header.equals("HOLIDAYS"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.holidays);
                            select_category.setText(sub_category);
                        }
                        if(header.equals("INSIDE THE HOME"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.inside_home);
                            select_category.setText(sub_category);
                        }
                        if(header.equals("OUTSIDE THE HOME"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.outside_home);
                            select_category.setText(sub_category);
                        }
                        if(header.equals("PERSONAL SERVICES"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.personal_services);
                            select_category.setText(sub_category);
                        }
                        if(header.equals("PETCARE"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.petcare);
                            select_category.setText(sub_category);
                        }
                        if(header.equals("TUTORING"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.tutoring);
                            select_category.setText(sub_category);
                        }
                        dialog.dismiss();
                        return false;
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                dialog.getWindow().setDimAmount(0);
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
            openDatePickerDialog(v);
        }
        if (v == time_layout) {
            openTimePickerDialog(v);
        }

    }

    public void validate()
    {
        name = job_name.getText().toString().trim();
        description = job_description.getText().toString().trim();
        date = date_text.getText().toString().trim();
        start_time = start_time_text.getText().toString();
        end_time = end_time_text.getText().toString();
        amount = job_amount.getText().toString().trim();
        payment_type = amount_text.getText().toString().trim();

        if(header==null)
        {
            final Dialog dialog = new Dialog(CreateJob.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must choose any one category");
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
            dialog.getWindow().setDimAmount(0);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            return;
        }else {
            if (header.equals("CARE GIVING")) {
                job_category_color = "#FF87FA";
                System.out.println("cccccccccccccc:colorcode:::" + job_category_color);
            }
            if (header.equals("COACHING")) {
                job_category_color = "#BED2EA";
                System.out.println("cccccccccccccc:colorcode:::" + job_category_color);

            }
            if (header.equals("HOLIDAYS")) {
                job_category_color = "#FF4B13";
                System.out.println("cccccccccccccc:colorcode:::" + job_category_color);
            }
            if (header.equals("INSIDE THE HOME")) {
                job_category_color = "#FFFB86";
                System.out.println("cccccccccccccc:colorcode:::" + job_category_color);
            }
            if (header.equals("OUTSIDE THE HOME")) {
                job_category_color = "#00D034";
                System.out.println("cccccccccccccc:colorcode:::" + job_category_color);
            }
            if (header.equals("PERSONAL SERVICES")) {
                job_category_color = "#FFC834";
                System.out.println("cccccccccccccc:colorcode:::" + job_category_color);
            }
            if (header.equals("PETCARE")) {
                job_category_color = "#AA84FA";
                System.out.println("cccccccccccccc:colorcode:::" + job_category_color);
            }
            if (header.equals("TUTORING")) {
                job_category_color = "#6BAEFB";
                System.out.println("cccccccccccccc:colorcode:::" + job_category_color);
            }
        }
        if (TextUtils.isEmpty(name)) {
            // custom dialog
            final Dialog dialog = new Dialog(CreateJob.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must Fill In \"Job Title\" Box");
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
            dialog.getWindow().setDimAmount(0);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            return;
        }
        if (TextUtils.isEmpty(description)) {
            // custom dialog
            final Dialog dialog = new Dialog(CreateJob.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must Fill In \"Detailed Description of Job\" Box");
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
            dialog.getWindow().setDimAmount(0);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            return;
        }
        if (TextUtils.isEmpty(amount)) {
            // custom dialog
            final Dialog dialog = new Dialog(CreateJob.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must Fill In \"Payment Details\"");
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
            dialog.getWindow().setDimAmount(0);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            return;
        }
        if(categoryId.equals("0"))
        {
            final Dialog dialog = new Dialog(CreateJob.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must choose any one category");
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
            dialog.getWindow().setDimAmount(0);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            return;
        }
        if (checkBox.isChecked())
        {
            flexible_status = "yes";
        }
        else
        {
            flexible_status = "no";
        }
        if(date.equals("Job Date"))
        {
            final Dialog dialog = new Dialog(CreateJob.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must choose \"Job Date\"");
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
            dialog.getWindow().setDimAmount(0);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            return;
        }
        if(start_time.equals("Start Time"))
        {
            final Dialog dialog = new Dialog(CreateJob.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must choose \"Start Time\"");
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
            dialog.getWindow().setDimAmount(0);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            return;
        }
        if(end_time.equals("Expected Duration"))
        {
            final Dialog dialog = new Dialog(CreateJob.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must choose \"Expected Duration\"");
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
            dialog.getWindow().setDimAmount(0);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            return;
        }
        if(!name.equals("")&&!description.equals("")&&!date.equals("")&&!start_time.equals("")&&!end_time.equals("")&&!amount.equals(""))
        {
            System.out.println("ffff:create:::"+date+"...."+start_time+",."+end_time);
            String expected_hours = end_time_text.getText().toString();
            System.out.println("eeeeeeeee:time:::"+expected_hours+"...."+amount);
            job_estimated = String.valueOf(Float.valueOf(expected_hours)*Float.valueOf(amount));
            System.out.println("eeeeeeeee:estimated:::"+job_estimated);
            System.out.println("eeeeeeeee:time_value:::"+time_value);
            String job_expire = date_format + " " + time_value ;
            System.out.println("eeeeeeeee:job_expire:::"+job_expire);
            String hours = hour.getText().toString();
            String duration = expected_hours+" "+hours;
            System.out.println("eeeeeeeee:duration:::"+duration);

            Intent i = new Intent(CreateJob.this, CreateJob2.class);
            i.putExtra("userId", id);
            i.putExtra("job_name",name);
            i.putExtra("job_category",categoryId);
            i.putExtra("job_category_color", job_category_color);
            i.putExtra("sub_category", sub_category);
            i.putExtra("job_decription",description);
            i.putExtra("job_date", date_format);
            i.putExtra("start_time",time_value);
            i.putExtra("expected_hours",expected_hours);
            i.putExtra("duration",duration);
            i.putExtra("payment_amount",amount);
            i.putExtra("payment_type", expected_hours);
            i.putExtra("flexible_status", flexible_status);
            i.putExtra("estimated_amount", job_estimated);
            i.putExtra("job_expire", job_expire);

            startActivity(i);
        }
        else
        {

        }
    }

    public void listCategory() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:" + response);
                        onResponserecieved1(response, 2);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                       /* try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            System.out.println("error" + jsonObject);
                        } catch (JSONException e) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException error1) {

                        }*/
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(KEY_USERID, id);
                params.put(Constant.DEVICE, Constant.ANDROID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved1(String jsonobject, int i) {
        System.out.println("response from interface"+jsonobject);

        String status = null;
        String categories = null;

        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            categories = jResult.getString("categories");
            System.out.println("jjjjjjjjjjjjjjjob:::categories:::"+categories);
            if(status.equals("success"))
            {

                JSONArray array = new JSONArray(categories);
                for(int n = 0; n < array.length(); n++)
                {
                    JSONObject object = (JSONObject) array.get(n);
                    job_category_name = object.getString("name");
                    System.out.println(":job_category_name::" + job_category_name);
                    job_id = object.getString("id");
                    System.out.println(":job_id::" + job_id);

                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("job_category", job_category_name);
                    map.put("job_id", job_id);
                    job_title.add(map);
                    System.out.println("menuitems:::" + job_title);
                }


            }
            else
            {
            }

        } catch (JSONException e) {
            e.printStackTrace();
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

    public static class DatePickerDialogTheme4 extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT,this,year,month,day);

            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){

           /* TextView textview = (TextView)getActivity().findViewById(R.id.textView1);

            textview.setText(day + ":" + (month+1) + ":" + year);*/

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
        }
    }

    @Override
    public void onDoubleTap() {

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event){

        this.detector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    public void openDatePickerDialog(View view) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
        String strDate = mdformat.format(calendar.getTime());
        Integer current_year = Calendar.getInstance().get(Calendar.YEAR);
        System.out.println("cccccccc:strDate::"+strDate);
        System.out.println("cccccccc:current_year::"+current_year);

        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(CreateJob.this, new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                //Toast.makeText(CreateJob.this, dateDesc, Toast.LENGTH_SHORT).show();
                System.out.println("cccccccc:dateDesc::"+dateDesc);
                date_format = dateDesc;
                DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat destDf = new SimpleDateFormat("MMMM dd, yyyy");
                try {
                    Date dates = srcDf.parse(dateDesc);
                    date_text.setText("" + destDf.format(dates));

                } catch (Exception e)
                {
                    System.out.println("error " + e.getMessage());
                }
            }
        }).textConfirm(" Done") //text of confirm button
                .textCancel("CANCEL") //text of cancel button
                .btnTextSize(22) // button text size
                .viewTextSize(22) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#000000"))//color of confirm button
                .minYear(current_year) //min year in loop
                .maxYear(2550) // max year in loop
                .dateChose(strDate)// date chose when init popwindow
                .build();

        pickerPopWin.showPopWin(this);
    }

    public void openTimePickerDialog(View view) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strTime = mdformat.format(calendar.getTime());
        System.out.println("cccccccc:strTime::"+strTime);

        TimePickerPopWin pickerPopWin = new TimePickerPopWin.Builder(CreateJob.this, new TimePickerPopWin.OnTimePickedListener() {
            @Override
            public void onTimePickCompleted(int hour, int min, int sec, String meridium, String timeDesc) {
                //Toast.makeText(CreateJob.this, timeDesc, Toast.LENGTH_SHORT).show();
                System.out.println("cccccccc:timeDesc::"+timeDesc);
                String u = meridium;
                int text1 = 0;
                int text2 = 0;
                if(hour<12&&min==00&&u.equals("AM"))
                {
                    text1 = hour;
                    text2 = hour;
                    System.out.println("hhhhhhhhhhhhh:1:::"+text1+"..."+text2);
                }
                if(hour<12&&min==00&&u.equals("PM"))
                {
                    text1 = hour+12;
                    text2 = hour;
                    System.out.println("hhhhhhhhhhhhh:2:::"+text1+"..."+text2);
                }
                if(hour==12&&min==00&&u.equals("AM"))
                {
                    text1 = hour;
                    text2 = hour;
                    System.out.println("hhhhhhhhhhhhh:3:::"+text1+"..."+text2);
                }
                if(hour==12&&min==00&&u.equals("PM"))
                {
                    text1 = 24;
                    text2 = hour;
                    System.out.println("hhhhhhhhhhhhh:4:::"+text1+"..."+text2);
                }
                if(hour==12&&min!=00&&u.equals("PM"))
                {
                    text1 = 1;
                    text2 = 1;
                    u = "AM";
                    System.out.println("hhhhhhhhhhhhh:5:::"+text1+"..."+text2);
                }
                if(hour<12&&min!=00&&u.equals("AM"))
                {
                    text2 = hour+1;
                    text1 = text2;
                    System.out.println("hhhhhhhhhhhhh:6:::"+text1+"..."+text2);
                }
                if(hour<12&&min!=00&&u.equals("PM"))
                {
                    text2 = hour+1;
                    text1 = text2+12;
                    System.out.println("hhhhhhhhhhhhh:7:::"+text1+"..."+text2);
                }
                if(hour==12&&min!=00&&u.equals("AM"))
                {
                    text1 = 1;
                    text2 = 1;
                    System.out.println("hhhhhhhhhhhhh:8:::"+text1+"..."+text2);
                }
                time_value = (text1 < 10 ? "0" : "") + text1 + ":" + "00" + ":" + "00";
                System.out.println("hhhhhhhhhhhhh:time_value:::"+time_value);
                String s = text2 + ":"+ "00" +" "+ u;
                System.out.println("hhhhhhhhhhhhh:s:::"+s);
                start_time_text.setText(s);

            }

        }).textConfirm("Done") //text of confirm button
                .textCancel("CANCEL") //text of cancel button
                .btnTextSize(22) // button text size
                .viewTextSize(22) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#000000"))
                .timeChose(strTime)//color of confirm button
                .build();

        pickerPopWin.showPopWin(this);
    }


}
