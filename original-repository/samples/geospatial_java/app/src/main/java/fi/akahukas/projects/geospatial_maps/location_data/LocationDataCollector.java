/*
    ---------- THIS FILE WAS CREATED BY SAKU HAKAMÄKI ----------
 */

package fi.akahukas.projects.geospatial_maps.location_data;

import com.google.ar.core.GeospatialPose;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fi.akahukas.projects.geospatial_maps.common.ObservableEnum;
import fi.akahukas.projects.geospatial_maps.common.ObservableInt;

/**
 * Class responsible for handling location data collection.
 */
public class LocationDataCollector {

    private boolean isCollecting_;
    private ArrayList<LocationDataSample> currentDataSampleSet_;
    private ArrayList<ArrayList<LocationDataSample>> allDataSampleSets_;
    private GeospatialPose currentGeospatialPose_;
    private LocalTime currentTimestamp_;

    private ObservableInt currentSetSize_;
    private ObservableInt totalSetsSize_;
    private ObservableEnum<CollectingMode> currentCollectingMode_;

    private ScheduledExecutorService scheduler_;

    private final CollectingMode INITIAL_COLLECTING_MODE = CollectingMode.CONTINUOUS;
    private final int LOCATION_UPDATE_INITIAL_DELAY = 1; // seconds
    private final int LOCATION_UPDATE_INTERVAL = 5; // seconds

    /**
     * Constructor method.
     */
    public LocationDataCollector() {
        this.isCollecting_ = false;
        this.currentDataSampleSet_ = new ArrayList<>();
        this.allDataSampleSets_ = new ArrayList<>();
        this.currentGeospatialPose_ = null;
        this.currentTimestamp_ = null;
        this.currentCollectingMode_ = new ObservableEnum<>(INITIAL_COLLECTING_MODE);
        this.currentSetSize_ = new ObservableInt(currentDataSampleSet_.size());
        this.totalSetsSize_ = new ObservableInt(allDataSampleSets_.size());
    }

    /**
     * Stores the latest geospatial pose and timestamp.
     *
     * @param geospatialPose The latest geospatial pose.
     * @param timestamp The latest timestamp.
     */
    public void updateCurrentGeospatialPose(GeospatialPose geospatialPose, LocalTime timestamp) {
        this.currentGeospatialPose_ = geospatialPose;
        this.currentTimestamp_ = timestamp;
    }

    /**
     * Starts the location data collection process with current CollectingMode.
     */
    public void startCollecting() {
        if (this.currentCollectingMode_.getValue() == CollectingMode.CONTINUOUS) {
            this.scheduler_ = Executors.newScheduledThreadPool(1);

            Runnable locationCaller = new Runnable() {
                @Override
                public void run() {
                    addNewLocationDataSample(currentGeospatialPose_, currentTimestamp_);
                }
            };

            ScheduledFuture<?> callerHandle = this.scheduler_.scheduleAtFixedRate(
                    locationCaller,
                    LOCATION_UPDATE_INITIAL_DELAY,
                    LOCATION_UPDATE_INTERVAL,
                    TimeUnit.SECONDS
            );
            this.isCollecting_ = true;
        } else if (this.currentCollectingMode_.getValue() == CollectingMode.SINGLE) {
            addNewLocationDataSample(this.currentGeospatialPose_, this.currentTimestamp_);
            this.isCollecting_ = true;
        } else {
            return;
        }
    }

    /**
     * Stops the location data collection process.
     */
    public void stopCollecting() {
        if (this.currentCollectingMode_.getValue() == CollectingMode.SINGLE) {
            this.isCollecting_ = false;
            return;
        }

        if (this.scheduler_ == null) { return; }

        this.scheduler_.shutdownNow();

        this.isCollecting_ = false;
    }

    /**
     * Adds a new LocationDataSample to the current set of samples.
     *
     * @param geospatialPose The geospatial pose of the sample to be added.
     * @param timestamp The timestamp of the sample to be added.
     */
    private void addNewLocationDataSample(GeospatialPose geospatialPose, LocalTime timestamp) {
        if (geospatialPose != null) {
            this.currentDataSampleSet_.add(new LocationDataSample(geospatialPose, timestamp));
            this.currentSetSize_.setValue(currentDataSampleSet_.size());
        }
    }

    /**
     * Saves the current set of samples to the set of sample sets.
     */
    public void saveCurrentSet() {
        if (!this.currentDataSampleSet_.isEmpty()) {
            // Remove possible already saved copy first.
            this.allDataSampleSets_.remove(this.currentDataSampleSet_);
            this.allDataSampleSets_.add(this.currentDataSampleSet_);

            this.totalSetsSize_.setValue(this.allDataSampleSets_.size());
        }
    }

    /**
     * Saves the current set of samples to the set of sample sets
     * and replaces it with an empty set.
     */
    public void addNewDataSampleSet() {
        if (!this.currentDataSampleSet_.isEmpty()) {
            saveCurrentSet();

            this.currentDataSampleSet_ = new ArrayList<>();

            this.currentSetSize_.setValue(this.currentDataSampleSet_.size());
        }
    }

    /**
     * Removes all samples from the current set.
     */
    public void clearCurrentDataSampleSet() {
        this.currentDataSampleSet_.clear();

        this.currentSetSize_.setValue(this.currentDataSampleSet_.size());
    }

    /**
     * Removes all sets from the set of sample sets.
     */
    public void clearAllDataSampleSets() {
        this.allDataSampleSets_.clear();

        this.totalSetsSize_.setValue(this.allDataSampleSets_.size());
    }

    /**
     * Changes the current CollectingMode to a new mode.
     * @param newMode The new CollectingMode.
     */
    public void changeCurrentCollectingMode(CollectingMode newMode) {
        stopCollecting();

        this.currentCollectingMode_.setValue(newMode);
    }

    /**
     * Getter for the collection status.
     *
     * @return The collection status.
     *          True if collecting samples.
     *          False if not collecting samples.
     */
    public boolean isCollecting() {
        return this.isCollecting_;
    }

    /**
     * Getter for the current set of samples.
     *
     * @return A set containing the current samples.
     */
    public ArrayList<LocationDataSample> getCurrentDataSampleSet() {
        return this.currentDataSampleSet_;
    }

    /**
     * Getter for the set of sample sets.
     *
     * @return A set containing all the sample sets.
     */
    public ArrayList<ArrayList<LocationDataSample>> getDataSampleSets() {
        return this.allDataSampleSets_;
    }

    /**
     * Getter for the number of samples in the current set.
     *
     * @return Number of samples in the current set.
     */
    public ObservableInt getCurrentSetSize() {
        return this.currentSetSize_;
    }

    /**
     * Getter for the number of sample sets in total.
     *
     * @return The number of saved sets in total.
     */
    public ObservableInt getTotalSetsSize() {
        return this.totalSetsSize_;
    }

    /**
     * Getter for the current CollectingMode.
     *
     * @return The current CollectingMode.
     */
    public ObservableEnum<CollectingMode> getCurrentCollectingMode() {
        return this.currentCollectingMode_;
    }
}
