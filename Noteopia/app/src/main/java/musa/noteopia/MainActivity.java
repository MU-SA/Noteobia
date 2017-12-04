package musa.noteopia;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Vibrator vibrator;
    Toolbar addNoteToolBar;
    PopupWindow popup;
    Button add;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    ArrayList<Note> notes;
    EditText addNote;
    LinearLayout popLayout;
    Button addInsidePop;
    EditText insertInsidePop;
    TextView margin;
    TextView addTemp;
    CheckBox schedules, tasks, draw;
    DBHandeler dbHandeler;
    long date;
    RecyclerView.ViewHolder viewHolder;
    Intent give;
    Intent service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        popup = new PopupWindow(this);
        addNoteToolBar = findViewById(R.id.add_toolbar);
        addNoteToolBar.setOnClickListener(this);
        recyclerView = findViewById(R.id.recycler1);
        recyclerView.setOnClickListener(this);
        schedules = findViewById(R.id.schedulesCB);
        tasks = findViewById(R.id.tasksCH);

        service = new Intent(MainActivity.this, NoteHead.class);
        stopService(service);
        tasks.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupWindow popupWindow = new PopupWindow();
                TextView textView = new TextView(MainActivity.this);
                LinearLayout linearLayout = new LinearLayout(MainActivity.this);
                textView.setText("Tasks");
                textView.setHeight(60);
                textView.setWidth(200);
                textView.setPadding(2, 2, 2, 2);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                textView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                linearLayout.addView(textView);
                popupWindow.setHeight(60);
                popupWindow.setWidth(200);
                popupWindow.setContentView(linearLayout);
                vibrator.vibrate(100);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAsDropDown(tasks);
                return true;

            }
        });
        draw = findViewById(R.id.drawCB);
        draw.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupWindow popupWindow = new PopupWindow();
                TextView textView = new TextView(MainActivity.this);
                LinearLayout linearLayout = new LinearLayout(MainActivity.this);
                textView.setText("Draw");
                textView.setHeight(60);
                textView.setWidth(200);
                textView.setPadding(2, 2, 2, 2);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                textView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                linearLayout.addView(textView);
                popupWindow.setHeight(60);
                popupWindow.setWidth(200);
                popupWindow.setContentView(linearLayout);
                vibrator.vibrate(100);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAsDropDown(draw);
                return true;
            }
        });
        schedules.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupWindow popupWindow = new PopupWindow();
                TextView textView = new TextView(MainActivity.this);
                LinearLayout linearLayout = new LinearLayout(MainActivity.this);
                textView.setText("Schedules");
                textView.setHeight(60);
                textView.setWidth(250);
                textView.setPadding(2, 2, 2, 2);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                textView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                linearLayout.addView(textView);
                popupWindow.setHeight(60);
                popupWindow.setWidth(250);
                popupWindow.setContentView(linearLayout);
                vibrator.vibrate(100);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAsDropDown(schedules);
                return true;
            }
        });

        notes = new ArrayList<>();
        noteAdapter = new NoteAdapter(notes, this);
        addNotes();
        dbHandeler = new DBHandeler(this);
        addTemp = findViewById(R.id.addTemp);
        addTemp.setOnClickListener(this);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(noteAdapter);
        noteAdapter.notifyDataSetChanged();
        chechIf();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        viewAll2();
    }

    void chechIf() {
        if (dbHandeler.getSize() > 0) {
            addTemp.setVisibility(View.GONE);
        } else {
            addTemp.setVisibility(View.VISIBLE);
        }
    }

    public void addNotes() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void viewAll2() {

        Cursor res = dbHandeler.getAllData();
        if (res.getCount() == 0) {
            return;
        }

        while (res.moveToNext()) {
            prepareTaskData(res.getString(1));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.openNoteHead) {

            startService(service);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayPopupWindow(View anchorView) {

        popLayout = new LinearLayout(this);
        insertInsidePop = new EditText(this);
        addInsidePop = new Button(this);
        insertInsidePop.setHint("Aa");
        addInsidePop.setText("Done");
        addInsidePop.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        insertInsidePop.setTextColor(Integer.parseInt(String.valueOf(R.color.colorPrimary)));
        insertInsidePop.setHeight((int) (getWindowManager().getDefaultDisplay().getHeight() * .72));
        insertInsidePop.setWidth((int) (getWindowManager().getDefaultDisplay().getWidth()));
        insertInsidePop.setGravity(Gravity.TOP);
        addInsidePop.setTextColor(getResources().getColor(R.color.colorAccent));
        addInsidePop.setGravity(Gravity.CENTER);
        addInsidePop.setWidth((int) (getWindowManager().getDefaultDisplay().getWidth()));
        addInsidePop.setPadding(0, 0, 0, 8);
        margin = new TextView(this);
        margin.setWidth((int) (getWindowManager().getDefaultDisplay().getWidth()));
        margin.setHeight(8);
        margin.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        popLayout.addView(insertInsidePop);
        popLayout.addView(addInsidePop);
        popLayout.addView(margin);
        popLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        popLayout.setOrientation(LinearLayout.VERTICAL);
        popup.setContentView(popLayout);
        popup.setHeight((int) (getWindowManager().getDefaultDisplay().getHeight() * .8));
        popup.setWidth((int) (getWindowManager().getDefaultDisplay().getWidth()));
        popup.setOutsideTouchable(false);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAsDropDown(anchorView);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                addNoteToolBar.setEnabled(true);
                if (!insertInsidePop.getText().toString().equals("")) {
                    dbHandeler.insertObject(insertInsidePop.getText().toString());
                    prepareTaskData(insertInsidePop.getText().toString());
                    chechIf();
                    noteAdapter.notifyDataSetChanged();
                }
            }
        });

        addInsidePop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();

            }
        });
    }

    public void prepareTaskData(String name) {
        Note item = new Note(name);
        notes.add(item);
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {

        if (popup.isShowing()) {
            popup.dismiss();
            addNoteToolBar.setEnabled(true);
        } else
            super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if (view.equals(addTemp)) {
            displayPopupWindow(addNoteToolBar);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {

                notes.remove(data.getExtras().getInt("position"));
                dbHandeler.deleteData(data.getExtras().getInt("position"));
                noteAdapter.notifyItemRemoved(data.getExtras().getInt("position"));

                prepareTaskData(data.getExtras().getString("editDone"));
                dbHandeler.insertObject(data.getExtras().getString("editDone"));
                noteAdapter.notifyDataSetChanged();
                chechIf();
            }
        }

    }

    public void addNew(View view) {
        if (popup.isShowing()) {
            addNoteToolBar.setEnabled(false);
        } else {
            displayPopupWindow(addNoteToolBar);
        }
    }


    ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                @Override
                public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                }

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;

                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    final int deleted = viewHolder.getPosition();
                    final int VHGA = viewHolder.getAdapterPosition();
                    notes.remove(deleted);
                    dbHandeler.deleteData(viewHolder.getPosition());
                    noteAdapter.notifyItemRemoved(VHGA);
                    chechIf();
                }

                @Override
                public void onMoved
                        (RecyclerView recyclerView,
                         RecyclerView.ViewHolder viewHolder,
                         int fromPos,
                         RecyclerView.ViewHolder target,
                         int toPos,
                         int x,
                         int y) {

                    super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);

                }

            };

}
