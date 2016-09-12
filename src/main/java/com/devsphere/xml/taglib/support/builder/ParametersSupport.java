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

package com.devsphere.xml.taglib.support.builder;

import java.util.ArrayList;

/**
 * Support class for processing the command line parameters.
 *
 * <P>There are three types of parameters
 * <UL>
 * <LI>Single-value: <CODE>-name value</CODE>
 * <LI>Multi-value: <CODE>-name value1 value2 value3 ...</CODE>
 * <LI>Flag: <CODE>-name</CODE>
 */
public class ParametersSupport implements java.io.Serializable {
    private String args[];

    /**
     * Constructor that saves the parameters to the <CODE>args</CODE> field.
     *
     * @param   args    the command line parameters
     */
    public ParametersSupport(String args[]) {
        this.args = args;
    }

    /**
     * Returns the first value of a parameter.
     * Use this method for single-value parameters.
     *
     * @param   name    the name of the parameter
     * @return          the value of the parameter
     *                  or <CODE>null</CODE> if the parameter is missing.
     */
    public String getParameter(String name) {
        name = "-" + name;
        for (int i = 0; i < args.length; i++)
            if (args[i].equals(name) && i+1 < args.length)
                return args[i+1];
        return null;
    }

    /**
     * Returns the values of a parameter.
     * Use this method for multi-value parameters.
     *
     * @param   name    the name of the parameter
     * @return          the values of the parameter
     *                  or an empty array if the parameter is missing.
     */
    public String[] getParameterValues(String name) {
        name = "-" + name;
        for (int i = 0; i < args.length; i++)
            if (args[i].equals(name)) {
                ArrayList list = new ArrayList();
                int j = i + 1;
                while (j < args.length && !args[j].startsWith("-"))
                    list.add(args[j++]);
                return (String[]) list.toArray(new String[list.size()]);
            }
        return new String[0];
    }

    /**
     * Returns <CODE>true</CODE> if a parameter is present.
     * Use this method for flags.
     *
     * @param   name    the name of the parameter
     * @return          a boolean value that indicates whether the parameter
     *                  is present or is missing.
     */
    public boolean getFlag(String name) {
        name = "-" + name;
        for (int i = 0; i < args.length; i++)
            if (args[i].equals(name))
                return true;
        return false;
    }

}
