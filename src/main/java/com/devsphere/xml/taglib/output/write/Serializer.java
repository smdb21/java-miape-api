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

package com.devsphere.xml.taglib.output.write;

import org.w3c.dom.Node;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import javax.servlet.jsp.JspException;

import java.io.Writer;
import java.util.Properties;

/**
 * This class provides support for serializing SAX events and DOM trees.
 */
public class Serializer {
    private static SAXTransformerFactory traxFactory;
    protected Filter filter;
    protected Properties properties;
    protected Transformer domToStream;
    protected TransformerHandler saxToStream;

    /**
     * Initializes the serializer
     */
    public Serializer() {
        filter = new Filter();
    }

    /**
     * Gets the current writer used for output.
     *
     * @return  the current writer used for output
     */
    public Writer getOut() {
        return filter.getOut();
    }

    /**
     * Sets the current writer used for output.
     *
     * @param   out     the current writer used for output
     */
    public void setOut(Writer out) {
        filter.setOut(out);
    }

    /**
     * Gets the output properties of the serializer.
     *
     * @return  the output properties
     */
    public Properties getOutputProperties() {
        return properties;
    }

    /**
     * Sets the output properties of the serializer.
     *
     * @param   properties  the output properties
     */
    public void setOutputProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * Serializes a DOM tree.
     *
     * @param   dom             the root of the DOM tree
     * @throws  JspException    to signal any error that might occur
     */
    public void write(Node dom) throws JspException {
        try {
            if (domToStream == null)
                domToStream = getTransformerFactory().newTransformer();
            if (properties != null)
                   domToStream.setOutputProperties(properties);
            DOMSource source = new DOMSource(dom);
              StreamResult result = new StreamResult(getOut());
            domToStream.transform(source, result);
        } catch (TransformerException e) {
            throw new JspException(e);
        }
    }

    /**
     * Returns a SAX handler that can be used to serialize SAX events.
     *
     * @return                  a SAX handler that serializes SAX events
     * @throws  JspException    to signal any error that might occur
     */
    public TransformerHandler getHandler() throws JspException {
        if (saxToStream == null)
            try {
                StreamResult result = new StreamResult(filter);
                saxToStream = getTransformerFactory().newTransformerHandler();
                if (properties != null)
                    saxToStream.getTransformer().setOutputProperties(properties);
                saxToStream.setResult(result);
            } catch (TransformerException e) {
                throw new JspException(e);
            }
        return saxToStream;
    }

    /**
     * Returns a JAXP transformer factory.
     *
     * @return  a JAXP transformer factory
     */
    protected static synchronized SAXTransformerFactory getTransformerFactory() {
        if (traxFactory == null)
            traxFactory
                = (SAXTransformerFactory) TransformerFactory.newInstance();
        return traxFactory;
    }

}
