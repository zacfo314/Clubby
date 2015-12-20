package com.mathedia.clubby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

public class MemberFragment extends ListFragment {

    private String clubID;

    public MemberFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        this.setListAdapter(adapter);

        Bundle extras = getArguments();

        if (extras != null) {
            clubID = extras.getString("clubID");

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Club");
            query.getInBackground(clubID, new GetCallback<ParseObject>() {
                public void done(ParseObject club, ParseException e) {
                    if (e == null) {
                        ParseRelation usersRelation = club.getRelation("users");
                        try {
                            List<ParseUser> members = usersRelation.getQuery().find();
                            for (int i = 0; i < members.size(); i++) {
                                String memberName = members.get(i).getString("username");
                                adapter.add(memberName);
                            }
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                    } else {
                        // something went wrong
                        Toast.makeText(getActivity(), "Keine Mitglieder des Clubs gefunden", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_club_members, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_member) {
            Intent intent = new Intent(getActivity(), AddMemberActivity.class);
            intent.putExtra("clubID", clubID);
            startActivityForResult(intent, 0);
        }
        return super.onOptionsItemSelected(item);
    }
}
