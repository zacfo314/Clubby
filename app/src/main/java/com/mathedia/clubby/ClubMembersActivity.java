package com.mathedia.clubby;

import android.support.v4.app.ListFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

public class ClubMembersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_members);
        Toolbar Toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(Toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

        ListFragment fragment = new MemberFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        //In extras ist der Club gespeichert, der in der ClubActivity angeclickt wurde
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fragment.setArguments(extras);
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, fragment, "MitgliederFragment");
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

/*    //Das Fragment, das die Mitglieder anzeigt
    public static class ClubMembersFragment extends ListFragment {

        private static final ArrayList<String> memberIDs = new ArrayList<String>();

        public ClubMembersFragment() {
        }

        public static ClubMembersFragment newInstance() {
            ClubMembersFragment fragment = new ClubMembersFragment();
            return fragment;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            Toast.makeText(getActivity(), "Fragment gestartet ", Toast.LENGTH_LONG).show();

            final ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
            setListAdapter(adapter);

            adapter.add("bla");
            adapter.add("bla");
            adapter.add("bla");
            adapter.add("bla");
            adapter.add("bla");
            adapter.add("bla");
            adapter.add("bla");

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Club");
            query.getInBackground(clubID, new GetCallback<ParseObject>() {
                public void done(ParseObject club, ParseException e) {
                    if (e == null) {
                        ParseRelation usersRelation = club.getRelation("users");
                        try {
                            List<ParseUser> members = usersRelation.getQuery().find();
                            for (int i = 0; i < members.size(); i++) {
                                String memberName = members.get(i).getString("username");
                                Toast.makeText(getActivity(), "Mitglied: " + memberName, Toast.LENGTH_LONG).show();
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

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setHasOptionsMenu(true);

        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_clubs, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_logout) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getActivity(), "Abmelden erfolgreich", Toast.LENGTH_LONG).show();

                            //TODO Logout richtig implementieren, damit wieder die Startseite geladen wird

//                            Intent intent = new Intent(
//                                    ClubActivity.this,
//                                    MainActivity.class);
//                            startActivity(intent);
//                            finish();

                        } else {
                            Toast.makeText(getActivity(), "Abmelden fehlgeschlagen", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });
                return true;
            }

            if (id == R.id.action_create_group) {

                Toast.makeText(getActivity(), "Club erstellt", Toast.LENGTH_LONG).show();

                //TODO Logout richtig implementieren, damit wieder die Startseite geladen wird

//                            Intent intent = new Intent(
//                                    ClubActivity.this,
//                                    MainActivity.class);
//                            startActivity(intent);
//                            finish();


                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_club_members, container, false);
        return rootView;
        }
    }*/
}
