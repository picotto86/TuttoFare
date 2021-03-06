package com.picotto86.tuttofare;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class MainActivity extends Activity {

    static List<ContactInfo> result;
    ContactAdapter ca;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my);

        mContext=this;

        setContentView(R.layout.activity_main);
        ListView recList = (ListView) findViewById(R.id.list_view);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.attachToListView(recList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(mContext);

                dialog.setContentView(R.layout.dialog);

                Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Button buttonOk = (Button) dialog.findViewById(R.id.buttonOk);

                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ip = String.valueOf(((EditText) dialog.findViewById(R.id.text_ip)).getText());
                        String port = String.valueOf(((EditText) dialog.findViewById(R.id.text_port)).getText());
                        String command = String.valueOf(((EditText) dialog.findViewById(R.id.text_command)).getText());
                        String nome = String.valueOf(((EditText) dialog.findViewById(R.id.text_title)).getText());

                        ContactInfo ci = new ContactInfo();
                        ci.ip = ip;
                        ci.port = port;
                        ci.command = command;
                        ci.title = nome;

                        MainActivity.result.add(ci);

                        ca.notifyDataSetChanged();

                        dialog.dismiss();

                        salvaLista();
                    }
                });

                dialog.show();


            }
        });

        result = new ArrayList<ContactInfo>();

        try {
            InputStream is=openFileInput("data");

            InputStreamReader reader=new InputStreamReader(is);
            BufferedReader buff=new BufferedReader(reader);

            String received="";
            StringBuilder builder=new StringBuilder();
            try {
                while((received=buff.readLine())!=null){

                    builder.append(received);

                    StringTokenizer st=new StringTokenizer(received.toString(),":");

                    ContactInfo contact=new ContactInfo();

                    contact.ip=st.nextToken();
                    contact.port=st.nextToken();
                    contact.command=st.nextToken();
                    contact.title=st.nextToken();

                    MainActivity.result.add(contact);

                    received="";

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        ca = new ContactAdapter(this,R.layout.card_layout,result);
        recList.setAdapter(ca);
        registerForContextMenu(recList);

        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long id) {

                Log.d("D:", "toccato" + result.get(position).title);

                ContactInfo con=result.get(position);

                Log.d("D:","Elemento "+position);

                Intent nuovaPagina;
                //nuovaPagina = new Intent(getBaseContext(), Dettaglio.class);
                //nuovaPagina.putExtra("ip", con.ip);
                //nuovaPagina.putExtra("porta", con.port);
                //nuovaPagina.putExtra("comando", con.command);

                AsyncHttpTask task=new AsyncHttpTask(con);

                task.execute();


            }
        };
        recList.setOnItemClickListener(clickListener);

        AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view,
                                           int position, long id) {

                Log.d("D:","cliccato a lungo elemento "+position);

                return false;
            }
        };
        recList.setOnItemLongClickListener(longClickListener);

       /*recList.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Log.d("D:", "toccato" + result.get(position).title);

                        ContactInfo con=result.get(position);

                        Log.d("D:","Elemento "+position);

                        Intent nuovaPagina;
                        //nuovaPagina = new Intent(getBaseContext(), Dettaglio.class);
                        //nuovaPagina.putExtra("ip", con.ip);
                        //nuovaPagina.putExtra("porta", con.port);
                        //nuovaPagina.putExtra("comando", con.command);

                        AsyncHttpTask task=new AsyncHttpTask(con);

                        task.execute();

                    }

                })
        );*/

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.list_view) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(result.get(info.position).title);
            String[] menuItems = getResources().getStringArray(R.array.menu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 0:

                MyDialog myDialog=new MyDialog(mContext,ca,info.position);

                return true;
            case 1:
                ca.remove(ca.getItem(info.position));
                salvaLista();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void salvaLista(){

        try {
            FileOutputStream outputStream = openFileOutput("data", Context.MODE_PRIVATE);
            for (int i = 0; i < MainActivity.result.size(); i++) {

                ContactInfo element = MainActivity.result.get(i);
                String res = element.ip + ":" + element.port + ":" + element.command + ":" + element.title + "\n";
                outputStream.write(res.getBytes());
            }
            outputStream.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }



    }

    private List<ContactInfo> createList(int size) {

        result = new ArrayList<ContactInfo>();

        try {
            InputStream is=openFileInput("data");

            InputStreamReader reader=new InputStreamReader(is);
            BufferedReader buff=new BufferedReader(reader);

            String received="";
            StringBuilder builder=new StringBuilder();
            try {
                while((received=buff.readLine())!=null){

                    builder.append(received);

                    StringTokenizer st=new StringTokenizer(received.toString(),":");

                    ContactInfo contact=new ContactInfo();

                    contact.ip=st.nextToken();
                    contact.port=st.nextToken();
                    contact.command=st.nextToken();
                    contact.title=st.nextToken();

                    MainActivity.result.add(contact);

                    received="";

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /*for (int i=1; i <= size; i++) {
            ContactInfo ci = new ContactInfo();
            ci.name = ContactInfo.NAME_PREFIX + i;
            ci.surname = ContactInfo.SURNAME_PREFIX + i;
            ci.email = ContactInfo.EMAIL_PREFIX + i + "@test.com";

            result.add(ci);

        }
        */
        return result;
    }

    private class AsyncHttpTask extends AsyncTask<ContactInfo, Void, Integer> {

        ContactInfo contatto;

        String text;

        AsyncHttpTask(ContactInfo con){

            contatto=con;



        }


        @Override
        protected void onPreExecute() {

            Toast.makeText(getApplicationContext(), "Connessione....", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Integer doInBackground(ContactInfo... params) {


            Socket socket = null;

            try {
                socket = new Socket(contatto.ip, Integer.parseInt(contatto.port));

                Handler handler =  new Handler(getApplicationContext().getMainLooper());
                handler.post( new Runnable(){
                    public void run(){
                        Toast.makeText(getApplicationContext(), "Creata connessione",Toast.LENGTH_LONG).show();
                    }
                });


                Log.d("d:",contatto.command);




                BufferedWriter bufOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()) );

                bufOut.write( contatto.command );
                bufOut.newLine(); //HERE!!!!!!
                bufOut.flush();

                Log.d("D:", "ci sono");

                handler.post( new Runnable(){
                    public void run(){
                        Toast.makeText(getApplicationContext(), "Dati inviati \nIn attesa di risposta....",Toast.LENGTH_LONG).show();
                    }
                });

                BufferedReader bufIn = new BufferedReader( new InputStreamReader(socket.getInputStream() ) );

                text=bufIn.readLine();

                System.out.println("Ricevuto: "+text);

                handler.post( new Runnable(){
                    public void run(){
                        Toast.makeText(getApplicationContext(), "Risposta: "+text,Toast.LENGTH_LONG).show();
                    }
                });





            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {

        }
    }
}