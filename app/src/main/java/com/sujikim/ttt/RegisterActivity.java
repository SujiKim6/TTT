package com.sujikim.ttt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //뷰에 있는 것이 여기서 다뤄지게 된다.
        EditText idText = (EditText)findViewById(R.id.idText);
        EditText passwordText = (EditText)findViewById(R.id.passwordText);
        EditText passwordCheckText = (EditText)findViewById(R.id.passwordCheckText);

        Button registerButton   = (Button)findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, MainActivity.class);
                RegisterActivity.this.startActivity(loginIntent);
            }
        });
    }
}
