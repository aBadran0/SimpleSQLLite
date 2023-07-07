package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText latitudeEditText,longiEditText,editTextDesc,editTextSearch;
    Button buttonAdd,buttonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeEditText = findViewById(R.id.latitudeEditText);
        longiEditText = findViewById(R.id.longiEditText);
        editTextDesc = findViewById(R.id.editTextDesc);
        editTextSearch = findViewById(R.id.editTextSearch);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSearch = findViewById(R.id.buttonSearch);

        buttonAdd.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonAdd)
        {
            String latitude = latitudeEditText.getText()+"";
            String longitude=  longiEditText.getText()+"";
            String description = editTextDesc.getText()+"";
            DatabaseConnector db = new DatabaseConnector(this);
            db.insertRecord(latitude,longitude,description);
            Toast.makeText(this,"Record Added",Toast.LENGTH_LONG).show();
            latitudeEditText.setText("");
            longiEditText.setText("");
            editTextDesc.setText("");
        }
        else if(v.getId()==R.id.buttonSearch)
        {
            Intent intent =
                    new Intent(this, ViewDatabase.class);
            intent.putExtra("search",editTextSearch.getText()+"");
            startActivity(intent); // start the AddEditContact Activity
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // handle choice from options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //create a new Intent to launch the AddEditContact Activity
        Intent intent =
                new Intent(this, ViewDatabase.class);
        startActivity(intent); // start the AddEditContact Activity
        return super.onOptionsItemSelected(item); // call super's method
    }
}