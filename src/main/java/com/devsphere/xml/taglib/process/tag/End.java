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

import com.devsphere.xml.taglib.process.event.EndDocument;
import com.devsphere.xml.taglib.process.event.EndElement;

import javax.servlet.jsp.JspException;

import java.util.Arrays;
import java.util.List;

/**
 * Handler for the &lt;p:end&gt; tag, whose body can process an
 * <CODE>endElement()</CODE> or <CODE>endDocument()</CODE> SAX event.
 */
public class End extends ProcessSupport {
    private static final List ALLOWED_ANCESTORS = Arrays.asList(new String[] {
        "parse",
        "element"
    });

    /**
     * Initializes the fields of this tag handler.
     */
    protected void init() {
        super.init();
    }

    /**
     * Returns the name of the handled tag
     *
     * @return  the name of the handled tag
     */
    public String getTagName() {
        return "end";
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
     * Initializes the loop to process end-element and end-document events.
     *
     * @throws  JspException    to signal any tag validation error
     */
    protected void initLoop() throws JspException {
        super.initLoop();
        handler = ancestor.handler;
        indentLevel = ancestor.indentLevel;
    }

    /**
     * Returns <CODE>true</CODE> if the current event is a
     * <CODE>EndElement</CODE> or <CODE>EndDocument</CODE> instance.
     * The element variables are re-exported just in case some other
     * contained tag modified their values.
     *
     * @return  <CODE>true</CODE> if the event loop should be started
     * @throws  JspException    to signal an error
     */
    protected boolean startLoop() throws JspException {
        if (getEvent())
            if (indentLevel == event.getIndentLevel()
                    && (event instanceof EndElement
                    || event instanceof EndDocument)) {
                if (event instanceof EndElement
                        && ancestor instanceof Element) {
                    // Re-export the element variables
                    ((Element) ancestor).exportVariables();
                }
                event.setConsumed(true);
                logEvent();
                return true;
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
