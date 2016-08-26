package com.bndiapps.citysphere;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrei on 8/26/16.
 */
public class PollRVAdapter extends RecyclerView.Adapter<PollViewHolder> {
    private List<String> questions = new ArrayList<>();

    public PollRVAdapter(List<String> questions) {
        this.questions = questions;
    }

    @Override
    public PollViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.poll_layout, parent, false);
        PollViewHolder pvh = new PollViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(PollViewHolder holder, int position) {
        holder.setQuestion(questions.get(position));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}
