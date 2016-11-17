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
package com.github.jferard.fastods.datastyle;

import java.io.IOException;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.github.jferard.fastods.DomTester;
import com.github.jferard.fastods.util.FastOdsXMLEscaper;
import com.github.jferard.fastods.util.XMLUtil;

/**
 * @author Julien Férard Copyright (C) 2016 J. Férard
 */
public class DateStyleTest {
	private XMLUtil util;
	private Locale locale;
	private DataStyleBuilderFactory factory;

	@Before
	public void setUp() {
		this.util = new XMLUtil(new FastOdsXMLEscaper());
		this.locale = Locale.US;
		this.factory = new DataStyleBuilderFactory(this.util, this.locale);
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testWithNullName() {
		this.factory.dateStyleBuilder(null).locale(this.locale)
				.build();
	}

	@Test
	public final void testWithLocale() throws IOException, SAXException {
		DateStyle ds = this.factory.dateStyleBuilder("test").locale(this.locale)
				.build();
		StringBuilder sb = new StringBuilder();
		ds.appendXMLToStylesEntry(this.util, sb);
		Assert.assertTrue(DomTester.equals(
				"<number:date-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\" number:automatic-order=\"false\" number:format-source=\"language\"/>",
				sb.toString()));
	}

	@Test
	public final void testWithLanguage() throws IOException, SAXException {
		DateStyle ds = this.factory.dateStyleBuilder("test").language("fr")
				.build();
		StringBuilder sb = new StringBuilder();
		ds.appendXMLToStylesEntry(this.util, sb);
		Assert.assertTrue(DomTester.equals(
				"<number:date-style style:name=\"test\" number:language=\"fr\" number:country=\"US\" style:volatile=\"true\" number:automatic-order=\"false\" number:format-source=\"language\"/>",
				sb.toString()));
	}

	@Test
	public final void testWithOrder() throws IOException, SAXException {
		DateStyle ds = this.factory.dateStyleBuilder("test")
				.automaticOrder(true).build();
		StringBuilder sb = new StringBuilder();
		ds.appendXMLToStylesEntry(this.util, sb);
		Assert.assertTrue(DomTester.equals(
				"<number:date-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\" number:automatic-order=\"true\" number:format-source=\"language\"/>",
				sb.toString()));
	}

	@Test
	public final void testWithVolatile()
			throws IOException, SAXException {
		DateStyle ds = this.factory.dateStyleBuilder("test")
				.volatileStyle(false).build();
		StringBuilder sb = new StringBuilder();
		ds.appendXMLToStylesEntry(this.util, sb);
		Assert.assertTrue(DomTester.equals(
				"<number:date-style number:language=\"en\" style:name=\"test\" number:country=\"US\" number:automatic-order=\"false\" number:format-source=\"language\"/>",
				sb.toString()));
		Assert.assertFalse(ds.isAutomaticOrder());
	}

	@Test
	public final void testWithFormat1() throws IOException, SAXException {
		DateStyle ds = this.factory.dateStyleBuilder("test")
				.dateFormat(DateStyle.Format.DDMMYY).build();
		StringBuilder sb = new StringBuilder();
		ds.appendXMLToStylesEntry(this.util, sb);
		Assert.assertTrue(DomTester
				.equals("<number:date-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\" number:automatic-order=\"false\" number:format-source=\"fixed\"><number:day number:style=\"long\"/>"
						+ "<number:text>.</number:text><number:month number:style=\"long\"/>"
						+ "<number:text>.</number:text><number:year/>"
						+ "</number:date-style>", sb.toString()));
	}

	@Test
	public final void testWithFormat2() throws IOException, SAXException {
		DateStyle ds = this.factory.dateStyleBuilder("test")
				.dateFormat(DateStyle.Format.DDMMYYYY).build();
		StringBuilder sb = new StringBuilder();
		ds.appendXMLToStylesEntry(this.util, sb);
		Assert.assertTrue(DomTester
				.equals("<number:date-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\" number:automatic-order=\"false\" number:format-source=\"fixed\">"
						+ "<number:day number:style=\"long\"/>"
						+ "<number:text>.</number:text>"
						+ "<number:month number:style=\"long\"/>"
						+ "<number:text>.</number:text>"
						+ "<number:year number:style=\"long\"/>"
						+ "</number:date-style>", sb.toString()));
	}

	@Test
	public final void testWithFormat3() throws IOException, SAXException {
		DateStyle ds = this.factory.dateStyleBuilder("test")
				.dateFormat(DateStyle.Format.MMMM).build();
		StringBuilder sb = new StringBuilder();
		ds.appendXMLToStylesEntry(this.util, sb);
		Assert.assertTrue(DomTester
				.equals("<number:date-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\" number:automatic-order=\"false\" number:format-source=\"fixed\">"
						+ "<number:month number:style=\"long\" number:textual=\"true\"/>"
						+ "</number:date-style>", sb.toString()));
	}

	@Test
	public final void testWithFormat4() throws IOException, SAXException {
		DateStyle ds = this.factory.dateStyleBuilder("test")
				.dateFormat(DateStyle.Format.MMYY).build();
		StringBuilder sb = new StringBuilder();
		ds.appendXMLToStylesEntry(this.util, sb);
		Assert.assertTrue(
				DomTester.equals(
						"<number:date-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\" number:automatic-order=\"false\" number:format-source=\"fixed\">"
								+ "<number:month number:style=\"long\"/>"
								+ "<number:text>.</number:text>"
								+ "<number:year/>" + "</number:date-style>",
						sb.toString()));
	}

	@Test
	public final void testWithFormat5() throws IOException, SAXException {
		DateStyle ds = this.factory.dateStyleBuilder("test")
				.dateFormat(DateStyle.Format.TMMMMYYYY).build();
		StringBuilder sb = new StringBuilder();
		ds.appendXMLToStylesEntry(this.util, sb);
		Assert.assertTrue(DomTester
				.equals("<number:date-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\" number:automatic-order=\"false\" number:format-source=\"fixed\">"
						+ "<number:day/>" + "<number:text>. </number:text>"
						+ "<number:month number:style=\"long\" number:textual=\"true\"/>"
						+ "<number:text> </number:text>"
						+ "<number:year number:style=\"long\"/>"
						+ "</number:date-style>", sb.toString()));
	}

	@Test
	public final void testWithFormat6() throws IOException, SAXException {
		DateStyle ds = this.factory.dateStyleBuilder("test")
				.dateFormat(DateStyle.Format.WW).build();
		StringBuilder sb = new StringBuilder();
		ds.appendXMLToStylesEntry(this.util, sb);
		Assert.assertTrue(DomTester.equals(
				"<number:date-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\" number:automatic-order=\"false\" number:format-source=\"fixed\">"
						+ "<number:week-of-year/>" + "</number:date-style>",
				sb.toString()));
	}

	@Test
	public final void testWithFormat7() throws IOException, SAXException {
		DateStyle ds = this.factory.dateStyleBuilder("test")
				.dateFormat(DateStyle.Format.YYYYMMDD).build();
		StringBuilder sb = new StringBuilder();
		ds.appendXMLToStylesEntry(this.util, sb);
		Assert.assertTrue(DomTester
				.equals("<number:date-style style:name=\"test\" number:language=\"en\" number:country=\"US\" style:volatile=\"true\" number:automatic-order=\"false\" number:format-source=\"fixed\">"
						+ "<number:year number:style=\"long\"/>"
						+ "<number:text>-</number:text>"
						+ "<number:month number:style=\"long\"/>"
						+ "<number:text>-</number:text>"
						+ "<number:day number:style=\"long\"/>"
						+ "</number:date-style>", sb.toString()));
	}
}
