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

/**
 * This default factory for DOM nodes called helper is used
 * by <CODE>SDXBuilder</CODE>.
 *
 * @see com.devsphere.xml.saxdomix.SDXBuilder
 * @see com.devsphere.xml.saxdomix.helpers.SDXHelper
 * @see com.devsphere.xml.saxdomix.helpers.DefaultSDXOldHelper
 */
public class DefaultSDXHelper extends DefaultSDXOldHelper
        implements SDXHelper {
    /**
     * Creates a new helper.
     */
    public DefaultSDXHelper() {
        super();
    }

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
            Attributes attributes, Element parent) {
        // Create the DOM Element node
        Element element = document.createElementNS(namespaceURI, qualifiedName);

        if (attributes != null) {
            // Add the attributes
            int n = attributes.getLength();
            for (int i = 0; i < n; i++)
                element.setAttributeNS(
                    attributes.getURI(i),
                    attributes.getQName(i),
                    attributes.getValue(i));
        }

        if (parent != null)
            // Append the new node to its parent
            parent.appendChild(element);

        // Return the created node
        return element;
    }

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
            Element parent) {
        CDATASection cdataSection = null;
        if (!newCDATA && parent.getLastChild() instanceof CDATASection) {
            // The data we received must be added to an existent node
            // Get the CDATASection node
            cdataSection = (CDATASection) parent.getLastChild();

            // Append the data to the existent node
            cdataSection.appendData(data);
        } else {
            // We've just entered in a new CDATA section.
            // Create a new CDATASection node
            cdataSection = document.createCDATASection(data);

            // Append the new node to its parent
            parent.appendChild(cdataSection);
        }

        // Return the node that now stores the data
        return cdataSection;
    }

    /**
     * Creates a DOM <CODE>Comment</CODE> node and appends it as a child
     * to the given <CODE>parent</CODE> element.
     *
     * @param   data            The data for the new comment
     * @param   parent          The parent for the new comment
     * @return                  The created <CODE>Comment</CODE> node
     */
    public Comment createComment(String data, Element parent) {
        // Create a new DOM Comment node
        Comment comment = document.createComment(data);

        // Append the new node to its parent
        parent.appendChild(comment);

        // Return the created node
        return comment;
    }

}
