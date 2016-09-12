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

import com.devsphere.xml.taglib.process.event.Processing;

import javax.servlet.jsp.JspException;

import java.util.Arrays;
import java.util.List;

/**
 * Handler for the &lt;p:pi&gt; tag, whose body can process a
 * <CODE>processingInstruction()</CODE> SAX event. It exports two variables
 * that hold the target and data of the processing instruction.
 */
public class PI extends ProcessSupport {
    private static final List ALLOWED_ANCESTORS = Arrays.asList(new String[] {
        "parse",
        "element"
    });
    private static final String DEFAULT_VAR_TARGET = "data";
    private static final String DEFAULT_VAR_DATA = "data";
    private String target;
    private String varTarget;
    private int scopeTarget;
    private String testTargetExpr;
    private boolean testTarget;
    private String data;
    private String varData;
    private int scopeData;
    private String testExpr;
    private boolean test;
    private int scope;

    /**
     * Initializes the fields of this tag handler.
     */
    protected void init() {
        super.init();
        target = null;
        varTarget = DEFAULT_VAR_TARGET;
        scopeTarget = -1;
        testTargetExpr = null;
        testTarget = true;
        data = null;
        varData = DEFAULT_VAR_DATA;
        scopeData = -1;
        testExpr = null;
        test = true;
        scope = DEFAULT_SCOPE;
    }

    /**
     * Returns the name of the handled tag
     *
     * @return  the name of the handled tag
     */
    public String getTagName() {
        return "pi";
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
     * Sets the name of the target variable exported by this tag handler.
     * The processing instruction's target will be the value of the target
     * variable. The default name of this variable is <CODE>target</CODE>.
     *
     * @param   varTarget       the name of the target variable
     * @throws  JspException    to signal any error that might occur
     */
    public void setVarTarget(String varTarget) throws JspException {
        this.varTarget = checkVarName(varTarget);
    }

    /**
     * Sets the scope of the target variable exported by this tag handler.
     *
     * @param   scopeTarget     the scope of the target variable
     * @throws  JspException    to signal any error that might occur
     */
    public void setScopeTarget(String scopeTarget) throws JspException {
        this.scopeTarget = checkVarScope(scopeTarget);
    }

    /**
     * Sets the expression whose value will be compared with the processing
     * instruction's target. If this expression is present and the target test
     * is negative, the body of the tag won't be evaluated.
     *
     * @param   testTargetExpr  the expression for testing the target
     * @throws  JspException    to signal any error that might occur
     */
    public void setTestTarget(String testTargetExpr) throws JspException {
        this.testTargetExpr = testTargetExpr;
    }

    /**
     * Sets the name of the data variable exported by this tag handler.
     * The value of the data variable will be the processing instruction's data.
     * The default name of this variable is <CODE>data</CODE>.
     *
     * @param   varData         the name of the data variable
     * @throws  JspException    to signal any error that might occur
     */
    public void setVarData(String varData) throws JspException {
        this.varData = checkVarName(varData);
    }

    /**
     * Sets the scope of the data variable exported by this tag handler.
     *
     * @param   scopeData       the scope of the data variable
     * @throws  JspException    to signal any error that might occur
     */
    public void setScopeData(String scopeData) throws JspException {
        this.scopeData = checkVarScope(scopeData);
    }

    /**
     * If this expression is present and it evaluates to false
     * (the test is negative), the body of the tag won't be evaluated.
     *
     * @param   testExpr        the expression that will be evaluated
     * @throws  JspException    to signal any error that might occur
     */
     public void setTest(String testExpr) throws JspException {
        this.testExpr = testExpr;
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
     * Initializes the loop to process parsing instruction events.
     *
     * @throws  JspException    to signal any tag validation error
     */
    protected void initLoop() throws JspException {
        super.initLoop();
        handler = ancestor.handler;
        indentLevel = ancestor.indentLevel + 1;
        if (scopeTarget == -1)
            scopeTarget = scope;
        if (scopeData == -1)
            scopeData = scope;
    }

    /**
     * Exports the target and data variables.
     */
    protected void exportVariables() {
        export(varTarget, target, scopeTarget);
        export(varData, data, scopeData);
    }

    /**
     * Evaluates the test conditions and exports the variables.
     *
     * @return                  <CODE>true</CODE> is all tests are passed
     * @throws  JspException    to signal any error that might occur
     */
    protected boolean evalTests() throws JspException {
        if (testTargetExpr != null) {
            String wantedTarget = evalString("testTarget", testTargetExpr);
            testTarget = wantedTarget.equals(target);
        }
        if (testExpr != null)
            test = evalBoolean("test", testExpr);
        return test && testTarget;
    }

    /**
     * Returns <CODE>true</CODE> if the current event is a
     * <CODE>Processing</CODE> instance and all tests are passed.
     *
     * @return  <CODE>true</CODE> if the event loop should be started
     * @throws  JspException    to signal an error
     */
    protected boolean startLoop() throws JspException {
        if (getEvent())
            if (indentLevel == event.getIndentLevel()
                    && event instanceof Processing) {
                Processing processingEvent = (Processing) event;
                target = processingEvent.getTarget();
                data = processingEvent.getData();
                exportVariables();
                if (evalTests()) {
                    event.setConsumed(true);
                    logEvent();
                    removeEvent();
                    getEvent();
                    return true;
                }
            }
        return false;
    }

    /**
     * Returns <CODE>false</CODE> since the tag's body needs to be evaluated
     * only once in order to process the event.
     *
     * @return  <CODE>false</CODE>
     * @throws  JspException    to signal an error
     */
    protected boolean continueLoop() throws JspException {
        return false;
    }

}
