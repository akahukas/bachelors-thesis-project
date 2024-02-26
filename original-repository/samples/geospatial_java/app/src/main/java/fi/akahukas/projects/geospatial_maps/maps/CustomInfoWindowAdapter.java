/*
    ---------- THIS FILE WAS CREATED BY SAKU HAKAMÄKI ----------
 */

package fi.akahukas.projects.geospatial_maps.maps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.ar.core.examples.java.geospatial.R;

/**
 * Class implementing a custom InfoWindowAdapter for the Marker
 * objects in the Google Map object.
 */
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View contentView;

    /**
     * Constructor method.
     *
     * @param context The context of the activity in which
     *                the info window is being inflated to.
     */
    public CustomInfoWindowAdapter(Context context) {
        contentView = LayoutInflater
                .from(context)
                .inflate(R.layout.custom_marker_info_window, null);
    }

    /**
     * Renders the text inside the info window.
     *
     * @param marker The Marker object whose window is
     *               being rendered.
     * @param view The View in which the window is rendered.
     */
    private void renderWindowText(Marker marker, View view) {
        String title = marker.getTitle();
        TextView textViewTitle = (TextView) view.findViewById(R.id.title);

        if (title != null) {
            textViewTitle.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView textViewSnippet = (TextView) view.findViewById(R.id.snippet);

        if (snippet != null) {
            textViewSnippet.setText(snippet);
        }
    }

    /**
     * Provides custom contents for the default info
     * window frame of a Marker.
     *
     * @param marker The Marker whose info window is
     *               being modified.
     * @return The view with the customized info
     *          window content.
     */
    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        renderWindowText(marker, contentView);
        return contentView;
    }

    /**
     * Provides a custom info window for a Marker.
     *
     * @param marker The Marker whose info window is
     *               being modified.
     * @return The customized info window.
     */
    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        renderWindowText(marker, contentView);
        return contentView;
    }
}
