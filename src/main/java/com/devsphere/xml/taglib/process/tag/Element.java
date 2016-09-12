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

import com.devsphere.xml.taglib.process.event.EndElement;
import com.devsphere.xml.taglib.process.event.HandleDOM;
import com.devsphere.xml.taglib.process.event.EventSupport;
import com.devsphere.xml.taglib.process.event.StartElement;

import org.w3c.dom.Node;

import javax.servlet.jsp.JspException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Handler for the &lt;p:element&gt; tag, whose body may process a DOM sub-tree
 * or may contain sub-tags that process the SAX events that are generated
 * between a <CODE>startElement()</CODE> event and an <CODE>endElement()</CODE>
 * event. It exports variables that hold the element's namespace URI, local
 * name, qualified name and attributes. It may also export a variable
 * holding the root of a DOM sub-tree, in which case the body may not contain
 * any tags of this library. If the <CODE>varDom</CODE> attribute isn't present,
 * no DOM variable is exported and the body may contain a &lt;p:start&gt; tag
 * for processing the <CODE>startElement()</CODE> SAX event, followed by
 * one or more &lt;p:element&gt; and &lt;p:data&gt; tags for processing
 * the sub-elements and the character data, followed by an optional
 * &lt;p:end&gt; tag for processing the <CODE>endElement()</CODE> SAX event.
 */
public class Element extends ProcessSupport {
    private static final List ALLOWED_ANCESTORS = Arrays.asList(new String[] {
        "parse",
        "element"
    });
    private static final String DEFAULT_VAR_URI = "uri";
    private static final String DEFAULT_VAR_QNAME = "qname";
    private static final String DEFAULT_VAR_NAME = "name";
    private static final String DEFAULT_VAR_ATTR = "attr";
    private static final String DEFAULT_VAR_DOM = null;
    private String uri;
    private String varUri;
    private int scopeUri;
    private String testUriExpr;
    private boolean testUri;
    private String name;
    private String varName;
    private int scopeName;
    private String testNameExpr;
    private boolean testName;
    private String qname;
    private String varQname;
    private int scopeQname;
    private String testQnameExpr;
    private boolean testQname;
    private Map attr;
    private String varAttr;
    private int scopeAttr;
    private String testExpr;
    private boolean test;
    private Node dom;
    private String varDom;
    private int scopeDom;
    private int scope;

    /**
     * Initializes the fields of this tag handler.
     */
    protected void init() {
        super.init();
        uri = null;
        varUri = DEFAULT_VAR_URI;
        scopeUri = -1;
        testUriExpr = null;
        testUri = true;
        name = null;
        varName = DEFAULT_VAR_NAME;
        scopeName = -1;
        testNameExpr = null;
        testName = true;
        qname = null;
        varQname = DEFAULT_VAR_QNAME;
        scopeQname = -1;
        testQnameExpr = null;
        testQname = true;
        attr = null;
        varAttr = DEFAULT_VAR_ATTR;
        scopeAttr = -1;
        testExpr = null;
        test = true;
        dom = null;
        varDom = DEFAULT_VAR_DOM;
        scopeDom = -1;
        scope = DEFAULT_SCOPE;
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
     * Returns true if the body of this tag will process a DOM tree.
     *
     * @return  <CODE>true</CODE> if the dom variable must be exported.
     */
    public boolean getWantDOM() {
        return varDom != null;
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
     * Sets the name of the uri variable exported by this tag handler.
     * The element's namespace URI will be the value of the uri variable.
     * The default name of this variable is <CODE>uri</CODE>.
     *
     * @param   varUri          the name of the uri variable
     * @throws  JspException    to signal any error that might occur
     */
    public void setVarUri(String varUri) throws JspException {
        this.varUri = checkVarName(varUri);
    }

    /**
     * Sets the scope of the uri variable exported by this tag handler.
     *
     * @param   scopeUri        the scope of the uri variable
     * @throws  JspException    to signal any error that might occur
     */
    public void setScopeUri(String scopeUri) throws JspException {
        this.scopeUri = checkVarScope(scopeUri);
    }

    /**
     * Sets the expression whose value will be compared with the element's
     * namespace URI. If this expression is present and the URI test is
     * negative, the event loop will not be started.
     *
     * @param   testUriExpr     the expression for testing the URI
     * @throws  JspException    to signal any error that might occur
     */
    public void setTestUri(String testUriExpr) throws JspException {
        this.testUriExpr = testUriExpr;
    }

    /**
     * Sets the name of the name variable exported by this tag handler.
     * The element's local name will be the value of the name variable.
     * The default name of this variable is <CODE>name</CODE>.
     *
     * @param   varName         the name of the name variable
     * @throws  JspException    to signal any error that might occur
     */
    public void setVarName(String varName) throws JspException {
        this.varName = checkVarName(varName);
    }

    /**
     * Sets the scope of the name variable exported by this tag handler.
     *
     * @param   scopeName       the scope of the name variable
     * @throws  JspException    to signal any error that might occur
     */
    public void setScopeName(String scopeName) throws JspException {
        this.scopeName = checkVarScope(scopeName);
    }

    /**
     * Sets the expression whose value will be compared with the element's
     * local name. If this expression is present and the name test is negative,
     * the event loop will not be started.
     *
     * @param   testNameExpr    the expression for testing the local name
     * @throws  JspException    to signal any error that might occur
     */
    public void setTestName(String testNameExpr) throws JspException {
        this.testNameExpr = testNameExpr;
    }

    /**
     * Sets the name of the qname variable exported by this tag handler.
     * The element's qualified name will be the value of the qname variable.
     * The default name of this variable is <CODE>qname</CODE>.
     *
     * @param   varName         the name of the qname variable
     * @throws  JspException    to signal any error that might occur
     */
    public void setVarQname(String varQname) throws JspException {
        this.varQname = checkVarName(varQname);
    }

    /**
     * Sets the scope of the qname variable exported by this tag handler.
     *
     * @param   scopeName       the scope of the qname variable
     * @throws  JspException    to signal any error that might occur
     */
    public void setScopeQname(String scopeQname) throws JspException {
        this.scopeQname = checkVarScope(scopeQname);
    }

    /**
     * Sets the expression whose value will be compared with the element's
     * qualified name. If this expression is present and the qname test
     * is negative, the event loop will not be started.
     *
     * @param   testNameExpr    the expression for testing the qualified name
     * @throws  JspException    to signal any error that might occur
     */
    public void setTestQname(String testQnameExpr) throws JspException {
        this.testQnameExpr = testQnameExpr;
    }

    /**
     * Sets the name of the attr variable exported by this tag handler.
     * The element's attributes will be the value of the attr variable.
     * The default name of this variable is <CODE>attr</CODE>.
     *
     * @param   varAttr         the name of the attr variable
     * @throws  JspException    to signal any error that might occur
     */
    public void setVarAttr(String varAttr) throws JspException {
        this.varAttr = checkVarName(varAttr);
    }

    /**
     * Sets the scope of the attr variable exported by this tag handler.
     *
     * @param   scopeAttr       the scope of the attr variable
     * @throws  JspException    to signal any error that might occur
     */
    public void setScopeAttr(String scopeAttr) throws JspException {
        this.scopeAttr = checkVarScope(scopeAttr);
    }

    /**
     * If this expression is present and it evaluates to false
     * (the test is negative), the event loop will not be started.
     *
     * @param   testExpr        the expression that will be evaluated
     * @throws  JspException    to signal any error that might occur
     */
    public void setTest(String testExpr) throws JspException {
        this.testExpr = testExpr;
    }

    /**
     * Sets the name of the dom variable exported by this tag handler.
     * The presence of this variable indicates that the application
     * wants a DOM tree for processing rather than a sequence of SAX events.
     * The root node of the DOM sub-tree will be the value of the dom variable.
     *
     * @param   varDom          the name of the dom variable
     * @throws  JspException    to signal any error that might occur
     */
    public void setVarDom(String varDom) throws JspException {
        this.varDom = checkVarName(varDom);
    }

    /**
     * Sets the scope of the dom variable exported by this tag handler.
     *
     * @param   scopeDom        the scope of the dom variable
     * @throws  JspException    to signal any error that might occur
     */
    public void setScopeDom(String scopeDom) throws JspException {
        this.scopeDom = checkVarScope(scopeDom);
    }

    /**
     * Sets the default scope for the variables exported by this tag handler.
     *
     * @param   scope           the default scope of the exported variables
     * @throws  JspException    to signal any error that might occur
     */
    public void setScope(String scope) throws JspException {
        this.scope = checkVarScope(scope);
    }

    /**
     * Initializes the loop to process element events.
     *
     * @throws  JspException    to signal any tag validation error
     */
    protected void initLoop() throws JspException {
        super.initLoop();
        handler = ancestor.handler;
        indentLevel = ancestor.indentLevel + 1;
        if (scopeUri == -1)
            scopeUri = scope;
        if (scopeName == -1)
            scopeName = scope;
        if (scopeQname == -1)
            scopeQname = scope;
        if (scopeAttr == -1)
            scopeAttr = scope;
        if (scopeDom == -1)
            scopeDom = scope;
    }

    /**
     * Exports the element variables.
     */
    protected void exportVariables() {
        export(varUri, uri, scopeUri);
        export(varName, name, scopeName);
        export(varQname, qname, scopeQname);
        export(varAttr, attr, scopeAttr);
        if (varDom != null && dom != null)
            export(varDom, dom, scopeDom);
    }

    /**
     * Evaluates the test conditions.
     *
     * @return                  <CODE>true</CODE> is all tests are passed
     * @throws  JspException    to signal any error that might occur
     */
    protected boolean evalTests() throws JspException {
        if (testUriExpr != null) {
            String wantedUri = evalString("testUri", testUriExpr);
            testUri = wantedUri.equals(uri);
        }
        if (testNameExpr != null) {
            String wantedName = evalString("testName", testNameExpr);
            testName = wantedName.equals(name);
        }
        if (testQnameExpr != null) {
            String wantedQname = evalString("testQname", testQnameExpr);
            testQname = wantedQname.equals(qname);
        }
        if (testExpr != null)
            test = evalBoolean("test", testExpr);
        return test && testUri && testName && testQname;
    }

    /**
     * Returns <CODE>true</CODE> if the current event is a
     * <CODE>StartElement</CODE> instance and all tests are passed.
     *
     * @return  <CODE>true</CODE> if the event loop should be started
     * @throws  JspException    to signal an error
     */
    protected boolean startLoop() throws JspException {
        if (getEvent())
            if (indentLevel == event.getIndentLevel()
                    && event instanceof StartElement) {
                StartElement elementEvent = (StartElement) event;
                uri = elementEvent.getUri();
                name = elementEvent.getName();
                qname = elementEvent.getQname();
                attr = elementEvent.getAttr();
                exportVariables();
                if (evalTests()) {
                    if (getWantDOM()) {
                        ((StartElement) event).setWantDOM(true);
                        removeEvent();
                        getEvent();
                        if (event instanceof HandleDOM) {
                            dom = ((HandleDOM) event).getElement();
                            export(varDom, dom, scopeDom);
                        } else
                            throw new JspException("Expected HandleDOM event");
                    }
                    logEvent();
                    return true;
                }
            }
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
        boolean stop = indentLevel == event.getIndentLevel()
            && (event instanceof EndElement || event instanceof HandleDOM);
           removeEvent();
        if (!stop)
            if (getEvent())
                if (indentLevel == event.getIndentLevel() && event.isConsumed()
                        && event instanceof EndElement)
                    removeEvent();
                else {
                    logEvent();
                    return true;
                }
        return false;
    }

}
