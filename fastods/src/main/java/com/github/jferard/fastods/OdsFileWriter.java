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

package com.github.jferard.fastods;

import java.io.IOException;

/**
 * An OdsFileWriter may be a direct writer or an adapter for a writer thread
 * @author Julien Férard
 * @author Martin Schulz
 */
public interface OdsFileWriter {
	void close() throws IOException;

	OdsDocument document();


	/**
	 * Save the new file.
	 *
	 * @throws IOException If an I/O error occurs
	 */
	void save() throws IOException;

	/**
	 * Update the writer with a flusher
	 * @param flusher the chosen flusher
	 * @throws IOException
	 */
	void update(final OdsFlusher flusher) throws IOException;
}