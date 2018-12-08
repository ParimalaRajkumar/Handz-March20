package com.example.iz_test.handzforhire;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.listeners.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;


public class FindJobMap extends Fragment implements GoogleMap.OnMarkerClickListener,GoogleMap.OnMyLocationChangeListener,GoogleMap.OnCameraChangeListener,ResponseListener,OnMapReadyCallback {

   Context context ;
    private static final String URL = Constant.SERVER_URL+"job_lists";
    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    String user_id;
    ArrayList<HashMap<String,String>> disclosed = new ArrayList<HashMap<String,String>>();
    private GoogleMap googleMap;
    //GPSTracker gps;
    LocationTrack locationTrack;
    public static int  MY_PERMISSIONS_REQUEST_READ_CONTACTS=1;
    public static Double lat,lon;
    TextView txt_undisclosedjob;
    ImageView logo,menu,categprylist,key;
    private static Hashtable<String, String> markers =new Hashtable<String, String>();
    public static ArrayList<String> undisclosedjobs=new ArrayList<String>();
    public static JSONArray undisclosedjobsarray=new JSONArray();
    SessionManager session;
    String id,address,city,state,zipcode,profile_image,profilename,email,user_name;

    public static Fragment fragments;
    View rootView;
    int undisclosedjob=0;
    Dialog dialog;
    Location mCurrentLocaton=null;
    public static String job_id;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.find_job_map, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        SupportMapFragment fragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        session = new SessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        // get name
        user_name = user.get(SessionManager.USERNAME);
        id = user.get(SessionManager.ID);
        email = user.get(SessionManager.EMAIL);
        address = user.get(SessionManager.ADDRESS);
        city = user.get(SessionManager.CITY);
        state = user.get(SessionManager.STATE);
        zipcode = user.get(SessionManager.ZIPCODE);

       /* Intent intent = getIntent();
        user_id = intent.getStringExtra("userId");*/
        user_id=user.get(SessionManager.ID);

        txt_undisclosedjob=(TextView)rootView.findViewById(R.id.txt_undisclosedjob);
        logo = (ImageView) rootView.findViewById(R.id.logo);
        menu = (ImageView) rootView.findViewById(R.id.menu);
        categprylist = (ImageView) rootView.findViewById(R.id.categprylist);
        key = (ImageView) rootView.findViewById(R.id.key);
        //initilizeMap();
        /// Changing map type

        // create class object
       // gps = new GPSTracker(getActivity());
        locationTrack = LocationTrack.getInstance(getActivity());
        String fontPath = "fonts/LibreFranklin-SemiBoldItalic.ttf";
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), fontPath);
        txt_undisclosedjob.setTypeface(font);

        key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(categprylist.getVisibility()==View.VISIBLE)
                    categprylist.setVisibility(View.GONE);
                else
                    categprylist.setVisibility(View.VISIBLE);
            }
        });

        categprylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categprylist.setVisibility(View.VISIBLE);
            }
        });        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LendProfilePage.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
                getActivity().finish();
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LendProfilePage.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
                getActivity().finish();
            }
        });


        txt_undisclosedjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(undisclosedjob>0){
                   Intent in_job_list=new Intent(getActivity(),ViewSearchJob.class);
                    in_job_list.putExtra("type","undisclosed");
                    startActivity(in_job_list);
                }
            }
        });

        return rootView;
    }


    private double[] createRandLocation(double latitude, double longitude) {

        return new double[]{latitude + ((Math.random() - 0.5) / 500),
                longitude + ((Math.random() - 0.5) / 500),
                150 + ((Math.random() - 0.5) * 10)};
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (markers.get(marker.getId()) != null) {
            try {
                JSONObject obj = new JSONObject(markers.get(marker.getId()));
                showDetails(obj.toString(),getActivity());
            }catch (Exception e){
                System.out.println("Exception e test "+e.getMessage());
            }
        }

        return false;
    }

    public void showDetails(String obj,Activity activity){

        try{
            System.out.println("obj "+obj);
            AlertDialog.Builder builder=new AlertDialog.Builder(activity);
            JSONObject object=new JSONObject(obj);
            System.out.println("job cat "+object);
            String job_name=object.getString("job_name");
            String recurring=object.getString("recurring");
            String job_payment_amount=object.getString("job_payment_amount");
            String job_date=object.getString("job_date");
            String profile_image=object.getString("profile_image");
            String duration=object.getString("job_payment_type");
            LayoutInflater inflater = LayoutInflater.from(activity);
            View view = inflater.inflate(R.layout.findjob_popup, null);
            builder.setView(view);
            final AlertDialog alert = builder.create();

            LinearLayout lin_job_view=(LinearLayout)view.findViewById(R.id.lin_job_view);
            TextView txt_jobcat=(TextView)view.findViewById(R.id.txt_jobcat);
            final TextView txt_obj=(TextView)view.findViewById(R.id.txt_obj);
            TextView txt_amount=(TextView)view.findViewById(R.id.txt_amount);
            TextView txt_when=(TextView)view.findViewById(R.id.txt_when);
            TextView txt_dur=(TextView)view.findViewById(R.id.txt_duration);
            ImageView img_profile=(ImageView)view.findViewById(R.id.img_profile);
            ImageView img_close=(ImageView)view.findViewById(R.id.img_close);

            txt_jobcat.setText(job_name);
            String s1 = "1.00";
            String multi = String.valueOf(Float.valueOf(job_payment_amount)*Float.valueOf(s1));
            String total_amount = String.format("%.2f", Float.valueOf(multi));
            txt_amount.setText("PAY:$"+total_amount);
            txt_dur.setText("EXPECTED DURATION: "+duration);
            txt_obj.setText(object.toString());

            DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat destDf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
            try{
                Date dates = srcDf.parse(job_date);
                System.out.println("converted "+destDf.format(dates));
                txt_when.setText("WHEN: "+destDf.format(dates));

            }catch (Exception e)
            {
                System.out.println("error "+e.getMessage());
            }
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

           try {
               Picasso.with(activity).load(profile_image).networkPolicy(NetworkPolicy.NO_CACHE)
                       .memoryPolicy(MemoryPolicy.NO_CACHE)
                       .placeholder(R.drawable.default_profile)
                       .into(img_profile);
           }catch (Exception e){
               System.out.println("exception "+e.getMessage());
           }
            alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alert.show();
            lin_job_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Intent i = new Intent(getActivity(),ApplyJob.class);
                    System.out.println("current obj "+txt_obj.getText().toString());
                    try {
                        JSONObject obj = new JSONObject(txt_obj.getText().toString());
                        job_id=obj.getString("id");
                        String average_rating=obj.getString("average_rating");
                        /*RestClientPost rest = new RestClientPost(getActivity(), 2);
                        rest.execute1(RestClientPost.RequestMethod.POST, getActivity(), FindJobMap.this);*/
                        Intent i = new Intent(getActivity(),JobDescription.class);
                        i.putExtra("userId",user_id);
                        i.putExtra("jobId",job_id);
                        i.putExtra("average_rating",average_rating);
                        startActivity(i);
                    }catch (Exception e){
                        System.out.println("Exception e"+e.getMessage());;
                    }
                  /*  */
                   // startActivity(i);
                }
            });

        }catch (Exception e){
            System.out.println("Exception "+e.getMessage());
        }
    }

    @Override
    public void onMyLocationChange(Location location) {

    }


    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {

        Bitmap bitmap=null;
        try {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

         bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        }catch (Exception e){
            System.out.println("exception "+e.getMessage());
        }
        return bitmap;
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        System.out.println("cameraPosition"+cameraPosition);
        try {
            LatLng latLng=cameraPosition.target;
            lat=latLng.latitude;
            lon=latLng.longitude;
           // session.savelocation(String.valueOf(lat),String.valueOf(lon));
            RestClientPost rest = new RestClientPost(getActivity(), 1);
            rest.execute(RestClientPost.RequestMethod.POST, getActivity(),FindJobMap.this);
        }catch (Exception e){
            System.out.println("exception "+e.getMessage());
        }
    }

    @Override
    public void onResponseReceived(JSONObject responseObj, int requestType) {
        try{

            if(requestType==1) {
                undisclosedjobs.clear();
                undisclosedjobsarray = new JSONArray();
                String status = responseObj.getString("status");
                System.out.println("response on map" + responseObj);
                if (status.equals("error")) {
                    undisclosedjob = 0;
                    txt_undisclosedjob.setText("0 Additional Undisclosed Locations\n Within a 5 mile radius of  map center\n(Click Here for ListView With Job Details)");

                } else {
                    googleMap.clear();
                    JSONArray joblist = responseObj.getJSONArray("job_lists");
                    for (int i = 0; i < joblist.length(); i++) {
                        JSONObject object = joblist.getJSONObject(i);
                        String postaddress = object.getString("post_address");
                        if (postaddress.equals("yes")) {
                            String lat = object.getString("lat");
                            String lon = object.getString("lon");
                            String job_category = object.getString("job_category");

                            Bitmap icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_101);
                           //  Bitmap icon = getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_8);
                             if (job_category.equals("1")) {
                                 icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_101);
                            } else if (job_category.equals("2")) {
                                 icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_102);
                            } else if (job_category.equals("3")) {
                                 icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_103);
                            } else if (job_category.equals("4")) {
                                 icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_104);
                            } else if (job_category.equals("5")) {
                                 icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_105);
                            } else if (job_category.equals("6")) {
                                 icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_106);
                            } else if (job_category.equals("7")) {
                                 icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_107);
                            } else if (job_category.equals("8)")) {
                                 icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_108);
                            }
                            MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon))).icon(BitmapDescriptorFactory.fromBitmap(icon));
                            googleMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) FindJobMap.this);
                            final Marker groupMarker = googleMap.addMarker(marker);
                            markers.put(groupMarker.getId(), object.toString());
                        } else {
                            undisclosedjobsarray.put(object);
                            undisclosedjobs.add(object.toString());
                        }
                    }
                    undisclosedjob = undisclosedjobs.size();
                    txt_undisclosedjob.setText(undisclosedjobs.size() + " Additional Undisclosed Locations\n Within a 5 mile radius of  map center\n(Click Here for ListView With Job Details)");

                }
            }else if(requestType==2){
                System.out.println("response "+responseObj);
                String status = responseObj.getString("status");
                if (status.equals("success")) {
                   String job_data = responseObj.getString("job_data");

                    JSONObject object = new JSONObject(job_data);
                    if(object.getString("profile_name").isEmpty()){

                    }

              /*      i.putExtra("employerId",object.getString("employer_id"));
                    i.putExtra("job_name",object.getString("job_name"));
                    i.putExtra("date",object.getString("job_date"));
                    i.putExtra("start_time",object.getString("start_time"));
                    i.putExtra("end_time",object.getString("end_time"));
                    i.putExtra("profile_name",object.getString("profile_name"));
                    i.putExtra("amount",object.getString("job_payment_amount"));
                    i.putExtra("type",object.getString("job_payment_type"));
                    i.putExtra("image",object.getString("profile_image"));
                    i.putExtra("usertype",object.getString("usertype"));*/

                } else {
                }

            }
        }catch (Exception e){
            System.out.println("Error "+e.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap=map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.setOnCameraChangeListener(this);
        googleMap.setOnMyLocationChangeListener(this);

        try {
            RestClientPost rest = new RestClientPost(getActivity(), 1);
            rest.execute(RestClientPost.RequestMethod.POST, getActivity(),FindJobMap.this);
        }catch (Exception e){
            System.out.println("exception "+e.getMessage());
        }

       getLocation();

    }

    public void UpdateLocation(Location location)
    {
       mCurrentLocaton = location;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mCurrentLocaton.getLatitude(),
                        mCurrentLocaton.getLongitude())).zoom(12).build();
        session.savelocation(String.valueOf(mCurrentLocaton.getLatitude()),String.valueOf(mCurrentLocaton.getLongitude()));
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

    }

    public void getLocation()
    {
        locationTrack.getLocation();
    }

}
