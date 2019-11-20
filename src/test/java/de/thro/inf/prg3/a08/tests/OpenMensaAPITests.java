package de.thro.inf.prg3.a08.tests;

import de.thro.inf.prg3.a08.api.OpenMensaAPI;
import de.thro.inf.prg3.a08.model.Meal;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OpenMensaAPITests
{

	private static final Logger logger = LogManager.getLogger(OpenMensaAPITests.class);
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

	private OpenMensaAPI api;

	@BeforeEach
	void setUp()
	{

		// use this to intercept all requests and output them to the logging facilities
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		/* create a OkHttpClient and register the interceptor */
		OkHttpClient client = new OkHttpClient.Builder()
			.addInterceptor(interceptor)
			.build();

		Retrofit retrofit = new Retrofit.Builder()
			.addConverterFactory(GsonConverterFactory.create())
			.baseUrl("http://openmensa.org/api/v2/")
			.client(client)
			.build();

		/* retrieve a proxy object for the OpenMensaAPI interface */
		api = retrofit.create(OpenMensaAPI.class);
	}

	@Test
	void testGetMeals() throws IOException
	{
		/* create a call to get all meals of the current day */
		Call<List<Meal>> mealsCall = api.getMeals(dateFormat.format(new Date()));

		/* execute the call synchronous */
		Response<List<Meal>> mealsResponse = mealsCall.execute();

		/* unwrap the response */
		List<Meal> meals = mealsResponse.body();

		assertNotNull(meals);
		assertNotEquals(0, meals.size());

		/* display the results in the log of the test */
		for (Meal m : meals)
		{
			logger.info(m.toString());
		}
	}
}
