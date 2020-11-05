package com.example.bcsquiz.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.bcsquiz.controller.AppController;
import com.example.bcsquiz.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    ArrayList<Question> questionArrayList = new ArrayList<>();
    private String url = "https://raw.githubusercontent.com/Soliardi-Chyngyz/BCSquiz/master/bsc-data.json";

    public List<Question> getQuestions(final AnswerListAsyncResponse callBack) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                (JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Question question = new Question();
                                question.setQuestion(response.getJSONArray(i).get(0).toString());
                                question.setAnswerTrue(response.getJSONArray(i).get(1).toString());
                                question.setV1(response.getJSONArray(i).get(2).toString());
                                question.setV2(response.getJSONArray(i).get(3).toString());
                                question.setV3(response.getJSONArray(i).get(4).toString());
                                question.setV4(response.getJSONArray(i).get(5).toString());

                                // add question objects to list
                                questionArrayList.add(question);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (null != callBack)
                            callBack.processFinished(questionArrayList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("anime", error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }


}
