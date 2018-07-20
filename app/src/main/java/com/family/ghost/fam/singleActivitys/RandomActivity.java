package com.family.ghost.fam.singleActivitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.family.ghost.fam.R;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class RandomActivity extends AppCompatActivity {


   // private static ArrayList<String> femNames;
    private static ArrayList<String> maleNames;
    private static ArrayList<String> lastNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);


        Toolbar toolbar = findViewById(R.id.toolbar_random);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



        try {
            //loadFemNames();
            loadMaleNames();
            loadLastNames();
        } catch (IOException e) {
            //  Auto-generated catch block
            e.printStackTrace();
        }

        // Set the Male Button
        Button left = (Button) findViewById(R.id.maleButton);
        left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //String femName = getFemName();
                TextView butt = (TextView) findViewById(R.id.nameView);
                String maleName = getMaleName();
                String lastName = getLastName();
                String finalName = maleName + " " + lastName;
                butt.setText(finalName);
            }
        });

//        Button right = (Button) findViewById(R.id.femaleButton);
//        right.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                TextView butt = (TextView) findViewById(R.id.nameView);
//               // String femName = getFemName();
//                String lastName = getLastName();
//               // String name = femName + " " + lastName;
//              //  butt.setText(name);
//
//            }
//        });


    }



//    private void loadFemNames() throws IOException {
//        femNames = new ArrayList<String>();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("Female First Names.txt")));
//        String line;
//        while ((line = reader.readLine()) != null) {
//            femNames.add(line);
//        }
//        reader.close();
//    }

    private void loadMaleNames() throws IOException {
        maleNames = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("male_names.txt")));
        String line;
        while ((line = reader.readLine()) != null) {
            maleNames.add(line);
        }
        reader.close();
    }

    private void loadLastNames() throws IOException {
        lastNames = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("Last Names.txt")));
        String line;
        while ((line = reader.readLine()) != null) {
            lastNames.add(line);
        }
        reader.close();
    }




//
//    private String getFemName() {
//        Random r = new Random();
//        int i = r.nextInt(femNames.size());
//        return femNames.get(i);
//    }

    private String getMaleName() {
        Random r = new Random();
        int i = r.nextInt(maleNames.size());
        return maleNames.get(i);
    }

    private String getLastName() {
        Random r = new Random();
        int i = r.nextInt(lastNames.size());
        return lastNames.get(i);
    }

}