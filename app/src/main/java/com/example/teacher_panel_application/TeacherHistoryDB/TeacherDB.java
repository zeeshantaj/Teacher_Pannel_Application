package com.example.teacher_panel_application.TeacherHistoryDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.teacher_panel_application.Models.PDFModel;
import com.example.teacher_panel_application.Models.UploadClassModel;

import java.util.ArrayList;
import java.util.List;

public class TeacherDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "classData.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "ClassData";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DEPARTMENT = "department";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_SUBJECT = "subject";
    public static final String COLUMN_TOPIC = "topic";
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_MINUTES = "minutes";
    public static final String COLUMN_END_DATE_TIME = "endDateTime";
    public static final String COLUMN_CURRENT_DATE_TIME = "currentDateTime";
    public static final String COLUMN_DATE_TIME = "dateTime";
    public static final String COLUMN_STARTED_TIME = "startedTime";


    public static final String PDF_DATA = "USER_PDF_DATA";
    private static final String PDFName = "pdfName";
    private static final String PDFUrl = "pdfUrl";
    private static final String dateTime = "data_time";
    private static final String year = "year";
    private static final String semester = "semester";
    private static final String purpose = "purpose";



    public TeacherDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DEPARTMENT + " TEXT,"
                + COLUMN_LOCATION + " TEXT,"
                + COLUMN_SUBJECT + " TEXT,"
                + COLUMN_TOPIC + " TEXT,"
                + COLUMN_KEY + "TEXT,"
                + COLUMN_MINUTES + " TEXT,"
                + COLUMN_END_DATE_TIME + " TEXT,"
                + COLUMN_CURRENT_DATE_TIME + " TEXT,"
                + COLUMN_DATE_TIME + " TEXT,"
                + COLUMN_STARTED_TIME + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);

        String CREATE_TABLE1 = "CREATE TABLE " + PDF_DATA + "("
                + PDFName + " TEXT,"
                + PDFUrl + " TEXT,"
                + dateTime + " TEXT,"
                + purpose + " TEXT,"
                + semester + " TEXT,"
                + year + "TEXT" + ")";
        db.execSQL(CREATE_TABLE1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void insertUserPDfData(PDFModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PDFName, model.getPDFName());
        values.put(PDFUrl, model.getPDFUrl());
        values.put(dateTime, model.getDateTime());
        values.put(semester, model.getSemester());
        values.put(year, model.getYear());
        values.put(purpose, model.getPurpose());
        db.insert(PDF_DATA, null, values);
        db.close();
    }
    public List<PDFModel> getUserPDf(){
        List<PDFModel> classDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PDF_DATA, null);

        if (cursor.moveToFirst()) {
            do {

                PDFModel model = new PDFModel();
                model.setPDFName(cursor.getString(cursor.getColumnIndexOrThrow(PDFName)));
                model.setPDFUrl(cursor.getString(cursor.getColumnIndexOrThrow(PDFUrl)));
                model.setSemester(cursor.getString(cursor.getColumnIndexOrThrow(semester)));
                model.setYear(cursor.getString(cursor.getColumnIndexOrThrow(year)));
                model.setPurpose(cursor.getString(cursor.getColumnIndexOrThrow(purpose)));
                model.setDateTime(cursor.getString(cursor.getColumnIndexOrThrow(dateTime)));
                classDataList.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return classDataList;
    }

    public void insertClassData(UploadClassModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, model.getName());
        values.put(COLUMN_DEPARTMENT, model.getDepartment());
        values.put(COLUMN_LOCATION, model.getLocation());
        values.put(COLUMN_SUBJECT, model.getSubject());
        values.put(COLUMN_TOPIC, model.getTopic());
        values.put(COLUMN_KEY, model.getKey());
        values.put(COLUMN_MINUTES, model.getMinutes());
        values.put(COLUMN_END_DATE_TIME, model.getEndDateTime());
        values.put(COLUMN_CURRENT_DATE_TIME, model.getCurrentDateTime());
        values.put(COLUMN_DATE_TIME, model.getDateTime());
        values.put(COLUMN_STARTED_TIME, model.getStartedTime());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<UploadClassModel> getAllClassData() {
        List<UploadClassModel> classDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {

                UploadClassModel model = new UploadClassModel();
                model.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                model.setDepartment(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEPARTMENT)));
                model.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)));
                model.setSubject(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBJECT)));
                model.setTopic(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOPIC)));
                model.setKey(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KEY)));
                model.setMinutes(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MINUTES)));
                model.setEndDateTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE_TIME)));
                model.setCurrentDateTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_DATE_TIME)));
                model.setDateTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_TIME)));
                model.setStartedTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STARTED_TIME)));
                classDataList.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return classDataList;
    }
    public void clearAllClassData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}