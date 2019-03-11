package com.example.demo.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.demo.Adapter.HomepageAdapter;
import com.example.demo.Database.ChatDBHelper;
import com.example.demo.Database.ChatDBUtility;
import com.example.demo.Model.DataRecords;
import com.example.demo.R;

import java.util.ArrayList;

public class HomepageActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText search_text;
    ImageView remove_search;
    FloatingActionButton add_data;



    ChatDBHelper chatDBHelper;
    ChatDBUtility chatDBUtility;

    private RecyclerView.LayoutManager layoutManager;
    HomepageAdapter homepageAdapter;


    ArrayList<DataRecords> dataRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        chatDBUtility = new ChatDBUtility();
        chatDBHelper = chatDBUtility.CreateChatDB(HomepageActivity.this);

        initializeView();
        initializeListener();
        setData();


    }

    private void setData() {
        dataRecords=new ArrayList<DataRecords>();
        dataRecords=chatDBUtility.GetDataList(chatDBHelper);
        layoutManager = new LinearLayoutManager(HomepageActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        homepageAdapter = new HomepageAdapter(HomepageActivity.this, dataRecords);
        recyclerView.setAdapter(homepageAdapter);
        recyclerView.setItemViewCacheSize(dataRecords.size());

    }

    private void initializeListener() {
        remove_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                search_text.setText("");
                search_text.setCursorVisible(false);

            }
        });

        search_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_text.setCursorVisible(true);
            }
        });

        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (homepageAdapter != null)  {
                    homepageAdapter.getFilter().filter(search_text.getText().toString());

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(HomepageActivity.this,AddDataActivity.class);
                startActivity(intent);

            }
        });


    }

    private void initializeView() {

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        search_text=(EditText)findViewById(R.id.search_text);
        remove_search=(ImageView)findViewById(R.id.close_icon);
        add_data=(FloatingActionButton)findViewById(R.id.addData);

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        setData();
    }
}
