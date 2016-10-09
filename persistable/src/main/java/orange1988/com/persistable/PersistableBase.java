package orange1988.com.persistable;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.tencent.qqsports.common.toolbox.Loger;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mr. Orange on 16/4/20.
 */
public abstract class PersistableBase<E> implements Persistable<E> {

    private static final String TAG = "PersistableBase";

    protected String mTableName;
    protected HashMap<String, Integer> sColumnIndexMap = new HashMap<String, Integer>(10);

    public PersistableBase(String tableName) {
        this.mTableName = tableName;
    }

    public void setTableName(String tableName) {
        this.mTableName = tableName;
    }

    public String getTableName() {
        return mTableName;
    }

    public abstract ContentValues getContentValues(E item);

    @Override
    public Cursor query(SQLiteDatabase readableDatabase) {
        return readableDatabase.query(getTableName(), null, null, null, null, null, null);
    }

    @Override
    public Cursor query(SQLiteDatabase readableDatabase, String sql, String[] args) {
        Loger.d(TAG, "query, sql: " + sql + ", args: " + args);
        return readableDatabase != null ? readableDatabase.rawQuery(sql, args) : null;
    }

    @Override
    public long insert(SQLiteDatabase writableDatabase, E item) {
        long nRowId = -1;
        if (writableDatabase != null) {
            nRowId = writableDatabase.insert(getTableName(), null, getContentValues(item));
        }
        Loger.d(TAG, "insert, item: " + item + ", rowId: " + nRowId);
        return nRowId;
    }

    @Override
    public boolean store(SQLiteDatabase writableDatabase, List<E> items) {
        boolean isSuccess = true;
        if (items != null && items.size() > 0) {
            long nRowId;
            for (E item : items) {
                nRowId = insert(writableDatabase, item);
                isSuccess = isSuccess ? (nRowId != -1) : isSuccess;
            }
        }
        return isSuccess;
    }

    @Override
    public int clear(SQLiteDatabase writableDatabase) {
        int changeNum = 0;
        if (writableDatabase != null) {
            changeNum = writableDatabase.delete(getTableName(), null, null);
        }
        return changeNum;
    }

    @Override
    public long replace(SQLiteDatabase writableDatabase, E item) {
        long resultRowId = -1;
        if (writableDatabase != null) {
            resultRowId = writableDatabase.replace(getTableName(), null, getContentValues(item));
        }
        Loger.d(TAG, "replace the new rowid: " + resultRowId);
        return resultRowId;
    }

    protected int getColumnIndex(String column, Cursor cursor) {
        int index = -1;
        if (!TextUtils.isEmpty(column)) {
            Integer indexInteger = sColumnIndexMap.get(column);
            if (indexInteger != null) {
                index = indexInteger;
            } else if (cursor != null) {
                index = cursor.getColumnIndex(column);
                sColumnIndexMap.put(column, index);
            }
        }
        return index;
    }
}
