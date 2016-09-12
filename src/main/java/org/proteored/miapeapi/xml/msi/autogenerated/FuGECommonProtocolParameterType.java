//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.11 at 04:01:56 PM CEST 
//


package org.proteored.miapeapi.xml.msi.autogenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 *  A Parameter is a replaceable value in a Parameterizable class, and uses the Measurement class for giving a specific type of value.  Examples of Parameters include: scanning wavelength, laser power, centrifuge speed, multiplicative errors, the number of input nodes to a SOM, and PCR temperatures.   Parameter is abstract and should be extended by subclassing. The GenericParameter class offers the functionality of a parameter defined by a controlled vocabulary term. 
 * 
 * <p>Java class for FuGE.Common.Protocol.ParameterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FuGE.Common.Protocol.ParameterType">
 *   &lt;complexContent>
 *     &lt;extension base="{}FuGE.Common.IdentifiableType">
 *       &lt;sequence>
 *         &lt;group ref="{}MeasurementGroup" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FuGE.Common.Protocol.ParameterType", propOrder = {
    "atomicValue",
    "booleanValue",
    "complexValue",
    "range"
})
@XmlSeeAlso({
    FuGECommonProtocolGenericParameterType.class
})
public abstract class FuGECommonProtocolParameterType
    extends FuGECommonIdentifiableType
{

    @XmlElement(name = "AtomicValue")
    protected FuGECommonMeasurementAtomicValueType atomicValue;
    @XmlElement(name = "BooleanValue")
    protected FuGECommonMeasurementBooleanValueType booleanValue;
    @XmlElement(name = "ComplexValue")
    protected FuGECommonMeasurementComplexValueType complexValue;
    @XmlElement(name = "Range")
    protected FuGECommonMeasurementRangeType range;

    /**
     * Gets the value of the atomicValue property.
     * 
     * @return
     *     possible object is
     *     {@link FuGECommonMeasurementAtomicValueType }
     *     
     */
    public FuGECommonMeasurementAtomicValueType getAtomicValue() {
        return atomicValue;
    }

    /**
     * Sets the value of the atomicValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuGECommonMeasurementAtomicValueType }
     *     
     */
    public void setAtomicValue(FuGECommonMeasurementAtomicValueType value) {
        this.atomicValue = value;
    }

    /**
     * Gets the value of the booleanValue property.
     * 
     * @return
     *     possible object is
     *     {@link FuGECommonMeasurementBooleanValueType }
     *     
     */
    public FuGECommonMeasurementBooleanValueType getBooleanValue() {
        return booleanValue;
    }

    /**
     * Sets the value of the booleanValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuGECommonMeasurementBooleanValueType }
     *     
     */
    public void setBooleanValue(FuGECommonMeasurementBooleanValueType value) {
        this.booleanValue = value;
    }

    /**
     * Gets the value of the complexValue property.
     * 
     * @return
     *     possible object is
     *     {@link FuGECommonMeasurementComplexValueType }
     *     
     */
    public FuGECommonMeasurementComplexValueType getComplexValue() {
        return complexValue;
    }

    /**
     * Sets the value of the complexValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuGECommonMeasurementComplexValueType }
     *     
     */
    public void setComplexValue(FuGECommonMeasurementComplexValueType value) {
        this.complexValue = value;
    }

    /**
     * Gets the value of the range property.
     * 
     * @return
     *     possible object is
     *     {@link FuGECommonMeasurementRangeType }
     *     
     */
    public FuGECommonMeasurementRangeType getRange() {
        return range;
    }

    /**
     * Sets the value of the range property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuGECommonMeasurementRangeType }
     *     
     */
    public void setRange(FuGECommonMeasurementRangeType value) {
        this.range = value;
    }

}
