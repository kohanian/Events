package events.solomid.com.events;

import android.content.Context;
import android.net.Uri;
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
 */
public class HttpRequester {
    private static final String TAG = "HttpRequester";
    private String KEY_FOOTER = "?key=f0a4eb272636ecc69f9491831bbfb65e";
    private String BASE_URL = "http://api.reimaginebanking.com";
    private Context context;

    public HttpRequester(Context _context) {
        context = _context;
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
        url = "http://ign.com";
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