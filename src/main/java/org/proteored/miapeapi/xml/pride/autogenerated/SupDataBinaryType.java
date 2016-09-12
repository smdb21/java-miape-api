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
 * Extension of binary data group for supplemental data
 * 
 * <p>Java class for supDataBinaryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="supDataBinaryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arrayName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;group ref="{}binaryDataGroup"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "supDataBinaryType", propOrder = {
    "arrayName",
    "data"
})
public class SupDataBinaryType {

    @XmlElement(required = true)
    protected String arrayName;
    @XmlElement(required = true)
    protected org.proteored.miapeapi.xml.pride.autogenerated.PeakListBinaryType.Data data;
    @XmlAttribute(name = "id", required = true)
    protected int id;

    /**
     * Gets the value of the arrayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArrayName() {
        return arrayName;
    }

    /**
     * Sets the value of the arrayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArrayName(String value) {
        this.arrayName = value;
    }

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     {@link org.proteored.miapeapi.xml.pride.autogenerated.PeakListBinaryType.Data }
     *     
     */
    public org.proteored.miapeapi.xml.pride.autogenerated.PeakListBinaryType.Data getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.proteored.miapeapi.xml.pride.autogenerated.PeakListBinaryType.Data }
     *     
     */
    public void setData(org.proteored.miapeapi.xml.pride.autogenerated.PeakListBinaryType.Data value) {
        this.data = value;
    }

    /**
     * Gets the value of the id property.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

}
