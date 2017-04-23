/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2017 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FastODS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jferard.fastods.datastyle;

import java.util.Locale;

/**
 * @author Julien Férard
 */
public class FractionStyleBuilder {
	private final NumberStyleBuilder numberStyleBuilder;
	private int minDenominatorDigits;
	private int minNumeratorDigits;

	/**
	 * Create a new number style with the name name, minimum integer digits is
	 * minIntDigits and decimal places is decPlaces.
	 *
	 * @param name   The name of the number style, this name must be unique.
	 * @param locale The locale used
	 */
	FractionStyleBuilder(final String name, final Locale locale) {
		this.numberStyleBuilder = new NumberStyleBuilder(name, locale);
		this.minNumeratorDigits = 0;
		this.minDenominatorDigits = 0;
	}

	public FractionStyle build() {
		return new FractionStyle(this.numberStyleBuilder.build(), this.minNumeratorDigits,
				this.minDenominatorDigits);
	}

	/**
	 * Add the numerator and denominator values to be shown.<br>
	 * The number style is set to NUMBER_FRACTION
	 *
	 * @param numerator   the number of digits for the numerator
	 * @param denominator the number of digits for the denominator
	 * @return this for fluent style
	 */
	public FractionStyleBuilder fractionValues(final int numerator,
											   final int denominator) {
		this.minNumeratorDigits = numerator;
		this.minDenominatorDigits = denominator;
		return this;
	}

	public FractionStyleBuilder groupThousands(final boolean grouping) {
		this.numberStyleBuilder.groupThousands(grouping);
		return this;
	}

	public FractionStyleBuilder minIntegerDigits(final int minIntegerDigits) {
		this.numberStyleBuilder.minIntegerDigits(minIntegerDigits);
		return this;
	}

	public FractionStyleBuilder negativeValueColor(final String negativeValueColor) {
		this.numberStyleBuilder.negativeValueColor(negativeValueColor);
		return this;
	}

	public FractionStyleBuilder negativeValueRed() {
		this.numberStyleBuilder.negativeValueRed();
		return this;
	}

	public FractionStyleBuilder country(final String countryCode) {
		this.numberStyleBuilder.country(countryCode);
		return this;
	}

	public FractionStyleBuilder language(final String languageCode) {
		this.numberStyleBuilder.language(languageCode);
		return this;
	}

	public FractionStyleBuilder locale(final Locale locale) {
		this.numberStyleBuilder.locale(locale);
		return this;
	}

	public FractionStyleBuilder volatileStyle(final boolean volatileStyle) {
		this.numberStyleBuilder.volatileStyle(volatileStyle);
		return this;
	}
}
