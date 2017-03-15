package com.chinet.meethere;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DatabaseHandler db = new DatabaseHandler(this);

        /**
         * CRUD Operations
         * */
        // Inserting Contacts
        Log.d("Insert: ", "Inserting ..");
        db.addUser(new User("Ravi", "Haris", "ravi@example.com", "Los Angels", "10.05.1985"));
        db.addUser(new User("Srinivas", "Paul", "paul@example.com", "Beth", "7.09.2001"));

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<User> users = db.getAllUsers();

        for (User cn : users) {
            String log = "Id: "+cn.getId()+" ,Name: " + cn.getName() + ",Surname: " + cn.getSurname()
                    + ", Email: " + cn.getEmail();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
        }
}