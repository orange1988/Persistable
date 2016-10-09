package orange1988.com.persistable;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by Mr. Orange on 16/4/22.
 */
public class DBManager {

    private DBOperation operation;

    //每次进入一个包厢需要建立一个新的表
    public DBManager(Context context, SQLiteOpenHelper helper) {
        operation = new DBOperation(context, helper);
    }

    public <E> List<E> rawQuery(PersistableBase<E> persistableBase, String sql) {
        return operation.rawQuery(persistableBase, sql);
    }

    public <E> long insert(PersistableBase<E> persistableBase, E item) {
       return operation.insert(persistableBase, item);
    }

    public <E> void store(PersistableBase<E> persistableBase, List<E> items) {
        operation.store(persistableBase, items);
    }

    public <E> void update(PersistableBase<E> persistableBase, E item) {
        operation.update(persistableBase, item);
    }

    public <E> void replace(PersistableBase<E> persistableBase, E item) {
        operation.replace(persistableBase, item);
    }

    public <E> void delete(PersistableBase<E> persistableBase, E item) {
        operation.delete(persistableBase, item);
    }

    public <E> void clear(PersistableBase<E> persistableBase) {
        operation.clear(persistableBase);
    }

    public void deleteTable(String tableName) {
        operation.deleteTable(tableName);
    }
}
