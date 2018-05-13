package io.berrycorp.noti.adapters;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.berrycorp.noti.AddReminderActivity;
import io.berrycorp.noti.R;
import io.berrycorp.noti.models.Task;
import io.berrycorp.noti.receivers.AlarmReceiver;

import static io.berrycorp.noti.models.Task.REMINDER_IDLE;
import static io.berrycorp.noti.models.Task.REMINDER_REMINDING;

public class TaskAdapter extends ArrayAdapter<Task> {
    private Context mContext;
    private int mResource;
    private ArrayList<Task> mTasks;

    public TaskAdapter(Context context, int resource, ArrayList<Task> tasks) {
        super(context, resource, tasks);
        this.mContext = context;
        this.mResource = resource;
        this.mTasks = tasks;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        final Task task = mTasks.get(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
            viewHolder.tvName = convertView.findViewById(R.id.tv_name);
            viewHolder.tvInfo = convertView.findViewById(R.id.tv_info);
            viewHolder.btnRemove = convertView.findViewById(R.id.btn_remove);
            viewHolder.layoutContent = convertView.findViewById(R.id.layout_content);
            convertView.setTag(viewHolder);
        } else  {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(task.getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm - dd/MM/yyyy");
        StringBuilder stringBuilder = new StringBuilder();
        if (task.getState() == REMINDER_IDLE) {
            stringBuilder.append(dateFormat.format(task.getStartPoint())).append(" - ").append("Chưa nhắc").append(task.getId());
        } else if (task.getState() == REMINDER_REMINDING) {
            stringBuilder.append(dateFormat.format(task.getStartPoint())).append(" - ").append("Đang nhắc").append(task.getId());
        } else {
            stringBuilder.append(dateFormat.format(task.getStartPoint())).append(" - ").append("Hết hạn").append(task.getId());
        }
        viewHolder.tvInfo.setText(stringBuilder.toString());

        viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (task.delete(mContext)) {
                    AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(mContext, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    pendingIntent.cancel();
                    alarmManager.cancel(pendingIntent);
                    mTasks.remove(task);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, "Xóa thành công" + task.getId(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(mContext, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }

            }
        });

        viewHolder.layoutContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddReminderActivity.class);
                intent.putExtra("keyAction", "update");
                intent.putExtra("keyTask", task);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvInfo;
        ImageButton btnRemove;
        LinearLayout layoutContent;
        ViewHolder() {

        }
    }
}
