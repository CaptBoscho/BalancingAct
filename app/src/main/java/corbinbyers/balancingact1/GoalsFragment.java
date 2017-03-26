package corbinbyers.balancingact1;


import android.app.ActionBar;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import corbinbyers.balancingact1.DaoObjects.LifeArea;


/**
 * A simple {@link Fragment} subclass.
 */
public class GoalsFragment extends Fragment {

    private static GoalsFragment instance = null;
    private LinearLayout goalLayout;
    private BalanceDb balanceDb;
    public GoalsFragment() {
        // Required empty public constructor
    }

    public static GoalsFragment getInstance(){
        if(instance == null){
            instance = new GoalsFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goals, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        goalLayout = (LinearLayout)getView().findViewById(R.id.linearGoals);
        System.out.println("Hello there");
        balanceDb = BalanceDb.getInstance(getContext());
        List<LifeArea> areas = getLifeAreas();
        drawLifeAreas(areas);
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    private List<LifeArea> getLifeAreas(){
        List<LifeArea> areas = new ArrayList<>();
        balanceDb = BalanceDb.getInstance(getContext());
        SQLiteDatabase reader = balanceDb.getReadableDatabase();
        String[] projection = {BalanceDb.LifeEntry.nameColumn, BalanceDb.LifeEntry._ID,
                BalanceDb.LifeEntry.selectedColumn};
        String where = BalanceDb.LifeEntry.selectedColumn + " = ?";
        String[] whereArgs = {"1"};
        Cursor cursor = reader.query(BalanceDb.LifeEntry.Table_Name,
                projection,where,whereArgs,null,null,null);

        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(BalanceDb.LifeEntry.nameColumn));
            int id = cursor.getInt(
                    cursor.getColumnIndex(BalanceDb.LifeEntry._ID));
            int selected = cursor.getInt(
                    cursor.getColumnIndex(BalanceDb.LifeEntry.selectedColumn));
            System.out.println(name + " sel: " + selected + " id: " + id);
            name = name.substring(2);
            areas.add(new LifeArea(id,name,selected));
        }
        return areas;
    }

    private void drawLifeAreas(List<LifeArea> areas){
        for(LifeArea area : areas){
            final RelativeLayout rel = new RelativeLayout(getContext());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            params.topMargin = 80;
            rel.setLayoutParams(params);
            rel.setId(area.id);

            TextView title = new TextView(getContext());
            title.setText(area.title);
            title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            title.setTextSize(26);
            title.setGravity(Gravity.LEFT);
            title.setPaintFlags(title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
            title.setTextColor(getResources().getColor(R.color.colorAccent));

            Button addGoal = new Button(getContext());
            addGoal.setBackgroundResource(R.drawable.plus);
            addGoal.setPaintFlags(addGoal.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
            LinearLayout.LayoutParams butParams = new LinearLayout.LayoutParams(120,120);
            butParams.leftMargin = 100;
            butParams.topMargin = 15;
            addGoal.setLayoutParams(butParams);
            addGoal.setTextSize(16);
            addGoal.setGravity(Gravity.CENTER_HORIZONTAL);

            addGoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView newGoal = new TextView(getContext());
                    //popups and stuff
                    //
                    rel.addView(newGoal);
                }
            });

            LinearLayout ll = new LinearLayout(getContext());
            //ll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
            //        RelativeLayout.LayoutParams.WRAP_CONTENT));
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.addView(title);
            ll.addView(addGoal);

            rel.addView(ll);

            goalLayout.addView(rel);
        }
    }

}
