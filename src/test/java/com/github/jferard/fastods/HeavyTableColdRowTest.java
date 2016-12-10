package com.github.jferard.fastods;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import com.github.jferard.fastods.TableCell.Type;
import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.datastyle.DataStyles;
import com.github.jferard.fastods.datastyle.LocaleDataStyles;
import com.github.jferard.fastods.entry.StylesContainer;
import com.github.jferard.fastods.entry.StylesEntry;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.style.TableRowStyle;
import com.github.jferard.fastods.util.EqualityUtil;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;

public class HeavyTableColdRowTest {
	private DataStyles ds;
	private HeavyTableRow row;
	private StylesEntry se;
	private StylesContainer stc;
	private Table table;
	private XMLUtil xmlUtil;

	@Before
	public void setUp() {
		this.stc = PowerMock.createMock(StylesContainer.class);
		this.table = PowerMock.createMock(Table.class);
		final PositionUtil positionUtil = new PositionUtil(new EqualityUtil());
		final WriteUtil writeUtil = new WriteUtil();
		final XMLUtil xmlUtil = new XMLUtil(new FastOdsXMLEscaper());
		this.ds = new LocaleDataStyles(
				new DataStyleBuilderFactory(xmlUtil, Locale.US));
		this.row = new HeavyTableRow(writeUtil, xmlUtil, null, this.stc,
				this.ds, this.table, 10, 100);
		this.xmlUtil = XMLUtil.create();
	}

	@Test
	public final void testBoolean() {
		final TableCellStyle booleanStyle = this.ds.getBooleanStyle();
		this.stc.addDataStyle(booleanStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(booleanStyle);
		this.stc.addDataStyle(booleanStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(booleanStyle);
		PowerMock.replayAll();
		this.row.setBooleanValue(10, true);
		this.row.setBooleanValue(11, false);
		Assert.assertEquals("true", this.row.getBooleanValue(10));
		Assert.assertEquals("false", this.row.getBooleanValue(11));
		PowerMock.verifyAll();
	}

	@Test
	public final void testCalendar() {
		final TableCellStyle dateStyle = this.ds.getDateStyle();
		this.stc.addDataStyle(dateStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(dateStyle);
		PowerMock.replayAll();
		final Calendar d = Calendar.getInstance();
		d.setTimeInMillis(1234567891011l);
		this.row.setDateValue(7, d);
		Assert.assertEquals("2009-02-14T00:31:31.011",
				this.row.getDateValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testCurrencyFloat() {
		final TableCellStyle currencyStyle = this.ds.getCurrencyStyle();
		this.stc.addDataStyle(currencyStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(currencyStyle);
		PowerMock.replayAll();
		this.row.setCurrencyValue(10, 10.0, "€");
		Assert.assertEquals("€", this.row.getCurrency(10));
		Assert.assertEquals("10.0", this.row.getCurrencyValue(10));
		PowerMock.verifyAll();
	}

	@Test
	public final void testCurrencyInt() {
		final TableCellStyle currencyStyle = this.ds.getCurrencyStyle();
		this.stc.addDataStyle(currencyStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(currencyStyle);
		PowerMock.replayAll();
		this.row.setCurrencyValue(7, 10, "€");
		Assert.assertEquals("€", this.row.getCurrency(7));
		Assert.assertEquals("10.0", this.row.getCurrencyValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testCurrencyNumber() {
		final TableCellStyle currencyStyle = this.ds.getCurrencyStyle();
		this.stc.addDataStyle(currencyStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(currencyStyle);
		PowerMock.replayAll();
		this.row.setCurrencyValue(7, Double.valueOf(10.0), "€");
		Assert.assertEquals("€", this.row.getCurrency(7));
		Assert.assertEquals("10.0", this.row.getCurrencyValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testDate() {
		final TableCellStyle dateStyle = this.ds.getDateStyle();
		this.stc.addDataStyle(dateStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(dateStyle);
		PowerMock.replayAll();
		final Calendar d = Calendar.getInstance();
		d.setTimeInMillis(1234567891011l);
		this.row.setDateValue(7, d.getTime());
		Assert.assertEquals("2009-02-14T00:31:31.011",
				this.row.getDateValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testDouble() {
		final TableCellStyle numberStyle = this.ds.getNumberStyle();
		this.stc.addDataStyle(numberStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(numberStyle);
		PowerMock.replayAll();
		this.row.setFloatValue(7, Double.valueOf(10.999));
		Assert.assertEquals("10.999", this.row.getFloatValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testFloatDouble() {
		final TableCellStyle numberStyle = this.ds.getNumberStyle();
		this.stc.addDataStyle(numberStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(numberStyle);
		PowerMock.replayAll();
		this.row.setFloatValue(7, 9.999d);
		Assert.assertEquals("9.999", this.row.getFloatValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testFloatFloat() {
		final TableCellStyle numberStyle = this.ds.getNumberStyle();
		this.stc.addDataStyle(numberStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(numberStyle);
		PowerMock.replayAll();
		this.row.setFloatValue(7, 9.999f);
		Assert.assertEquals("9.999", this.row.getFloatValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testInt() {
		final TableCellStyle numberStyle = this.ds.getNumberStyle();
		this.stc.addDataStyle(numberStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(numberStyle);
		PowerMock.replayAll();
		this.row.setFloatValue(7, 999);
		Assert.assertEquals("999", this.row.getFloatValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge() {
		final HeavyTableRow row2 = PowerMock.createMock(HeavyTableRow.class);

		// PLAY
		for (int c = 11; c < 20; c++)
			EasyMock.expect(this.table.getRowSecure(c)).andReturn(row2);
		PowerMock.replayAll();
		this.row.setStringValue(7, "value");
		this.row.setCellMerge(7, 10, 8);
		Assert.assertEquals(10, this.row.getRowsSpanned(7));
		Assert.assertEquals(8, this.row.getColumnsSpanned(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge1b() {
		// PLAY
		PowerMock.replayAll();
		this.row.setStringValue(7, "value");
		this.row.setCellMerge(7, -1, 8);
		Assert.assertEquals(0, this.row.getRowsSpanned(7));
		Assert.assertEquals(8, this.row.getColumnsSpanned(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge1c() {
		final HeavyTableRow row2 = PowerMock.createMock(HeavyTableRow.class);

		// PLAY
		for (int c = 11; c < 20; c++)
			EasyMock.expect(this.table.getRowSecure(c)).andReturn(row2);
		PowerMock.replayAll();
		this.row.setStringValue(7, "value");
		this.row.setCellMerge(7, 10, -1);
		Assert.assertEquals(10, this.row.getRowsSpanned(7));
		Assert.assertEquals(0, this.row.getColumnsSpanned(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge1d() {
		// PLAY
		PowerMock.replayAll();
		this.row.setStringValue(7, "value");
		this.row.setCellMerge(7, -1, -1);
		Assert.assertEquals(0, this.row.getRowsSpanned(7));
		Assert.assertEquals(0, this.row.getColumnsSpanned(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge1e() {
		final HeavyTableRow row2 = PowerMock.createMock(HeavyTableRow.class);

		// PLAY
		EasyMock.expect(this.table.getRowSecure(EasyMock.anyInt())).andReturn(row2);
		EasyMock.expectLastCall().anyTimes();
		
		PowerMock.replayAll();
		this.row.setStringValue(7, "value");
		this.row.setCellMerge(0, 2, 2);
		this.row.setCellMerge(10, 3, 3);
		Assert.assertEquals(2, this.row.getRowsSpanned(0));
		Assert.assertEquals(2, this.row.getColumnsSpanned(0));
		Assert.assertEquals(3, this.row.getRowsSpanned(10));
		Assert.assertEquals(3, this.row.getColumnsSpanned(10));
		PowerMock.verifyAll();
	}

	@Test
	public final void testMerge1f() {
		final HeavyTableRow row2 = PowerMock.createMock(HeavyTableRow.class);
		
		// PLAY
		EasyMock.expect(this.table.getRowSecure(EasyMock.anyInt())).andReturn(row2);
		EasyMock.expectLastCall().anyTimes();
		
		PowerMock.replayAll();
		this.row.setStringValue(7, "value");
		this.row.setCellMerge(0, 20, 20);
		Assert.assertEquals(20, this.row.getRowsSpanned(0));
		Assert.assertEquals(20, this.row.getColumnsSpanned(0));
		Assert.assertEquals(-1, this.row.getColumnsSpanned(10));

		this.row.setCellMerge(10, 3, 3);
		Assert.assertEquals(-1, this.row.getColumnsSpanned(10));
		PowerMock.verifyAll();
	}

	/*
	@Test
	public final void testMerge1g() {
		// PLAY
		EasyMock.expect(this.table.getRowSecure(EasyMock.anyInt())).andReturn(row2);
		EasyMock.expectLastCall().anyTimes();
		
		PowerMock.replayAll();
		this.row.setStringValue(7, "value");
		this.row.setCellMerge(0, 20, 20);
		Assert.assertEquals(20, this.row.getRowsSpanned(0));
		Assert.assertEquals(20, this.row.getColumnsSpanned(0));
		Assert.assertEquals(-1, this.row.getColumnsSpanned(10));

		this.row.setCellMerge(10, 3, 3);
		Assert.assertEquals(-1, this.row.getColumnsSpanned(10));
		PowerMock.verifyAll();
	}*/
	
	@Test
	public final void testText() {
		// PLAY
		PowerMock.replayAll();
		Assert.assertNull(this.row.getText(0));
		final Text t0 = Text.content("text0");
		final Text t1 = Text.content("text1");
		this.row.setText(0, t0);
		this.row.setText(1, t1);
		Assert.assertEquals(t0, this.row.getText(0));
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testMerge2() throws IOException {
		final HeavyTableRow row2 = PowerMock.createMock(HeavyTableRow.class);

		// PLAY
		for (int c = 11; c < 20; c++)
			EasyMock.expect(this.table.getRowSecure(c)).andReturn(row2);
		PowerMock.replayAll();
		this.row.setStringValue(5, "value");
		this.row.setCellMerge(5, 10, 8);
		final StringBuilder sbt = new StringBuilder();
		this.row.appendXMLToTable(this.xmlUtil, sbt);
		Assert.assertEquals("<table:table-row table:style-name=\"ro1\">"
				+ "<table:table-cell table:number-columns-repeated=\"5\"/>"
				+ "<table:table-cell office:value-type=\"string\" office:string-value=\"value\" table:number-columns-spanned=\"8\" table:number-rows-spanned=\"10\"/>"
				+ "</table:table-row>", sbt.toString());
		PowerMock.verifyAll();
	}

	@Test
	public final void testObject() {
		PowerMock.replayAll();
		this.row.setObjectValue(7, null);
		PowerMock.verifyAll();
	}

	@Test
	public final void testPercentageFloat() {
		final TableCellStyle percentageStyle = this.ds.getPercentageStyle();
		this.stc.addDataStyle(percentageStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(percentageStyle);
		PowerMock.replayAll();
		this.row.setPercentageValue(7, 0.98);
		Assert.assertEquals("0.98", this.row.getPercentageValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testPercentageNumber() {
		final TableCellStyle percentageStyle = this.ds.getPercentageStyle();
		this.stc.addDataStyle(percentageStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(percentageStyle);
		PowerMock.replayAll();
		this.row.setPercentageValue(7, Double.valueOf(0.98));
		Assert.assertEquals("0.98", this.row.getPercentageValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testSpan() {
		PowerMock.replayAll();
		this.row.setColumnsSpanned(10, 2);
		Assert.assertEquals(2, this.row.getColumnsSpanned(10));
		PowerMock.verifyAll();
	}

	@Test
	public final void testString() {
		PowerMock.replayAll();
		this.row.setStringValue(7, "value");
		Assert.assertEquals("value", this.row.getStringValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testStyle() {
		final TableCellStyle tcs = TableCellStyle.builder("test").build();
		this.stc.addStyleToStylesCommonStyles(tcs);
		PowerMock.replayAll();
		this.row.setStyle(7, tcs);
		Assert.assertEquals("test", this.row.getStyleName(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testTime() {
		final TableCellStyle timeStyle = this.ds.getTimeStyle();
		this.stc.addDataStyle(timeStyle.getDataStyle());
		this.stc.addStyleToStylesCommonStyles(timeStyle);
		PowerMock.replayAll();
		this.row.setTimeValue(7, 1234567891011l);
		Assert.assertEquals("P14288DT23H31M31.11S", this.row.getTimeValue(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testTooltip() {
		PowerMock.replayAll();
		this.row.setTooltip(7, "tooltip");
		Assert.assertEquals("tooltip", this.row.getTooltip(7));
		PowerMock.verifyAll();
	}

	@Test
	public final void testColumnsSpanned() {
		PowerMock.replayAll();
		Assert.assertEquals(0, this.row.getColumnsSpanned(0));
		this.row.setColumnsSpanned(0, 1); // does nothing
		Assert.assertEquals(0, this.row.getColumnsSpanned(0));
		this.row.setColumnsSpanned(0, 10);
		Assert.assertEquals(10, this.row.getColumnsSpanned(0));
		for (int i = 1; i < 10; i++)
			Assert.assertEquals(-1, this.row.getColumnsSpanned(i));

		Assert.assertEquals(0, this.row.getColumnsSpanned(10));
		this.row.setColumnsSpanned(1, 4); // does nothing since cell is already
											// covered
		Assert.assertEquals(-1, this.row.getColumnsSpanned(1));
		PowerMock.verifyAll();
	}

	@Test
	public final void testRowsSpanned() {
		final HeavyTableRow r2 = PowerMock.createMock(HeavyTableRow.class);
		EasyMock.expect(this.table.getRowSecure(EasyMock.anyInt()))
				.andReturn(r2).anyTimes();

		PowerMock.replayAll();
		Assert.assertEquals(0, this.row.getRowsSpanned(0));
		this.row.setRowsSpanned(0, 1); // does nothing
		Assert.assertEquals(0, this.row.getRowsSpanned(0));
		this.row.setRowsSpanned(0, 10);
		Assert.assertEquals(10, this.row.getRowsSpanned(0));
		PowerMock.verifyAll();
	}

	@Test
	public final void testNullFieldCounter() throws IOException {
		PowerMock.replayAll();
		this.row.setStringValue(0, "v1");
		this.row.setStringValue(2, "v2");
		final StringBuilder sb = new StringBuilder();
		this.row.appendXMLToTable(this.xmlUtil, sb);
		DomTester.assertEquals("<table:table-row  table:style-name=\"ro1\">"
				+ "<table:table-cell office:value-type=\"string\" office:string-value=\"v1\"/>"
				+ "<table:table-cell/>"
				+ "<table:table-cell office:value-type=\"string\" office:string-value=\"v2\"/>"
				+ "</table:table-row>", sb.toString());
		PowerMock.verifyAll();
	}
	
	@Test
	public final void testGet() throws IOException {
		TableRowStyle trs = TableRowStyle.builder("a").build();
		
		//PLAY
		this.stc.addStyleToContentAutomaticStyles(trs);
		PowerMock.replayAll();
		this.row.setStyle(trs);
		this.row.setStringValue(0, "v1");
		
		Assert.assertEquals("a", this.row.getRowStyleName());
		Assert.assertNull("a", this.row.getTooltip(0));
		Assert.assertEquals(Type.STRING, this.row.getValueType(0));
		Assert.assertNull(this.row.getValueType(1));
		PowerMock.verifyAll();
	}
}
