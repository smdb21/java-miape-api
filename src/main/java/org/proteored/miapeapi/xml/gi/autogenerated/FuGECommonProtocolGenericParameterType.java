//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-257 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.07.18 at 02:51:39 PM CEST 
//


package org.proteored.miapeapi.xml.gi.autogenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 *  A subclass of the abstract Parameter class to represent a parameter that is defined by a controlled vocabulary term. 
 * 
 * <p>Java class for FuGE.Common.Protocol.GenericParameterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FuGE.Common.Protocol.GenericParameterType">
 *   &lt;complexContent>
 *     &lt;extension base="{}FuGE.Common.Protocol.ParameterType">
 *       &lt;sequence>
 *         &lt;element name="parameterType" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{}cvParam"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FuGE.Common.Protocol.GenericParameterType", propOrder = {
    "parameterType"
})
public class FuGECommonProtocolGenericParameterType
    extends FuGECommonProtocolParameterType
{

    protected FuGECommonProtocolGenericParameterType.ParameterType parameterType;

    /**
     * Gets the value of the parameterType property.
     * 
     * @return
     *     possible object is
     *     {@link FuGECommonProtocolGenericParameterType.ParameterType }
     *     
     */
    public FuGECommonProtocolGenericParameterType.ParameterType getParameterType() {
        return parameterType;
    }

    /**
     * Sets the value of the parameterType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuGECommonProtocolGenericParameterType.ParameterType }
     *     
     */
    public void setParameterType(FuGECommonProtocolGenericParameterType.ParameterType value) {
        this.parameterType = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{}cvParam"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "cvParam"
    })
    public static class ParameterType {

        @XmlElement(required = true)
        protected FuGECommonOntologyCvParamType cvParam;

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

    }

}
