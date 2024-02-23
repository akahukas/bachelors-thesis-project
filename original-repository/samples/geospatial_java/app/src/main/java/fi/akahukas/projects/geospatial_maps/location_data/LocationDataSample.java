/*
    ---------- THIS FILE WAS CREATED BY SAKU HAKAMÄKI ----------
 */

package fi.akahukas.projects.geospatial_maps.location_data;

import com.google.ar.core.GeospatialPose;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * Class representing a single location data sample.
 * Contains different location related information
 * as well as a timestamp.
 */
public class LocationDataSample implements Serializable {

    private double latitude_;
    private double longitude_;
    private double horizontalAccuracy_;
    private double altitude_;
    private double verticalAccuracy_;
    private float[] quaternion_;
    private double orientationYawAccuracy_;
    private LocalTime timestamp_;

    /**
     * Constructor method.
     *
     * @param geospatialPose The geospatial pose that is saved to the sample.
     * @param timestamp The timestamp that is saved to the sample.
     */
    public LocationDataSample(GeospatialPose geospatialPose, LocalTime timestamp) {
        this.latitude_ = geospatialPose.getLatitude();
        this.longitude_ = geospatialPose.getLongitude();
        this.horizontalAccuracy_ = geospatialPose.getHorizontalAccuracy();
        this.altitude_ = geospatialPose.getAltitude();
        this.verticalAccuracy_ = geospatialPose.getVerticalAccuracy();
        this.quaternion_ = geospatialPose.getEastUpSouthQuaternion();
        this.orientationYawAccuracy_ = geospatialPose.getOrientationYawAccuracy();
        this.timestamp_ = timestamp;
    }

    /**
     * Getter for latitude.
     *
     * @return The latitude value of the sample.
     */
    public double getLatitude() {
        return this.latitude_;
    }

    /**
     * Getter for longitude.
     *
     * @return The longitude value of the sample.
     */
    public double getLongitude() {
        return this.longitude_;
    }

    /**
     * Getter for horizontal accuracy.
     *
     * @return The horizontal accuracy value of the sample.
     */
    public double getHorizontalAccuracy() {
        return this.horizontalAccuracy_;
    }

    /**
     * Getter for altitude.
     *
     * @return The altitude value of the sample.
     */
    public double getAltitude() {
        return this.altitude_;
    }

    /**
     * Getter for vertical accuracy.
     *
     * @return The vertical accuracy value of the sample.
     */
    public double getVerticalAccuracy() {
        return this.verticalAccuracy_;
    }

    /**
     * Getter for quaternion.
     *
     * @return The quaternion value of the sample.
     */
    public float[] getQuaternion() {
        return this.quaternion_;
    }

    /**
     * Getter for yaw accuracy.
     *
     * @return The yaw accuracy value of the sample.
     */
    public double getOrientationYawAccuracy() {
        return this.orientationYawAccuracy_;
    }

    /**
     * Getter for timestamp.
     *
     * @return The timestamp value of the sample.
     */
    public LocalTime getTimestamp() {
        return this.timestamp_;
    }
}
