package shaiytan.irlbugtracker

import android.content.*
import android.database.Cursor
import android.database.sqlite.*
import models.*
import java.util.*

/**
 * Created by Shaiy'tan on 16.01.2018.
 */
private infix fun Cursor.id(name: String) = getInt(getColumnIndex(name))

private infix fun Cursor.field(name: String) = getString(getColumnIndex(name))
private infix fun Cursor.date(name: String) = getLong(getColumnIndex(name))

class MobileModel(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION), Model {
    override fun addBug(bug: TheBug) =
            writableDatabase.insert(TABLE_NAME, null, writeBug(bug)).toInt()

    override fun deleteBug(id: Int) {
        writableDatabase.delete(
                TABLE_NAME,
                "$COLUMN_ID = ?",
                arrayOf(id.toString())
        )
    }

    override fun getBugById(id: Int): TheBug? {
        val cursor = readableDatabase.query(
                true,
                TABLE_NAME,
                allColumns(),
                "$COLUMN_ID = ?",
                arrayOf(id.toString()),
                null, null, null, null)
        var bug: TheBug? = null
        cursor.use {
            if (it.moveToNext())
                bug = readBug(it)
        }
        return bug
    }

    override fun getBugs(type: BugType): List<TheBug> {
        val cursor = readableDatabase.query(
                TABLE_NAME,
                allColumns(),
                "$COLUMN_TYPE = ?",
                arrayOf(type.name), null, null, null)
        val bugslist = mutableListOf<TheBug>()
        cursor.use {
            while (it.moveToNext()) {
                bugslist.add(readBug(it))
            }
        }
        return bugslist
    }

    override fun updateBug(bug: TheBug) {
        writableDatabase.update(
                TABLE_NAME,
                writeBug(bug),
                "$COLUMN_ID=?",
                arrayOf(bug.id.toString())
        )
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_SCRIPT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP_TABLE_SCRIPT)
        onCreate(db)
    }


    companion object {
        private val DB_NAME = "foundbugs.db"
        private val DB_VERSION = 1

        private val TABLE_NAME = "bugs"

        private val COLUMN_ID = "_id"
        private val COLUMN_NAME = "name"
        private val COLUMN_DESCRIPTION = "description"
        private val COLUMN_DATE = "date"
        private val COLUMN_CATEGORY = "category"
        private val COLUMN_TYPE = "type"

        private fun allColumns() = arrayOf(
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_DESCRIPTION,
                COLUMN_TYPE,
                COLUMN_CATEGORY,
                COLUMN_DATE
        )

        private fun readBug(cursor: Cursor) = TheBug(
                id = cursor id COLUMN_ID,
                name = cursor field COLUMN_NAME,
                description = cursor field COLUMN_DESCRIPTION,
                category = Category.valueOf(cursor field COLUMN_CATEGORY),
                type = BugType.valueOf(cursor field COLUMN_TYPE),
                date = Date(cursor date COLUMN_DATE))

        private fun writeBug(bug: TheBug) = ContentValues().also { cv ->
            cv.put(COLUMN_NAME, bug.name)
            cv.put(COLUMN_DESCRIPTION, bug.description)
            cv.put(COLUMN_DATE, bug.date.time)
            cv.put(COLUMN_TYPE, bug.type.name)
            cv.put(COLUMN_CATEGORY, bug.category.name)
        }

        private val CREATE_TABLE_SCRIPT = """
            CREATE TABLE $TABLE_NAME(
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NAME TEXT NOT NULL,
            $COLUMN_DESCRIPTION TEXT,
            $COLUMN_DATE INTEGER NOT NULL,
            $COLUMN_CATEGORY TEXT NOT NULL,
            $COLUMN_TYPE TEXT NOT NULL
            );
        """
        private val DROP_TABLE_SCRIPT = "DROP TABLE IF EXISTS $TABLE_NAME:"
    }
}