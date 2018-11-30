package com.example.iz_test.handzforhire;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public abstract class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<String>> _listDataChild;

	public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final String childText = (String) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}

		TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);

		txtListChild.setText(childText);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, final boolean isExpanded,
							 View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		//System.out.println("hhhhhhhhhhhhh:headerTitle:::"+headerTitle);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}

		TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		if(groupPosition % 8 == 0) {
			//convertView.setBackgroundColor(Color.parseColor("#FF87FA"));
			convertView.setBackgroundResource(R.drawable.top_expandable_shape);
		}
		if(groupPosition % 8 == 1) {
			convertView.setBackgroundColor(Color.parseColor("#BED2EA"));
		}
		if(groupPosition % 8 == 2) {
			convertView.setBackgroundColor(Color.parseColor("#FF4B13"));
		}
		if(groupPosition % 8 == 3) {
			convertView.setBackgroundColor(Color.parseColor("#FFFB86"));
		}
		if(groupPosition % 8 == 4) {
			convertView.setBackgroundColor(Color.parseColor("#00D034"));
		}
		if(groupPosition % 8 == 5) {
			convertView.setBackgroundColor(Color.parseColor("#FFC834"));
		}
		if(groupPosition % 8 == 6) {
			convertView.setBackgroundColor(Color.parseColor("#AA84FA"));
		}
		if(groupPosition % 8 == 7) {
			//convertView.setBackgroundColor(Color.parseColor("#6BAEFB"));
			convertView.setBackgroundResource(R.drawable.bottom_expandable_shape);
		}

		ImageView indicator = (ImageView) convertView.findViewById(R.id.plus);
		indicator.setSelected(isExpanded);
		indicator.setTag(groupPosition);

		indicator.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = (Integer)v.getTag();
				OnIndicatorClick(isExpanded,position);

			}
		});

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public abstract void OnIndicatorClick(boolean isExpanded, int position);
}
