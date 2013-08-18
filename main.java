package com.cartoon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thread);
		 Thread logotimer= new Thread(){
	        	public void run(){
	        			try{ 
	        				
	        				sleep(5000);
	        				Intent logomenu = new Intent(main.this,CartoonActivity.class);
	        				startActivity(logomenu);
	        			
	        			} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	        			
	        		
	        		finally{
	        	
     				
	        			finish();
	        		}
	        	
	        	}
	        };
	        logotimer.start();
	}
	}


