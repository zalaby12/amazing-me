package amazingme.activities.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amazingme.activities.R;

import amazingme.app.EnumeratedActivity;
import amazingme.controller.ActivityManager;
import amazingme.database.FirebaseHelper;
import amazingme.model.AmazingMeAppCompatActivity;

public class LoginActivity extends AmazingMeAppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginBtn, registerBtn;
    private TextView forgotPasswordTextBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        forgotPasswordTextBtn = (TextView) findViewById(R.id.forgotpasswordBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                FirebaseHelper.loginUser(email, password, LoginActivity.this);
            }
        });

        forgotPasswordTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().goTo(LoginActivity.this, EnumeratedActivity.FORGOTPASSWORD);
            }
        });
    }

    @Override
    public EnumeratedActivity activityName() {
        return EnumeratedActivity.LOGIN;
    }
}
