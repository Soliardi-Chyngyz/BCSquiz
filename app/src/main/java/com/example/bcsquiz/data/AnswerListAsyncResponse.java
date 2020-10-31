package com.example.bcsquiz.data;

import com.example.bcsquiz.model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinished(ArrayList<Question> questionArrayList);
}
