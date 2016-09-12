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

package com.devsphere.examples.xml.saxdomix;

import com.devsphere.xml.saxdomix.SDXTransformer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

/**
 * This example uses <CODE>SDXTransformer</CODE> to apply an XSLT transformation
 * to each DOM sub-tree created during the mixed SAX-DOM parsing of an XML
 * document.
 *
 * @see com.devsphere.xml.saxdomix.SDXTransformer
 * @see com.devsphere.examples.xml.saxdomix.TransformerRunner
 */
public class MixedTransformer extends SDXTransformer {
    /**
     * Does the initialization
     */
    public MixedTransformer(String xslSystemID) throws TransformerException {
        super(new StreamSource(xslSystemID));
    }

    /**
     * Returns <CODE>true</CODE> when the application wants to receive
     * a DOM sub-tree for transforming, that is the current element
     * has a <CODE>wantDOM="true"</CODE> attribute.
     */
    public boolean wantDOM(String namespaceURI, String localName,
            String qualifiedName, Attributes attributes) throws SAXException {
        String wantDOM = attributes.getValue("wantDOM");
        return wantDOM != null && wantDOM.equals("true");
    }

    /**
     * Uses <CODE>TransformerRunner</CODE> in order to create an instance of
     * this class, configure it and call its <CODE>transform()</CODE> method.
     */
    public static void main(String args[]) {
        TransformerRunner.run(MixedTransformer.class.getName(), args);
    }

}
