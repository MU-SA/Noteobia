package musa.noteopia;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class NoteHead extends Service {
    WindowManager windowManager;
    private View noteHead;
    View popLayout;
    Button addInsidePop;
    EditText insertInsidePop;
    PopupWindow popup;
    ImageView close;
    WindowManager.LayoutParams params;
    DBHandeler dbHandeler;

    public NoteHead() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onCreate() {
        super.onCreate();
        noteHead = LayoutInflater.from(this).inflate(R.layout.note_head, null);
        close = noteHead.findViewById(R.id.closeHead);
        popup = new PopupWindow();
        popup.dismiss();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        });
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;


        params.x = 0;
        params.y = 100;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(noteHead, params);

        noteHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displayPopupWindow(noteHead);
            }


        });

        noteHead.setOnTouchListener(new View.OnTouchListener() {
            private WindowManager.LayoutParams paramsF = params;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = paramsF.x;
                        initialY = paramsF.y;

                        initialTouchX = motionEvent.getRawX();
                        initialTouchY = motionEvent.getRawY();
                        break;

                    case MotionEvent.ACTION_UP:

                        break;

                    case MotionEvent.ACTION_MOVE:
                        paramsF.x = initialX + (int) (motionEvent.getRawX() - initialTouchX);
                        paramsF.y = initialY + (int) (motionEvent.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(noteHead, paramsF);
                        break;
                }

                return false;
            }
        });
        dbHandeler = new DBHandeler(this);
    }


    private void displayPopupWindow(View anchorView) {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        params.x = display.getWidth();
        params.y = 6;
        popLayout = LayoutInflater.from(this).inflate(R.layout.add_note_pm, null);

        insertInsidePop = popLayout.findViewById(R.id.addnote);
        popup.setContentView(popLayout);
        popup.setHeight((int) (display.getHeight() / 1.5));
        popup.setWidth(display.getWidth());
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.showAsDropDown(anchorView);
        windowManager.updateViewLayout(noteHead, params);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (noteHead != null) {
            windowManager.removeView(noteHead);
        }
    }


    public void addNewFromOut(View view) {
        if (!(insertInsidePop.getText().toString().equals("") || insertInsidePop.getText().toString().equals(null))) {
            dbHandeler.insertObject(insertInsidePop.getText().toString());
        }


        popup.dismiss();
    }

    public void stopself2(View view) {
        popup.dismiss();
    }

    public void openBack(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        stopSelf();
        startActivity(intent);
        popup.dismiss();

    }
}
