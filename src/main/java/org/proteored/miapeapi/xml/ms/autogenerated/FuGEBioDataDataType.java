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
 *  Data can be an input to or an output from a ProtocolApplication. Data may be produced from a Material (data acquisition) or from another Data object (data transformation).  Examples of Data are gene expression measurements, or phenotypes associated with genetic manipulations. 
 * 
 * <p>Java class for FuGE.Bio.Data.DataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FuGE.Bio.Data.DataType">
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
@XmlType(name = "FuGE.Bio.Data.DataType")
@XmlSeeAlso({
    FuGEBioDataExternalDataType.class,
    FuGEBioDataInternalDataType.class
})
public abstract class FuGEBioDataDataType
    extends FuGECommonIdentifiableType
{


}
