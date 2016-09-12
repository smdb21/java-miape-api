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

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.dom.DOMResult;

/**
 * This default factory for DOM sub-trees called helper is used
 * by <CODE>SDXBuilderT</CODE>.
 *
 * @see com.devsphere.xml.saxdomix.SDXBuilderT
 * @see com.devsphere.xml.saxdomix.helpers.SDXHelperT
 */
public class DefaultSDXHelperT extends DefaultSDXHelper
        implements SDXHelperT {
    /** Factory for <CODE>TransformerHandler</CODE> objects */
    protected static SAXTransformerFactory traxFactory;

    static {
        try {
            // Crerate the TrAX factory
            traxFactory
                = (SAXTransformerFactory) TransformerFactory.newInstance();
        } catch (TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        }
    }

    /** Takes SAX events as input and produces a DOM sub-tree */
    protected TransformerHandler parser;

    /** Container for the current DOM sub-tree */
    protected DOMResult result;

    /**
     * Creates a new helper.
     */
    public DefaultSDXHelperT() {
        super();
    }

    /**
     * Creates a DOM <CODE>DocumentFragment</CODE> node
     *
     * @return                  The created DOM <CODE>DocumentFragment</CODE>
     */
    protected DocumentFragment createDocumentFragment() {
        return document.createDocumentFragment();
    }

    /**
     * Prepares this helper for building a DOM sub-tree from SAX events.
     * After calling this method, the user of this class may feed the
     * <CODE>ContentHandler</CODE> and <CODE>LexicalHandler</CODE> with
     * SAX events. Then, the <CODE>endBuilding()</CODE> method must be called
     * in order to obtain the root of the created DOM sub-tree.
     *
     * @throws  SAXException    If an error must be signaled
     */
    public void startBuilding() throws SAXException {
        try {
            // Create a DOM "parser" that builds a DOM sub-tree from SAX events
            parser = traxFactory.newTransformerHandler();
        } catch (TransformerConfigurationException e) {
            throw new SAXException(e);
        }

        // Create the container for the DOM sub-tree
        result = new DOMResult(createDocumentFragment());

        // Let the parser know where to store the DOM sub-tree
        parser.setResult(result);

        // Generate a startDocument() event.
        // Apache Xalan needs this for initialization
        parser.startDocument();
    }

    /**
     * This method must be called after the dispatching of the last SAX event
     * of the sequence that was used for building the current DOM sub-tree.
     * The SAX handlers may not receive events until the next call of
     * <CODE>startBuilding()</CODE>, which will mark the beginning of the
     * construction of another DOM sub-tree.
     *
     * @return                  The root of the created DOM sub-tree
     * @throws  SAXException    If an error must be signaled
     */
    public Element endBuilding() throws SAXException {
        // Generate an endDocument() event.
        // Apache Xalan needs this for finalization
        parser.endDocument();

        // Get the fragment wrapped by the DOMResult object
        DocumentFragment fragment = (DocumentFragment) result.getNode();

        // Dispose the result and parser objects
        result = null;
        parser = null;

        // Return the root element of the DOM sub-tree
        return (Element) fragment.getFirstChild();
    }

    /**
     * Returns the SAX <CODE>ContentHandler</CODE> that should receive
     * the events between <CODE>startBuilding()</CODE> and
     * <CODE>endBuilding()</CODE>.
     *
     * @return                  The <CODE>ContentHandler</CODE> of this helper
     */
    public ContentHandler getContentHandler() {
        return parser;
    }

    /**
     * Returns the SAX <CODE>LexicalHandler</CODE> that should receive
     * the events between <CODE>startBuilding()</CODE> and
     * <CODE>endBuilding()</CODE>.
     *
     * @return                  The <CODE>LexicalHandler</CODE> of this helper
     */
    public LexicalHandler getLexicalHandler() {
        return parser;
    }

}
