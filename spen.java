package com.cartoon;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import com.samsung.spensdk.SCanvasConstants;
import com.samsung.spensdk.SCanvasView;
import com.samsung.spensdk.applistener.SCanvasInitializeListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

public class spen extends Activity implements android.view.View.OnClickListener {
	SCanvasView foreground;
	static SCanvasView background;
	ImageButton save, pencil, eraser, move, undo, redo, zoomin,zoomout;
	Parcelable p;
	public static final String SAVED_FILE_EXTENSION = "png";

	public static final String TAG = "Example1";

	public static Bitmap saveimage, bmcamera, bmgallery, bmedit, bmsave,bgimage;
	byte a[];
	private File mFolder = null;
	Context mContext = null;
	public final static String APP_PATH_SD_CARD = "/DesiredSubfolderName/";
	public final static String APP_THUMBNAIL_PATH_SD_CARD = "thumbnails";
	FileOutputStream fos;
	String Filename;
	static Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spen);

		foreground = (SCanvasView) findViewById(R.id.canvas_view);
		background = (SCanvasView) findViewById(R.id.canvas_view2);

		pencil = (ImageButton) findViewById(R.id.pencil);
		eraser = (ImageButton) findViewById(R.id.eraser);
		undo = (ImageButton) findViewById(R.id.undo);
		redo = (ImageButton) findViewById(R.id.redo);
		
		zoomin = (ImageButton) findViewById(R.id.zoomin);
		zoomout = (ImageButton) findViewById(R.id.zoomout);
		save = (ImageButton) findViewById(R.id.save);
		pencil.setOnClickListener(this);
		eraser.setOnClickListener(this);
		undo.setOnClickListener(this);
		redo.setOnClickListener(this);

		zoomin.setOnClickListener(this);
		zoomout.setOnClickListener(this);
		save.setOnClickListener(this);
		HashMap<String, Integer> settingresourcemap = new HashMap<String, Integer>();
		settingresourcemap.put(SCanvasConstants.LAYOUT_PEN_SPINNER,
				R.layout.mspinner);
		settingresourcemap.put(SCanvasConstants.LOCALE_PEN_SETTING_TITLE,
				R.string.pensettings);
		settingresourcemap.put(SCanvasConstants.LOCALE_ERASER_SETTING_TITLE,
				R.string.erasersettings);
		settingresourcemap.put(
				SCanvasConstants.LOCALE_ERASER_SETTING_CLEARALL_MESSAGE,
				R.string.confirmclear);
		settingresourcemap.put(SCanvasConstants.LOCALE_ERASER_SETTING_CLEARALL,
				R.string.clearall);
		settingresourcemap.put(SCanvasConstants.LAYOUT_TEXT_SPINNER,
				R.layout.mspinnertext);
		settingresourcemap.put(SCanvasConstants.LAYOUT_TEXT_SPINNER_TABLET,
				R.layout.mspinnertext_tablet);
		boolean clearallerasersetings = true;
		FrameLayout fl = (FrameLayout) findViewById(R.id.setting_container);
		foreground.createSettingView(fl, settingresourcemap,
				clearallerasersetings);
		background
		.setSCanvasInitializeListener(mscanvasinitializelistener);
if(getIntent().hasExtra("filename")){
	Filename = getIntent().getStringExtra("filename");
	
}
		

		
		
		}
				
	

	@SuppressWarnings("null")

	private float mZoomValue = 1f;
	private float mMinZoom = 1.0f;
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.pencil:
			foreground.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
			foreground
					.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN);
			break;
		case R.id.eraser:
			foreground
					.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_ERASER);
			foreground
					.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER);
			break;
		case R.id.undo:
			foreground.undo();
			break;
		case R.id.redo:
			foreground.redo();
			break;
		case R.id.zoomin:
			foreground.zoomTo(mZoomValue += .2);
			background.zoomTo(mZoomValue += .2);
			if (mZoomValue > 50)
				mZoomValue = 50;
			break;
		case R.id.zoomout:
			foreground.zoomTo(mZoomValue -= .2);
			background.zoomTo(mZoomValue -= .2);
			if (mZoomValue < mMinZoom)
				mZoomValue = mMinZoom;
             break;
		
		case R.id.save:
			byte [] by = foreground.getData();
		bmsave = BitmapFactory.decodeByteArray(by, 0, by.length);
		if(Filename == null){
			
			AlertDialog.Builder alert = new AlertDialog.Builder(spen.this);
			alert.setTitle("Please Enter The Name Before Saving");  
			alert.setMessage("Enter name :");  
		

			 // Set an EditText view to get user input   
			 final EditText input = new EditText(this); 
			 alert.setView(input);
			 
			 alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
				    public void onClick(DialogInterface dialog, int whichButton) {  
				         Filename = input.getText().toString();
				         saveimage (Filename);
				    	    return;                  
				       }  
				     });  
			 alert.show();
			
		}
		else {
			saveimage(Filename);
		}
		 

			
			break;
			

	
	
	
		}
	}
	

	SCanvasInitializeListener mscanvasinitializelistener = new SCanvasInitializeListener() {

		@Override
		public void onInitialized() {
			// TODO Auto-generated method stub
			if (getIntent().hasExtra("byteArray")) {

				saveimage = BitmapFactory.decodeByteArray(getIntent()
						.getByteArrayExtra("byteArray"), 0, getIntent()
						.getByteArrayExtra("byteArray").length);
				background.setBGImage(saveimage);
				
			} else if (getIntent().hasExtra("bytegallery")) {
				bmgallery = BitmapFactory.decodeByteArray(getIntent()
						.getByteArrayExtra("bytegallery"), 0, getIntent()
						.getByteArrayExtra("bytegallery").length);
				background.setBGImage(bmgallery);
			} else if (getIntent().hasExtra("bytecamera")) {
				bmcamera = BitmapFactory.decodeByteArray(getIntent()
						.getByteArrayExtra("bytecamera"), 0, getIntent()
						.getByteArrayExtra("bytecamera").length);
				Toast.makeText(getApplicationContext(), "hii", Toast.LENGTH_LONG).show();
				background.setBGImage(bmcamera);
			}
		}
	};
	/*
	SCanvasInitializeListener mscanvasinitializelistenergallery = new SCanvasInitializeListener() {

		@Override
		public void onInitialized() {
			// TODO Auto-generated method stub
			background.setBGImage(bmgallery);
		}
	};
	SCanvasInitializeListener mscanvasinitializelistenercamera = new SCanvasInitializeListener() {

		@Override
		public void onInitialized() {
			// TODO Auto-generated method stub
			background.setBGImage(bmcamera);
		}
	};
	
	*/
	
	public void saveimage(String savefile){
		Filename =savefile;
		 String s = new String(Filename+ ".jpg");
			try {
				File sdCard = Environment.getExternalStorageDirectory();
				File directory = new File(sdCard.getAbsolutePath()
						+ "/CartoonYouself");
				directory.mkdirs();
				File file = new File(directory, s);
				file.createNewFile();
				FileOutputStream out1 = new FileOutputStream(file);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				bmsave.compress(Bitmap.CompressFormat.JPEG, 100, out);
				byte cy[]= out.toByteArray();
				out1.write(cy);
				// ---------------------

				
				out1.flush();
				out1.close();
				Toast.makeText(getBaseContext(),
						"File saved successfully!", Toast.LENGTH_SHORT)
						.show();
				
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		MenuInflater mn = new MenuInflater(this);
		mn.inflate(R.menu.menu1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item1) {
		// TODO Auto-generated method stub

		switch (item1.getItemId()) {
		case R.id.setbackground:
			byte [] by1 = foreground.getData();
			bgimage = BitmapFactory.decodeByteArray(by1, 0, by1.length);
			Toast.makeText(getApplicationContext(), "set successfully",
					Toast.LENGTH_LONG).show();
			try {
				getApplicationContext().setWallpaper(bgimage);
			
			} catch (IOException e) { // TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
		return false;
	

}}
