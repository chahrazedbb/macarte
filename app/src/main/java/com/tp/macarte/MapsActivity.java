package com.tp.macarte;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ZoomControls;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sa90.materialarcmenu.ArcMenu;
import com.sa90.materialarcmenu.StateChangeListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    Context context = this;
    BaseMarqueur data ;
    ZoomControls zoom;
    ImageButton geoLocationBt ;
    private final static int MA_PERMISSION_LOCATION = 101;
    Double myLatitude = null;
    Double myLongitude = null;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    protected static final String TAG = "MapsActvity";
    ArrayList<LatLng> MarkerPoints;
    ArrayList<MonMarqueur> oldMarkerPoints;
    public static List<MonMarqueur> maliste ;
    public static List<MonMarqueur> ma = null;
    public  List<MonMarqueur> malistedeux;
    String urlAddress="http://10.0.0.1/macarte/macarte_select.php";
    ArcMenu arcMenuAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(15 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //  zoom
        zoom = (ZoomControls) findViewById(R.id.zcZoom);
        zoom.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());

            }
        });
        zoom.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        // test
        // Initializing
        MarkerPoints = new ArrayList<>();
        oldMarkerPoints = new ArrayList<>();


        // connexion à la base de données
        data = new BaseMarqueur(context);

        try {
            data.open();

        } catch (Exception e) {
            Log.i("Bonjours", "Bonjours");
        }

        // pour la recherche

        geoLocationBt = (ImageButton) findViewById(R.id.btRecherche);
        geoLocationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText searchText = (EditText) findViewById(R.id.etLocationEntry);
                String chercher = searchText.getText().toString();
                if (chercher != null && !chercher.equals("")) {
                    mMap.clear();

                    if (myLatitude != null && myLongitude != null) {
                        LatLng mylocation = new LatLng(myLatitude, myLongitude);
                        mMap.addMarker(new MarkerOptions().position(mylocation).title("Vous etes ici!")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))).showInfoWindow();
                    }

                    List<MonMarqueur> m = data.getMonMarqueur(chercher);
                    maliste = data.getMonMarqueur(chercher);
                    for (int i = 0; i < m.size(); i++) {
                        LatLng lat = new LatLng(m.get(i).getLat(), m.get(i).getLng());
                        mMap.addMarker(new MarkerOptions()
                                .title(m.get(i).getTitre())
                                .snippet(m.get(i).getDescription())
                                .position(lat)
                        ).showInfoWindow();
                        oldMarkerPoints.add(m.get(i));
                    }

                }
            }
        });


        FloatingActionButton FAB1 = (FloatingActionButton)findViewById(R.id.maj);
        FAB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telechargeur d = new telechargeur(MapsActivity.this, urlAddress, data);
                d.execute();
            }
        });

        FloatingActionButton FAB2 = (FloatingActionButton)findViewById(R.id.lister);
        FAB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maliste!=null && !maliste.isEmpty()) {
                    Intent intent = new Intent(v.getContext(), MaListe.class);
                    startActivityForResult(intent, 0);
                }else {
                    Toast.makeText(MapsActivity.this,"la carte est vide", Toast.LENGTH_LONG).show();
                }
            }
        });

        FloatingActionButton FAB3 = (FloatingActionButton)findViewById(R.id.effacer);
        FAB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //maliste = data.getMonMarqueur();
               // int j = maliste.size();
                mMap.clear();
                /*for (int i = 0; i <  j; i++) {
                    data.SuppMarqueur(maliste.get(i));
                }*/
                maliste.clear();
            }
        });

        FloatingActionButton FAB4 = (FloatingActionButton)findViewById(R.id.fav);
        FAB4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ma = data.getFav();
                if (ma != null && !ma.isEmpty()) {
                    Intent i = new Intent(MapsActivity.this, Fav.class);
                    startActivityForResult(i, 0);
                }else
                {
                    Toast.makeText(MapsActivity.this,"la liste des favoris est vide", Toast.LENGTH_LONG).show();
                }
            }

        });
        FloatingActionButton FAB5 = (FloatingActionButton)findViewById(R.id.favmarq);
        FAB5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                ma = data.getFav();
                if (ma != null && !ma.isEmpty()) {
                    if (myLatitude != null && myLongitude != null) {
                        LatLng mylocation = new LatLng(myLatitude, myLongitude);
                        mMap.addMarker(new MarkerOptions().position(mylocation).title("Vous etes ici!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))).showInfoWindow();
                    }
                    for (int i = 0; i < ma.size(); i++) {
                        LatLng lat = new LatLng(ma.get(i).getLat(), ma.get(i).getLng());
                        mMap.addMarker(new MarkerOptions()
                                .title(ma.get(i).getTitre())
                                .snippet(ma.get(i).getDescription())
                                .position(lat)
                        ).showInfoWindow();
                        oldMarkerPoints.add(ma.get(i));
                    }
                }else {
                    Toast.makeText(MapsActivity.this,"la liste des favoris est vide", Toast.LENGTH_LONG).show();
                }

            }
        });

        FloatingActionButton FAB6 = (FloatingActionButton)findViewById(R.id.sat);
        FAB6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });

        arcMenuAndroid = (ArcMenu)findViewById(R.id.arcmenu_android_example_layout);
        arcMenuAndroid.setStateChangeListener(new StateChangeListener() {
            @Override
            public void onMenuOpened() {
                //something when menu is opened
            }
            @Override
            public void onMenuClosed() {
                // something when menu is closed
            }
        });

}
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        if(myLongitude == null || myLatitude == null) {
            LatLng sba = new LatLng(35.202807, -0.632714);
            mMap.addMarker(new MarkerOptions().position(sba).title("Bien Venu !").
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sba, 15));
        }else{
            LatLng sba = new LatLng(myLatitude,myLongitude);
            mMap.addMarker(new MarkerOptions().position(sba).title("Vous etes ici!").
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sba, 15));
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mMap.setMyLocationEnabled(true);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MA_PERMISSION_LOCATION);
            }
        }


        // connexion à la base de données
        data = new BaseMarqueur(context);

        try {
            data.open();

        } catch (Exception e) {
            Log.i("Bonjours", "Bonjours");
        }
        // Setting onclick event listener for the map
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Already two locations
                if (MarkerPoints.size() > 1)
                {
                    MarkerPoints.clear();
                    mMap.clear();
                    if(myLatitude != null && myLongitude != null)
                    {
                        LatLng mylocation = new LatLng(myLatitude, myLongitude);
                        mMap.addMarker(new MarkerOptions().position(mylocation).title("Vous etes ici!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))).showInfoWindow();
                    }
                    for (int i = 0; i < oldMarkerPoints.size(); i++)
                    {
                        LatLng lat = new LatLng(oldMarkerPoints.get(i).getLat(), oldMarkerPoints.get(i).getLng());
                        mMap.addMarker(new MarkerOptions()
                                .title(oldMarkerPoints.get(i).getTitre())
                                .snippet(oldMarkerPoints.get(i).getDescription())
                                .position(lat)
                        ).showInfoWindow();
                    }
                }
                LatLng point = marker.getPosition();
                // Adding new item to the ArrayList
                MarkerPoints.add(point);
                if(myLatitude == null || myLongitude == null)
                {
                    LatLng sba = new LatLng(35.202807, -0.632714);
                    MarkerPoints.add(sba);
                }else{
                    LatLng mylocation = new LatLng(myLatitude, myLongitude);
                    MarkerPoints.add(mylocation);
                }

                MarkerOptions options = new MarkerOptions();

                options.position(point);

                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                if (MarkerPoints.size() >= 2) {
                    LatLng origin = MarkerPoints.get(0);
                    LatLng dest = MarkerPoints.get(1);
                    String url = getUrl(origin, dest);
                    Log.d("onMapClick", url.toString());
                    FetchUrl FetchUrl = new FetchUrl();
                    FetchUrl.execute(url);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                }
                return true ;
            }
        });


    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MA_PERMISSION_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "cette application demande une permissions pour la loalisation", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection Failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()) {
            requestLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }


    // this is just a test

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                infoAnalyseur parser = new infoAnalyseur();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            String distance = "";
            String duration = "";

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }
            Toast.makeText(getApplicationContext(),"Distance:"+distance +"     , Durée:"+duration,Toast.LENGTH_LONG).show();
            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }

}
