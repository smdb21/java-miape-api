//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-257 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.09.14 at 07:25:56 PM CEST 
//


package org.proteored.miapeapi.xml.mzidentml.autogenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParamType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParamType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://psidev.info/psi/pi/mzIdentML/1.0}ParamGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParamType", propOrder = {
    "cvParam",
    "userParam"
})
public class ParamType {

    protected FuGECommonOntologyCvParamType cvParam;
    protected FuGECommonOntologyUserParamType userParam;

    /**
     * Gets the value of the cvParam property.
     * 
     * @return
     *     possible object is
     *     {@link FuGECommonOntologyCvParamType }
     *     
     */
    public FuGECommonOntologyCvParamType getCvParam() {
        return cvParam;
    }

    /**
     * Sets the value of the cvParam property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuGECommonOntologyCvParamType }
     *     
     */
    public void setCvParam(FuGECommonOntologyCvParamType value) {
        this.cvParam = value;
    }

    /**
     * Gets the value of the userParam property.
     * 
     * @return
     *     possible object is
     *     {@link FuGECommonOntologyUserParamType }
     *     
     */
    public FuGECommonOntologyUserParamType getUserParam() {
        return userParam;
    }

    /**
     * Sets the value of the userParam property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuGECommonOntologyUserParamType }
     *     
     */
    public void setUserParam(FuGECommonOntologyUserParamType value) {
        this.userParam = value;
    }

}
