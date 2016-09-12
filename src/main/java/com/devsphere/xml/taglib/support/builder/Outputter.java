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

import java.io.PrintWriter;

/**
 * Class used for indented output.
 */
public class Outputter {
    public static final String INDENT_STRING = "    ";
    private PrintWriter writer;
    private int indentLevel;

    /**
     * Initializes this object setting the indent level to <CODE>0</CODE>.
     *
     * @param   writer  the <CODE>java.io.PrintWriter</CODE> used for output
     */
    public Outputter(PrintWriter writer) {
        this.writer = writer;
        this.indentLevel = 0;
    }

    /**
     * Sets the indent level.
     *
     * @param   indentLevel     the new indent level.
     */
    public void setLevel(int indentLevel) {
        this.indentLevel = indentLevel;
    }

    /**
     * Gets the indent level.
     *
     * @return  the indent level
     */
    public int getLevel() {
        return indentLevel;
    }

    /**
     * Increments the indent level.
     */
    public void incLevel() {
        indentLevel++;
    }

    /**
     * Decrements the indent level.
     */
    public void decLevel() {
        indentLevel--;
    }

    /**
     * Prints a blank line.
     */
    public void println() {
        writer.println();
    }

    /**
     * Outputs the given string
     *
     * @param   line    the string that must be printed
     */
    public void println(String line) {
        for (int i = 0; i < indentLevel; i++)
            writer.print(INDENT_STRING);
        writer.println(line);
    }

    /**
     * Closes the writer
     */
    public void close() {
        writer.close();
    }

}
