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

package com.devsphere.examples.xml.saxdomix;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This class outputs SAX events.
 * It is used by <CODE>MixedParsing</CODE>.
 *
 * @see com.devsphere.examples.xml.saxdomix.OutputterBase
 * @see com.devsphere.examples.xml.saxdomix.MixedParsing
 */
public class SAXOutputter extends OutputterBase
        implements ContentHandler, ErrorHandler {
    /**
     * Zero-arg constructor.
     */
    public SAXOutputter() {
        super();
    }

    /**
     * Prints information about a <CODE>setDocumentLocator()</CODE> event.
     */
    public void setDocumentLocator(Locator locator) {
        blankLine();
        output("SAX setDocumentLocator");
    }

    /**
     * Prints information about a <CODE>startDocument()</CODE> event.
     */
    public void startDocument() throws SAXException {
        blankLine();
        output("SAX startDocument");
        incLevel();
    }

    /**
     * Prints information about an <CODE>endDocument()</CODE> event.
     */
    public void endDocument() throws SAXException {
        decLevel();
        blankLine();
        output("SAX endDocument");
    }

    /**
     * Prints information about a <CODE>startElement()</CODE> event.
     */
    public void startElement(String namespaceURI, String localName,
            String qualifiedName, Attributes attributes) throws SAXException {
        blankLine();
        output("SAX startElement " + qualifiedName);
        incLevel();
    }

    /**
     * Prints information about an <CODE>endElement()</CODE> event.
     */
    public void endElement(String namespaceURI, String localName,
            String qualifiedName) throws SAXException {
        decLevel();
        blankLine();
        output("SAX endElement " + qualifiedName);
    }

    /**
     * Prints information about a <CODE>characters()</CODE> event.
     */
    public void characters(char ch[], int start, int length)
            throws SAXException {
        String data = new String(ch, start, length);
        blankLine();
        output("SAX characters", data);
    }

    /**
     * Does nothing.
     */
    public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException {
    }

    /**
     * Prints information about a <CODE>processingInstruction()</CODE> event.
     */
    public void processingInstruction(String target, String data)
            throws SAXException {
        blankLine();
        output("SAX processingInstruction " + target);
    }

    /**
     * Does nothing.
     */
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
    }

    /**
     * Does nothing.
     */
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    /**
     * Does nothing.
     */
    public void skippedEntity(String name) throws SAXException {
    }

    /**
     * Prints a warning.
     */
    public void warning(SAXParseException e) throws SAXException {
        blankLine();
        output("WARNING " + e.getMessage());
    }

    /**
     * Prints an error.
     */
    public void error(SAXParseException e) throws SAXException {
        blankLine();
        output("ERROR " + e.getMessage());
    }

    /**
     * Prints a fatal error.
     */
    public void fatalError(SAXParseException e) throws SAXException {
        blankLine();
        output("FATAL ERROR " + e.getMessage());
        throw e;
    }

}
