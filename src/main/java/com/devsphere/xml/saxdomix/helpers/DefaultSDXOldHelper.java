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

import org.xml.sax.AttributeList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This default factory for DOM nodes called helper is used
 * by <CODE>SDXOldBuilder</CODE>.
 *
 * @see com.devsphere.xml.saxdomix.SDXOldBuilder
 * @see com.devsphere.xml.saxdomix.helpers.SDXOldHelper
 */
public class DefaultSDXOldHelper implements SDXOldHelper {
    /** Factory for JAXP <CODE>DocumentBuilder</CODE> objects */
    protected static DocumentBuilderFactory jaxpFactory;

    /** Factory for DOM <CODE>Document</CODE> objects */
    protected static DocumentBuilder jaxpBuilder;

    static {
        try {
            // Create the JAXP builder factory
            jaxpFactory = DocumentBuilderFactory.newInstance();

            // Create the JAXP document builder
            jaxpBuilder = jaxpFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /** DOM <CODE>Document</CODE> used as factory for DOM nodes */
    protected Document document;

    /**
     * Creates a new helper.
     */
    public DefaultSDXOldHelper() {
        document = jaxpBuilder.newDocument();
    }

    /**
     * Creates a DOM <CODE>Element</CODE> node and appends it as a child
     * to the given <CODE>parent</CODE> element. The attributes stored by
     * the SAX object are retrieved and set to the created DOM object.
     *
     * @param   name            The name for the new element
     * @param   attributes      The attributes for the new element
     * @param   parent          The parent for the new element or
     *                          <CODE>null</CODE> if this is a root element
     * @return                  The created DOM <CODE>Element</CODE>
     */
    public Element createElement(String name, AttributeList attributes,
            Element parent) {
        // Create the DOM Element node
        Element element = document.createElement(name);

        if (attributes != null) {
            // Add the attributes
            int n = attributes.getLength();
            for (int i = 0; i < n; i++)
                element.setAttribute(
                    attributes.getName(i),
                    attributes.getValue(i));
        }

        if (parent != null)
            // Append the new node to its parent
            parent.appendChild(element);

        // Return the created node
        return element;
    }

    /**
     * Creates a DOM <CODE>Text</CODE> node and appends it as a child
     * to the given <CODE>parent</CODE> element or just appends the data
     * to the last child of <CODE>parent</CODE> if that last child is
     * a <CODE>Text</CODE> node. In other words, this method avoids the
     * creation of adjacent <CODE>Text</CODE> nodes and creates
     * a <CODE>Text</CODE> node only when this is necessary.
     *
     * @param   data            The character data for the text node
     * @param   parent          The parent for the text node
     * @return                  The created or existent <CODE>Text</CODE> node
     */
    public Text createTextNode(String data, Element parent) {
        Text textNode = null;
        if (parent.getLastChild() instanceof Text) {
            // The data we received must be added to an existent node
            // Get the Text node
            textNode = (Text) parent.getLastChild();

            // Append the data to the existent node
            textNode.appendData(data);
        } else {
            // We've just entered in a new text section.
            // Create a new Text node
            textNode = document.createTextNode(data);

            // Append the new node to its parent
            parent.appendChild(textNode);
        }

        // Return the node that now stores the data
        return textNode;
    }

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
            String target, String data, Element parent) {
        // Create a new DOM ProcessingInstruction node
        ProcessingInstruction processingInstruction
            = document.createProcessingInstruction(target, data);

        // Append the new node to its parent
        parent.appendChild(processingInstruction);

        // Return the created node
        return processingInstruction;
    }

}
