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

import java.util.Arrays;
import java.util.List;

/**
 * Handler for the &lt;o:attributes&gt; tag, which exports a variable holding
 * a SAX list of attributes.
 */
public class Attributes extends OutputSupport {
    private static final List ALLOWED_ANCESTORS = Arrays.asList(new String[] {
        "document",
        "element"
    });
    private AttributesImpl attr;
    private String var;
    private int scope;
    private String copyExpr;
    private AttributesImpl copy;

    /**
     * Initializes the fields of this tag handler.
     */
    protected void init() {
        super.init();
        attr = null;
        var = null;
        scope = DEFAULT_SCOPE;
        copyExpr = null;
        copy = null;
    }

    /**
     * Returns the name of the handled tag
     *
     * @return  the name of the handled tag
     */
    public String getTagName() {
        return "attributes";
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
     * Returns the <CODE>org.xml.sax.helpers.AttributesImpl</CODE> object
     * that holds the attributes.
     *
     * @return  the SAX object that holds the attributes
     */
    protected AttributesImpl getAttr() {
        return attr;
    }

    /**
     * Sets the name of the variable exported by this tag handler.
     * The value of the variable will be the SAX object that holds
     * the attributes.
     *
     * @param   var             the name of the variable
     * @throws  JspException    to signal any error that might occur
     */
    public void setVar(String var) throws JspException {
        this.var = checkVarName(var);
    }

    /**
     * Sets the scope of the variable exported by this tag handler.
     *
     * @param   scope           the scope of the variable
     * @throws  JspException    to signal any error that might occur
     */
    public void setScope(String scope) throws JspException {
        this.scope = checkVarScope(scope);
    }

    /**
     * Sets the initial attribute list, which is empty by default.
     * The value of the evaluated expression is expected to be
     * a <CODE>org.xml.sax.helpers.AttributesImpl</CODE> object.
     * The exported list will contain copies of all attributes of
     * the given initial list.
     *
     * @param   valueExpr           the expression that will be evaluated
     * @throws  JspException        to signal any error that might occur
     */
    public void setCopy(String copyExpr) throws JspException {
        this.copyExpr = copyExpr;
    }

    /**
     * Initializes the SAX object that holds the attributes.
     *
     * @throws  JspException    to signal an error
     * @throws  SAXException    to signal an error
     */
    protected void startOutput() throws JspException, SAXException {
        if (copyExpr != null) {
            copy = (AttributesImpl) eval("copy", copyExpr,
                AttributesImpl.class);
            attr = new AttributesImpl(copy);
        } else
            attr = new AttributesImpl();
    }

    /**
     * Exports the attribute variable.
     *
     * @throws  JspException    to signal an error
     * @throws  SAXException    to signal an error
     */
    protected void endOutput() throws JspException, SAXException {
        export(var, attr, scope, false);
    }

}
