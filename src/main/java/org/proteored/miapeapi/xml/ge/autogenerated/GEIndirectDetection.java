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
 *         &lt;element name="Transfer_Medium" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Detection_Medium" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Protocol" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GE_Detection_Agent" type="{}MIAPE_Substance_Type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="GE_Detection_Additional_Agent" type="{}MIAPE_Substance_Type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="GE_Detection_Buffer" type="{}MIAPE_Buffer_Type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="GE_Detection_Equipment" type="{}MIAPE_GE_Equipment_Type" maxOccurs="unbounded" minOccurs="0"/>
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
    "transferMedium",
    "detectionMedium",
    "protocol",
    "geDetectionAgent",
    "geDetectionAdditionalAgent",
    "geDetectionBuffer",
    "geDetectionEquipment"
})
@XmlRootElement(name = "GE_Indirect_Detection")
public class GEIndirectDetection {

    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "Transfer_Medium")
    protected String transferMedium;
    @XmlElement(name = "Detection_Medium")
    protected String detectionMedium;
    @XmlElement(name = "Protocol")
    protected String protocol;
    @XmlElement(name = "GE_Detection_Agent")
    protected List<MIAPESubstanceType> geDetectionAgent;
    @XmlElement(name = "GE_Detection_Additional_Agent")
    protected List<MIAPESubstanceType> geDetectionAdditionalAgent;
    @XmlElement(name = "GE_Detection_Buffer")
    protected List<MIAPEBufferType> geDetectionBuffer;
    @XmlElement(name = "GE_Detection_Equipment")
    protected List<MIAPEGEEquipmentType> geDetectionEquipment;
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
     * Gets the value of the transferMedium property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransferMedium() {
        return transferMedium;
    }

    /**
     * Sets the value of the transferMedium property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransferMedium(String value) {
        this.transferMedium = value;
    }

    /**
     * Gets the value of the detectionMedium property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetectionMedium() {
        return detectionMedium;
    }

    /**
     * Sets the value of the detectionMedium property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetectionMedium(String value) {
        this.detectionMedium = value;
    }

    /**
     * Gets the value of the protocol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets the value of the protocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocol(String value) {
        this.protocol = value;
    }

    /**
     * Gets the value of the geDetectionAgent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the geDetectionAgent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGEDetectionAgent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MIAPESubstanceType }
     * 
     * 
     */
    public List<MIAPESubstanceType> getGEDetectionAgent() {
        if (geDetectionAgent == null) {
            geDetectionAgent = new ArrayList<MIAPESubstanceType>();
        }
        return this.geDetectionAgent;
    }

    /**
     * Gets the value of the geDetectionAdditionalAgent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the geDetectionAdditionalAgent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGEDetectionAdditionalAgent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MIAPESubstanceType }
     * 
     * 
     */
    public List<MIAPESubstanceType> getGEDetectionAdditionalAgent() {
        if (geDetectionAdditionalAgent == null) {
            geDetectionAdditionalAgent = new ArrayList<MIAPESubstanceType>();
        }
        return this.geDetectionAdditionalAgent;
    }

    /**
     * Gets the value of the geDetectionBuffer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the geDetectionBuffer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGEDetectionBuffer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MIAPEBufferType }
     * 
     * 
     */
    public List<MIAPEBufferType> getGEDetectionBuffer() {
        if (geDetectionBuffer == null) {
            geDetectionBuffer = new ArrayList<MIAPEBufferType>();
        }
        return this.geDetectionBuffer;
    }

    /**
     * Gets the value of the geDetectionEquipment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the geDetectionEquipment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGEDetectionEquipment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MIAPEGEEquipmentType }
     * 
     * 
     */
    public List<MIAPEGEEquipmentType> getGEDetectionEquipment() {
        if (geDetectionEquipment == null) {
            geDetectionEquipment = new ArrayList<MIAPEGEEquipmentType>();
        }
        return this.geDetectionEquipment;
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
