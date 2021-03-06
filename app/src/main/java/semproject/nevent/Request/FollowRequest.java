package semproject.nevent.Request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aayush on 7/6/2017.
 */

public class FollowRequest extends StringRequest {
    final String STRING_TAG= "FollowRequest";
    private static final String REGISTER_REQUEST_URL = "http://avsadh96.000webhostapp.com/Follow.php";
    private Map<String, String> params;//maps key to value dont have fixed size any number of values can be stored.

    public FollowRequest(String username, String otherusername, String check, Response.Listener<String> listener)
    {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);// post request or get request any one can be used to transfer data
        Log.e("uuuUsername",username);
        Log.e("Otherusername",otherusername);
        params = new HashMap<>();
        params.put("username",username);
        params.put("otherusername",otherusername);
        params.put("check",check);

    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }

}