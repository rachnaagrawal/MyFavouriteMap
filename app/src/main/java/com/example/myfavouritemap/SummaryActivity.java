package com.example.myfavouritemap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

public class SummaryActivity extends AppCompatActivity {

    public Button back_btn;
    public Button report_pothole;
    private static final int ALERT_DIALOG = 1;

    TextView tv1,tv2,tv3,tv4,tv5;
    ImageView image_preview;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        back_btn = findViewById(R.id.back_button);
        tv1=(TextView) findViewById(R.id.tv1);
        tv2=(TextView) findViewById(R.id.tv2);
        tv3=(TextView) findViewById(R.id.tv3);
        tv4=(TextView) findViewById(R.id.tv4);
        tv5=(TextView) findViewById(R.id.tv5);

        image_preview=(ImageView)findViewById(R.id.image_preview);

        Intent i=getIntent();
        String location= i.getStringExtra("location");
        String tolocation= i.getStringExtra("tolocation");
        String severity= i.getStringExtra("severity");
        String description= i.getStringExtra("description");
        String namee= i.getStringExtra("namee");
        tv1.setText(location);
        tv2.setText(tolocation);
        tv3.setText(severity);
        tv4.setText(description);
        tv5.setText(namee);

        String ur=i.getStringExtra("img");



        new DownloadImageFromInternet((ImageView) findViewById(R.id.image_preview))
                .execute(ur);




        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SummaryActivity.this, PhotoDetailActivity.class);
                startActivity(i);
            }
        });

        report_pothole = findViewById(R.id.Report_button);
        report_pothole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t=Toast.makeText(getApplicationContext(),"Spothole Reported Successfully !!",Toast.LENGTH_SHORT);
                t.show();
                Intent i=new Intent(SummaryActivity.this,PhotoActivity.class);
                startActivity(i);
            }

        });
    }


    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }



}