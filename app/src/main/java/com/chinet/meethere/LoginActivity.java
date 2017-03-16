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

        Log.d("Insert: ", "Inserting ..");
        db.addUser(new User("Ravi", "Haris", "ravi@example.com", "Los Angels", "10.05.1985"));
        db.addUser(new User("Srinivas", "Paul", "paul@example.com", "New York", "7.09.2001"));

        Log.d("Reading: ", "Reading all users..");
        List<User> users = db.getAllUsers();

        for (User usr : users) {
            String log = "Id: "+usr.getId()+" ,Name: " + usr.getName() + ",Surname: " + usr.getSurname()
                    + ", Email: " + usr.getEmail() ;
            Log.d("Name: ", log);
        }
    }
}