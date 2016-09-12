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

import com.devsphere.xml.saxdomix.SDXBuilderT;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

import javax.xml.transform.sax.TransformerHandler;

import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * This example is based on <CODE>SmartTransformer</CODE>, but it creates
 * an HTML file as output. The SAX events are ignored during the mixed parsing.
 * The markup that results from the transformation of the DOM sub-trees
 * is outputted between a header and a footer, which are taken from an HTML
 * wrapper.
 *
 * @see com.devsphere.xml.saxdomix.SDXTransformer
 * @see com.devsphere.examples.xml.saxdomix.SmartTransformer
 * @see com.devsphere.examples.xml.saxdomix.TransformerRunner
 */
public class WrapperTransformer extends SmartTransformer {
    /** The content of the HTML wrapper */
    protected byte wrapper[];

    /** The name of the wariable from the HTML wrapper */
    protected byte variable[];

    /** The offset of the HTML header */
    protected int headerOffset;

    /** The length of the HTML header */
    protected int headerLength;

    /** The offset of the HTML footer */
    protected int footerOffset;

    /** The length of the HTML footer */
    protected int footerLength;

    /** The stream used for output */
    protected OutputStream stream;

    /** The writer used for output */
    protected Writer writer;

    /**
     * Does the initialization.
     */
    public WrapperTransformer(String xslSystemID, String wrapperSystemID,
            String variableName) throws TransformerException,
            ParserConfigurationException, SAXException, IOException {
        super(xslSystemID);
        initHeaderAndFooter(wrapperSystemID, variableName);
    }

    /**
     * Does the transformation.
     *
     * <P>This method can work only with a <CODE>StreamResult</CODE>.
     * It saves its stream or writer within an instance variable for later
     * usage. Then it calls the method with the same name inherited from
     * <CODE>SDXTransformer</CODE>.
     */
    public void transform(Source xmlSource, Result outputTarget)
            throws TransformerException {
        if (!(outputTarget instanceof StreamResult))
            throw new IllegalArgumentException(
                "WrapperTransformer works only with StreamResult");
        StreamResult result = (StreamResult) outputTarget;
        stream = result.getOutputStream();
        writer = result.getWriter();
        if (stream != null && writer != null)
            throw new IllegalArgumentException(
                "The StreamResult instance can wrap "
                    + "either an output stream or a writer "
                    + "but not both");
        if (stream != null || writer != null)
            super.transform(xmlSource, outputTarget);
        else {
            String systemID = result.getSystemId();
            if (systemID == null)
                throw new IllegalArgumentException(
                    "The StreamResult instance must wrap a writer, "
                        + "an output stream or a system ID");
            if (systemID.startsWith("file:///"))
                systemID = systemID.substring("file:///".length());
            systemID.replace('/', File.separatorChar);
            try {
                stream = new FileOutputStream(systemID);
                try {
                    result = new StreamResult(systemID);
                    result.setOutputStream(stream);
                    super.transform(xmlSource, result);
                } finally {
                    stream.close();
                }
            } catch (IOException e) {
                throw new TransformerException(e);
            }
        }
    }

    /**
     * Creates the <CODE>SDXBuilderT</CODE> object. This customization of
     * <CODE>SDXTransformer</CODE> in necessary in order to save the header
     * and the footer of the HTML output.
     *
     * <P>Instead of using the <CODE>serializer</CODE> object, the application
     * provides its own handler for the SAX events that don't participate to
     * the construction of the DOM sub-trees. The <CODE>startDocument()</CODE>
     * and <CODE>endDocument()</CODE> events are still forwarded to the
     * <CODE>serializer</CODE> object.
     */
    protected ContentHandler createBuilder(final TransformerHandler serializer)
            throws TransformerException {
        // If "omit-xml-declaration" is "no", the xml declaration
        // is written after the header. This is not desired
        serializer.getTransformer().setOutputProperty(
            "omit-xml-declaration", "yes");

        // Create the handler for the SAX events
        ContentHandler handler = new DefaultHandler() {
            public void startDocument() throws SAXException {
                try {
                    // Write the header
                    if (stream != null)
                        stream.write(wrapper, headerOffset, headerLength);
                    else if (writer != null)
                        writer.write(
                            new String(wrapper, headerOffset, headerLength));

                    // Forward the event
                    serializer.startDocument();
                } catch (IOException e) {
                    throw new SAXException(e);
                }
            }

            public void endDocument() throws SAXException {
                try {
                    // Forward the event
                    serializer.endDocument();

                    // Write the footer
                    if (stream != null)
                        stream.write(wrapper, footerOffset, footerLength);
                    else if (writer != null)
                        writer.write(
                            new String(wrapper, footerOffset, footerLength));
                } catch (IOException e) {
                    throw new SAXException(e);
                }
            }
        };

        // Create and return the builder
        return new SDXBuilderT(handler, this);
    }

    /**
     * Initializes the fields that maintain the offsets and the lengths of
     * the HTML header and footer.
     */
    protected void initHeaderAndFooter(String wrapperSystemID,
            String variableName) throws IOException {
        // Get the name of the variable
        variable = variableName.getBytes();
        if (variable.length == 0)
            throw new IllegalArgumentException("The variable's name is empty");

        // Get the first char of the variable's name
        byte firstChar = variable[0];

        // Load the HTML wrapper
        wrapper = loadWrapper(wrapperSystemID);

        // Loop over the wrapper's content
        int i = 0;
        while (i < wrapper.length) {
            if (wrapper[i] == firstChar && i+variable.length <= wrapper.length) {
                // Could be the variable
                int j = 1;
                while (j < variable.length) {
                    // It isn't the variable
                    if (wrapper[i+j] != variable[j])
                        break;
                    j++;
                }
                if (j == variable.length)
                    // Found the variable
                    break;
            }
            i++;
        }

        // See if the variable was found
        if (i == wrapper.length)
            throw new IllegalArgumentException(
                "The wrapper doesn't contain the variable");

        // Initialize the fields
        headerOffset = 0;
        headerLength = i;
        footerOffset = i + variable.length;
        footerLength = wrapper.length - footerOffset;
    }

    /**
     * Loads the wrapper and returns its content as a byte array.
     */
    protected byte[] loadWrapper(String wrapperSystemID) throws IOException {
        if (wrapperSystemID.startsWith("file:///"))
            wrapperSystemID = wrapperSystemID.substring("file:///".length());
        wrapperSystemID.replace('/', File.separatorChar);
        File file = new File(wrapperSystemID);
        int size = (int) file.length();
        byte buf[] = new byte[size];
        InputStream in = new FileInputStream(file);
        try {
            int offset = 0;
            while (offset < size)
                offset += in.read(buf, offset, size-offset);
        } finally {
            in.close();
        }
        return buf;
    }

    /**
     * Uses <CODE>TransformerRunner</CODE> in order to create an instance of
     * this class, configure it and call its <CODE>transform()</CODE> method.
     */
    public static void main(String args[]) {
        TransformerRunner.run(WrapperTransformer.class.getName(), args,
            new String[] { "xsl", "wrapper", "variable" } );
    }

}
