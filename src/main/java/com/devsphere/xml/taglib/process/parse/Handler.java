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

package com.devsphere.xml.taglib.process.parse;

import com.devsphere.xml.taglib.process.event.Characters;
import com.devsphere.xml.taglib.process.event.EndDocument;
import com.devsphere.xml.taglib.process.event.EndElement;
import com.devsphere.xml.taglib.process.event.HandleDOM;
import com.devsphere.xml.taglib.process.event.EventSupport;
import com.devsphere.xml.taglib.process.event.Processing;
import com.devsphere.xml.taglib.process.event.StartDocument;
import com.devsphere.xml.taglib.process.event.StartElement;

import com.devsphere.xml.saxdomix.SDXController;

import org.w3c.dom.Element;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This handler receives the SAX events as method calls and creates
 * event objects. This is useful when the parsing is performed in one thread
 * and the events must be processed/consumed by another thread. In the case
 * of this tag library, the tag handlers consume the event objects in the
 * JSP thread. The parsing must be done in a separate thread. Otherwise,
 * the parsing would block the JSP thread until the whole document is parsed.
 * The separate parsing thread allows the tag library to parse the document
 * and process the events in parallel.
 */
public class Handler extends DefaultHandler
        implements LexicalHandler, SDXController {
    private boolean ignoreSpaces;
    private boolean parseEnded;
    private EventSupport currentEvent;
    private int indentLevel;

    /**
     * Initializes the handler.
     *
     * @param   ignoreSpaces    a flag indicating if white space character data
     *                          should be ignored
     */
    public Handler(boolean ignoreSpaces) {
        this.ignoreSpaces = ignoreSpaces;
        parseEnded = false;
        currentEvent = null;
        indentLevel = 0;
    }

    /**
     * Makes an event available for processing. This becomes the new current
     * event that is returned by <CODE>getEvent()</CODE>. Before that, this
     * method waits until the old current event, if any, is consumed.
     *
     * @param   event   the new current event
     * @return          <CODE>true</CODE> if the event was accepted or
     *                  <CODE>false</CODE> if the parsing ended before receiving
     *                  this event
     * @throws  Ender   to interrupt the parsing when <CODE>parseEnded</CODE>
     *                  is <CODE>true</CODE> and the parsing thread must
     *                  be stopped
     */
    protected synchronized boolean addEvent(EventSupport event) throws Ender {
        if (parseEnded) {
            // The parse thread is still alive and parse events are still
            // generated, but parseEnded is true. This means that endParse()
            // was called from the JSP thread probably because an error ocurred
            // during the XML processing.
            throw new Ender();
        }
        while (!parseEnded && currentEvent != null)
            try {
                // Wait until the current event is consumend
                // or the parsing is interrupted
                wait();
            } catch (InterruptedException e) {
                // The JSP thread called interrupt() on the parsing thread.
                // This means that the parsing must end.
                endParse();
                throw new Ender();
            }
        if (!parseEnded && currentEvent == null && event != null) {
            // The given event becomes the new current event
            currentEvent = event;
            // Notify the JSP thread that a new event is available
            notifyAll();
            return true;
        } else
            return false;
    }

    /**
     * Returns the current parsing event. Successive calls on this method
     * will return the same event until the event is consumed or the parsing
     * is interrupted.
     *
     * @return      the current event or <CODE>null</CODE> if the parsing ended
     */
    public synchronized EventSupport getEvent() {
        while (!parseEnded && currentEvent == null)
            try {
                // Wait until an event becomes available
                // or the parsing is interrupted
                wait();
            } catch (InterruptedException e) {
                // Somebody interrupted the JSP thread
                endParse();
                return null;
            }
        // Return the current event
        return currentEvent;
    }

    /**
     * Removes the current event to make room for a new event.
     *
     * @param   event   this must coincide with the current event in order
     *                  to be removed
     * @return  <CODE>true</CODE> if the current event was removed
     */
    public synchronized boolean removeEvent(EventSupport event) {
        if (currentEvent == event) {
            // Remove the current event so that a new event can be added
            currentEvent = null;
            // Notify the parse thread that a new event can be added
            notifyAll();
            return true;
        } else
            return false;
    }

    /**
     * Sets the <CODE>parseEnded</CODE> to <CODE>true</CODE>. Next time a SAX
     * event is received for handling an <CODE>Ender</CODE> exception will
     * be thrown in order to interrupt the parsing.
     */
    public synchronized void endParse() {
        parseEnded = true;
        currentEvent = null;
        notifyAll();
    }

    /**
     * Returns <CODE>true</CODE> when the application wants to receive
     * a DOM sub-tree for handling.
     *
     * <P>During the SAX parsing, a builder object invokes the
     * <CODE>wantDOM()</CODE> method for each <CODE>startElement()</CODE>
     * parsing event. If <CODE>wantDOM()</CODE> returns <CODE>false</CODE>
     * the SAX parsing mode continues. When <CODE>wantDOM()</CODE> returns
     * <CODE>true</CODE> the builder enters in DOM parsing mode and starts
     * creating a DOM sub-tree from all SAX events between the current
     * <CODE>startElement()</CODE> and the corresponding
     * <CODE>endElement()</CODE>.
     *
     * @param   namespaceURI    The element's namespace URI
     * @param   localName       The element's local name
     * @param   qualifiedName   The element's qualified name
     * @param   attributes      The element's attributes
     * @return                  A boolean value indicating whether the builder
     *                          must enter in DOM parsing mode or must remain
     *                          in SAX parsing mode
     * @throws  SAXException    If an error must be signaled
     */
    public boolean wantDOM(String namespaceURI, String localName,
            String qualifiedName, Attributes attributes) throws SAXException {
        StartElement event = new StartElement(indentLevel,
            namespaceURI, localName, qualifiedName, attributes);
        addEvent(event);
        // Wait until the element event is consumed
        addEvent(null);
        if (event.getWantDOM())
            return true;
        return false;
    }

    /**
     * Receives the DOM sub-trees for handling.
     *
     * <P>After <CODE>wantDOM()</CODE> returns <CODE>true</CODE>, the builder
     * constructs a DOM sub-tree from SAX events and passes it to the
     * <CODE>handleDOM()</CODE> method. After handling, the builder
     * returns to the SAX parsing mode.
     *
     * @param   element         The root of the DOM sub-tree
     * @throws  SAXException    If an error must be signaled
     */
    public void handleDOM(Element element) throws SAXException {
        HandleDOM event = new HandleDOM(indentLevel, element);
        addEvent(event);
    }

    /**
     * Does nothing.
     *
     * @param   locator     The SAX <CODE>Locator</CODE> object
     */
    public void setDocumentLocator(Locator locator) {
    }

    /**
     * Adds a <CODE>StartDocument</CODE> event.
     *
     * @throws  SAXException    Error reported by the handler's method
     */
    public void startDocument() throws SAXException {
        StartDocument event = new StartDocument(indentLevel);
        addEvent(event);
        indentLevel++;
    }

    /**
     * Adds an <CODE>EndDocument</CODE> event.
     *
     * @throws  SAXException    Error reported by the handler's method
     */
    public void endDocument() throws SAXException {
        indentLevel--;
        EndDocument event = new EndDocument(indentLevel);
        addEvent(event);
    }

    /**
     * Doesn't add any event because the <CODE>StartElement</CODE> event was
     * created and added by <CODE>wantDOM()</CODE>
     *
     * @param   namespaceURI    The element's namespace URI
     * @param   localName       The element's local name
     * @param   qualifiedName   The element's qualified name
     * @param   attributes      The element's attributes
     * @throws  SAXException    Error reported by the handler's method
     */
    public void startElement(String namespaceURI, String localName,
            String qualifiedName, Attributes attributes) throws SAXException {
        indentLevel++;
    }

    /**
     * Adds an <CODE>EndElement</CODE> event.
     *
     * @param   namespaceURI    The element's namespace URI
     * @param   localName       The element's local name
     * @param   qualifiedName   The element's qualified name
     * @throws  SAXException    Error reported by the handler's method
     */
    public void endElement(String namespaceURI, String localName,
            String qualifiedName) throws SAXException {
        indentLevel--;
        EndElement event = new EndElement(indentLevel,
            namespaceURI, localName, qualifiedName);
        addEvent(event);
    }

    /**
     * Adds a <CODE>Characters</CODE> event.
     *
     * @param   ch              The characters from the XML document
     * @param   start           The start position in the array
     * @param   length          The number of characters to get from the array
     * @throws  SAXException    Error reported by the handler's method
     */
    public void characters(char ch[], int start, int length)
            throws SAXException {
        String data = new String(ch, start, length);
        if (ignoreSpaces && data.trim().length() == 0)
            return;
        Characters event = new Characters(indentLevel, data);
        addEvent(event);
    }

    /**
     * Adds a <CODE>Processing</CODE> event.
     *
     * @param   target          The target of the processing instruction.
     * @param   data            The data of the processing instruction.
     * @throws  SAXException    Error reported by the handler's method
     */
    public void processingInstruction(String target, String data)
            throws SAXException {
        Processing event = new Processing(indentLevel, target, data);
        addEvent(event);
    }

    /**
     * Does nothing.
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
     * Does nothing.
     *
     * @throws  SAXException    Cannot be thrown since the method does nothing
     */
    public void endDTD() throws SAXException {
    }

    /**
     * Does nothing.
     *
     * @param   name            The name of an entity
     * @throws  SAXException    Cannot be thrown since the method does nothing
     */
    public void startEntity(String name) throws SAXException {
    }

    /**
     * Does nothing.
     *
     * @param   name            The name of an entity
     * @throws  SAXException    Cannot be thrown since the method does nothing
     */
    public void endEntity(String name) throws SAXException {
    }

    /**
     * Does nothing.
     *
     * @throws  SAXException    Error reported by the handler's method
     */
    public void startCDATA() throws SAXException {
    }

    /**
     * Does nothing.
     *
     * @throws  SAXException    Error reported by the handler's method
     */
    public void endCDATA() throws SAXException {
    }

    /**
     * Does nothing.
     *
     * @param   ch              An array holding the characters in the comment
     * @param   start           The start position in the array
     * @param   length          The number of characters to get from the array
     * @throws  SAXException    Error reported by the handler's method
     */
    public void comment(char ch[], int start, int length)
            throws SAXException {
    }

}
