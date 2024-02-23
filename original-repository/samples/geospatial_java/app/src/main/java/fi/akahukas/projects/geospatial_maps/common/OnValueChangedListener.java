/*
    ---------- THIS FILE WAS CREATED BY SAKU HAKAMÄKI ----------
 */

package fi.akahukas.projects.geospatial_maps.common;

/**
 * An interface representing a generic type onValueChangedListener.
 *
 * @param <T> The type of the value that has changed.
 */
public interface OnValueChangedListener<T> {

    /**
     * Handles functionalities after the value has changed.
     *
     * @param newValue The new value of the variable of interest.
     */
    void onValueChanged(T newValue);
}
