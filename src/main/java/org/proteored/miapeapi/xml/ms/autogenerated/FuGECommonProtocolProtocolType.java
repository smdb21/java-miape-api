//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.06.19 at 01:51:42 PM CEST 
//


package org.proteored.miapeapi.xml.ms.autogenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 *  A Protocol is a parameterizable description of a method.  ProtocolApplication is used to specify the ParameterValues of its Protocol's Parameters.   Protocol should be extended in data formats. For cases where no extension is developed, the subclass of Protocol, GenericProtocol, should be used to capture experimental protocols. 
 * 
 * <p>Java class for FuGE.Common.Protocol.ProtocolType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FuGE.Common.Protocol.ProtocolType">
 *   &lt;complexContent>
 *     &lt;extension base="{}FuGE.Common.IdentifiableType">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FuGE.Common.Protocol.ProtocolType")
@XmlSeeAlso({
    FuGECommonProtocolGenericProtocolType.class
})
public abstract class FuGECommonProtocolProtocolType
    extends FuGECommonIdentifiableType
{


}
