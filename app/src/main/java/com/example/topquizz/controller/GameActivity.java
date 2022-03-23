package com.example.topquizz.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topquizz.R;
import com.example.topquizz.model.Question;
import com.example.topquizz.model.QuestionBank;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView mGameTextView;
    public Button mGameButton1;
    public Button mGameButton2;
    public Button mGameButton3;
    public Button mGameButton4;

    // bank de questions
    public QuestionBank mQuestionBank = generateQuestions();

    // question actuelle
    public Question mCurrentQuestion;

    // Compteur pour savoir quand arreter le jeu
    private int mRemainingQuestionCount;

    // enregistrer le score du joeur
    private int mScore;

    // clé pour identifier le score qui sera passé à l'activité principal
    public static final String BUNDLE_EXTRA_CODE = "BUNDLE_EXTRA_CODE";

    // variable boolean permettant de controler l'activation et désactivation de boutons
    private boolean mEnableTouchEvents;

    public static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE";
    public static final String BUNDLE_STATE_QUESTION = "BUNDLE_STATE_QUESTION";

    // méthode permettant de controler l'activation et désactivation de boutons
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    // sauvegarde des données dans le Bundle afin de les utilisées plus tard
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTION, mRemainingQuestionCount);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGameTextView = findViewById(R.id.game_activity_textview_question);
        mGameButton1 = findViewById(R.id.game_activity_button_1);
        mGameButton2 = findViewById(R.id.game_activity_button_2);
        mGameButton3 = findViewById(R.id.game_activity_button_3);
        mGameButton4 = findViewById(R.id.game_activity_button_4);

        mGameButton1.setOnClickListener(this);
        mGameButton2.setOnClickListener(this);
        mGameButton3.setOnClickListener(this);
        mGameButton4.setOnClickListener(this);

        mCurrentQuestion = mQuestionBank.getCurrentQuestion();
        displayQuestion(mCurrentQuestion);

        // activation des boutons lors du lancement du jeu
        mEnableTouchEvents = true;

        if(savedInstanceState != null){
            mRemainingQuestionCount = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
        }
        else{
            mRemainingQuestionCount = 4;
            mScore = 0;
        }
    }

    // récupération d'une question
    private void displayQuestion(final Question question){
        mGameTextView.setText(question.getQuestion());
        mGameButton1.setText(question.getChoiceList().get(0));
        mGameButton2.setText(question.getChoiceList().get(1));
        mGameButton3.setText(question.getChoiceList().get(2));
        mGameButton4.setText(question.getChoiceList().get(3));
    }

    // Bank des questions
    private QuestionBank generateQuestions(){
        Question question1 = new Question(
                "Quelle est la capitale de la Turquie ?",
                Arrays.asList(
                        "Ankara",
                        "Istanbul",
                        "Antalya",
                        "Trabzon"
                ),
                0
        );

        Question question2 = new Question(
                "Quel pays africain portait autrefois le nom de Rhodésie Brittanique ?",
                Arrays.asList(
                        "Zambie",
                        "Ghana",
                        "Afrique du sud",
                        "Zimbabwe"
                ),
                3
        );

        Question question3 = new Question(
                "Quel est le plus grand état des États-Unis ?",
                Arrays.asList(
                        "Floride",
                        "Alaska",
                        "Californie",
                        "Texas"
                ),
                1
        );

        Question question4 = new Question(
                "Qui a été le premier président des États-Unis ?",
                Arrays.asList(
                        "JFK",
                        "Ronald Reagan",
                        "Georges Washington",
                        "Abraham Lincoln"
                ),
                2
        );

        Question question5 = new Question(
                "Quel roi de France s'appelait le Roi Soleil ?",
                Arrays.asList(
                        "Louis XIV",
                        "Louis XV",
                        "Louis XVI",
                        "Louis XVIII"
                ),
                1
        );

        return new QuestionBank(
                Arrays.asList(
                question1, question2, question3, question4, question5
        ));
    }

    @Override
    public void onClick(View view) {
        int index;

        if(view == mGameButton1){
            index = 0;
        }
        else if(view == mGameButton2){
            index = 1;
        }
        else if(view == mGameButton3){
            index = 2;
        }
        else if(view == mGameButton4){
            index = 3;
        }
        else{
            throw new IllegalStateException("Unknown clicked view : " + view);
        }

        if(index == mQuestionBank.getCurrentQuestion().getAnswerIndex()){
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            mScore++;
        }
        else{
            Toast.makeText(this, "InCorrect!", Toast.LENGTH_SHORT).show();
        }

        // désactivation des boutons après le click d'un bouton
        mEnableTouchEvents = false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRemainingQuestionCount--;

                if(mRemainingQuestionCount > 0){
                    mCurrentQuestion = mQuestionBank.getNextQuestion();
                    displayQuestion(mCurrentQuestion);
                }
                else{
                    endGame();
                }
                mEnableTouchEvents = true;
            }
        }, 2000);
    }

    // Affichage d'AlertDialog
    private void endGame(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Well done!")
                .setMessage("Your Score is " + mScore)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        /*
                            lors du click du bouton "OK" le score passera vers l'activité
                            principale
                         */
                        Intent intent = new Intent();
                        intent.putExtra(BUNDLE_EXTRA_CODE, mScore);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .create()
                .show();
    }
}