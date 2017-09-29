package com.graction.developer.lumi.Net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Net {
	private static final Net instance = new Net();
//	private static final String BASE_URL = "http://10.0.2.2:8101/lumi/";
	private static final String BASE_URL = "http://1.241.0.30:8101/lumi/";
	private NetFactoryIm netFactoryIm;
	private Retrofit retrofit;
	
	{
		retrofit = new Retrofit.Builder()
				.baseUrl(BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
	}

	public static Net getInstance() {
		return instance;
	}

	public NetFactoryIm getFactoryIm() {
		if (netFactoryIm == null)
			netFactoryIm = retrofit.create(NetFactoryIm.class);
		return netFactoryIm;
	}
}