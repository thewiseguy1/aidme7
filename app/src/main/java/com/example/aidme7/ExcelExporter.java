package com.example.aidme7;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelExporter {

    public static void exportToExcel(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USERS, null);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Users");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Username");
        headerRow.createCell(2).setCellValue("Email");
        headerRow.createCell(3).setCellValue("Password");

        while (cursor.moveToNext()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
            row.createCell(1).setCellValue(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME)));
            row.createCell(2).setCellValue(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL)));
            row.createCell(3).setCellValue(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD)));
        }

        cursor.close();

        try {
            File directory = new File(Environment.getExternalStorageDirectory(), "ExcelFiles");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, "users.xlsx");
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.close();

            Toast.makeText(context, "Excel file saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to save Excel file", Toast.LENGTH_SHORT).show();
        }
    }
}
