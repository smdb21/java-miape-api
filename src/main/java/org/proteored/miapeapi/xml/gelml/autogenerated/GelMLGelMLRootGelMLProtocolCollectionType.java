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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 *  Collection class for accessing all Software, Protocol, Equipment and
 *     GenericProtocolApplications. 
 * 
 * <p>Java class for GelML.GelMLRoot.GelMLProtocolCollectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GelML.GelMLRoot.GelMLProtocolCollectionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}GenericEquipment" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}GenericSoftware" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}GenericProtocol" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}SampleLoadingProtocol" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}Gel2DProtocol" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}Gel1DProtocol" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}OtherGelProtocol" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}ElectrophoresisProtocol" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}SubstanceMixtureProtocol" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}DetectionProtocol" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}ImageAcquisitionProtocol" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GelML.GelMLRoot.GelMLProtocolCollectionType", propOrder = {
    "genericEquipment",
    "genericSoftware",
    "genericProtocol",
    "sampleLoadingProtocol",
    "gel2DProtocol",
    "gel1DProtocol",
    "otherGelProtocol",
    "electrophoresisProtocol",
    "substanceMixtureProtocol",
    "detectionProtocol",
    "imageAcquisitionProtocol"
})
public class GelMLGelMLRootGelMLProtocolCollectionType {

    @XmlElement(name = "GenericEquipment")
    protected List<FuGECommonProtocolGenericEquipmentType> genericEquipment;
    @XmlElement(name = "GenericSoftware")
    protected List<FuGECommonProtocolGenericSoftwareType> genericSoftware;
    @XmlElement(name = "GenericProtocol")
    protected List<FuGECommonProtocolGenericProtocolType> genericProtocol;
    @XmlElement(name = "SampleLoadingProtocol")
    protected List<GelMLSampleLoadingSampleLoadingProtocolType> sampleLoadingProtocol;
    @XmlElement(name = "Gel2DProtocol")
    protected List<GelMLGel2DProtocolGel2DProtocolType> gel2DProtocol;
    @XmlElement(name = "Gel1DProtocol")
    protected List<GelMLGel1DProtocolGel1DProtocolType> gel1DProtocol;
    @XmlElement(name = "OtherGelProtocol")
    protected List<GelMLOtherGelProtocolOtherGelProtocolType> otherGelProtocol;
    @XmlElement(name = "ElectrophoresisProtocol")
    protected List<GelMLElectrophoresisElectrophoresisProtocolType> electrophoresisProtocol;
    @XmlElement(name = "SubstanceMixtureProtocol")
    protected List<GelMLSelectSubstanceSubstanceMixtureProtocolType> substanceMixtureProtocol;
    @XmlElement(name = "DetectionProtocol")
    protected List<GelMLDetectionDetectionProtocolType> detectionProtocol;
    @XmlElement(name = "ImageAcquisitionProtocol")
    protected List<GelMLImageAcquisitionImageAcquisitionProtocolType> imageAcquisitionProtocol;

    /**
     * Gets the value of the genericEquipment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the genericEquipment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGenericEquipment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FuGECommonProtocolGenericEquipmentType }
     * 
     * 
     */
    public List<FuGECommonProtocolGenericEquipmentType> getGenericEquipment() {
        if (genericEquipment == null) {
            genericEquipment = new ArrayList<FuGECommonProtocolGenericEquipmentType>();
        }
        return this.genericEquipment;
    }

    /**
     * Gets the value of the genericSoftware property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the genericSoftware property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGenericSoftware().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FuGECommonProtocolGenericSoftwareType }
     * 
     * 
     */
    public List<FuGECommonProtocolGenericSoftwareType> getGenericSoftware() {
        if (genericSoftware == null) {
            genericSoftware = new ArrayList<FuGECommonProtocolGenericSoftwareType>();
        }
        return this.genericSoftware;
    }

    /**
     * Gets the value of the genericProtocol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the genericProtocol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGenericProtocol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FuGECommonProtocolGenericProtocolType }
     * 
     * 
     */
    public List<FuGECommonProtocolGenericProtocolType> getGenericProtocol() {
        if (genericProtocol == null) {
            genericProtocol = new ArrayList<FuGECommonProtocolGenericProtocolType>();
        }
        return this.genericProtocol;
    }

    /**
     * Gets the value of the sampleLoadingProtocol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sampleLoadingProtocol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSampleLoadingProtocol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GelMLSampleLoadingSampleLoadingProtocolType }
     * 
     * 
     */
    public List<GelMLSampleLoadingSampleLoadingProtocolType> getSampleLoadingProtocol() {
        if (sampleLoadingProtocol == null) {
            sampleLoadingProtocol = new ArrayList<GelMLSampleLoadingSampleLoadingProtocolType>();
        }
        return this.sampleLoadingProtocol;
    }

    /**
     * Gets the value of the gel2DProtocol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gel2DProtocol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGel2DProtocol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GelMLGel2DProtocolGel2DProtocolType }
     * 
     * 
     */
    public List<GelMLGel2DProtocolGel2DProtocolType> getGel2DProtocol() {
        if (gel2DProtocol == null) {
            gel2DProtocol = new ArrayList<GelMLGel2DProtocolGel2DProtocolType>();
        }
        return this.gel2DProtocol;
    }

    /**
     * Gets the value of the gel1DProtocol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gel1DProtocol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGel1DProtocol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GelMLGel1DProtocolGel1DProtocolType }
     * 
     * 
     */
    public List<GelMLGel1DProtocolGel1DProtocolType> getGel1DProtocol() {
        if (gel1DProtocol == null) {
            gel1DProtocol = new ArrayList<GelMLGel1DProtocolGel1DProtocolType>();
        }
        return this.gel1DProtocol;
    }

    /**
     * Gets the value of the otherGelProtocol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the otherGelProtocol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOtherGelProtocol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GelMLOtherGelProtocolOtherGelProtocolType }
     * 
     * 
     */
    public List<GelMLOtherGelProtocolOtherGelProtocolType> getOtherGelProtocol() {
        if (otherGelProtocol == null) {
            otherGelProtocol = new ArrayList<GelMLOtherGelProtocolOtherGelProtocolType>();
        }
        return this.otherGelProtocol;
    }

    /**
     * Gets the value of the electrophoresisProtocol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the electrophoresisProtocol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElectrophoresisProtocol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GelMLElectrophoresisElectrophoresisProtocolType }
     * 
     * 
     */
    public List<GelMLElectrophoresisElectrophoresisProtocolType> getElectrophoresisProtocol() {
        if (electrophoresisProtocol == null) {
            electrophoresisProtocol = new ArrayList<GelMLElectrophoresisElectrophoresisProtocolType>();
        }
        return this.electrophoresisProtocol;
    }

    /**
     * Gets the value of the substanceMixtureProtocol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the substanceMixtureProtocol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubstanceMixtureProtocol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GelMLSelectSubstanceSubstanceMixtureProtocolType }
     * 
     * 
     */
    public List<GelMLSelectSubstanceSubstanceMixtureProtocolType> getSubstanceMixtureProtocol() {
        if (substanceMixtureProtocol == null) {
            substanceMixtureProtocol = new ArrayList<GelMLSelectSubstanceSubstanceMixtureProtocolType>();
        }
        return this.substanceMixtureProtocol;
    }

    /**
     * Gets the value of the detectionProtocol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the detectionProtocol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDetectionProtocol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GelMLDetectionDetectionProtocolType }
     * 
     * 
     */
    public List<GelMLDetectionDetectionProtocolType> getDetectionProtocol() {
        if (detectionProtocol == null) {
            detectionProtocol = new ArrayList<GelMLDetectionDetectionProtocolType>();
        }
        return this.detectionProtocol;
    }

    /**
     * Gets the value of the imageAcquisitionProtocol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the imageAcquisitionProtocol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImageAcquisitionProtocol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GelMLImageAcquisitionImageAcquisitionProtocolType }
     * 
     * 
     */
    public List<GelMLImageAcquisitionImageAcquisitionProtocolType> getImageAcquisitionProtocol() {
        if (imageAcquisitionProtocol == null) {
            imageAcquisitionProtocol = new ArrayList<GelMLImageAcquisitionImageAcquisitionProtocolType>();
        }
        return this.imageAcquisitionProtocol;
    }

}
