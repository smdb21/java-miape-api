//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.09.16 at 08:13:24 PM CEST 
//


package org.proteored.miapeapi.xml.pride.autogenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * The method of precursor ion selection and activation
 * 
 * <p>Java class for precursorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="precursorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ionSelection" type="{}paramType"/>
 *         &lt;element name="activation" type="{}paramType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="msLevel" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="spectrumRef" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "precursorType", propOrder = {
    "ionSelection",
    "activation"
})
public class PrecursorType {

    @XmlElement(required = true)
    protected ParamType ionSelection;
    @XmlElement(required = true)
    protected ParamType activation;
    @XmlAttribute(name = "msLevel", required = true)
    protected int msLevel;
    @XmlAttribute(name = "spectrumRef", required = true)
    protected int spectrumRef;

    /**
     * Gets the value of the ionSelection property.
     * 
     * @return
     *     possible object is
     *     {@link ParamType }
     *     
     */
    public ParamType getIonSelection() {
        return ionSelection;
    }

    /**
     * Sets the value of the ionSelection property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParamType }
     *     
     */
    public void setIonSelection(ParamType value) {
        this.ionSelection = value;
    }

    /**
     * Gets the value of the activation property.
     * 
     * @return
     *     possible object is
     *     {@link ParamType }
     *     
     */
    public ParamType getActivation() {
        return activation;
    }

    /**
     * Sets the value of the activation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParamType }
     *     
     */
    public void setActivation(ParamType value) {
        this.activation = value;
    }

    /**
     * Gets the value of the msLevel property.
     * 
     */
    public int getMsLevel() {
        return msLevel;
    }

    /**
     * Sets the value of the msLevel property.
     * 
     */
    public void setMsLevel(int value) {
        this.msLevel = value;
    }

    /**
     * Gets the value of the spectrumRef property.
     * 
     */
    public int getSpectrumRef() {
        return spectrumRef;
    }

    /**
     * Sets the value of the spectrumRef property.
     * 
     */
    public void setSpectrumRef(int value) {
        this.spectrumRef = value;
    }

}
