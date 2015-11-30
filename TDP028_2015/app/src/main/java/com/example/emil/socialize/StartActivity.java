package com.example.emil.socialize;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import bolts.Task;

public class StartActivity extends Activity implements View.OnClickListener, View.OnKeyListener {

    EditText usernameField;
    EditText passwordField;
    EditText emailField;
    EditText nameField;
    Button signUpButton;
    TextView changeMode;
    RelativeLayout relativeLayout;
    ImageView logo;

    Boolean signUpMode;

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(signUpMode == true && v.getId() == R.id.email) {

            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                signUpOrLogin(v);

            }

        } else if(signUpMode == false && v.getId() == R.id.password ) {

            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                signUpOrLogin(v);

            }

        }
        return false;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.changeMode) {

            Log.i("AppInfo", "Changed mode");
            if(signUpMode == true) {

                signUpMode = false;
                changeMode.setText("Sign up");
                signUpButton.setText("Log in");
                emailField.setVisibility(View.INVISIBLE);
                nameField.setVisibility(View.INVISIBLE);


            } else {
                signUpMode = true;
                changeMode.setText("Log in");
                signUpButton.setText("Sign up");
                emailField.setVisibility(View.VISIBLE);
                nameField.setVisibility(View.VISIBLE);






            }



        }  else if (v.getId() == R.id.logo || v.getId() == R.id.relativeLayout) {

            InputMethodManager inm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }

    }

    public void signUpOrLogin(View view) {

        Log.i("AppInfo", String.valueOf(usernameField.getText()));
        Log.i("AppInfo", String.valueOf(passwordField.getText()));
        Log.i("AppInfo", String.valueOf(nameField.getText()));
        Log.i("AppInfo", String.valueOf(emailField.getText()));

        if (signUpMode == true) {


            ParseUser user = new ParseUser();
            user.setUsername(String.valueOf(usernameField.getText()));
            if (emailField.getText() != null) {

                user.setEmail(String.valueOf(emailField.getText()));

            }

            if (nameField.getText() != null) {

                user.put("name", String.valueOf(nameField.getText()));

            }

            user.setPassword(String.valueOf(passwordField.getText()));

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {

                        Log.i("AppInfo", "Signup successful");

                    } else {

                        //e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_SHORT).show();
                        Log.i("AppInfo", "Signup failed");
                    }
                }
            });
        } else {

            ParseUser.logInInBackground(String.valueOf(usernameField.getText()), String.valueOf(passwordField.getText()), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(user != null) {

                        Log.i("AppInfo", "Login successful");

                    } else {

                        Log.i("AppInfo", "Login failed");
                        //e.printStackTrace();
                        //TODO More informative error messages displayed to users.
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        /*ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();*/

        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        emailField = (EditText) findViewById(R.id.email);
        nameField = (EditText) findViewById(R.id.name);
        changeMode = (TextView) findViewById(R.id.changeMode);
        signUpButton = (Button) findViewById(R.id.signup);
        changeMode.setText("Sign up");
        signUpButton.setText("Log in");
        emailField.setVisibility(View.INVISIBLE);
        nameField.setVisibility(View.INVISIBLE);
        signUpMode = false;
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        logo = (ImageView) findViewById(R.id.logo);

        changeMode.setOnClickListener(this);
        logo.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);

        passwordField.setOnKeyListener(this);
        emailField.setOnKeyListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
