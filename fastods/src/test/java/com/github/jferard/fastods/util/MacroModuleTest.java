package com.github.jferard.fastods.util;

import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.testlib.DomTester;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;

public class MacroModuleTest {
    @Test public void testIndexLine() throws IOException {
        final MacroModule module = new MacroModule("n", "l", "module content");
        final StringBuilder sb = new StringBuilder();
        module.appendIndexLine(XMLUtil.create(), sb);

        DomTester.assertEquals("<library:element library:name=\"n\"/>", sb.toString());
    }

    @Test public void testAdd() throws IOException {
        final OdsDocument document = PowerMock.createMock(OdsDocument.class);
        final MacroModule module = new MacroModule("n", "l", "module content");
        final Capture<StringBuilder> sb = new Capture<StringBuilder>();

        PowerMock.resetAll();
        document.addExtraFile(EasyMock.eq("slash/n.xml"), EasyMock.eq("text/xml"),
                EasyMock.capture(sb));

        PowerMock.replayAll();
        module.add(XMLUtil.create(), document, "slash/");

        PowerMock.verifyAll();
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE script:module" +
                " PUBLIC \"-//OpenOffice.org//DTD OfficeDocument 1.0//EN\" \"module" +
                ".dtd\"><script:module xmlns:script=\"http://openoffice.org/2000/script\" " +
                "script:name=\"n\" script:language=\"l\" script:moduleType=\"normal\">module " +
                "content</script:module>", sb.toString());
    }
}