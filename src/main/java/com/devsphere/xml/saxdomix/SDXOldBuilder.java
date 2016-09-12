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

import com.devsphere.xml.saxdomix.helpers.DefaultSDXOldHelper;
import com.devsphere.xml.saxdomix.helpers.ElementStack;
import com.devsphere.xml.saxdomix.helpers.SDXOldHelper;

import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.HandlerBase;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * This class provides the mixed SAX 1.0 - DOM Level 1 parsing functionality.
 * It needs JAXP 1.0 support.
 *
 * <P><IMG SRC="ParsingModes.gif" WIDTH="780" HEIGHT="440">
 *
 * <P>Each builder object needs from the application a SAX document
 * handler (<CODE>org.xml.sax.DocumentHandler</CODE>) and a controller
 * (<CODE>com.devsphere.xml.saxdomix.SDXOldController</CODE>). Any builder
 * also has an internal <CODE>wantDOM</CODE> flag.
 *
 * <P>The application receives the SAX events via the document handler
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
 * <P>A DOM sub-tree is made of three types of DOM objects:
 * <UL>
 *    <LI><CODE>org.w3c.dom.Element</CODE>
 *    <LI><CODE>org.w3c.dom.Text</CODE>
 *    <LI><CODE>org.w3c.dom.ProcessingInstruction</CODE></UL>
 *
 * <P>These DOM objects are created with a helper object. In most cases
 * you'll let the framework to use its own <CODE>DefaultSDXOldHelper</CODE>,
 * but you may provide your own implementation of the <CODE>SDXOldHelper</CODE>
 * interface, which is declared in the
 * <CODE>com.devsphere.xml.saxdomix.helpers</CODE> package.
 *
 * <P>The entity references are expanded. The comments are ignored.
 * The character data of the CDATA sections is stored in
 * <CODE>org.w3c.dom.Text</CODE> nodes. The DOM sub-trees cannot contain
 * <CODE>org.w3c.dom.CDATASection</CODE> and <CODE>org.w3c.dom.Comment</CODE>
 * nodes because SAX 1.0 doesn't support lexical handlers, which report
 * the comments and the boundaries of the CDATA sections in SAX 2.0.
 *
 * <P>In conclusion, the builder is initially in SAX parsing mode, which means
 * that SAX events are forwarded to a document handler provided by the
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
 * build the sub-trees are passed to the application's document handler.
 *
 * <P><CODE>SDXOldBuilder</CODE> extends the <CODE>HandlerBase</CODE> SAX
 * class but overrides only the methods of the <CODE>DocumentHandler</CODE>
 * SAX interface. A SAX parser is needed in order to feed the builder
 * with SAX events. Some of these events will be forwarded to the application's
 * document handler and the others will be used to build the DOM sub-trees
 * as described above.
 *
 * <P>There are two ways to use <CODE>SDXOldBuilder</CODE>: you may pass it to
 * the <CODE>parse()</CODE> method of a JAXP <CODE>SAXParser</CODE> or you may
 * register it as document handler to a SAX <CODE>Parser</CODE> and then call
 * its <CODE>parse()</CODE> method. In the second case you could create
 * a JAXP <CODE>SAXParser</CODE> and then obtain the SAX <CODE>Parser</CODE>
 * using the <CODE>getParser()</CODE> method of the <CODE>SAXParser</CODE>
 * object.
 *
 * <P><B>Usage 1:</B>
 * <PRE>
 *    org.xml.sax.DocumentHandler handler = ...
 *    SDXOldController controller = ...
 *    org.xml.sax.HandlerBase builder
 *        = new SDXOldBuilder(handler, controller);
 *    javax.xml.parsers.SAXParser saxParser = ...
 *    java.io.InputStream in = ...
 *    saxParser.parse(in, builder);</PRE>
 *
 * <P>If the default error handling or entity resolving mechanisms
 * don't suit the needs of your application or you need information about
 * unparsed entities and notations, then you should subclass
 * <CODE>SDXOldBuilder</CODE> and override the methods defined
 * by the <CODE>ErrorHandler</CODE>, <CODE>EntityResolver</CODE>
 * and <CODE>DTDHandler</CODE> SAX interfaces.
 *
 * <P><B>Usage 2:</B>
 * <PRE>
 *    org.xml.sax.DocumentHandler handler = ...
 *    SDXOldController controller = ...
 *    org.xml.sax.DocumentHandler builder
 *        = new SDXOldBuilder(handler, controller);
 *    org.xml.sax.Parser parser = ...
 *    parser.setDocumentHandler(builder);
 *    parser.setErrorHandler(...);
 *    org.xml.sax.InputSource in = ...
 *    parser.parse(in);</PRE>
 *
 * <P>In this case, you should always register an error handler. (If you don't
 * have one then register a <CODE>HandlerBase</CODE> as error handler.)
 *
 * <P>If the default entity resolving mechanism doesn't suit the needs
 * of your application and you need information about unparsed entities
 * and notations, then register an entity resolver and a DTD handler
 * to the parser.
 *
 * @see com.devsphere.xml.saxdomix.SDXOldController
 * @see com.devsphere.xml.saxdomix.helpers.SDXOldHelper
 * @see com.devsphere.xml.saxdomix.helpers.DefaultSDXOldHelper
 */
public class SDXOldBuilder extends HandlerBase {
    /** The SAX document handler provided by the application */
    protected DocumentHandler handler;

    /** The controller provided by the application */
    protected SDXOldController controller;

    /** The helper object used as factory for DOM nodes */
    protected SDXOldHelper helper;

    /** The flag that indicates the current parsing mode
        (false means <CODE>SAX</CODE>; <CODE>true</CODE> means DOM) */
    protected boolean wantDOM;

    /** A stack of DOM <CODE>Element</CODE> nodes */
    protected ElementStack stack;

    /**
     * Creates a builder that forwards the SAX events to the given handler,
     * switches to DOM when the <CODE>wantDOM()</CODE> method of the given
     * controller returns true and also passes the DOM sub-trees to the
     * controller's <CODE>handleDOM()</CODE> method. The created builder
     * will use a <CODE>DefaultSDXOldHelper</CODE> to create the DOM nodes.
     *
     * @param   handler     The application's SAX <CODE>DocumentHandler</CODE>
     * @param   controller  The application's <CODE>SDXController</CODE>
     * @throws  NullPointerException
     *                      If one of the parameters is <CODE>null</CODE>
     */
    public SDXOldBuilder(DocumentHandler handler, SDXOldController controller) {
        if (handler == null)
            throw new NullPointerException("handler cannot be null");
        if (controller == null)
            throw new NullPointerException("controller cannot be null");
        this.handler = handler;
        this.controller = controller;
    }

    /**
     * Creates a builder that forwards the SAX events to the given handler,
     * switches to DOM when the <CODE>wantDOM()</CODE> method of the given
     * controller returns true and also passes the DOM sub-trees to the
     * controller's <CODE>handleDOM()</CODE> method. The created builder
     * will use the given helper to create the DOM nodes.
     *
     * @param   handler     The application's SAX <CODE>DocumentHandler</CODE>
     * @param   controller  The application's <CODE>SDXController</CODE>
     * @param   helper      The application's optional <CODE>SDXOldHelper</CODE>
     * @throws  NullPointerException
     *                      If one of the parameters is <CODE>null</CODE>
     */
    public SDXOldBuilder(DocumentHandler handler, SDXOldController controller,
            SDXOldHelper helper) {
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
            helper = new DefaultSDXOldHelper();

        // Initially, we are in SAX mode
        wantDOM = false;

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
     * @param   name            The element's name
     * @param   attributes      The element's attributes
     * @throws  SAXException    Error reported by the handler's method
     */
    public void startElement(String name, AttributeList attributes)
            throws SAXException {
        if (!wantDOM) {
            // We are in SAX mode. See if the controller wants to switch to DOM
            wantDOM = controller.wantDOM(name, attributes);

            if (!wantDOM) {
                // We remained in SAX mode. Forward the SAX event and return
                handler.startElement(name, attributes);
                return;
            }

            // We've just entered in DOM mode. Create a new element stack
            stack = new ElementStack();
        }

        // We are in DOM mode. Create a DOM Element and add it to the sub-tree
        Element element = helper.createElement(name, attributes, stack.peek());

        // Push the element on the stack to be used as parent for its contents
        stack.push(element);
    }

    /**
     * Calls the method with the same name of the handler object
     * or passes the DOM sub-trees to the controller's <CODE>handleDOM()</CODE>
     * method depending on the value of the <CODE>wantDOM</CODE> flag.
     *
     * @param   name            The element's name
     * @throws  SAXException    Error reported by the handler's method
     */
    public void endElement(String name) throws SAXException {
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
            handler.endElement(name);
    }

    /**
     * Calls the method with the same name of the handler object
     * or creates a DOM <CODE>Text</CODE> node depending on the value
     * of the <CODE>wantDOM</CODE> flag.
     *
     * @param   ch              The characters from the XML document
     * @param   start           The start position in the array
     * @param   length          The number of characters to get from the array
     * @throws  SAXException    Error reported by the handler's method
     */
    public void characters(char ch[], int start, int length)
            throws SAXException {
        if (wantDOM)
            // We are in DOM mode. Create a Text node and add it to the sub-tree
            helper.createTextNode(new String(ch, start, length), stack.peek());
        else
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

}
