/*
    ---------- THIS FILE WAS CREATED BY SAKU HAKAMÄKI ----------
 */

package fi.akahukas.projects.geospatial_maps.common;

import java.util.ArrayList;

/**
 * Class representing an observable integer value.
 */
public class ObservableInt {
    private int value_;
    private ArrayList<OnValueChangedListener<Integer>> listeners_;

    /**
     * Constructor method.
     *
     * @param value The initial value.
     */
    public ObservableInt(int value) {
        this.value_ = value;
        this.listeners_ = new ArrayList<>();
    }

    /**
     * Getter for the value.
     *
     * @return The current value of the object.
     */
    public int getValue() {
        return this.value_;
    }

    /**
     * Setter for the value.
     *
     * @param newValue The new value of the object.
     */
    public void setValue(int newValue) {
        if (this.value_ != newValue) {
            this.value_ = newValue;
            notifyListeners();
        }
    }

    /**
     * Add a new listener to the objects value.
     *
     * @param listener The listener that listens to the objects value.
     */
    public void addOnValueChangedListener(OnValueChangedListener<Integer> listener) {
        this.listeners_.add(listener);
    }

    /**
     * Remove a new listener from the objects value.
     *
     * @param listener The listener that listens to the objects value.
     */
    public void removeOnValueChangedListener(OnValueChangedListener<Integer> listener) {
        this.listeners_.remove(listener);
    }

    /**
     * Notifies all the listeners when the value of the object has changed.
     */
    private void notifyListeners() {
        for (OnValueChangedListener<Integer> listener : this.listeners_) {
            listener.onValueChanged(this.value_);
        }
    }
}
