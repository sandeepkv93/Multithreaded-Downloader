package com.example.adm1;

import java.util.Calendar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class sch extends Activity{
	static long period;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sch);
		final TimePicker dp=(TimePicker)findViewById(R.id.timePicker1);
		Button schdown=(Button)findViewById(R.id.schbutton);
		schdown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	Toast.makeText(sch.this, "Scheduled", Toast.LENGTH_SHORT).show();
            	Calendar now = Calendar.getInstance();
            	//Toast.makeText(sch.this, dp.getCurrentHour()+" "+dp.getCurrentMinute()+"   ---   "+now.get(Calendar.HOUR_OF_DAY)+" "+now.get(Calendar.MINUTE), Toast.LENGTH_LONG).show();
                period=((dp.getCurrentHour()-now.get(Calendar.HOUR_OF_DAY))*60*60 + (dp.getCurrentMinute()-now.get(Calendar.MINUTE))*60 - now.get(Calendar.SECOND))*1000;
                //new CountDown().execute(Long.toString(period));
            	//Toast.makeText(sch.this,""+period+"",Toast.LENGTH_LONG).show();
                EditText editText = (EditText) findViewById(R.id.editText1);
    		    String Download_path= editText.getEditableText().toString();
    		    final String path=Download_path;
    		    Log.d("Scheduler Class","Scheduler is called at "+dp.getCurrentHour()+":"+dp.getCurrentMinute());
            	/*
            	 * Alaram clock will be set
            	 */
    		    new CountDownTimer(period, 1000) {
            		

          		     public void onTick(long millisUntilFinished) {
          		     }

          		     public void onFinish() {
          		    	 /*
          		    	  * When alarm is invoked.
          		    	  * Create an intent and pass the activity from this class to Download class
          		    	  */
          		    	 Log.d("Scheduler Class","Scheduled Download Started");
          		    	 Toast.makeText(sch.this, "Download Starts", Toast.LENGTH_LONG).show();
          		    	Intent in=new Intent(sch.this,down.class);
          		    	in.putExtra("args",100);
          		    	in.putExtra("path",path);
                    	startActivity(in);
          		    	
          		     }
          		  }.start();
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

/*class CountDown extends AsyncTask<sch,Void,sch>
{
    
	@Override
	protected String doInBackground(sch... params) {
		// TODO Auto-generated method stub
		try {   
				new CountDownTimer(sch.period, 1000) {

     		     public void onTick(long millisUntilFinished) {
     		     }

     		     public void onFinish() {}
     		  }.start();
            	
            } catch (Exception e) {} 

		return "not executed";
	}
	@Override
	public void onPostExecute(Main param)
	{
		Intent in=new Intent(result,down.class);
	    in.putExtra("args",100);
	    startActivity(in);
	}
	
}*/

