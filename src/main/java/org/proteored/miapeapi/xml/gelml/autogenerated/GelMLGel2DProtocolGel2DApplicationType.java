//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-257 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.09.14 at 07:25:51 PM CEST 
//


package org.proteored.miapeapi.xml.gelml.autogenerated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 *  Gel2DApplication is the application of a Gel2DProtocol. An instance of
 *     Gel2DApplication should reference SampleLoadingApplication, ElectrophoresisApplication,
 *     DetectionApplications and GenericProtocolApplication as its sub-steps, as defined by the
 *     referenced Gel2DProtocol. 
 * 
 * <p>Java class for GelML.Gel2DProtocol.Gel2DApplicationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GelML.Gel2DProtocol.Gel2DApplicationType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.psidev.info/gelml/1_1candidate}FuGE.Common.Protocol.ProtocolApplicationType">
 *       &lt;sequence>
 *         &lt;element name="inputFirstDimension">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}Gel"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="inputSecondDimension">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}Gel"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="output">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}Gel2D"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ChildProtocolApplications">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}SampleLoadingApplication" maxOccurs="unbounded"/>
 *                   &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}ElectrophoresisApplication" maxOccurs="2" minOccurs="2"/>
 *                   &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}DetectionApplication" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Gel2DProtocol_ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GelML.Gel2DProtocol.Gel2DApplicationType", propOrder = {
    "inputFirstDimension",
    "inputSecondDimension",
    "output",
    "childProtocolApplications"
})
public class GelMLGel2DProtocolGel2DApplicationType
    extends FuGECommonProtocolProtocolApplicationType
{

    @XmlElement(required = true)
    protected GelMLGel2DProtocolGel2DApplicationType.InputFirstDimension inputFirstDimension;
    @XmlElement(required = true)
    protected GelMLGel2DProtocolGel2DApplicationType.InputSecondDimension inputSecondDimension;
    @XmlElement(required = true)
    protected GelMLGel2DProtocolGel2DApplicationType.Output output;
    @XmlElement(name = "ChildProtocolApplications", required = true)
    protected GelMLGel2DProtocolGel2DApplicationType.ChildProtocolApplications childProtocolApplications;
    @XmlAttribute(name = "Gel2DProtocol_ref", required = true)
    protected String gel2DProtocolRef;

    /**
     * Gets the value of the inputFirstDimension property.
     * 
     * @return
     *     possible object is
     *     {@link GelMLGel2DProtocolGel2DApplicationType.InputFirstDimension }
     *     
     */
    public GelMLGel2DProtocolGel2DApplicationType.InputFirstDimension getInputFirstDimension() {
        return inputFirstDimension;
    }

    /**
     * Sets the value of the inputFirstDimension property.
     * 
     * @param value
     *     allowed object is
     *     {@link GelMLGel2DProtocolGel2DApplicationType.InputFirstDimension }
     *     
     */
    public void setInputFirstDimension(GelMLGel2DProtocolGel2DApplicationType.InputFirstDimension value) {
        this.inputFirstDimension = value;
    }

    /**
     * Gets the value of the inputSecondDimension property.
     * 
     * @return
     *     possible object is
     *     {@link GelMLGel2DProtocolGel2DApplicationType.InputSecondDimension }
     *     
     */
    public GelMLGel2DProtocolGel2DApplicationType.InputSecondDimension getInputSecondDimension() {
        return inputSecondDimension;
    }

    /**
     * Sets the value of the inputSecondDimension property.
     * 
     * @param value
     *     allowed object is
     *     {@link GelMLGel2DProtocolGel2DApplicationType.InputSecondDimension }
     *     
     */
    public void setInputSecondDimension(GelMLGel2DProtocolGel2DApplicationType.InputSecondDimension value) {
        this.inputSecondDimension = value;
    }

    /**
     * Gets the value of the output property.
     * 
     * @return
     *     possible object is
     *     {@link GelMLGel2DProtocolGel2DApplicationType.Output }
     *     
     */
    public GelMLGel2DProtocolGel2DApplicationType.Output getOutput() {
        return output;
    }

    /**
     * Sets the value of the output property.
     * 
     * @param value
     *     allowed object is
     *     {@link GelMLGel2DProtocolGel2DApplicationType.Output }
     *     
     */
    public void setOutput(GelMLGel2DProtocolGel2DApplicationType.Output value) {
        this.output = value;
    }

    /**
     * Gets the value of the childProtocolApplications property.
     * 
     * @return
     *     possible object is
     *     {@link GelMLGel2DProtocolGel2DApplicationType.ChildProtocolApplications }
     *     
     */
    public GelMLGel2DProtocolGel2DApplicationType.ChildProtocolApplications getChildProtocolApplications() {
        return childProtocolApplications;
    }

    /**
     * Sets the value of the childProtocolApplications property.
     * 
     * @param value
     *     allowed object is
     *     {@link GelMLGel2DProtocolGel2DApplicationType.ChildProtocolApplications }
     *     
     */
    public void setChildProtocolApplications(GelMLGel2DProtocolGel2DApplicationType.ChildProtocolApplications value) {
        this.childProtocolApplications = value;
    }

    /**
     * Gets the value of the gel2DProtocolRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGel2DProtocolRef() {
        return gel2DProtocolRef;
    }

    /**
     * Sets the value of the gel2DProtocolRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGel2DProtocolRef(String value) {
        this.gel2DProtocolRef = value;
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
     *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}SampleLoadingApplication" maxOccurs="unbounded"/>
     *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}ElectrophoresisApplication" maxOccurs="2" minOccurs="2"/>
     *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}DetectionApplication" maxOccurs="unbounded" minOccurs="0"/>
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
        "sampleLoadingApplication",
        "electrophoresisApplication",
        "detectionApplication"
    })
    public static class ChildProtocolApplications {

        @XmlElement(name = "SampleLoadingApplication", required = true)
        protected List<GelMLSampleLoadingSampleLoadingApplicationType> sampleLoadingApplication;
        @XmlElement(name = "ElectrophoresisApplication", required = true)
        protected List<GelMLElectrophoresisElectrophoresisApplicationType> electrophoresisApplication;
        @XmlElementRef(name = "DetectionApplication", namespace = "http://www.psidev.info/gelml/1_1candidate", type = JAXBElement.class)
        protected List<JAXBElement<? extends GelMLDetectionDetectionApplicationType>> detectionApplication;

        /**
         * Gets the value of the sampleLoadingApplication property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the sampleLoadingApplication property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSampleLoadingApplication().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link GelMLSampleLoadingSampleLoadingApplicationType }
         * 
         * 
         */
        public List<GelMLSampleLoadingSampleLoadingApplicationType> getSampleLoadingApplication() {
            if (sampleLoadingApplication == null) {
                sampleLoadingApplication = new ArrayList<GelMLSampleLoadingSampleLoadingApplicationType>();
            }
            return this.sampleLoadingApplication;
        }

        /**
         * Gets the value of the electrophoresisApplication property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the electrophoresisApplication property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getElectrophoresisApplication().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link GelMLElectrophoresisElectrophoresisApplicationType }
         * 
         * 
         */
        public List<GelMLElectrophoresisElectrophoresisApplicationType> getElectrophoresisApplication() {
            if (electrophoresisApplication == null) {
                electrophoresisApplication = new ArrayList<GelMLElectrophoresisElectrophoresisApplicationType>();
            }
            return this.electrophoresisApplication;
        }

        /**
         * Gets the value of the detectionApplication property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the detectionApplication property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDetectionApplication().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link JAXBElement }{@code <}{@link GelMLDetectionDirectDetectionType }{@code >}
         * {@link JAXBElement }{@code <}{@link GelMLDetectionDetectionApplicationType }{@code >}
         * {@link JAXBElement }{@code <}{@link GelMLDetectionIndirectDetectionType }{@code >}
         * 
         * 
         */
        public List<JAXBElement<? extends GelMLDetectionDetectionApplicationType>> getDetectionApplication() {
            if (detectionApplication == null) {
                detectionApplication = new ArrayList<JAXBElement<? extends GelMLDetectionDetectionApplicationType>>();
            }
            return this.detectionApplication;
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
     *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}Gel"/>
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
        "gel"
    })
    public static class InputFirstDimension {

        @XmlElement(name = "Gel", required = true)
        protected GelMLGelGelType gel;

        /**
         * Gets the value of the gel property.
         * 
         * @return
         *     possible object is
         *     {@link GelMLGelGelType }
         *     
         */
        public GelMLGelGelType getGel() {
            return gel;
        }

        /**
         * Sets the value of the gel property.
         * 
         * @param value
         *     allowed object is
         *     {@link GelMLGelGelType }
         *     
         */
        public void setGel(GelMLGelGelType value) {
            this.gel = value;
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
     *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}Gel"/>
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
        "gel"
    })
    public static class InputSecondDimension {

        @XmlElement(name = "Gel", required = true)
        protected GelMLGelGelType gel;

        /**
         * Gets the value of the gel property.
         * 
         * @return
         *     possible object is
         *     {@link GelMLGelGelType }
         *     
         */
        public GelMLGelGelType getGel() {
            return gel;
        }

        /**
         * Sets the value of the gel property.
         * 
         * @param value
         *     allowed object is
         *     {@link GelMLGelGelType }
         *     
         */
        public void setGel(GelMLGelGelType value) {
            this.gel = value;
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
     *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}Gel2D"/>
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
        "gel2D"
    })
    public static class Output {

        @XmlElement(name = "Gel2D", required = true)
        protected GelMLGel2DProtocolGel2DType gel2D;

        /**
         * Gets the value of the gel2D property.
         * 
         * @return
         *     possible object is
         *     {@link GelMLGel2DProtocolGel2DType }
         *     
         */
        public GelMLGel2DProtocolGel2DType getGel2D() {
            return gel2D;
        }

        /**
         * Sets the value of the gel2D property.
         * 
         * @param value
         *     allowed object is
         *     {@link GelMLGel2DProtocolGel2DType }
         *     
         */
        public void setGel2D(GelMLGel2DProtocolGel2DType value) {
            this.gel2D = value;
        }

    }

}
