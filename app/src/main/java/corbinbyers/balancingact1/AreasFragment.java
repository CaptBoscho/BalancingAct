package corbinbyers.balancingact1;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 */
public class AreasFragment  extends Fragment implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private static AreasFragment instance = null;
    public AreasFragment() {
        // Required empty public constructor
    }

    public static AreasFragment getInstance(){
        if(instance==null){
            instance = new AreasFragment();
        }
        return instance;
    }

    Button healthBut;
    Button financeBut;
    Button studyBut;
    Button socialBut;
    Button religionBut;
    Button otherBut;
    LinearLayout linLay;
    List<Button> allAreas;
    List<Integer> allSelected;
    BalanceDb balanceDB;
    SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_areas, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        linLay = (LinearLayout)getView().findViewById(R.id.linearButtons);
        /*healthBut = (Button)getView().findViewById(R.id.buttonhealth);
        financeBut = (Button)getView().findViewById(R.id.buttonfinance);
        studyBut = (Button)getView().findViewById(R.id.buttonstudy);
        socialBut = (Button)getView().findViewById(R.id.buttonsocial);
        religionBut = (Button)getView().findViewById(R.id.buttonreligion);*/
        otherBut = (Button)getView().findViewById(R.id.buttonother);
        allAreas = new ArrayList<>();
        allSelected = new ArrayList<>();
        //healthBut.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        createDBTable();

        for(int i=0; i<allAreas.size(); i++){
            final int index = i;
            allAreas.get(i).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v){
                    System.out.println("index: " + index);
                    if(allSelected.get(index) == 1){
                        v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                        setSelected(index, 0);
                    } else{
                        v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        setSelected(index, 1);
                    }
                }
            });
        }


        otherBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("New Life Area");

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        String newArea = input.getText().toString();
                        addArea(newArea);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    public void addArea(String nA){
        System.out.println(nA);
        allSelected.add(1);
        //Add to database
        allAreas.add(createButton("+ " + nA, allSelected.size()-1, 1, true));
    }

    public Button createButton(String title, final int index, int selected, boolean firstCreate){
        Button newBut = new Button(getContext());
        newBut.setText(title);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1050,200);
        params.topMargin = 35;
        params.gravity = Gravity.CENTER;
        newBut.setLayoutParams(params);
        newBut.setTextSize(18);
        newBut.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        newBut.setPadding(90,0,0,0);
        if(selected == 1) {
            newBut.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            newBut.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
        }

        if(firstCreate){
            addButToDb(newBut,selected);
        }
        newBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if(allSelected.get(index) == 1){
                    v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                    setSelected(index, 0);
                } else{
                    v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    setSelected(index, 1);
                }
            }
        });

        linLay.addView(newBut);
        return newBut;
    }

    private void setSelected(int index, int selected){
        allSelected.set(index, selected);
        balanceDB = BalanceDb.getInstance(getContext());
        db = balanceDB.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BalanceDb.LifeEntry.selectedColumn, selected);

        String selection = BalanceDb.LifeEntry.nameColumn + " LIKE ?";
        String[] selectionArgs = {allAreas.get(index).getText().toString()};

        int count = db.update(BalanceDb.LifeEntry.Table_Name, values,
                selection, selectionArgs);

        System.out.println("Set Selected:");
        printDB();
    }

    private void printDB(){
        balanceDB = BalanceDb.getInstance(getContext());
        SQLiteDatabase reader = balanceDB.getReadableDatabase();
        String[] projection = {"*"};
        Cursor cursor = reader.query(BalanceDb.LifeEntry.Table_Name, projection, null, null,
                null, null, null);
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(BalanceDb.LifeEntry.nameColumn));
            int selected = cursor.getInt(
                    cursor.getColumnIndex(BalanceDb.LifeEntry.selectedColumn));
            int id = cursor.getInt(
                    cursor.getColumnIndex(BalanceDb.LifeEntry.stringID));
            System.out.println(name + " sel: " + selected + " id: " + id);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("****** lifeareas size: " + allAreas.size());
        if(balanceDB == null){
            balanceDB = BalanceDb.getInstance(getContext());
        }
        SQLiteDatabase reader = balanceDB.getReadableDatabase();
        String[] projection = {BalanceDb.LifeEntry.selectedColumn};
        String where = BalanceDb.LifeEntry.nameColumn + " = ?";
        //Sort order?
        /*
        for(int i=0; i<allAreas.size(); i++){
            String[] whereArgs = {allAreas.get(i).getText().toString()};
            Cursor cursor = db.query(BalanceDb.LifeEntry.Table_Name,
                    projection,where,whereArgs,null,null,null);

            while(cursor.moveToNext()){
                int selected = cursor.getInt(
                        cursor.getColumnIndex(BalanceDb.LifeEntry.selectedColumn));
                System.out.println(allAreas.get(i).getText() + " sel.: " + selected);
                if(selected == 1){
                    allAreas.get(i).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    allSelected.set(i, 1);
                } else {
                    allSelected.set(i, 0);
                }
            }
        }*/
        reader = balanceDB.getReadableDatabase();
        String[] proj = {"*"};
        Cursor cursor = reader.query(BalanceDb.LifeEntry.Table_Name, proj, null, null,
                null, null, null);
        int index = 0;
        while(cursor.moveToNext()){
            System.out.println("TESTING AAA");
            String name = cursor.getString(cursor.getColumnIndex(BalanceDb.LifeEntry.nameColumn));
            int selected = cursor.getInt(
                    cursor.getColumnIndex(BalanceDb.LifeEntry.selectedColumn));
            int id = cursor.getInt(
                    cursor.getColumnIndex(BalanceDb.LifeEntry.stringID));
            allAreas.add(createButton(name,index,selected,false));
            allSelected.add(selected);
            index++;
        }

        System.out.println("On Resume: ");
        printDB();
    }

    private void createDBTable(){
        balanceDB = BalanceDb.getInstance(getContext());
        db = balanceDB.getWritableDatabase();
        //balanceDB.deleteLifeAreasTable(db);
        //balanceDB.createLifeAreaTable(db);
        if(allAreas.size() == 0){return;}

        //Check if it already has info
        SQLiteDatabase reader = balanceDB.getReadableDatabase();
        String[] projection = {BalanceDb.LifeEntry.selectedColumn};
        String where = BalanceDb.LifeEntry.nameColumn + " = ?";
        String[] whereArgs = {allAreas.get(0).getText().toString()};
        Cursor cursor = db.query(BalanceDb.LifeEntry.Table_Name,
               projection,where,whereArgs,null,null,null);

        System.out.println(cursor.getCount());
        if(cursor.getCount() == 0) {
            System.out.println("adding");
            for (int i = 0; i < allAreas.size(); i++) {
                addButToDb(allAreas.get(i), 0);
            }
        }

    }

    public void addButToDb(Button but, int selected){
        balanceDB = BalanceDb.getInstance(getContext());
        db = balanceDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BalanceDb.LifeEntry.nameColumn, but.getText().toString());
        values.put(BalanceDb.LifeEntry.selectedColumn, selected);
        values.put(BalanceDb.LifeEntry.colorColumn, R.color.colorPrimaryLight);
        values.put(BalanceDb.LifeEntry.stringID, but.getId());
        long RowID = db.insert(BalanceDb.LifeEntry.Table_Name, null, values);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }
}
