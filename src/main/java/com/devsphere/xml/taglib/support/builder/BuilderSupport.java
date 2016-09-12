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

package com.devsphere.xml.taglib.support.builder;

import com.devsphere.xml.saxdomix.SDXBuilder;
import com.devsphere.xml.saxdomix.SDXController;

import com.devsphere.logging.AbstractLogger;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.PrintWriter;
import java.io.IOException;

/**
 * This helper class extends <CODE>com.devsphere.xml.saxdomix.SDXBuilder</CODE>
 * with methods for parsing an input source and logging parsing errors.
 */
public class BuilderSupport extends SDXBuilder {
    private static SAXParserFactory saxFactoryVal;
    private static SAXParserFactory saxFactoryNoVal;
    private AbstractLogger logger;

    /**
     * Initializes the builder
     *
     * @param   handler     <CODE>org.xml.sax.helpers.DefaultHandler</CODE>
     *                      instance that will handle the SAX events
     * @param   controller  <CODE>com.devsphere.xml.saxdomix.SDXController</CODE>
     *                      instance that will handle the DOM sub-trees and
     *                      control the switches between SAX and DOM
     * @param   logger      <CODE>com.devsphere.logging.AbstractLogger</CODE>
     *                      instance that will be used to log the parsing
     *                      errors, if any
     */
    public BuilderSupport(DefaultHandler handler, SDXController controller,
        AbstractLogger logger) {
        super(handler, controller);
        this.logger = logger;
    }

    /**
     * Parses an input source using SAXDOMIX. It creates a SAX parser and
     * passes <CODE>this</CODE> to its <CODE>parse()</CODE> method.
     * Therefore this builder object will act as SAX handler and
     * forward the SAX events or create DOM sub-trees as indicated
     * by the controller object.
     *
     * @param   source          the XML input source that must be parsed
     * @param   validating      a flag indicating if the XML content should be
     *                          validated by the XML parser
     * @throws  SAXException    to signal a SAX error
     * @throws  IOException     to signal an I/O error
     * @throws  ParserConfigurationException    to signal a configuration error
     */
    public void parse(InputSource source, boolean validating)
            throws ParserConfigurationException, SAXException, IOException {
        // Get a SAX parser
        SAXParser saxParser = getSAXParser(validating);

        // Register the builder as handler for lexical SAX events
        saxParser.getXMLReader().setProperty(
            "http://xml.org/sax/properties/lexical-handler", this);

        // Parse the input source
        saxParser.parse(source, this);
    }

    /**
     * Logs a parse warning.
     *
     * @param   e               the SAX warning
     * @throws  SAXException    no exception is thrown
     */
    public void warning(SAXParseException e) throws SAXException {
        log("Parse Warning", e);
    }

    /**
     * Logs a parse error.
     *
     * @param   e               the SAX error
     * @throws  SAXException    no exception is thrown
     */
    public void error(SAXParseException e) throws SAXException {
        log("Parse Error", e);
    }

    /**
     * Throws a fatal parse error. The exception isn't logged.
     * The catcher of the exception should log it.
     *
     * @param   e               the SAX fatal error
     * @throws  SAXException    the SAX fatal error
     */
    public void fatalError(SAXParseException e) throws SAXException {
        throw e;
    }

    /**
     * Logs a SAX warning or error.
     *
     * @param   message the message that must be logged
     * @param   e       the exception that must be logged
     */
    protected void log(String message, SAXParseException e) {
        StringBuffer buf = new StringBuffer();
        buf.append(message);
        buf.append(" ");
        if (e.getLineNumber() != -1)
            buf.append("at line " + e.getLineNumber() + " ");
        if (e.getSystemId() != null)
            buf.append("of " + e.getSystemId() + " ");
        buf.append("- ");
        buf.append(e.getMessage());
        logger.log(buf.toString());
    }

    /**
     * Creates a SAX parser object.
     *
     * @param   validating  flag indicating if validation should be performed
     *                      during the parsing of an XML source
     * @throws  SAXException    to signal a SAX error
     * @throws  ParserConfigurationException    to signal a configuration error
     * @return  a SAX parser object
     */
    protected SAXParser getSAXParser(boolean validating)
            throws SAXException, ParserConfigurationException {
        return getSAXParserFactory(validating).newSAXParser();
    }

    /**
     * Creates a factory for SAX parsers.
     *
     * @param   validating  flag indicating if validation should be performed
     *                      during the parsing of an XML source
     * @return  a factory for SAX parsers
     */
    protected static synchronized SAXParserFactory getSAXParserFactory(
        boolean validating) {
        SAXParserFactory saxFactory
            = validating ? saxFactoryVal : saxFactoryNoVal;
        if (saxFactory == null) {
            saxFactory = SAXParserFactory.newInstance();
            saxFactory.setValidating(validating);
            saxFactory.setNamespaceAware(true);
            if (validating)
                saxFactoryVal = saxFactory;
            else
                saxFactoryNoVal = saxFactory;
        }
        return saxFactory;
    }

}
