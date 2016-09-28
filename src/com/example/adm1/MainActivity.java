package com.example.adm1;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
interface Constants {
	  String LOG = "com.example.adm1";
	} 
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(Constants.LOG, "The Application is launched. MainActivity onCreate called");
		File nfile=new File(Environment.getExternalStorageDirectory()+"/ADM");
        nfile.mkdir();
        /*
         * File Log button
         * Opens the log of Downloaded files
         */
        Button log=(Button)findViewById(R.id.log);
		log.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	Intent logintent=new Intent(MainActivity.this,log.class);
            	
            	startActivity(logintent);
            }
        });
		
		/*
		 * Download Button
		 * Opens Download Handler
		 */
		Button dwnld_now=(Button)findViewById(R.id.down);
		dwnld_now.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	Intent dwnintent=new Intent(MainActivity.this,down.class);
            	dwnintent.putExtra("args",0);
            	startActivity(dwnintent);
            }
        });
		
		/*
		 * 
		 * Scheduler Button
		 * User can set the desired time for download
		 */
		Button sch=(Button)findViewById(R.id.sch);
		sch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	Intent schintent=new Intent(MainActivity.this,sch.class);
            	startActivity(schintent);
            }
        });
		/*
		 * 
		 * FAQ Button
		 * User can view here for getting familiar with the features of the application
		 */
		Button faq=(Button)findViewById(R.id.faq);
		faq.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	Intent faqintent=new Intent(MainActivity.this,faq.class);
            	startActivity(faqintent);
            }
        });
		
		/*
		 * 
		 * Rate the App button
		 * User can rate our app here
		 */
		Button rate=(Button)findViewById(R.id.rate);
		rate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	Intent rateintent=new Intent(MainActivity.this,rate.class);
            	startActivity(rateintent);
            }
        });
		
		/*
		 * 
		 * Exit Button
		 * This button Closes the application
		 */
		Button ext=(Button)findViewById(R.id.ext);
		ext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	Log.d("Exit Button is Called","The App is Closed");
            	System.exit(0);
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
