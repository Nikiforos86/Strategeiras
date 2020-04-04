package gr.stratego.patrastournament.me.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.core.content.ContextCompat;
import android.util.DisplayMetrics;

import com.google.android.gms.maps.model.LatLng;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import gr.stratego.patrastournament.me.StrategoApplication;

public class GeneralUtils {
    public static int getProperColor(int colorId) {
        return ContextCompat.getColor(StrategoApplication.getContext(), colorId);
    }

    public static Drawable getProperDrawable(int drawableId) {
        return ContextCompat.getDrawable(StrategoApplication.getContext(), drawableId);
    }

    public static Drawable getThemedIcon(int drawableId, final Activity activity) {
        int[] attrs = new int[]{drawableId};
        TypedArray ta = activity.obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = ta.getDrawable(0 /* index */);
        ta.recycle();
        return drawableFromTheme;
    }

    public static ColorDrawable getProperColorDrawable(int colorId) {
        return new ColorDrawable(getProperColor(colorId));
    }

    public static int convertPxtoDp(int px) {
        final float scale = StrategoApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int convertDptoPx(float dp) {
        // Get the screen's density scale
        final float scale = StrategoApplication.getContext().getResources().getDisplayMetrics().density;
        // Convert the DPs to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

    //Navigate to Google Play Store
//    public static void nagivateToGooglePlayStore(Activity activity) {
//        if (activity == null) {
//            return;
//        }
//        try {
//            DSQApplication.LogAnswersEvent(FirebaseEventNames.USER_NAVIGATED_TO_RATE_AT_STORE);
//            // Provide a link for the user to download the app
//            Intent intentGooglePlay = new Intent(Intent.ACTION_VIEW);
//            intentGooglePlay.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intentGooglePlay.setData(Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID));
//            activity.startActivity(intentGooglePlay);
//        } catch (Exception e) {
//        }
//    }


//
//    public static void setProperUserAvatar(ImageView imageView, TextView textView) {
//        if (imageView == null || textView == null) {
//            return;
//        }
//        if (StringUtils.isNullOrEmpty(UserStore.getInstance().getUserAvatar())) {
//            textView.setVisibility(View.VISIBLE);
//            imageView.setVisibility(View.GONE);
//            textView.setText(UserStore.getInstance().getUserInitials());
//        } else {
//            final File imageFile = new File(UserStore.getInstance().getUserAvatar());
//            GlideApp.with(DSQApplication.getContext())
//                    .load(imageFile)
//                    .apply(RequestOptions.circleCropTransform())
//                    .into(imageView);
//            imageView.setVisibility(View.VISIBLE);
//            textView.setVisibility(View.GONE);
//        }
//    }

//    public static void setProperUserCircularAvatar(ImageView imageView, boolean male) {
//        if (imageView == null) {
//            return;
//        }
//
//        String maleAvatar = "https://www.w3schools.com/howto/img_avatar.png";
//        String femaleAvatar = "https://www.w3schools.com/howto/img_avatar2.png";
//        String url = maleAvatar;
//        if (StringUtils.isNullOrEmpty(UserStore.getInstance().getUserAvatar())) {
//            if (!male) {
//                url = femaleAvatar;
//            }
//            GlideApp.with(DSQApplication.getContext())
//                    .load(url)
//                    .apply(RequestOptions.circleCropTransform())
//                    .into(imageView);
//            imageView.setVisibility(View.VISIBLE);
//        } else {
//            final File imageFile = new File(UserStore.getInstance().getUserAvatar());
//            GlideApp.with(DSQApplication.getContext())
//                    .load(imageFile)
//                    .apply(RequestOptions.circleCropTransform())
//                    .into(imageView);
//            imageView.setVisibility(View.VISIBLE);
//        }
//
//    }

    public static void startNavigationToLatLng(WeakReference<Activity> activity, LatLng currentPosition, LatLng position) {
//        DSQApplication.LogAnswersEvent(FirebaseEventNames.NAVIGATE_TO_LOCATOR_POINT);
        try {
            Intent intent = null;
            if (currentPosition == null) {
                intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" +
                                position.latitude + "," + position.longitude));
            } else {
                intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" +
                                currentPosition.latitude + "," +
                                currentPosition.longitude + "&daddr=" +
                                position.latitude + "," + position.longitude));
            }
            if (intent == null) {
                return;
            }
            List<ResolveInfo> activities = activity.get().getPackageManager().queryIntentActivities(intent, 0);
            boolean isIntentSafe = activities.size() > 0;
            if (isIntentSafe) {
                activity.get().startActivity(intent);
            }
        } catch (Exception e) {
        }
    }



//    public static void showErrorDialog(android.support.v4.app.Fragment fragment, String errorMessage) {
//        if (fragment == null || fragment.getActivity() == null) {
//            return;
//        }
//        showErrorDialog(fragment.getActivity(), errorMessage);
//    }

//    public static void showErrorDialog(Activity activity, String errorMessage) {
//        try {
//            if (StringUtils.isNullOrEmpty(errorMessage)) {
//                errorMessage = DSQApplication.getContext().getString(R.string.generalFailureError);
//            }
//
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
//            // set title
//            alertDialogBuilder.setTitle(activity.getString(R.string.app_name));
//            // set dialog message
//            alertDialogBuilder
//                    .setMessage(errorMessage)
//                    .setCancelable(false)
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            // if this button is clicked, close
//                            // current activity
//                        }
//                    });
//
//            // create alert dialog
//            final AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                @Override
//                public void onShow(DialogInterface arg0) {
//                    try {
//                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(DSQApplication.getContext(), R.color.colorPrimary));
//                    } catch (Exception e) {
//                    }
//                }
//            });
//            // show it
//            alertDialog.show();
//        } catch (Exception e) {
//        }
//    }

//    public static void showErrorDialog(Activity activity, String errorMessage, final DialogOnClickListener mListener) {
//        try {
//            if (StringUtils.isNullOrEmpty(errorMessage)) {
//                errorMessage = DSQApplication.getContext().getString(R.string.generalFailureError);
//            }
//
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
//            // set title
//            alertDialogBuilder.setTitle(activity.getString(R.string.app_name));
//            // set dialog message
//            alertDialogBuilder
//                    .setMessage(errorMessage)
//                    .setCancelable(false)
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            mListener.onOkClicked();
//                            // if this button is clicked, close
//                            // current activity
//                        }
//                    });
//
//            // create alert dialog
//            final AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                @Override
//                public void onShow(DialogInterface arg0) {
//                    try {
//                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(DSQApplication.getContext(), R.color.colorPrimary));
//                    } catch (Exception e) {
//                    }
//                }
//            });
//            // show it
//            alertDialog.show();
//        } catch (Exception e) {
//        }
//    }

//    public static void showErrorDialogWithOnlyOk(Activity activity, String title, String errorMessage, final DialogOnClickListener mListener) {
//        try {
//            if (StringUtils.isNullOrEmpty(errorMessage)) {
//                errorMessage = DSQApplication.getContext().getString(R.string.generalFailureError);
//            }
//
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
//            // set title
//            alertDialogBuilder.setTitle(title);
//            // set dialog message
//            alertDialogBuilder
//                    .setMessage(errorMessage)
//                    .setCancelable(false)
//                    .setPositiveButton(activity.getResources().getString(R.string.ok_text), new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            mListener.onOkClicked();
//                            // if this button is clicked, close
//                            // current activity
//                        }
//                    });
//            // create alert dialog
//            final AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                @Override
//                public void onShow(DialogInterface arg0) {
//                    try {
//                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(DSQApplication.getContext(), R.color.colorPrimaryDark));
//                    } catch (Exception e) {
//                    }
//                }
//            });
//            // show it
//            alertDialog.show();
//        } catch (Exception e) {
//        }
//    }

//    public static void showErrorDialog(Activity activity, String title, String errorMessage, final DialogOnClickListener mListener) {
//        try {
//            if (StringUtils.isNullOrEmpty(errorMessage)) {
//                errorMessage = DSQApplication.getContext().getString(R.string.generalFailureError);
//            }
//
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
//            // set title
//            alertDialogBuilder.setTitle(title);
//            // set dialog message
//            alertDialogBuilder
//                    .setMessage(errorMessage)
//                    .setCancelable(false)
//                    .setPositiveButton(activity.getResources().getString(R.string.sure_text), new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            mListener.onOkClicked();
//                            // if this button is clicked, close
//                            // current activity
//                        }
//                    })
//                    .setNeutralButton(activity.getResources().getString(R.string.cancel_text), new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            mListener.onCancelClicked();
//                            // if this button is clicked, close
//                            // current activity
//                        }
//                    });
//
//            // create alert dialog
//            final AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                @Override
//                public void onShow(DialogInterface arg0) {
//                    try {
//                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(DSQApplication.getContext(), R.color.colorPrimaryDark));
//                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(DSQApplication.getContext(), R.color.colorPrimaryDark));
//                    } catch (Exception e) {
//                    }
//                }
//            });
//            // show it
//            alertDialog.show();
//        } catch (Exception e) {
//        }
//    }

//    public static void showDialog(Activity activity, String title, String errorMessage, String positiveText, String negativeText, final DialogOnClickListener mListener) {
//        try {
//            if (StringUtils.isNullOrEmpty(errorMessage)) {
//                errorMessage = DSQApplication.getContext().getString(R.string.generalFailureError);
//            }
//
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
//            // set title
//            alertDialogBuilder.setTitle(title);
//            // set dialog message
//            alertDialogBuilder
//                    .setMessage(errorMessage)
//                    .setCancelable(false)
//                    .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            mListener.onOkClicked();
//                            // if this button is clicked, close
//                            // current activity
//                        }
//                    })
//                    .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            mListener.onCancelClicked();
//                            // if this button is clicked, close
//                            // current activity
//                        }
//                    });
//
//            // create alert dialog
//            final AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                @Override
//                public void onShow(DialogInterface arg0) {
//                    try {
//                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(DSQApplication.getContext(), R.color.colorPrimaryDark));
//                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(DSQApplication.getContext(), R.color.colorPrimaryDark));
//                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
//                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
//
//                    } catch (Exception e) {
//                    }
//                }
//            });
//            // show it
//            alertDialog.show();
//        } catch (Exception e) {
//        }
//    }

    public static boolean isGreekLocale() {
        Locale currentLocale = Locale.getDefault();
        return StringUtils.areEqual("el", currentLocale.getLanguage());
    }

//    public static void showNotAvailableDialog(Activity activity) {
//        try {
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
//            // set title
//            alertDialogBuilder.setTitle(activity.getString(R.string.app_name));
//            // set dialog message
//            alertDialogBuilder
//                    .setMessage(activity.getString(R.string.demo_not_available_msg))
//                    .setCancelable(false)
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            // if this button is clicked, close
//                        }
//                    });
//
//            // create alert dialog
//            final AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                @Override
//                public void onShow(DialogInterface arg0) {
//                    try {
//                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(DSQApplication.getContext(), R.color.colorPrimary));
//                    } catch (Exception e) {
//                    }
//                }
//            });
//            // show it
//            alertDialog.show();
//        } catch (Exception e) {
//        }
//    }

    public static String getDeviceImageDensityString() {
        String res;
        int density = StrategoApplication.getContext().getResources().getDisplayMetrics().densityDpi;

        if (density <= DisplayMetrics.DENSITY_MEDIUM) {
            res = "@x1.png";
        } else if (density <= DisplayMetrics.DENSITY_XHIGH) {
            res = "@x2.png";
        } else {
            res = "@x3.png";
        }
        return res;
    }

    public static String getDistanceWithText(double distanceFromUser, final String metersText, final String kmText) {
        // In meters.
        distanceFromUser = 1000 * distanceFromUser;

        // If in meters
        if (distanceFromUser < 1000) {
            // Less the 1km. Example 933m -> 940m
            return roundToMeters((int) distanceFromUser) + " " + metersText;
        }
        if (distanceFromUser >= 1000 && distanceFromUser <= 10000) {
            // More than 1km and less than 10km.
            if (distanceFromUser % 1000 < 100) {
                // If close to a km, example: 1050m -> 1 km
                return (distanceFromUser / 1000) + " " + kmText;
            } else {
                // Else, example: 1150m -> 1.1 km
                DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(new Locale("el", "gr"));
                otherSymbols.setDecimalSeparator('.');
                otherSymbols.setGroupingSeparator(',');
                DecimalFormat df = new DecimalFormat("#.#", otherSymbols);
                return df.format(distanceFromUser / 1000f) + " " + kmText;
            }
        }
        if (distanceFromUser > 10000) {
            // More than 10km, example 16120m -> 16km
            return ((int) distanceFromUser / 1000) + " " + kmText;
        }
        return "";
    }

    private static int roundToMeters(final int meters) {
        if (meters % 10 == 0) {
            return meters;
        }
        return meters + (10 - (meters % 10));
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

//    public static List<String> spitAddressToLines(final String address) {
//        final List<String> list = new LinkedList<>();
//        final StringTokenizer stoken = new StringTokenizer(address, ",", false);
//        while (stoken.hasMoreTokens()) {
//            list.add(stoken.nextToken().trim());
//        }
//        return list;
//    }

//    /**
//     * @param userLocation
//     * @return a double array with boundaring Lat and Lng as
//     * boundaries[0] = fromLatitude;
//     * boundaries[1] = toLatitude;
//     * boundaries[2] = fromLongitude;
//     * boundaries[3] = fromLongitude;
//     */
//    public static double[] getLocationBoundaries(UserLocation userLocation) {
//
//        double[] boundaries = new double[4];
//        double tmpLat = userLocation.getLatitude();
//        double tmpLong = userLocation.getLongitude();
//
//        double radius = userLocation.getRadius() * 0.00001;
//
//        boundaries[0] = tmpLat - radius;
//        boundaries[1] = tmpLat + radius;
//        boundaries[2] = tmpLong - radius;
//        boundaries[3] = tmpLong + radius;
//
//        Timber.d("Center " + userLocation.getLatitude() + " : " + userLocation.getLongitude() + " <-> , Radius " + radius + ". LatFrom: " + boundaries[0] + ", LatTo: " + boundaries[1] + " LongFrom: " + boundaries[2] + ", LongTo: " + boundaries[3]);
//        return boundaries;
//    }

    public static String sanitisePhoneNumber(final String phoneNumberToSanitise) {
        String phoneNumber = "";
        try {
            if (StringUtils.isNullOrEmpty(phoneNumberToSanitise)) {
                return "";
            }

            // Allow only digits
            phoneNumber = phoneNumberToSanitise.replaceAll("[^0-9]", "");

            if(phoneNumber.length() > 10) {
                // Remove leading +
                if (phoneNumber.startsWith("+")) {
                    phoneNumber = phoneNumber.substring(phoneNumber.indexOf("+") + 1);
                }

                // Remove leading zeros
                if (phoneNumber.startsWith("00")) {
                    phoneNumber = phoneNumber.substring(phoneNumber.indexOf("00") + 2);
                }

                // Remove leading zero
                if (phoneNumber.startsWith("0")) {
                    phoneNumber = phoneNumber.substring(phoneNumber.indexOf("0") + 1);
                }

                // Remove leading 30
                if (phoneNumber.startsWith("30")) {
                    phoneNumber = phoneNumber.substring(phoneNumber.indexOf("30") + 2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }

}
