package com.example.practicaltest;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    RequestQueue requestQueue;
    ArrayList<Records> array_records = new ArrayList<>();
    ArrayList<AllRecords> array_allrecords = new ArrayList<>();
    ArrayList<Double> array_latitude = new ArrayList<>();
    ArrayList<Double> array_longtitude = new ArrayList<>();
    ArrayList<record_img> array_images;
    private static DecimalFormat df = new DecimalFormat("0.00");
    record_img imageData;
    private boolean filter = false;
    Button btn_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btn_filter = findViewById(R.id.btn_acending);
        requestQueue = Volley.newRequestQueue(this);
        GetResponse();

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filter) {
                    filter = false;
                    btn_filter.setText("Acending");
                    sortNameByAsc();
                } else {
                    filter = true;
                    btn_filter.setText("Descending");
                    sortNameByDesc();
                }
            }
        });
    }

    public void sortNameByAsc() {
        Comparator<AllRecords> comparator = new Comparator<AllRecords>() {
            @Override
            public int compare(AllRecords object1, AllRecords object2) {
                return object1.person_name.compareToIgnoreCase(object2.person_name);
            }
        };
        Collections.sort(array_allrecords, comparator);
        data_handler.sendMessage(data_handler.obtainMessage(0));
    }

    public void sortNameByDesc() {
        Comparator<AllRecords> comparator = new Comparator<AllRecords>() {
            @Override
            public int compare(AllRecords object1, AllRecords object2) {
                return object2.person_name.compareToIgnoreCase(object1.person_name);
            }
        };
        Collections.sort(array_allrecords, comparator);
        data_handler.sendMessage(data_handler.obtainMessage(0));
    }

    private void GetResponse() {
        array_records.clear();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppHelper.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String responseString = null;
                responseString = response.toString();
                Log.e("TAG", responseString);

                JSONObject jsonResultObj = null;
                try {
                    jsonResultObj = new JSONObject(responseString);
                } catch (Exception e) {
                    Log.e("JSON", e.toString());
                }

                if (jsonResultObj == null) {
                    data_handler.sendMessage(data_handler.obtainMessage(99));
                }

                try {
                    JSONObject recordObj = jsonResultObj.getJSONObject("data");
                    String totalRecords = recordObj.optString("total_records");
                    String message = recordObj.optString("message");

                    Records route_data = new Records();
                    route_data.total_records = totalRecords.trim();
                    route_data.message = message.trim();

                    JSONArray json_records_array = new JSONArray();
                    json_records_array = recordObj.getJSONArray("records");
                    if (json_records_array.length() > 0) {
                        ArrayList<AllRecords> array_all_records = new ArrayList<AllRecords>();
                        array_all_records.clear();

                        for (int j = 0; j < json_records_array.length(); j++) {
                            JSONObject all_records_obj = json_records_array.getJSONObject(j);

                            String id = all_records_obj.optString("id");
                            String profilePic = all_records_obj.optString("profile_pic");
                            String businessName = all_records_obj.optString("business_name");
                            String personName = all_records_obj.optString("person_name");
                            String email = all_records_obj.optString("email");
                            String countryId = all_records_obj.optString("country_id");
                            String stateId = all_records_obj.optString("state_id");
                            String cityId = all_records_obj.optString("city_id");
                            String Country = all_records_obj.optString("country");
                            String State = all_records_obj.optString("state");
                            String City = all_records_obj.optString("city");
                            String Address = all_records_obj.optString("address");
                            String Contact = all_records_obj.optString("contact");
                            String zipCode = all_records_obj.optString("zip_code");
                            String aboutUs = all_records_obj.optString("about_us");
                            double Latitude = all_records_obj.optDouble("c_latitude");
                            double Longtitude = all_records_obj.optDouble("c_longitude");
                            String Status = all_records_obj.optString("status");
                            String isBlock = all_records_obj.optString("is_block");
                            double distanceKm = all_records_obj.optDouble("distance_in_km");
                            double distanceMiles = all_records_obj.optDouble("distance_in_miles");
                            double distanceMeter = all_records_obj.optDouble("distance_in_meter");
                            String totalpartyJoined = all_records_obj.optString("total_party_joined");
                            String totalpartyAnimal = all_records_obj.optString("total_party_animal");
                            String isbusiestLocation = all_records_obj.optString("is_busiest_location");
                            String iseventnearestMe = all_records_obj.optString("is_event_nearest_me");
                            String joinpartyEvent = all_records_obj.optString("joinpartyEvent");
                            String isFavorite = all_records_obj.optString("is_favorite");
                            String isProfile = all_records_obj.optString("is_public_profile");
                            float avgRating = Float.parseFloat(all_records_obj.optString("avg_rating"));
                            float totalReview = Float.parseFloat(all_records_obj.optString("total_review"));
                            String Rating = all_records_obj.optString("rating");
                            String Review = all_records_obj.optString("review");
                            String Type = all_records_obj.optString("type");

                            AllRecords all_records_data = new AllRecords();
                            all_records_data.id = id;
                            all_records_data.profile_pic = profilePic;
                            all_records_data.business_name = businessName;
                            all_records_data.person_name = personName;
                            all_records_data.email = email;
                            all_records_data.country_id = countryId;
                            all_records_data.state_id = stateId;
                            all_records_data.city_id = cityId;
                            all_records_data.country = Country;
                            all_records_data.state = State;
                            all_records_data.city = City;
                            all_records_data.address = Address;
                            all_records_data.contact = Contact;
                            all_records_data.zip_code = zipCode;
                            all_records_data.about_us = aboutUs;
                            all_records_data.c_latitude = Latitude;
                            all_records_data.c_longitude = Longtitude;
                            all_records_data.status = Status;
                            all_records_data.is_block = isBlock;
                            all_records_data.distance_in_km = distanceKm;
                            all_records_data.distance_in_miles = distanceMiles;
                            all_records_data.distance_in_meter = distanceMeter;
                            all_records_data.total_party_joined = totalpartyJoined;
                            all_records_data.total_party_animal = totalpartyAnimal;
                            all_records_data.is_busiest_location = isbusiestLocation;
                            all_records_data.is_event_nearest_me = iseventnearestMe;
                            all_records_data.join_party_event = joinpartyEvent;
                            all_records_data.is_favorite = isFavorite;
                            all_records_data.is_public_profile = isProfile;
                            all_records_data.avg_rating = avgRating;
                            all_records_data.total_review = String.valueOf(totalReview);
                            all_records_data.rating = Rating;
                            all_records_data.review = Review;
                            all_records_data.type = Type;

                            JSONArray json_images_array = new JSONArray();
                            json_images_array = all_records_obj.getJSONArray("business_images");

                            JSONArray json_review_array = new JSONArray();
                            json_review_array = all_records_obj.getJSONArray("all_rating_reviews");

                            if (json_images_array.length() != 0) {
                                array_images = new ArrayList<record_img>();
                                array_images.clear();

                                for (int k = 0; k < json_images_array.length(); k++) {
                                    imageData = new record_img();
                                    imageData.business_images = json_images_array.getString(k);
                                    array_images.add(imageData);
                                }
                                all_records_data.setArray_images(array_images);
                            }

                            if (json_review_array.length() != 0) {
                                ArrayList<rating_review> array_reviews = new ArrayList<rating_review>();
                                array_reviews.clear();

                                for (int l = 0; l < json_review_array.length(); l++) {
                                    JSONObject all_reviews_obj = json_review_array.getJSONObject(j);

                                    //String review = all_records_obj.optString("review");

                                    rating_review allReviews = new rating_review();
                                    allReviews.user_review = "";

                                    array_reviews.add(allReviews);
                                }
                                all_records_data.setArray_rating(array_reviews);
                            }
                            array_all_records.add(all_records_data);
                        }
                        route_data.setAllData(array_all_records);

                        array_records.add(route_data);

                        data_handler.sendMessage(data_handler.obtainMessage(0));
                    } else {
                        data_handler.sendMessage(data_handler.obtainMessage(99));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                data_handler.sendMessage(data_handler.obtainMessage(99));
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("c_latitude", "23.03291750");
                params.put("c_longitude", "72.55576330");
                params.put("page", "0");
                //add params <key,value>
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // add headers <key,value>
                String credentials = "admin" + ":" + "admin";
                /*String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);*/
                headers.put("Authorization", "Basic YWRtaW46YWRtaW4=");
                return headers;
            }
        };
        requestQueue.add(strReq);
    }

    private Handler data_handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0: // Succeeded
                {
                    Log.e("MapActivity", "Trains Data...");
                    int i = 0;
                    if (array_records.size() > 0) {
                        array_latitude.clear();
                        array_longtitude.clear();

                        array_allrecords = array_records.get(0).getAllData();
                        if (array_allrecords.size() > 0) {
                            for (i = 0; i < array_allrecords.size(); i++) {
                                array_latitude.add(array_allrecords.get(i).c_latitude);
                                array_longtitude.add(array_allrecords.get(i).c_longitude);
                                String title = array_allrecords.get(i).business_name;

                                if (mMap != null) {
                                    LatLng sydney = new LatLng(array_allrecords.get(i).c_latitude, array_allrecords.get(i).c_longitude);
                                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.wine_glass);
                                    mMap.addMarker(new MarkerOptions().position(sydney).title(title)
                                            .icon(bitmapDescriptor));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


                                    int finalI = i;
                                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                        @Override
                                        public View getInfoWindow(Marker arg0) {
                                            ContextThemeWrapper cw = new ContextThemeWrapper(
                                                    getApplicationContext(), R.style.Transparent);
                                            // AlertDialog.Builder b = new AlertDialog.Builder(cw);
                                            LayoutInflater inflater = (LayoutInflater) cw
                                                    .getSystemService(LAYOUT_INFLATER_SERVICE);
                                            View v = inflater.inflate(R.layout.map_info_window,
                                                    null);

                                            ImageView img_party = v.findViewById(R.id.img_party);
                                            TextView txt_partyName = v.findViewById(R.id.txt_partyName);
                                            TextView txt_add = v.findViewById(R.id.txt_add);
                                            TextView txt_miles = v.findViewById(R.id.txt_miles);
                                            RatingBar rating = v.findViewById(R.id.rating);


                                            txt_partyName.setText(array_allrecords.get(finalI).business_name);
                                            txt_add.setText(array_allrecords.get(finalI).address);
                                            rating.setRating(array_allrecords.get(0).avg_rating);

                                            double miles = (array_allrecords.get(finalI).distance_in_miles);
                                            txt_miles.setText(String.format("%.2f", miles) + " Miles");

                                            String url = array_images.get(0).business_images;
                                            Glide.with(MapsActivity.this)
                                                    .load(url)
                                                    .centerCrop()
                                                    .into(img_party);
                                            return v;
                                        }
                                        @Override
                                        public View getInfoContents(Marker arg0) {
                                            return null;
                                        }
                                    });
                                }
                            }
                        }
                        Toast.makeText(MapsActivity.this, "Size" + array_records.size(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                case 99:{}
                break;
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));
            if (!success) {
                Log.e("MapActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapActivity", "Can't find style. Error: ", e);
        }
        mMap = googleMap;
    }

}