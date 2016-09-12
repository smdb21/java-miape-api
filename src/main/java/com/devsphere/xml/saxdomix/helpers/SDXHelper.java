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

import org.xml.sax.Attributes;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * <CODE>SDXBuilder</CODE> needs a factory for DOM nodes that implements
 * this interface.
 *
 * @see com.devsphere.xml.saxdomix.SDXBuilder
 * @see com.devsphere.xml.saxdomix.helpers.DefaultSDXHelper
 */
public interface SDXHelper {
    /**
     * Creates a DOM <CODE>Element</CODE> node and appends it as a child
     * to the given <CODE>parent</CODE> element. The attributes stored by
     * the SAX object are retrieved and set to the created DOM object.
     *
     * @param   namespaceURI    The namespace URI for the new element
     * @param   localName       The local name for the new element
     * @param   qualifiedName   The qualified name for the new element
     * @param   attributes      The attributes for the new element
     * @param   parent          The parent for the new element or
     *                          <CODE>null</CODE> if this is a root element
     * @return                  The created DOM <CODE>Element</CODE>
     */
    public Element createElement(String namespaceURI, String qualifiedName,
            Attributes attributes, Element parent);

    /**
     * Creates a DOM <CODE>Text</CODE> node and appends it as a child
     * to the given <CODE>parent</CODE> element or just appends the data
     * to the last child of <CODE>parent</CODE> if that last child is
     * a <CODE>Text</CODE> node. In other words, this method doesn't allow
     * the creation of adjacent <CODE>Text</CODE> nodes and creates
     * a <CODE>Text</CODE> node only when this is necessary.
     *
     * @param   data            The character data for the text node
     * @param   parent          The parent for the text node
     * @return                  The created or existent <CODE>Text</CODE> node
     */
    public Text createTextNode(String data, Element parent);

    /**
     * Creates a DOM <CODE>CDATASection</CODE> node and appends it as a child
     * to the given <CODE>parent</CODE> element or just appends the data
     * to the last child of <CODE>parent</CODE> if that last child is
     * a <CODE>CDATASection</CODE> node and <CODE>newCDATA</CODE> is
     * <CODE>false</CODE>. In other words, this method avoids the creation
     * of adjacent <CODE>CDATASection</CODE> nodes and creates a
     * <CODE>CDATASection</CODE> node only when this is necessary or required.
     *
     * @param   data            The character data for the CDATA section
     * @param   newCDATA        Indicates the beginning of a new CDATA section
     * @param   parent          The parent for the CDATA section
     * @return                  The created or existent
     *                          <CODE>CDATASection</CODE> node
     */
    public CDATASection createCDATASection(String data, boolean newCDATA,
            Element parent);

    /**
     * Creates a DOM <CODE>ProcessingInstruction</CODE> node and appends it
     * as a child to the given <CODE>parent</CODE> element.
     *
     * @param   target          The target for the new processing instruction
     * @param   data            The data for the new processing instruction
     * @param   parent          The parent for the new processing instruction
     * @return                  The created <CODE>ProcessingInstruction</CODE>
     */
    public ProcessingInstruction createProcessingInstruction(
            String target, String data, Element parent);

    /**
     * Creates a DOM <CODE>Comment</CODE> node and appends it as a child
     * to the given <CODE>parent</CODE> element.
     *
     * @param   data            The data for the new comment
     * @param   parent          The parent for the new comment
     * @return                  The created <CODE>Comment</CODE> node
     */
    public Comment createComment(String data, Element parent);
}
