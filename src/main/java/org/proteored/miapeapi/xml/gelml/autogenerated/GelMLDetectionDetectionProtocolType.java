//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-257 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.09.14 at 07:25:51 PM CEST 
//


package org.proteored.miapeapi.xml.gelml.autogenerated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 *  The process by which proteins are to be detected on a gel, either by a direct
 *     process e.g. staining or by an indirect process e.g. Western blot. The type of protocol should
 *     be specified using the types association inherited from Protocol 
 * 
 * <p>Java class for GelML.Detection.DetectionProtocolType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GelML.Detection.DetectionProtocolType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.psidev.info/gelml/1_1candidate}FuGE.Common.Protocol.ProtocolType">
 *       &lt;sequence>
 *         &lt;element name="protocolText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DetectionAgent" type="{http://www.psidev.info/gelml/1_1candidate}ParamType" maxOccurs="unbounded"/>
 *         &lt;element name="detectionEquipment" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="GenericEquipment_ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}AddBufferAction" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GelML.Detection.DetectionProtocolType", propOrder = {
    "protocolText",
    "detectionAgent",
    "detectionEquipment",
    "addBufferAction"
})
public class GelMLDetectionDetectionProtocolType
    extends FuGECommonProtocolProtocolType
{

    @XmlElement(required = true)
    protected String protocolText;
    @XmlElement(name = "DetectionAgent", required = true)
    protected List<ParamType> detectionAgent;
    protected List<GelMLDetectionDetectionProtocolType.DetectionEquipment> detectionEquipment;
    @XmlElement(name = "AddBufferAction")
    protected List<GelMLElectrophoresisAddBufferActionType> addBufferAction;

    /**
     * Gets the value of the protocolText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocolText() {
        return protocolText;
    }

    /**
     * Sets the value of the protocolText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocolText(String value) {
        this.protocolText = value;
    }

    /**
     * Gets the value of the detectionAgent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the detectionAgent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDetectionAgent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParamType }
     * 
     * 
     */
    public List<ParamType> getDetectionAgent() {
        if (detectionAgent == null) {
            detectionAgent = new ArrayList<ParamType>();
        }
        return this.detectionAgent;
    }

    /**
     * Gets the value of the detectionEquipment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the detectionEquipment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDetectionEquipment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GelMLDetectionDetectionProtocolType.DetectionEquipment }
     * 
     * 
     */
    public List<GelMLDetectionDetectionProtocolType.DetectionEquipment> getDetectionEquipment() {
        if (detectionEquipment == null) {
            detectionEquipment = new ArrayList<GelMLDetectionDetectionProtocolType.DetectionEquipment>();
        }
        return this.detectionEquipment;
    }

    /**
     * Gets the value of the addBufferAction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addBufferAction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddBufferAction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GelMLElectrophoresisAddBufferActionType }
     * 
     * 
     */
    public List<GelMLElectrophoresisAddBufferActionType> getAddBufferAction() {
        if (addBufferAction == null) {
            addBufferAction = new ArrayList<GelMLElectrophoresisAddBufferActionType>();
        }
        return this.addBufferAction;
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
    public static class DetectionEquipment {

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

}
