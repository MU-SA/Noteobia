package musa.noteopia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MUSA on 11/30/2017.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {
    private static Context context;
    private List<Note> noteArray;
    TextView textView;

    public NoteAdapter(ArrayList<Note> arrayList, Context context) {
        this.context = context;
        this.noteArray = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.note, parent, false);


        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note note = noteArray.get(position);
        holder.note.setText(note.getThing());
        holder.note.setTag(position);
    }


    @Override
    public int getItemCount() {
        return noteArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView note;
        Context that;

        public MyViewHolder(final View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.noteId);
            that = itemView.getContext();
            note.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.equals(note)) {
                Intent intent = new Intent(that, Edit.class);
                intent.putExtra("position", getAdapterPosition());
                intent.putExtra("data", note.getText());
                ((Activity) context).startActivityForResult(intent, 2);
            }
        }
    }
    public class Send extends Activity {
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
    }
}

