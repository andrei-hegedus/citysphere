package com.bndiapps.citysphere;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrei on 8/26/16.
 */
public class PollActivity extends Activity {

    private TextView pollTitle;
    private RecyclerView pollQuestionsRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        pollTitle = (TextView) findViewById(R.id.pollTitle);
        pollQuestionsRV = (RecyclerView) findViewById(R.id.pollQuestionsRV);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        pollQuestionsRV.setLayoutManager(llm);

        List<String> questions = new ArrayList<>();
        questions.add("The First Question");
        PollRVAdapter pollRVAdapter = new PollRVAdapter(questions);
        pollQuestionsRV.setAdapter(pollRVAdapter);
    }
}
