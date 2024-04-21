package com.example.aidme7;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import com.example.aidme7.DatabaseHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportToCsv {

    public static boolean exportDatabaseToCsv(Context context, String tableName) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);

        File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, tableName + ".csv");
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            if (cursor.moveToFirst()) {
                int columns = cursor.getColumnCount();
                for (int i = 0; i < columns; i++) {
                    writer.write(cursor.getColumnName(i) + ",");
                }
                writer.write("\n");

                do {
                    for (int i = 0; i < columns; i++) {
                        writer.write(cursor.getString(i) + ",");
                    }
                    writer.write("\n");
                } while (cursor.moveToNext());

                writer.close();
                cursor.close();

                Toast.makeText(context, "Exported to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(context, "No data to export", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error exporting data", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
