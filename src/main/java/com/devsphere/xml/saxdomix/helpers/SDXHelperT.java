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

package com.devsphere.xml.saxdomix.helpers;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import org.w3c.dom.Element;

/**
 * <CODE>SDXBuilderT</CODE> needs a factory for DOM sub-trees that implements
 * this interface.
 *
 * @see com.devsphere.xml.saxdomix.SDXBuilderT
 * @see com.devsphere.xml.saxdomix.helpers.DefaultSDXHelperT
 */
public interface SDXHelperT {
    /**
     * Prepares the helper for building a DOM sub-tree from SAX events.
     * After calling this method, the user of a class that implements this
     * interface may feed the <CODE>ContentHandler</CODE> and
     * <CODE>LexicalHandler</CODE> with SAX events. Then,
     * the <CODE>endBuilding()</CODE> method must be called in order
     * to obtain the root of the created DOM sub-tree.
     *
     * @throws  SAXException    If an error must be signaled
     */
    public void startBuilding() throws SAXException;

    /**
     * This method must be called after the dispatching of the last SAX event
     * of the sequence that was used for building the current DOM sub-tree.
     * The SAX handlers may not receive events until the next call of
     * <CODE>startBuilding()</CODE>, which will mark the beginning of the
     * construction of another DOM sub-tree.
     *
     * @return                  The root of the created DOM sub-tree
     * @throws  SAXException    If an error must be signaled
     */
    public Element endBuilding() throws SAXException;

    /**
     * Returns the SAX <CODE>ContentHandler</CODE> that should receive
     * the events between <CODE>startBuilding()</CODE> and
     * <CODE>endBuilding()</CODE>.
     *
     * @return                  The <CODE>ContentHandler</CODE> of this helper
     */
    public ContentHandler getContentHandler();

    /**
     * Returns the SAX <CODE>LexicalHandler</CODE> that should receive
     * the events between <CODE>startBuilding()</CODE> and
     * <CODE>endBuilding()</CODE>.
     *
     * @return                  The <CODE>LexicalHandler</CODE> of this helper
     */
    public LexicalHandler getLexicalHandler();
}
