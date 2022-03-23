package com.example.topquizz.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class QuestionBank implements Serializable {
    private List<Question> mQuestionList;
    private int mQuestionIndex;

    public QuestionBank(List<Question> mQuestionList){
        this.mQuestionList = mQuestionList;

        Collections.shuffle(this.mQuestionList);
    }

    public Question getCurrentQuestion(){
        return this.mQuestionList.get(mQuestionIndex);
    }

    public Question getNextQuestion(){
        mQuestionIndex++;
        return getCurrentQuestion();
    }
}
