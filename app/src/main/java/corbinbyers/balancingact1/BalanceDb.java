package corbinbyers.balancingact1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Corbin Byers on 3/4/2017.
 */

public class BalanceDb extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Balance.db";
    public static final int DB_VERSION = 1;
    private static BalanceDb instance;

    public static final String SQL_CREATE_LifeArea =
            "CREATE TABLE " + LifeEntry.Table_Name + " (" +
                    LifeEntry._ID + " INTEGER PRIMARY KEY," +
                    LifeEntry.nameColumn + " TEXT," +
                    LifeEntry.selectedColumn + " INTEGER," +
                    LifeEntry.colorColumn + " INTEGER," +
                    LifeEntry.stringID + " INTEGER)";
    public static final String SQL_DELETE_LifeArea =
            "DROP TABLE IF EXISTS " + LifeEntry.Table_Name;



    public BalanceDb(Context context){
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    public static BalanceDb getInstance(Context context){
        if(instance == null){
            instance = new BalanceDb(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //db.execSQL(sql create entries);
        System.out.println("*************************************");
        db.execSQL(SQL_CREATE_LifeArea);
        addDefaults(db);
    }

    public void createLifeAreaTable(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_LifeArea);
        addDefaults(db);
    }

    public void addDefaults(SQLiteDatabase db){
        String[] defaults = {"+ Health", "+ Finance", "+ Family", "+ Religion"};

        for (String d:defaults) {
            ContentValues values = new ContentValues();
            values.put(BalanceDb.LifeEntry.nameColumn, d);
            values.put(BalanceDb.LifeEntry.selectedColumn, 0);
            values.put(BalanceDb.LifeEntry.colorColumn, R.color.colorPrimaryLight);
            values.put(BalanceDb.LifeEntry.stringID, 0);
            long RowID = db.insert(BalanceDb.LifeEntry.Table_Name, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //delete data
        //start over onCreate(db);
    }

    public void deleteLifeAreasTable(SQLiteDatabase db){
        db.execSQL(SQL_DELETE_LifeArea);
    }

    public static class LifeEntry implements BaseColumns {
        public static final String Table_Name = "LifeAreas";
        public static final String nameColumn = "Name";
        public static final String selectedColumn = "Selected";
        public static final String colorColumn = "Color";
        public static final String stringID = "StringID";
    }


}

