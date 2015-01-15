package com.nativeappsexplored.zahavs.camera;

import java.io.ByteArrayOutputStream;

import com.nativeappsexplored.zahavs.HistoryActivity;
import com.nativeappsexplored.zahavs.MainActivity;
import com.nativeappsexplored.zahavs.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CameraActivity extends Activity {
	public static final String PREFS_NAME = MainActivity.PREFS_NAME;
	private String textEncode;
	String TAG = "CameraActivity";

	private Camera mCamera;
	private CameraPreview mPreview;
	private PictureCallback mPicture;
	
	private int brcd_counter;
	
	//private Context myContext;

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initialize();
	}

	private int findBackFacingCamera() {
		int cameraId = -1;
		// Search for the back facing camera
		// get the number of cameras
		int numberOfCameras = Camera.getNumberOfCameras();
		// for every camera check
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
				cameraId = i;

				break;
			}
		}
		return cameraId;
	}

	public void onResume() {
		super.onResume();
		Context myContext = this.getApplicationContext();
		if (!hasCamera(myContext)) {
			Toast toast = Toast.makeText(myContext,
					"Sorry, your phone does not have a camera!",
					Toast.LENGTH_LONG);
			toast.show();
			finish();
		}
		if (mCamera == null) {

			mCamera = Camera.open(findBackFacingCamera());
			mCamera.setDisplayOrientation(90);
			mPicture = getPictureCallback();
			mPreview.refreshCamera(mCamera);
		}
	}

	public void initialize() {
		LinearLayout cameraPreview = (LinearLayout) findViewById(R.id.camera_preview);
		Context myContext = this.getApplicationContext();
		mPreview = new CameraPreview(myContext, mCamera);
		cameraPreview.addView(mPreview);

		Button capture = (Button) findViewById(R.id.button_capture);
		capture.setOnClickListener(captrureListener);

		Button approve = (Button) findViewById(R.id.aprrove);
		approve.setOnClickListener(approveListener);

	}

	OnClickListener approveListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			// releaseCamera();

			Intent intent = new Intent(getApplicationContext(),
					HistoryActivity.class);

			if (textEncode != null) {
				startActivity(intent);
			}

		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		// when on Pause, release camera in order to be used from other
		// applications
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		brcd_counter = settings.getInt("brcd_counter", 0);
		if (textEncode != null) {
			editor.putString("photo" + brcd_counter, textEncode);
		}
		editor.commit();

		Log.d("prefernces", settings.getAll().toString());

		ImageView v = (ImageView) findViewById(R.id.imageView1);
		v.refreshDrawableState();

//		bitmap.recycle();
//		bitmap = null;

		releaseCamera();
//		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
//		    // Running on something older than API level 11, so disable
//		    // the drag/drop features that use ClipboardManager APIs
//			onTrimMemory(TRIM_MEMORY_UI_HIDDEN);
//		}
		
		finish();
	}

	private boolean hasCamera(Context context) {
		// check if the device has camera
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	private PictureCallback getPictureCallback() {
		PictureCallback picture = new PictureCallback() {

			@Override
			public void onPictureTaken(final byte[] data, Camera camera) {

				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize=8;      // 1/8 of original image
				Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
				
				if (bitmap == null) {
					Toast.makeText(getApplicationContext(), "not taken",
							Toast.LENGTH_SHORT).show();
				} else {

					ImageView v = (ImageView) findViewById(R.id.imageView1);
					v.setDrawingCacheEnabled(true);
					v.setImageBitmap(bitmap);
				}

				Runnable runnable = new Runnable() {
					public void run() {
						Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
								data.length);
						if (bitmap == null) {
							Toast.makeText(getApplicationContext(),
									"not taken", Toast.LENGTH_SHORT).show();
						} else {

							// Bitmap realImage = bitmap;
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							bitmap.compress(Bitmap.CompressFormat.JPEG, 50,
									baos);
							byte[] b = baos.toByteArray();

							String encodedImage = Base64.encodeToString(b,
									Base64.DEFAULT);

							textEncode = encodedImage;

							bitmap.recycle();
							bitmap = null;

							// SharedPreferences shre = PreferenceManager
							// .getDefaultSharedPreferences(getApplicationContext());
							// Editor edit = shre.edit();
							// edit.putString("image_data", encodedImage);
							// edit.commit();

						}

					}

					
				};

				Thread mythread = new Thread(runnable);
				mythread.start();

				// refresh camera to continue preview
				mPreview.refreshCamera(mCamera);
			}
		};
		return picture;
	}

	OnClickListener captrureListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// if (mCamera == null) {
			//
			// mCamera = Camera.open(findBackFacingCamera());
			// mCamera.setDisplayOrientation(90);
			// mPicture = getPictureCallback();
			// mPreview.refreshCamera(mCamera);
			// }
			mCamera.takePicture(null, null, mPicture);
		}
	};

	private void releaseCamera() {
		// stop and release camera
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}

}