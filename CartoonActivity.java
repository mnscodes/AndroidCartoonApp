package com.cartoon;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.graphics.drawable.BitmapDrawable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CartoonActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	/** Called when the activity is first created. */
	LayoutInflater inflator;
	Bitmap bmcanvas, bmuse;
	static Bitmap bmcamera;
	LinearLayout linear, main;
	Button new1, open, saveas, edit, setbg, share, sendemail, exit;
	View view1, view2, view3, view4;
	ImageButton newview, camera, gallery, sdcard;
	private final int camra = 1;
	private final int gal = 107;
	boolean a = false;
	boolean b = false;
	boolean c = false;
	boolean music,doback=false;
	private GridView sdcardImages;
	private ImageAdapter imageAdapter;
	private Display display;
	TextView tv;
	SharedPreferences sprefs;
	String Values, value,imagePath;
	MediaPlayer player;
	File [] imageList;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
   tv = (TextView)findViewById(R.id.dateformat);
		new1 = (Button) findViewById(R.id.new1);
		open = (Button) findViewById(R.id.open);

		edit = (Button) findViewById(R.id.edit);
		setbg = (Button) findViewById(R.id.setbackground);
		sendemail = (Button) findViewById(R.id.sendemail);
		
		exit = (Button) findViewById(R.id.exit);
		new1.setOnClickListener(this);
		open.setOnClickListener(this);
		
		edit.setOnClickListener(this);
		setbg.setOnClickListener(this);
		sendemail.setOnClickListener(this);
		
		exit.setOnClickListener(this);
		linear = (LinearLayout) findViewById(R.id.ll1);
		main = (LinearLayout) findViewById(R.id.maillayout);
		  

		linear.setBackgroundColor(Color.TRANSPARENT);
		bmuse = BitmapFactory.decodeResource(getResources(), R.drawable.move);
		sprefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = df.format(c.getTime());
		tv.setTextSize(18);
            tv.setText(formattedDate);
            
           
            player= MediaPlayer.create(this, R.raw.background);
            
           
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.new1:
			linear.removeAllViews();
			view1 = getLayoutInflater().inflate(R.layout.new1, linear, false);
			linear.addView(view1);
			if (view1 != null) {

				newview = (ImageButton) view1.findViewById(R.id.newview1);
				newview.setOnClickListener(CartoonActivity.this);

			}

			break;
		case R.id.open:
			linear.removeAllViews();
			view2 = getLayoutInflater().inflate(R.layout.open, linear, false);
			linear.addView(view2);
			if (view2 != null) {

				camera = (ImageButton) view2.findViewById(R.id.camera);
				camera.setOnClickListener(CartoonActivity.this);
				gallery = (ImageButton) view2.findViewById(R.id.gallery);
				gallery.setOnClickListener(CartoonActivity.this);
				
			}

			break;
		case R.id.setbackground:
			linear.removeAllViews();
			a = true;
			
			view4 = getLayoutInflater().inflate(R.layout.gridview, linear,
					false);
			linear.addView(view4);
			setupViews();
			loadImages();
			setProgressBarIndeterminateVisibility(true);

			break;

		case R.id.camera:

			Intent spenjump = new Intent(CartoonActivity.this, spen.class);

			Intent cam = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivity(spenjump);
			startActivityForResult(cam, camra);
			break;

		case R.id.gallery:
			Intent galry = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			
			startActivityForResult(galry, gal);

		
			break;
		
		case R.id.newview1:
			AlertDialog.Builder alert = new AlertDialog.Builder(CartoonActivity.this);
			alert.setTitle("Enter the name of file");  
			alert.setMessage("Enter name :");  
		

			 // Set an EditText view to get user input   
			 final EditText input = new EditText(this); 
			 alert.setView(input);
			 
			 alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
				    public void onClick(DialogInterface dialog, int whichButton) {  
				         value = input.getText().toString();
				    	Intent newview = new Intent(CartoonActivity.this, spen.class);
				    	newview.putExtra("filename",value);
							startActivity(newview);
				        return;                  
				       }  
				     });  
			 alert.show();

		//	Intent newview = new Intent(CartoonActivity.this, spen.class);
		//	startActivity(newview);
			break;
		
		case R.id.sendemail:
			linear.removeAllViews();
			c = true;
			view4 = getLayoutInflater().inflate(R.layout.gridview, linear,
					false);
			linear.addView(view4);
			setupViews();
			loadImages();
			setProgressBarIndeterminateVisibility(true);

			break;

		case R.id.edit:
			linear.removeAllViews();
			b = true;
			view4 = getLayoutInflater().inflate(R.layout.gridview, linear,
					false);
			linear.addView(view4);
			setupViews();
			loadImages();
			setProgressBarIndeterminateVisibility(true);

			break;
		case R.id.exit:
			finish();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == gal) {
			Uri uri = data.getData();

			try {
				bmcanvas = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(uri));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (bmcanvas != null) {
				Intent galleryspen = new Intent(CartoonActivity.this,
						spen.class);
				ByteArrayOutputStream bs2 = new ByteArrayOutputStream();
				bmcanvas.compress(Bitmap.CompressFormat.PNG, 100, bs2);
				galleryspen.putExtra("bytegallery", bs2.toByteArray());
				startActivity(galleryspen);
				
			}

		}

		else if (requestCode == camra) {

			bmcamera = (Bitmap) data.getExtras().get("data");
			if (bmcamera != null) {
				Intent cameraspen = new Intent(CartoonActivity.this, spen.class);
				ByteArrayOutputStream bs1 = new ByteArrayOutputStream();
				bmcamera.compress(Bitmap.CompressFormat.PNG, 100, bs1);
				cameraspen.putExtra("bytecamera", bs1.toByteArray());
				startActivity(cameraspen);

			}
		}
	}

	private void setupViews() {
		sdcardImages = (GridView) findViewById(R.id.myGrid);

		sdcardImages.setClipToPadding(false);
		sdcardImages.setOnItemClickListener(CartoonActivity.this);
		imageAdapter = new ImageAdapter(getApplicationContext());
		sdcardImages.setAdapter(imageAdapter);
	}

	private void loadImages() {
		final Object data = getLastNonConfigurationInstance();
		if (data == null) {
			new LoadImagesFromSDCard().execute();
		} else {
			final LoadedImage[] photos = (LoadedImage[]) data;
			if (photos.length == 0) {
				new LoadImagesFromSDCard().execute();
			}
			for (LoadedImage photo : photos) {
				addImage(photo);
			}
		}
	}

	private void addImage(LoadedImage... value) {
		for (LoadedImage image : value) {
			imageAdapter.addPhoto(image);
			imageAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		// TODO Auto-generated method stub
		final GridView grid = sdcardImages;
		final int count = grid.getChildCount();
		final LoadedImage[] list = new LoadedImage[count];

		for (int i = 0; i < count; i++) {
			final ImageView v = (ImageView) grid.getChildAt(i);
			v.getLayoutParams().height = 50;
			v.getLayoutParams().width = 50;

			list[i] = new LoadedImage(
					((BitmapDrawable) v.getDrawable()).getBitmap());
		}

		return list;
	}

	class LoadImagesFromSDCard extends AsyncTask<Object, LoadedImage, Object> {

		/**
		 * Load images from SD Card in the background, and display each image on
		 * the screen.
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		protected Object doInBackground(Object... params) {
			// setProgressBarIndeterminateVisibility(true);
			Bitmap bitmap = null;
			Bitmap newBitmap = null;
			Uri uri = null;
			// String condition = uri + " like '%/bluetooth/%'";
			// Set up an array of the Thumbnail Image ID column we want
			String[] projection = { MediaStore.Images.Media._ID };
			// Create the cursor pointing to the SDCard
			Cursor cursor = managedQuery(
					MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
					projection, // Which columns to return
					null,
			        null,  
					 null);
			int columnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
			int size = cursor.getCount();
		//	File imagesDir = new File(Environment.getExternalStorageDirectory().toString() + "/chintu");
		//	imageList = imagesDir.listFiles(); 
			// If size is 0, there are no images on the SD Card.
			if (size == 0) {
				// No Images available, post some message to the user
			}
			int imageID = 0;
			for (int i = 0; i < size; i++) {
				cursor.moveToPosition(i);
				imageID = cursor.getInt(columnIndex);
				uri = Uri.withAppendedPath(
						MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, ""
								+ imageID);
				try {
					bitmap = BitmapFactory.decodeStream(getContentResolver()
							.openInputStream(uri));
					if (bitmap != null) {
						newBitmap = Bitmap.createScaledBitmap(bitmap, 70, 70,
								true);
						bitmap.recycle();
						if (newBitmap != null) {
							publishProgress(new LoadedImage(newBitmap));
							
						}
					}
				} catch (IOException e) {
					// Error fetching image, try to recover
				}
			}
			cursor.moveToPosition(0);
			cursor.close();
			return null;
		}

		@Override
		public void onProgressUpdate(LoadedImage... value) {
			addImage(value);
		}

		protected void onPostExecute(Object result) {
			setProgressBarIndeterminateVisibility(false);
		}
	}

	class ImageAdapter extends BaseAdapter {

		private Context mContext;
		private ArrayList<LoadedImage> photos = new ArrayList<LoadedImage>();

		public ImageAdapter(Context context) {
			mContext = context;
		}

		public void addPhoto(LoadedImage photo) {
			photos.add(photo);
		}

		public int getCount() {
			return photos.size();
		}

		public Object getItem(int position) {
			return photos.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(mContext);
			} else {
				imageView = (ImageView) convertView;
			}
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			imageView.setPadding(8, 8, 8, 8);
			imageView.setLayoutParams(new GridView.LayoutParams(150	,120));
			imageView.setMaxHeight(40);
		    imageView.setImageBitmap(photos.get(position).getBitmap());
			
		    return imageView;
		}
			
	}
	
	/**
	 * A LoadedImage contains the Bitmap loaded for the image.
	 */
	private static class LoadedImage {
		Bitmap mBitmap;

		LoadedImage(Bitmap bitmap) {
			mBitmap = bitmap;
		}

		public Bitmap getBitmap() {
			return mBitmap;
		}
	}

	/**
	 * When an image is clicked, load that image as a puzzle.
	 */
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		int columnIndex = 0;
		String[] projection = { MediaStore.Images.Media._ID };
		Cursor cursor = managedQuery(
				MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection,
				null, null, null);
		cursor.moveToPosition(position);
		if (cursor != null) {
			
			columnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
		
		   String imagePath = cursor.getString(columnIndex); 


			FileInputStream is = null;
			BufferedInputStream bis = null;
			try {
				is = new FileInputStream(new File(imagePath));
				bis = new BufferedInputStream(is);
				Bitmap bitmap = BitmapFactory.decodeStream(bis);
				Bitmap useThisBitmap = Bitmap.createScaledBitmap(bitmap,
						parent.getWidth(), parent.getHeight(), true);
				bitmap.recycle();

				if (b == true) {

					Intent intent4 = new Intent(CartoonActivity.this,
							spen.class);

					ByteArrayOutputStream bs = new ByteArrayOutputStream();
					useThisBitmap.compress(Bitmap.CompressFormat.PNG, 100, bs);
					intent4.putExtra("byteArray", bs.toByteArray());
					b = false;
					startActivity(intent4);

				} else if (c == true) {
					String path = Images.Media.insertImage(
							getContentResolver(), useThisBitmap, "title", null);
					Uri screenuri = Uri.parse(path);
					Intent emailintent = new Intent(
							android.content.Intent.ACTION_SEND);
					emailintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					emailintent.putExtra(Intent.EXTRA_STREAM, screenuri);
					emailintent.setType("image/png");
					c = false;
					startActivity(emailintent);

				} else if (a == true) {
					Toast.makeText(getApplicationContext(), "set successfully",
							Toast.LENGTH_LONG).show();
					try {
						getApplicationContext().setWallpaper(useThisBitmap);
						a = false;
					} catch (IOException e) { // TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// Display bitmap (useThisBitmap)

			} catch (Exception e) {
				// Try to recover
			} finally {
				try {
					if (bis != null) {
						bis.close();
					}
					if (is != null) {
						is.close();
					}
					cursor.close();
					projection = null;
				} catch (Exception e) {
				}
			}
		}
	}

	protected void onDestroy() {
		super.onDestroy();
		if(a==true || b==true || c== true){
		final GridView grid = sdcardImages;
		final int count = grid.getChildCount();
		ImageView v = null;
		for (int i = 0; i < count; i++) {
			v = (ImageView) grid.getChildAt(i);
			((BitmapDrawable) v.getDrawable()).setCallback(null);
		}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		MenuInflater mn = new MenuInflater(this);
		mn.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case R.id.settings:
			Intent intent5 = new Intent(CartoonActivity.this, prefs.class);
			startActivity(intent5);
			break;
		case R.id.help:
			Intent intent6 = new Intent(CartoonActivity.this, help.class);
			startActivity(intent6);
			break;
			
		}
		return false;
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		 Values = sprefs.getString("list", "0");
		
		 music= sprefs.getBoolean("checkmusic", true);
		
		if (Values.contentEquals("1")) {
			main.setBackgroundResource(R.drawable.brown);
			linear.setBackgroundResource(R.drawable.background2);
		} else if (Values.contentEquals("2")) {
			main.setBackgroundResource(R.drawable.red);
			linear.setBackgroundResource(R.drawable.red2);
		
		} else if (Values.contentEquals("3")) {
			main.setBackgroundResource(R.drawable.blue);
			linear.setBackgroundResource(R.drawable.blue2);
		}
		if(music == true){
			player.setLooping(true);
			player.setVolume(50, 50);

			player.start();
			
		}
		else if(music== false){
			player.stop();
		}
		
	}
	
}
