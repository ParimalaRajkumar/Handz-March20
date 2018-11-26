package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewJobAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    private final Integer[] imageId;

    public ViewJobAdapter(Activity a, ArrayList<HashMap<String, String>> d, Integer[] imageId) {
        activity = a;
        data = d;
        this.imageId = imageId;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.job_list, null);

        TextView job = (TextView) vi.findViewById(R.id.text);
        TextView id = (TextView) vi.findViewById(R.id.id);
        ImageView imageView = (ImageView) vi.findViewById(R.id.image);

        HashMap<String, String> items = new HashMap<String, String>();
        items = data.get(position);
        final String job_category = items.get("job_category");
        final String job_id = items.get("job_id");

        job.setText(job_category);
        id.setText(job_id);
        imageView.setImageResource(imageId[position]);

        return vi;
    }
}
