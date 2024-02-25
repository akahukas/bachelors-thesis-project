/*
    ---------- THIS FILE WAS CREATED BY SAKU HAKAMÄKI ----------
 */

package fi.akahukas.projects.geospatial_maps.maps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.ar.core.examples.java.geospatial.R;

import java.util.ArrayList;
import java.util.Locale;

import fi.akahukas.projects.geospatial_maps.location_data.LocationDataSample;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private ArrayList<ArrayList<LocationDataSample>> locationDataSampleSets;
    private ArrayList<ArrayList<Marker>> locationMarkerSets;
    private ArrayList<ArrayList<Polyline>> locationPolylineSets;

    private final String EXTRA_TAG_LOCATION_DATA = "LOCATION_DATA";

    private final int DEFAULT_GROUP_ID = 0;
    private final int DEFAULT_ORDER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.map_menu);

        if (!areExtrasValid()) {
            locationDataSampleSets = new ArrayList<>();
            locationMarkerSets = new ArrayList<>();
            locationPolylineSets = new ArrayList<>();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Called when another activity comes into the foreground.
     * Clears the different sets related to the LocationDataSamples.
     */
    @Override
    protected void onPause() {
        super.onPause();

        clearSets();
    }

    /**
     * Called when this activity is no longer visible.
     * Clears the different sets related to the LocationDataSamples.
     */
    @Override
    protected void onStop() {
        super.onStop();

        clearSets();
    }

    /**
     * Called when this activity is finishing or being destroyed
     * by the system.
     * Clears the different sets related to the LocationDataSamples.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        clearSets();
    }

    /**
     * Clears the set containing the LocationDataSamples, as well
     * as the set of Marker and Polyline objects used to plot the
     * location data.
     */
    private void clearSets() {
        if (locationDataSampleSets != null) {
            locationDataSampleSets.clear();
            locationMarkerSets.clear();
            locationPolylineSets.clear();
        }
    }

    /**
     * Checks whether the sent extras are valid or not.
     * If valid, assigns extras to corresponding attributes.
     *
     * @return True if sent extras are valid. False if not.
     */
    private boolean areExtrasValid() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            Object extraObject = extras.get(EXTRA_TAG_LOCATION_DATA);

            if (extraObject instanceof ArrayList<?>) {
                ArrayList<?> outerList = (ArrayList<?>) extraObject;
                locationDataSampleSets = new ArrayList<>();

                if (!outerList.isEmpty()) {
                    for (int i = 0; i < outerList.size(); i++) {
                        if (!(outerList.get(i) instanceof ArrayList<?>)) {
                            return false;
                        } else {
                            ArrayList<?> innerList = (ArrayList<?>) outerList.get(i);

                            if (!innerList.isEmpty()) {
                                locationDataSampleSets.add(new ArrayList<>());

                                for (int j = 0; j < innerList.size(); j++) {
                                    Object object = innerList.get(j);

                                    if ((object instanceof LocationDataSample)) {
                                        LocationDataSample sample = (LocationDataSample) object;

                                        locationDataSampleSets.get(i).add(sample);

                                        if ((i == outerList.size() - 1) &&
                                                (j == outerList.size() - 1)) {

                                            locationMarkerSets = new ArrayList<>();
                                            locationPolylineSets = new ArrayList<>();

                                            return true;
                                        }
                                    } else {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Modifies the options menu inside the MapsActivity.
     * Adds new SubMenu for setting the visibility of the sets of plotted
     * LocationDataSamples.
     *
     * @param menu The options menu in which you place your items.
     *
     * @return True if the menu was modified successfully, i.e. the menu is shown.
     *          False if the modification was unsuccessful, i.e. the menu is not shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu subMenu = menu.addSubMenu(
                Menu.NONE,
                R.id.menu_map_set_visibility_status,
                Menu.NONE,
                R.string.menu_map_set_visibility_status
        );

        for (int i = 0; i < locationDataSampleSets.size() ; i++) {

            String title = String.format(Locale.ENGLISH, "Set %d", i + 1);

            subMenu.add(
                    DEFAULT_GROUP_ID, // All at the same menu level.
                    i, // itemId
                    DEFAULT_ORDER,
                    title
            ).setCheckable(true).setChecked(true);
        }

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
