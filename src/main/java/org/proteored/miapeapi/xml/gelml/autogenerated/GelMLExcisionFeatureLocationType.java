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


/**
 *  Abstract superclass representing the different types of method that could be
 *     used to identify a location. 
 * 
 * <p>Java class for GelML.Excision.FeatureLocationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GelML.Excision.FeatureLocationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="centroidX" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="centroidY" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GelML.Excision.FeatureLocationType")
@XmlSeeAlso({
    GelMLExcisionRectangleType.class,
    GelMLExcisionBoundaryPointSetType.class,
    GelMLExcisionCircleType.class,
    GelMLExcisionBoundaryChainType.class
})
public class GelMLExcisionFeatureLocationType {

    @XmlAttribute
    protected Integer centroidX;
    @XmlAttribute
    protected Integer centroidY;

    /**
     * Gets the value of the centroidX property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCentroidX() {
        return centroidX;
    }

    /**
     * Sets the value of the centroidX property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCentroidX(Integer value) {
        this.centroidX = value;
    }

    /**
     * Gets the value of the centroidY property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCentroidY() {
        return centroidY;
    }

    /**
     * Sets the value of the centroidY property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCentroidY(Integer value) {
        this.centroidY = value;
    }

}
