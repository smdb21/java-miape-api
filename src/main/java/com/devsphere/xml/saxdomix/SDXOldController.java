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

package com.devsphere.xml.saxdomix;

import org.xml.sax.AttributeList;
import org.xml.sax.SAXException;

import org.w3c.dom.Element;

/**
 * The application implements this interface in order to control the mixed
 * SAX 1.0 - DOM Level 1 parsing.
 *
 * <P>The <code>com.devsphere.xml.saxdomix.SDXOldBuilder</code> class of the
 * framework provides the mixed SAX 1.0 - DOM Level 1 parsing functionality.
 *
 * @see com.devsphere.xml.saxdomix.SDXOldBuilder
 */
public interface SDXOldController {
    /**
     * Returns <CODE>true</CODE> when the application wants to receive
     * a DOM sub-tree for handling.
     *
     * <P>During the SAX parsing, a builder object invokes the controller's
     * <CODE>wantDOM()</CODE> method for each <CODE>startElement()</CODE>
     * parsing event. If <CODE>wantDOM()</CODE> returns <CODE>false</CODE>
     * the SAX parsing mode continues. When <CODE>wantDOM()</CODE> returns
     * <CODE>true</CODE> the builder enters in DOM parsing mode and starts
     * creating a DOM sub-tree from all SAX events between the current
     * <CODE>startElement()</CODE> and the corresponding
     * <CODE>endElement()</CODE>.
     *
     * @param   name            The element's name
     * @param   attributes      The element's attributes
     * @return                  A boolean value indicating whether the builder
     *                          must enter in DOM parsing mode or must remain
     *                          in SAX parsing mode
     * @throws  SAXException    If an error must be signaled
     */
    public boolean wantDOM(String name, AttributeList attributes)
            throws SAXException;

    /**
     * Receives the DOM sub-trees for handling.
     *
     * <P>After <CODE>wantDOM()</CODE> returns <CODE>true</CODE>, the builder
     * constructs a DOM sub-tree from SAX events and passes it to the
     * controller's <CODE>handleDOM()</CODE> method. After handling, the builder
     * returns to the SAX parsing mode.
     *
     * @param   element         The root of the DOM sub-tree
     * @throws  SAXException    If an error must be signaled
     */
    public void handleDOM(Element element) throws SAXException;

}
