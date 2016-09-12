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

package com.devsphere.xml.taglib.process.event;

import org.xml.sax.Attributes;

import java.util.HashMap;
import java.util.Map;

/**
 * This class wraps the parameters of the <CODE>startElement()</CODE> SAX event.
 */
public class StartElement extends EventSupport {
    private String uri;
    private String name;
    private String qname;
    private Map attr;
    private boolean wantDOM;

    /**
     * Initializes the event.
     *
     * @param   indentLevel     the indent level of the element
     * @param   namespaceURI    the element's namespace URI
     * @param   localName       the element's local name
     * @param   qualifiedName   the element's qualified name
     * @param   attributes      the element's attributes
     */
    public StartElement(int indentLevel, String namespaceURI, String localName,
            String qualifiedName, Attributes attributes) {
        super(indentLevel);
        this.uri = namespaceURI;
        this.name = localName;
        this.qname = qualifiedName;
        this.attr = new HashMap();
        int len = attributes.getLength();
        for (int i = 0; i < len; i++)
            this.attr.put(attributes.getLocalName(i), attributes.getValue(i));
        this.wantDOM = false;
    }

    /**
     * Returns the element's namespace URI
     *
     * @return the element's namespace URI
     */
    public String getUri() {
        return uri;
    }

    /**
     * Returns the element's local name
     *
     * @return the element's local name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the element's qualified name
     *
     * @return the element's qualified name
     */
    public String getQname() {
        return qname;
    }

    /**
     * Returns the element's attributes
     *
     * @return the element's attributes
     */
    public Map getAttr() {
        return attr;
    }

    /**
     * Returns the <CODE>wantDOM</CODE> flag used by the SAXDOMIX controller.
     *
     * @return      the flag that indicates whether the parsing should continue
     *              in SAX mode or a DOM sub-tree should be built by SAXDOMIX.
     */
    public boolean getWantDOM() {
        return wantDOM;
    }
    /**
     * Sets the <CODE>wantDOM</CODE> flag used by the SAXDOMIX controller.
     *
     * @param   wantDOM     the flag that indicates whether the parsing should
     *                      continue in SAX mode or a DOM sub-tree should be
     *                      built by SAXDOMIX.
     */
    public void setWantDOM(boolean wantDOM) {
        this.wantDOM = wantDOM;
    }

    /**
     * Returns a text containing the values of some of the properties of
     * this event, including its type. It is primarily intended for debugging.
     *
     * @return      a string representation of this event
     */
    public String toString() {
        StringBuffer buf = new StringBuffer(super.toString());
        buf.append("startElement(");
        if (uri != null && uri.length() > 0) {
            buf.append(uri);
            buf.append(", ");
        }
        if (name != null) {
            buf.append(name);
            buf.append(", ");
        }
        if (qname != null && !qname.equals(name)) {
            buf.append(qname);
            buf.append(", ");
        }
        if (attr != null && !attr.isEmpty()) {
            buf.append(attr.toString());
            buf.append(", ");
        }
        buf.setLength(buf.length() - 2);
        buf.append(")");
        return buf.toString();
    }

}
