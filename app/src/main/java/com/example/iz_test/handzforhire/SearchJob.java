package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchJob extends Activity implements SimpleGestureFilter.SimpleGestureListener{

    private static final String URL = Constant.SERVER_URL+"job_category_lists";

    LinearLayout layout,search_layout;
    ProgressDialog progress_dialog;
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    String id,address,zipcode,state,city,name,job_category_name,job_id,categoryId,item,job_cat_name;
    static ArrayList<HashMap<String, String>> job_title = new ArrayList<HashMap<String, String>>();
    Spinner list;
    ImageView logo;
    TextView category_name;
    Button search;
    EditText zip,radius;
    CheckBox checkBox;
    LocationManager locationManager;
    String get_zipcode,get_radius,all_jobs,header,sub_category;
    Integer cat;
    ImageView img_dropdown;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static PopupWindow popupWindowDogs;
    CustomJobListAdapter adapter;
    public static TextView textview;
    public static ImageView  main_category_image;
    static String category="0",category_id="0";
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private SimpleGestureFilter detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_job);

        layout = (LinearLayout)findViewById(R.id.relay);
        category_name = (TextView)findViewById(R.id.cat_name);
        logo = (ImageView) findViewById(R.id.logo);
        search = (Button)findViewById(R.id.search);
        zip = (EditText) findViewById(R.id.zip);
        radius = (EditText) findViewById(R.id.radius);
        checkBox = (CheckBox) findViewById(R.id.checkBox1);
        search_layout = (LinearLayout)findViewById(R.id.linear);

        textview = (TextView) findViewById(R.id.textview);
        main_category_image =(ImageView)findViewById(R.id.main_category);

        Intent i = getIntent();
        id = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");
        System.out.println("iiiiiiiiiiiiiiiiiiiii:" + id);
        categoryId = "";
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryId = "";
                System.out.println("ssssssssssselected:job_cat_name:response:" + categoryId);
            }
        });

        detector = new SimpleGestureFilter(this,this);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get_zipcode = zip.getText().toString().trim();
                get_radius = radius.getText().toString().trim();
                System.out.println("ccccccccccccc:categoryId:::"+categoryId+",,,"+get_zipcode+",,,,"+get_radius);

                if(categoryId==null&&TextUtils.isEmpty(get_zipcode)&&TextUtils.isEmpty(get_radius)&&!checkBox.isChecked()) {
                    final Dialog dialog = new Dialog(SearchJob.this);
                    dialog.setContentView(R.layout.custom_dialog);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("Select atleast one value");
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
                }
                else
                {
                    if(checkBox.isChecked())
                    {
                        all_jobs = "all_jobs";
                        categoryId = "";
                        get_zipcode = "";
                        get_radius = "";
                    }
                    else
                    {
                        all_jobs = "";
                    }
                    Intent i = new Intent(SearchJob.this,ViewSearchJob.class);
                    i.putExtra("type","search");
                    i.putExtra("userId",Profilevalues.user_id);
                    i.putExtra("address",address);
                    i.putExtra("city",city);
                    i.putExtra("state",state);
                    i.putExtra("zipcode",zipcode);
                    i.putExtra("categoryId",categoryId);
                    i.putExtra("zip",get_zipcode);
                    i.putExtra("radius",get_radius);
                    i.putExtra("alljobs",all_jobs);
                    startActivity(i);
                }
            }
        });

        search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(SearchJob.this);
                dialog.setContentView(R.layout.category_popup);

                expListView = (ExpandableListView) dialog.findViewById(R.id.lvExp);

                // preparing list data
                prepareListData();

                listAdapter = new ExpandableListAdapter(SearchJob.this, listDataHeader, listDataChild)
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
                            category_name.setText(header);
                            dialog.dismiss();
                        }
                        if(header.equals("COACHING"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.coaching);
                            category_name.setText(header);
                            dialog.dismiss();
                        }
                        if(header.equals("HOLIDAYS"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.holidays);
                            category_name.setText(header);
                            dialog.dismiss();
                        }
                        if(header.equals("INSIDE THE HOME"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.inside_home);
                            category_name.setText(header);
                            dialog.dismiss();
                        }
                        if(header.equals("OUTSIDE THE HOME"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.outside_home);
                            category_name.setText(header);
                            dialog.dismiss();
                        }
                        if(header.equals("PERSONAL SERVICES"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.personal_services);
                            category_name.setText(header);
                            dialog.dismiss();
                        }
                        if(header.equals("PETCARE"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.petcare);
                            category_name.setText(header);
                            dialog.dismiss();
                        }
                        if(header.equals("TUTORING"))
                        {
                            main_category_image.setVisibility(View.VISIBLE);
                            main_category_image.setImageResource(R.drawable.tutoring);
                            category_name.setText(header);
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

                Intent  i = new Intent(getApplicationContext(), LendProfilePage.class);
                i.putExtra("userId", Profilevalues.user_id);
                i.putExtra("address", Profilevalues.address);
                i.putExtra("city", Profilevalues.city);
                i.putExtra("state", Profilevalues.state);
                i.putExtra("zipcode", Profilevalues.zipcode);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
                break;
           /* case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                break;
*/
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
