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

package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.UniqueList;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;

/**
 * content.xml/office:document-content
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
public class ContentElement implements OdsElement {
	private final FlushPosition flushPosition;
	private final DataStyles format;
	private final PositionUtil positionUtil;
	private final StylesContainer stylesContainer;
	private final UniqueList<Table> tables;
	private final WriteUtil writeUtil;
	private final XMLUtil xmlUtil;
	private List<String> autofilters;

	ContentElement(final PositionUtil positionUtil, final XMLUtil xmlUtil,
				   final WriteUtil writeUtil,
				   final DataStyles format,
				   final StylesContainer stylesContainer) {
		this.writeUtil = writeUtil;
		this.xmlUtil = xmlUtil;
		this.positionUtil = positionUtil;
		this.format = format;
		this.stylesContainer = stylesContainer;
		this.tables = new UniqueList<Table>();
		this.flushPosition = new FlushPosition();
	}

	public void addChildCellStyle(final TableCellStyle style, final TableCell.Type type) {
		this.stylesContainer.addChildCellStyle(style, this.format.getDataStyle(type));
	}

	/**
	 * @param name           the name of the table to create
	 * @param columnCapacity the initial capacity in columns: this will be allocated at table creation
	 * @param rowCapacity    the initial capacity in rows: this will be allocated at table creation
	 * @return the table (whether it existed before call or not). Never null
	 */
	public Table addTable(final String name, final int rowCapacity,
						  final int columnCapacity) {
		Table table = this.tables.getByName(name);
		if (table == null) {
			table = new Table(this.positionUtil, this.writeUtil, this.xmlUtil,
					this.stylesContainer, this.format, name,
					rowCapacity, columnCapacity);
			this.tables.add(table);
		}
		return table;
	}

	private void ensureContentBegin(final XMLUtil util, final ZipUTF8Writer writer) throws IOException {
		if (this.flushPosition.isUndefined()) {
			this.writePreamble(util, writer);
			this.flushPosition.set(0, -1);
		}
	}

	public void flushRows(final XMLUtil util, final ZipUTF8Writer writer, final SettingsElement settingsElement)
			throws IOException {
		this.ensureContentBegin(util, writer);
		final int lastTableIndex = this.tables.size() - 1;
		int tableIndex = this.flushPosition.getTableIndex();
		Table table = this.tables.get(tableIndex);
		if (tableIndex < lastTableIndex) {
			System.out.println("Moving from table" + tableIndex + " to table " + lastTableIndex);
			table.flushRemainingRowsFrom(util, writer, this.flushPosition.getLastRowIndex() + 1);
			settingsElement.addTableConfig(table.getConfigEntry());
			tableIndex++;
			while (tableIndex < lastTableIndex) {
				table = this.tables.get(tableIndex);
				table.appendXMLToContentEntry(util, writer);
				settingsElement.addTableConfig(table.getConfigEntry());
				tableIndex++;
			}
			table = this.tables.get(lastTableIndex);
			table.flushAllAvailableRows(util, writer);
		} else {
			table.flushSomeAvailableRowsFrom(util, writer, this.flushPosition.getLastRowIndex() + 1);
		}
		this.flushPosition.set(lastTableIndex, this.tables.get(lastTableIndex).getLastRowNumber());
	}

	public void flushTables(final XMLUtil util, final ZipUTF8Writer writer)
			throws IOException {
		this.ensureContentBegin(util, writer);
		final int lastTableIndex = this.tables.size() - 1;
		int tableIndex = this.flushPosition.getTableIndex();

		Table table = this.tables.get(tableIndex);
		table.flushRemainingRowsFrom(util, writer, this.flushPosition.getLastRowIndex() + 1);
		tableIndex++;
		while (tableIndex <= lastTableIndex) {
			table = this.tables.get(tableIndex);
			table.appendXMLToContentEntry(util, writer);
			tableIndex++;
		}
	}

	public Table getLastTable() {
		final int size = this.tables.size();
		return size <= 0 ? null : this.tables.get(size -1);
	}

	public StylesContainer getStyleTagsContainer() {
		return this.stylesContainer;
	}

	public Table getTable(final int tableIndex) {
		return this.tables.get(tableIndex);
	}

	/**
	 * @param name the name of the table to find
	 * @return the table, or null if none present
	 */
	public Table getTable(final String name) {
		return this.tables.getByName(name);
	}

	public int getTableCount() {
		return this.tables.size();
	}

	/**
	 * @return the list of tables
	 */
	public List<Table> getTables() {
		return this.tables;
	}

	@Override
	public void write(final XMLUtil util, final ZipUTF8Writer writer)
			throws IOException {
		this.writePreamble(util, writer);
		for (final Table table : this.tables)
			table.appendXMLToContentEntry(util, writer);
		this.writePostamble(util, writer);
	}

	public void writePostamble(final XMLUtil util, final ZipUTF8Writer writer) throws IOException {
		if (this.autofilters != null)
			this.appendAutofilters(writer, util);
		writer.write("</office:spreadsheet>");
		writer.write("</office:body>");
		writer.write("</office:document-content>");
		writer.flush();
		writer.closeEntry();
	}

	public void writePreamble(final XMLUtil util, final ZipUTF8Writer writer) throws IOException {
		writer.putNextEntry(new ZipEntry("content.xml"));
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.write(
				"<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" " +
						"xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\" " +
						"xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" " +
						"xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" " +
						"xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\" " +
						"xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\" " +
						"xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" " +
						"xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" " +
						"xmlns:number=\"urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0\" " +
						"xmlns:presentation=\"urn:oasis:names:tc:opendocument:xmlns:presentation:1.0\" " +
						"xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\" " +
						"xmlns:chart=\"urn:oasis:names:tc:opendocument:xmlns:chart:1.0\" " +
						"xmlns:dr3d=\"urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0\" xmlns:math=\"http://www" +
						".w3.org/1998/Math/MathML\" xmlns:form=\"urn:oasis:names:tc:opendocument:xmlns:form:1.0\" " +
						"xmlns:script=\"urn:oasis:names:tc:opendocument:xmlns:script:1.0\" " +
						"xmlns:ooo=\"http://openoffice.org/2004/office\" xmlns:ooow=\"http://openoffice" +
						".org/2004/writer\" xmlns:oooc=\"http://openoffice.org/2004/calc\" xmlns:dom=\"http://www" +
						".w3.org/2001/xml-events\" xmlns:xforms=\"http://www.w3.org/2002/xforms\" " +
						"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www" +
						".w3.org/2001/XMLSchema-instance\" office:version=\"1.1\">");
		writer.write("<office:scripts/>");
		writer.write("<office:font-face-decls>");
		writer.write(
				"<style:font-face style:name=\"Arial\" svg:font-family=\"Arial\" style:font-family-generic=\"swiss\"" +
						" " +
						"style:font-pitch=\"variable\"/>");
		writer.write(
				"<style:font-face style:name=\"Lucida Sans Unicode\" svg:font-family=\"'Lucida Sans Unicode'\" " +
						"style:font-family-generic=\"system\" style:font-pitch=\"variable\"/>");
		writer.write(
				"<style:font-face style:name=\"Tahoma\" svg:font-family=\"Tahoma\" " +
						"style:font-family-generic=\"system\" style:font-pitch=\"variable\"/>");
		writer.write("</office:font-face-decls>");
		writer.write("<office:automatic-styles>");

		this.stylesContainer.writeDataStyles(util, writer);
		this.stylesContainer.writeContentAutomaticStyles(util, writer);

		writer.write("</office:automatic-styles>");
		writer.write("<office:body>");
		writer.write("<office:spreadsheet>");
	}

	private void appendAutofilters(final Appendable appendable, final XMLUtil util) throws IOException {
		appendable.append("<table:database-ranges>");
		for (final String autofilter : this.autofilters) {
			appendable.append("<table:database-range");
			util.appendAttribute(appendable, "table:display-filter-buttons", "true");
			util.appendAttribute(appendable, "table:target-range-address", autofilter);
			appendable.append("/>");
		}
		appendable.append("</table:database-ranges>");
	}

	public void addAutofilter(final Table table, final int r1, final int c1, final int r2, final int c2) {
		if (this.autofilters == null)
			this.autofilters = new ArrayList<String>();

		this.autofilters.add(this.positionUtil.toRangeAddress(table, r1, c1, r2, c2));
	}

}
