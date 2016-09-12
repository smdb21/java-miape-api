//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.11 at 04:01:56 PM CEST 
//


package org.proteored.miapeapi.xml.msi.autogenerated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 *  A range value. 
 * 
 * <p>Java class for FuGE.Common.Measurement.RangeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FuGE.Common.Measurement.RangeType">
 *   &lt;complexContent>
 *     &lt;extension base="{}FuGE.Common.Measurement.MeasurementType">
 *       &lt;sequence>
 *         &lt;element name="lowerLimit">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{}PropertyValue"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="upperLimit">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{}PropertyValue"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="rangeDescriptors" maxOccurs="unbounded" minOccurs="0">
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
@XmlType(name = "FuGE.Common.Measurement.RangeType", propOrder = {
    "lowerLimit",
    "upperLimit",
    "rangeDescriptors"
})
public class FuGECommonMeasurementRangeType
    extends FuGECommonMeasurementMeasurementType
{

    @XmlElement(required = true)
    protected FuGECommonMeasurementRangeType.LowerLimit lowerLimit;
    @XmlElement(required = true)
    protected FuGECommonMeasurementRangeType.UpperLimit upperLimit;
    protected List<FuGECommonMeasurementRangeType.RangeDescriptors> rangeDescriptors;

    /**
     * Gets the value of the lowerLimit property.
     * 
     * @return
     *     possible object is
     *     {@link FuGECommonMeasurementRangeType.LowerLimit }
     *     
     */
    public FuGECommonMeasurementRangeType.LowerLimit getLowerLimit() {
        return lowerLimit;
    }

    /**
     * Sets the value of the lowerLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuGECommonMeasurementRangeType.LowerLimit }
     *     
     */
    public void setLowerLimit(FuGECommonMeasurementRangeType.LowerLimit value) {
        this.lowerLimit = value;
    }

    /**
     * Gets the value of the upperLimit property.
     * 
     * @return
     *     possible object is
     *     {@link FuGECommonMeasurementRangeType.UpperLimit }
     *     
     */
    public FuGECommonMeasurementRangeType.UpperLimit getUpperLimit() {
        return upperLimit;
    }

    /**
     * Sets the value of the upperLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuGECommonMeasurementRangeType.UpperLimit }
     *     
     */
    public void setUpperLimit(FuGECommonMeasurementRangeType.UpperLimit value) {
        this.upperLimit = value;
    }

    /**
     * Gets the value of the rangeDescriptors property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rangeDescriptors property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRangeDescriptors().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FuGECommonMeasurementRangeType.RangeDescriptors }
     * 
     * 
     */
    public List<FuGECommonMeasurementRangeType.RangeDescriptors> getRangeDescriptors() {
        if (rangeDescriptors == null) {
            rangeDescriptors = new ArrayList<FuGECommonMeasurementRangeType.RangeDescriptors>();
        }
        return this.rangeDescriptors;
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
     *         &lt;element ref="{}PropertyValue"/>
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
        "propertyValue"
    })
    public static class LowerLimit {

        @XmlElement(name = "PropertyValue", required = true)
        protected FuGECommonOntologyPropertyValue propertyValue;

        /**
         * Gets the value of the propertyValue property.
         * 
         * @return
         *     possible object is
         *     {@link FuGECommonOntologyPropertyValue }
         *     
         */
        public FuGECommonOntologyPropertyValue getPropertyValue() {
            return propertyValue;
        }

        /**
         * Sets the value of the propertyValue property.
         * 
         * @param value
         *     allowed object is
         *     {@link FuGECommonOntologyPropertyValue }
         *     
         */
        public void setPropertyValue(FuGECommonOntologyPropertyValue value) {
            this.propertyValue = value;
        }

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
    public static class RangeDescriptors {

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
     *         &lt;element ref="{}PropertyValue"/>
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
        "propertyValue"
    })
    public static class UpperLimit {

        @XmlElement(name = "PropertyValue", required = true)
        protected FuGECommonOntologyPropertyValue propertyValue;

        /**
         * Gets the value of the propertyValue property.
         * 
         * @return
         *     possible object is
         *     {@link FuGECommonOntologyPropertyValue }
         *     
         */
        public FuGECommonOntologyPropertyValue getPropertyValue() {
            return propertyValue;
        }

        /**
         * Sets the value of the propertyValue property.
         * 
         * @param value
         *     allowed object is
         *     {@link FuGECommonOntologyPropertyValue }
         *     
         */
        public void setPropertyValue(FuGECommonOntologyPropertyValue value) {
            this.propertyValue = value;
        }

    }

}
