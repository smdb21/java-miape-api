/*
 * Copyright (c) 2000-2002 Devsphere.com. All rights reserved. The source code
 * and documentation are provided for reference purposes and to modify and
 * recompile the code if necessary. You may not redistribute the source code or
 * documentation without prior written permission of Devsphere. For written
 * permission, please contact info@devsphere.com Redistribution and use of the
 * xmltaglib.jar, saxdomix.jar, dslogging.jar, OutputXML.tld and ProcessXML.tld
 * files, with or without modification, are permitted provided that the
 * following conditions are met: 1. The redistribution must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution. 2. The
 * end-user documentation included with the redistribution, if any, must include
 * the following acknowledgment:
 * "This product includes software developed by Devsphere (www.devsphere.com)"
 * Alternately, this acknowledgment may appear in the software itself, if and
 * wherever such third-party acknowledgments normally appear. 3. The names
 * "SAXDOMIX", "XJTL" and "Devsphere" must not be used to endorse or promote
 * products derived from this software without prior written permission of
 * Devsphere. 4. Products derived from this software may not be called
 * "Devsphere", nor may "Devsphere" appear in their name, without prior written
 * permission of Devsphere. THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL DEVSPHERE OR ITS DEVELOPERS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.proteored.miapeapi.xml.mzml.lightParser.utils;

import java.util.Vector;

import org.w3c.dom.CDATASection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.devsphere.xml.saxdomix.SDXController;

/**
 * This class outputs DOM sub-trees and implements <CODE>SDXController</CODE>.
 * It is used by <CODE>MixedParsing</CODE>.
 * 
 * 
 */
public class DOMOutputter extends OutputterBase implements SDXController {
	/** The names of the elements that will be the roots of the DOM sub-trees */
	protected Vector elements;

	/**
	 * Creates a new outputter and takes as parameter a vector containing the
	 * element names for which <CODE>wantDOM()</CODE> will return
	 * <CODE>true</CODE>.
	 */
	public DOMOutputter(Vector elements) {
		super();
		this.elements = elements;
	}

	/**
	 * Returns <CODE>true</CODE> when the application wants to receive a DOM
	 * sub-tree for handling, that is the <CODE>elements</CODE> vector contains
	 * the qualified name of the current element
	 */
	public boolean wantDOM(String namespaceURI, String localName, String qualifiedName, Attributes attributes)
			throws SAXException {
		return elements.contains(qualifiedName);
	}

	/**
	 * Outputs the constructed DOM sub-tree
	 */
	public void handleDOM(Element element) throws SAXException {
		blankLine();
		output("DOM Tree");
		incLevel();
		output(element);
		decLevel();
	}

	/**
	 * Outputs a DOM node and is called recursively for all children
	 */
	public void output(Node node) {
		if (node instanceof Element) {
			String tagName = ((Element) node).getTagName();
			output("Element " + tagName);
			incLevel();
			Node child = node.getFirstChild();
			while (child != null) {
				output(child);
				child = child.getNextSibling();
			}
			decLevel();
		} else if (node instanceof CharacterData) {
			String data = ((CharacterData) node).getData();
			if (node instanceof CDATASection)
				output("CDATASection", data);
			else if (node instanceof Text)
				output("Text", data);
			else if (node instanceof Comment)
				output("Comment", data);
		} else if (node instanceof ProcessingInstruction) {
			String target = ((ProcessingInstruction) node).getTarget();
			output("ProcessingInstruction " + target);
		}
	}

}
