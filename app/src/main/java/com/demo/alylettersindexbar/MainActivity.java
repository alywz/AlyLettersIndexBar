package com.demo.alylettersindexbar;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements LettersIndexBar.LettersListener {

    private LettersIndexBar lettersIndexbar;
    private TextView tvLetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lettersIndexbar = findViewById(R.id.letters_indexbar);
        tvLetter = findViewById(R.id.tv_letter);
        lettersIndexbar.setLettersListener(this);

    }

    @Override
    public void touchLetter(CharSequence charSequence, boolean isTouch) {
        if (isTouch) {
            tvLetter.setVisibility(View.VISIBLE);
            tvLetter.setText(charSequence);
        } else {
            tvLetter.setVisibility(View.GONE);
        }
    }
}
