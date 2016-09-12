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

import com.devsphere.xml.taglib.support.builder.ParametersSupport;

/**
 * Parses the command line parameters of the code generator that produces JSPs
 * that output XML documents.
 */
public class Parameters extends ParametersSupport {
    private String sample;
    private String output;

    /**
     * Constructor that takes the command line parameters
     * and initializes the builder's parameters
     *
     * @param   args    the command line parameters
     */
    public Parameters(String args[]) {
        super(args);
        if (args.length == 0)
            exit("");
        sample = getParameter("sample");
        if (sample == null)
            exit("You must provide the path of a sample XML file");
        output = getParameter("output");
        if (output == null)
            exit("You must provide the path of the output JSP file");
    }

    /**
     * Stops the code generator and prints a message followed by
     * the command line syntax.
     *
     * @param   msg     message that must be printed
     */
    public void exit(String msg) {
        System.err.println(msg);
        System.err.println("java " + Main.class.getName()
            + "    -sample  XML_file_path    -output  JSP_file_path");
        System.exit(1);
    }

    /**
     * Gets the value of the <CODE>sample</CODE> parameter
     */
    public String getSample() {
        return this.sample;
    }

    /**
     * Gets the value of the <CODE>output</CODE> parameter
     */
    public String getOutput() {
        return this.output;
    }

}
