package com.bndiapps.citysphere;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andrei on 8/26/16.
 */
public class Poll implements Serializable{
    private String id;
    private String title;
    private List<String> questions = new ArrayList<>();


    public static List<Poll> fromMap(Map map) {
        ArrayList<Poll> list = new ArrayList<>();
        for (Object key : map.keySet()) {
            HashMap pollValues = (HashMap) map.get(key);
            String title = (String) pollValues.get("title");
            List<String> questions = (List<String>) pollValues.get("questions");
            Poll poll = new Poll((String) key, title, questions);
            list.add(poll);
        }
        return list;
    }

    public Poll(String id, String title, List<String> questions) {
        this.id = id;
        this.title = title;
        this.questions = questions;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Poll{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", questions=" + questions +
                '}';
    }
}
