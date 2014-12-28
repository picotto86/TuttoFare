package com.picotto86.tuttofare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<ContactInfo> {

    public ContactAdapter(Context context, int textViewResourceId,List<ContactInfo> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.card_layout, null);
        TextView ip = (TextView)convertView.findViewById(R.id.txtip);
        TextView porta = (TextView)convertView.findViewById(R.id.txtport);
        TextView command=(TextView)convertView.findViewById(R.id.txtcommand);
        TextView title=(TextView)convertView.findViewById(R.id.title);
        ContactInfo c = getItem(position);
        ip.setText(c.ip);
        porta.setText(c.port);
        command.setText(c.command);
        title.setText(c.title);
        return convertView;
    }
}