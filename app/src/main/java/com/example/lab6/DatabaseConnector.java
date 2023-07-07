package com.example.lab6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseConnector {
    // database name
    private static final String DATABASE_NAME = "locations";
    private SQLiteDatabase database; // TS: to run SQL commands
    private DatabaseOpenHelper databaseOpenHelper; // TS: create or open the database

    // public constructor for DatabaseConnector
    public DatabaseConnector(Context context) {
        // create a new DatabaseOpenHelper
        databaseOpenHelper =
                new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
    } // end DatabaseConnector constructor

    // open the database connection
    public void open() throws SQLException {
        // create or open a database for reading/writing
        database = databaseOpenHelper.getWritableDatabase();//TS: at the first call, onCreate is called
    }

    // close the database connection
    public void close() {
        if (database != null)
            database.close(); // close the database connection
    }

    // inserts a new contact in the database
    public void insertRecord(String latitude,String longitude,String description) {
        ContentValues newRecord = new ContentValues();
        newRecord.put("latitude", latitude);
        newRecord.put("longitude", longitude);
        newRecord.put("description", description);


        open(); // open the database
        database.insert("locations", null, newRecord);
        close(); // close the database
    }

    // inserts a new contact in the database
    public void updateContact(long id, String name, String email,
                              String phone, String state, String city) {
        ContentValues editContact = new ContentValues();
        editContact.put("name", name);
        editContact.put("email", email);
        editContact.put("phone", phone);
        editContact.put("street", state);
        editContact.put("city", city);

        open(); // open the database
        database.update("contacts", editContact, "_id=" + id, null);
        close(); // close the database
    }

    // return a Cursor with all contact information in the database
    public Cursor getAllRecords() {
        //return database.query("contacts", new String[] {"_id", "name"},
        //	         null, null, null, null, "name"/*order by*/);
        return database.query("locations", new String[]{"_id", "latitude", "longitude","description"},
                null, null, null, null, "_id"/*order by*/);
    }
    public Cursor getSearchedRecords(String descTxt) {
        //return database.query("contacts", new String[] {"_id", "name"},
        //	         null, null, null, null, "name"/*order by*/);
        return database.rawQuery("SELECT * FROM locations WHERE description like  '%" + descTxt + "%'"  , null);
    }

    // get a Cursor containing all information about the contact specified
    // by the given id
    public Cursor getOneContact(long id) {

        return database.query("contacts", null/*get all fields*/,
                "_id=" + id /*selection*/, null, null, null, null);

        //TS: OR
        //return database.rawQuery("SELECT * FROM contacts WHERE _id = " + String.valueOf(id)  , null);

    }

    // delete the contact specified by the given String name
    public void deleteContact(long id) {
        open();
        database.delete("locations", "_id=" + id, null);
        /*OR*/ //database.delete("contacts", "_id = ?", new String[] {String.valueOf(id)});
        close();
    }

    //--TS: function that gets a database cursor and returns an array list of contacts
    public ArrayList<String> getAllContactsInAListOfStrings()
    {
        String rst = "";
        ArrayList<String> listOfContacts = new ArrayList<String>();

        open();
        Cursor c = database.query("contacts", null,null, null, null, null, "name"/*order by*/);

        if (c.moveToFirst()){
            do{

                rst += c.getString(1)+"\n";//name
                rst += c.getString(2)+"\n";//email
                rst += c.getString(3)+"\n";//phone
                rst += c.getString(4)+"\n";//street
                rst += c.getString(5)+"\n";//city
                rst += "--------------------\n";
                listOfContacts.add(rst);
                rst = "";

            }while(c.moveToNext());
        }

        close();
        return listOfContacts;
    }


    private class DatabaseOpenHelper extends SQLiteOpenHelper {
        // public constructor
        public DatabaseOpenHelper(Context context, String name,
                                  SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // creates the contacts table when the database is created
        // TS: this is called from  open()->getWritableDatabase(). Only if the database does not exist
        @Override
        public void onCreate(SQLiteDatabase db) {
            // query to create a new table named contacts
            String createQuery = "CREATE TABLE locations" +
                    "(_id integer primary key autoincrement," +
                    "latitude TEXT, longitude TEXT, description TEXT);";

            db.execSQL(createQuery); // execute the query
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
        }
    }
}
