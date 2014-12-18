package com.picotto86.tuttofare;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<ContactInfo> contactList;

    public ContactAdapter(List<ContactInfo> contactList) {
        this.contactList = contactList;
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        ContactInfo ci = contactList.get(i);
        contactViewHolder.vIp.setText(ci.ip);
        contactViewHolder.vCommand.setText(ci.command);
        contactViewHolder.vPort.setText(ci.port);
        contactViewHolder.vTitle.setText(ci.title);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView vIp;
        protected TextView vCommand;
        protected TextView vPort;
        protected TextView vTitle;

        public ContactViewHolder(View v) {
            super(v);
            vIp =  (TextView) v.findViewById(R.id.txtip);
            vCommand = (TextView)  v.findViewById(R.id.txtcommand);
            vPort = (TextView)  v.findViewById(R.id.txtport);
            vTitle = (TextView) v.findViewById(R.id.title);
        }
    }
}