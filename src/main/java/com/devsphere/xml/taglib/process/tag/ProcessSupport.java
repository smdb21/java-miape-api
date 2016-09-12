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
import com.devsphere.xml.taglib.process.event.EventSupport;
import com.devsphere.xml.taglib.support.tag.CommonSupport;

import javax.servlet.jsp.JspException;

import java.util.List;

/**
 * This class provides support for XML processing tag handlers.
 * It provides methods for looping over a sequence of SAX events.
 */
public abstract class ProcessSupport extends CommonSupport {
    /** The tag handler of the nearest ancestor tag */
    protected ProcessSupport ancestor;

    /** The event handler */
    protected Handler handler;

    /** The event handled by this object */
    protected EventSupport event;

    /** The indent level of the tag */
    protected int indentLevel;

    private int loopCounter;

    /**
     * Initializes the fields of this tag handler.
     */
    protected void init() {
        super.init();
        ancestor = null;
        handler = null;
        event = null;
        indentLevel = -1;
        loopCounter = -1;
    }

    /**
     * Verify if the handled tag was placed in the right context.
     * This methods throws a <CODE>JspException</CODE> if
     * <UL>
     * <LI><CODE>ancestor</CODE> is <CODE>null</CODE>,
     * but the allowed ancestors list is not empty or
     * <LI><CODE>ancestor</CODE> is not <CODE>null</CODE>,
     * but the allowed ancestors list is empty or
     * <LI>the name of the given <CODE>ancestor</CODE> tag
     * is not found in the allowed ancestors list
     * returned by <CODE>getAllowedAncestors()</CODE> or
     * <LI>the ancestor tag is an &lt;p:element&gt; tag with a dom variable.
     * Such tags may not contain tags of this library because a DOM sub-tree
     * is constructed instead of generating SAX events.
     * </UL>
     *
     * @param   ancestor    the tag handler of an ancestor whose name
     *                      is searched in the list of allowed ancestors
     * @throws  JspException    to signal a tag validation error
     */
    protected void validate(CommonSupport ancestor) throws JspException {
        super.validate(ancestor);
        if (ancestor instanceof Element
                && ((Element) ancestor).getWantDOM())
            throw new JspException("JSP Validation Error: "
                + "'" + getTagName() + "' tags "
                + "may not be placed within an 'element' tag "
                + "that processes a DOM tree. Remove either the contained tag "
                + "or the 'varDom' attribute of the parent 'element' tag.");
    }

    /**
     * Gets the current parsing event and returns true if this isn't
     * <CODE>null</CODE>.
     *
     * @return  <CODE>true</CODE> if an event is available for processing
     */
    protected boolean getEvent() {
        event = handler.getEvent();
        return event != null;
    }

    /**
     * Tries to removes the current parsing event in order to make room for
     * another event.
     *
     * @return  <CODE>true</CODE> if the event was removed
     */
    protected boolean removeEvent() {
        return handler.removeEvent(event);
    }

    /**
     * Logs an event if the debug flag is <CODE>true</CODE>.
     */
    protected void logEvent() {
        if (getDebug()) {
            String status = event.isConsumed() ? "C" : "";
            logEvent(status);
        }
    }

    /**
     * Logs an event if the debug flag is <CODE>true</CODE>.
     *
     * @param   status      a string describing the status of the event
     */
    protected void logEvent(String status) {
        if (getDebug()) {
            StringBuffer buf = new StringBuffer();
            buf.append(getTagName());
            buf.append('\t');
            buf.append(indentLevel);
            buf.append(' ');
            buf.append('#');
            buf.append(loopCounter+1);
            if (status != null && status.length() > 0) {
                buf.append(' ');
                buf.append(status);
            }
            buf.append('\t');
            buf.append(event);
            log(buf.toString());
        }
    }

    /**
     * Initializes the event loop, finding the nearest ancestor tag so that
     * the current tag can be validated and handled within its context.
     *
     * @throws  JspException    to signal a tag validation error
     */
    protected void initLoop() throws JspException {
        ancestor = (ProcessSupport) findAncestorWithClass(this,
            ProcessSupport.class);
        if (ancestor != null) {
            cachedDebug = ancestor.cachedDebug;
            cachedLogger = ancestor.cachedLogger;
        }
        validate(ancestor);
    }

    /**
     * Subclasses must implement this method and return <CODE>true</CODE>
     * if the event loop should be started.
     *
     * @return  <CODE>true</CODE> if the event loop should be started
     * @throws  JspException    to signal an error
     */
    protected abstract boolean startLoop() throws JspException;

    /**
     * Subclasses must implement this method and return <CODE>true</CODE>
     * if the event loop should be continued.
     *
     * @return  <CODE>true</CODE> if the event loop should be continued
     * @throws  JspException    to signal an error
     */
    protected abstract boolean continueLoop() throws JspException;

    /**
     * Subclasses may override this method if they need to execute some
     * operations after the event loop is finished.
     */
    protected void finalizeLoop() {
    }

    /**
     * Calls <CODE>initLoop()</CODE> and then <CODE>startLoop()</CODE>.
     * If the latter returns <CODE>true</CODE>, this method returns
     * <CODE>EVAL_BODY_INCLUDE</CODE> so that the body of the tag is evaluated
     * getting the chance to process one or more parsing events.
     * Otherwise it returns <CODE>SKIP_BODY</CODE>.
     *
     * @return  <CODE>EVAL_BODY_INCLUDE</CODE> or <CODE>SKIP_BODY</CODE>
     *          depending on the value returned by <CODE>startLoop()</CODE>
     * @throws  JspException    to signal an error
     */
    public int doStartTag() throws JspException {
        loopCounter = 0;
        initLoop();
        if (startLoop())
            return EVAL_BODY_INCLUDE;
        else
            return SKIP_BODY;
    }

    /**
     * Calls <CODE>continueLoop()</CODE>. If that returns <CODE>true</CODE>,
     * this method returns <CODE>EVAL_BODY_AGAIN</CODE> so that the body of
     * the tag is re-evaluated getting the chance to process new events.
     * Otherwise it returns <CODE>SKIP_BODY</CODE>.
     *
     * @return  <CODE>EVAL_BODY_AGAIN</CODE> or <CODE>SKIP_BODY</CODE>
     *          depending on the value returned by <CODE>continueLoop()</CODE>
     * @throws  JspException    to signal an error
     */
    public int doAfterBody() throws JspException {
        if (event != null && !event.isConsumed())
            logEvent("NC");
        loopCounter++;
        if (continueLoop())
            return EVAL_BODY_AGAIN;
        else
            return SKIP_BODY;
    }

    /**
     * Returns <CODE>EVAL_PAGE</CODE>.
     *
     * @return  <CODE>EVAL_PAGE</CODE>.
     */
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    /**
     * Calls <CODE>finalizeLoop()</CODE>
     */
    public void doFinally() {
        finalizeLoop();
        super.doFinally();
    }

}
