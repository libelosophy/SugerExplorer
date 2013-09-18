package com.surger.sugerexplorer;

import java.io.File;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ListView listView;

	private SimpleAdapter adapter;

	private FileDataServices fileServices;

	private LinkedList<List<Map<String, Object>>> pageStack;

	private List<Map<String, Object>> pageData = new ArrayList<Map<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		fileServices = new FileDataServices();

		// 检测 Sdcard（？） 是否挂载
		if (!isMediaMounted()) {
			showMSG("u can mount u sdcard ");
			TextView errorTextView = (TextView) findViewById(R.id.sdcard_asked);
			errorTextView.setVisibility(TextView.VISIBLE);
		} else {
			File filePath = Environment.getExternalStorageDirectory();

			pageData = fileServices.loadFileList(this, filePath);

			pageStack = new LinkedList<List<Map<String, Object>>>();

			pageStack.add(pageData);

			listView = (ListView) findViewById(R.id.fileLv);

			adapter = fileServices.getAdapter(this, pageData, listView);

			/*
			 * View view = adapter.getView(1, listView, null);
			 * 
			 * view.findViewById(R.id.fileCb).setVisibility(View.INVISIBLE);
			 */

			listView.setAdapter(adapter);

			// 设置listview item 监听，点击后或返回上级目录或进入下级目录，或对文件进行响应（没做还）
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					CheckBox cb = (CheckBox) view.findViewById(R.id.fileCb);
					cb.setChecked(!cb.isChecked());
					TextView tv = (TextView) view.findViewById(R.id.fileNameTv);
					// 构造下个页面 没有使用栈

					if (tv.getText().equals(".") && position == 0) {

						// pageStack.removeLast();
						pageData.clear();
						// pageData.addAll(pageStack.getLast());

						List<Map<String, Object>> list = fileServices
								.loadFileList(MainActivity.this, fileServices
										.getCurrentPath().getParentFile());
						// pageStack.addLast(list);
						// pageData.clear();
						pageData.addAll(list);

						Log.i("TAG", "----------------------->");
						adapter.notifyDataSetChanged();
					} else {
						String nextPath = null;

						nextPath = fileServices.getCurrentPath()
								.getAbsolutePath()
								+ "/"
								+ tv.getText().toString();

						File nextFilePath = new File(nextPath);

						if (nextFilePath.isDirectory()) {
							// pageStack.add(pageData);
							List<Map<String, Object>> list = fileServices
									.loadFileList(MainActivity.this, new File(
											nextPath));
							// pageStack.addLast(list);
							pageData.clear();
							pageData.addAll(list);

							adapter.notifyDataSetChanged();

						}

						Log.d("TAG", nextPath);

						Toast.makeText(getApplicationContext(),
								"hello u clicked", Toast.LENGTH_SHORT).show();
						Log.d("TAG", "hello");

					}
				}
			});

			listView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					// TODO Auto-generated method stub
					return false;
				}
			});

			registerForContextMenu(listView);
		}

	}

	private void showMSG(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

	}

	private boolean isMediaMounted() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Animation.DropDownDown

		// getMenuInflater().inflate(R.menu.options_menu, menu);
		// this.setTheme(R.style.AppTheme);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		// v.setAlpha(1);
		getMenuInflater().inflate(R.menu.context_menu, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onContextItemSelected(item);
	}

}
