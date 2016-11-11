package com.coursera.capstore.impatient.client;

import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class HttpClientImpatient {

	public static final String CLOUD_IMPATIENT_URL = "http://192.168.1.10:8080/impatient/";

	private static OkHttpClient httpClient = new OkHttpClient();
	private static Retrofit.Builder builder = new Retrofit.Builder();

	public static <S> S createClient(Class<S> serviceClass) {
		builder.baseUrl(CLOUD_IMPATIENT_URL);
		builder.addConverterFactory(GsonConverterFactory.create());		
		Retrofit retrofit = builder.client(httpClient).build();
		
		return retrofit.create(serviceClass);
	}
}
