package com.pinoyseoul.talaarawan;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listview;
    private ArrayList arraylist;
    private ArrayAdapter arrayadapter;

    private String textField1String;
    private String textField2String;

    private Button noteFeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = findViewById(R.id.listView);
        arraylist = new ArrayList();
        arrayadapter = new ArrayAdapter<String>(this, R.layout.add_button, R.id.button, arraylist);
        listview.setAdapter(arrayadapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LinearLayout linLayout = new LinearLayout(MainActivity.this);
                linLayout.setOrientation(LinearLayout.VERTICAL);
                builder.setTitle("Remove Note");
                builder.setMessage("Would you like to remove this note?");
                builder.setView(linLayout);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        arraylist.remove(position);
                        arrayadapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                });

                builder.show();

            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();

        noteFeed = findViewById(R.id.noteFeedButton);

        noteFeed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "This is the Note Feed", Toast.LENGTH_SHORT).show();

            }



        });



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("notes");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String valueKey = dataSnapshot.getKey();
                String value = dataSnapshot.getValue(String.class);
                arrayadapter.add("TITLE: " + valueKey + "\n\n" + value);
                Log.d("test", "Value is: " + valueKey + " " + value);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu, menu);
        return  super.onCreateOptionsMenu(menu);

    }

    public boolean onOptionsItemSelected(MenuItem item){

        int getID = item.getItemId();
        if(getID == R.id.plusButton){


            clickedMethod();

           Log.d("testClick",
                   "Yes, you clicked the " + getID + " button");


        }else{

            Log.d("NOPE", "Nah, I don't know what that is");
        }

        return super.onOptionsItemSelected(item);


    }

    private void clickedMethod() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout linLayout = new LinearLayout(this);
        linLayout.setOrientation(LinearLayout.VERTICAL);
        builder.setTitle("Create New Note");
        builder.setView(linLayout);

        final EditText textField1 = new EditText(this);
        linLayout.addView(textField1);

        final EditText textField2 = new EditText(this);
        linLayout.addView(textField2);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                textField1String = textField1.getText().toString();
                textField2String = textField2.getText().toString();

                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                myRef.child("notes").child(textField1String).setValue(textField2String);

                arrayadapter.add(textField1String + textField2String);
                arrayadapter.notifyDataSetChanged();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();

    }

}
