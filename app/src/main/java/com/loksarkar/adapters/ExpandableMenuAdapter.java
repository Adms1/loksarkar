package com.loksarkar.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.loksarkar.R;
import com.loksarkar.models.ChildListModel;
import com.loksarkar.models.ExpandabelModel;

import java.util.HashMap;
import java.util.List;

public class ExpandableMenuAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<ExpandabelModel> mListDataHeader; // header titles

    // child data in format of header title, child title
    private HashMap<ExpandabelModel, List<ChildListModel>> mListDataChild;
    ExpandableListView expandList;

    public ExpandableMenuAdapter(Context context, List<ExpandabelModel> listDataHeader, HashMap<ExpandabelModel, List<ChildListModel>> listChildData, ExpandableListView mView) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
        this.expandList = mView;
    }

    @Override
    public int getGroupCount() {
        int i = mListDataHeader.size();
        Log.d("GROUPCOUNT", String.valueOf(i));
        return this.mListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int childCount = 0;
        if (groupPosition == 5) {
            childCount = this.mListDataChild.get(this.mListDataHeader.get(groupPosition)).size();
        }
        return childCount;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Log.d("CHILD", mListDataChild.get(this.mListDataHeader.get(groupPosition)).get(childPosition).toString());
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ExpandabelModel headerTitle = (ExpandabelModel) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listheader, null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.submenu);
        ImageView headerIcon = (ImageView) convertView.findViewById(R.id.iconimage);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.getName());
        headerIcon.setImageDrawable(headerTitle.getDrawable_id());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final ChildListModel childListModel = (ChildListModel)getChild(groupPosition,childPosition);

        //final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_submenu, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.submenu);
        ImageView imageCheck = (ImageView)convertView.findViewById(R.id.iv_check_submenu);

        txtListChild.setText(childListModel.getMenuName());

        if(childListModel.isSelected()){
            imageCheck.setVisibility(View.VISIBLE);
        }else{
            imageCheck.setVisibility(View.INVISIBLE);

        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
