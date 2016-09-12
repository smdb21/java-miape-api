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

package com.devsphere.xml.benchmark;

import com.devsphere.xml.saxdomix.SDXBuilder;
import com.devsphere.xml.saxdomix.SDXBuilderT;
import com.devsphere.xml.saxdomix.SDXController;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.w3c.dom.Element;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import java.io.InputStream;
import java.io.IOException;

/**
 * This is the main class that tests the SAX 2.0 and DOM Level 2 parsing
 * and also their mix.
 *
 * <P><B>Command Line Syntax:</B>
 * <PRE>
 *    java  &lt;command&gt; &lt;recordCount&gt; &lt;threadCount&gt; [-n] [-v]</PRE>
 *
 * <P>Arguments:
 * <UL>
 *    <LI>command - the task to perform
 *    <LI>recordCount - the number of records of the XML database
 *    <LI>threadCount - the number of threads that parse the XML database
 *    <LI>-n - enables the namespaces support
 *    <LI>-v - enables the validation support</UL>

 * <P>Valid Commands:
 * <UL>
 *    <LI>create - generates an XML database
 *    <LI>sax - parses an XML database with SAX
 *    <LI>saxdomix - parses an XML database with SAXDOMIX
 *    <LI>saxdomixt - parses an XML database with SAXDOMIX based on TrAX
 *    <LI>dom - parses an XML database with DOM</UL>
 *
 * @see com.devsphere.xml.benchmark.MainBase
 * @see com.devsphere.xml.saxdomix.SDXBuilder
 * @see com.devsphere.xml.saxdomix.SDXBuilderT
 * @see com.devsphere.xml.saxdomix.SDXController
 */
public class Main2 extends MainBase {
    /**
     * The main method of the application
     */
    public static void main(String[] args) throws Exception {
        new Main2(args).main();
    }

    /**
     * Does the initialization
     */
    protected Main2(String[] args) {
        super(args);
    }

    /**
     * Parses an XML database with SAX 2.0
     */
    protected void parseWithSAX(InputStream in)
            throws IOException, SAXException, ParserConfigurationException {
        // Create a new SAX parser
        SAXParser saxParser = getSAXParser();

        // Create a SAX handler that does nothing
        DefaultHandler handler = new DefaultHandler();

        // Parse the database
        saxParser.parse(in, handler);
    }

    /**
     * Parses an XML database with SAXDOMIX
     */
    protected void parseWithSAXDOMIX(InputStream in)
            throws IOException, SAXException, ParserConfigurationException {
        // Create a new SAX parser
        SAXParser saxParser = getSAXParser();

        // Create a SAX handler that does nothing
        DefaultHandler handler = new DefaultHandler();

        // Create the controller. Half of the records are parsed with SAX
        // and the other half is used to create DOM sub-trees
        SDXController controller = new SDXController() {
            boolean flag = false;

            public boolean wantDOM(String namespaceURI, String localName,
                    String qualifiedName, Attributes attributes)
                    throws SAXException {
                String name = namespaces ? localName : qualifiedName;
                if (name.equals("person")) {
                    flag = !flag;
                    return flag;
                } else
                    return false;
            }

            public void handleDOM(Element element) throws SAXException {
            }
        };

        // Create the builder
        DefaultHandler builder;
        if (command.endsWith("t"))
            builder = new SDXBuilderT(handler, controller);
        else
            builder = new SDXBuilder(handler, controller);
        saxParser.getXMLReader().setProperty(
            "http://xml.org/sax/properties/lexical-handler", builder);

        // Parse the database
        saxParser.parse(in, builder);
    }

    /**
     * Parses an XML database with DOM Level 2
     */
    protected void parseWithDOM(InputStream in)
            throws IOException, SAXException, ParserConfigurationException {
        // Create a new DOM parser
        DocumentBuilder docBuilder = getDocumentBuilder();

        // Set a default error handler
        docBuilder.setErrorHandler(new DefaultHandler());

        // Parse the database
        Document doc = docBuilder.parse(in);

        // Don't allow the garbage collection of the created DOM tree
        garbage.addElement(doc);
    }

}
