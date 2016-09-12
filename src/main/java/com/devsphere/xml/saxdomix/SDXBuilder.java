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

package com.devsphere.xml.saxdomix;

import com.devsphere.xml.saxdomix.helpers.DefaultSDXHelper;
import com.devsphere.xml.saxdomix.helpers.ElementStack;
import com.devsphere.xml.saxdomix.helpers.SDXHelper;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * This class provides the mixed SAX 2.0 - DOM Level 2 parsing functionality.
 * It needs JAXP 1.1 support. TrAX support isn't necessary.
 *
 * <P><IMG SRC="ParsingModes.gif" WIDTH="780" HEIGHT="440">
 *
 * <P>Each builder object needs from the application a SAX content
 * handler (<CODE>org.xml.sax.ContentHandler</CODE>) and a controller
 * (<CODE>com.devsphere.xml.saxdomix.SDXController</CODE>). Any builder
 * also has an internal <CODE>wantDOM</CODE> flag.
 *
 * <P>The application receives the SAX events via the content handler
 * as long as the <CODE>wantDOM</CODE> flag is <CODE>false</CODE>.
 * During the SAX parsing, the builder object invokes the controller's
 * <CODE>wantDOM()</CODE> method for each <CODE>startElement()</CODE>
 * parsing event. If <CODE>wantDOM()</CODE> returns <CODE>false</CODE>
 * the SAX parsing mode continues. When <CODE>wantDOM()</CODE> returns
 * <CODE>true</CODE> the value of the internal <CODE>wantDOM</CODE> flag
 * is switched to <CODE>true</CODE> and the builder starts creating a DOM
 * sub-tree from the SAX events, which aren't forwarded to the application
 * anymore.
 *
 * <P>The building of the sub-tree ends after the <CODE>endElement()</CODE>
 * event that corresponds to the <CODE>startElement()</CODE> event whose
 * associated <CODE>wantDOM()</CODE> call returned <CODE>true</CODE>.
 * The DOM sub-tree, whose root is an <CODE>org.w3c.dom.Element</CODE>
 * object is passed to the controller's <CODE>handleDOM()</CODE> method.
 * After this, the <CODE>wantDOM</CODE> flag is switched automatically to
 * <CODE>false</CODE> re-enabling the SAX parsing. The application will
 * receive SAX events until the controller's <CODE>wantDOM()</CODE> returns
 * <CODE>true</CODE> for another element or the end of the parsed document
 * is reached.
 *
 * <P>A DOM sub-tree is made of five types of DOM objects:
 * <UL>
 *    <LI><CODE>org.w3c.dom.Element</CODE>
 *    <LI><CODE>org.w3c.dom.Text</CODE>
 *    <LI><CODE>org.w3c.dom.CDATASection</CODE>
 *    <LI><CODE>org.w3c.dom.Comment</CODE>
 *    <LI><CODE>org.w3c.dom.ProcessingInstruction</CODE></UL>
 *
 * <P>These DOM objects are created with a helper object. In most cases
 * you'll let the framework to use its own <CODE>DefaultSDXHelper</CODE>,
 * but you may provide your own implementation of the <CODE>SDXHelper</CODE>
 * interface, which is declared in the
 * <CODE>com.devsphere.xml.saxdomix.helpers</CODE> package.
 *
 * <P>The entity references are expanded.
 *
 * <P>If the class of the content handler object implements
 * <CODE>org.xml.sax.ext.LexicalHandler</CODE>, the application will also
 * receive <CODE>comment()</CODE>, <CODE>startCDATA()</CODE> and
 * <CODE>endCDATA()</CODE> SAX lexical events during the SAX parsing mode.
 *
 * <P>In conclusion, the builder is initially in SAX parsing mode, which means
 * that SAX events are forwarded to a content handler provided by the
 * application. During the SAX parsing, the <CODE>wantDOM()</CODE> method of
 * a controller object is called for each element start.
 * When <CODE>wantDOM()</CODE> returns <CODE>true</CODE>, the builder retains
 * the SAX events and uses them to build a DOM sub-tree, which is then passed
 * for handling to the <CODE>handleDOM()</CODE> method of the controller object.
 * During the DOM building, the <CODE>wantDOM()</CODE> method is NOT called.
 * The builder switches to the SAX parsing mode automatically when the DOM
 * sub-tree is completed. This process is repeated until the end of the parsed
 * document, which means that multiple DOM sub-trees can be constructed during
 * the parsing of a single XML document. All SAX events that aren't used to
 * build the sub-trees are passed to the application's content handler.
 *
 * <P><CODE>SDXBuilder</CODE> extends the <CODE>DefaultHandler</CODE> SAX helper
 * class but overrides only the methods of the <CODE>ContentHandler</CODE>
 * SAX interface. It also implements the <CODE>LexicalHandler</CODE>
 * SAX extension. A SAX parser is needed in order to feed the builder
 * with SAX events. Some of these events will be forwarded to the application's
 * content handler and the others will be used to build the DOM sub-trees
 * as described above.
 *
 * <P>There are two ways to use <CODE>SDXBuilder</CODE>: you may pass it to
 * the <CODE>parse()</CODE> method of a JAXP <CODE>SAXParser</CODE> or you may
 * register it as content handler to a SAX <CODE>XMLReader</CODE> and then call
 * its <CODE>parse()</CODE> method. In the second case you could create
 * a JAXP <CODE>SAXParser</CODE> and then obtain the SAX <CODE>XMLReader</CODE>
 * using the <CODE>getXMLReader()</CODE> method of the <CODE>SAXParser</CODE>
 * object.
 *
 * <P><B>Usage 1:</B>
 * <PRE>
 *    org.xml.sax.ContentHandler handler = ...
 *    SDXController controller = ...
 *    org.xml.sax.helpers.DefaultHandler builder
 *        = new SDXBuilder(handler, controller);
 *    javax.xml.parsers.SAXParser saxParser = ...
 *    saxParser.getXMLReader().setProperty(
 *        "http://xml.org/sax/properties/lexical-handler", builder);
 *    java.io.InputStream in = ...
 *    saxParser.parse(in, builder);</PRE>
 *
 * <P>If the default error handling or entity resolving mechanisms
 * don't suit the needs of your application or you need information about
 * unparsed entities and notations, then you should subclass
 * <CODE>SDXBuilder</CODE> and override the methods defined
 * by the <CODE>ErrorHandler</CODE>, <CODE>EntityResolver</CODE>
 * and <CODE>DTDHandler</CODE> SAX interfaces.
 *
 * <P><B>Usage 2:</B>
 * <PRE>
 *    org.xml.sax.ContentHandler handler = ...
 *    SDXController controller = ...
 *    org.xml.sax.ContentHandler builder
 *        = new SDXBuilder(handler, controller);
 *    org.xml.sax.XMLReader xmlReader = ...
 *    xmlReader.setContentHandler(builder);
 *    xmlReader.setProperty(
 *        "http://xml.org/sax/properties/lexical-handler", builder);
 *    xmlReader.setErrorHandler(...);
 *    org.xml.sax.InputSource in = ...
 *    xmlReader.parse(in);</PRE>
 *
 * <P>In this case, you should always register an error handler. (If you don't
 * have one then register a <CODE>DefaultHandler</CODE> as error handler.)
 *
 * <P>If the default entity resolving mechanism doesn't suit the needs
 * of your application and you need information about unparsed entities
 * and notations, then register an entity resolver and a DTD handler
 * to the XML reader.
 *
 * @see com.devsphere.xml.saxdomix.SDXController
 * @see com.devsphere.xml.saxdomix.helpers.SDXHelper
 * @see com.devsphere.xml.saxdomix.helpers.DefaultSDXHelper
 */
public class SDXBuilder extends DefaultHandler implements LexicalHandler {
    /** The SAX content handler provided by the application */
    protected ContentHandler handler;

    /** The controller provided by the application */
    protected SDXController controller;

    /** A cast to the content handler or null if its class doesn't implement
        <CODE>LexicalHandler</CODE> */
    protected LexicalHandler lexicalHandler;

    /** The helper object used as factory for DOM nodes */
    protected SDXHelper helper;

    /** The flag that indicates the current parsing mode
        (false means <CODE>SAX</CODE>; <CODE>true</CODE> means DOM) */
    protected boolean wantDOM;

    /** A stack of DOM <CODE>Element</CODE> nodes */
    protected ElementStack stack;

    /** A flag indicating that we just entered inside a CDATA section */
    protected boolean newCDATA;

    /** A flag indicating that we are inside a CDATA section */
    protected boolean inCDATA;

    /**
     * Creates a builder that forwards the SAX events to the given handler,
     * switches to DOM when the <CODE>wantDOM()</CODE> method of the given
     * controller returns true and also passes the DOM sub-trees to the
     * controller's <CODE>handleDOM()</CODE> method. The created builder
     * will use a <CODE>DefaultSDXHelper</CODE> to create the DOM nodes.
     *
     * @param   handler     The application's SAX <CODE>ContentHandler</CODE>
     * @param   controller  The application's <CODE>SDXController</CODE>
     * @throws  NullPointerException
     *                      If one of the parameters is <CODE>null</CODE>
     */
    public SDXBuilder(ContentHandler handler, SDXController controller) {
        if (handler == null)
            throw new NullPointerException("handler cannot be null");
        if (controller == null)
            throw new NullPointerException("controller cannot be null");
        this.handler = handler;
        this.controller = controller;
        if (handler instanceof LexicalHandler)
            lexicalHandler = (LexicalHandler) handler;
    }

    /**
     * Creates a builder that forwards the SAX events to the given handler,
     * switches to DOM when the <CODE>wantDOM()</CODE> method of the given
     * controller returns true and also passes the DOM sub-trees to the
     * controller's <CODE>handleDOM()</CODE> method. The created builder
     * will use the given helper to create the DOM nodes.
     *
     * @param   handler     The application's SAX <CODE>ContentHandler</CODE>
     * @param   controller  The application's <CODE>SDXController</CODE>
     * @param   helper      The application's optional <CODE>SDXHelper</CODE>
     * @throws  NullPointerException
     *                      If one of the parameters is <CODE>null</CODE>
     */
    public SDXBuilder(ContentHandler handler, SDXController controller,
            SDXHelper helper) {
        this(handler, controller);
        if (helper == null)
            throw new NullPointerException("helper cannot be null");
        this.helper = helper;
    }

    /**
     * Calls the method with the same name of the handler object passing
     * the given locator as parameter.
     *
     * @param   locator     The SAX <CODE>Locator</CODE> object
     */
    public void setDocumentLocator(Locator locator) {
        handler.setDocumentLocator(locator);
    }

    /**
     * Calls the method with the same name of the handler object
     * and initializes some of the fields of this object.
     *
     * @throws  SAXException    Error reported by the handler's method
     */
    public void startDocument() throws SAXException {
        if (helper == null)
            // We'll need a helper to create the DOM nodes
            helper = new DefaultSDXHelper();

        // Initially, we are in SAX mode
        wantDOM = false;

        // Initially, we aren't inside a CDATA section
        inCDATA = false;

        // We are in SAX mode. Forward the SAX event
        handler.startDocument();
    }

    /**
     * Calls the method with the same name of the handler object.
     *
     * @throws  SAXException    Error reported by the handler's method
     */
    public void endDocument() throws SAXException {
        // We must be in SAX mode. Forward the SAX event
        handler.endDocument();
    }

    /**
     * Calls the method with the same name of the handler object
     * or creates a DOM <CODE>Element</CODE> node depending on the value
     * of the <CODE>wantDOM</CODE> flag.
     *
     * @param   namespaceURI    The element's namespace URI
     * @param   localName       The element's local name
     * @param   qualifiedName   The element's qualified name
     * @param   attributes      The element's attributes
     * @throws  SAXException    Error reported by the handler's method
     */
    public void startElement(String namespaceURI, String localName,
            String qualifiedName, Attributes attributes) throws SAXException {
        if (!wantDOM) {
            // We are in SAX mode. See if the controller wants to switch to DOM
            wantDOM = controller.wantDOM(
                namespaceURI, localName, qualifiedName, attributes);

            if (!wantDOM) {
                // We remained in SAX mode. Forward the SAX event and return
                handler.startElement(
                    namespaceURI, localName, qualifiedName, attributes);
                return;
            }

            // We've just entered in DOM mode. Create a new element stack
            stack = new ElementStack();
        }

        // We are in DOM mode. Create a DOM Element and add it to the sub-tree
        Element element = helper.createElement(
            namespaceURI, qualifiedName, attributes, stack.peek());

        // Push the element on the stack to be used as parent for its contents
        stack.push(element);
    }

    /**
     * Calls the method with the same name of the handler object
     * or passes the DOM sub-trees to the controller's <CODE>handleDOM()</CODE>
     * method depending on the value of the <CODE>wantDOM</CODE> flag.
     *
     * @param   namespaceURI    The element's namespace URI
     * @param   localName       The element's local name
     * @param   qualifiedName   The element's qualified name
     * @throws  SAXException    Error reported by the handler's method
     */
    public void endElement(String namespaceURI, String localName,
            String qualifiedName) throws SAXException {
        if (wantDOM) {
            // We are in DOM mode.
            // Get the element created by the corresponding startElement()
            Element element = stack.pop();

            if (stack.isEmpty()) {
                // The DOM sub-tree is completed
                // We don't need the stack anymore
                stack = null;

                // Make the sub-tree look like a regular DOM tree
                // (The sub-tree's root becomes "document element")
                element.getOwnerDocument().appendChild(element);

                // Ask the controller to handle the DOM sub-tree
                controller.handleDOM(element);

                // Don't keep any reference to the DOM sub-tree
                // so that it can be garbage collected
                element.getOwnerDocument().removeChild(element);

                // Return to the SAX mode
                wantDOM = false;
            }
        } else
            // We are in SAX mode. Forward the SAX event
            handler.endElement(namespaceURI, localName, qualifiedName);
    }

    /**
     * Calls the method with the same name of the handler object
     * or creates a DOM character data node depending on the value
     * of the <CODE>wantDOM</CODE> flag.
     *
     * @param   ch              The characters from the XML document
     * @param   start           The start position in the array
     * @param   length          The number of characters to get from the array
     * @throws  SAXException    Error reported by the handler's method
     */
    public void characters(char ch[], int start, int length)
            throws SAXException {
        if (wantDOM) {
            // We are in DOM mode.
            String data = new String(ch, start, length);
            if (inCDATA) {
                // Create a DOM CDATASection and add it to the sub-tree
                helper.createCDATASection(data, newCDATA, stack.peek());
                newCDATA = false;
            } else
                // Create a DOM Text node and add it to the sub-tree
                helper.createTextNode(data, stack.peek());
        } else
            // We are in SAX mode. Forward the SAX event
            handler.characters(ch, start, length);
    }

    /**
     * Calls the method with the same name of the handler object
     * or creates a DOM <CODE>ProcessingInstruction</CODE> node depending on
     * the value of the <CODE>wantDOM</CODE> flag.
     *
     * @param   target          The target of the processing instruction.
     * @param   data            The data of the processing instruction.
     * @throws  SAXException    Error reported by the handler's method
     */
    public void processingInstruction(String target, String data)
            throws SAXException {
        if (wantDOM)
            // We are in DOM mode. Create a DOM PI and add it to the sub-tree
            helper.createProcessingInstruction(target, data, stack.peek());
        else
            // We are in SAX mode. Forward the SAX event
            handler.processingInstruction(target, data);
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
     * Calls the method with the same name of the handler object
     * if the value of the <CODE>wantDOM</CODE> flag if <CODE>false</CODE>.
     * The <CODE>inCDATA</CODE> and <CODE>newCDATA</CODE> flags are set to
     * <CODE>true</CODE>.
     *
     * @throws  SAXException    Error reported by the handler's method
     */
    public void startCDATA() throws SAXException {
        // We've just entered in a CDATA section
        inCDATA = true;
        newCDATA = true;

        if (!wantDOM && lexicalHandler != null)
            // We are in SAX mode. Forward the SAX event
            lexicalHandler.startCDATA();
    }

    /**
     * Calls the method with the same name of the handler object
     * if the value of the <CODE>wantDOM</CODE> flag if <CODE>false</CODE>.
     * The <CODE>inCDATA</CODE> flag is set to <CODE>false</CODE>.
     *
     * @throws  SAXException    Error reported by the handler's method
     */
    public void endCDATA() throws SAXException {
        if (!wantDOM && lexicalHandler != null)
            // We are in SAX mode. Forward the SAX event
            lexicalHandler.endCDATA();

        // We've just exited from a CDATA section
        inCDATA = false;
    }

    /**
     * Calls the method with the same name of the handler object
     * or creates a DOM <CODE>Comment</CODE> node depending on the value
     * of the <CODE>wantDOM</CODE> flag.
     *
     * @param   ch              An array holding the characters in the comment
     * @param   start           The start position in the array
     * @param   length          The number of characters to get from the array
     * @throws  SAXException    Error reported by the handler's method
     */
    public void comment(char ch[], int start, int length)
            throws SAXException {
        if (wantDOM)
            // We are in DOM mode. Create a Comment and add it to the sub-tree
            helper.createComment(new String(ch, start, length), stack.peek());
        else if (lexicalHandler != null)
            // We are in SAX mode. Forward the SAX event
            lexicalHandler.comment(ch, start, length);
    }

}
