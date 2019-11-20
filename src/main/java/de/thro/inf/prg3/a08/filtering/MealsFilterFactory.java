package de.thro.inf.prg3.a08.filtering;

import java.util.HashMap;
import java.util.Map;

public abstract class MealsFilterFactory
{
	private static Map<String, MealsFilter> filter = new HashMap<>();

	static
	{
		filter.put("All",        new AllMealsStrategy());
		filter.put("Vegetarian", new VegetarianStrategy());
		filter.put("No pork",    new NoPorkStrategy());
		filter.put("No soy",     new NoSoyStrategy());
	}

	public static MealsFilter getStrategy(String key)
	{
		return filter.get(key);
	}
}
