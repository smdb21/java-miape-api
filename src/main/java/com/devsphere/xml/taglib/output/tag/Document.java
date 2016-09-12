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

import com.devsphere.xml.taglib.output.write.Serializer;

import org.w3c.dom.Node;

import org.xml.sax.SAXException;

import javax.servlet.jsp.JspException;

import java.util.List;
import java.util.Properties;

/**
 * Handler for the &lt;o:document&gt; tag that outputs a DOM tree or just
 * sets the context for generating an XML document dynamically.
 */
public class Document extends Fragment {
    private static final List ALLOWED_ANCESTORS = null;
    private static final String OMIT_XML_DECLARATION_PROPERTY
        = "omit-xml-declaration";
    private static final String STANDALONE_PROPERTY = "standalone";
    private static final String VERSION_PROPERTY = "version";
    private static final String ENCODING_PROPERTY = "encoding";
    private static final String DOCTYPE_PUBLIC_PROPERTY = "doctype-public";
    private static final String DOCTYPE_SYSTEM_PROPERTY = "doctype-system";
    private String xmldeclExpr;
    private boolean xmldecl;
    private String standaloneExpr;
    private boolean standalone;
    private String versionExpr;
    private String version;
    private String encodingExpr;
    private String encoding;
    private String systemIdExpr;
    private String systemId;
    private String publicIdExpr;
    private String publicId;

    /**
     * Initializes the fields of this tag handler.
     */
    protected void init() {
        super.init();
        xmldeclExpr = null;
        xmldecl = true;
        standaloneExpr = null;
        standalone = false;
        versionExpr = null;
        version = null;
        encodingExpr = null;
        encoding = null;
        systemIdExpr = null;
        systemId = null;
        publicIdExpr = null;
        publicId = null;
    }

    /**
     * Returns the name of the handled tag
     *
     * @return  the name of the handled tag
     */
    public String getTagName() {
        return "document";
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
     * Sets a flag indicating if the XML declaration should be included
     * at the beginning of the produced XML document.
     *
     * @param   xmlDeclExpr         the expression that will be evaluated
     * @throws  JspException        to signal any error that might occur
     */
    public void setXmldecl(String xmldeclExpr) throws JspException {
        this.xmldeclExpr = xmldeclExpr;
    }

    /**
     * Sets a flag indicating if the document should be declared standalone.
     *
     * @param   standaloneExpr      the expression that will be evaluated
     * @throws  JspException        to signal any error that might occur
     */
    public void setStandalone(String standaloneExpr) throws JspException {
        this.standaloneExpr = standaloneExpr;
    }

    /**
     * Sets the XML version.
     *
     * @param   versionExpr         the expression that will be evaluated
     * @throws  JspException        to signal any error that might occur
     */
    public void setVersion(String versionExpr) throws JspException {
        this.versionExpr = versionExpr;
    }

    /**
     * Sets the XML encoding.
     *
     * @param   encodingExpr        the expression that will be evaluated
     * @throws  JspException        to signal any error that might occur
     */
    public void setEncoding(String encodingExpr) throws JspException {
        this.encodingExpr = encodingExpr;
    }

    /**
     * Sets the public doctype identifier of the produced XML document.
     *
     * @param   publicIdExpr        the expression that will be evaluated
     * @throws  JspException        to signal any error that might occur
     */
    public void setPublicId(String publicIdExpr) throws JspException {
        this.publicIdExpr = publicIdExpr;
    }

    /**
     * Sets the system doctype identifier of the produced XML document.
     *
     * @param   systemIdExpr        the expression that will be evaluated
     * @throws  JspException        to signal any error that might occur
     */
    public void setSystemId(String systemIdExpr) throws JspException {
        this.systemIdExpr = systemIdExpr;
    }

    /**
     * Returns the output properties.
     *
     * @return                      the output properties
     * @throws  JspException        to signal any error that might occur
     */
    protected Properties getOutputProperties() throws JspException {
        Properties properties = super.getOutputProperties();
        if (xmldeclExpr != null) {
            xmldecl = evalBoolean("xmldecl", xmldeclExpr);
            properties.put(OMIT_XML_DECLARATION_PROPERTY,
                xmldecl ? "no" : "yes");
        }
        if (standaloneExpr != null) {
            standalone = evalBoolean("standalone", standaloneExpr);
            properties.put(STANDALONE_PROPERTY, standalone ? "yes" : "no");
        }
        if (versionExpr != null) {
            version = evalString("version", versionExpr);
            properties.put(VERSION_PROPERTY, version);
        }
        if (encodingExpr != null) {
            encoding = evalString("encoding", encodingExpr);
            properties.put(ENCODING_PROPERTY, encoding);
        }
        if (publicIdExpr != null) {
            publicId = evalString("public", publicIdExpr);
            properties.put(DOCTYPE_PUBLIC_PROPERTY, publicId);
        }
        if (systemIdExpr != null) {
            systemId = evalString("system", systemIdExpr);
            properties.put(DOCTYPE_SYSTEM_PROPERTY, systemId);
        }
        return properties;
    }

    /**
     * Initializes this tag handler for output, creating the serializer object.
     *
     * @throws  JspException    to signal a tag validation error
     */
    protected void initOutput() throws JspException {
        super.initOutput();
        serializer = new Serializer();
    }

    /**
     * Serializes the DOM tree, if the <CODE>dom</CODE> attribute is present.
     * Otherwise, it starts a document whose content can be produced by
     * tags of this library.
     *
     * @throws  JspException    to signal an error
     * @throws  SAXException    to signal an error
     */
    protected void startOutput() throws JspException, SAXException {
        Properties properties = getOutputProperties();
        serializer.setOutputProperties(properties);
        if (domExpr != null) {
            dom = (Node) eval("dom", domExpr, Node.class);
            serializer.write(dom);
        } else {
            serializer.getHandler().startDocument();
            // workaround: force the serializer to write the document header
            emptyComment();
        }
    }

    /**
     * Ends the output of the document.
     *
     * @throws  JspException    to signal an error
     * @throws  SAXException    to signal an error
     */
    protected void endOutput() throws JspException, SAXException {
        if (domExpr == null)
            serializer.getHandler().endDocument();
    }

}
