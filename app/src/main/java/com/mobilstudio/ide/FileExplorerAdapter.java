package com.mobilstudio.ide;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FileExplorerAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<ExplorerItem> items;

    public FileExplorerAdapter(Context context, ArrayList<ExplorerItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView icon;
        TextView name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = LayoutInflater.from(context)
                    .inflate(android.R.layout.activity_list_item, parent, false);

            holder = new ViewHolder();

            holder.icon = convertView.findViewById(android.R.id.icon);
            holder.name = convertView.findViewById(android.R.id.text1);

            holder.name.setTextColor(Color.parseColor("#111827"));
            holder.name.setTextSize(16);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        ExplorerItem item = items.get(position);

        holder.name.setText(item.getName());

        if (item.isFolder()) {
            holder.icon.setImageResource(android.R.drawable.ic_menu_agenda);
        } else {

            String name = item.getName().toLowerCase();

            if (name.endsWith(".java")) {
                holder.icon.setImageResource(android.R.drawable.ic_menu_edit);

            } else if (name.endsWith(".xml")) {
                holder.icon.setImageResource(android.R.drawable.ic_menu_view);

            } else if (name.endsWith(".gradle")) {
                holder.icon.setImageResource(android.R.drawable.ic_menu_manage);

            } else if (name.endsWith(".kt")) {
                holder.icon.setImageResource(android.R.drawable.ic_menu_edit);

            } else {
                holder.icon.setImageResource(android.R.drawable.ic_menu_save);
            }

        }

        return convertView;
    }

}
