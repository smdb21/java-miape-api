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

package com.devsphere.xml.saxdomix.helpers;

import org.w3c.dom.Element;

/**
 * This is a stack implementation for DOM <CODE>Element</CODE> nodes.
 * It is used internally by the builder classes of the framework.
 */
public class ElementStack {
    /** The top element of this stack */
    private Element top;

    /** The rest of this stack, which is a stack too */
    private ElementStack rest;

    /**
     * Creates a new empty stack.
     */
    public ElementStack() {
        this(null, null);
    }

    /**
     * Creates a new stack with the given top element and rest stack.
     *
     * @param   elem    The top element for the new stack
     * @param   stack   The rest for the new stack
     */
    private ElementStack(Element elem, ElementStack stack) {
        top = elem;
        rest = stack;
    }

    /**
     * Pushes an element onto the top of this stack.
     *
     * @param   elem    The new top element for this stack
     */
    public void push(Element elem) {
        rest = new ElementStack(top, rest);
        top = elem;
    }

    /**
     * Removes the element at the top of this stack and returns it.
     *
     * @return          The element that was just removed from this stack
     */
    public Element pop() {
        Element elem = top;
        top = rest.top;
        rest = rest.rest;
        return elem;
    }

    /**
     * Returns the element at the top of this stack without removing it.
     *
     * @return          The top element of this stack
     */
    public Element peek() {
        return top;
    }

    /**
     * Removes all elements of this stack.
     */
    public void clear() {
        top = null;
        rest = null;
    }

    /**
     * Indicates whether this stack contains no elements.
     *
     * @return          <CODE>true</CODE> if this stack is empty;
     *                  <CODE>false</CODE> otherwise
     */
    public boolean isEmpty() {
        return rest == null;
    }

}
