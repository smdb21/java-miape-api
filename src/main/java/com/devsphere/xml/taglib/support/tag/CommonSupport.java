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

package com.devsphere.xml.taglib.support.tag;

import com.devsphere.logging.AbstractLogger;
import com.devsphere.logging.CompoundLogger;
import com.devsphere.logging.PrintLogger;
import com.devsphere.logging.ServletLogger;

import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class provides support for custom tag handlers and is the base class for
 * all tag classes. It provides methods to validate tags, evaluate expressions
 * export and restore variables, logging and debugging.
 */
public abstract class CommonSupport extends BodyTagSupport
        implements TryCatchFinally {
    public static final String DEBUG_ATTR = "com.devsphere.xml.taglib.debug";
    public static final String LOGGER_ATTR = "com.devsphere.xml.taglib.logger";
    protected static final boolean DEFAULT_DEBUG = false;
    protected static final int DEFAULT_SCOPE = PageContext.PAGE_SCOPE;
    protected static final boolean DEFAULT_RESTORE_VALUE = false;
    private static final String PAGE_SCOPE = "page";
    private static final String REQUEST_SCOPE = "request";
    private static final String SESSION_SCOPE = "session";
    private static final String APPLICATION_SCOPE = "application";

    /** the cached value of the debug flag */
    protected Boolean cachedDebug;

    /** the cached reference to the logger object */
    protected AbstractLogger cachedLogger;

    /** the original values of the variables that were exported */
    protected Map originalValues[];

    /**
     * Calls <CODE>init()</CODE>
     */
    public CommonSupport() {
        init();
    }

    /**
     * Initializes the fields of this tag handler. Since instances of this
     * classes may be recycled by Servlet containers (such as Tomcat),
     * it is not enough to initialize everything in constructor.
     * <CODE>init()</CODE> is also called in <CODE>doFinally()</CODE>
     * to let the object in its initial state after handling a tag.
     */
    protected void init() {
        cachedDebug = null;
        cachedLogger = null;
        // do not initialize 'originalValues' because the maps are created
        // by export(), cleared by restore() and then reused
    }

    /**
     * Subclasses must implement this method to return the tag name.
     *
     * @return  the name of the handled tag
     */
    public abstract String getTagName();

    /**
     * Subclasses must implement this method to return the list of names
     * of the tags that may contain the tag that is handled by this class.
     * The returned list is used by <CODE>validate()</CODE>
     *
     * @return  the list of names of the allowed ancestor tags
     *          or <CODE>null</CODE> if the tag context doesn't matter.
     */
    protected abstract List getAllowedAncestors();

    /**
     * Subclasses may call this method to verify if the handled tag was placed
     * in the right context. This methods throws a <CODE>JspException</CODE> if
     * <UL>
     * <LI><CODE>ancestor</CODE> is <CODE>null</CODE>,
     * but the allowed ancestors list is not empty or
     * <LI><CODE>ancestor</CODE> is not <CODE>null</CODE>,
     * but the allowed ancestors list is empty or
     * <LI>the name of the given <CODE>ancestor</CODE> tag
     * is not found in the allowed ancestors list
     * returned by <CODE>getAllowedAncestors()</CODE>.
     * </UL>
     *
     * @param   ancestor    the tag handler of an ancestor whose name
     *                      is searched in the list of allowed ancestors
     * @throws  JspException    to signal a tag validation error
     */
    protected void validate(CommonSupport ancestor) throws JspException {
        List allowedAncestors = getAllowedAncestors();
        if (allowedAncestors != null) {
            if (ancestor == null && allowedAncestors.size() > 0)
                throw new JspException("JSP Validation Error: "
                    + "'" + getTagName() + "' tags "
                    + "must be placed in one of the following tags: "
                    + allowedAncestors.toString());
            if (ancestor != null && allowedAncestors.size() == 0)
                throw new JspException("JSP Validation Error: "
                    + "'" + getTagName() + "' tags "
                    + "may not be placed in any of the tags of this library.");
            if (ancestor != null &&
                    !allowedAncestors.contains(ancestor.getTagName()))
                throw new JspException("JSP Validation Error: "
                    + "'" + getTagName() + "' tags may not be placed within "
                    + "'" + ancestor.getTagName() + "' tags. "
                    + "'" + getTagName() + "' tags can be placed only in one "
                    + "of the following tags: " + allowedAncestors.toString());
        }
    }

    /**
     * Verifies the name of a variable. Currently, no checks are made.
     *
     * @throws  JspException    to signal an attribute validation error
     */
    protected String checkVarName(String varName) throws JspException {
        return varName;
    }

    /**
     * Verifies the scope of a variable. The allowed scopes are
     * <UL>
     * <LI><CODE>page</CODE>
     * <LI><CODE>request</CODE>
     * <LI><CODE>session</CODE>
     * <LI><CODE>application</CODE>
     * </UL>
     *
     * @throws  JspException    to signal an attribute validation error
     */
    protected int checkVarScope(String scope) throws JspException {
        if (scope == null || scope.length() == 0)
            return DEFAULT_SCOPE;
        else if (PAGE_SCOPE.equalsIgnoreCase(scope))
            return PageContext.PAGE_SCOPE;
        else if (REQUEST_SCOPE.equalsIgnoreCase(scope))
            return PageContext.REQUEST_SCOPE;
        else if (SESSION_SCOPE.equalsIgnoreCase(scope))
            return PageContext.SESSION_SCOPE;
        else if (APPLICATION_SCOPE.equalsIgnoreCase(scope))
            return PageContext.APPLICATION_SCOPE;
        else
            throw new JspException("Invalid Scope: " + scope);
    }

    /**
     * Returns <CODE>true</CODE> if there is a page/request/session/application
     * variable called <CODE>com.devsphere.xml.taglib.debug</CODE> and its
     * value is <CODE>true</CODE>. This value is cached. Also a tag may look
     * for this debug flag to an ancestor tag instead of checking the variable.
     *
     * <P>The <CODE>com.devsphere.xml.taglib.debug</CODE> should
     * be set at the beginning of a page with the <CODE>page</CODE> or
     * <CODE>request</CODE> scope or in the login/welcome page of an application
     * with the <CODE>session</CODE> or <CODE>application</CODE> scope depending
     * on what you want to debug: a page, a page together with its included
     * pages, a user session or the entire Web application.
     *
     * @return  the value of the <CODE>com.devsphere.xml.taglib.debug</CODE>
     *          flag or <CODE>false</CODE> if this variable is not set.
     */
    protected boolean getDebug() {
        if (cachedDebug == null) {
            Object debug = pageContext.findAttribute(DEBUG_ATTR);
            if (debug == null)
                debug = pageContext.getServletContext().getInitParameter(
                    DEBUG_ATTR);
            if (debug == null) {
                debug = new Boolean(DEFAULT_DEBUG);
                pageContext.setAttribute(DEBUG_ATTR, debug);
            } else if (!(debug instanceof Boolean)) {
                String debugValue = debug.toString();
                debug = new Boolean("true".equalsIgnoreCase(debugValue));
            }
            cachedDebug = (Boolean) debug;
        }
        return cachedDebug.booleanValue();
    }

    /**
     * Returns the logger used by this tag handler, which must be an instance
     * of a subclass of <CODE>com.devsphere.logging.AbstractLogger</CODE>.
     *
     * <P>You may set a logger for a page, request, session or application
     * by registering your logger object as the value of a variable called
     * <CODE>com.devsphere.xml.taglib.logger</CODE>.
     *
     * <P>A tag handler derived from this class should look for a logger object
     * at an ancestor tag of its tag library and set <CODE>cachedLogger</CODE>.
     * If this field is <CODE>null</CODE>, <CODE>getLogger()</CODE> tries
     * to get the logger from <CODE>com.devsphere.xml.taglib.logger</CODE>
     * and caches it. If such a variable doesn't exist, a default logger
     * will be created.
     *
     * <P>The <CODE>com.devsphere.xml.taglib.logger</CODE> should
     * be set at the beginning of a page within the <CODE>page</CODE> or
     * <CODE>request</CODE> scope or in the login/welcome page of an application
     * within the <CODE>session</CODE> or <CODE>application</CODE> scope.
     *
     * <P>The default logger forwards the logging requests to the
     * <CODE>ServletContext</CODE> object, by calling its <CODE>log()</CODE>
     * methods. If the debug flag is set, the logged messages and exceptions
     * are also printed to <CODE>System.err</CODE>
     *
     * @return  the logger of this tag handler.
     */
    protected AbstractLogger getLogger() {
        if (cachedLogger == null) {
            AbstractLogger logger
                = (AbstractLogger) pageContext.findAttribute(LOGGER_ATTR);
            if (logger == null) {
                logger = new ServletLogger(pageContext.getServletContext());
                if (getDebug()) {
                    AbstractLogger debugLogger = new PrintLogger(System.err);
                    logger = new CompoundLogger(logger, debugLogger);
                }
                pageContext.setAttribute(LOGGER_ATTR, logger);
            }
            cachedLogger = logger;
        }
        return cachedLogger;
    }

    /**
     * Logs a message.
     *
     * @param   message     the message that has to be logged.
     */
    protected void log(String message) {
        getLogger().log(message);
    }

    /**
     * Logs an exception.
     *
     * @param   throwable   the exception that has to be logged.
     */
    public void log(Throwable throwable) {
        getLogger().log(throwable);
    }

    /**
     * Logs a message and an exception.
     *
     * @param   message     the message that has to be logged.
     * @param   throwable   the exception that has to be logged.
     */
    public void log(String message, Throwable throwable) {
        getLogger().log(message, throwable);
    }

    /**
     * Evaluates an expression that follows the syntax defined by JSTL.
     *
     * <P>The current implementation uses the expression evaluator of JSTL 1.0
     * implemented by Apache.
     *
     * @param   attributeName   the name of the tag attribute whose value
     *                          is the expression that must be evaluated.
     * @param   expression      the expression that is evaluated
     * @param   expectedType    the type of the object returned by this method
     * @return                  the value of the evaluated expression
     */
    protected Object eval(String attributeName, String expression,
            Class expectedType) throws JspException {
        return ExpressionUtil.evalNotNull(
            getTagName(), attributeName, expression, expectedType,
            this, pageContext);
    }

    /**
     * Evaluates a JSTL expression to a <CODE>String</CODE>.
     *
     * @param   attributeName   the name of the tag attribute whose value
     *                          is the expression that must be evaluated.
     * @param   expression      the expression that is evaluated
     * @return                  the value of the evaluated expression
     */
    protected String evalString(String attributeName, String expression)
            throws JspException {
        Object value = eval(attributeName, expression, String.class);
        return (String) value;
    }

    /**
     * Evaluates a JSTL expression to a <CODE>boolean</CODE>.
     *
     * @param   attributeName   the name of the tag attribute whose value
     *                          is the expression that must be evaluated.
     * @param   expression      the expression that is evaluated
     * @return                  the value of the evaluated expression
     */
    protected boolean evalBoolean(String attributeName, String expression)
            throws JspException {
        Object value = eval(attributeName, expression, Boolean.class);
        return ((Boolean) value).booleanValue();
    }

    /**
     * Evaluates a JSTL expression to a <CODE>int</CODE>.
     *
     * @param   attributeName   the name of the tag attribute whose value
     *                          is the expression that must be evaluated.
     * @param   expression      the expression that is evaluated
     * @return                  the value of the evaluated expression
     */
    protected int evalInt(String attributeName, String expression)
            throws JspException {
        Object value = eval(attributeName, expression, Integer.class);
        return ((Integer) value).intValue();
    }

    /**
     * Evaluates a JSTL expression to a <CODE>double</CODE>.
     *
     * @param   attributeName   the name of the tag attribute whose value
     *                          is the expression that must be evaluated.
     * @param   expression      the expression that is evaluated
     * @return                  the value of the evaluated expression
     */
    protected double evalDouble(String attributeName, String expression)
            throws JspException {
        Object value = eval(attributeName, expression, Double.class);
        return ((Double) value).doubleValue();
    }

    /**
     * Exports a JSP variable with the given name, value and scope.
     *
     * This method calls <CODE>pageContext.setAttribute()</CODE>
     * if the value is not <CODE>null</CODE>. Otherwise it calls
     * <CODE>pageContext.removeAttribute()</CODE>.
     *
     * @param   name    the name of the variable
     * @param   value   the value of the variable
     * @param   scope   the scope of the variable
     */
    protected void export(String name, Object value, int scope) {
        export(name, value, scope, DEFAULT_RESTORE_VALUE);
    }

    /**
     * Exports a JSP variable with the given name, value and scope.
     *
     * This method calls <CODE>pageContext.setAttribute()</CODE>
     * if the value is not <CODE>null</CODE>. Otherwise it calls
     * <CODE>pageContext.removeAttribute()</CODE>.
     *
     * <P>If <CODE>restoreValue</CODE> is <CODE>true</CODE> and there is
     * already a variable with the given name within the given scope,
     * the existent value is saved before setting the new value.
     * In this case, <CODE>doFinally()</CODE> will restore the old value.
     *
     * @param   name    the name of the variable
     * @param   value   the value of the variable
     * @param   scope   the scope of the variable
     * @param   restoreValue indicates if the old value should be restored.
     *                  If there is no old value, the new value will remain
     *                  available after <CODE>doFinally()</CODE>
     */
    protected void export(String name, Object value, int scope,
        boolean restoreValue) {
        if (restoreValue) {
            Object oldValue = pageContext.getAttribute(name, scope);
            if (oldValue != null) {
                int i = scope - 1;
                if (originalValues == null)
                    originalValues = new HashMap[4];
                if (originalValues[i] == null)
                    originalValues[i] = new HashMap();
                Object olderValue = originalValues[i].put(name, oldValue);
                if (olderValue != null) {
                    // This tag exports the same variable more than once
                    // Keep the original value in 'originalValues'
                    originalValues[i].put(name, olderValue);
                }
            }
        }
        if (value != null)
            pageContext.setAttribute(name, value, scope);
        else
            pageContext.removeAttribute(name, scope);
    }

    /**
     * Restores the values of the variables that were exported with
     * <CODE>restoreValue == true</CODE> and had a previous value.
     * This method is called by <CODE>doFinally()</CODE>.
     */
    protected void restore() {
        if (originalValues != null)
            for (int i = 0; i < originalValues.length; i++)
                if (originalValues[i] != null) {
                    int scope = i + 1;
                    Iterator iterator = originalValues[i].entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        String name = (String) entry.getKey();
                        Object value = entry.getValue();
                        pageContext.setAttribute(name, value, scope);
                    }
                    originalValues[i].clear();
                }
    }

    /**
     * Catches an exception that was thrown during the handling of the tag
     * or during the processing of its body. If the debug flag is
     * <CODE>true</CODE>, this method throws the exception further.
     * Otherwise it logs the exception.
     *
     * @param   t       the exception that occurred during
     *                  tag handling or body processing
     * @throws  Throwable   the same exception if the debug flag is
     *                  <CODE>true</CODE>
     */
    public void doCatch(Throwable t) throws Throwable {
        if (getDebug())
            throw t;
        else
            log(getTagName(), t);
    }

    /**
     * This method is invoked by the Servlet container after tag handling.
     * It calls <CODE>restore()</CODE> and <CODE>init()</CODE>.
     */
    public void doFinally() {
        restore();
        init();
    }

}
