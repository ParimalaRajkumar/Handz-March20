package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ReviewAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    public ReviewAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.review_list, null);

        final TextView rating = (TextView) vi.findViewById(R.id.rating);
        final TextView comments = (TextView) vi.findViewById(R.id.comments);
        final TextView date = (TextView) vi.findViewById(R.id.date);
        ImageView image1 = (ImageView) vi.findViewById(R.id.img1);
        RatingBar rating_bar = (RatingBar) vi.findViewById(R.id.ratingBar1);
        TextView cancelJob = vi.findViewById(R.id.cancel_job);

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface font = Typeface.createFromAsset(activity.getAssets(), fontPath);
        String fontPath1 = "fonts/calibri.ttf";
        Typeface font1 = Typeface.createFromAsset(activity.getAssets(), fontPath1);

        HashMap<String, String> items = new HashMap<String, String>();
        items = data.get(position);
        String get_image= items.get("image");
        final String get_average = items.get("average");
        final String get_date = items.get("date");
        final String comment = items.get("comments");

        System.out.println("Comment "+comment);

        rating.setText(get_average);
        rating.setTypeface(font);
      //  date.setText(get_date);
        rating.setTypeface(font1);
        comments.setText(comment);
        comments.setTypeface(font1);

        DateFormat dateInstance = SimpleDateFormat.getDateInstance();
        DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat destDf = new SimpleDateFormat("MMMM dd, yyyy");
        try {
            java.util.Date dates = srcDf.parse(get_date);
            date.setText("on " + destDf.format(dates));

        } catch (Exception e)
        {
            System.out.println("error " + e.getMessage());
        }

        System.out.println("Average rating "+get_average);
        if(get_average!=null && !get_average.equals(""))
        {
           rating_bar.setVisibility(View.VISIBLE);
            rating_bar.setRating(Float.parseFloat(get_average));
            cancelJob.setVisibility(View.INVISIBLE);
        }else {
            rating_bar.setVisibility(View.INVISIBLE);
            cancelJob.setVisibility(View.VISIBLE);
        }
        if(get_image!= null && get_image.contains("http://graph.facebook.com/"))
        {
            get_image = get_image.replace("https://www.handzadmin.com/assets/images/uploads/profile/","");
        }
            Glide.with(activity).load(get_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(activity,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image1);

        return vi;
    }

}
