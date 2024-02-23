/*
    ---------- THIS FILE WAS CREATED BY SAKU HAKAMÄKI ----------
 */

package fi.akahukas.projects.geospatial_maps.location_data;

/**
 * Enum representing the possible collecting modes for collecting
 * the location data samples. In mode SINGLE, a single sample is
 * collected when requested. In mode CONTINUOUS, a sample is
 * collected at certain intervals, e.g. every 5 seconds.
 */
public enum CollectingMode {
    SINGLE,
    CONTINUOUS
}
