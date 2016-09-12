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

import com.devsphere.xml.taglib.support.builder.BuilderSupport;

import com.devsphere.logging.AbstractLogger;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * An instance of this class will get all SAX events during the parsing of an
 * input source. These events will be forwarded to a SAX handler (that is also
 * a SAXDOMIX controller) or the builder will create DOM sub-trees depending on
 * the value returned by the <CODE>wantDOM()</CODE> method of the handler
 * object. The DOM sub-trees will be passed to the <CODE>handleDOM()</CODE>
 * method of the handler object.
 */
public class Builder extends BuilderSupport {
    private Handler handler;
    private PageContext context;

    /**
     * Initializes the builder.
     *
     * @param   handler     SAX event handler that is also SAXDOMIX controller
     * @param   logger      used for logging error messages and exceptions
     * @param   context     the JSP <CODE>PageContext</CODE> object
     */
    public Builder(Handler handler, AbstractLogger logger, PageContext context) {
        super(handler, handler, logger);
        this.handler = handler;
        this.context = context;
    }

    /**
     * Parses an input source using SAXDOMIX.
     *
     * @param   source          the XML input source that must be parsed
     * @param   validating      a flag indicating if the XML content should be
     *                          validated by the XML parser
     * @throws  SAXException    to signal a SAX error
     * @throws  IOException     to signal an I/O error
     * @throws  ParserConfigurationException    to signal a configuration error
     */
    public void parse(InputSource source, boolean validating)
            throws ParserConfigurationException, SAXException, IOException {
        if (source.getByteStream() == null
                && source.getCharacterStream() == null
                && source.getSystemId() != null)
            source = resolveEntity(source.getPublicId(), source.getSystemId());
        super.parse(source, validating);
    }

    /**
     * Throws a fatal parse error.
     *
     * @param   e               the SAX fatal error
     * @throws  SAXException    the SAX fatal error
     */
    public void fatalError(SAXParseException e) throws SAXException {
        handler.endParse();
        super.fatalError(e);
    }

    /**
     * Resolves an entity in the context of the web application.
     *
     * @param   publicId    the public identifier of the entity
     * @param   systemId    the system identifier of the entity
     * @throws  SAXException    to signal an error
     */
    public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException {
        if (systemId == null || isAbsoluteUrl(systemId))
            return null;
        String path = systemId;
        if (!path.startsWith("/")) {
            // Get the path of the JSP page
            path = ((HttpServletRequest) context.getRequest()).getServletPath();
            // Remove the JSP file name
            path = path.substring(0, path.lastIndexOf("/") + 1);
            // Add the system ID
            path += systemId;
        }
        path = context.getServletContext().getRealPath(path);
        try {
            InputStream stream = new FileInputStream(path);
            InputSource source = new InputSource(stream);
            source.setSystemId(systemId);
            source.setPublicId(publicId);
            return source;
        } catch (FileNotFoundException e) {
            throw new SAXException(e);
        }
    }

    /**
     * Verifies if a given URL is absolute.
     *
     * @param   url     the URL that must be verified if it's absolute
     * @return          <CODE>true</CODE> if the given URL is absolute
     */
    public static boolean isAbsoluteUrl(String url) {
        if (url == null)
            return false;
        int colonIndex = url.indexOf(":");
        if (colonIndex == -1)
            return false;
        for (int i = 0; i < colonIndex; i++) {
            char ch = url.charAt(i);
            if (('a' <= ch && ch <= 'z')
                    || ('A' <= ch && ch <= 'Z')
                    || ('0' <= ch && ch <= '9')
                    || ch == '+' || ch == '-' || ch == '.')
                continue;
            else
                return false;
        }
        return true;
    }

}
