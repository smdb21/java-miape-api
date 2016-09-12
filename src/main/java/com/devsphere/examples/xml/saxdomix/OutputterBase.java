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

/**
 * Base class for <CODE>SAXOutputter</CODE> and <CODE>DOMOutputter</CODE>.
 *
 * @see com.devsphere.examples.xml.saxdomix.SAXOutputter
 * @see com.devsphere.examples.xml.saxdomix.DOMOutputter
 */
public abstract class OutputterBase {
    /** Default value for <CODE>maxDataLength</CODE> */
    public static final int DEFAULT_MAX_DATA_LENGTH = 40;

    /** Data strings longer than this value are truncated before printing */
    protected int maxDataLength;

    /** The current indenting level */
    protected int level;

    /**
     * Zero-arg constructor.
     */
    public OutputterBase() {
        this(0);
    }

    /**
     * Constructor that takes as parameter the initial level.
     */
    public OutputterBase(int level) {
        this(level, DEFAULT_MAX_DATA_LENGTH);
    }

    /**
     * Constructor that takes as parameter the initial level
     * and the value for the <CODE>maxDataLength</CODE> property.
     */
    public OutputterBase(int level, int maxDataLength) {
        this.level = level;
        this.maxDataLength = maxDataLength;
    }

    /**
     * Sets the indenting level.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Gets the indenting level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Increments the indenting level.
     */
    public void incLevel() {
        level++;
    }

    /**
     * Decrements the indenting level.
     */
    public void decLevel() {
        level--;
    }

    /**
     * Sets the value of the <CODE>maxDataLength</CODE> property.
     */
    public void setMaxDataLength(int maxDataLength) {
        this.maxDataLength = maxDataLength;
    }

    /**
     * Gets the value of the <CODE>maxDataLength</CODE> property.
     */
    public int getMaxDataLength() {
        return maxDataLength;
    }

    /**
     * Prints a blank line.
     */
    public void blankLine() {
        System.out.println();
    }

    /**
     * Outputs an information
     */
    public void output(String info) {
        output(info, null);
    }

    /**
     * Outputs an information followed by a data string
     */
    public void output(String info, String data) {
        for (int i = 0; i < level; i++)
            System.out.print("    ");
        System.out.print(info);
        if (data != null) {
            data = data.replace('\r', ' ');
            data = data.replace('\n', ' ');
            if (data.length() > maxDataLength)
                data = data.substring(0, maxDataLength) + "...";
            System.out.print(" ");
            System.out.print(data);
        }
        System.out.println();
    }

}
