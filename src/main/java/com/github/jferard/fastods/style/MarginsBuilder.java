/*******************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FastODS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.jferard.fastods.style;

public class MarginsBuilder {
	private String top;
	private String right;
	private String bottom;
	private String left;
	
	public MarginsBuilder top(String top) {
		this.top = top;
		return this;
	}
	
	public MarginsBuilder right(String right) {
		this.right = right;
		return this;
	}
	
	public MarginsBuilder bottom(String bottom) {
		this.bottom = bottom;
		return this;
	}
	
	public MarginsBuilder left(String left) {
		this.left = left;
		return this;
	}
	
	public MarginsBuilder all(String size) {
		this.top = size;
		this.right = size;
		this.bottom = size;
		this.left = size;
		return this;
	}
	
	
	public Margins build() {
		return new Margins(this.top, this.right, this.bottom, this.left);
	}
}
