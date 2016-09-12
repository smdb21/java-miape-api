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

package com.devsphere.xml.taglib.output.builder;

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
 * This class implements <CODE>com.devsphere.xml.saxdomix.SDXController</CODE>,
 * but it won't handle any DOM sub-trees because <CODE>wantDOM()</CODE>
 * always returns <CODE>false</CODE>.
 */
public class Controller implements SDXController {
    private Parameters params;
    private Outputter out;

    /**
     * Initializes the controller.
     *
     * @param   params  the command line parameters
     * @param   out     the object used to output the generated code
     */
    public Controller(Parameters params, Outputter out) {
        this.params = params;
        this.out = out;
    }

    /**
     * Returns <CODE>false</CODE>.
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
        return false;
    }

    /**
     * This method won't receive any DOM sub-trees since <CODE>wantDOM()</CODE>
     * always returns <CODE>false</CODE>.
     *
     * @param   element         The root of the DOM sub-tree
     * @throws  SAXException    If an error must be signaled
     */
    public void handleDOM(Element element) throws SAXException {
    }

}
