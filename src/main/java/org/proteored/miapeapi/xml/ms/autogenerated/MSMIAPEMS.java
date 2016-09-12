//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.06.19 at 01:51:42 PM CEST 
//


package org.proteored.miapeapi.xml.ms.autogenerated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element ref="{}MIAPEProject"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Modification_Date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="Resolution" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Accuracy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AttachedFileLocation" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="Template" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="MS_Contact" type="{}MIAPE_Contact_Type" minOccurs="0"/>
 *         &lt;element ref="{}MS_Spectrometer" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}MS_Instrument_Configuration" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}MS_Acquisition" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}MS_DataAnalysis" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}MS_Resulting_Data" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="MS_Additional_Information" type="{}ParamType" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "", propOrder = {
    "miapeProject",
    "name",
    "version",
    "date",
    "modificationDate",
    "resolution",
    "accuracy",
    "attachedFileLocation",
    "template",
    "msContact",
    "msSpectrometer",
    "msInstrumentConfiguration",
    "msAcquisition",
    "msDataAnalysis",
    "msResultingData",
    "msAdditionalInformation"
})
@XmlRootElement(name = "MS_MIAPE_MS")
public class MSMIAPEMS {

    @XmlElement(name = "MIAPEProject", required = true)
    protected MIAPEProject miapeProject;
    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "Version")
    protected String version;
    @XmlElement(name = "Date")
    protected String date;
    @XmlElement(name = "Modification_Date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar modificationDate;
    @XmlElement(name = "Resolution")
    protected String resolution;
    @XmlElement(name = "Accuracy")
    protected String accuracy;
    @XmlElement(name = "AttachedFileLocation")
    @XmlSchemaType(name = "anyURI")
    protected String attachedFileLocation;
    @XmlElement(name = "Template")
    protected Boolean template;
    @XmlElement(name = "MS_Contact")
    protected MIAPEContactType msContact;
    @XmlElement(name = "MS_Spectrometer")
    protected List<MSSpectrometer> msSpectrometer;
    @XmlElement(name = "MS_Instrument_Configuration")
    protected List<MSInstrumentConfiguration> msInstrumentConfiguration;
    @XmlElement(name = "MS_Acquisition")
    protected List<MSAcquisition> msAcquisition;
    @XmlElement(name = "MS_DataAnalysis")
    protected List<MSDataAnalysis> msDataAnalysis;
    @XmlElement(name = "MS_Resulting_Data")
    protected List<MSResultingData> msResultingData;
    @XmlElement(name = "MS_Additional_Information")
    protected List<ParamType> msAdditionalInformation;
    @XmlAttribute(name = "id", required = true)
    protected int id;

    /**
     * Gets the value of the miapeProject property.
     * 
     * @return
     *     possible object is
     *     {@link MIAPEProject }
     *     
     */
    public MIAPEProject getMIAPEProject() {
        return miapeProject;
    }

    /**
     * Sets the value of the miapeProject property.
     * 
     * @param value
     *     allowed object is
     *     {@link MIAPEProject }
     *     
     */
    public void setMIAPEProject(MIAPEProject value) {
        this.miapeProject = value;
    }

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
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDate(String value) {
        this.date = value;
    }

    /**
     * Gets the value of the modificationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getModificationDate() {
        return modificationDate;
    }

    /**
     * Sets the value of the modificationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setModificationDate(XMLGregorianCalendar value) {
        this.modificationDate = value;
    }

    /**
     * Gets the value of the resolution property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResolution() {
        return resolution;
    }

    /**
     * Sets the value of the resolution property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResolution(String value) {
        this.resolution = value;
    }

    /**
     * Gets the value of the accuracy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccuracy() {
        return accuracy;
    }

    /**
     * Sets the value of the accuracy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccuracy(String value) {
        this.accuracy = value;
    }

    /**
     * Gets the value of the attachedFileLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttachedFileLocation() {
        return attachedFileLocation;
    }

    /**
     * Sets the value of the attachedFileLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttachedFileLocation(String value) {
        this.attachedFileLocation = value;
    }

    /**
     * Gets the value of the template property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTemplate() {
        return template;
    }

    /**
     * Sets the value of the template property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTemplate(Boolean value) {
        this.template = value;
    }

    /**
     * Gets the value of the msContact property.
     * 
     * @return
     *     possible object is
     *     {@link MIAPEContactType }
     *     
     */
    public MIAPEContactType getMSContact() {
        return msContact;
    }

    /**
     * Sets the value of the msContact property.
     * 
     * @param value
     *     allowed object is
     *     {@link MIAPEContactType }
     *     
     */
    public void setMSContact(MIAPEContactType value) {
        this.msContact = value;
    }

    /**
     * Gets the value of the msSpectrometer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the msSpectrometer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMSSpectrometer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MSSpectrometer }
     * 
     * 
     */
    public List<MSSpectrometer> getMSSpectrometer() {
        if (msSpectrometer == null) {
            msSpectrometer = new ArrayList<MSSpectrometer>();
        }
        return this.msSpectrometer;
    }

    /**
     * Gets the value of the msInstrumentConfiguration property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the msInstrumentConfiguration property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMSInstrumentConfiguration().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MSInstrumentConfiguration }
     * 
     * 
     */
    public List<MSInstrumentConfiguration> getMSInstrumentConfiguration() {
        if (msInstrumentConfiguration == null) {
            msInstrumentConfiguration = new ArrayList<MSInstrumentConfiguration>();
        }
        return this.msInstrumentConfiguration;
    }

    /**
     * Gets the value of the msAcquisition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the msAcquisition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMSAcquisition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MSAcquisition }
     * 
     * 
     */
    public List<MSAcquisition> getMSAcquisition() {
        if (msAcquisition == null) {
            msAcquisition = new ArrayList<MSAcquisition>();
        }
        return this.msAcquisition;
    }

    /**
     * Gets the value of the msDataAnalysis property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the msDataAnalysis property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMSDataAnalysis().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MSDataAnalysis }
     * 
     * 
     */
    public List<MSDataAnalysis> getMSDataAnalysis() {
        if (msDataAnalysis == null) {
            msDataAnalysis = new ArrayList<MSDataAnalysis>();
        }
        return this.msDataAnalysis;
    }

    /**
     * Gets the value of the msResultingData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the msResultingData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMSResultingData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MSResultingData }
     * 
     * 
     */
    public List<MSResultingData> getMSResultingData() {
        if (msResultingData == null) {
            msResultingData = new ArrayList<MSResultingData>();
        }
        return this.msResultingData;
    }

    /**
     * Gets the value of the msAdditionalInformation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the msAdditionalInformation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMSAdditionalInformation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParamType }
     * 
     * 
     */
    public List<ParamType> getMSAdditionalInformation() {
        if (msAdditionalInformation == null) {
            msAdditionalInformation = new ArrayList<ParamType>();
        }
        return this.msAdditionalInformation;
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
