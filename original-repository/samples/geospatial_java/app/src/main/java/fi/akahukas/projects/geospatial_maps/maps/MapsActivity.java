/*
    ---------- THIS FILE WAS CREATED BY SAKU HAKAMÄKI ----------
 */

package fi.akahukas.projects.geospatial_maps.maps;

import androidx.annotation.NonNull;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.ar.core.examples.java.geospatial.R;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fi.akahukas.projects.geospatial_maps.location_data.LocationDataSample;

/**
 * Activity adding an implementation of Google Maps with a
 * possibility to plot and inspect collected location data.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap_;

    private ArrayList<ArrayList<LocationDataSample>> locationDataSampleSets_;
    private ArrayList<ArrayList<Marker>> locationMarkerSets_;
    private ArrayList<ArrayList<Polyline>> locationPolylineSets_;

    private final String EXTRA_TAG_LOCATION_DATA = "LOCATION_DATA";

    private final int DEFAULT_GROUP_ID = 0;
    private final int DEFAULT_ORDER = 0;
    private final int DEFAULT_ZOOM_LEVEL = 10;

    private final List<Float> MARKER_COLORS = Arrays.asList(
            BitmapDescriptorFactory.HUE_AZURE,
            BitmapDescriptorFactory.HUE_BLUE,
            BitmapDescriptorFactory.HUE_CYAN,
            BitmapDescriptorFactory.HUE_GREEN,
            BitmapDescriptorFactory.HUE_MAGENTA,
            BitmapDescriptorFactory.HUE_ORANGE,
            BitmapDescriptorFactory.HUE_RED,
            BitmapDescriptorFactory.HUE_ROSE,
            BitmapDescriptorFactory.HUE_VIOLET,
            BitmapDescriptorFactory.HUE_YELLOW
    );

    private final LatLng DEFAULT_LOCATION_TAMPERE_FINLAND = new LatLng(
            61.4979482599281, 23.76363183871995
    );

    private final LatLng NO_LOCATION = new LatLng(-1, -1);


    /**
     * Called when this activity is launched or when the user navigates to the
     * activity. Initializes the content inside this activity.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.
     *     <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.map_menu);

        if (!areExtrasValid()) {
            locationDataSampleSets_ = new ArrayList<>();
            locationMarkerSets_ = new ArrayList<>();
            locationPolylineSets_ = new ArrayList<>();
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
        if (locationDataSampleSets_ != null) {
            locationDataSampleSets_.clear();
            locationMarkerSets_.clear();
            locationPolylineSets_.clear();
        }
    }

    /**
     * Checks whether the sent extras are valid or not.
     *
     * @return True if sent extras are valid. False if not.
     */
    private boolean areExtrasValid() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            Object extraObject = extras.get(EXTRA_TAG_LOCATION_DATA);

            return isLocationDataValid(extraObject);
        }
        return false;
    }

    /**
     * Checks whether the LocationDataSamples sent in extras
     * are valid or not. If valid, assigns extras to
     * corresponding attributes.
     *
     * @param extraObject The object possibly containing the
     *                    location data.
     * @return True if sent location data is valid. False if not.
     */
    private boolean isLocationDataValid(Object extraObject) {
        if (extraObject instanceof ArrayList<?>) {
            ArrayList<?> outerList = (ArrayList<?>) extraObject;
            locationDataSampleSets_ = new ArrayList<>();

            if (!outerList.isEmpty()) {
                for (int i = 0; i < outerList.size(); i++) {
                    if (!(outerList.get(i) instanceof ArrayList<?>)) {
                        return false;
                    } else {
                        ArrayList<?> innerList = (ArrayList<?>) outerList.get(i);

                        if (!innerList.isEmpty()) {
                            locationDataSampleSets_.add(new ArrayList<>());

                            for (int j = 0; j < innerList.size(); j++) {
                                Object object = innerList.get(j);

                                if ((object instanceof LocationDataSample)) {
                                    LocationDataSample sample = (LocationDataSample) object;

                                    locationDataSampleSets_.get(i).add(sample);

                                    if ((i == outerList.size() - 1) &&
                                            (j == innerList.size() - 1)) {

                                        locationMarkerSets_ = new ArrayList<>();
                                        locationPolylineSets_ = new ArrayList<>();

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

        for (int i = 0; i < locationDataSampleSets_.size() ; i++) {

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
     * Handles the event emitted from the options menu.
     *
     * @param menuItem The menu item that was selected.
     *
     * @return True if the event was consumed successfully.
     *          False if not.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.map_type) {  // Ignore parent item.
            return true;
        } else if (menuItem.getItemId() == R.id.type_normal) {

            mMap_.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            return true;
        } else if (menuItem.getItemId() == R.id.type_satellite) {

            mMap_.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            return true;
        } else if (menuItem.getItemId() == R.id.type_terrain) {

            mMap_.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

            return true;
        } else if (menuItem.getItemId() == R.id.type_hybrid) {

            mMap_.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            return true;
        } else if (menuItem.getItemId() == R.id.menu_map_set_visibility_status) {  // Ignore parent item.
            return true;
        }

        int itemId = menuItem.getItemId();

        boolean newVisibilityState = !menuItem.isChecked();

        setMarkerCollectionVisibility(itemId, newVisibilityState);

        menuItem.setChecked(newVisibilityState);

        return true;
    }

    /**
     * Called when the Google Map element is ready to be used.
     * Customizes and modifies the content displayed on the map
     * and the map itself.
     *
     * @param googleMap The GoogleMap object that is displayed
     *                  in the activity.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap_ = googleMap;

        mMap_.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap_.getUiSettings().setMapToolbarEnabled(false);

        mMap_.moveCamera(CameraUpdateFactory.zoomTo(15));

        if (!locationDataSampleSets_.isEmpty()) {
            plotLocationData();

            moveCameraToFirstMarker(15);
        } else {
            moveCamera(DEFAULT_LOCATION_TAMPERE_FINLAND, DEFAULT_ZOOM_LEVEL);
        }
    }

    /**
     * Plots the collected location data on the map.
     * Displays each location data sample with a Marker
     * and connects all the Markers in the same set with
     * Polyline objects. The location data related to a
     * specific sample is displayed in an InfoWindow on
     * top of the corresponding Marker.
     */
    private void plotLocationData() {
        mMap_.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));

        int markerColorIndex = 0;

        for (int i = 0; i < locationDataSampleSets_.size(); i++) {
            ArrayList<LocationDataSample> set = locationDataSampleSets_.get(i);

            ArrayList<Marker> markerSet = new ArrayList<>();

            ArrayList<Polyline> lineSet = new ArrayList<>();

            LatLng prevLocation = NO_LOCATION; // Initial value for each set.

            for (int j = 0; j < set.size(); j++) {
                LocationDataSample sample = set.get(j);

                LatLng location = new LatLng(sample.getLatitude(), sample.getLongitude());

                LocalTime timestamp = sample.getTimestamp();

                String snippet = String.format(Locale.ENGLISH,
                        "Time: %02d:%02d:%02d.%d\n" + // Hours:Minutes:Seconds.Milliseconds
                                "Latitude: %f˚\n" +
                                "Longitude: %f˚\n" +
                                "Horizontal Accuracy: %fm\n" +
                                "Altitude: %fm\n" +
                                "Vertical Accuracy: %fm\n" +
                                "Yaw Accuracy: %f˚",
                        timestamp.getHour(),
                        timestamp.getMinute(),
                        timestamp.getSecond(),
                        (timestamp.getNano() / 1000000), // Convert to milliseconds.
                        sample.getLatitude(),
                        sample.getLongitude(),
                        sample.getHorizontalAccuracy(),
                        sample.getAltitude(),
                        sample.getVerticalAccuracy(),
                        sample.getOrientationYawAccuracy()
                );

                if (i >= MARKER_COLORS.size()) {
                    markerColorIndex = 0;
                }

                Marker marker = mMap_.addMarker(new MarkerOptions()
                        .position(location)
                        .title(String.format(
                                Locale.ENGLISH,
                                "Set #%d | Sample #%d",
                                i+1,
                                j+1
                        ))
                        .icon(BitmapDescriptorFactory.defaultMarker(
                                MARKER_COLORS.get(markerColorIndex)
                        ))
                        .snippet(snippet)
                );

                markerSet.add(marker);

                if (!(prevLocation == NO_LOCATION)) {
                    Polyline polyline = mMap_.addPolyline(new PolylineOptions()
                            .clickable(false)
                            .add(
                                    prevLocation,
                                    location
                            )
                    );

                    polyline.setStartCap(new RoundCap());
                    polyline.setEndCap(new RoundCap());

                    lineSet.add(polyline);
                }

                prevLocation = location;
            }

            locationMarkerSets_.add(markerSet);

            locationPolylineSets_.add(lineSet);

            markerColorIndex++;
        }
    }

    /**
     * Sets the Markers and Polyline objects related to a specific
     * set of LocationDataSamples either to visible or invisible.
     *
     * @param setId The id of the set whose visibility is altered.
     * @param visible True if the set is set to visible. False if
     *                set to invisible.
     */
    private void setMarkerCollectionVisibility(int setId, boolean visible) {
        ArrayList<Marker> markerSet = locationMarkerSets_.get(setId);

        for (Marker marker : markerSet) {
            marker.setVisible(visible);
        }

        ArrayList<Polyline> lineSet = locationPolylineSets_.get(setId);

        for (Polyline line : lineSet) {
            line.setVisible(visible);
        }
    }

    /**
     * Moves the camera of the GoogleMap object to specific
     * coordinates and to a specific zoom level.
     *
     * @param coordinates The new coordinates where the camera is moved.
     * @param zoomLevel The new zoom level where the camera zoom is set.
     */
    private void moveCamera(LatLng coordinates, int zoomLevel) {
        mMap_.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
        mMap_.moveCamera(CameraUpdateFactory.zoomTo(zoomLevel));
    }

    /**
     * Moves the camera of the GoogleMap object to the Marker
     * corresponding to the first location data sample of the
     * first collected set of samples.
     *
     * @param zoomLevel The new zoom level where the camera zoom is set.
     */
    private void moveCameraToFirstMarker(int zoomLevel) {
        if (!locationDataSampleSets_.isEmpty()) {
            LocationDataSample first = locationDataSampleSets_.get(0).get(0);

            LatLng firstLocation = new LatLng(first.getLatitude(), first.getLongitude());

            moveCamera(firstLocation, zoomLevel);
        }
    }
}
