package semproject.nevent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import semproject.nevent.Connection.ConnectivityReceiver;
import semproject.nevent.Connection.InternetConnection;
import semproject.nevent.MapsActivities.ShowEvents;
import semproject.nevent.Request.RecyclerRequest;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ConnectivityReceiver.ConnectivityReceiverListener{
    final String STRING_TAG= "HomePage";
    NavigationView navigationView=null;
    Toolbar toolbar=null;
    String username;
    Context context;
    int id;
    static EventRecyclerView staticeventRecyclerView = new EventRecyclerView();
    static EventRecyclerView stat_forsearch_eventRecyclerView = new EventRecyclerView();

    static EventRecyclerView.AllItemAdapter staticadapter=new EventRecyclerView.AllItemAdapter();
    static EventRecyclerView.FollowItemAdapter stat_forsearch_Useradapter=new EventRecyclerView.FollowItemAdapter();

    static ShowEvents showEvents;

    List<String>userid=new ArrayList<>();
    List<String>fusername=new ArrayList<>();
    List<String>useremail=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        staticeventRecyclerView = new EventRecyclerView();
        stat_forsearch_eventRecyclerView = new EventRecyclerView();
        staticadapter=new EventRecyclerView.AllItemAdapter();
        stat_forsearch_Useradapter=new EventRecyclerView.FollowItemAdapter();

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        id=intent.getIntExtra("id",3);
        Button userbutton=(Button)findViewById(R.id.user_button);
        userbutton.setText(username);

        listenerFunction(username);
        if (id==1)
        {
            Recent recent = new Recent();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putInt("id",id);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, recent);
            recent.setArguments(bundle);
            fragmentTransaction.commit();
        }
        else if(id==2)
        {
            NearByList nearByList = new NearByList();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, nearByList);
            nearByList.setArguments(bundle);
            fragmentTransaction.commit();
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
         getSupportActionBar().setDisplayShowTitleEnabled(false);
         toolbar.setLogo(R.drawable.logo);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed(); //use property of super class - Appcompat actvity
        }
    }

    @Override //http://stackoverflow.com/questions/31231609/creating-a-button-in-android-toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homepage, menu);
/*        MenuItem menuItem=menu.findItem(R.id.action_search);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_location:
                showEvents=new ShowEvents();
                Log.e("Searching","action_location");
                Intent intent=new Intent(this, ShowEvents.class);
                intent.putExtra("username",username);
                finish();
                startActivity(intent);
                break;

            case R.id.action_search:
                Log.e("Searching","action_search");
                Intent searchintent=new Intent(this, SearchResultActivity.class);
                searchintent.putExtra("username",username);
                startActivity(searchintent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Button all = (Button) findViewById(R.id.button4);


        Button user = (Button) findViewById(R.id.user_button);

        Button trend = (Button) findViewById(R.id.button5);

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        if (id == R.id.nav_sports) {

            trend.setBackgroundResource(R.drawable.cdefault);
            all.setBackgroundResource(R.drawable.cdefault);
            user.setBackgroundResource(R.drawable.cdefault);


            Sports sports=new Sports();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, sports);
            sports.setArguments(bundle);
            fragmentTransaction.commit();

            // Handle the camera action
        } else if (id == R.id.nav_parties) {
            trend.setBackgroundResource(R.drawable.cdefault);
            all.setBackgroundResource(R.drawable.cdefault);
            user.setBackgroundResource(R.drawable.cdefault);
            Parties parties=new Parties();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, parties);
            parties.setArguments(bundle);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_conferences) {
            trend.setBackgroundResource(R.drawable.cdefault);
            all.setBackgroundResource(R.drawable.cdefault);
            user.setBackgroundResource(R.drawable.cdefault);
            Conference conference=new Conference();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, conference);
            conference.setArguments(bundle);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_donations) {
            trend.setBackgroundResource(R.drawable.cdefault);
            all.setBackgroundResource(R.drawable.cdefault);
            user.setBackgroundResource(R.drawable.cdefault);
            Donations donations=new Donations();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, donations);
            donations.setArguments(bundle);
            fragmentTransaction.commit();


        }

        else if (id == R.id.nav_others) {
            trend.setBackgroundResource(R.drawable.cdefault);
            all.setBackgroundResource(R.drawable.cdefault);
            user.setBackgroundResource(R.drawable.cdefault);
            Others others=new Others();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, others);
            others.setArguments(bundle);
            fragmentTransaction.commit();


        }
        else if (id == R.id.nav_business) {
            trend.setBackgroundResource(R.drawable.cdefault);
            all.setBackgroundResource(R.drawable.cdefault);
            user.setBackgroundResource(R.drawable.cdefault);
            Business business=new Business();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, business);
            business.setArguments(bundle);
            fragmentTransaction.commit();


        }
        else if (id == R.id.nav_concert) {
            trend.setBackgroundResource(R.drawable.cdefault);
            all.setBackgroundResource(R.drawable.cdefault);
            user.setBackgroundResource(R.drawable.cdefault);
            Concert concert=new Concert();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, concert);
            concert.setArguments(bundle);
            fragmentTransaction.commit();


        }

        else if (id == R.id.nav_educational) {
            trend.setBackgroundResource(R.drawable.cdefault);
            all.setBackgroundResource(R.drawable.cdefault);
            user.setBackgroundResource(R.drawable.cdefault);
            Educational educational=new Educational();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, educational);
            educational.setArguments(bundle);
            fragmentTransaction.commit();


        }
        else if (id == R.id.nav_exhibition) {
            trend.setBackgroundResource(R.drawable.cdefault);
            all.setBackgroundResource(R.drawable.cdefault);
            user.setBackgroundResource(R.drawable.cdefault);
            Exhibition exhibition=new Exhibition();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, exhibition);
            exhibition.setArguments(bundle);
            fragmentTransaction.commit();


        }
        else if (id == R.id.nav_gaming) {
            trend.setBackgroundResource(R.drawable.cdefault);
            all.setBackgroundResource(R.drawable.cdefault);
            user.setBackgroundResource(R.drawable.cdefault);
            Gaming gaming=new Gaming();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, gaming);
            gaming.setArguments(bundle);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void trending(View view) {
        Button all = (Button) findViewById(R.id.button4);
        all.setBackgroundResource(R.drawable.cdefault);

        Button user = (Button) findViewById(R.id.user_button);
        user.setBackgroundResource(R.drawable.cdefault);

        Button trend = (Button) findViewById(R.id.button5);
        trend.setBackgroundResource(R.drawable.shape3);
        if(checkConnection(this)){
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            Trending trending=new Trending();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, trending);
            trending.setArguments(bundle);
            fragmentTransaction.commit();
        }
    }

    public void recent(View view) {

        Button user = (Button) findViewById(R.id.user_button);
        user.setBackgroundResource(R.drawable.cdefault);

        Button trend = (Button) findViewById(R.id.button5);
        trend.setBackgroundResource(R.drawable.cdefault);


        Button all = (Button) findViewById(R.id.button4);
        all.setBackgroundResource(R.drawable.shape3);
        if(checkConnection(this)){
            Recent recent=new Recent();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, recent);
            recent.setArguments(bundle);
            fragmentTransaction.commit();
        }
    }

    public void addevents(View view) {
        if(checkConnection(this)){
            Intent i=new Intent(this, Upload.class);
            i.putExtra("username",username);
            finish();
            startActivity(i);
        }


    }

    public void userdetails(View view){



        Button all = (Button) findViewById(R.id.button4);
        all.setBackgroundResource(R.drawable.cdefault);

        Button trend = (Button) findViewById(R.id.button5);
        trend.setBackgroundResource(R.drawable.cdefault);

        Button user = (Button) findViewById(R.id.user_button);
        user.setBackgroundResource(R.drawable.shape3);

        if(checkConnection(this)){
            /*Intent intent = new Intent(this, UserDetails.class);
            intent.putExtra("username",username);
            startActivity(intent);
            finish();*/

            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            Userdetail userDetails=new Userdetail();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, userDetails);
            userDetails.setArguments(bundle);
            fragmentTransaction.commit();
        }
    }

     public void retreiveFromDatabase(Context context){
        Log.e(STRING_TAG,"database");
        if(checkConnection(this)){
            for (int i=0;i < userid.size();i++)
            {
                Log.i("Value of element "+i,fusername.get(i));
                stat_forsearch_eventRecyclerView.initializeDataFollow(userid.get(i),fusername.get(i),useremail.get(i),context);
                stat_forsearch_Useradapter = new EventRecyclerView.FollowItemAdapter(context, stat_forsearch_eventRecyclerView.getItemFollow());
               /* mRecyclerView.setAdapter(staticadapter);*/
            }
        }

    }

    public void listenerFunction(String username){
        Log.e(STRING_TAG,"insideListiner");
        Response.Listener<String> responseListener= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e(STRING_TAG,"try");
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("userid");

                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        Log.e(STRING_TAG,"insideSuccess");
                        //JSONArray jsonArray = jsonObject.getJSONArray("userid");
                        Log.e(STRING_TAG,"userid");
                        JSONArray jsonArray2 = jsonObject.getJSONArray("username");
                        Log.e(STRING_TAG,"username");
                        JSONArray jsonArray3 = jsonObject.getJSONArray("email");
                        Log.e(STRING_TAG,"email");

                        if (jsonArray != null) {
                            int len = jsonArray.length();
                            Log.e(STRING_TAG,Integer.toString(len));

                            //for userid
                            for (int i=0;i<len;i++){
                                userid.add(jsonArray.get(i).toString());
                            }
                            //for username
                            for (int i=0;i<len;i++){
                                fusername.add(jsonArray2.get(i).toString());
                            }
                            //for email
                            for (int i=0;i<len;i++){
                                useremail.add(jsonArray3.get(i).toString());
                            }

                            retreiveFromDatabase(HomePage.this);
                        }
                        else
                            Log.e(STRING_TAG,"insideNull");

                    }
                    else {
                        AlertDialog.Builder builder= new AlertDialog.Builder(HomePage.this);
                        builder.setMessage("Connection Failed")
                                .setNegativeButton("Retry",null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        if(checkConnection(this)){
            RecyclerRequest recyclerRequest=new RecyclerRequest(username,"alluser", responseListener);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(recyclerRequest);
        }
    }

    private boolean checkConnection(Context context) {
        Log.e(STRING_TAG,"checkConnection");
        boolean isConnected = ConnectivityReceiver.isConnected(context);
        if(!isConnected){
            Intent intent= new Intent(this,InternetConnection.class);
            finish();
            startActivity(intent);
        }
        return isConnected;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected){
            Intent intent= new Intent(this,MainActivity.class);
            finish();
            startActivity(intent);
        }
        else{
            Intent intent= new Intent(this,InternetConnection.class);
            finish();
            startActivity(intent);

        }
    }

}
