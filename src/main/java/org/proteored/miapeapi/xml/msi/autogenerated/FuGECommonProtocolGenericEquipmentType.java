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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 *  A subclass of the abstract Equipment class for capturing the description of Equipment used. 
 * 
 * <p>Java class for FuGE.Common.Protocol.GenericEquipmentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FuGE.Common.Protocol.GenericEquipmentType">
 *   &lt;complexContent>
 *     &lt;extension base="{}FuGE.Common.Protocol.EquipmentType">
 *       &lt;sequence>
 *         &lt;element name="software" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="GenericSoftware_ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{}GenericParameter" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="equipmentParts" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="GenericEquipment_ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
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
@XmlType(name = "FuGE.Common.Protocol.GenericEquipmentType", propOrder = {
    "software",
    "genericParameter",
    "equipmentParts"
})
public class FuGECommonProtocolGenericEquipmentType
    extends FuGECommonProtocolEquipmentType
{

    protected List<FuGECommonProtocolGenericEquipmentType.Software> software;
    @XmlElement(name = "GenericParameter")
    protected List<FuGECommonProtocolGenericParameterType> genericParameter;
    protected List<FuGECommonProtocolGenericEquipmentType.EquipmentParts> equipmentParts;

    /**
     * Gets the value of the software property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the software property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSoftware().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FuGECommonProtocolGenericEquipmentType.Software }
     * 
     * 
     */
    public List<FuGECommonProtocolGenericEquipmentType.Software> getSoftware() {
        if (software == null) {
            software = new ArrayList<FuGECommonProtocolGenericEquipmentType.Software>();
        }
        return this.software;
    }

    /**
     *  The parameters for this piece of GenericEquipment. Gets the value of the genericParameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the genericParameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGenericParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FuGECommonProtocolGenericParameterType }
     * 
     * 
     */
    public List<FuGECommonProtocolGenericParameterType> getGenericParameter() {
        if (genericParameter == null) {
            genericParameter = new ArrayList<FuGECommonProtocolGenericParameterType>();
        }
        return this.genericParameter;
    }

    /**
     * Gets the value of the equipmentParts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the equipmentParts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEquipmentParts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FuGECommonProtocolGenericEquipmentType.EquipmentParts }
     * 
     * 
     */
    public List<FuGECommonProtocolGenericEquipmentType.EquipmentParts> getEquipmentParts() {
        if (equipmentParts == null) {
            equipmentParts = new ArrayList<FuGECommonProtocolGenericEquipmentType.EquipmentParts>();
        }
        return this.equipmentParts;
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
     *       &lt;attribute name="GenericEquipment_ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class EquipmentParts {

        @XmlAttribute(name = "GenericEquipment_ref", required = true)
        protected String genericEquipmentRef;

        /**
         * Gets the value of the genericEquipmentRef property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGenericEquipmentRef() {
            return genericEquipmentRef;
        }

        /**
         * Sets the value of the genericEquipmentRef property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGenericEquipmentRef(String value) {
            this.genericEquipmentRef = value;
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
     *       &lt;attribute name="GenericSoftware_ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Software {

        @XmlAttribute(name = "GenericSoftware_ref", required = true)
        protected String genericSoftwareRef;

        /**
         * Gets the value of the genericSoftwareRef property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGenericSoftwareRef() {
            return genericSoftwareRef;
        }

        /**
         * Sets the value of the genericSoftwareRef property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGenericSoftwareRef(String value) {
            this.genericSoftwareRef = value;
        }

    }

}
