package musa.noteopia;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Edit extends AppCompatActivity implements View.OnClickListener {
    Intent get;
    int position;
    DBHandeler dbHandeler;
    EditText edEditText;
    Button done;
    NoteAdapter noteAdapter;
    ArrayList<Note> arrayList;
    Intent editDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        get = getIntent();
        dbHandeler = new DBHandeler(this);
        position = get.getExtras().getInt("position");
        edEditText = findViewById(R.id.editEditText);
        edEditText.append(get.getExtras().getString("data"));
        done = findViewById(R.id.doneedit);
        done.setOnClickListener(this);
        noteAdapter = new NoteAdapter(arrayList, this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(done)) {
            if (!edEditText.getText().toString().equals(get.getExtras().getString("data"))) {
                editDone = new Intent();
                editDone.putExtra("editDone", edEditText.getText().toString());
                editDone.putExtra("position", position);
                setResult(RESULT_OK, editDone);
                finish();
            } else {
                finish();
                return;
            }
        }
    }


}
