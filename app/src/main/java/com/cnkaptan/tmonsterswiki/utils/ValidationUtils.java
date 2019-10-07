package com.cnkaptan.tmonsterswiki.utils;

import android.util.Log;
import io.reactivex.Observable;

public class ValidationUtils {
    private static final String TAG = ValidationUtils.class.getSimpleName();

    /**
     * The combine function for and operation. It checks at the booleans match.
     */
    public static Observable<Boolean> and(Observable<Boolean> a, Observable<Boolean> b) {
        return Observable.combineLatest(a, b, (valueA, valueB) -> valueA && valueB);
    }

    public static Observable<Boolean> or(Observable<Boolean> a, Observable<Boolean> b) {
        return Observable.combineLatest(a, b, (valueA, valueB) -> valueA || valueB);
    }

    public static Observable<Boolean> and(Observable<Boolean> a, Observable<Boolean> b, Observable<Boolean> c) {
        return Observable.combineLatest(a, and(b,c), (valueA, valueB) -> valueA && valueB);
    }

    /**
     * The combine function for checking the object equality. In production code you should
     * probably do null checks too.
     */
    public static Observable<Boolean> equals(Observable<?> a, Observable<?> b){
        return Observable.combineLatest(a, b, (valueA, valueB) -> valueA.equals(valueB));
    }

    public static boolean checkExpirationDate(String candidate) {
        return candidate.matches("^\\d\\d/\\d\\d$");
    }

    public static boolean checkCardCheckSum(String number) {
        final int[] digits = new int[number.length()];
        for (int i = 0; i < number.length(); i++) {
            digits[i] = Integer.valueOf(number.substring(i, i + 1));
        }
        boolean checkCardChecksum = checkCardChecksum(digits);
        Log.d(TAG, "checkCardChecksum(" + number + ") = " + checkCardChecksum);
        return checkCardChecksum;
    }

    private static boolean checkCardChecksum(int[] digits) {
        int sum = 0;
        int length = digits.length;
        for (int i = 0; i < length; i++) {
            // Get digits in reverse order
            int digit = digits[length - i - 1];
            // Every 2nd number multiply with 2
            if (i % 2 == 1) {
                digit *= 2;
            }
            sum += digit > 9 ? digit - 9 : digit;
        }
        return sum % 10 == 0;
    }
}
