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
 *  Gel1DExperiment captures the ProtocolApplications that should be reported for
 *     1D gel electrophoresis in a GelML file. 
 * 
 * <p>Java class for GelML.GelMLRoot.Gel1DExperimentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GelML.GelMLRoot.Gel1DExperimentType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.psidev.info/gelml/1_1candidate}GelML.GelMLRoot.GelExperimentType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}GelManufactureApplication" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}Gel1DApplication"/>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}ImageAcquisitionApplication" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}ExcisionApplication" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GelML.GelMLRoot.Gel1DExperimentType", propOrder = {
    "gelManufactureApplication",
    "gel1DApplication",
    "imageAcquisitionApplication",
    "excisionApplication"
})
public class GelMLGelMLRootGel1DExperimentType
    extends GelMLGelMLRootGelExperimentType
{

    @XmlElement(name = "GelManufactureApplication")
    protected List<GelMLGelGelManufactureApplicationType> gelManufactureApplication;
    @XmlElement(name = "Gel1DApplication", required = true)
    protected GelMLGel1DProtocolGel1DApplicationType gel1DApplication;
    @XmlElement(name = "ImageAcquisitionApplication")
    protected List<GelMLImageAcquisitionImageAcquisitionApplicationType> imageAcquisitionApplication;
    @XmlElement(name = "ExcisionApplication")
    protected List<GelMLExcisionExcisionApplicationType> excisionApplication;

    /**
     * Gets the value of the gelManufactureApplication property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gelManufactureApplication property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGelManufactureApplication().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GelMLGelGelManufactureApplicationType }
     * 
     * 
     */
    public List<GelMLGelGelManufactureApplicationType> getGelManufactureApplication() {
        if (gelManufactureApplication == null) {
            gelManufactureApplication = new ArrayList<GelMLGelGelManufactureApplicationType>();
        }
        return this.gelManufactureApplication;
    }

    /**
     * Gets the value of the gel1DApplication property.
     * 
     * @return
     *     possible object is
     *     {@link GelMLGel1DProtocolGel1DApplicationType }
     *     
     */
    public GelMLGel1DProtocolGel1DApplicationType getGel1DApplication() {
        return gel1DApplication;
    }

    /**
     * Sets the value of the gel1DApplication property.
     * 
     * @param value
     *     allowed object is
     *     {@link GelMLGel1DProtocolGel1DApplicationType }
     *     
     */
    public void setGel1DApplication(GelMLGel1DProtocolGel1DApplicationType value) {
        this.gel1DApplication = value;
    }

    /**
     * Gets the value of the imageAcquisitionApplication property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the imageAcquisitionApplication property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImageAcquisitionApplication().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GelMLImageAcquisitionImageAcquisitionApplicationType }
     * 
     * 
     */
    public List<GelMLImageAcquisitionImageAcquisitionApplicationType> getImageAcquisitionApplication() {
        if (imageAcquisitionApplication == null) {
            imageAcquisitionApplication = new ArrayList<GelMLImageAcquisitionImageAcquisitionApplicationType>();
        }
        return this.imageAcquisitionApplication;
    }

    /**
     * Gets the value of the excisionApplication property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the excisionApplication property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExcisionApplication().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GelMLExcisionExcisionApplicationType }
     * 
     * 
     */
    public List<GelMLExcisionExcisionApplicationType> getExcisionApplication() {
        if (excisionApplication == null) {
            excisionApplication = new ArrayList<GelMLExcisionExcisionApplicationType>();
        }
        return this.excisionApplication;
    }

}
