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

/**
 * Logging mechanism that redirects any operation to two other loggers.
 *
 * @see     com.devsphere.logging.AbstractLogger
 */
public class CompoundLogger extends AbstractLogger {
    private AbstractLogger logger1;
    private AbstractLogger logger2;

    /**
     * Creates a compound logger.
     * Any logging operation will be redirected to the two given loggers.
     *
     * @param       logger1     the first logger.
     * @param       logger2     the second logger.
     * @exception   NullPointerException    if any of the logger parameters
     *                                      is <code>null</code>.
     */
    public CompoundLogger(AbstractLogger logger1, AbstractLogger logger2) {
        if (logger1 == null)
            throw new NullPointerException("logger1");
        this.logger1 = logger1;
        if (logger2 == null)
            throw new NullPointerException("logger2");
        this.logger2 = logger2;
    }

    /**
     * Logs a message.
     *
     * @param   message     the message that has to be logged.
     */
    public synchronized void log(String message) {
        logger1.log(message);
        logger2.log(message);
    }

    /**
     * Logs an exception.
     *
     * @param   throwable   the exception that has to be logged.
     */
    public synchronized void log(Throwable throwable) {
        logger1.log(throwable);
        logger2.log(throwable);
    }

    /**
     * Logs a message and an exception.
     *
     * @param   message     the message that has to be logged.
     * @param   throwable   the exception that has to be logged.
     */
    public synchronized void log(String message, Throwable throwable) {
        logger1.log(message, throwable);
        logger2.log(message, throwable);
    }

    /**
     * Flushes the two loggers.
     */
    public synchronized void flush() {
        logger1.flush();
        logger2.flush();
    }

    /**
     * Closes the two loggers.
     */
    public synchronized void close() {
        logger1.close();
        logger2.close();
    }

}
