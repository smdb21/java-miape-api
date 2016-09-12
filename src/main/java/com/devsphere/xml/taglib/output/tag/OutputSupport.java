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
import com.devsphere.xml.taglib.support.tag.CommonSupport;

import org.xml.sax.SAXException;

import javax.servlet.jsp.JspException;

import java.io.IOException;
import java.io.Writer;

/**
 * This class provides support for XML output tag handlers.
 */
public abstract class OutputSupport extends CommonSupport {
    /** The tag handler of the nearest ancestor tag. */
    protected OutputSupport ancestor;

    /** An object used to produce the XML content. */
    protected Serializer serializer;

    /** The previous JSP out used by the serializer object. */
    protected Writer previousOut;

    /**
     * Initializes the fields of this tag handler.
     */
    protected void init() {
        super.init();
        ancestor = null;
        serializer = null;
        previousOut = null;
    }

    /**
     * Returns <CODE>false</CODE>. Subclasses may override this method to return
     * <CODE>true</CODE> if the body content of the handled tag should be
     * ignored during the execution of the JSP page.
     *
     * @return  a flag indicating whether the body content of this tag should be
     *          ignored or evaluated
     */
    protected boolean shouldIgnoreContent() {
        return false;
    }

    /**
     * Returns <CODE>false</CODE>. Subclasses may override this method to return
     * <CODE>true</CODE> if the body content of the handled tag should be
     * buffered during the execution of the JSP page.
     *
     * @return  a flag indicating if the body content of this tag should be
     *          buffered
     */
    protected boolean shouldBufferContent() {
        return false;
    }

    /**
     * Initializes this tag handler for output, finding the nearest ancestor
     * tag so that the current tag can be validated and handled within its
     * context.
     *
     * @throws  JspException    to signal a tag validation error
     */
    protected void initOutput() throws JspException {
        ancestor = (OutputSupport) findAncestorWithClass(this,
            OutputSupport.class);
        if (ancestor != null) {
            cachedDebug = ancestor.cachedDebug;
            cachedLogger = ancestor.cachedLogger;
            serializer = ancestor.serializer;
        }
        validate(ancestor);
    }

    /**
     * Subclasses must implement this method to start the XML content
     * that the handled tag is supposed to produce.
     *
     * @throws  JspException    to signal an error
     * @throws  SAXException    to signal an error
     */
    protected abstract void startOutput() throws JspException, SAXException;

    /**
     * Subclasses must implement this method to end the XML content
     * that the handled tag is supposed to produce.
     *
     * @throws  JspException    to signal an error
     * @throws  SAXException    to signal an error
     */
    protected abstract void endOutput() throws JspException, SAXException;

    /**
     * Produces an empty comment. This forces the serializer to flush its
     * buffer. Those empty comments are eliminated by a filter and do not show
     * up in the final output.
     *
     * @throws  JspException    to signal an error
     * @throws  SAXException    to signal an error
     */
    protected void emptyComment() throws JspException, SAXException {
        serializer.getHandler().comment(new char[0], 0, 0);
    }

    /**
     * Calls <CODE>initOutput()</CODE> and then <CODE>startOutput()</CODE>.
     * This method returns
     * <UL>
     * <LI><CODE>SKIP_BODY</CODE> if <CODE>shouldIgnoreContent()</CODE>
     * returns <CODE>true</CODE>
     * <LI><CODE>EVAL_BODY_BUFFERED</CODE> if <CODE>shouldIgnoreContent()</CODE>
     * returns <CODE>false</CODE> and <CODE>shouldBufferContent()</CODE>
     * returns <CODE>true</CODE>
     * <LI><CODE>EVAL_BODY_INCLUDE</CODE> if <CODE>shouldIgnoreContent()</CODE>
     * returns <CODE>false</CODE> and <CODE>shouldBufferContent()</CODE>
     * returns <CODE>false</CODE>
     * </UL>
     *
     * @return  a code that tells the Servlet container what to do with the
     *          body content of the handled tag.
     * @throws  JspException    to signal an error
     */
    public int doStartTag() throws JspException {
        initOutput();
        if (serializer != null) {
            previousOut = serializer.getOut();
            serializer.setOut(pageContext.getOut());
        }
        try {
            startOutput();
        } catch (SAXException e) {
            throw new JspException(e);
        }
        if (shouldIgnoreContent())
            return SKIP_BODY;
        else if (shouldBufferContent())
            return EVAL_BODY_BUFFERED;
        else
            return EVAL_BODY_INCLUDE;
    }

    /**
     * Calls <CODE>endOutput()</CODE> and returns <CODE>EVAL_PAGE</CODE>.
     *
     * @return  <CODE>EVAL_PAGE</CODE>.
     */
    public int doEndTag() throws JspException {
        try {
            endOutput();
        } catch (SAXException e) {
            throw new JspException(e);
        }
        if (serializer != null)
            serializer.setOut(previousOut);
        return EVAL_PAGE;
    }

}
