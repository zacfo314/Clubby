package com.mathedia.clubby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ClubMembersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_members);
        Toolbar Toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(Toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String clubID = extras.getString("clubID");

            final ListView listview = (ListView) findViewById(R.id.listview);

            ArrayList<String> membersNameList = new ArrayList<String>();
            final ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            listview.setAdapter(adapter);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Club");
            query.getInBackground(clubID, new GetCallback<ParseObject>() {
                public void done(ParseObject club, ParseException e) {
                    if (e == null) {
                        ParseRelation usersRelation = club.getRelation("users");
                        try {
                            List<ParseUser> members = usersRelation.getQuery().find();
                            for(int i=0; i < members.size(); i++) {
                                String memberName = members.get(i).getString("username");
                                adapter.add(memberName);
                            }
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                    } else {
                        // something went wrong
                    }
                }
            });
        }
    }
}
