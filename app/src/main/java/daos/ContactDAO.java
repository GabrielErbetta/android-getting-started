package daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import models.Contact;

/**
 * Created by Erbetta on 06/02/2017.
 */

public class ContactDAO extends SQLiteOpenHelper {
    public ContactDAO(Context context) {
        super(context, "ContactManager", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE Contacts (id INTEGER PRIMARY KEY, " +
                                              "name TEXT NOT NULL, " +
                                              "address TEXT, " +
                                              "phone TEXT, " +
                                              "website TEXT, " +
                                              "rating REAL, " +
                                              "photo_path TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql;
        switch (oldVersion) {
            case 1:
                sql = "ALTER TABLE Contacts ADD COLUMN photo_path TEXT";
                db.execSQL(sql);
        }
    }

    public Boolean insert(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = getContentValues(contact);

        return db.insert("Contacts", null, values) != -1;
    }

    private ContentValues getContentValues(Contact contact) {
        ContentValues values = new ContentValues();
        values.put("name", contact.getName());
        values.put("address", contact.getAddress());
        values.put("phone", contact.getPhone());
        values.put("website", contact.getWebsite());
        values.put("rating", contact.getRating());
        values.put("photo_path", contact.getPhotoPath());
        return values;
    }

    public List<Contact> getContacts() {
        SQLiteDatabase db = getReadableDatabase();
        List<Contact> contact_list = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM Contacts;", null);
        while (cursor.moveToNext()) {
            Contact contact = new Contact();

            contact.setId(cursor.getLong(cursor.getColumnIndex("id")));
            contact.setName(cursor.getString(cursor.getColumnIndex("name")));
            contact.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            contact.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            contact.setWebsite(cursor.getString(cursor.getColumnIndex("website")));
            contact.setRating(cursor.getDouble(cursor.getColumnIndex("rating")));
            contact.setPhotoPath(cursor.getString(cursor.getColumnIndex("photo_path")));

            contact_list.add(contact);
        }
        cursor.close();

        return contact_list;
    }

    public Boolean delete(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();

        String[] values = {contact.getId().toString()};
        return db.delete("Contacts", "id = ?", values) != 0;
    }

    public Boolean update(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = getContentValues(contact);
        String[] where_values = {contact.getId().toString()};

        return db.update("Contacts", values, "id = ?", where_values) != -1;
    }
}
