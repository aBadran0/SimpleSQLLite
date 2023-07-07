package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ViewDatabase extends ListActivity {

    String search;
    public static final String ROW_ID = "row_id"; // Intent extra key
    private ListView recordListView; // the ListActivity's ListView
    private CursorAdapter recordAdapter; // adapter for ListView
    private boolean searchbtnclicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TS: 1. create list view and set it event handler
        recordListView = getListView(); // Get the activity's list view widget
      recordListView.setOnItemClickListener(viewRecordListener);
        search = getIntent().getStringExtra("search");
        String[] from = new String[] { "latitude", "longitude", "description" };
        int[] to = new int[] { R.id.textViewLatiList, R.id.textViewLongiList,R.id.textViewDescList };//from contact_list_item.xml

        recordAdapter = new SimpleCursorAdapter(
                this, R.layout.listview_item, null, from, to, 0); //ts: code update to include flag

        //TS: 3. link the cursor to the list view
        setListAdapter(recordAdapter); // set contactView's adapter
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(search==null)
        {
            searchbtnclicked = false;
            GetRecordsTask();

        }
        else {
            searchbtnclicked = true;
            GetSearchedRecordTask();
        }


    }

    private void GetRecordsTask ()
    {
        DatabaseConnector databaseConnector =
                new DatabaseConnector(this);

        databaseConnector.open();
        // get a cursor containing call contacts
        Cursor result = databaseConnector.getAllRecords();
        recordAdapter.changeCursor(result); // set the adapter's Cursor
        databaseConnector.close();
    }
    private void GetSearchedRecordTask ()
    {
        DatabaseConnector databaseConnector =
                new DatabaseConnector(this);

        databaseConnector.open();
        // get a cursor containing call contacts
        Cursor result = databaseConnector.getSearchedRecords(search);
        recordAdapter.changeCursor(result); // set the adapter's Cursor
        databaseConnector.close();
    }


    AdapterView.OnItemClickListener viewRecordListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int pos,long id)
        {
            if(!searchbtnclicked) {
                // create a new AlertDialog Builder
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(ViewDatabase.this);
                TextView c = (TextView) arg1.findViewById(R.id.textViewDescList);
                String msg = c.getText().toString();
                builder.setTitle("Delete"); // title bar string
                builder.setMessage("Do you want to delete \"" + msg + "\"  ?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseConnector db = new DatabaseConnector(ViewDatabase.this);
                        db.deleteContact(id);
                        GetRecordsTask();

                    }
                });
                builder.setNegativeButton("Cancel",null);

                builder.show(); // display the Dialog
            }
            else {
                Toast.makeText(ViewDatabase.this,"Can not Delete",Toast.LENGTH_LONG).show();
            }


        } // end method onItemClick
    };
}