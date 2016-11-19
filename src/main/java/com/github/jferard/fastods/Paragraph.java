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
/*
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
*    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.jferard.fastods;

import java.io.IOException;
import java.util.List;

import com.github.jferard.fastods.util.FullList;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard
 */
public class Paragraph {
	private final List<Span> texts;

	Paragraph() {
		this.texts = FullList.<Span> builder().capacity(16).build();
	}

	public void add(final Span span) {
		this.texts.add(span);
	}

	public void add(final String content) {
		this.texts.add(new Span(content));
	}

	public void appendXMLToRegionBody(final XMLUtil util,
			final Appendable appendable) throws IOException {
		switch (this.texts.size()) {
		case 0:
			appendable.append("<text:p/>");
			break;
		case 1:
			final Span text = this.texts.get(0);
			appendable.append("<text:p>");
			text.appendXMLOptionalSpanToParagraph(util, appendable);
			appendable.append("</text:p>");
			break;
		default:
			appendable.append("<text:p>");
			for (final Span textChunk : this.texts)
				textChunk.appendXMLOptionalSpanToParagraph(util, appendable);
			appendable.append("</text:p>");
			break;
		}
	}

	public List<Span> getTexts() {
		return this.texts;
	}

}