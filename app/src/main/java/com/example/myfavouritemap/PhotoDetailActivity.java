package com.example.myfavouritemap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class PhotoDetailActivity extends AppCompatActivity {


    public String x;
    public Button next_button;
    public Button back_btn;

    public String y;
    DatabaseReference rootRef,demoRef;

    public String location;
    public String tolocation;
    public String severity;
    public String description;
    public String namee;

    TextInputEditText editTextLocation,editTextRemark;
    TextInputEditText editTextName;
    MaterialBetterSpinner Spinner_type_of_location;
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        String[] SPINNERLIST = {"Middle of Road", "Sides of Road", "FootPath", "Crosswalks"};

        editTextLocation=(TextInputEditText) findViewById(R.id.editTextLocation);
        Spinner_type_of_location=(MaterialBetterSpinner)findViewById(R.id.Spinner_type_of_location);
        rg= (RadioGroup) findViewById(R.id.rg);
        editTextRemark=(TextInputEditText)findViewById(R.id.editTextRemark);
        editTextName=(TextInputEditText) findViewById(R.id.editTextName);

        rootRef = FirebaseDatabase.getInstance().getReference();

//database reference pointing to demo node
        demoRef = rootRef.child("2_Pothole_Detail");



        MaterialBetterSpinner location_type_spinner = findViewById(R.id.Spinner_type_of_location);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
// Apply the adapter to the spinner
        location_type_spinner.setAdapter(adapter);

        next_button=findViewById(R.id.next_button);




        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





              location=editTextLocation.getText().toString();


                Spinner_type_of_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        x=parent.getItemAtPosition(position).toString();

                        Toast.makeText(getApplicationContext(),x,Toast.LENGTH_LONG).show();



                    }
                });


              tolocation=x;

              int idd=rg.getCheckedRadioButtonId();
              RadioButton button=findViewById(idd);

              severity=button.getText().toString();



              description=editTextRemark.getText().toString();

              namee=editTextName.getText().toString();

                Intent ii=getIntent();
                y=ii.getStringExtra("URI");
                SharedPreferences pref = getApplicationContext().getSharedPreferences("My", 0); // 0 - for private mode
                String ur=pref.getString("URI", null);

                Const_Pothole_Detail const_pothole_detail=new Const_Pothole_Detail(location,tolocation,severity,description,namee,ur);
                demoRef.push().setValue(const_pothole_detail);
                Toast t=Toast.makeText(getApplicationContext(),"saved successfully",Toast.LENGTH_SHORT);
                t.show();




                Intent i=new Intent(PhotoDetailActivity.this, SummaryActivity.class);

                i.putExtra("location", location);
                i.putExtra("tolocation", tolocation);
                i.putExtra("severity", severity);
                i.putExtra("description", description);
                i.putExtra("namee", namee);
                i.putExtra("img",ur);

                startActivity(i);
            }
        });

        back_btn = findViewById(R.id.back_button);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(PhotoDetailActivity.this,PhotoActivity.class);
                startActivity(i);
            }
        });
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.severity_high:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.severity_medium:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.severity_low:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }
}
