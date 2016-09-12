//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-257 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.09.14 at 07:25:51 PM CEST 
//


package org.proteored.miapeapi.xml.gelml.autogenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 *  The use of a protocol with the requisite Parameters and ParameterValues. ProtocolApplications can take Material or Data (or both) as input and produce Material or Data (or both) as output. ProtocolApplication is abstract and should be subclassed in the development of modular formats. The subclass GenericProtocolApplication can be used without extension. 
 * 
 * <p>Java class for FuGE.Common.Protocol.ProtocolApplicationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FuGE.Common.Protocol.ProtocolApplicationType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.psidev.info/gelml/1_1candidate}FuGE.Common.IdentifiableType">
 *       &lt;attribute name="activityDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FuGE.Common.Protocol.ProtocolApplicationType")
@XmlSeeAlso({
    GelMLGel1DProtocolGel1DApplicationType.class,
    GelMLGelGelManufactureApplicationType.class,
    GelMLExcisionExcisionApplicationType.class,
    GelMLElectrophoresisElectrophoresisApplicationType.class,
    FuGECommonProtocolGenericProtocolApplicationType.class,
    GelMLSampleLoadingSampleLoadingApplicationType.class,
    GelMLImageAcquisitionImageAcquisitionApplicationType.class,
    GelMLGel2DProtocolGel2DApplicationType.class,
    GelMLDetectionDetectionApplicationType.class,
    GelMLOtherGelProtocolOtherGelApplicationType.class
})
public abstract class FuGECommonProtocolProtocolApplicationType
    extends FuGECommonIdentifiableType
{

    @XmlAttribute
    protected XMLGregorianCalendar activityDate;

    /**
     * Gets the value of the activityDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getActivityDate() {
        return activityDate;
    }

    /**
     * Sets the value of the activityDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setActivityDate(XMLGregorianCalendar value) {
        this.activityDate = value;
    }

}
