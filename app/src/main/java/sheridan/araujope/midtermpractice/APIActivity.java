package sheridan.araujope.midtermpractice;

import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIActivity extends AppCompatActivity {

    private ListView mCountriesList;
    private RequestQueue mQueue;
    private List<String> countryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i("apitest", "on create");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCountriesList = findViewById(R.id.countriesList);
        mQueue = Volley.newRequestQueue(this);
        jsonParse();
    }

    private void jsonParse() {
        String url = getString(R.string.httphost);

        Log.i("apitest", "json parse");

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.i("apitest", "on response");
                    Log.i("apitest", "results found: " + response.length());

                    for (int i = 0; i < 100; i++) {
                        JSONObject countryJSON = response.getJSONObject(i);
                        Log.i("apitest", response.getJSONObject(i).toString());
                        String name = countryJSON.getString("name");
                        String capital = countryJSON.getString("capital");

                        countryList.add(name + " - " + capital);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                populateListView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("apitest", error.getMessage());
                error.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-RapidAPI-Key", getString(R.string.api_key));
                return params;
            }
        };

        mQueue.add(request);
    }

    private void populateListView() {
        Log.i("apitest", "Populating list view");

        for(String item : countryList) {
            Log.i("apitest", item);
        }

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, R.layout.list_item, countryList);
        mCountriesList.setAdapter(arrayAdapter);
    }

}
