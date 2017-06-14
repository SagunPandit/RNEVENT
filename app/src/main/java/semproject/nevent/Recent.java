package semproject.nevent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static semproject.nevent.HomePage.staticadapter;
import static semproject.nevent.HomePage.staticeventRecyclerView;

/**
 * Created by User on 1/23/2017.
 */

public class Recent extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener{
    private RecyclerView mRecyclerView;
    String STRING_TAG="RECENT";
    String username;
    String current_date;
    List<String> eventId=new ArrayList<>();
    List<String>eventList=new ArrayList<>();
    List<String>eventLocation=new ArrayList<>();
    List<String>eventDate=new ArrayList<>();
    List<String>eventCategory=new ArrayList<>();
    List<String>eventOrganizer=new ArrayList<>();
    List<Integer>viewcount=new ArrayList<>();
    static public List<Double>latitude=new ArrayList<>();
    static public List<Double>longitude=new ArrayList<>();
    List<String>slatitude=new ArrayList<>();
    List<String>slongitude=new ArrayList<>();
    List<Integer>outdatedEvent= new ArrayList<>();

    public static List<String> extracteventId=new ArrayList<>();
    public static List<String>extracteventList=new ArrayList<>();
    public static List<String>extracteventLocation=new ArrayList<>();
    public static List<String>extracteventDate=new ArrayList<>();
    public static List<String>extracteventCategory=new ArrayList<>();
    public static List<String>extracteventOrganizer=new ArrayList<>();
    public static List<Integer>extractviewcount=new ArrayList<>();
    public static List<Double>extractlatitude=new ArrayList<>();
    public static List<Double>extractlongitude=new ArrayList<>();


    public Recent() {
        staticeventRecyclerView=new EventRecyclerView();
        staticadapter=new EventRecyclerView.AllItemAdapter();
        extractTime();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        username = getArguments().getString("username");
        latitude=new ArrayList<>();
        longitude=new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_recent, container, false);

        // BEGIN_INCLUDE(initializeRecyclerView)
        RecyclerView.LayoutManager mLayoutManager;
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.all_recycler_view);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }


        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        listenerFunction(username);
        return rootView;
    }
    public void retreiveFromDatabase(RecyclerView mRecyclerView,Context context){
        Log.e(STRING_TAG,"database");
        if(checkConnection(getContext())){
            for (int i=0;i < eventList.size();i++)
            {
                Log.i("Value of element "+i,eventList.get(i));
                staticeventRecyclerView.initializeData(eventId.get(i),eventList.get(i),eventCategory.get(i),eventLocation.get(i),eventDate.get(i),eventOrganizer.get(i),viewcount.get(i),context,0);
                staticadapter = new EventRecyclerView.AllItemAdapter(context, staticeventRecyclerView.getItem(),username,false);
                mRecyclerView.setAdapter(staticadapter);
            }
        }

    }

    public void listenerFunction(String username){
        extracteventId=new ArrayList<>();
        extracteventList=new ArrayList<>();
        extracteventLocation=new ArrayList<>();
        extracteventDate=new ArrayList<>();
        extracteventCategory=new ArrayList<>();
        extracteventOrganizer=new ArrayList<>();
        extractviewcount=new ArrayList<>();
        extractlatitude=new ArrayList<>();
        extractlongitude=new ArrayList<>();
        Log.e(STRING_TAG,"insideListiner");
        Response.Listener<String> responseListener= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e(STRING_TAG,"try");
                    JSONObject jsonObject=new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        Log.e(STRING_TAG,"insideSuccess");
                        JSONArray jsonArray = jsonObject.getJSONArray("event_name");
                        JSONArray jsonArray2 = jsonObject.getJSONArray("location_name");
                        JSONArray jsonArray3 = jsonObject.getJSONArray("event_date");
                        JSONArray jsonArray4 = jsonObject.getJSONArray("event_category");
                        JSONArray jsonArray5 = jsonObject.getJSONArray("event_organizer");
                        JSONArray jsonArray6 = jsonObject.getJSONArray("event_id");
                        JSONArray jsonArray7 = jsonObject.getJSONArray("viewcount");
                        JSONArray jsonArray8 = jsonObject.getJSONArray("latitude");
                        JSONArray jsonArray9 = jsonObject.getJSONArray("longitude");
                        if (jsonArray != null) {
                            int len = jsonArray.length();
                            Log.e(STRING_TAG,Integer.toString(len));
                            //for eventId
                            for (int i=0;i<len;i++){
                                /*eventId.add(jsonArray6.get(i).toString());
                                extracteventId.add(jsonArray6.get(i).toString());*/
                                if(compareDate(jsonArray3.get(i).toString())){
                                    eventId.add(jsonArray6.get(i).toString());
                                    extracteventId.add(jsonArray6.get(i).toString());
                                }
                                else{
                                    Log.e(STRING_TAG,jsonArray6.get(i).toString());
                                    outdatedEvent.add(Integer.valueOf(jsonArray6.get(i).toString()));}
                            }
                            //for eventname
                            for (int i=0;i<len;i++){
                               /* eventList.add(jsonArray.get(i).toString());
                                extracteventList.add(jsonArray.get(i).toString());*/
                                if(compareDate(jsonArray3.get(i).toString())) {
                                    eventList.add(jsonArray.get(i).toString());
                                    extracteventList.add(jsonArray.get(i).toString());
                                }
                            }
                            //for eventlocation
                            for (int i=0;i<len;i++){
                               /* eventLocation.add(jsonArray2.get(i).toString());
                                extracteventLocation.add(jsonArray2.get(i).toString());*/
                                if(compareDate(jsonArray3.get(i).toString())) {
                                    eventLocation.add(jsonArray2.get(i).toString());
                                    extracteventLocation.add(jsonArray2.get(i).toString());
                                }
                            }
                            //for eventdate
                            for (int i=0;i<len;i++){
                               /* eventDate.add(jsonArray3.get(i).toString());
                                extracteventDate.add(jsonArray3.get(i).toString());*/
                                if(compareDate(jsonArray3.get(i).toString())){
                                    eventDate.add(jsonArray3.get(i).toString());
                                    extracteventDate.add(jsonArray3.get(i).toString());
                                }
                            }
                            //for eventcategory
                            for (int i=0;i<len;i++){
                               /* eventCategory.add(jsonArray4.get(i).toString());
                                extracteventCategory.add(jsonArray4.get(i).toString());*/
                                if(compareDate(jsonArray3.get(i).toString())) {
                                    eventCategory.add(jsonArray4.get(i).toString());
                                    extracteventCategory.add(jsonArray4.get(i).toString());
                                }
                            }
                            //for eventorganizer
                            for (int i=0;i<len;i++){
                               /* eventOrganizer.add(jsonArray5.get(i).toString());
                                extracteventOrganizer.add(jsonArray5.get(i).toString());*/
                                if(compareDate(jsonArray3.get(i).toString())) {
                                    eventOrganizer.add(jsonArray5.get(i).toString());
                                    extracteventOrganizer.add(jsonArray5.get(i).toString());
                                }
                            }
                            //for count
                            for (int i=0;i<len;i++){
                               /* viewcount.add((Integer) jsonArray7.get(i));
                                extractviewcount.add((Integer) jsonArray7.get(i));*/
                                if(compareDate(jsonArray3.get(i).toString())) {
                                    viewcount.add((Integer) jsonArray7.get(i));
                                    extractviewcount.add((Integer) jsonArray7.get(i));
                                }
                            }
                            //for longitude
                            for (int i=0;i<len;i++){
                               /* try {
                                    longitude.add(Double.parseDouble(jsonArray9.get(i).toString()));
                                    extractlongitude.add(Double.parseDouble(jsonArray9.get(i).toString()));
                                } catch (NumberFormatException e) {
                                    longitude.add(1.00000);
                                    extractlongitude.add(1.00000);
                                }*/
                                if(compareDate(jsonArray3.get(i).toString())) {
                                    try {
                                        longitude.add(Double.parseDouble(jsonArray9.get(i).toString()));
                                        extractlongitude.add(Double.parseDouble(jsonArray9.get(i).toString()));
                                    } catch (NumberFormatException e) {
                                        longitude.add(1.00000);
                                        extractlongitude.add(1.00000);
                                    }
                                }
                            }
                            //for latitude
                            for (int i=0;i<len;i++){
                                /*try{
                                    latitude.add(Double.parseDouble(jsonArray8.get(i).toString()));
                                    extractlatitude.add(Double.parseDouble(jsonArray8.get(i).toString()));
                                }
                                catch (NumberFormatException e) {
                                    latitude.add(1.00000);
                                    extractlatitude.add(1.00000);
                                }*/
                                if(compareDate(jsonArray3.get(i).toString())){
                                    try{
                                        latitude.add(Double.parseDouble(jsonArray8.get(i).toString()));
                                        extractlatitude.add(Double.parseDouble(jsonArray8.get(i).toString()));
                                    }
                                    catch (NumberFormatException e) {
                                        latitude.add(1.00000);
                                        extractlatitude.add(1.00000);
                                    }
                                }
                            }

                            retreiveFromDatabase(mRecyclerView, getContext());
                            deleteOutdated();
                        }
                        else
                            Log.e(STRING_TAG,"insideNull");

                    }
                    else {
                        AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
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
        if(checkConnection(getContext())){
            RecyclerRequest recyclerRequest=new RecyclerRequest(username,"all", responseListener);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(recyclerRequest);
        }
    }

    public void extractTime(){
        /*Response.Listener<String> responseListener= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    String date_str= jsonObject.getString("time");
                    String[] split_date= date_str.split(" ");
                    current_date=split_date[0];
                    Log.e(STRING_TAG+" Download",current_date);
                } catch (JSONException e) {
                    Log.e(STRING_TAG+" Download","exception");
                    e.printStackTrace();
                }
            }
        };
        if(checkConnection(getContext())){
            TimeRequest timeRequest=new TimeRequest(responseListener);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(timeRequest);
        }*/
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int month= c.get(Calendar.MONTH)+1;
        int year= c.get(Calendar.YEAR);
        current_date=String.valueOf(day)+"-"+ String.valueOf(month)+"-"+ String.valueOf(year);
        Log.e(STRING_TAG+" Date", String.valueOf(day)+"-"+ String.valueOf(month)+"-"+ String.valueOf(year));
    }


    private boolean compareDate(String comp_date){
        try {
            Log.e(STRING_TAG,comp_date);
            Log.e(STRING_TAG,current_date);

           /* DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Date date = df.parse(current_date);
            Log.e(STRING_TAG,"Checking2 "+ String.valueOf(date));*/

            Date current = new SimpleDateFormat("dd-MM-yyyy").parse(current_date);
            Date comp = new SimpleDateFormat("dd/MM/yyyy").parse(comp_date);
            Log.e(STRING_TAG,"Checking2");
/*            if (comp.compareTo(current)> 0) {
                System.out.println("start is after end");
            } else if (current.compareTo(comp) < 0) {
                System.out.println("start is before end");
            } else if (current.compareTo(comp) == 0) {
                System.out.println("start is equal to end");
            } else {
                System.out.println("Something weird happened...");
            }*/
            if (comp.compareTo(current)<0){
                Log.e(STRING_TAG,"Return False");
                return false;}

        } catch (ParseException e) {
            Log.e(STRING_TAG,"exception");
            e.printStackTrace();
        }
        Log.e(STRING_TAG,"Return true");
        return true;
    }

    private void deleteOutdated(){
        for(int id: outdatedEvent){
            Response.Listener<String> responseListener= new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e(STRING_TAG,"inside outdated");
                        JSONObject jsonObject=new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if(success){
                            Log.e(STRING_TAG,"deleted events");
                        }
                        else
                            Log.e(STRING_TAG,"Not deleted events");
                    }
                    catch(JSONException e) {
                        e.printStackTrace();
                    }
                };
            };
            DeleteOutdatedRequest deleteOutdatedRequest= new DeleteOutdatedRequest(id,responseListener);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(deleteOutdatedRequest);

        }
    }


    private boolean checkConnection(Context context) {
        Log.e(STRING_TAG,"checkConnection");
        boolean isConnected = ConnectivityReceiver.isConnected(context);
        if(!isConnected){
            Intent intent= new Intent(getContext(),InternetConnection.class);
            getActivity().finish();
            startActivity(intent);
        }
        return isConnected;
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected){
            Intent intent= new Intent(getContext(),MainActivity.class);
            getActivity().finish();
            startActivity(intent);
        }
        else{
            Intent intent= new Intent(getContext(),InternetConnection.class);
            getActivity().finish();
            startActivity(intent);

        }
    }
}
