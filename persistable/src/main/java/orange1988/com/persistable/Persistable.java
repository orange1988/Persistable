package orange1988.com.persistable;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by Mr. Orange on 16/4/20.
 * <p/>
 * * 描述如何存储加载和更新数据集的接口
 *
 * @param <E> type of item1
 */
public interface Persistable<E> {

    /**
     * 从数据库查询所有
     *
     * @param readableDatabase
     * @return cursor
     */

    Cursor query(SQLiteDatabase readableDatabase);

    /**
     * 从数据库按条件查询
     *
     * @param readableDatabase
     * @return cursor
     */
    Cursor query(SQLiteDatabase readableDatabase, String sql, String[] args);

    /**
     * 从数据库查询一个条目
     *
     * @param cursor
     * @return a single item, read from this row of the cursor
     */
    E loadFrom(Cursor cursor);

    /**
     * 向数据库存入数据
     *
     * @param writableDatabase
     * @param items
     */
    boolean store(SQLiteDatabase writableDatabase, List<E> items);

    /**
     * 向数据库插入一条数据
     *
     * @param writableDatabase
     * @param item
     */
    long insert(SQLiteDatabase writableDatabase, E item);

    /**
     * 向数据库更新一条数据
     *
     * @param writableDatabase
     * @param item
     * @return the number of rows changed
     */
    int update(SQLiteDatabase writableDatabase, E item);

    /**
     * 从数据库删除数据
     *
     * @param writableDatabase
     * @param item
     */
    int delete(SQLiteDatabase writableDatabase, E item);


    /**
     * 向数据库中替换一条数据
     *
     * @param writableDatabase
     * @param item
     */
    long replace(SQLiteDatabase writableDatabase, E item);

    /**
     * 清除该表数据库内容
     *
     * @param writableDatabase
     */
    int clear(SQLiteDatabase writableDatabase);

}
