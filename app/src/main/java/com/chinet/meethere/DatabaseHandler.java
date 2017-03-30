package com.chinet.meethere;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHandler";

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "userManager";

    private static final String TABLE_USERS = "users";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SURNAME = "surname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CITY = "city";
    private static final String KEY_DAYOFBIRTHDAY = "dayOfBirthday";

    private static final String TABLE_FRIENDS = "friends";

    private static final String KEY_FRIEND1 = "friend1";
    private static final String KEY_FRIEND2 = "friend2";
    private static final String KEY_CREATED_AT = "createdAt";

    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
            + KEY_SURNAME + " TEXT," + KEY_EMAIL + " TEXT," + KEY_CITY + " TEXT,"
            + KEY_DAYOFBIRTHDAY + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_FRIENDS_TABLE = "CREATE TABLE " + TABLE_FRIENDS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FRIEND1 + " INTEGER,"
            + KEY_FRIEND2 + " INTEGER," + KEY_CREATED_AT + " DATETIME" + ")";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_FRIENDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
        onCreate(sqLiteDatabase);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_SURNAME, user.getSurname());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_CITY, user.getCity());
        values.put(KEY_DAYOFBIRTHDAY, user.getDayOfBirthday());
        values.put(KEY_CREATED_AT, getCreatedAtDate());

        db.insert(TABLE_USERS, null, values);
    }

    public String getCreatedAtDate() {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
        String createdAtDate = format.format(now);
        return createdAtDate;
    }

    public void makeFriend(long id, long friendId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FRIEND1, id);
        values.put(KEY_FRIEND2, friendId);
        values.put(KEY_CREATED_AT, getCreatedAtDate());

        db.insert(TABLE_FRIENDS, null, values);
    }

    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID,
            KEY_NAME, KEY_SURNAME, KEY_EMAIL, KEY_CITY, KEY_DAYOFBIRTHDAY, KEY_CREATED_AT}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
           User user = new User(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5), cursor.getString(6));
            return user;
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<User>();

        String selectQuery = "SELECT * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setName(cursor.getString(1));
                user.setSurname(cursor.getString(2));
                user.setEmail(cursor.getString(3));
                user.setCity(cursor.getString(4));
                user.setDayOfBirthday(cursor.getString(5));
                user.setCreatedAt(cursor.getString(6));

                userList.add(user);
            } while (cursor.moveToNext());
        }
        return userList;
    }

    public List<User> getAllFriends(int id) {
        List<User> friendsList = new ArrayList<User>();

        String selectQuery = "SELECT * FROM " + TABLE_USERS + " tu, " + TABLE_FRIENDS
                + " tf WHERE tf." + KEY_FRIEND1 + " = " + id + " AND tu."
                + KEY_ID + " = tf." + KEY_FRIEND2;

        Log.d(LOG, selectQuery);

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                user.setSurname(cursor.getString(cursor.getColumnIndex(KEY_SURNAME)));

                friendsList.add(user);
            } while (cursor.moveToNext());
        }
        return friendsList;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_SURNAME, user.getSurname());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_CITY, user.getCity());
        values.put(KEY_DAYOFBIRTHDAY, user.getDayOfBirthday());

        return db.update(TABLE_USERS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
    }

    public void deleteFriend(int id, int friendId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String findId = "SELECT " + KEY_ID + " FROM " + TABLE_FRIENDS + " WHERE "
                + KEY_FRIEND1 + " = " + id + " AND " + KEY_FRIEND2 + " = " + friendId;
        Cursor cursor = db.rawQuery(findId, null);

        if (cursor != null)
            cursor.moveToFirst();
        db.delete(TABLE_FRIENDS, KEY_ID + " = ?",
                new String[] {String.valueOf(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID))
                )});
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}