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

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.servlet.jsp.JspException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Handler for the &lt;o:element&gt; tag that outputs an XML element.
 */
public class Element extends OutputSupport {
    public static final List ALLOWED_ANCESTORS = Arrays.asList(new String[] {
        "document",
        "element"
    });
    private String uriExpr;
    private String uri;
    private String nameExpr;
    private String name;
    private String qname;
    private String attrExpr;
    private AttributesImpl attr;
    private String emptyExpr;
    private boolean empty;

    /**
     * Initializes the fields of this tag handler.
     */
    protected void init() {
        super.init();
        uriExpr = null;
        uri = null;
        nameExpr = null;
        name = null;
        qname = null;
        attrExpr = null;
        attr = null;
        emptyExpr = null;
        empty = false;
    }

    /**
     * Returns the name of the handled tag
     *
     * @return  the name of the handled tag
     */
    public String getTagName() {
        return "element";
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
     * Sets the namespace URI of the element, which will be the value
     * of a <CODE>XMLNS</CODE> attribute.
     *
     * @param   uriExpr             the expression that will be evaluated
     * @throws  JspException        to signal any error that might occur
     */
    public void setUri(String uriExpr) throws JspException {
        this.uriExpr = uriExpr;
    }

    /**
     * Sets the name of the element.
     *
     * @param   nameExpr            the expression that will be evaluated
     * @throws  JspException        to signal any error that might occur
     */
    public void setName(String nameExpr) throws JspException {
        this.nameExpr = nameExpr;
    }

    /**
     * Sets the attributes of the element.
     * The value of the evaluated expression is expected to be
     * a <CODE>org.xml.sax.helpers.AttributesImpl</CODE> object
     * or a list of names of variables. In the latter case, the
     * attributes will have the names and values of the variables.
     *
     * @param   attrExpr            the expression that will be evaluated
     * @throws  JspException        to signal any error that might occur
     */
    public void setAttr(String attrExpr) throws JspException {
        this.attrExpr = attrExpr;
    }

    /**
     * Sets a flag indicating if the produced element must be empty.
     *
     * @param   emptyExpr           the expression that will be evaluated
     * @throws  JspException        to signal any error that might occur
     */
    public void setEmpty(String emptyExpr) throws JspException {
        this.emptyExpr = emptyExpr;
    }

    /**
     * Returns the value of the <CODE>empty</CODE> flag.
     *
     * @return  a flag indicating whether the body content of this tag should be
     *          ignored or evaluated
     */
    protected boolean shouldIgnoreContent() {
        return empty;
    }

    /**
     * Outputs the start tag of the element.
     *
     * @throws  JspException    to signal an error
     * @throws  SAXException    to signal an error
     */
    protected void startOutput() throws JspException, SAXException {
        if (uriExpr != null)
            uri = evalString("uri", uriExpr);
        String prefix = null;
        if (nameExpr != null) {
            qname = evalString("name", nameExpr);
            int colonIndex = qname.indexOf(':');
            if (colonIndex != -1) {
                prefix = qname.substring(0, colonIndex);
                name = qname.substring(colonIndex + 1);
            } else
                name = qname;
        }
        if (attrExpr != null) {
            Object attrList = eval("attr", attrExpr, Object.class);
            if (attrList instanceof AttributesImpl)
                attr = (AttributesImpl) attrList;
            else {
                attr = new AttributesImpl();
                StringTokenizer tokenizer
                    = new StringTokenizer(attrList.toString());
                while (tokenizer.hasMoreTokens()) {
                    String expr = tokenizer.nextToken();
                    int dotIndex = expr.indexOf('.');
                    String attrName = expr;
                    if (dotIndex != -1)
                        attrName = expr.substring(dotIndex + 1);
                    expr = "${" + expr + "}";
                    String attrValue = evalString("attr", expr);
                    attr.addAttribute(null, attrName, attrName,
                        "CDATA", attrValue);
                }
            }
        }
        if (emptyExpr != null)
            empty = evalBoolean("empty", emptyExpr);
        int nsIndex = -1;
        String oldUri = null;
        if (uri != null) {
            String qns = prefix != null ? "xmlns:" + prefix : "xmlns";
            String ns = prefix != null ? prefix : "xmlns";
            nsIndex = attr.getIndex(qns);
            if (nsIndex != -1) {
                oldUri = attr.getValue(nsIndex);
                attr.setValue(nsIndex, uri);
            } else {
                attr.addAttribute(null, ns, qns, "CDATA", uri);
                nsIndex = attr.getIndex(qns);
            }
        }
        if (attr == null)
            attr = new AttributesImpl();
        serializer.getHandler().startElement(uri, name, qname, attr);
        if (nsIndex != -1)
            if (oldUri != null)
                attr.setValue(nsIndex, oldUri);
            else
                attr.removeAttribute(nsIndex);
        if (!empty) {
            // workaround: force the serializer to close the start tag
            emptyComment();
        }
    }

    /**
     * Outputs the end tag of the element.
     *
     * @throws  JspException    to signal an error
     * @throws  SAXException    to signal an error
     */
    protected void endOutput() throws JspException, SAXException {
        if (uriExpr != null && uri == null)
            uri = evalString("uri", uriExpr);
        if (nameExpr != null && qname == null) {
            qname = evalString("name", nameExpr);
            int colonIndex = qname.indexOf(':');
            if (colonIndex != -1)
                name = qname.substring(colonIndex + 1);
            else
                name = qname;
        }
        serializer.getHandler().endElement(uri, name, qname);
    }

}
