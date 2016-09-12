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

package com.devsphere.xml.saxdomix;

import com.devsphere.xml.saxdomix.helpers.ResultFilter;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import org.w3c.dom.Element;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;

import java.util.Properties;

/**
 * This class applies an XSLT transformation to each DOM sub-tree
 * created during the mixed SAX-DOM parsing of an XML document.
 *
 * <P><IMG SRC="MixedTransformation.gif" WIDTH="653" HEIGHT="653">
 *
 * <P><CODE>SDXTransformer</CODE> extends <CODE>Transformer</CODE>, which is
 * a TrAX abstract class introduced by JAXP 1.1. <CODE>SDXTransformer</CODE>
 * also implements the <CODE>handleDOM()</CODE> method of the
 * <CODE>SDXController</CODE> interface. The application must define
 * subclasses of <CODE>SDXTransformer</CODE> and implement the
 * <CODE>wantDOM()</CODE> method of <CODE>SDXController</CODE>.
 *
 * <P>All configuration methods defined by <CODE>Transformer</CODE>,
 * such as <CODE>setOutputProperty()</CODE> or <CODE>setParameter()</CODE>
 * delegate their task to another <CODE>Transformer</CODE> object called
 * <CODE>fragmentTransformer</CODE>. This object can be provided by the
 * application or can be constructed internally from any XSLT suitable
 * to transform the DOM sub-trees.
 *
 * <P>The <CODE>transform()</CODE> method creates three objects named
 * <CODE>builder</CODE>, <CODE>serializer</CODE> and <CODE>resultFilter</CODE>.
 * These objects are created internally and you don't have to use them
 * in a typical case. You may, however, override the methods that create them
 * in order to customize the transformation process.
 *
 * <P>After creating the three objects, <CODE>transform()</CODE> parses the
 * given <CODE>xmlSource</CODE> and feeds the <CODE>builder</CODE>
 * with the SAX input events.
 *
 * <P>The <CODE>serializer</CODE> will receive the SAX output events that
 * represent the result produced by <CODE>SDXTransformer</CODE>. The default
 * <CODE>serializer</CODE> is an identity <CODE>TransformerHandler</CODE>
 * that simply saves the SAX events to the given <CODE>outputTarget</CODE>,
 * which is an instance of the TrAX <CODE>Result</CODE>.
 *
 * <P>The role of the <CODE>resultFilter</CODE> object is to filter some of the
 * SAX events that are generated during the transforming of the DOM sub-trees.
 * For example, each transformation produces a <CODE>startDocument()</CODE>
 * and an <CODE>endDocument()</CODE>. The multiple such pairs should be
 * omitted from the result of the transformation. The SAX events that pass
 * through the filter will reach the <CODE>serializer</CODE> object.
 *
 * <P>The default <CODE>builder</CODE> is a <CODE>SDXBuilderT</CODE> that
 * receives the SAX events of the XML input source, uses the
 * <CODE>serializer</CODE> as handler and this <CODE>SDXTransformer</CODE>
 * as controller. The next paragraph explains the consequences.
 *
 * <P>The <CODE>builder</CODE> is initially in SAX parsing mode, which means
 * that SAX events are forwarded to the <CODE>serializer</CODE> object.
 * During the SAX parsing, the <CODE>wantDOM()</CODE> method of
 * this object is called for each element start. When <CODE>wantDOM()</CODE>
 * returns <CODE>true</CODE>, the <CODE>builder</CODE> constructs a DOM
 * sub-tree, which is then passed for handling to the <CODE>handleDOM()</CODE>
 * method of this object. During the DOM building, the <CODE>wantDOM()</CODE>
 * method is NOT called. The <CODE>builder</CODE> switches to the SAX parsing
 * mode automatically when the DOM sub-tree is completed. This process is
 * repeated until the end of the parsed document. All SAX events that aren't
 * used to build the sub-trees are passed to the <CODE>serializer</CODE>.
 *
 * <P>The <CODE>handleDOM()</CODE> method wraps each DOM sub-tree in a TrAX
 * <CODE>DOMSource</CODE>, wraps the <CODE>resultFilter</CODE> in a TrAX
 * <CODE>SAXResult</CODE> and passes these two TrAX objects to the
 * <CODE>transform()</CODE> method of the <CODE>fragmentTransformer</CODE>
 * object. This means that the XSLT transformation is applied to the DOM
 * sub-trees and the resulted SAX events are filtered before reaching
 * the <CODE>serializer</CODE>.
 *
 * <P>In conclusion, the <CODE>SDXTransformer</CODE> uses the framework's
 * <CODE>SDXBuilderT</CODE> to do mixed SAX-DOM parsing. By default,
 * all SAX events that aren't used to build DOM sub-trees will be part
 * of the final result. The DOM sub-trees are transformed using
 * a <CODE>fragmentTransformer</CODE> and the produced SAX events
 * will also be inserted into the final result.
 *
 * <P><B>Typical usage:</B>
 * <PRE>
 *    import com.devsphere.xml.saxdomix.SDXTransformer;
 *    import org.xml.sax.Attributes;
 *    import org.xml.sax.SAXException;
 *    import javax.xml.transform.Transformer;
 *    import javax.xml.transform.TransformerException;
 *    import javax.xml.transform.stream.StreamSource;
 *    import javax.xml.transform.stream.StreamResult;
 *
 *    public class MyTransformer extends SDXTransformer {
 *
 *        public MyTransformer(String xslSystemID)
 *                throws TransformerException {
 *            super(new StreamSource(xslSystemID));
 *        }
 *
 *        public boolean wantDOM(String namespaceURI, String localName,
 *                String qualifiedName, Attributes attributes)
 *                throws SAXException {
 *            return ... // application specific
 *        }
 *
 *        public static void main(String args[]) {
 *            Transformer transformer = new MyTransformer(args[0]);
 *            transformer.setOutputProperties(...);
 *            StreamSource source = new StreamSource(args[1]);
 *            StreamResult result = new StreamResult(args[2]);
 *            transformer.transform(source, result);
 *        }
 *    }</PRE>
 *
 * <P><B>Warning:</B>
 * The developer must take into account that the XSLT stylesheet is used
 * to transform DOM sub-trees / fragments. In some cases, applying the same
 * stylesheet to the whole XML source document will produce a different result.
 *
 * @see com.devsphere.xml.saxdomix.SDXBuilderT
 * @see com.devsphere.xml.saxdomix.SDXController
 */
public abstract class SDXTransformer extends Transformer
        implements SDXController {
    /** Factory for <CODE>Transformer</CODE> and
        <CODE>TransformerHandler</CODE> objects */
    protected static SAXTransformerFactory traxFactory;

    static {
        try {
            // Create the TrAX factory
            traxFactory
                = (SAXTransformerFactory) TransformerFactory.newInstance();
        } catch (TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        }
    }

    /** Object used to transform the DOM sub-trees */
    protected Transformer fragmentTransformer;

    /** Builder used to do the mixed SAX-DOM parsing */
    protected ContentHandler builder;

    /** Serializer that receives the resulted SAX output events */
    protected TransformerHandler serializer;

    /** Filter for the SAX events generated during the transforming
        of the DOM sub-trees */
    protected ContentHandler resultFilter;

    /**
     * Creates a transformer that will use the given
     * <CODE>fragmentTransformer</CODE> for processing the DOM sub-trees.
     *
     * @param   fragmentTransformer     The transformer for the DOM sub-trees
     * @throws  NullPointerException    If the parameter is <CODE>null</CODE>
     */
    public SDXTransformer(Transformer fragmentTransformer) {
        if (fragmentTransformer == null)
            throw new NullPointerException(
                "fragmentTransformer cannot be null");
        this.fragmentTransformer = fragmentTransformer;
    }

    /**
     * Creates a transformer that will use the given <CODE>xslSource</CODE>
     * to construct the <CODE>fragmentTransformer</CODE>.
     *
     * @param   xslSource               The stylesheet for the DOM sub-trees
     * @throws  NullPointerException    If the parameter is <CODE>null</CODE>
     */
    public SDXTransformer(Source xslSource) throws TransformerException {
        if (xslSource == null)
            throw new NullPointerException("xslSource cannot be null");
        this.fragmentTransformer = traxFactory.newTransformer(xslSource);
    }

    /**
     * The application must implement this method and returns
     * <CODE>true</CODE> when a DOM sub-tree should be transformed
     * by <CODE>fragmentTransformer</CODE>.
     *
     * <P>During the SAX parsing, the <CODE>builder</CODE> object invokes
     * the <CODE>wantDOM()</CODE> method for each <CODE>startElement()</CODE>
     * parsing event. If <CODE>wantDOM()</CODE> returns <CODE>false</CODE>
     * the SAX parsing mode continues. When <CODE>wantDOM()</CODE> returns
     * <CODE>true</CODE> the <CODE>builder</CODE> enters in DOM parsing mode
     * and starts creating a DOM sub-tree from all SAX events between the
     * current <CODE>startElement()</CODE> and the corresponding
     * <CODE>endElement()</CODE>.
     *
     * @param   namespaceURI    The element's namespace URI
     * @param   localName       The element's local name
     * @param   qualifiedName   The element's qualified name
     * @param   attributes      The element's attributes
     * @return                  A boolean value indicating whether
     *                          the <CODE>builder</CODE> must enter
     *                          in DOM parsing mode or must remain
     *                          in SAX parsing mode
     * @throws  SAXException    If an error must be signaled
     */
    public abstract boolean wantDOM(String namespaceURI, String localName,
            String qualifiedName, Attributes attributes) throws SAXException;

    /**
     * Receives the DOM sub-trees for transforming.
     *
     * <P>After <CODE>wantDOM()</CODE> returns <CODE>true</CODE>,
     * the <CODE>builder</CODE> constructs a DOM sub-tree from SAX events
     * and passes it to the <CODE>handleDOM()</CODE> method.
     *
     * <P>The <CODE>handleDOM()</CODE> method wraps the given DOM sub-tree
     * in a TrAX <CODE>DOMSource</CODE>, wraps the <CODE>resultFilter</CODE>
     * in a TrAX <CODE>SAXResult</CODE> and passes these two TrAX objects to the
     * <CODE>transform()</CODE> method of the <CODE>fragmentTransformer</CODE>
     * object.
     *
     * <P>After the DOM sub-tree is transformed, the <CODE>builder</CODE>
     * returns to the SAX parsing mode.
     *
     * @param   element         The root of the DOM sub-tree
     * @throws  SAXException    If an error must be signaled
     */
    public void handleDOM(Element element) throws SAXException {
        try {
            // Create a DOMSource that wraps the DOM sub-tree
            DOMSource domSource = new DOMSource(element);

            // Create a SAXResult that wraps the resultFilter object
            SAXResult saxResult = new SAXResult(resultFilter);
            if (resultFilter instanceof LexicalHandler)
                saxResult.setLexicalHandler((LexicalHandler) resultFilter);

            // Transform the DOM sub-tree using the fragmnentTransformer.
            // The resulted SAX events that go through resultFilter
            // are passed to the serializer object
            fragmentTransformer.transform(domSource, saxResult);
        } catch (TransformerException e) {
            throw new SAXException(e);
        }
    }

    /**
     * Creates the <CODE>serializer</CODE> object. Its role is to receive
     * the SAX output events that represent the result produced by this
     * <CODE>SDXTransformer</CODE>.
     *
     * <P>The <CODE>serializer</CODE> created by the current implementation
     * is an identity <CODE>TransformerHandler</CODE> that simply saves
     * the SAX events to the given <CODE>outputTarget</CODE>.
     *
     * <P>Subclasses may override this method for replacing the default
     * <CODE>serializer</CODE>.
     *
     * @param   outputTarget    The <CODE>Result</CODE> instance passed by the
     *                          application to the <CODE>transform()</CODE>
     *                          method
     * @throws  TransformerException    If an error must be signaled
     * @return                  The created <CODE>serializer</CODE>
     */
    protected TransformerHandler createSerializer(Result outputTarget)
            throws TransformerException {
        // Create the serializer object using the TrAX factory
        TransformerHandler serializer = traxFactory.newTransformerHandler();

        // Set the serializer's output properties to be the same as those
        // set to this transformer, which are actually held by the
        // fragmentTransformer object. Without this call, some of the
        // output properties would have no effect because of the chaining
        // of the transformers. The serializer is the last link in this chain.
        serializer.getTransformer().setOutputProperties(getOutputProperties());

        // Set the serializer's output target.
        serializer.setResult(outputTarget);

        // Return the created serializer
        return serializer;
    }

    /**
     * Creates the <CODE>resultFilter</CODE> object. Its role is to filter some
     * of the SAX events that are generated during the transforming of the DOM
     * sub-trees. For example, each transformation produces
     * a <CODE>startDocument()</CODE> and an <CODE>endDocument()</CODE>.
     * The multiple such pairs should be omitted from the result of the
     * transformation. The SAX events that pass through the filter should reach
     * the <CODE>serializer</CODE> object.
     *
     * <P>The current implementation returns an instance of the
     * <CODE>com.devsphere.xml.saxdomix.helpers.ResultFilter</CODE> class.
     *
     * <P>Subclasses may override this method for replacing the default
     * <CODE>resultFilter</CODE>.
     *
     * @param   serializer      The current <CODE>serializer</CODE> object
     * @throws  TransformerException    If an error must be signaled
     * @return                  The created <CODE>resultFilter</CODE>
     */
    protected ContentHandler createResultFilter(
            TransformerHandler serializer) throws TransformerException {
        // Create and return the filter
        return new ResultFilter(serializer);
    }

    /**
     * Creates the <CODE>builder</CODE> object. Its role is to receive the SAX
     * input events of the <CODE>xmlSource</CODE> that is passed to the
     * <CODE>transform()</CODE> method. The <CODE>builder</CODE> is expected to
     * act like a <CODE>SDXBuilderT</CODE> and forward the DOM sub-trees to the
     * <CODE>handleDOM()</CODE> method of this <CODE>SDXTransformer</CODE>
     * when <CODE>wantDOM()</CODE> returns <CODE>true</CODE>.
     *
     * <P>The current implementation returns a <CODE>SDXBuilderT</CODE> that
     * uses the <CODE>serializer</CODE> object as handler and this
     * <CODE>SDXTransformer</CODE> as controller.
     *
     * <P>Subclasses may override this method for replacing the default
     * <CODE>builder</CODE>. Instead of using the <CODE>serializer</CODE>
     * object, the application may provide its own handler for the SAX events
     * that don't participate to the construction of the DOM sub-trees.
     * In this case, the application's handler should still forward the
     * <CODE>startDocument()</CODE> and <CODE>endDocument()</CODE> events
     * to the <CODE>serializer</CODE>. The handler could act as a SAX filter
     * retaining some of the SAX events and transforming the others before
     * forwarding them to the <CODE>serializer</CODE> object.
     *
     * <P>Note that <CODE>this</CODE> should always be the
     * <CODE>SDXController</CODE> of the <CODE>builder</CODE>.
     *
     * @param   serializer      The current <CODE>serializer</CODE> object
     * @throws  TransformerException    If an error must be signaled
     * @return                  The created <CODE>builder</CODE>
     */
    protected ContentHandler createBuilder(TransformerHandler serializer)
            throws TransformerException {
        // Create a SDXBuilderT that passes the SAX events to
        // the serializer and uses this object as SDXController
        // The handleDOM() method will get the DOM sub-trees created by
        // the builder. These are transformed using the fragmentTransformer
        // and the results are also sent to the serializer
        return new SDXBuilderT(serializer, this);
    }

    /**
     * Process the source tree to the output result.
     *
     * <P>The <CODE>transform()</CODE> method creates the three objects
     * named <CODE>builder</CODE>, <CODE>serializer</CODE> and
     * <CODE>resultFilter</CODE>. After this, <CODE>transform()</CODE> parses
     * the given <CODE>xmlSource</CODE> and feeds the <CODE>builder</CODE>
     * with the SAX input events.
     *
      * @param   xmlSource       The input source for the transformation
     * @param   outputTarget    The output target for the transformation
     * @throws  TransformerException    If an error must be signaled
     */
    public void transform(Source xmlSource, Result outputTarget)
            throws TransformerException {
        // Create a TransformerHandler that takes SAX parsing events
        // and serializes them to the outputTarget
        serializer = createSerializer(outputTarget);

        // Create a ContentHandler that passes some of the SAX events it gets
        // to the serializer object
        resultFilter = createResultFilter(serializer);

        // Create a builder that will receive the SAX parsing events of
        // the xmlSource. The builder is expected to forward the DOM sub-trees
        // to handleDOM()
        builder = createBuilder(serializer);

        // Create a Transformer that acts as a SAX parser. Such an object must
        // be used instead of the JAXP SAXParser because Transformer can take
        // as input any kind of TrAX Source while SAXParser can process only
        // a SAX InputSource (stream, file, URL, but not a DOM tree).
        Transformer parser = traxFactory.newTransformer();

        // Create a SAXResult that wraps the builder
        SAXResult saxResult = new SAXResult(builder);
        if (builder instanceof LexicalHandler)
            saxResult.setLexicalHandler((LexicalHandler) builder);

        // Parse the xmlSource to get the SAX content and lexical events.
        // The builder wrapped by the saxResult will handle the SAX events
        parser.transform(xmlSource, saxResult);
    }

    /**
     * Calls the method with the same name of the
     * <CODE>fragmentTransformer</CODE> object
     * for setting the value of a parameter.
     *
     * @param   name    The name of the parameter
     * @param   value   The value for the parameter
     */
    public void setParameter(String name, Object value) {
        fragmentTransformer.setParameter(name, value);
    }

    /**
     * Calls the method with the same name of the
     * <CODE>fragmentTransformer</CODE> object
     * for getting the value of a parameter.
     *
     * @param   name    The name of the parameter
     * @return          The value of the parameter
     */
    public Object getParameter(String name) {
        return fragmentTransformer.getParameter(name);
    }

    /**
     * Calls the method with the same name of the
     * <CODE>fragmentTransformer</CODE> object
     * for clearing the parameter.
     */
    public void clearParameters() {
        fragmentTransformer.clearParameters();
    }

    /**
     * Calls the method with the same name of the
     * <CODE>fragmentTransformer</CODE> object
     * for setting the URI resolver.
     *
     * @param   resolver    An object whose class implements
     *                      the TrAX <CODE>URIResolver</CODE> interface
     */
    public void setURIResolver(URIResolver resolver) {
        fragmentTransformer.setURIResolver(resolver);
    }

    /**
     * Calls the method with the same name of the
     * <CODE>fragmentTransformer</CODE> object
     * for getting the URI resolver.
     *
     * @return              An object whose class implements
     *                      the TrAX <CODE>URIResolver</CODE> interface
     */
    public URIResolver getURIResolver() {
        return fragmentTransformer.getURIResolver();
    }

    /**
     * Calls the method with the same name of the
     * <CODE>fragmentTransformer</CODE> object
     * for setting the output properties.
     *
     * @param   oformat     The new output properties
     * @throws  IllegalArgumentException    If the argument is invalid
     */
    public void setOutputProperties(Properties oformat)
            throws IllegalArgumentException {
        fragmentTransformer.setOutputProperties(oformat);
    }

    /**
     * Calls the method with the same name of the
     * <CODE>fragmentTransformer</CODE> object
     * for getting the output properties.
     *
     * @return              The current output properties
     */
    public Properties getOutputProperties() {
        return fragmentTransformer.getOutputProperties();
    }

    /**
     * Calls the method with the same name of the
     * <CODE>fragmentTransformer</CODE> object
     * for setting an output property.
     *
     * @param   name        The name of the property
     * @param   value       The value for the property
     * @throws  IllegalArgumentException    If the arguments are invalid
     */
    public void setOutputProperty(String name, String value)
            throws IllegalArgumentException {
        fragmentTransformer.setOutputProperty(name, value);
    }

    /**
     * Calls the method with the same name of the
     * <CODE>fragmentTransformer</CODE> object
     * for getting an output property.
     *
     * @param   name        The name of the property
     * @return              The value of the property
     * @throws  IllegalArgumentException    If the argument is invalid
     */
    public String getOutputProperty(String name)
            throws IllegalArgumentException {
        return fragmentTransformer.getOutputProperty(name);
    }

    /**
     * Calls the method with the same name of the
     * <CODE>fragmentTransformer</CODE> object
     * for setting the error listener.
     *
     * @param   listener    An object whose class implements
     *                      the TrAX <CODE>ErrorListener</CODE> interface
     */
    public void setErrorListener(ErrorListener listener)
            throws IllegalArgumentException {
        fragmentTransformer.setErrorListener(listener);
    }

    /**
     * Calls the method with the same name of the
     * <CODE>fragmentTransformer</CODE> object
     * for getting the error listener.
     *
     * @return              An object whose class implements
     *                      the TrAX <CODE>ErrorListener</CODE> interface
     */
    public ErrorListener getErrorListener() {
        return fragmentTransformer.getErrorListener();
    }

}
