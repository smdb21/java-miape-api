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

package com.devsphere.xml.taglib.process.tag;

import com.devsphere.xml.taglib.process.parse.Handler;
import com.devsphere.xml.taglib.process.parse.Worker;
import com.devsphere.xml.taglib.process.event.EndDocument;
import com.devsphere.xml.taglib.process.event.StartDocument;

import org.xml.sax.InputSource;

import javax.servlet.jsp.JspException;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

/**
 * Handler for the &lt;p:parse&gt; tag, which can be used to parse an XML
 * document. Its body may contain a &lt;p:start&gt; tag for processing the
 * <CODE>startDocument()</CODE> SAX event, followed by an &lt;p:element&gt;
 * tag for processing the document's root element, followed by an optional
 * &lt;p:end&gt; tag for processing the <CODE>endDocument()</CODE> SAX event.
 */
public class Parse extends ProcessSupport {
    private static final List ALLOWED_ANCESTORS = null;
    private static final boolean DEFAULT_VALIDATE = false;
    private static final boolean DEFAULT_IGNORE_SPACES = false;
    private InputSource source;
    private Worker thread;
    private String xmlExpr;
    private Object xml;
    private String systemIdExpr;
    private String systemId;
    private String validateExpr;
    private boolean validate;
    private String ignoreSpacesExpr;
    private boolean ignoreSpaces;

    /**
     * Initializes the fields of this tag handler.
     */
    protected void init() {
        super.init();
        source = null;
        thread = null;
        xmlExpr = null;
        xml = null;
        systemIdExpr = null;
        systemId = null;
        validateExpr = null;
        validate = DEFAULT_VALIDATE;
        ignoreSpacesExpr = null;
        ignoreSpaces = DEFAULT_IGNORE_SPACES;
    }

    /**
     * Returns the name of the handled tag
     *
     * @return  the name of the handled tag
     */
    public String getTagName() {
        return "parse";
    }

    /**
     * Returns the list of names of the tags that may contain the tag that
     * is handled by this class.
     *
     * @return  the list of names of the allowed ancestor tags
     *          or <CODE>null</CODE> if the tag context doesn't matter.
     */
    protected List getAllowedAncestors() {
        return ALLOWED_ANCESTORS;
    }

    /**
     * Sets the XML input source that must be parsed. The parameter must
     * be an expression that can be evaluated to one of the following types:
     * <UL>
     * <LI>org.xml.sax.InputSource
     * <LI>java.io.InputStream
     * <LI>java.io.Reader
     * <LI>java.lang.String
     * </UL>
     *
     * @param   xmlExpr             the expression that must evaluate to
     *                              an XML input source, input stream, reader
     *                              or string
     * @throws  JspException        to signal any error that might occur
     */
    public void setXml(String xmlExpr) throws JspException {
        this.xmlExpr = xmlExpr;
    }

    /**
     * Sets the system identifier of the XML input source that must be parsed.
     *
     * @param   systemIdExpr        the expression that will be evaluated
     * @throws  JspException        to signal any error that might occur
     */
    public void setSystemId(String systemIdExpr) throws JspException {
        this.systemIdExpr = systemIdExpr;
    }

    /**
     * Sets the flag indicating if the parser should validate the XML input
     * source.
     *
     * @param   validateExpr        the expression that will be evaluated
     * @throws  JspException        to signal any error that might occur
     */
    public void setValidate(String validateExpr) throws JspException {
        this.validateExpr = validateExpr;
    }

    /**
     * Sets the flag indicating if space character data should be ignored.
     *
     * @param   ignoreSpacesExpr    the expression that will be evaluated
     * @throws  JspException        to signal any error that might occur
     */
    public void setIgnoreSpaces(String ignoreSpacesExpr) throws JspException {
        this.ignoreSpacesExpr = ignoreSpacesExpr;
    }

    /**
     * Initializes the loop to process parsing events.
     *
     * @throws  JspException    to signal any tag validation error
     */
    protected void initLoop() throws JspException {
        super.initLoop();
        // Make sure that debug and logger are initialized
        getDebug();
        getLogger();
        // Get the input source
        if (xmlExpr != null) {
            xml = eval("xml", xmlExpr, Object.class);
            if (xml instanceof InputSource)
                source = (InputSource) xml;
            else {
                source = new InputSource();
                if (xml instanceof InputStream)
                    source.setByteStream((InputStream) xml);
                else if (xml instanceof Reader)
                    source.setCharacterStream((Reader) xml);
                else if (xml instanceof String)
                    source.setCharacterStream(new StringReader((String) xml));
                else
                    source = null;
            }
        }
        // Get the system ID
        if (systemIdExpr != null) {
            systemId = evalString("systemId", systemIdExpr);
            if (source == null)
                source = new InputSource();
            source.setSystemId(systemId);
        }
        // Get the validate flag
        if (validateExpr != null)
            validate = evalBoolean("validate", validateExpr);
        // Get the ignore spaces flag
        if (ignoreSpacesExpr != null)
            ignoreSpaces = evalBoolean("ignoreSpaces", ignoreSpacesExpr);
        // Check if the input source is available
        if (source == null)
            throw new JspException("No XML input source. "
                + "At least one of the attributes 'xml' and 'systemId' "
                + "must be specified in the 'parse' tag.");
        // Parse the input source
        handler = new Handler(ignoreSpaces);
        indentLevel = 0;
        thread = new Worker(source, handler, validate, getLogger(), pageContext);
        thread.start();
    }

    /**
     * Returns <CODE>true</CODE> if the current event is a
     * <CODE>StartDocument</CODE> instance.
     *
     * @return  <CODE>true</CODE> if the event loop should be started
     * @throws  JspException    to signal an error
     */
    protected boolean startLoop() throws JspException {
        if (getDebug())
            log("\n\r\n\r\n\r\n\r");
        if (getEvent())
            if (event instanceof StartDocument) {
                logEvent();
                return true;
            }
        if (thread.getException() != null)
            throw new JspException(thread.getException());
        return false;
    }

    /**
     * Returns <CODE>true</CODE> if the current event could be processed
     * by a sub-tag.
     *
     * @return  <CODE>true</CODE> if the event loop should be continued
     * @throws  JspException    to signal an error
     */
    protected boolean continueLoop() throws JspException {
        if (event instanceof EndDocument)
            handler.endParse();
        else {
            removeEvent();
            if (getEvent())
                if (event instanceof EndDocument && event.isConsumed())
                    handler.endParse();
                else {
                    logEvent();
                    return true;
                }
        }
        if (thread.getException() != null)
            throw new JspException(thread.getException());
        return false;
    }

    /**
     * Ends the parsing.
     */
    protected void finalizeLoop() {
        if (handler != null)
            handler.endParse();
        if (thread != null && thread.isAlive())
            thread.interrupt();
        super.finalizeLoop();
    }

}
