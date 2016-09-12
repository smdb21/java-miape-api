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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 *  The root of the GelML document. 
 * 
 * <p>Java class for GelML.GelMLRoot.GelMLType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GelML.GelMLRoot.GelMLType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.psidev.info/gelml/1_1candidate}FuGE.Common.IdentifiableType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}Provider" minOccurs="0"/>
 *         &lt;element name="cvList">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}cv" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}GelMLMaterialCollection" minOccurs="0"/>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}GelMLDataCollection" minOccurs="0"/>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}GelMLProtocolCollection" minOccurs="0"/>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}GelExperiment" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}AuditCollection" minOccurs="0"/>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}ReferenceableCollection" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GelML.GelMLRoot.GelMLType", propOrder = {
    "provider",
    "cvList",
    "gelMLMaterialCollection",
    "gelMLDataCollection",
    "gelMLProtocolCollection",
    "gelExperiment",
    "auditCollection",
    "referenceableCollection"
})
public class GelMLGelMLRootGelMLType
    extends FuGECommonIdentifiableType
{

    @XmlElement(name = "Provider")
    protected FuGECollectionProviderType provider;
    @XmlElement(required = true)
    protected GelMLGelMLRootGelMLType.CvList cvList;
    @XmlElement(name = "GelMLMaterialCollection")
    protected GelMLGelMLRootGelMLMaterialCollectionType gelMLMaterialCollection;
    @XmlElement(name = "GelMLDataCollection")
    protected GelMLGelMLRootGelMLDataCollectionType gelMLDataCollection;
    @XmlElement(name = "GelMLProtocolCollection")
    protected GelMLGelMLRootGelMLProtocolCollectionType gelMLProtocolCollection;
    @XmlElementRef(name = "GelExperiment", namespace = "http://www.psidev.info/gelml/1_1candidate", type = JAXBElement.class)
    protected List<JAXBElement<? extends GelMLGelMLRootGelExperimentType>> gelExperiment;
    @XmlElement(name = "AuditCollection")
    protected FuGECollectionAuditCollectionType auditCollection;
    @XmlElement(name = "ReferenceableCollection")
    protected FuGECollectionReferenceableCollectionType referenceableCollection;

    /**
     * Gets the value of the provider property.
     * 
     * @return
     *     possible object is
     *     {@link FuGECollectionProviderType }
     *     
     */
    public FuGECollectionProviderType getProvider() {
        return provider;
    }

    /**
     * Sets the value of the provider property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuGECollectionProviderType }
     *     
     */
    public void setProvider(FuGECollectionProviderType value) {
        this.provider = value;
    }

    /**
     * Gets the value of the cvList property.
     * 
     * @return
     *     possible object is
     *     {@link GelMLGelMLRootGelMLType.CvList }
     *     
     */
    public GelMLGelMLRootGelMLType.CvList getCvList() {
        return cvList;
    }

    /**
     * Sets the value of the cvList property.
     * 
     * @param value
     *     allowed object is
     *     {@link GelMLGelMLRootGelMLType.CvList }
     *     
     */
    public void setCvList(GelMLGelMLRootGelMLType.CvList value) {
        this.cvList = value;
    }

    /**
     * Gets the value of the gelMLMaterialCollection property.
     * 
     * @return
     *     possible object is
     *     {@link GelMLGelMLRootGelMLMaterialCollectionType }
     *     
     */
    public GelMLGelMLRootGelMLMaterialCollectionType getGelMLMaterialCollection() {
        return gelMLMaterialCollection;
    }

    /**
     * Sets the value of the gelMLMaterialCollection property.
     * 
     * @param value
     *     allowed object is
     *     {@link GelMLGelMLRootGelMLMaterialCollectionType }
     *     
     */
    public void setGelMLMaterialCollection(GelMLGelMLRootGelMLMaterialCollectionType value) {
        this.gelMLMaterialCollection = value;
    }

    /**
     * Gets the value of the gelMLDataCollection property.
     * 
     * @return
     *     possible object is
     *     {@link GelMLGelMLRootGelMLDataCollectionType }
     *     
     */
    public GelMLGelMLRootGelMLDataCollectionType getGelMLDataCollection() {
        return gelMLDataCollection;
    }

    /**
     * Sets the value of the gelMLDataCollection property.
     * 
     * @param value
     *     allowed object is
     *     {@link GelMLGelMLRootGelMLDataCollectionType }
     *     
     */
    public void setGelMLDataCollection(GelMLGelMLRootGelMLDataCollectionType value) {
        this.gelMLDataCollection = value;
    }

    /**
     * Gets the value of the gelMLProtocolCollection property.
     * 
     * @return
     *     possible object is
     *     {@link GelMLGelMLRootGelMLProtocolCollectionType }
     *     
     */
    public GelMLGelMLRootGelMLProtocolCollectionType getGelMLProtocolCollection() {
        return gelMLProtocolCollection;
    }

    /**
     * Sets the value of the gelMLProtocolCollection property.
     * 
     * @param value
     *     allowed object is
     *     {@link GelMLGelMLRootGelMLProtocolCollectionType }
     *     
     */
    public void setGelMLProtocolCollection(GelMLGelMLRootGelMLProtocolCollectionType value) {
        this.gelMLProtocolCollection = value;
    }

    /**
     * Gets the value of the gelExperiment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gelExperiment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGelExperiment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link GelMLGelMLRootOtherGelExperimentType }{@code >}
     * {@link JAXBElement }{@code <}{@link GelMLGelMLRootGelExperimentType }{@code >}
     * {@link JAXBElement }{@code <}{@link GelMLGelMLRootGel1DExperimentType }{@code >}
     * {@link JAXBElement }{@code <}{@link GelMLGelMLRootGel2DExperimentType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends GelMLGelMLRootGelExperimentType>> getGelExperiment() {
        if (gelExperiment == null) {
            gelExperiment = new ArrayList<JAXBElement<? extends GelMLGelMLRootGelExperimentType>>();
        }
        return this.gelExperiment;
    }

    /**
     * Gets the value of the auditCollection property.
     * 
     * @return
     *     possible object is
     *     {@link FuGECollectionAuditCollectionType }
     *     
     */
    public FuGECollectionAuditCollectionType getAuditCollection() {
        return auditCollection;
    }

    /**
     * Sets the value of the auditCollection property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuGECollectionAuditCollectionType }
     *     
     */
    public void setAuditCollection(FuGECollectionAuditCollectionType value) {
        this.auditCollection = value;
    }

    /**
     * Gets the value of the referenceableCollection property.
     * 
     * @return
     *     possible object is
     *     {@link FuGECollectionReferenceableCollectionType }
     *     
     */
    public FuGECollectionReferenceableCollectionType getReferenceableCollection() {
        return referenceableCollection;
    }

    /**
     * Sets the value of the referenceableCollection property.
     * 
     * @param value
     *     allowed object is
     *     {@link FuGECollectionReferenceableCollectionType }
     *     
     */
    public void setReferenceableCollection(FuGECollectionReferenceableCollectionType value) {
        this.referenceableCollection = value;
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
     *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}cv" maxOccurs="unbounded"/>
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
        "cv"
    })
    public static class CvList {

        @XmlElement(required = true)
        protected List<FuGECommonOntologyCvType> cv;

        /**
         * Gets the value of the cv property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the cv property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCv().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link FuGECommonOntologyCvType }
         * 
         * 
         */
        public List<FuGECommonOntologyCvType> getCv() {
            if (cv == null) {
                cv = new ArrayList<FuGECommonOntologyCvType>();
            }
            return this.cv;
        }

    }

}
