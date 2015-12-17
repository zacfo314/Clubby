package com.mathedia.clubby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CreateClubActivity extends AppCompatActivity {

    private EditText mNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);

        Toolbar Toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(Toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNameView = (EditText) findViewById(R.id.name);

        Button mCreateClubButton = (Button) findViewById(R.id.create_club_button);
        mCreateClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCreateClub();
            }
        });
    }

    private void attemptCreateClub() {

        // Reset errors.
        mNameView.setError(null);

        // Store values at the time of the create attempt.
        String name = mNameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid name.
        if (!TextUtils.isEmpty(name) && !isNameValid(name)) {
            mNameView.setError(getString(R.string.error_min_3_chars));
            focusView = mNameView;
            cancel = true;
        } else if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_min_3_chars));
            focusView = mNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            final ParseObject club = new ParseObject("Club");
            club.put("clubName", name);
            club.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        ParseRelation relation = club.getRelation("users");
                        relation.add(ParseUser.getCurrentUser());
                        club.saveInBackground();
                        Toast.makeText(getApplicationContext(),
                                "Club erstellt",
                                Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Fehlgeschlagen",
                                Toast.LENGTH_LONG).show();
                        //Log.e("Clubby", "exception", e);
                    }
                }
            });
        }
    }

    private boolean isNameValid(String name) {
        //TODO: Replace this with your own logic
        return name.length() > 2;
    }
}



