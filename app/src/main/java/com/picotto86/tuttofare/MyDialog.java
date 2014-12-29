package com.picotto86.tuttofare;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileOutputStream;

/**
 * Created by picot_000 on 29/12/2014.
 */
public class MyDialog extends Dialog {


    public MyDialog(Context mContext, final ContactAdapter adapter, final int position) {
        super(mContext);

        ContactInfo item=adapter.getItem(position);



        this.setContentView(R.layout.dialog);

        final EditText txtip=(EditText) this.findViewById(R.id.text_ip);
        txtip.setText(item.ip);
        final EditText txtport=(EditText)this.findViewById(R.id.text_port);
        txtport.setText(item.port);
        final EditText txtcommand=(EditText)this.findViewById(R.id.text_command);
        txtcommand.setText(item.command);
        final EditText txtnome=(EditText)this.findViewById(R.id.text_title);
        txtnome.setText(item.title);

        this.show();
        Button ok=(Button)this.findViewById(R.id.buttonOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getItem(position).ip=txtip.getText().toString();
                adapter.getItem(position).port=txtport.getText().toString();
                adapter.getItem(position).command=txtcommand.getText().toString();
                adapter.getItem(position).title=txtnome.getText().toString();
                adapter.notifyDataSetChanged();

                try {
                    FileOutputStream outputStream = getContext().openFileOutput("data", Context.MODE_PRIVATE);
                    for (int i = 0; i < MainActivity.result.size(); i++) {

                        ContactInfo element = MainActivity.result.get(i);
                        String res = element.ip + ":" + element.port + ":" + element.command + ":" + element.title + "\n";
                        outputStream.write(res.getBytes());
                    }
                    outputStream.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }


                dismiss();
            }

        });
    }
}
