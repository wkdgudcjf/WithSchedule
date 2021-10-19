package com.example.withschedule.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WithsDB extends SQLiteOpenHelper{

	public WithsDB(Context context)
	{
		super(context,"Withs.db",null,1);
	}
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE schedule_tb ( sno INTEGER PRIMARY KEY,title TEXT, starttime TEXT, type INTEGER, memo TEXT,isopen INTEGER,ispublic INTEGER);");
		db.execSQL("CREATE TABLE alarm_tb ( ano INTEGER PRIMARY KEY,isset INTEGER, time INTEGER);");
		db.execSQL("CREATE TABLE friend_tb ( email TEXT,phonenum TEXT, nickname TEXT);");
	//	db.execSQL("CREATE TABLE phonebook_tb ( phonenum TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("DROP TABLE IF EXISTS schedule_tb;");
		db.execSQL("DROP TABLE IF EXISTS friend_tb;");
	//	db.execSQL("DROP TABLE IF EXISTS phonebook_tb;");
		db.execSQL("DROP TABLE IF EXISTS alarm_tb;");
		onCreate(db);
	}
	
}
