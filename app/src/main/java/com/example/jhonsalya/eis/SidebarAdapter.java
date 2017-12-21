package com.example.jhonsalya.eis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jhonsalya.eis.Constant;
import com.example.jhonsalya.eis.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jhonsalya on 12/20/17.
 */

public class SidebarAdapter extends BaseExpandableListAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> listParent;
    private HashMap<String, List<String>> listChild;

    public SidebarAdapter(Context context, List<String> listParent, HashMap<String, List<String>> listChild) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listParent = listParent;
        this.listChild = listChild;
    }

    @Override
    public int getGroupCount() {
        return this.listParent.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int childCount = 0; // if group don't have child, return 0
        if (groupPosition == Constant.S_POS_SORT) {
            // if group have child, return size of child
            childCount = this.listChild.get(this.listParent.get(groupPosition))
                    .size();
        }
        if (groupPosition == Constant.S_POS_MANAGE) {
            // if group have child, return size of child
            childCount = this.listChild.get(this.listParent.get(groupPosition))
                    .size();
        }
        return childCount;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listParent.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listChild.get(this.listParent.get(groupPosition))
                .get(childPosition);
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

        ParentHolder holder;
        String parentTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_sidebar_parent, null);
            holder = new ParentHolder();
            holder.parentTitle = (TextView) convertView.findViewById(R.id.parent_text);
            holder.parentImage = (ImageView) convertView.findViewById(R.id.parent_img);
            holder.parentIcon = (ImageView) convertView.findViewById(R.id.parent_icon);
            convertView.setTag(holder);
        } else {
            holder = (ParentHolder) convertView.getTag();
        }

        holder.parentTitle.setText(parentTitle);

        //Create Icon of each group

        if (groupPosition == 0){ //Account
            holder.parentIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person_black_24dp));
        }
        if (groupPosition == 1){ //Category
            holder.parentIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_view_agenda_black_24dp));
        }
        if (groupPosition == 2){ //Sort
            holder.parentIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_sort_black_24dp));
        }
        if (groupPosition == 3){ //Manage
            holder.parentIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_menu_manage));
        }
        if (groupPosition == 4){ //Logout
            holder.parentIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_exit_to_app_black_24dp));
        }

        //Create arrow if group have child
        if (getChildrenCount(groupPosition) != 0) { // if group have child
            if (isExpanded)
                holder.parentImage.setImageDrawable(context.getResources().getDrawable(R.mipmap.arrow_below));
            else if(!isExpanded)
                holder.parentImage.setImageDrawable(context.getResources().getDrawable(R.mipmap.arrow_right));
        }
        else
        { // if group don't have child
            holder.parentImage.setVisibility(View.GONE); // remove arrow icon
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ChildHolder holder;
        String childTitle = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_sidebar_child, null);
            holder = new ChildHolder();
            holder.childTitle = (TextView) convertView.findViewById(R.id.child_text);
            holder.childIcon = (ImageView) convertView.findViewById(R.id.child_icon);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }

        holder.childTitle.setText(childTitle);

        //icon for child (sort alphabetically
        if(groupPosition == 2 && childPosition == 0){
            holder.childIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_sort_by_alpha_black_24dp));
        }//sort by time
        if(groupPosition == 2 && childPosition == 1){
            holder.childIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_access_time_black_24dp));
        }//add event
        if(groupPosition == 3 && childPosition == 0){
            holder.childIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_circle_outline_black_24dp));
        }//add category
        if(groupPosition == 3 && childPosition == 1){
            holder.childIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_circle_outline_black_24dp));
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ParentHolder {
        private TextView parentTitle;
        private ImageView parentImage;
        private ImageView parentIcon;
    }

    static class ChildHolder {
        private TextView childTitle;
        private ImageView childIcon;
    }
}
