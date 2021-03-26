package com.Class;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class AlarmReboot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("Phone Reboot: ", "AlarmRebootCalled.");

        SharedPreferences jammat_reminder = context.getSharedPreferences("jammat_reminder", Context.MODE_PRIVATE);

        String[] reminderTimes = new String[5];
        reminderTimes[0] = jammat_reminder.getString("fajr_time", "NA");
        reminderTimes[1] = jammat_reminder.getString("duhr_time", "NA");
        reminderTimes[2] = jammat_reminder.getString("asar_time", "NA");
        reminderTimes[3] = jammat_reminder.getString("magrib_time", "NA");
        reminderTimes[4] = jammat_reminder.getString("isha_time", "NA");

        int j = 0;

        for (int i = 525; i <= 529; i++) {

            String jammatReminderTime = reminderTimes[j];

            if (!jammatReminderTime.equalsIgnoreCase("NA")) {

                String[] time = jammatReminderTime.split(":");

                Calendar calendar = Calendar.getInstance();

                Log.e("Reminder Time = ", i + " - " + time.toString());

                if (time[0].startsWith("0")) {
                    String hoursFinal = time[0].substring(1);
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hoursFinal));
                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
                }

                if (time[1].startsWith("0")) {
                    String minutesFinal = time[1].substring(1);
                    calendar.set(Calendar.MINUTE, Integer.parseInt(minutesFinal));
                } else {
                    calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
                }

                //calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
                //calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
                calendar.set(Calendar.SECOND, 0);
                calendar.add(Calendar.MINUTE, -30);

                PendingIntent pendingIntent;
                Intent myIntent = new Intent(context, MyReceiver.class);
                myIntent.putExtra("message", "Jamaat reminder : Next prayer will be at " + time[0] + ":" + time[1]);
                pendingIntent = PendingIntent.getBroadcast(context, i, myIntent, 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }

            j++;
        }
    }
}
