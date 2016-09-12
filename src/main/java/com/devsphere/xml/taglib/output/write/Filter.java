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

package com.devsphere.xml.taglib.output.write;

import java.io.IOException;
import java.io.Writer;

/**
 * This class filters the output and removes the empty comments.
 */
public class Filter extends Writer {
    protected Writer out;
    protected boolean inComment;
    protected boolean emptyComment;

    /**
     * Initializes the filter.
     */
    public Filter() {
        inComment = false;
        emptyComment = false;
    }

    /**
     * Gets the current writer used for output.
     *
     * @return  the current writer used for output
     */
    public Writer getOut() {
        return out;
    }

    /**
     * Sets the current writer used for output.
     *
     * @param   out     the current writer used for output
     */
    public void setOut(Writer out) {
        this.out = out;
    }

    /**
     * Writes a character.
     *
     * @param   c               the character to be written
     * @throws  IOException     to signal any error that might occur
     */
    public void write(int c) throws IOException {
        if (inComment && emptyComment) {
            out.write("<!--");
            emptyComment = false;
        }
        out.write(c);
    }

    /**
     * Writes an array of characters.
     *
     * @param   cbuf            the characters to be written
     * @throws  IOException     to signal any error that might occur
     */
    public void write(char cbuf[]) throws IOException {
        if (inComment && emptyComment && cbuf.length > 0) {
            out.write("<!--");
            emptyComment = false;
        }
        out.write(cbuf);
    }

    /**
     * Writes an array of characters.
     *
     * @param   cbuf            the characters to be written
     * @param   off             the start position in the array
     * @param   len             the number of characters to get from the array
     * @throws  IOException     to signal any error that might occur
     */
    public void write(char cbuf[], int off, int len) throws IOException {
        if (inComment && emptyComment && len > 0) {
            out.write("<!--");
            emptyComment = false;
        }
        out.write(cbuf, off, len);
    }

    /**
     * Writes a string.
     *
     * @param   str             the string to be written
     * @throws  IOException     to signal any error that might occur
     */
    public void write(String str) throws IOException {
        if (!inComment) {
            if (str.equals("<!--")) {
                inComment = true;
                emptyComment = true;
                return;
            }
        } else {
            if (str.equals("-->")) {
                inComment = false;
                if (emptyComment)
                    return;
            }
            if (emptyComment && str.length() > 0) {
                out.write("<!--");
                emptyComment = false;
            }
        }
        out.write(str);
    }

    /**
     * Writes a string.
     *
     * @param   str             the string to be written
     * @param   off             the start position in the string
     * @param   len             the number of characters to get from the string
     * @throws  IOException     to signal any error that might occur
     */
    public void write(String str, int off, int len) throws IOException {
        if (inComment && emptyComment && len > 0) {
            out.write("<!--");
            emptyComment = false;
        }
        out.write(str, off, len);
    }

    /**
     * Flushes the current writer.
     *
     * @throws  IOException     to signal any error that might occur
     */
    public void flush() throws IOException {
        out.flush();
    }

    /**
     * Closes the current writer.
     *
     * @throws  IOException     to signal any error that might occur
     */
    public void close() throws IOException {
        out.close();
    }

}
