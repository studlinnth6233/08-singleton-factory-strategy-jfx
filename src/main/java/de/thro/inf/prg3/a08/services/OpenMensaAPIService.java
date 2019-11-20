package de.thro.inf.prg3.a08.services;

import de.thro.inf.prg3.a08.api.OpenMensaAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenMensaAPIService
{
	private static OpenMensaAPIService instance = null;
	private        OpenMensaAPI        apiInstance;

	private OpenMensaAPIService()
	{
		Retrofit retrofit = new Retrofit.Builder()
			.addConverterFactory(GsonConverterFactory.create())
			.baseUrl("http://openmensa.org/api/v2/")
			.build();

		this.apiInstance = retrofit.create(OpenMensaAPI.class);
	}

	public static OpenMensaAPIService instance()
	{
		if (instance == null) instance = new OpenMensaAPIService();

		return instance;
	}

	public OpenMensaAPI getAPI()
	{
		return this.apiInstance;
	}
}
