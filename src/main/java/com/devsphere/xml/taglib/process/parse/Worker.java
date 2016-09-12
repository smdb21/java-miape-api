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

package com.devsphere.xml.taglib.process.parse;

import com.devsphere.logging.AbstractLogger;

import org.xml.sax.InputSource;

import javax.servlet.jsp.PageContext;

/**
 * This thread class does the parsing of the XML documents.
 */
public class Worker extends Thread {
    private InputSource source;
    private Handler handler;
    private boolean validating;
    private AbstractLogger logger;
    private PageContext context;
    private Exception exception;

    /**
     * Initializes the parsing thread.
     *
     * @param   source      the XML input source that must be parsed
     * @param   handler     SAX event handler that is also SAXDOMIX controller
     * @param   validating  a flag indicating if the XML content should be
     *                      validated by the XML parser
     * @param   logger      used for logging error messages and exceptions
     * @param   context     the JSP <CODE>PageContext</CODE> object
     */
    public Worker(InputSource source, Handler handler, boolean validating,
        AbstractLogger logger, PageContext context) {
        this.source = source;
        this.handler = handler;
        this.validating = validating;
        this.logger = logger;
        this.context = context;
    }

    /**
     * Creates the builder object and calls its <CODE>parse()</CODE> method.
     */
    public void run() {
        try {
            Builder builder = new Builder(handler, logger, context);
            builder.parse(source, validating);
        } catch (Ender e) {
            handler.endParse();
        } catch (Exception e) {
            exception = e;
            handler.endParse();
        }
    }

    /**
     * Returns the exception that interrupted the parsing or <CODE>null<CODE>
     * if the parsing was completed successfully.
     *
     * @return      a parsing error or <CODE>null<CODE>
     */
    public Exception getException() {
        return exception;
    }

}
