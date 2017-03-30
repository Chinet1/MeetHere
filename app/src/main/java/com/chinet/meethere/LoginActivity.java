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
        db.addUser(new User("Ravi", "Haris", "ravi@example.com", "Los Angels", "1985, 2, 17", null));
        db.addUser(new User("Srinivas", "Paul", "paul@example.com", "New York", "2001, 4, 30", null));
        db.addUser(new User("Smith", "Mark", "Mark@example.com", "New London", "1994, 9, 25", null));

        Log.d("Reading: ", "Reading all users..");
        List<User> users = db.getAllUsers();

        for (User usr : users) {
            String log = "Id: "+usr.getId()+", Name: " + usr.getName() + ", Surname: " + usr.getSurname()
                    + ", Email: " + usr.getEmail() ;
            Log.d("Name: ", log);
        }

        Log.d("Making friends:", "Adding friends");
        db.makeFriend(1, 2);
        db.makeFriend(2, 1);
        db.makeFriend(2, 3);
        db.makeFriend(3, 2);

        Log.d("Making friends:", "Get All Firends");
        List<User> friends = db.getAllFriends(2);

        for (User usr : friends) {
            String log = "Id: "+usr.getId()+", Name: " + usr.getName() + ", Surname: " + usr.getSurname();
            Log.d("Friend: ", log);
        }

        db.closeDB();
    }
}