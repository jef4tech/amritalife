package com.kraftlabs.crm_new.Helper;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * User: ashik
 * Date: 12/10/17
 * Time: 12:31 PM
 */

public class DateHelper {

    private static final String TAG = "DateDiff";

    public DateHelper() {
    }

    public static int[] dateDiff(String expiryDate) {
        int args[] = new int[2];

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date expDate = null;
        //String expiryDate = "2017-09-20";
        int dateDiff = 0;
        try

        {
            expDate = formatter.parse(expiryDate);
            //logger.info("Expiry Date is " + expDate);
            // logger.info(formatter.format(expDate));
            Calendar cal = Calendar.getInstance();
            int today = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            Log.i(TAG, "dateDiff: " + month);
            cal.setTime(expDate);
            dateDiff = cal.get(Calendar.DAY_OF_MONTH) - today;

            args[0] = dateDiff;
            args[1] = month;

        } catch (
                ParseException e)

        {
            e.printStackTrace();
        }
        return args;
    }

}
