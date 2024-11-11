package com.example.newsmreader.Dtabase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.newsmreader.Models.BillModel;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.Models.ReadModel;
import com.example.newsmreader.Models.SlabModel;
import com.example.newsmreader.S_P;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "smreaders.db";


    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL(Tables_.CUSTOMER_TABLE);
        DB.execSQL(Tables_.SLAB_QUERY);
        DB.execSQL(Tables_.READING_QUERY);
        DB.execSQL(Tables_.BILL_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP TABLE IF EXISTS " + Tables_.TABLE_CUSTOMER);
        DB.execSQL("DROP TABLE IF EXISTS " + Tables_.TABLE_SLAB);
        DB.execSQL("DROP TABLE IF EXISTS " + Tables_.TABLE_READING);
        DB.execSQL("DROP TABLE IF EXISTS " + Tables_.TABLE_BILL);
    }

    public boolean update_customer(String consumer_id, String amt, ReadModel readModel) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Column_.Meter_Number, readModel.getMeter_number());
        cv.put(Column_.Mobile, readModel.getMobile());
        cv.put("Latefee", "0");
        cv.put("AdditionalCharges", "0");
        cv.put("PrevBalance", amt);
        cv.put("isRead", "1");
        long result = db.update(Tables_.TABLE_CUSTOMER, cv, "CustomerID = ?", new String[]{consumer_id});
        if (result == -1) {
            db.close();
            return false;
        }
        db.close();
        return true;
    }

    public boolean insertreading(ReadModel model, Context context) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mobile", model.getMobile());
        contentValues.put("meter_number", model.getMeter_number());
        contentValues.put("Latitude", model.getLatitude());
        contentValues.put("Longitude", model.getLongitude());
        contentValues.put("Importeddate", model.getImporteddate());
        contentValues.put("customer_guid", model.getCustomer_guid());
        contentValues.put("reading", model.getReading());
        contentValues.put("Previous_Bal", model.getPrevious_Bal());
        contentValues.put("MinRent", model.getMinRent());
        contentValues.put("latefee", model.getLatefee());
        contentValues.put("Additional", model.getAdditional());
        contentValues.put("read_date", model.getRead_date());
        contentValues.put("read_time", model.getRead_time());
        contentValues.put("owner_guid", model.getOwner_guid());
        contentValues.put("BillAmount", model.getBillAmount());
        contentValues.put("SpotBilling", model.getSpotBilling());
        contentValues.put("Total", model.getTotal());
        contentValues.put("SpotBillingCharge", model.getSpotBillingCharge());
        contentValues.put("PrevReading", model.getPrevReading());
        contentValues.put("Billid", model.getBillid());
        contentValues.put("average", model.getAverage());
        contentValues.put("Send", "0");
        contentValues.put("Saved", "0");
        contentValues.put("usage", model.getUsage());
        contentValues.put("hold", model.getHold());
        contentValues.put("closed", model.getClosed());
        long result = db.insert(Tables_.TABLE_READING, null, contentValues);
        if (result == -1) {
            db.close();
            return false;
        }
        db.close();
        S_P.increment_the_readcount(context);
        return update_customer(model.getCustomer_guid(), model.getTotal(), model);

    }


    public boolean insertBilling(BillModel model, CustomerModel cModel, Context context) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("customer_guid", model.getCustomer_guid());
        contentValues.put("amount", model.getAmount());
        contentValues.put("recieved", model.getRecieved());
        contentValues.put("latefee", model.getLatefee());
        contentValues.put("balance", model.getBalance());
        contentValues.put("payment_date", model.getPayment_date());
        contentValues.put("owner_guid", model.getOwner_guid());
        contentValues.put("Addtnl", model.getAddtnl());
        contentValues.put("BillRecvd", model.getBillRecvd());
        contentValues.put("Paid_time", model.getPaid_time());
        contentValues.put("SpotBilling", model.getSpotBilling());
        contentValues.put("SpotBillingCharge", model.getSpotBillingCharge());
        contentValues.put("RecNo", model.getRecNo());
        contentValues.put("online", model.getOnline());

        contentValues.put("Reference", "0");
        contentValues.put("Send", "0");
        contentValues.put("Saved", "0");

        long result = db.insert(Tables_.TABLE_BILL, null, contentValues);
        if (result == -1) {
            db.close();
            return false;
        }
        db.close();

        S_P.increment_the_Billcount(context);
        return update_customer(model.getCustomer_guid(), cModel);

    }

    public boolean update_customer(String consumer_id, CustomerModel model) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Column_.Meter_Number, model.getMeter_number());
        cv.put(Column_.Mobile, model.getMobile());
        cv.put(Column_.isBill, "1");
        long result = db.update(Tables_.TABLE_CUSTOMER, cv, "CustomerID = ?", new String[]{consumer_id});
        if (result == -1) {
            db.close();
            return false;
        }
        db.close();
        return true;
    }

    public boolean update_Bill(String consumer_id, String ref) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("online", "1");
        cv.put("Reference", ref);

        long result = db.update(Tables_.TABLE_BILL, cv, "customer_guid = ?", new String[]{consumer_id});
        if (result == -1) {
            db.close();
            return false;
        }
        db.close();
        return true;
    }


    public boolean insertConsumerList(ArrayList<CustomerModel> yourObjects) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (CustomerModel obj : yourObjects) {
            ContentValues values = new ContentValues();
            values.put(Column_.CustomerID, obj.getCustomerID());
            values.put(Column_.Consumer, obj.getConsumer());
            values.put(Column_.Consumer_No, obj.getConsumer_no());
            values.put(Column_.Mobile, obj.getMobile());
            values.put(Column_.Address, obj.getAddress());
            values.put(Column_.Location, obj.getLocation());
            values.put(Column_.Category, obj.getCategory());
            values.put(Column_.Meter_Number, obj.getMeter_number());
            values.put(Column_.PrevReading, obj.getPrevReading());
            values.put(Column_.PrevBalance, obj.getPrevBalance());
            values.put(Column_.AdditionalCharges, obj.getAdditionalCharges());
            values.put(Column_.ServiceCharge, obj.getServiceCharge());
            values.put(Column_.EnableServiceCharge, obj.getEnableServiceCharge());
            values.put(Column_.Latefee, obj.getLatefee());
            values.put(Column_.Minrent, obj.getMinrent());
            values.put(Column_.Catid, obj.getCatid());
            values.put(Column_.PrevReadingDate, obj.getPrevReadingDate());
            values.put(Column_.PrevBillDate, obj.getPrevBillDate());
            values.put(Column_.BasedonReadDate, obj.getBasedonReadDate());
            values.put(Column_.Days, obj.getDays());
            values.put(Column_.Passwords, obj.getPasswords());
            values.put(Column_.NoofMonths, obj.getNoofMonths());
            values.put(Column_.Latitude, obj.getLatitude());
            values.put(Column_.Longitude, obj.getLongitude());
            values.put(Column_.readingexists, obj.getReadingexists());
            values.put(Column_.isRead, "0");
            values.put(Column_.isBill, "0");
            long result = db.insert(Tables_.TABLE_CUSTOMER, null, values);
            if (result == -1) {
                db.close();
                return false;
            }
        }


        db.close();

        return true;
    }

    public boolean insertSlab(ArrayList<SlabModel> list) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (SlabModel obj : list) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(Column_.CID, obj.getCategory_guid());
            contentValues.put(Column_.TITLE, obj.getTitle());
            contentValues.put(Column_.UNITFROM, obj.getUnit_from());
            contentValues.put(Column_.UNITTO, obj.getUnit_to());
            contentValues.put(Column_.RATE, obj.getRate());
            contentValues.put(Column_.ABOVE, obj.getAbove());
            contentValues.put(Column_.MinRent, obj.getMinRent());

            long result = db.insert(Tables_.TABLE_SLAB, null, contentValues);
            if (result == -1) {
                db.close();
                return false;
            }
        }
        db.close();
        return true;
    }


    // we have created a new method for reading all the courses.
    public ArrayList<CustomerModel> CustomerList(String s) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Tables_.TABLE_CUSTOMER + s, null);
        ArrayList<CustomerModel> courseModalArrayList = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new CustomerModel(
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3),
                        cursorCourses.getString(4),
                        cursorCourses.getString(5),
                        cursorCourses.getString(6),
                        cursorCourses.getString(7),
                        cursorCourses.getString(8),
                        cursorCourses.getString(9),
                        cursorCourses.getString(10),
                        cursorCourses.getString(11),
                        cursorCourses.getString(12),
                        cursorCourses.getString(13),
                        cursorCourses.getString(14),
                        cursorCourses.getString(15),
                        cursorCourses.getString(16),
                        cursorCourses.getString(17),
                        cursorCourses.getString(18),
                        cursorCourses.getString(19),
                        cursorCourses.getString(20),
                        cursorCourses.getString(21),
                        cursorCourses.getString(22),
                        cursorCourses.getString(23),
                        cursorCourses.getString(24),
                        cursorCourses.getString(25)
                ));
            } while (cursorCourses.moveToNext());

        }
        cursorCourses.close();
        return courseModalArrayList;
    }

    public CustomerModel Single_Customer(String s) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + Tables_.TABLE_CUSTOMER + s, null);
        CustomerModel courseModalArrayList = null;

        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList = new CustomerModel(
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3),
                        cursorCourses.getString(4),
                        cursorCourses.getString(5),
                        cursorCourses.getString(6),
                        cursorCourses.getString(7),
                        cursorCourses.getString(8),
                        cursorCourses.getString(9),
                        cursorCourses.getString(10),
                        cursorCourses.getString(11),
                        cursorCourses.getString(12),
                        cursorCourses.getString(13),
                        cursorCourses.getString(14),
                        cursorCourses.getString(15),
                        cursorCourses.getString(16),
                        cursorCourses.getString(17),
                        cursorCourses.getString(18),
                        cursorCourses.getString(19),
                        cursorCourses.getString(20),
                        cursorCourses.getString(21),
                        cursorCourses.getString(22),
                        cursorCourses.getString(23),
                        cursorCourses.getString(24),
                        cursorCourses.getString(25)
                );
            } while (cursorCourses.moveToNext());

        }
        cursorCourses.close();
        return courseModalArrayList;
    }

    @SuppressLint("Range")
    public ReadModel Singleread(String s) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cr = db.rawQuery("SELECT * FROM " + Tables_.TABLE_READING + s, null);
        ReadModel readModel = null;
        if (cr.moveToFirst()) {
            do {
                readModel = new ReadModel(cr.getString(cr.getColumnIndex("mobile")), cr.getString(cr.getColumnIndex("meter_number"))
                        , "" + cr.getString(cr.getColumnIndex("Latitude")), "" + cr.getString(cr.getColumnIndex("Longitude")),
                        "" + cr.getString(cr.getColumnIndex("Importeddate")), "" + cr.getString(cr.getColumnIndex("customer_guid"))
                        , "" + cr.getString(cr.getColumnIndex("reading")), "" + cr.getString(cr.getColumnIndex("Previous_Bal"))
                        , "" + cr.getString(cr.getColumnIndex("MinRent")), "" + cr.getString(cr.getColumnIndex("latefee"))
                        , "" + cr.getString(cr.getColumnIndex("Additional")), "" + cr.getString(cr.getColumnIndex("read_date"))
                        , "" + cr.getString(cr.getColumnIndex("read_time")), "" + cr.getString(cr.getColumnIndex("owner_guid"))
                        , "" + cr.getString(cr.getColumnIndex("BillAmount")), "" + cr.getString(cr.getColumnIndex("SpotBilling"))
                        , "" + cr.getString(cr.getColumnIndex("SpotBillingCharge")), "" + cr.getString(cr.getColumnIndex("PrevReading")),
                        "" + cr.getString(cr.getColumnIndex("Billid"))
                        , "" + cr.getString(cr.getColumnIndex("Total"))
                        , "" + cr.getString(cr.getColumnIndex("usage"))
                        , "" + cr.getString(cr.getColumnIndex("average"))
                        , "" + cr.getString(cr.getColumnIndex("hold"))
                        , "" + cr.getString(cr.getColumnIndex("closed")));
            } while (cr.moveToNext());

        }
        cr.close();
        return readModel;
    }

    @SuppressLint("Range")
    public BillModel SingleBill(String s) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cr = db.rawQuery("SELECT * FROM " + Tables_.TABLE_BILL + s, null);
        BillModel billModel = null;

        if (cr.moveToFirst()) {
            do {
                billModel = new BillModel("" + cr.getString(cr.getColumnIndex(Column_.Bill.customer_guid))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.amount))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.recieved))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.balance))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.latefee))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.payment_date))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.owner_guid))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.Addtnl))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.BillRecvd))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.Paid_time))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.SpotBilling))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.SpotBillingCharge))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.RecNo))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.online))
                        , "" + cr.getString(cr.getColumnIndex("Reference")));
            } while (cr.moveToNext());

        }
        cr.close();
        return billModel;
    }

    @SuppressLint("Range")
    public ArrayList<SlabModel> Slablist(String s, String s2) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cr = db.rawQuery("SELECT * FROM " + Tables_.TABLE_SLAB + " WHERE " + s + "='" + s2 + "'", null);
        ArrayList<SlabModel> list = new ArrayList<>();

        if (cr.moveToFirst()) {
            do {
                list.add(new SlabModel(
                        cr.getString(cr.getColumnIndex("ID")),
                        cr.getString(cr.getColumnIndex(Column_.CID)),
                        cr.getString(cr.getColumnIndex(Column_.TITLE)),
                        cr.getString(cr.getColumnIndex(Column_.UNITFROM)),
                        cr.getString(cr.getColumnIndex(Column_.UNITTO)),
                        cr.getString(cr.getColumnIndex(Column_.RATE)),
                        cr.getString(cr.getColumnIndex(Column_.ABOVE)),
                        cr.getString(cr.getColumnIndex(Column_.MinRent))

                ));
            } while (cr.moveToNext());

        }
        cr.close();
        return list;
    }

    @SuppressLint("Range")
    public ReadModel getreadingList() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cr = db.rawQuery("SELECT * FROM " + Tables_.TABLE_READING + " LIMIT '1'", null);
        ReadModel readModel = null;
        if (cr.moveToFirst()) {
            do {
                readModel = new ReadModel(cr.getString(cr.getColumnIndex("mobile")), cr.getString(cr.getColumnIndex("meter_number"))
                        , "" + cr.getString(cr.getColumnIndex("Latitude")), "" + cr.getString(cr.getColumnIndex("Longitude")),
                        "" + cr.getString(cr.getColumnIndex("Importeddate")), "" + cr.getString(cr.getColumnIndex("customer_guid"))
                        , "" + cr.getString(cr.getColumnIndex("reading")), "" + cr.getString(cr.getColumnIndex("Previous_Bal"))
                        , "" + cr.getString(cr.getColumnIndex("MinRent")), "" + cr.getString(cr.getColumnIndex("latefee"))
                        , "" + cr.getString(cr.getColumnIndex("Additional")), "" + cr.getString(cr.getColumnIndex("read_date"))
                        , "" + cr.getString(cr.getColumnIndex("read_time")), "" + cr.getString(cr.getColumnIndex("owner_guid"))
                        , "" + cr.getString(cr.getColumnIndex("BillAmount")), "" + cr.getString(cr.getColumnIndex("SpotBilling"))
                        , "" + cr.getString(cr.getColumnIndex("SpotBillingCharge")), "" + cr.getString(cr.getColumnIndex("PrevReading")),
                        "" + cr.getString(cr.getColumnIndex("Billid"))
                        , "" + cr.getString(cr.getColumnIndex("Total"))
                        , "" + cr.getString(cr.getColumnIndex("usage"))
                        , "" + cr.getString(cr.getColumnIndex("average"))
                        , "" + cr.getString(cr.getColumnIndex("hold"))
                        , "" + cr.getString(cr.getColumnIndex("closed")));
            } while (cr.moveToNext());

        }
        cr.close();
        return readModel;
    }

    public boolean Droptablereadingrow(String cid) {
        SQLiteDatabase db = this.getWritableDatabase();


        String whereClause = "customer_guid= ?";
        String[] whereArgs = {String.valueOf(cid)};


        int rowcount = db.delete(Tables_.TABLE_READING, whereClause, whereArgs);
        Log.e(">>>>>>>>>>>>>>", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + rowcount);


        db.close();
        if (rowcount > 0) {
            return true;
        } else {

            return false;

        }


    }


    public boolean Droptablebillingrow(String cid) {
        SQLiteDatabase db = this.getWritableDatabase();


        String whereClause = "customer_guid= ?";
        String[] whereArgs = {String.valueOf(cid)};


        int rowcount = db.delete(Tables_.TABLE_BILL, whereClause, whereArgs);


        db.close();
        if (rowcount > 0) {
            return true;
        } else {

            return false;

        }


    }

    @SuppressLint("Range")
    public BillModel getBillList() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cr = db.rawQuery("SELECT * FROM " + Tables_.TABLE_BILL + " LIMIT '1'", null);
        BillModel billModel = null;

        if (cr.moveToFirst()) {
            do {
                billModel = new BillModel("" + cr.getString(cr.getColumnIndex(Column_.Bill.customer_guid))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.amount))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.recieved))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.balance))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.latefee))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.payment_date))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.owner_guid))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.Addtnl))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.BillRecvd))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.Paid_time))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.SpotBilling))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.SpotBillingCharge))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.RecNo))
                        , "" + cr.getString(cr.getColumnIndex(Column_.Bill.online))
                        , "" + "" + cr.getString(cr.getColumnIndex("Reference")));
            } while (cr.moveToNext());

        }
        cr.close();
        return billModel;
    }


    public long getCount(String Tbname) {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, Tbname);
        db.close();
        return count;
    }


    public boolean Droptables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + Tables_.TABLE_SLAB);
        db.execSQL("DELETE FROM " + Tables_.TABLE_CUSTOMER);

        try {

            db.execSQL("DELETE FROM " + Tables_.TABLE_SLAB);
            return true;
        } catch (SQLException e) {

            return false;

        } finally {

            db.close();
        }


    }


}
