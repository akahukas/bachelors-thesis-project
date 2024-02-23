/*
    ---------- THIS FILE WAS CREATED BY SAKU HAKAMÄKI ----------
 */

package fi.akahukas.projects.geospatial_maps.common;

import java.util.ArrayList;

/**
 * A class representing an observable Enum value.
 *
 * @param <T> The type of the Enum.
 */
public class ObservableEnum<T extends Enum<T>> {
    private T value_;
    private ArrayList<OnValueChangedListener<T>> listeners_;

    /**
     * Constructor method.
     *
     * @param value The initial value.
     */
    public ObservableEnum(T value) {
        this.value_ = value;
        this.listeners_ = new ArrayList<>();
    }

    /**
     * Getter for the value.
     *
     * @return The current value of the Enum.
     */
    public T getValue() {
        return this.value_;
    }

    /**
     * Setter for the value.
     *
     * @param newValue The new value of the enum.
     */
    public void setValue(T newValue) {
        if (this.value_ != newValue) {
            this.value_ = newValue;
            notifyListeners();
        }
    }

    /**
     * Add a new listener to the Enum value.
     *
     * @param listener The listener that listens to the Enum value.
     */
    public void addOnValueChangedListener(OnValueChangedListener<T> listener) {
        this.listeners_.add(listener);
    }

    /**
     * Remove a new listener from the Enum value.
     *
     * @param listener The listener that listens to the Enum value.
     */
    public void removeOnValueChangedListener(OnValueChangedListener<T> listener) {
        this.listeners_.remove(listener);
    }

    /**
     * Notifies all the listeners when the value of the Enum has changed.
     */
    private void notifyListeners() {
        for (OnValueChangedListener<T> listener : this.listeners_) {
            listener.onValueChanged(this.value_);
        }
    }
}
