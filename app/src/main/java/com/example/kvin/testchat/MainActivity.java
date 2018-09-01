package com.example.kvin.testchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.provider.Settings.Secure;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("chat");

    ArrayList<String> messages = new ArrayList();
    ArrayAdapter<String> adapter;

    private ListView mMessagesList;
    private Button mSendBut;
    private String mAndroidId;
    private EditText messageEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAndroidId = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);

        messageEditText = findViewById(R.id.textView);
        mMessagesList = findViewById(R.id.messageList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messages);
        mMessagesList.setAdapter(adapter);
        mSendBut = findViewById(R.id.button_send);
        mSendBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(view);
            }
        });


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey() != mAndroidId) {
                    String msg = dataSnapshot.getValue(String.class);
                    adapter.add(new MyMessage("Interlocutor", msg).toString());
                    adapter.notifyDataSetChanged();
                } else {
                    String msg = dataSnapshot.getValue(String.class);
                    adapter.add(new MyMessage("Me", msg).toString());
                    adapter.notifyDataSetChanged();
                }
            }



            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStop(){
        myRef.child(mAndroidId).setValue("Disconnected from chat");
        super.onStop();
    }
    public void add(View v){

        String msg = messageEditText.getText().toString();
        if(!msg.isEmpty()){
            myRef.child(mAndroidId).setValue(msg,  new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Toast.makeText(getApplicationContext(),"Message could not be sent " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    messageEditText.setText("");
                     // code for changing data on database, that help to send same messages

        }
            });
        }
    }
}
