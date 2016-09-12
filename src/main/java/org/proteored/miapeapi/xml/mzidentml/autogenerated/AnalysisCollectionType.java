//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-257 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.09.14 at 07:25:56 PM CEST 
//


package org.proteored.miapeapi.xml.mzidentml.autogenerated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AnalysisCollectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AnalysisCollectionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SpectrumIdentification" type="{http://psidev.info/psi/pi/mzIdentML/1.0}PSI-PI.analysis.search.SpectrumIdentificationType" maxOccurs="unbounded"/>
 *         &lt;element name="ProteinDetection" type="{http://psidev.info/psi/pi/mzIdentML/1.0}PSI-PI.analysis.process.ProteinDetectionType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnalysisCollectionType", propOrder = {
    "spectrumIdentification",
    "proteinDetection"
})
public class AnalysisCollectionType {

    @XmlElement(name = "SpectrumIdentification", required = true)
    protected List<PSIPIAnalysisSearchSpectrumIdentificationType> spectrumIdentification;
    @XmlElement(name = "ProteinDetection")
    protected PSIPIAnalysisProcessProteinDetectionType proteinDetection;

    /**
     * Gets the value of the spectrumIdentification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the spectrumIdentification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpectrumIdentification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PSIPIAnalysisSearchSpectrumIdentificationType }
     * 
     * 
     */
    public List<PSIPIAnalysisSearchSpectrumIdentificationType> getSpectrumIdentification() {
        if (spectrumIdentification == null) {
            spectrumIdentification = new ArrayList<PSIPIAnalysisSearchSpectrumIdentificationType>();
        }
        return this.spectrumIdentification;
    }

    /**
     * Gets the value of the proteinDetection property.
     * 
     * @return
     *     possible object is
     *     {@link PSIPIAnalysisProcessProteinDetectionType }
     *     
     */
    public PSIPIAnalysisProcessProteinDetectionType getProteinDetection() {
        return proteinDetection;
    }

    /**
     * Sets the value of the proteinDetection property.
     * 
     * @param value
     *     allowed object is
     *     {@link PSIPIAnalysisProcessProteinDetectionType }
     *     
     */
    public void setProteinDetection(PSIPIAnalysisProcessProteinDetectionType value) {
        this.proteinDetection = value;
    }

}
