package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class EditPostedAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    public EditPostedAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
            vi = inflater.inflate(R.layout.edit_posted_list, null);

        TextView job_name = (TextView) vi.findViewById(R.id.text1);
        TextView when = (TextView) vi.findViewById(R.id.text2);
        TextView pay = (TextView) vi.findViewById(R.id.text6);
        TextView date = (TextView) vi.findViewById(R.id.text3);
        TextView amount = (TextView) vi.findViewById(R.id.text7);
        TextView expected = (TextView) vi.findViewById(R.id.expected);
        TextView type = (TextView) vi.findViewById(R.id.es_text);
        TextView jobId = (TextView) vi.findViewById(R.id.job_id);
        TextView applicants = (TextView) vi.findViewById(R.id.no_applicants);

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface font = Typeface.createFromAsset(activity.getAssets(), fontPath);
        String fontPath1 = "fonts/calibri.ttf";
        Typeface font1 = Typeface.createFromAsset(activity.getAssets(), fontPath1);

        HashMap<String, String> items = new HashMap<String, String>();
        items = data.get(position);
        final String get_name = items.get("name");
        final String get_date = items.get("date");
        String get_amount = items.get("amount");
        String get_type = items.get("type");
        String get_id = items.get("jobId");


        DateFormat dateInstance = SimpleDateFormat.getDateInstance();
        DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat destDf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        try {
            java.util.Date dates = srcDf.parse(get_date);
            date.setText("" + destDf.format(dates));

        } catch (Exception e)
        {
            System.out.println("error " + e.getMessage());
        }

        job_name.setText(get_name);
        job_name.setTypeface(font);
        when.setTypeface(font);
        expected.setTypeface(font);
        pay.setTypeface(font);
        date.setTypeface(font);
        amount.setText(get_amount);
        amount.setTypeface(font);
        type.setText(get_type);
        type.setTypeface(font);
        jobId.setText(get_id);

        return vi;
    }
}
