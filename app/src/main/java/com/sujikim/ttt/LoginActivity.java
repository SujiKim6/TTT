package com.sujikim.ttt;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    //한글 폰트 적용
    @Override protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //뷰에 있는 것이 여기서 다뤄지게 된다.
        EditText idText = (EditText)findViewById(R.id.idText);
        EditText passwordText = (EditText)findViewById(R.id.passwordText);

        final Button loginButton   = (Button)findViewById(R.id.loginButton);
        TextView registerButton = (TextView)findViewById(R.id.registerButton);

        //회원가입버튼에 따른 이벤트 처리
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(mainIntent);
            }
        });
    }
}
