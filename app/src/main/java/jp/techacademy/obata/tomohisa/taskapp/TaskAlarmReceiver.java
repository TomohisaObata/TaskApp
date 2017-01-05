package jp.techacademy.obata.tomohisa.taskapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by tomohisa on 2017/01/03.
 */

public class TaskAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.small_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.large_icon));
        builder.setWhen(System.currentTimeMillis());
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);

        // タスクの情報を設定する
        Task task = (Task) intent.getSerializableExtra(MainActivity.EXTRA_TASK);
        builder.setTicker(task.getTitle()); // 5.0以降は表示されない
        builder.setContentTitle(task.getTitle());
        builder.setContentText(task.getContents());

        // 通知をタップしたらアプリを起動するようにする
        Intent startAppIntent = new Intent(context, MainActivity.class);
        startAppIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startAppIntent, 0);
        builder.setContentIntent(pendingIntent);

        // 通知を表示する
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(task.getId(), builder.build());
    }
}
