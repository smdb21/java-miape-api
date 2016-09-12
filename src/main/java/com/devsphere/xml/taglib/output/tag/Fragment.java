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

package com.devsphere.xml.taglib.output.tag;

import org.w3c.dom.Node;
import org.w3c.dom.Document;

import org.xml.sax.SAXException;

import javax.servlet.jsp.JspException;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Handler for the &lt;o:fragment&gt; tag that outputs a DOM fragment/sub-tree.
 */
public class Fragment extends OutputSupport {
    private static final List ALLOWED_ANCESTORS = Arrays.asList(new String[] {
        "document",
        "element"
    });
    private static final String METHOD_PROPERTY = "method";
    private static final String INDENT_PROPERTY = "indent";
    private static final String INDENT_AMOUNT_PROPERTY
        = "{http://xml.apache.org/xslt}indent-amount";
    protected String domExpr;
    protected Node dom;
    private String methodExpr;
    private String method;
    private String indentExpr;
    private int indent;

    /**
     * Initializes the fields of this tag handler.
     */
    protected void init() {
        super.init();
        domExpr = null;
        dom = null;
        methodExpr = null;
        method = null;
        indentExpr = null;
        indent = 0;
    }

    /**
     * Returns the name of the handled tag
     *
     * @return  the name of the handled tag
     */
    public String getTagName() {
        return "fragment";
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
     * Sets the DOM tree that must be serialized to the JSP output.
     *
     * @param   somExpr             the expression that will be evaluated
     * @throws  JspException        to signal any error that might occur
     */
    public void setDom(String domExpr) throws JspException {
        this.domExpr = domExpr;
    }

    /**
     * Sets the output method: html, xml, text.
     *
     * @param   methodExpr          the expression that will be evaluated
     * @throws  JspException        to signal any error that might occur
     */
    public void setMethod(String methodExpr) throws JspException {
        this.methodExpr = methodExpr;
    }

    /**
     * Sets the number of spaces used for indenting.
     *
     * @param   indentExpr          the expression that will be evaluated
     * @throws  JspException        to signal any error that might occur
     */
    public void setIndent(String indentExpr) throws JspException {
        this.indentExpr = indentExpr;
    }

    /**
     * Returns the output properties.
     *
     * @return                      the output properties
     * @throws  JspException        to signal any error that might occur
     */
    protected Properties getOutputProperties() throws JspException {
        Properties properties = new Properties();
        if (methodExpr != null) {
            method = evalString("method", methodExpr);
            properties.put(METHOD_PROPERTY, method);
        }
        if (indentExpr != null) {
            indent = evalInt("indent", indentExpr);
            if (indent > 0) {
                properties.put(INDENT_PROPERTY, "yes");
                properties.put(INDENT_AMOUNT_PROPERTY,
                    Integer.toString(indent));
            } else
                properties.put(INDENT_PROPERTY, "no");
        }
        return properties;
    }

    /**
     * Returns <CODE>true</CODE> if the <CODE>dom</CODE> attribute is present.
     *
     * @return  a flag indicating whether the body content of this tag should be
     *          ignored or evaluated
     */
    protected boolean shouldIgnoreContent() {
        return domExpr != null;
    }

    /**
     * Serializes the DOM tree.
     *
     * @throws  JspException    to signal an error
     * @throws  SAXException    to signal an error
     */
    protected void startOutput() throws JspException, SAXException {
        dom = (Node) eval("dom", domExpr, Node.class);
        if (dom instanceof Document)
            dom = ((Document) dom).getDocumentElement();
        Properties properties = getOutputProperties();
        properties.put("omit-xml-declaration", "yes");
        serializer.setOutputProperties(properties);
        serializer.write(dom);
    }

    /**
     * Does nothing.
     *
     * @throws  JspException    to signal an error
     * @throws  SAXException    to signal an error
     */
    protected void endOutput() throws JspException, SAXException {
    }

}
