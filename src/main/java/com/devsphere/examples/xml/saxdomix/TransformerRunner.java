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

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.Properties;

/**
 * This class is a generic launcher for <CODE>Transformer</CODE>s.
 *
 * <P>The class that uses <CODE>TransformerRunner</CODE> must be a subclasses
 * of <CODE>Transformer</CODE>, it must have a constructor that accepts only
 * <CODE>String</CODE> parameters and it must call <CODE>run()</CODE>
 * passing as parameters the full class name, the command line arguments
 * and the names of the command line arguments that correspond to the
 * constructor's parameters.
 *
 * <P><B>Usage Example:</B>
 * <PRE>
 *    import com.devsphere.xml.saxdomix.SDXTransformer;
 *
 *    import org.xml.sax.Attributes;
 *    import org.xml.sax.SAXException;
 *
 *    import javax.xml.transform.TransformerException;
 *    import javax.xml.transform.stream.StreamSource;
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
 *            return ...
 *        }
 *
 *        public static void main(String args[]) {
 *            TransformerRunner.run(MyTransformer.class.getName(),
 *                args, new String[] { "xsl" });
 *        }
 *
 *    }</PRE>
 *
 * <P><B>Command Line Syntax:</B>
 * <PRE>
 *    java &lt;className&gt; -xsl &lt;stylesheet&gt; -in &lt;input&gt; [-out &lt;output&gt;]
 *    [-indent &lt;amount&gt;] [-method &lt;method&gt;] [-encoding &lt;encoding&gt;]
 *    [-omitdecl] [other application-specific parameters]</PRE>
 *
 * <P>The <CODE>run()</CODE> method
 * <UL>
 *    <LI>loads the class with the given name
 *    <LI>calls its constructor to create an instance
 *    <LI>uses the command line arguments to configure the created instance
 *    <LI>calls the <CODE>transform()</CODE> method of the created instance</UL>
 *
 * @see com.devsphere.examples.xml.saxdomix.MixedTransformer
 * @see com.devsphere.examples.xml.saxdomix.SmartTransformer
 * @see com.devsphere.examples.xml.saxdomix.WrapperTransformer
 */
public class TransformerRunner {
    /** The prefix of the output properties that are Xalan specific */
    public static final String XALAN_PREFFIX = "{http://xml.apache.org/xslt}";

    /** The default number of spaces for indenting */
    public static final int DEFAULT_INDENT = 4;

    /**
     * Creates an instance of a <CODE>Transformer</CODE> subclass,
     * configures it and calls its <CODE>transform()</CODE> method.
     * It uses the constructor that takes the system ID of the XSLT stylesheet
     * as parameter.
     */
    public static void run(String className, String args[]) {
        run(className, args, new String[] { "xsl" });
    }

    /**
     * Creates an instance of a <CODE>Transformer</CODE> subclass,
     * configures it and calls its <CODE>transform()</CODE> method.
     * It uses the constructor with the given list of parameters.
     */
    public static void run(String className, String args[], String params[]) {
        // Extract the common arguments from the command line
        String xslArg = null, inArg = null, outArg = null;
        String indentArg = null, methodArg = null, encodingArg = null;
        boolean omitDeclFlag = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-xsl") && i+1<args.length)
                xslArg = args[++i];
            else if (args[i].equalsIgnoreCase("-in") && i+1<args.length)
                inArg = args[++i];
            else if (args[i].equalsIgnoreCase("-out") && i+1<args.length)
                outArg = args[++i];
            else if (args[i].equalsIgnoreCase("-indent") && i+1<args.length)
                indentArg = args[++i];
            else if (args[i].equalsIgnoreCase("-method") && i+1<args.length)
                methodArg = args[++i];
            else if (args[i].equalsIgnoreCase("-encoding") && i+1<args.length)
                encodingArg = args[++i];
            else if (args[i].equalsIgnoreCase("-omitdecl"))
                omitDeclFlag = true;
        }

        // Extract the values for the constructor's parameters
        Object paramValues[] = new String[params.length];
        for (int i = 0; i < args.length; i++) {
            if (i+1 < args.length)
                for (int j = 0; j < params.length; j++)
                    if (args[i].equalsIgnoreCase("-" + params[j]))
                        paramValues[j] = args[++i];
        }

        // See if any non-optional parameter is missing
        boolean missingParam = xslArg == null || inArg == null;
        if (!missingParam)
            for (int j = 0; j < paramValues.length; j++)
                if (paramValues[j] == null)
                    missingParam = true;

        if (missingParam) {
            // Invalid command line. Show the correct syntax
            String help = "java " + className
                + " -xsl <stylesheet> -in <input> [-out <output>]"
                + " [-indent <amount>] [-method <method>]"
                + " [-encoding <encoding>] [-omitdecl]";
            for (int j = 0; j < params.length; j++)
                if (help.indexOf("-" + params[j] + " ") == -1)
                    help += " -" + params[j] + " <" + params[j] + ">";
            System.err.println(help);
            return;
        }

        // Initialize the indent variable. 0 means no indenting
        int indent = DEFAULT_INDENT;
        if (indentArg != null) {
            try {
                indent = Integer.parseInt(indentArg);
            } catch (NumberFormatException e) {
                indent = -1;
            }
            if (indent < 0) {
                System.err.println("Invalid indent amount: " + indentArg);
                indent = DEFAULT_INDENT;
            }
        }

        // Perform the main tasks
        try {
            // Load the class
            Class clazz = Class.forName(className);

            // Get the constructor
            Class paramTypes[] = new Class[paramValues.length];
            for (int i = 0; i < paramTypes.length; i++)
                paramTypes[i] = String.class;
            Constructor constructor = clazz.getConstructor(paramTypes);

            // Instantiate the class
            Object instance = constructor.newInstance(paramValues);
            if (instance instanceof Transformer) {
                // Cast the created instance to Transformer
                Transformer transformer = (Transformer) instance;

                // Set the output properties
                Properties outputProps = transformer.getOutputProperties();
                if (outputProps == null)
                    outputProps = new Properties();
                if (indent != 0) {
                    String indentStr = Integer.toString(indent);
                    outputProps.put("indent", "yes");
                    outputProps.put("indent-amount", indentStr);
                    outputProps.put(XALAN_PREFFIX + "indent-amount", indentStr);
                }
                if (methodArg != null)
                    outputProps.put("method", methodArg);
                if (encodingArg != null)
                    outputProps.put("encoding", encodingArg);
                if (omitDeclFlag)
                    outputProps.put("omit-xml-declaration", "yes");
                transformer.setOutputProperties(outputProps);

                // Call the transform() method
                StreamSource source = new StreamSource(inArg);
                StreamResult result = outArg != null
                    ? new StreamResult(outArg)
                    : new StreamResult(System.out);
                transformer.transform(source, result);
            } else {
                System.err.println("Class doesn't extend Transformer: "
                    + className);
                return;
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + className);
            return;
        } catch (NoSuchMethodException e) {
            System.err.println("Constructor not found or not public: "
                + className + "(String)");
            return;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.err.println("Couldn't create the transformer instance: "
                + className);
            e.getTargetException().printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

}
