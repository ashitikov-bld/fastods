/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2019 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 9.5.6<table:filter-set-item>
 * @author J. Férard
 *
 * TODO: See compatibility note in 9.5.5<table:filter-condition>
 */
public class FilterEnumerate implements Filter {
    private final int colIndex;
    private final List<String> values;

    /**
     * @param colIndex the index
     * @param values the values
     */
    public FilterEnumerate(final int colIndex, final String... values) {
        this.colIndex = colIndex;
        this.values = Arrays.asList(values);
    }

    @Override
    public void appendXMLContent(final XMLUtil util, final Appendable appendable)
            throws IOException {
        appendable.append("<table:filter-condition");
        util.appendAttribute(appendable, "table:field-number", this.colIndex);
        appendable.append(">");
        for (final String value : this.values) {
            appendable.append("<table:filter-set-item");
            util.appendAttribute(appendable, "table:value", value);
            appendable.append("/>");
        }
        appendable.append("</table:filter-condition>");
    }
}
