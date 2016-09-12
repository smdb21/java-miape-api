//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-257 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.07.18 at 02:51:38 PM CEST 
//


package org.proteored.miapeapi.xml.ge.autogenerated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Electrophoresis_Conditions" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GE_Running_Buffer" type="{}MIAPE_Buffer_Type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="GE_EProtocol_Additional_Buffer" type="{}MIAPE_Buffer_Type" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "name",
    "electrophoresisConditions",
    "geRunningBuffer",
    "geeProtocolAdditionalBuffer"
})
@XmlRootElement(name = "GE_Electrophoresis_Protocol")
public class GEElectrophoresisProtocol {

    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "Electrophoresis_Conditions")
    protected String electrophoresisConditions;
    @XmlElement(name = "GE_Running_Buffer")
    protected List<MIAPEBufferType> geRunningBuffer;
    @XmlElement(name = "GE_EProtocol_Additional_Buffer")
    protected List<MIAPEBufferType> geeProtocolAdditionalBuffer;
    @XmlAttribute(required = true)
    protected String id;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the electrophoresisConditions property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElectrophoresisConditions() {
        return electrophoresisConditions;
    }

    /**
     * Sets the value of the electrophoresisConditions property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElectrophoresisConditions(String value) {
        this.electrophoresisConditions = value;
    }

    /**
     * Gets the value of the geRunningBuffer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the geRunningBuffer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGERunningBuffer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MIAPEBufferType }
     * 
     * 
     */
    public List<MIAPEBufferType> getGERunningBuffer() {
        if (geRunningBuffer == null) {
            geRunningBuffer = new ArrayList<MIAPEBufferType>();
        }
        return this.geRunningBuffer;
    }

    /**
     * Gets the value of the geeProtocolAdditionalBuffer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the geeProtocolAdditionalBuffer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGEEProtocolAdditionalBuffer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MIAPEBufferType }
     * 
     * 
     */
    public List<MIAPEBufferType> getGEEProtocolAdditionalBuffer() {
        if (geeProtocolAdditionalBuffer == null) {
            geeProtocolAdditionalBuffer = new ArrayList<MIAPEBufferType>();
        }
        return this.geeProtocolAdditionalBuffer;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
