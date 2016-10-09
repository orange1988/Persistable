package orange1988.com.persistable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatatypeMismatchException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr. Orange on 16/4/20.
 */
public class DBOperation {

    private static final String TAG = "DBOperation";

    private final SQLiteOpenHelper helper;
    private final Context context;
    private SQLiteDatabase mWritableDB = null;
    private SQLiteDatabase mReadableDB = null;

    public DBOperation(Context context, SQLiteOpenHelper helper) {
        this.helper = helper;
        this.context = context;
    }

    public SQLiteDatabase getWritable() {
        if (mWritableDB == null) {
            if (helper != null) {
                try {
                    mWritableDB = helper.getWritableDatabase();
                } catch (SQLiteException e1) {
                    try {
                        mWritableDB = helper.getWritableDatabase();
                    } catch (SQLiteException e2) {
                        mWritableDB = null;
                    }
                }
            }
        }
        return mWritableDB;
    }

    public SQLiteDatabase getReadable() {
        if (mReadableDB == null) {
            if (helper != null) {
                try {
                    mReadableDB = helper.getReadableDatabase();
                } catch (SQLiteException e1) {
                    try {
                        mReadableDB = helper.getReadableDatabase();
                    } catch (SQLiteException e2) {
                        mReadableDB = null;
                    }
                }
            }
        }
        return mReadableDB;
    }

    public <E> List<E> rawQuery(final PersistableBase<E> persistableBase, String sql) {
        List<E> cached = null;
        SQLiteDatabase db = getReadable();
        if (db != null && persistableBase != null) {
            Cursor cursor = db.rawQuery(sql, null);
            try {
                if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                    cached = new ArrayList<E>();
                    E tItem = null;
                    do {
                        tItem = persistableBase.loadFrom(cursor);
                        if (tItem != null) {
                            cached.add(tItem);
                        }
                    } while (cursor.moveToNext());
                }
            } catch (SQLiteDatatypeMismatchException e) {
                Log.e(TAG, "SQLiteDatatypeMismatchException : db is empty!");
                cached = null;
            } catch (Exception e) {
                Log.e(TAG, "load from db error");
                cached = null;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        Log.d(TAG, "result: " + cached + ", sql: " + sql);
        return cached;
    }

    //适用于查询数据库的相关操作，如果要更新表建议使用execSql
    public Cursor rawQuery(String sql) {
        Cursor cursor = null;
        final SQLiteDatabase db = getWritable();
        if (db != null) {
            try {
                cursor = db.rawQuery(sql, null);
            } catch (Exception e) {
                Log.e(TAG, "execute sql error ");
            }
        }
        return cursor;
    }

    public <E> boolean store(PersistableBase<E> persistableBase, List<E> items) {
        boolean isSuccess = false;
        final SQLiteDatabase db = getWritable();
        if (db != null) {
            boolean beginSuccess = beginTransaction(db);
            try {
                isSuccess = persistableBase.store(db, items);
                setTransactionSuccessful(db, beginSuccess);
            } catch (Exception e) {
                Log.e(TAG, "store items error");
            } finally {
                endTransaction(db, beginSuccess);
            }
        }
        return isSuccess;
    }

    public <E> long insert(PersistableBase<E> persistableBase, E item) {
        long resultRowId = -1;
        final SQLiteDatabase db = getWritable();
        if (db != null) {
            boolean beginSuccess = beginTransaction(db);
            try {
                resultRowId = persistableBase.insert(db, item);
                setTransactionSuccessful(db, beginSuccess);
            } catch (Exception e) {
                Log.e(TAG, "insert specified item error");
            } finally {
                endTransaction(db, beginSuccess);
            }
        }
        Log.d(TAG, "insert persistableBase: " + persistableBase + " item: " + item +
                ", resultRowId: " + resultRowId + ", db: " + db);
        return resultRowId;
    }

    public <E> int update(PersistableBase<E> persistableBase, E item) {
        int changeNum = 0;
        final SQLiteDatabase db = getWritable();
        if (db != null) {
            boolean beginSuccess = beginTransaction(db);
            try {
                changeNum = persistableBase.update(db, item);
                setTransactionSuccessful(db, beginSuccess);
            } catch (Exception e) {
                Log.e(TAG, "update specified item error");
            } finally {
                endTransaction(db, beginSuccess);
            }
        }
        Log.d(TAG, "update, db: " + db);
        return changeNum;
    }

    public <E> long replace(PersistableBase<E> persistableBase, E item) {
        long nRowId = -1;
        final SQLiteDatabase db = getWritable();
        if (db != null && persistableBase != null && item != null) {
            boolean beginSuccess = beginTransaction(db);
            try {
                nRowId = persistableBase.replace(db, item);
                setTransactionSuccessful(db, beginSuccess);
            } catch (Exception e) {
                Log.e(TAG, "replace specified item error, exception: " + e);
            } finally {
                endTransaction(db, beginSuccess);
            }
        }
        Log.d(TAG, "replace persistableBase: " + persistableBase + ", new row id: " + nRowId + ", item: " + item);
        return nRowId;
    }

    public <E> int delete(PersistableBase<E> persistableBase, E item) {
        int changeNum = 0;
        final SQLiteDatabase db = getWritable();
        if (db != null) {
            boolean beginSuccess = beginTransaction(db);
            try {
                changeNum = persistableBase.delete(db, item);
                setTransactionSuccessful(db, beginSuccess);
            } catch (Exception e) {
                Log.e(TAG, "delete specified item error");
            } finally {
                endTransaction(db, beginSuccess);
            }
        }
        Log.d(TAG, "delete item, db: " + db + ", changeNum: " + changeNum + ", item: " + item);
        return changeNum;
    }

    public <E> int clear(PersistableBase<E> persistableBase) {
        int changeNum = 0;
        final SQLiteDatabase db = getWritable();
        if (db != null) {
            boolean beginSuccess = beginTransaction(db);
            try {
                changeNum = persistableBase.clear(db);
                setTransactionSuccessful(db, beginSuccess);
            } catch (Exception e) {
                Log.e(TAG, "clear specified table error");
            } finally {
                endTransaction(db, beginSuccess);
            }
        }
        Log.d(TAG, "clear database, db: " + db + ", changeNum: " + changeNum + ", persistanbleBase: " + persistableBase);
        return changeNum;
    }

    public int deleteTable(String tableName) {
        int changeNum = 0;
        final SQLiteDatabase db = getWritable();
        if (db != null) {
            boolean beginSuccess = beginTransaction(db);
            try {
                changeNum = db.delete(tableName, null, null);
                setTransactionSuccessful(db, beginSuccess);
            } catch (Exception e) {
                Log.e(TAG, "clear data error");
            } finally {
                endTransaction(db, beginSuccess);
            }
        }
        Log.d(TAG, "deleteTable, db: " + db + ", tableName: " + tableName + ", changeNum: " + changeNum);
        return changeNum;
    }

    public void close() {
        SQLiteDatabase writable = getWritable();
        if (writable != null) {
            writable.close();
        }
        SQLiteDatabase readable = getReadable();
        if (readable != null) {
            readable.close();
        }
    }

    public boolean deleteDB(Context context) {
        boolean isSuccess = false;
        if (context != null && helper != null) {
            close();
            String dataBaseName = helper.getDatabaseName();
            if (!TextUtils.isEmpty(dataBaseName)) {
                isSuccess = context.deleteDatabase(helper.getDatabaseName());
            }
        }
        return isSuccess;
    }

    //判断当前Thread下数据库是否inTransaction
    public boolean inTransaction(SQLiteDatabase db) {
        return db == null ? false : db.inTransaction();
    }

    public boolean beginTransaction(SQLiteDatabase db) {
        boolean success = false;
        if (db != null && !inTransaction(db)) {
            db.beginTransaction();
            success = true;
            Log.d(TAG, "beginTransaction - SUCCESS!");
        } else {
            Log.d(TAG, "beginTransaction - ALREADY IN TRANSACTION");
        }
        return success;
    }

    public void setTransactionSuccessful(SQLiteDatabase db) {
        setTransactionSuccessful(db, false);
    }

    public boolean setTransactionSuccessful(SQLiteDatabase db, boolean force) {
        boolean success = false;
        if (db != null && (!inTransaction(db) || force)) {
            db.setTransactionSuccessful();
            success = true;
            Log.d(TAG, "setTransactionSuccessful - SUCCESS!");
        } else {
            Log.d(TAG, "setTransactionSuccessful - ALREADY IN TRANSACTION!");
        }
        return success;
    }

    public void endTransaction(SQLiteDatabase db) {
        endTransaction(db, false);
    }

    public boolean endTransaction(SQLiteDatabase db, boolean force) {
        boolean success = false;
        if (db != null && (!inTransaction(db) || force)) {
            db.endTransaction();
            success = true;
            Log.d(TAG, "endTransaction - SUCCESS!");
        } else {
            Log.d(TAG, "endTransaction - ALREADY IN TRANSACTION!");
        }
        return success;
    }

    public SQLiteDatabase beginWritableTransaction() {
        SQLiteDatabase db = getWritable();
        beginTransaction(db);
        return db;
    }

}
