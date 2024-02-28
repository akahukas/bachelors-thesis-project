/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.ar.core.examples.java.geospatial;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

/** A DialogFragment for the Privacy Notice Dialog Box. */
public class PrivacyNoticeDialogFragment extends DialogFragment {

  // -------------------- ADDED BY SAKU HAKAMÄKI | START --------------------

  private static boolean isShowRequestedFromUser_ = false;

  // -------------------- ADDED BY SAKU HAKAMÄKI |  END  --------------------

  /** Listener for a privacy notice response. */
  public interface NoticeDialogListener {

    /** Invoked when the user accepts sharing experience. */
    void onDialogPositiveClick(DialogFragment dialog);
  }

  NoticeDialogListener noticeDialogListener;

  // -------------------- MODIFIED BY SAKU HAKAMÄKI | START --------------------

  static PrivacyNoticeDialogFragment createDialog(boolean isShowRequestFromUser) {
    isShowRequestedFromUser_ = isShowRequestFromUser;

    PrivacyNoticeDialogFragment dialogFragment = new PrivacyNoticeDialogFragment();
    return dialogFragment;
  }

  // -------------------- MODIFIED BY SAKU HAKAMÄKI |  END  --------------------

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    // Verify that the host activity implements the callback interface
    try {
      noticeDialogListener = (NoticeDialogListener) context;
    } catch (ClassCastException e) {
      throw new AssertionError("Must implement NoticeDialogListener", e);
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    noticeDialogListener = null;
  }

  // -------------------- MODIFIED BY SAKU HAKAMÄKI | START --------------------

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    // The initial AlertDialog.
    if (!isShowRequestedFromUser_) {

      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
      builder
              .setTitle(R.string.share_experience_title)
              .setMessage(R.string.share_experience_message)
              .setPositiveButton(
                      R.string.agree_to_share,
                      new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                          // Send the positive button event back to the host activity
                          noticeDialogListener.onDialogPositiveClick(PrivacyNoticeDialogFragment.this);
                        }
                      })
              .setNegativeButton(
                      R.string.learn_more,
                      new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                          Intent browserIntent =
                                  new Intent(
                                          Intent.ACTION_VIEW, Uri.parse(getString(R.string.learn_more_url)));
                          getActivity().startActivity(browserIntent);
                        }
                      });
      return builder.create();

    } else { // The dialog opened by user from the menu.
      Dialog dialog = new Dialog(getActivity(), R.style.AlertDialogCustom);
      dialog.setContentView(R.layout.custom_alert_dialog);

      return dialog;
    }
  }

  // -------------------- MODIFIED BY SAKU HAKAMÄKI |  END  --------------------

  // -------------------- ADDED BY SAKU HAKAMÄKI | START --------------------

  /**
   * Called when the Fragment is visible to the user.
   */
  @Override
  public void onStart() {
    super.onStart();

    TextView textView_1 = (TextView) getDialog().findViewById(R.id.privacy_notice_play_services_for_ar);
    TextView textView_2 = (TextView) getDialog().findViewById(R.id.privacy_notice_anchors_geospatial);

    // Use the setMovementMethod and LinkMovementMethod to set links to clickable.
    if (textView_1 != null && textView_2 != null) {
      textView_1.setMovementMethod(LinkMovementMethod.getInstance());
      textView_2.setMovementMethod(LinkMovementMethod.getInstance());
    }
  }

  // -------------------- ADDED BY SAKU HAKAMÄKI |  END  --------------------


}
