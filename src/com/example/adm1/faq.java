package com.example.adm1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class faq extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.faq);
		/*
		 * This string is displayed as FAQ
		 */
		String content="1) What is ADM?\n" +
				"Ans: Absolute Download Manager\n"+"2) How to set the number of parallel connections?\n"
				+"Ans: Goto Mainpage -> Downloads, then select from the dropdown list.\n"
				+"3) How to schedule a download?\n"
				+"Ans: Goto Mainpage -> Scheduler, then set the time for scheduling. Then press the schedule button."
				+"Downloading automatically starts after the scheduling at the scheduled time\n"
				+"4) How to check the list of files downloaded?\n"
				+"Ans: Goto Mainpage -> log. There you will find the list of downloaded files.\n";
		//TextView tv=(TextView)findViewById(R.id.FAQs);
		//tv.setText(content);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
