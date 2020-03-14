package gr.stratego.patrastournament.me.Utils;

import android.app.Activity;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static boolean isNullOrEmpty(String value) {
        if (value != null && !value.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isNotNullOrEmpty(String value) {
        return !isNullOrEmpty(value);
    }

    public static boolean areEqual(String value1, String value2) {
        if (value1 == null || value2 == null) {
            return false;
        } else {
            return value1.equals(value2);
        }
    }

    public static boolean contains(String value1, String value2) {
        if (value1 == null || value2 == null) {
            return false;
        } else {
            return value1.contains(value2);
        }
    }

    public static String getMaskedPanLast4Digits(String maskedPan) {
        String res = maskedPan;
        if (StringUtils.isNotNullOrEmpty(res) && res.length() >= 4) {
            res = res.substring(res.length() - 4, res.length());
        } else {
            res = "";
        }
        return res;
    }

    /**
     * Accepts a 10 digit phone and returns it as 123 456 78 90
     *
     * @param phoneNumber
     * @return
     */
    public static String pretifyPhoneNumber(String phoneNumber) {
        StringBuilder prettyPhoneNumber = new StringBuilder();
        if (phoneNumber.length() == 10) {
            prettyPhoneNumber.append(phoneNumber.substring(0, 3));
            prettyPhoneNumber.append(" ");
            prettyPhoneNumber.append(phoneNumber.substring(3, 6));
            prettyPhoneNumber.append(" ");
            prettyPhoneNumber.append(phoneNumber.substring(6, 8));
            prettyPhoneNumber.append(" ");
            prettyPhoneNumber.append(phoneNumber.substring(8, 10));
            return prettyPhoneNumber.toString();
        } else {
            return phoneNumber;
        }

    }

    //Apply Rule for greek names
    public static String getResFromInput(String rawText) {
        String res = "";
        if (StringUtils.isNotNullOrEmpty(rawText)) {
            rawText = rawText.toLowerCase();
            rawText = rawText.substring(0, 1).toUpperCase() + rawText.substring(1);
            int sylabusCount = 0;
            int tonosPoint = -1;
            if (rawText.length() > 2) {
                if (isSygma(rawText.charAt(rawText.length() - 1))) {
                    if (isRuleEligible(rawText.charAt(rawText.length() - 2))) {
                        for (int i = 0; i < rawText.length(); i++) {
                            if (isFonien(rawText.charAt(i))) {
                                sylabusCount++;
                            }
                            if (isFonienWithIntonation(rawText.charAt(i))) {
                                tonosPoint = sylabusCount;
                            }
                        }
                    } else {
                        //το ας γίνεται α ή το ης γίνεται η
                        res = rawText.substring(0, rawText.length() - 1);
                    }
                } else {
                    res = rawText;
                }
            }
            if (tonosPoint > 0) {
                if (tonosPoint == sylabusCount || (sylabusCount - tonosPoint > 1) || isRuleExclution(rawText.charAt(rawText.length() - 3), sylabusCount)) {
                    //το ος γίνεται ε
                    res = rawText.substring(0, rawText.length() - 2) + "ε";
                } else {
                    //το ος γίνεται ο
                    res = rawText.substring(0, rawText.length() - 2) + "ο";
                }
            }
            if (StringUtils.isNotNullOrEmpty(res)) {
                return res;
            } else {
                //Not supported for now...
                return rawText;
            }
        }
        return "";
    }

    private static boolean isFonien(char value) {
        if (value == 'ο' || value == 'ω' || value == 'ι' || value == 'η' || value == 'υ' || value == 'ε' || value == 'α' || value == 'ό' || value == 'ώ' || value == 'ί' || value == 'ή' || value == 'ύ' || value == 'έ' || value == 'ά') {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isFonienWithIntonation(char value) {
        if (value == 'ό' || value == 'ώ' || value == 'ί' || value == 'ή' || value == 'ύ' || value == 'έ' || value == 'ά') {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isSygma(char value) {
        if (value == 'ς' || value == 'σ') {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isRuleEligible(char value) {
        if (value == 'ο' || value == 'ό') {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isRuleExclution(char value, int sylabusCount) {
        if (sylabusCount > 2 && (value == 'ν' || value == 'ρ')) {
            return true;
        } else {
            return false;
        }
    }

    public static String replaceNonNumbersWithChar(String rawString, String replaceChar) {
        if (isNullOrEmpty(rawString)) {
            return rawString;
        }
        rawString = rawString.replaceAll("[^\\d.]", replaceChar);
        return rawString;
    }

    public static Spanned getHtmlText(Activity activity, int textId) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = (Html.fromHtml(activity.getString(textId), Html.FROM_HTML_MODE_LEGACY));
        } else {
            result = (Html.fromHtml(activity.getString(textId)));
        }
        return result;
    }

    public static Spanned getHtmlText(String text) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = (Html.fromHtml(text, Html.FROM_HTML_SEPARATOR_LINE_BREAK_DIV));
        } else {
            result = (Html.fromHtml(text));
        }
        return result;
    }

    public static String convertDoubleToString(Double value) {
        if (value == null) {
            return null;
        }
        try {
            return String.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static Map<String, String> extractParamsFromURL(final String url) {
        try {
            HashMap<String, String> params = new HashMap<String, String>() {{
                Uri uri = Uri.parse(url);
                for (String paramName : uri.getQueryParameterNames()) {
                    if (paramName != null) {
                        String paramValue = uri.getQueryParameter(paramName);
                        if (paramValue != null) {
                            put(paramName, paramValue);
                        }
                    }
                }
            }};
            return params;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean hasSpecialChars(String givenString) {
        String expression = "[^A-Za-z0-9Α-Ωα-ω ]";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(givenString);
        boolean hasSpecialChars = matcher.find();
        return hasSpecialChars;
    }

    /**
     * method is used for checking valid ic_mail id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String getMaskedNumber(int unmaskedFirstDigitsCount, int unmaskedLastDigitsCount, String inputText) {
        if (StringUtils.isNullOrEmpty(inputText)) {
            return "";
        }
        StringBuilder res = new StringBuilder("");
        for (int i = 0; i < inputText.length(); i++) {
            if (i < unmaskedFirstDigitsCount || i >= inputText.length() - unmaskedLastDigitsCount) {
                res.append(inputText.charAt(i));
            } else {
                res.append("\u2022");
            }
        }

        return res.toString();
    }

    public static String getMaskedNumber(String inputText) {
        return getMaskedNumber(3, 3, inputText);
    }


    public static String removeSpaces(final String value) {
        if (StringUtils.isNullOrEmpty(value)) {
            return null;
        }
        return value.replaceAll("\\s+", "");
    }

    public static String formatIntegerToThousands(int value) {
        return NumberFormat.getNumberInstance(Locale.ITALY).format(value);//We must have consistency with the input texts from Tribal.
    }

    public static String getFormatedDistance(float value) {
        String greekMetres = " μ.";
        String greekKiloMetres = " χλμ.";
        String englishMetres = " m.";
        String englishKiloMetres = " km.";

        int distance = Math.round(value);
        if (distance < 1080) {
            if (GeneralUtils.isGreekLocale()) {
                return String.valueOf(distance).concat(greekMetres);
            } else {
                return String.valueOf(distance).concat(englishMetres);
            }
        } else {
            float kmDistance = (float) distance / 1000;
            if (GeneralUtils.isGreekLocale()) {
                return String.format(Locale.getDefault(), "%.1f", kmDistance).concat(greekKiloMetres);
            } else {
                return String.format(Locale.getDefault(), "%.1f", kmDistance).concat(englishKiloMetres);
            }
        }
    }

    public static String encodeString(byte[] data) {
        if (data == null) {
            return "";
        }
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }

    public static String removeControlCharacters(String inString) {
        return inString.replaceAll("\\r|\\n", "");
    }
}
