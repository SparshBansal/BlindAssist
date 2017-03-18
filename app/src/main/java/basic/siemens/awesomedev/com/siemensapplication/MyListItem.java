package basic.siemens.awesomedev.com.siemensapplication;

import android.database.Cursor;
import android.graphics.Bitmap;

/**
 * Created by sparsh on 3/18/17.
 */
public class MyListItem{
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public static MyListItem fromCursor(Cursor cursor) {
        //TODO return your MyListItem from cursor.
        MyListItem mItem = new MyListItem();
        mItem.setBitmap();
    }
}
