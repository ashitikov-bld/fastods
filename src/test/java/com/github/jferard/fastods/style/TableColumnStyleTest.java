package com.github.jferard.fastods.style;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.DomTester;
import com.github.jferard.fastods.entry.OdsEntries;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

public class TableColumnStyleTest {
	private XMLUtil util;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
	}

	@Test
	public final void testAddEmptyToFile() {
		final TableColumnStyle tcs = TableColumnStyle.builder("test").build();
		final OdsEntries entries = PowerMock.createMock(OdsEntries.class);
		final StyleTag styleTag = tcs;

		entries.addStyleTag(styleTag);
		PowerMock.replayAll();

		tcs.addToEntries(entries);

		PowerMock.verifyAll();
	}

	@Test
	public final void testDefaultCellStyle() throws IOException {
		final TableCellStyle cs = TableCellStyle.builder("t").build();
		final TableColumnStyle tcs = TableColumnStyle.builder("test")
				.defaultCellStyle(cs).build();
		final StringBuilder sbt = new StringBuilder();

		tcs.appendXMLToTable(this.util, sbt, -1);

		DomTester.assertEquals(
				"<table:table-column table:style-name=\"test\" table:default-cell-style-name=\"t\"/>",
				sbt.toString());
		Assert.assertEquals(cs.getName(), tcs.getDefaultCellStyleName());
	}

	@Test
	public final void testEmpty() throws IOException {
		final TableColumnStyle tcs = TableColumnStyle.builder("test").build();
		final StringBuilder sbc = new StringBuilder();
		final StringBuilder sbt = new StringBuilder();

		tcs.appendXMLToStylesEntry(this.util, sbc);
		tcs.appendXMLToTable(this.util, sbt, 1);

		DomTester.assertEquals("<style:style style:name=\"test\" style:family=\"table-column\">"
						+ "<style:table-column-properties fo:break-before=\"auto\" style:column-width=\"2.5cm\"/>"
						+ "</style:style>", sbc.toString());
		DomTester.assertEquals(
				"<table:table-column table:style-name=\"test\" table:default-cell-style-name=\"Default\"/>",
				sbt.toString());
	}

	@Test
	public final void testEmpty2() throws IOException {
		final TableColumnStyle tcs = TableColumnStyle.builder("test").build();
		final StringBuilder sbt = new StringBuilder();
		tcs.appendXMLToTable(this.util, sbt, 2);

		DomTester.assertEquals(
				"<table:table-column table:style-name=\"test\" table:number-columns-repeated=\"2\" table:default-cell-style-name=\"Default\"/>",
				sbt.toString());
	}

	@Test
	public final void testWidth() throws IOException {
		final TableColumnStyle tcs = TableColumnStyle.builder("test")
				.columnWidth("1pt").build();
		final StringBuilder sbc = new StringBuilder();

		tcs.appendXMLToStylesEntry(this.util, sbc);

		Assert.assertTrue(DomTester
				.equals("<style:style style:name=\"test\" style:family=\"table-column\">"
						+ "<style:table-column-properties fo:break-before=\"auto\" style:column-width=\"1pt\"/>"
						+ "</style:style>", sbc.toString()));
		Assert.assertEquals("1pt", tcs.getColumnWidth());
		Assert.assertEquals(tcs, tcs);
		Assert.assertEquals(tcs.hashCode(), tcs.hashCode());
	}
}
