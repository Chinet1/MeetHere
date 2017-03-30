package com.chinet.meethere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Years;

public class UserActivity extends AppCompatActivity {

    private int userID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        DatabaseHandler db = new DatabaseHandler(this);

        User user = db.getUser(userID);

        String name = user.getName() + " " + user.getSurname();
        Integer age = calculateYears(user.getDayOfBirthday());
        String city = "City: " + user.getCity();
        Integer daysInApp = calculateDays(user.getCreatedAt());

        String ageString = "Age: " + age;
        String daysInAppString = "You are " + daysInApp + " days in here.";

        TextView nameText = (TextView)findViewById(R.id.nameText);
        nameText.setText(name);

        TextView ageText = (TextView)findViewById(R.id.ageText);
        ageText.setText(ageString);

        TextView cityText = (TextView)findViewById(R.id.cityText);
        cityText.setText(city);

        TextView diApp = (TextView)findViewById(R.id.diAppText);
        diApp.setText(daysInAppString);
    }

    public void goFriends(View view) {
        Intent intent = new Intent(this, FriendsActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    private Integer calculateYears(String dataFrom) {
        String[] parts = dataFrom.split(", ");
        LocalDate df = new LocalDate(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]));
        LocalDate now = new LocalDate();
        Years yearsIns = Years.yearsBetween(df, now);
        Integer years = yearsIns.getYears();
        return years;
    }

    private Integer calculateDays(String date) {
        String[] parts = date.split(", ");
        LocalDate dt = new LocalDate(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]));
        LocalDate now = new LocalDate();
        Days daysIns = Days.daysBetween(dt, now);
        Integer days = daysIns.getDays();
        return days;
    }
}