package cdictv.englishfour.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cdictv.englishfour.R;

/**
 * 作者： ZiJing Wu
 * 日期： 2017/12/6
 * 邮箱：296824952@qq.com
 * 内容：导入数据库
 */

public class DBManager {

	private final int BUFFER_SIZE = 1024;
	public static final String DB_NAME = "railwaysystem.db"; //保存的数据库文件名
	public static final String PACKAGE_NAME = "cdictv.englishfour";//包名
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME+"/databases";  //在手机里存放数据库的位置(/data/data/com.gzy.cet4/railwaysystem.db)


	private SQLiteDatabase database;
	private Context context;

	public DBManager(Context context) {
		this.context = context;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void setDatabase(SQLiteDatabase database) {
		this.database = database;
	}

	public void openDatabase() {
		this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
	}

	private SQLiteDatabase openDatabase(String dbfile) {

		try {

			File destDir = new File(DB_PATH);
			if (!destDir.exists()) {
				destDir.mkdirs();
			}

			if (!(new File(dbfile).exists())) {
				//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
				InputStream is = this.context.getResources().openRawResource(
						R.raw.railwaysystem); //欲导入的  数据库
				FileOutputStream fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
				Toast.makeText(context, "数据库导入完毕", Toast.LENGTH_SHORT).show();
			}

			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,null);
			return db;

		} catch (FileNotFoundException e) {
			Log.e("Database", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Database", "IO exception");
			e.printStackTrace();
		}
		return null;
	}

	public void closeDatabase() {
		this.database.close();

	}
}