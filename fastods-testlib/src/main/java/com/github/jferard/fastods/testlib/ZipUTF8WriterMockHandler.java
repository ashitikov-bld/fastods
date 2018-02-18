/*
 * FastODS - A very fast and lightweight (no dependency) library for creating ODS
 *    (Open Document Spreadsheet, mainly for Calc) files in Java.
 *    It's a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016-2018 J. Férard <https://github.com/jferard>
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

package com.github.jferard.fastods.testlib;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.zip.ZipEntry;

/**
 * See https://github.com/jferard/fastods/issues/29
 *
 * @author Julien Férard
 */
public class ZipUTF8WriterMockHandler implements InvocationHandler {
    private final ZipUTF8WriterMock mock;

    public static ZipUTF8WriterMockHandler create() {
        return new ZipUTF8WriterMockHandler(ZipUTF8WriterMock.createMock());
    }

    ZipUTF8WriterMockHandler(final ZipUTF8WriterMock mock) {
        this.mock = mock;
    }

    @Override
    public Object invoke(final Object o, final Method method, final Object[] objects) throws Throwable {
        final String name = method.getName();
        if (name.equals("append")) {
            if (objects[0] instanceof Character) {
                this.mock.append((Character) objects[0]);
            } else {
                if (objects.length > 1)
                    this.mock.append((CharSequence) objects[0], (Integer) objects[1], (Integer) objects[2]);
                else
                    this.mock.append((CharSequence) objects[0]);
            }
            return this.mock;
        } else if (name.equals("close")) {
            this.mock.close();
        } else if (name.equals("closeEntry")) {
            this.mock.closeEntry();
        } else if (name.equals("finish")) {
            this.mock.finish();
        } else if (name.equals("flush")) {
            this.mock.flush();
        } else if (name.equals("putNextEntry")) {
            this.mock.putNextEntry((ZipEntry) objects[0]);
        } else if (name.equals("setComment")) {
            this.mock.setComment((String) objects[0]);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance(final Class<T> cls) {
        final ClassLoader classLoader = cls.getClassLoader();
        final Class<?>[] classes = new Class<?>[]{cls};
        return (T) Proxy.newProxyInstance(classLoader, classes, this);
    }

    public String getEntryAsString(final String name) {
        final StringBuilder stringBuilder = this.mock.getBuilder(name);
        return stringBuilder == null ? null : stringBuilder.toString();
    }

    public Document getEntryAsDocument(final String name) throws IOException, SAXException, ParserConfigurationException {
        final StringBuilder stringBuilder = this.mock.getBuilder(name);
        if (stringBuilder == null)
            return null;

        final String xml = stringBuilder.toString();
        final DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xml)));
    }
}

