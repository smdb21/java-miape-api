/*
 * Copyright (c) 2000-2002 Devsphere.com. All rights reserved.
 *
 * The source code and documentation are provided for reference purposes and
 * to modify and recompile the code if necessary. You may not redistribute 
 * the source code or documentation without prior written permission of 
 * Devsphere. For written permission, please contact info@devsphere.com
 *
 * Redistribution and use of the xmltaglib.jar, saxdomix.jar, dslogging.jar,
 * OutputXML.tld and ProcessXML.tld files, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 * 1. The redistribution must reproduce the above copyright notice, this 
 * list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 2. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by Devsphere (www.devsphere.com)"
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 3. The names "SAXDOMIX", "XJTL" and "Devsphere" must not be used to 
 * endorse or promote products derived from this software without prior 
 * written permission of Devsphere.
 *
 * 4. Products derived from this software may not be called "Devsphere",
 * nor may "Devsphere" appear in their name, without prior written
 * permission of Devsphere.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL DEVSPHERE OR ITS DEVELOPERS 
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, 
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT 
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.devsphere.xml.taglib.output.builder;

import com.devsphere.xml.taglib.support.builder.Outputter;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class extends <CODE>org.xml.sax.helpers.DefaultHandler</CODE>
 * to generate JSP code that outputs XML.
 *
 * <P>An instance of this class is created by a <CODE>Main</CODE> object.
 */
public class Handler extends DefaultHandler {
    private Parameters params;
    private Outputter out;

    /**
     * Initializes the handler.
     *
     * @param   params          the command line parameters
     * @param   out             the object used to output the generated code
     */
    public Handler(Parameters params, Outputter out) {
        this.params = params;
        this.out = out;
    }

    /**
     * Does nothing.
     *
     * @param   locator         the SAX <CODE>Locator</CODE> object
     */
    public void setDocumentLocator(Locator locator) {
    }

    /**
     * Generates the JSP header and JSP code that outputs an XML header.
     *
     * @throws  SAXException    to signal any error that might occur
     */
    public void startDocument() throws SAXException {
        out.println("<%@ page language=\"java\" %>");
        out.println("<%@ taglib prefix=\"c\" "
            + "uri=\"http://java.sun.com/jstl/core\" %>");
        out.println("<%@ taglib prefix=\"x\" "
            + "uri=\"http://java.sun.com/jstl/xml\" %>");
        out.println("<%@ taglib prefix=\"o\" "
            + "uri=\"http://devsphere.com/xml/taglib/output\" %>");
        out.println();
        out.println("<o:document>");
        out.incLevel();
    }

    /**
     * Generates JSP code that ends the XML producing.
     *
     * @throws  SAXException    to signal any error that might occur
     */
    public void endDocument() throws SAXException {
        out.decLevel();
        out.println("</o:document>");
    }

    /**
     * Generates JSP code that outputs a start tag.
     *
     * @param   namespaceURI    the element's namespace URI
     * @param   localName       the element's local name
     * @param   qualifiedName   the element's qualified name
     * @param   attributes      the element's attributes
     * @throws  SAXException    to signal any error that might occur
     */
    public void startElement(String namespaceURI, String localName,
            String qualifiedName, Attributes attributes) throws SAXException {
        StringBuffer buf = new StringBuffer();
        buf.append("<o:element name=\"");
        buf.append(qualifiedName);
        buf.append("\"");
        if (attributes.getLength() > 0) {
            buf.append(" attr=\"");
            for (int i = 0; i < attributes.getLength(); i++) {
                if (i > 0)
                    buf.append(" ");
                buf.append(attributes.getLocalName(i));
                out.println("<c:set var=\"" + attributes.getLocalName(i) + "\""
                    + " value=\"" + attributes.getValue(i) + "\"/>");
            }
            buf.append("\"");
        }
        buf.append(">");
        out.println(buf.toString());
        out.incLevel();
    }

    /**
     * Generates JSP code that outputs an end tag.
     *
     * @param   namespaceURI    the element's namespace URI
     * @param   localName       the element's local name
     * @param   qualifiedName   the element's qualified name
     * @throws  SAXException    to signal any error that might occur
     */
    public void endElement(String namespaceURI, String localName,
            String qualifiedName) throws SAXException {
        out.decLevel();
        out.println("</o:element>");
    }

    /**
     * Generates JSP code that outputs some character data.
     *
     * @param   ch              the characters from the XML document
     * @param   start           the start position in the array
     * @param   length          the number of characters to get from the array
     * @throws  SAXException    to signal any error that might occur
     */
    public void characters(char ch[], int start, int length)
            throws SAXException {
        String data = new String(ch, start, length);
        if (data.trim().length() > 0)
            out.println("<o:data>" + data + "</o:data>");
    }

    /**
     * Generates JSP code that outputs a processing instruction.
     *
     * @param   target          the target of the processing instruction.
     * @param   data            the data of the processing instruction.
     * @throws  SAXException    to signal any error that might occur
     */
    public void processingInstruction(String target, String data)
            throws SAXException {
        StringBuffer buf = new StringBuffer();
        out.println("<o:pi target=\"" + target + "\" data=\"" + data + "\"/>");
    }

}
