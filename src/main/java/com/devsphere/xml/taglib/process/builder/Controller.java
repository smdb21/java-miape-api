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

package com.devsphere.xml.taglib.process.builder;

import com.devsphere.xml.taglib.support.builder.Outputter;

import com.devsphere.xml.saxdomix.SDXController;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.Arrays;
import java.util.List;

/**
 * This class implements <CODE>com.devsphere.xml.saxdomix.SDXController</CODE>
 * to generate JSP code for handling the DOM sub-trees.
 *
 * <P>An instance of this class is created by a <CODE>Main</CODE> object,
 * which is referred further as SAXDOMIX builder because it inherits
 * <CODE>com.devsphere.xml.saxdomix.SDXBuilder</CODE> through
 * <CODE>com.devsphere.xml.taglib.support.builder.BuilderSupport</CODE>.
 */
public class Controller implements SDXController {
    private Parameters params;
    private Outputter out;
    private boolean ignoreSpaces;
    private List rootElements;

    /**
     * Initializes the controller.
     *
     * @param   params  the command line parameters
     * @param   out     the object used to output the generated code
     */
    public Controller(Parameters params, Outputter out) {
        this.params = params;
        this.out = out;
        this.ignoreSpaces = params.getIgnoreSpaces();
        this.rootElements = Arrays.asList(params.getRootElements());
    }

    /**
     * Generates code for handling a DOM sub-tree.
     *
     * Returns <CODE>true</CODE> when the application wants to receive a DOM
     * sub-tree for handling. This happens when the qualified name of the
     * current element was specified in the <CODE>rootElements</CODE> list.
     *
     * <P>During the SAX parsing of the XML sample, a builder object
     * invokes the controller's <CODE>wantDOM()</CODE> method for each
     * <CODE>startElement()</CODE> parsing event. If <CODE>wantDOM()</CODE>
     * returns <CODE>false</CODE> the SAX parsing mode continues.
     * When <CODE>wantDOM()</CODE> returns <CODE>true</CODE> the builder
     * enters in DOM parsing mode and starts creating a DOM sub-tree from
     * all SAX events between the current <CODE>startElement()</CODE>
     * and the corresponding <CODE>endElement()</CODE>.
     *
     * @param   namespaceURI    The element's namespace URI
     * @param   localName       The element's local name
     * @param   qualifiedName   The element's qualified name
     * @param   attributes      The element's attributes
     * @return                  A boolean value indicating whether the builder
     *                          must enter in DOM parsing mode or must remain
     *                          in SAX parsing mode
     * @throws  SAXException    If an error must be signaled
     */
    public boolean wantDOM(String namespaceURI, String localName,
            String qualifiedName, Attributes attributes) throws SAXException {
        if (!rootElements.contains(qualifiedName))
            return false;
        StringBuffer buf = new StringBuffer();
        buf.append("<p:element varName=\"name\" varAttr=\"attr\" ");
        buf.append("varDom=\"dom\" testName=\"");
        buf.append(localName);
        buf.append("\">");
        out.println(buf.toString());
        out.incLevel();
        buf.setLength(0);
        buf.append("<%-- dom=\"...\" name=\"");
        buf.append(localName);
        buf.append("\"");
        for (int i = 0; i < attributes.getLength(); i++) {
            buf.append(", attr.");
            buf.append(attributes.getLocalName(i));
            buf.append("=\"");
            buf.append(attributes.getValue(i));
            buf.append("\"");
        }
        buf.append(" --%>");
        out.println(buf.toString());
        return true;
    }

    /**
     * Generates code for handling a DOM sub-tree. This method will call
     * <CODE>xpathExpressions()</CODE> to generate a list of valid XPath
     * expressions that can be evaluated in the context of the sample
     * DOM sub-tree.
     *
     * <P>After <CODE>wantDOM()</CODE> returns <CODE>true</CODE>, the builder
     * constructs a DOM sub-tree from SAX events and passes it to the
     * controller's <CODE>handleDOM()</CODE> method. After handling, the builder
     * returns to the SAX parsing mode.
     *
     * @param   element         The root of the DOM sub-tree
     * @throws  SAXException    If an error must be signaled
     */
    public void handleDOM(Element element) throws SAXException {
        out.println("<%-- XPath expressions:");
        out.incLevel();
        xpathExpressions("$dom", element);
        out.decLevel();
        out.println("--%>");
        out.decLevel();
        out.println("</p:element>");
    }

    /**
     * Generates a list of valid XPath expressions that can be evaluated
     * in the given context.
     *
     * @param   xpath           The expression used to refer the DOM sub-tree.
     * @param   context         The root of the DOM sub-tree whose XPaths
     *                          must be generated.
     */
    protected void xpathExpressions(String xpath, Element context) {
        out.println(xpath);
        out.incLevel();
        NamedNodeMap attrMap = context.getAttributes();
        if (attrMap != null)
            for (int i = 0; i < attrMap.getLength(); i++) {
                Node attr = attrMap.item(i);
                if (attr instanceof Attr) {
                    String attrName = ((Attr) attr).getName();
                    out.println("string(" + xpath + "/@" + attrName + ")");
                }
            }
        boolean hasData = false;
        Node child = context.getFirstChild();
        while (child != null) {
            if (child instanceof Element) {
                String tagName = ((Element) child).getTagName();
                xpathExpressions(xpath + "/" + tagName, (Element) child);
            } else if (!hasData && child instanceof CharacterData) {
                if (ignoreSpaces) {
                    String data = ((CharacterData) child).getData();
                    if (data.trim().length() > 0)
                        hasData = true;
                } else
                    hasData = true;
            }
            if (hasData)
                out.println("string(" + xpath + "/text())");
            child = child.getNextSibling();
        }
        out.decLevel();
    }

}
