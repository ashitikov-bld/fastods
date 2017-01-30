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

import com.github.jferard.fastods.style.Margins;
import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;

/**
 * @author Julien Férard
 * @author Martin Schulz
 */
class FooterHeaderStyle {
	private final FooterHeader.Type footerHeaderType;
	private final Margins margins;
	private final String minHeight;

	/**
	 * Create a new footer object.
	 *
	 * @param minHeight the height of the footer/header
	 */
	FooterHeaderStyle(final FooterHeader.Type footerHeaderType,
					  final Margins margins, final String minHeight) {
		this.footerHeaderType = footerHeaderType;
		this.margins = margins;
		this.minHeight = minHeight;
	}

	public static void appendFooterHeaderStyleXMLToAutomaticStyle(final FooterHeaderStyle footerHeader,
																  final FooterHeader.Type type, final XMLUtil util,
																  final Appendable appendable) throws IOException {
		if (footerHeader == null)
			appendable.append("</style:").append(type.getTypeName())
					.append("-style />");
		else
			footerHeader.appendFooterHeaderStyleXMLToAutomaticStyle(util,
					appendable);
	}

	public void appendFooterHeaderStyleXMLToAutomaticStyle(final XMLUtil util,
														   final Appendable appendable) throws IOException {
		appendable.append("<style:").append(this.footerHeaderType.getTypeName())
				.append("-style>");
		appendable.append("<style:header-footer-properties");
		util.appendAttribute(appendable, "fo:min-height", this.minHeight);
		this.margins.appendXMLToTableCellStyle(util, appendable);
		appendable.append("/></style:").append(this.footerHeaderType.getTypeName())
				.append("-style>");
	}

	/**
	 * @return The current margins of the footer/header.
	 */
	public Margins getMargins() {
		return this.margins;
	}

	/**
	 * @return The current minimum height of the footer/header.
	 */
	public String getMinHeight() {
		return this.minHeight;
	}

	public String getTypeName() {
		return this.footerHeaderType.getTypeName();
	}
}