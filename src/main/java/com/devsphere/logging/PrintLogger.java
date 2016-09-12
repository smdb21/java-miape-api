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

package com.devsphere.logging;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Logging mechanisms that uses a print stream or a writer.
 *
 * @see     com.devsphere.logging.AbstractLogger
 */
public class PrintLogger extends AbstractLogger {
    private PrintStream stream;
    private PrintWriter writer;

    /**
     * Creates the print logger.
     *
     * @param       stream      the output stream used for logging.
     * @exception   NullPointerException    if <code>stream</code>
     *                                      is <code>null</code>.
     */
    public PrintLogger(PrintStream stream) {
        if (stream == null)
            throw new NullPointerException("stream");
        this.stream = stream;
        writer = null;
    }

    /**
     * Creates the print logger.
     *
     * @param       writer      the writer used for logging.
     * @exception   NullPointerException    if <code>writer</code>
     *                                      is <code>null</code>.
     */
    public PrintLogger(PrintWriter writer) {
        if (writer == null)
            throw new NullPointerException("writer");
        this.writer = writer;
        stream = null;
    }

    /**
     * Logs a message.
     *
     * @param   message     the message that has to be logged.
     */
    public void log(String message) {
        log(message, null);
    }

    /**
     * Logs an exception.
     *
     * @param   throwable   the exception that has to be logged.
     */
    public void log(Throwable throwable) {
        log(null, throwable);
    }

    /**
     * Logs a message and an exception.
     *
     * @param   message     the message that has to be logged.
     * @param   throwable   the exception that has to be logged.
     */
    public synchronized void log(String message, Throwable throwable) {
        if (message != null) {
            if (stream != null)
                stream.println(message);
            else
                writer.println(message);
        }
        if (throwable != null) {
            if (stream != null)
                throwable.printStackTrace(stream);
            else
                throwable.printStackTrace(writer);
        }
    }

    /**
     * Flushes the output stream / writer that is used by this logger.
     */
    public synchronized void flush() {
        if (stream != null)
            stream.flush();
        else
            writer.flush();
    }

    /**
     * Closes the output stream / writer that is used by this logger.
     * The System.out and System.err streams are not closed by this method.
     */
    public synchronized void close() {
        if (stream != null) {
            if (stream != System.out && stream != System.err)
                stream.close();
        } else
            writer.close();
    }

}
