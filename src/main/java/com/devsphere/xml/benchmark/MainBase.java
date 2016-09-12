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

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.text.NumberFormat;

import java.util.Vector;

/**
 * This is the base class for <CODE>Main1</CODE> and <CODE>Main2</CODE>
 * and provides most of the functionality of the XML parsing benchmark.
 *
 * @see com.devsphere.xml.benchmark.Main1
 * @see com.devsphere.xml.benchmark.Main2
 */
public abstract class MainBase {
    /** A reusable <CODE>java.lang.Runtime</CODE> instance */
    protected static final Runtime RUNTIME = Runtime.getRuntime();

    /** The valid commands are: "create", "sax", "saxdomix" and "dom" */
    protected String command;

    /** The number of records of the XML database */
    protected int recordCount;

    /** The number of concurrent threads that parse the database */
    protected int threadCount;

    /** Flag for enabling the namespace support */
    protected boolean namespaces;

    /** Flag for enabling the validation support */
    protected boolean validation;

    /** A container for the garbage that must not be collected */
    protected Vector garbage;

    /** Flag indicating the the parsing ran out of memory */
    private boolean outOfMemory;

    /** The used memory before starting the parsing */
    private long startMemory;

    /** The time when the parsing was started */
    private long startTime;

    /** Synchronization lock for starting the parsing */
    private Object startLock;

    /** Synchronization lock for finishing the parsing */
    private Object doneLock;

    /** The number of alive parsing threads */
    private int aliveCount;

    /** JAXP factory for SAX parsers */
    private SAXParserFactory saxFactory;

    /** JAXP factory for DOM parsers */
    private DocumentBuilderFactory docFactory;

    /**
     * Does the initialization
     */
    protected MainBase(String[] args) {
        if (args.length < 3) {
            // Invalid command line. Print the help
            System.out.println("java " + getClass().getName()
                + " <command> <recordCount> <threadCount> [-n] [-v]");
            System.out.println();
            System.out.println("command - the task to perform");
            System.out.println("recordCount - the number of records of the XML database");
            System.out.println("threadCount - the number of threads that parse the XML database");
            System.out.println("-n - enables the namespaces support");
            System.out.println("-v - enables the validation support");
            System.out.println();
            System.out.println("Valid Commands: ");
            System.out.println("    create - generates an XML database");
            System.out.println("    sax - parses an XML database with SAX");
            System.out.println("    saxdomix - parses an XML database with SAXDOMIX");
            System.out.println("    saxdomixt - parses an XML database with SAXDOMIX based on TrAX");
            System.out.println("    dom - parses an XML database with DOM");
            System.exit(1);
        }

        // Get the arguments
        command = args[0];
        recordCount = Integer.parseInt(args[1]);
        threadCount = Integer.parseInt(args[2]);
        namespaces = false;
        validation = false;
        for (int i = 3; i < args.length; i++)
            if (args[i].equals("-n"))
                namespaces = true;
            else if (args[i].equals("-v"))
                validation = true;

        // This is not an error. We use System.err not to mix this message
        // with the output that goes to System.out, which is normally
        // redirected to a file
        System.err.println(command + " " + recordCount + " " + threadCount);

        // Initialize other fields
        garbage = new Vector();
        outOfMemory = false;

        if (command.equals("sax") || command.startsWith("saxdomix")) {
            // Create and configure the JAXP factory for SAX parsers
            saxFactory = SAXParserFactory.newInstance();
            saxFactory.setValidating(validation);
            saxFactory.setNamespaceAware(namespaces);
        }

        if (command.equals("dom")) {
            // Create and configure the JAXP factory for DOM parsers
            docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setValidating(validation);
            docFactory.setNamespaceAware(namespaces);
        }
    }

    /**
     * Forwards the main task to the appropriate method
     */
    protected void main() throws Exception {
        // Construct the name of the database
        String name = "db" + recordCount;
        if (namespaces)
            name += 'n';
        if (validation)
            name += 'v';
        name += ".xml";
        File dbFile = new File(name);

        if (!command.equals("create"))
            // Cache the file into the operating system's memory
            cacheFile(dbFile);

        // Invoke the garbage collector
        System.gc();

        // Save the current size of the used memory
        startMemory = RUNTIME.totalMemory() - RUNTIME.freeMemory();

        // Save the current time
        startTime = System.currentTimeMillis();

        // Forward the main task to the appropriate method
        if (command.equals("create"))
            createDatabase(dbFile);
        else if (threadCount == 0)
            parseDatabase(dbFile);
        else
            parseDatabaseMultithreading(dbFile);

        if (threadCount == 0)
            // Finalize the benchmark
            done();
    }

    /**
     * Prints the time spent to perform the main task and the size of the used
     * memory.
     */
    protected void done() {
        // Get the current time
        long endTime = System.currentTimeMillis();

        // Get the current size of the used memory
        long endMemory = RUNTIME.totalMemory() - RUNTIME.freeMemory();

        // Invoke the garbage collector
        System.gc();

        // Get the size of the used memory ofter GC
        long endMemoryAfterGC = RUNTIME.totalMemory() - RUNTIME.freeMemory();

        // Print the record or thread count
        if (threadCount == 0)
            System.out.print(recordCount);
        else
            System.out.print(threadCount);
        System.out.print('\t');

        // Print the time spent to perform the main task
        NumberFormat timeNF = NumberFormat.getInstance();
        timeNF.setMinimumFractionDigits(3);
        timeNF.setMaximumFractionDigits(3);
        System.out.print(timeNF.format(
            (endTime - startTime) / 1000.0));
        System.out.print('\t');

        // Print the size of the used memory
        if (outOfMemory)
            System.out.print("out of memory");
        else {
            NumberFormat memoryNF = NumberFormat.getInstance();
            memoryNF.setMinimumFractionDigits(2);
            memoryNF.setMaximumFractionDigits(2);
            System.out.print(memoryNF.format(
                (endMemory - startMemory) / 1000000.0));
            System.out.print('\t');
            System.out.print(memoryNF.format(
                (endMemoryAfterGC - startMemory) / 1000000.0));
        }
        System.out.println();
    }

    /**
     * Creates an XML database.
     */
    protected void createDatabase(File dbFile) throws IOException {
        NumberFormat recordNF = NumberFormat.getInstance();
        recordNF.setGroupingUsed(false);
        recordNF.setMinimumIntegerDigits(6);
        BufferedWriter out = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(dbFile), "ASCII"));
        try {
            out.write("<?xml version='1.0' encoding='US-ASCII'?>\r\n");
            String prefix = namespaces ? "benchmark:" : "";
            if (validation) {
                out.write("<!DOCTYPE " + prefix + "database [\r\n");
                if (namespaces)
                out.write("<!ATTLIST " + prefix + "database xmlns:benchmark "
                    + "CDATA #FIXED 'http://devsphere.com/xml/benchmark'>\r\n");
                out.write("<!ELEMENT " + prefix + "database (" + prefix + "person*)>\r\n");
                out.write("<!ELEMENT " + prefix + "person (" + prefix + "name, "
                    + prefix + "email, " + prefix + "phone, " + prefix + "address)>\r\n");
                out.write("<!ATTLIST " + prefix + "person id CDATA #REQUIRED>\r\n");
                out.write("<!ELEMENT " + prefix + "name (#PCDATA)>\r\n");
                out.write("<!ELEMENT " + prefix + "email (#PCDATA)>\r\n");
                out.write("<!ELEMENT " + prefix + "phone (#PCDATA)>\r\n");
                out.write("<!ELEMENT " + prefix + "address (" + prefix + "line1, " + prefix + "line2)>\r\n");
                out.write("<!ATTLIST " + prefix + "address city CDATA #REQUIRED>\r\n");
                out.write("<!ATTLIST " + prefix + "address state CDATA #REQUIRED>\r\n");
                out.write("<!ATTLIST " + prefix + "address zip CDATA #REQUIRED>\r\n");
                out.write("<!ATTLIST " + prefix + "address country CDATA #REQUIRED>\r\n");
                out.write("<!ELEMENT " + prefix + "line1 (#PCDATA)>\r\n");
                out.write("<!ELEMENT " + prefix + "line2 (#PCDATA)>\r\n");
                out.write("]>\r\n");
            }
            String xmlns = namespaces ? " xmlns:benchmark='http://devsphere.com/xml/benchmark'" : "";
            out.write("<" + prefix + "database" + xmlns + ">\r\n");
            for (int i = 0; i < recordCount; i++) {
                String id = recordNF.format(i);
                out.write("    <" + prefix + "person id='" + id + "'>\r\n");
                out.write("        <" + prefix + "name>Name" + id + "</" + prefix + "name>\r\n");
                out.write("        <" + prefix + "email>" + id + "@company.com</" + prefix + "email>\r\n");
                out.write("        <" + prefix + "phone>12345 " + id + "</" + prefix + "phone>\r\n");
                out.write("        <" + prefix + "address city='City" + id + "'"
                    + " state='" + id.substring(id.length()-2) + "' zip='" + id + "'"
                    + " country='" + id.substring(id.length()-3) + "'>\r\n");
                out.write("            <" + prefix + "line1>L i n e 1 " + id + " " + id + "</" + prefix + "line1>\r\n");
                out.write("            <" + prefix + "line2>L i n e 2 " + id + " " + id + "</" + prefix + "line2>\r\n");
                out.write("        </" + prefix + "address>\r\n");
                out.write("    </" + prefix + "person>\r\n");
            }
            out.write("</" + prefix + "database>\r\n");
        } finally {
            out.close();
        }
    }

    /**
     * Parses an XML database with SAX
     */
    protected abstract void parseWithSAX(InputStream in)
            throws IOException, SAXException, ParserConfigurationException;

    /**
     * Parses an XML database with SAXDOMIX
     */
    protected abstract void parseWithSAXDOMIX(InputStream in)
            throws IOException, SAXException, ParserConfigurationException;

    /**
     * Parses an XML database with DOM
     */
    protected abstract void parseWithDOM(InputStream in)
            throws IOException, SAXException, ParserConfigurationException;

    /**
     * Forwards the parsing task to the appropriate method
     */
    protected void parseDatabase(InputStream in)
            throws IOException, SAXException, ParserConfigurationException {
        try {
            if (command.equals("sax"))
                parseWithSAX(in);
            else if (command.startsWith("saxdomix"))
                parseWithSAXDOMIX(in);
            else if (command.equals("dom"))
                parseWithDOM(in);
            else
                throw new IllegalArgumentException(
                    "Invalid command: " + command);
        } catch (OutOfMemoryError e) {
            outOfMemory = true;
        }
    }

    /**
     * Forwards the parsing task to the appropriate method
     */
    protected void parseDatabase(File dbFile)
            throws IOException, SAXException, ParserConfigurationException {
        InputStream in = new FileInputStream(dbFile);
        try {
            parseDatabase(in);
        } finally {
            in.close();
        }
    }

    /**
     * Creates multiple threads that parse the same database. All threads
     * are put in a wait state after creation. The last thread will
     * notify all to start the parsing at the same time.
     */
    protected void parseDatabaseMultithreading(final File dbFile)
            throws IOException, SAXException, ParserConfigurationException {
        startLock = new Object();
        doneLock = new Object();
        aliveCount = 0;
        for (int i = 0; i <= threadCount; i++)
            new Thread() {
                public void run() {
                    synchronized (startLock) {
                        try {
                            if (aliveCount == threadCount) {
                                startTime = System.currentTimeMillis();
                                startLock.notifyAll();
                                return;
                            }
                            aliveCount++;
                            startLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        parseDatabase(dbFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        synchronized (doneLock) {
                            aliveCount--;
                            if (aliveCount == 0)
                                done();
                        }
                    }
                }
            }.start();
    }

    /**
     * Returns a new SAX parser.
     */
    protected SAXParser getSAXParser()
            throws SAXException, ParserConfigurationException {
        return saxFactory.newSAXParser();
    }

    /**
     * Returns a new DOM parser.
     */
    protected DocumentBuilder getDocumentBuilder()
            throws ParserConfigurationException {
        return docFactory.newDocumentBuilder();
    }

    /**
     * Caches a file into the operating system's memory by simply reading
     * its content.
     */
    protected void cacheFile(File file) throws IOException {
        int size = (int) file.length();
        byte buf[] = new byte[4096];
        InputStream in = new FileInputStream(file);
        try {
            int offset = 0;
            while (offset < size)
                offset += in.read(buf);
        } finally {
            in.close();
        }
    }

}
