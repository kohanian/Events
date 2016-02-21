package events.solomid.com.events;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jake on 2/20/2016.
 * The Capitol One API is offline at the time of submission,
 * but these routes should work properly once the server is back.
 */
public class HttpRequester {
    public static final String SHARED_PREF_NAME = "Events_Prefs";
    public static final String SHARED_PREF_ID_CHARITY = "PREFS_CHARITY";
    public static final String SHARED_PREF_ID_ACCID = "PREFS_ACCOUNTID";
    public static final String SHARED_PREF_ID_TOTALDONATED = "PREFS_TOTAL";
    private static final String TAG = "HttpRequester";
    private String KEY_FOOTER = "?key=f0a4eb272636ecc69f9491831bbfb65e";
    private String BASE_URL = "http://api.reimaginebanking.com";
    private Context context;

    public HttpRequester(Context _context) {
        context = _context;
    }

    public void donateCash() {
        //Transfer cash from dummy account into another dummy account to simulate donation
        SharedPreferences sharedPreferences = context.
                getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int AMOUNT = 5; //Cash to donate
        String DONER_ID = sharedPreferences.getString("id", "123456789");
        DONER_ID = DONER_ID.replace(" ","");
        String DONER_ACCOUNT_ID = "56c66be6a73e492741507b78";
        String RECIEVER_ID = "56c66be5a73e492741507298";
        String RECIEVER_ACCOUNT_ID = "56c66be6a73e492741507b79";

        HashMap<String,String> params = new HashMap<>();
        params.put("medium","balance");
        params.put("payee_id",RECIEVER_ACCOUNT_ID);
        params.put("amount",Integer.toString(AMOUNT));
        params.put("transaction_date","1-1-2016");
        params.put("status","balance");
        params.put("description","balance");
        postData("/accounts/"+DONER_ID+"/transfers", params);

        //Update shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int currentDollarsDonated = sharedPreferences.getInt(SHARED_PREF_ID_TOTALDONATED, 0);
        editor.putInt(SHARED_PREF_ID_TOTALDONATED,currentDollarsDonated+AMOUNT);
        Log.d("plzz","Updating: "+currentDollarsDonated+" and "+AMOUNT);
        editor.commit();
    }

    public void getData(String route) {
        // Instantiate the RequestQueue
        Log.d(TAG,"Making GET request...");
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = BASE_URL+route+KEY_FOOTER;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d(TAG, "GET Response is: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Failed GET request: " + error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void postData(String route, final Map<String,String> params) {
        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = BASE_URL+route+KEY_FOOTER;

        Log.d(TAG,"Making POST request: "+url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d(TAG, "POST Response is: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Failed POST request: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }



    public interface PostCommentResponseListener {
        public void requestStarted();
        public void requestCompleted();
        public void requestEndedWithError(VolleyError error);
    }


}
