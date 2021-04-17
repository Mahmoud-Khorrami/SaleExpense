package com.example.boroodat.general;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class AppController extends Application
{

	public static final String TAG = AppController.class.getSimpleName();

	private RequestQueue mRequestQueue;

	private static AppController mInstance;

	@Override
	public void onCreate()
	{
		super.onCreate();
		mInstance = this;

		Realm.init(this);

		final RealmConfiguration configuration = new RealmConfiguration.Builder()
				.name("sample.realm")
				.schemaVersion(0)
				.migration(new Migration())
				.build();
		Realm.setDefaultConfiguration(configuration);
		Realm.getInstance(configuration);
	}

	@Override
	public void onTerminate() {
		Realm.getDefaultInstance().close();
		super.onTerminate();
	}
	public static synchronized AppController getInstance()
	{
		return mInstance;
	}

	public RequestQueue getRequestQueue()
	{
		if (mRequestQueue == null)
		{
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag)
	{
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req)
	{
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag)
	{
		if (mRequestQueue != null)
		{
			mRequestQueue.cancelAll(tag);
		}
	}

	public void cancelPendingRequests()
	{
		if (mRequestQueue != null)
		{
			mRequestQueue.cancelAll(TAG);
		}
	}

}