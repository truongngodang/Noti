package io.berrycorp.noti.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.berrycorp.noti.AddNoteActivity;
import io.berrycorp.noti.AddReminderActivity;
import io.berrycorp.noti.R;
import io.berrycorp.noti.controllers.TaskController;
import io.berrycorp.noti.models.Note;
import io.berrycorp.noti.models.Task;

import static io.berrycorp.noti.models.Task.REMINDER_IDLE;
import static io.berrycorp.noti.models.Task.REMINDER_REMINDING;

public class NoteAdapter extends ArrayAdapter<Note> {
    private Context mContext;
    private int mResource;
    private ArrayList<Note> mNotes;

    public NoteAdapter(Context context, int resource, ArrayList<Note> notes) {
        super(context, resource, notes);
        this.mContext = context;
        this.mResource = resource;
        this.mNotes = notes;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        final Note note = mNotes.get(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
            viewHolder.tvName = convertView.findViewById(R.id.tv_name);
            viewHolder.btnRemove = convertView.findViewById(R.id.btn_remove);
            viewHolder.layoutContent = convertView.findViewById(R.id.layout_content);
            convertView.setTag(viewHolder);
        } else  {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(note.getName());

        viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (note.delete(mContext)) {
                    mNotes.remove(note);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, "Xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewHolder.layoutContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddNoteActivity.class);
                intent.putExtra("keyAction", "update");
                intent.putExtra("keyNote", note);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView tvName;
        ImageButton btnRemove;
        LinearLayout layoutContent;
        ViewHolder() {

        }
    }
}
