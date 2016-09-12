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

import com.devsphere.xml.saxdomix.SDXTransformer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import java.io.IOException;

import java.util.Vector;

/**
 * This example is similar to <CODE>MixedTransformer</CODE>, but it "knows"
 * when to request the DOM sub-trees for transformation.
 * <CODE>SmartTransformer</CODE> parses the XSL file and analyzes
 * the <CODE>match</CODE> attribute of the <CODE>template</CODE> and
 * <CODE>key</CODE> tags. This process is done before the mixed SAX-DOM parsing
 * and is useful to find out the names of the elements for which there are
 * template rules. The <CODE>wantDOM()</CODE> method returns <CODE>true</CODE>
 * for these elements.
 *
 * @see com.devsphere.xml.saxdomix.SDXTransformer
 * @see com.devsphere.examples.xml.saxdomix.TransformerRunner
 */
public class SmartTransformer extends SDXTransformer {
    /** JAXP factory for SAX parsers used to analyze the XSL files */
    protected static SAXParserFactory jaxpFactory;

    static {
        // Create and configure the JAXP factory
        jaxpFactory = SAXParserFactory.newInstance();
        jaxpFactory.setValidating(false);
        jaxpFactory.setNamespaceAware(true);
    }

    /** The names of the elements that will be the roots of the DOM sub-trees */
    protected Vector elements;

    /**
     * Does the initialization
     */
    public SmartTransformer(String xslSystemID) throws TransformerException,
            ParserConfigurationException, SAXException, IOException {
        super(new StreamSource(xslSystemID));
        buildElementsVector(xslSystemID);
    }

    /**
     * Returns <CODE>true</CODE> when the application wants to receive
     * a DOM sub-tree for handling, that is the <CODE>elements</CODE> vector
     * contains the qualified name of the current element
     */
    public boolean wantDOM(String namespaceURI, String localName,
            String qualifiedName, Attributes attributes) throws SAXException {
        return elements.contains(qualifiedName);
    }

    /**
     * Parses the XSL file end analyzes the <CODE>match</CODE> attributes
     * of the <CODE>template</CODE> and <CODE>key</CODE> tags. This process
     * is done before the mixed SAX-DOM parsing and is useful to find out
     * the names of the elements for which there are template rules.
     * The <CODE>wantDOM()</CODE> method returns <CODE>true</CODE> for these
     * elements.
     */
    protected void buildElementsVector(String xslSystemID)
            throws ParserConfigurationException, SAXException, IOException {
        // Create the vector for the element names
        elements = new Vector();

        // Create a SAX handler for the parsing of the XSL file
        DefaultHandler handler = new DefaultHandler() {
            public void startElement(String namespaceURI, String localName,
                String qualifiedName, Attributes attributes)
                throws SAXException {
                if (qualifiedName.equals("xsl:template")
                    || qualifiedName.equals("xsl:key")) {
                    // Got a template or a key
                    String pattern = attributes.getValue("match");
                    if (pattern != null && pattern.length() > 0)
                        // Got a pattern. Process it
                        processPattern(pattern);
                }
            }
        };

        // Create the SAX parser
        SAXParser saxParser = jaxpFactory.newSAXParser();

        // Parse the XSL file to extract the element names for wantDOM()
        saxParser.parse(xslSystemID, handler);
    }

    /**
     * Processes the value of the <CODE>match</CODE> attribute of
     * a <CODE>template</CODE> or <CODE>key</CODE> tag. It removes all
     * predicates and passes each XPath to <CODE>processPath()</CODE>.
     */
    protected void processPattern(String pattern) {
        // Possible states during the parsing of the pattern
        final int IN_PATH = 0;
        final int IN_PREDICATE = 1;
        final int IN_QUOTE = 2;

        // Initialize the parsing of the pattern string
        StringBuffer path = new StringBuffer();
        int state = IN_PATH;
        char quote = 0;
        int length = pattern.length();
        int i = 0;

        // Parse the pattern string
        while (i < length) {
            char ch = pattern.charAt(i);
            switch (state) {
                case IN_PATH:
                    switch (ch) {
                        case '|':
                            // Got a path separator. Process the current path
                            // and then clear the path buffer
                            processPath(path.toString());
                            path.setLength(0);
                            break;
                        case '[':
                            // Enter in a predicate
                            state = IN_PREDICATE;
                            break;
                        case ' ':
                        case '\t':
                            // Ignore spaces and tabs
                            break;
                        default:
                            // Append the character to the current path
                            path.append(ch);
                    }
                    break;
                case IN_PREDICATE:
                    switch (ch) {
                        case ']':
                            // Exit from a predicate
                            state = IN_PATH;
                            break;
                        case '\'':
                        case '"':
                            // Enter in a quoted string of the predicate
                            state = IN_QUOTE;
                            quote = ch;
                            break;
                    }
                    break;
                case IN_QUOTE:
                    if (ch == quote)
                        // Exit from the quoted string of the predicate
                        state = IN_PREDICATE;
                    break;
            }
            i++;
        }

        // Process the last path too
        processPath(path.toString());
    }

    /**
     * Adds the last element of an XPath to the <CODE>elements</CODE> vector.
     */
    protected void processPath(String path) {
        if (path.length() == 0)
            return;

        // Get the last step of the given path
        String step = path;
        int slashIndex = path.lastIndexOf('/');
        if (slashIndex != -1)
            step = path.substring(slashIndex + 1);

        // See if the last step is a name. (It could be *, for example)
        boolean nameFlag = true;
        int length = step.length();
        for (int i = 0; i < length; i++) {
            char ch = step.charAt(i);
            if (('a' > ch || ch > 'z')
                && ('A' > ch || ch > 'Z')
                && ('0' > ch || ch > '9')
                && ch != '.' && ch != ':'
                && ch != '-' && ch != '_') {
                    nameFlag = false;
                    break;
                }
        }

        if (nameFlag) {
            // Got a name. Add it to the vector if it's not already there
            if (step.startsWith("child::"))
                step = step.substring("child::".length());
            if (step.indexOf("::") != -1)
                step = null;
            if (step.equals(".") || step.equals(".."))
                step = null;
            if (step != null && step.length() > 0)
                if (!elements.contains(step))
                    elements.addElement(step);
        }
    }

    /**
     * Uses <CODE>TransformerRunner</CODE> in order to create an instance of
     * this class, configure it and call its <CODE>transform()</CODE> method.
     */
    public static void main(String args[]) {
        TransformerRunner.run(SmartTransformer.class.getName(), args);
    }

}
