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

package com.devsphere.xml.saxdomix.helpers;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import javax.xml.transform.sax.TransformerHandler;

/**
 * This is a filter for SAX events used by <CODE>SDXTransformer</CODE>.
 */
public class ResultFilter implements ContentHandler, LexicalHandler {
    /** The serializer that receives the filtered events */
    protected TransformerHandler serializer;

    /**
     * Creates a new filter.
     *
     * @param   serializer      Object that receives the filtered events
     */
    public ResultFilter(TransformerHandler serializer) {
        this.serializer = serializer;
    }

    /**
     * Ignores the SAX event.
     *
     * @param   locator     The SAX <CODE>Locator</CODE> object
     */
    public void setDocumentLocator(Locator locator) {
    }

    /**
     * Ignores the SAX event.
     *
     * @throws  SAXException    Cannot be thrown since the method does nothing
     */
    public void startDocument() throws SAXException {
    }

    /**
     * Ignores the SAX event.
     *
     * @throws  SAXException    Cannot be thrown since the method does nothing
     */
    public void endDocument() throws SAXException {
    }

    /**
     * Forwards the SAX event to the serializer object.
     *
     * @param   prefix          The namespace prefix.
     * @param   uri             The namespace URI the prefix is mapped to.
     * @throws  SAXException    Error reported by the serializer's method
     */
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        serializer.startPrefixMapping(prefix, uri);
    }

    /**
     * Forwards the SAX event to the serializer object.
     *
     * @param   prefix          The namespace prefix.
     * @throws  SAXException    Error reported by the serializer's method
     */
    public void endPrefixMapping(String prefix) throws SAXException {
        serializer.endPrefixMapping(prefix);
    }

    /**
     * Forwards the SAX event to the serializer object.
     *
     * @param   namespaceURI    The element's namespace URI
     * @param   localName       The element's local name
     * @param   qualifiedName   The element's qualified name
     * @param   attributes      The element's attributes
     * @throws  SAXException    Error reported by the serializer's method
     */
    public void startElement(String namespaceURI, String localName,
            String qualifiedName, Attributes attributes) throws SAXException {
        serializer.startElement(
            namespaceURI, localName, qualifiedName, attributes);
    }

    /**
     * Forwards the SAX event to the serializer object.
     *
     * @param   namespaceURI    The element's namespace URI
     * @param   localName       The element's local name
     * @param   qualifiedName   The element's qualified name
     * @throws  SAXException    Error reported by the serializer's method
     */
    public void endElement(String namespaceURI, String localName,
            String qualifiedName) throws SAXException {
        serializer.endElement(namespaceURI, localName, qualifiedName);
    }

    /**
     * Forwards the SAX event to the serializer object.
     *
     * @param   ch              The characters from the XML document
     * @param   start           The start position in the array
     * @param   length          The number of characters to get from the array
     * @throws  SAXException    Error reported by the serializer's method
     */
    public void characters(char ch[], int start, int length)
            throws SAXException {
        serializer.characters(ch, start, length);
    }

    /**
     * Forwards the SAX event to the serializer object.
     *
     * @param   ch              The characters from the XML document
     * @param   start           The start position in the array
     * @param   length          The number of characters to get from the array
     * @throws  SAXException    Error reported by the serializer's method
     */
    public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException {
        serializer.characters(ch, start, length);
    }

    /**
     * Forwards the SAX event to the serializer object.
     *
     * @param   target          The target of the processing instruction.
     * @param   data            The data of the processing instruction.
     * @throws  SAXException    Error reported by the serializer's method
     */
    public void processingInstruction(String target, String data)
            throws SAXException {
        serializer.processingInstruction(target, data);
    }

    /**
     * Forwards the SAX event to the serializer object.
     *
     * @param   name            The name of an entity
     * @throws  SAXException    Error reported by the serializer's method
     */
    public void skippedEntity(String name) throws SAXException {
        serializer.skippedEntity(name);
    }

    /**
     * Ignores the SAX event.
     *
     * @param   name            The name of the document's root element.
     * @param   publicId        The public identifier of the external DTD
     * @param   systemId        The system identifier of the external DTD
     * @throws  SAXException    Cannot be thrown since the method does nothing
     */
    public void startDTD(String name, String publicId, String systemId)
            throws SAXException {
    }

    /**
     * Ignores the SAX event.
     *
     * @throws  SAXException    Cannot be thrown since the method does nothing
     */
    public void endDTD() throws SAXException {
    }

    /**
     * Forwards the SAX event to the serializer object.
     *
     * @param   name            The name of an entity
     * @throws  SAXException    Cannot be thrown since the method does nothing
     */
    public void startEntity(String name) throws SAXException {
        serializer.startEntity(name);
    }

    /**
     * Forwards the SAX event to the serializer object.
     *
     * @param   name            The name of an entity
     * @throws  SAXException    Cannot be thrown since the method does nothing
     */
    public void endEntity(String name) throws SAXException {
        serializer.endEntity(name);
    }

    /**
     * Forwards the SAX event to the serializer object.
     *
     * @throws  SAXException    Error reported by the serializer's method
     */
    public void startCDATA() throws SAXException {
        serializer.startCDATA();
    }

    /**
     * Forwards the SAX event to the serializer object.
     *
     * @throws  SAXException    Error reported by the serializer's method
     */
    public void endCDATA() throws SAXException {
        serializer.endCDATA();
    }

    /**
     * Forwards the SAX event to the serializer object.
     *
     * @param   ch              An array holding the characters in the comment
     * @param   start           The start position in the array
     * @param   length          The number of characters to get from the array
     * @throws  SAXException    Error reported by the serializer's method
     */
    public void comment(char ch[], int start, int length) throws SAXException {
        serializer.comment(ch, start, length);
    }

}
