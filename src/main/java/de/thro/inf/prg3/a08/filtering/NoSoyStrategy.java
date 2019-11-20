package de.thro.inf.prg3.a08.filtering;

import de.thro.inf.prg3.a08.model.Meal;

public class NoSoyStrategy extends FilterBase
{
	@Override
	protected boolean include(Meal m)
	{
		return !m.toString().toLowerCase().contains("soja");
	}
}
