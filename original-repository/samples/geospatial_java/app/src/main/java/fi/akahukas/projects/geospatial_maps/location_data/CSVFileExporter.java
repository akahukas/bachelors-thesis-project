/*
    ---------- THIS FILE WAS CREATED BY SAKU HAKAMÄKI ----------
 */

package fi.akahukas.projects.geospatial_maps.location_data;


import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.opencsv.CSVWriter;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Class responsible for creating and writing data held by
 * collected LocationDataSamples to CSV files.
 */
public class CSVFileExporter {

    private Activity startActivity_;

    private final int CREATE_FILE_CODE = 1;

    private final String DEFAULT_FILE_TYPE = "text/csv";
    private final String DEFAULT_FILENAME = "geospatial_samples.csv";

    /**
     * Constructor method.
     *
     * @param startActivity The Activity where the system file
     *                      picker is launched from.
     */
    public CSVFileExporter(Activity startActivity) {
        this.startActivity_ = startActivity;
    }

    /**
     * Starts the Android system file picker to allow the user
     * to select a location where to write the contents of the
     * CSV file.
     *
     * @return The request code that is returned in
     * onActivityResult() method of start Activity when the
     * system file picker exits.
     */
    public int createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(DEFAULT_FILE_TYPE);
        intent.putExtra(Intent.EXTRA_TITLE, DEFAULT_FILENAME);

        startActivityForResult(this.startActivity_, intent, CREATE_FILE_CODE, null);

        return CREATE_FILE_CODE;
    }

    /**
     * Writes the collected LocationDataSamples to the CSV file
     * created in {@link #createFile()} method.
     *
     * @param uri The URI of the created CSV file.
     * @param sampleSets The collected LocationDataSamples.
     * @return True if the write was successful, False otherwise.
     */
    public boolean writeToFile(@NonNull Uri uri, @NonNull ArrayList<ArrayList<LocationDataSample>> sampleSets) {

        try (OutputStream out = startActivity_.getContentResolver().openOutputStream(uri);
             OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {

            CSVWriter csvWriter = new CSVWriter(writer);

            csvWriter.writeAll(
                    formatOutputData(sampleSets)
            );

            return true;

        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Edits the collected LocationDataSamples to a format
     * which is expected in the output CSV file.
     *
     * @param sampleSets The collected LocationDataSamples.
     * @return The content to be written to the output CSV
     * file in a List of String Arrays.
     */
    private List<String[]> formatOutputData(ArrayList<ArrayList<LocationDataSample>> sampleSets) {
        List<String[]> data = new ArrayList<String[]>();

        // Header row.
        data.add(new String[] {
                "Set ID",
                "Sample ID",
                "Timestamp (hh:mm:ss:sss)",
                "Latitude (degrees)",
                "Longitude (degrees)",
                "Horizontal Accuracy (metres)",
                "Altitude (metres)",
                "Vertical Accuracy (metres)",
                "Quaternion",
                "Yaw Accuracy (degrees)"
        });

        for (int i = 0; i < sampleSets.size(); i++) {
            ArrayList<LocationDataSample> set = sampleSets.get(i);

            for (int j = 0; j < set.size(); j++) {
                LocationDataSample sample = set.get(j);

                // One row for each LocationDataSample.
                data.add(new String[] {
                        String.valueOf(i + 1), // Start from 1
                        String.valueOf(j + 1), // Start from 1
                        formatTimestamp(sample.getTimestamp()),
                        String.valueOf(sample.getLatitude()),
                        String.valueOf(sample.getLongitude()),
                        String.valueOf(sample.getHorizontalAccuracy()),
                        String.valueOf(sample.getAltitude()),
                        String.valueOf(sample.getVerticalAccuracy()),
                        Arrays.toString(sample.getQuaternion()),
                        String.valueOf(sample.getOrientationYawAccuracy())
                });
            }
        }
        return data;
    }

    /**
     * Formats a LocalTime object to a format which is
     * expected in the output CSV file.
     *
     * @param timestamp The LocalTime object to format.
     * @return The formatted time as String in format
     * hh:mm:ss:sss.
     */
    private String formatTimestamp(LocalTime timestamp) {
        return String.format(
                Locale.ENGLISH,
                "%02d:%02d:%02d:%d",
                timestamp.getHour(),
                timestamp.getMinute(),
                timestamp.getSecond(),
                (timestamp.getNano() / 1000000)
        );
    }
}
