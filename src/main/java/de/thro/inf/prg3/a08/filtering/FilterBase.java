package de.thro.inf.prg3.a08.filtering;

import de.thro.inf.prg3.a08.model.Meal;

import java.util.ArrayList;
import java.util.List;

public abstract class FilterBase implements MealsFilter
{
	@Override
	public List<Meal> filter(List<Meal> meals)
	{
		List<Meal> filtered;

		try
		{
			filtered = meals.getClass().newInstance();
		}

		catch (InstantiationException | IllegalAccessException e)
		{
			filtered = new ArrayList<>();
		}

		meals.stream().filter(this::include).forEach(filtered::add);

		return filtered;
	}

	protected abstract boolean include(Meal m);
}
