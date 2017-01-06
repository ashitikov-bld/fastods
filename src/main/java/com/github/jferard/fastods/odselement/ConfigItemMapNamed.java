/* *****************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
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
 * ****************************************************************************/
package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.util.XMLUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 3.10.4 config:config-item-map-indexed
 */
public class ConfigItemMapNamed implements ConfigBlock {
	private final String name;
	private final Map<String, ConfigItemMapEntry> map;

	public ConfigItemMapNamed(String name) {
		this.name = name;
		this.map = new HashMap<String, ConfigItemMapEntry>();
	}

	@Override
	public String getName() {
		return this.name;
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public ConfigItemMapEntry get(Object key) {
		return map.get(key);
	}

	public ConfigItemMapEntry put(ConfigItemMapEntry value) {
		return map.put(value.getName(), value);
	}

	public ConfigItemMapEntry remove(Object key) {
		return map.remove(key);
	}

	@Override
	public void appendXML(XMLUtil util, Appendable appendable) throws IOException {
		appendable.append("<config:config-item-map-named");
		util.appendAttribute(appendable, "config:name", this.name);
		appendable.append(">");
		for (ConfigItemMapEntry entry : this.map.values())
			entry.appendXML(util, appendable);
		appendable.append("</config:config-item-map-named>");
	}
}