package com.family.ghost.fam.singleActivitys;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.family.ghost.fam.R;

import java.util.ArrayList;
import java.util.List;





import com.family.ghost.fam.R;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;

public class PayActivity extends AppCompatActivity {

    private static final String[][] DATA_TO_SHOW = { { "This", "is", "a", "test" },
            { "and", "a", "second", "test" } };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pay);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


//        TableView tableView = (TableView) findViewById(R.id.tableView);



        TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.tableView);
        //tableView.setColumnCount(4);
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, DATA_TO_SHOW));

    }
}
