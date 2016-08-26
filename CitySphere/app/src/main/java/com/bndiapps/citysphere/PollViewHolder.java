package com.bndiapps.citysphere;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by andrei on 8/26/16.
 */
public class PollViewHolder extends RecyclerView.ViewHolder {

    private TextView questionTV;

    public PollViewHolder(View itemView) {
        super(itemView);

        questionTV = (TextView) itemView.findViewById(R.id.poll_question);
    }

    public void setQuestion(String question) {
        questionTV.setText(question);
    }

    public String getQuestion() {
        return (String) questionTV.getText();
    }
}
