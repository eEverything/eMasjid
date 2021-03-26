package http.CommenRequest;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import http.constant.AppConstants;

/**
 * Created by Ki-Admin on 30/01/2017.
 */

public class ValidationClass {

    public static boolean fromvalidation(Context context, String[][] xmldata, String[] vakidation, String[] alldata) {

        String password = "";

        for (int j = 0; j < vakidation.length; j++) {


            for (int i = 0; i < xmldata.length; i++) {


                if (vakidation[j].equals(xmldata[i][1])) {
                    switch (xmldata[i][0]) {

                        case "TextView":
                            if (AppConstants.nullCheck(alldata[j])) {
                                continue;
                            } else {
                                AppConstants.showToast(context, "Please Enter " + vakidation[j].replace("_", " "));
                                return false;
                            }

                        case "EditText":
                            if (AppConstants.nullCheck(alldata[j])) {

                                if (vakidation[j].toLowerCase().contains("email")) {

                                    if (emailformatevalidation(context, alldata[j])) {
                                        continue;
                                    } else {
                                        return false;
                                    }


                                } else if (vakidation[j].toLowerCase().contains("password")) {
                                    password = alldata[j];
                                    if (passwordvalidation(context, alldata[j])) {
                                        continue;
                                    } else {
                                        return false;
                                    }

                                } else if (vakidation[j].toLowerCase().contains("confirmpssword")) {

                                    if (confirmpasswordvalidation(context, password, alldata[j])) {
                                        continue;
                                    } else {
                                        return false;
                                    }

                                }

                            } else {
                                AppConstants.showToast(context, "Please Enter " + vakidation[j].replace("_", " "));
                                return false;
                            }

                        case "ProgressBar":
                            if (AppConstants.nullCheck(alldata[j])) {
                                continue;
                            } else {
                                AppConstants.showToast(context, "Please Enter " + vakidation[j].replace("_", " "));
                                return false;
                            }


                        case "RatingBar":
                            if (AppConstants.nullCheck(alldata[j])) {
                                continue;
                            } else {
                                AppConstants.showToast(context, "Please Enter " + vakidation[j].replace("_", " "));
                                return false;
                            }

                        case "AutoCompleteTextView":
                            if (AppConstants.nullCheck(alldata[j])) {
                                continue;
                            } else {
                                AppConstants.showToast(context, "Please Enter " + vakidation[j].replace("_", " "));
                                return false;
                            }


                    }

                }


            }


        }

        return true;
    }


    public static boolean emailformatevalidation(Context context, String email) {
        if (AppConstants.isEmailValid(email)) {
            return true;
        } else {
            AppConstants.showToast(context, "Please Enter Valid Email Address");
            return false;
        }
    }


    public static boolean passwordvalidation(Context context, String password) {

        if (password.length() >= 6) {
            return true;

        } else {
            AppConstants.showToast(context, "Please Enter Password Minimum 6 character");
            return false;
        }


    }


    public static boolean selectionvalidation(Context context, String type, String value, String tag) {

        if (value.length() > 0) {
            return true;

        } else {
            AppConstants.showToast(context, "Please select " + tag.replace("_", " "));
            return false;
        }


    }


    public static boolean confirmpasswordvalidation(Context context, String password, String confirmpassword) {

        if (password.length() >= 6) {

            if (password.equals(confirmpassword)) {
                return true;
            } else {
                AppConstants.showToast(context, "Please Enter Confirm Password Same as Password");
                return false;
            }


        } else {
            AppConstants.showToast(context, "Please Enter Password Minimum 6 character");
            return false;
        }


    }


}
