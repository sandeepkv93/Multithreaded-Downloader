package com.example.adm1;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class log extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/*
		 * Displaying the log of files downloaded in ListView
		 */
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);
		String fileName="";
		File sdCardRoot = Environment.getExternalStorageDirectory();
		final ListView listview = (ListView) findViewById(R.id.listView1);
		final ArrayList<String> list = new ArrayList<String>();
		File yourDir = new File(sdCardRoot, "/ADM/");
		for (File f : yourDir.listFiles()) { 
		    if (f.isFile())
		        fileName = f.getName();
		        list.add(fileName);
		}
		final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
		    listview.setAdapter(adapter);
		    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    	@Override
				public void onItemClick(AdapterView<?> parent, final View view, int Position, long id) {
		    		final String item = (String) parent.getItemAtPosition(Position);
		    		Log.d("File Logs Class","The Clicked file "+item+"is opened");
		    		final File SDCardRooT = Environment.getExternalStorageDirectory();
		    		Intent intent = new Intent();
		    		intent.setAction(android.content.Intent.ACTION_VIEW);
		    		File file = new File(SDCardRooT+"/ADM/"+item);
		    		
		    		String ext = null;
		            String s = file.getName();
		            int i = s.lastIndexOf('.');
		            /*
		             * Getting the file extension
		             */
		            if (i > 0 && i < s.length() - 1) {
		                ext = s.substring(i + 1).toLowerCase();
		                //Toast.makeText(getApplicationContext(), "Extension is "+ext, Toast.LENGTH_SHORT).show();
		            }
		            /*
		             * This will open different type of files
		             */
		    		if(ext.equals("mp4") || ext.equals("3gp") || ext.equals("avi") || (ext.equals("flv"))){
		    			intent.setDataAndType(Uri.fromFile(file), "video/*");
		    		}
		    		else if(ext.equals("pdf"))
		    		{
		    			intent.setDataAndType(Uri.fromFile(file), "application/pdf");
		    		}
		    		
		    		else if(ext.equals("mp3"))
		    		{
		    			intent.setDataAndType(Uri.fromFile(file), "audio/*");
		    		}
		    		else if(ext.equals("txt"))
		    		{
		    			intent.setDataAndType(Uri.fromFile(file), "text/*");
		    		}
		    		else
		    		{
		    			 MimeTypeMap mime = MimeTypeMap.getSingleton();
		                 String type = mime.getMimeTypeFromExtension(ext);
		               
		                 intent.setDataAndType(Uri.fromFile(file),type);
		    		}
		    		startActivity(intent);
				}

		      });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
