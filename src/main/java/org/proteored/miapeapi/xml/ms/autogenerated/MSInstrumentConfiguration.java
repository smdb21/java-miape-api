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
 *         &lt;element ref="{}MS_ESI" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}MS_MALDI" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}MS_Other_IonSource" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}MS_Analyzer" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}MS_ActivationDissociation" maxOccurs="unbounded" minOccurs="0"/>
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
    "name",
    "msesi",
    "msmaldi",
    "msOtherIonSource",
    "msAnalyzer",
    "msActivationDissociation"
})
@XmlRootElement(name = "MS_Instrument_Configuration")
public class MSInstrumentConfiguration {

    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "MS_ESI")
    protected List<MSESI> msesi;
    @XmlElement(name = "MS_MALDI")
    protected List<MSMALDI> msmaldi;
    @XmlElement(name = "MS_Other_IonSource")
    protected List<MSOtherIonSource> msOtherIonSource;
    @XmlElement(name = "MS_Analyzer")
    protected List<MSAnalyzer> msAnalyzer;
    @XmlElement(name = "MS_ActivationDissociation")
    protected List<MSActivationDissociation> msActivationDissociation;

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
     * Gets the value of the msesi property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the msesi property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMSESI().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MSESI }
     * 
     * 
     */
    public List<MSESI> getMSESI() {
        if (msesi == null) {
            msesi = new ArrayList<MSESI>();
        }
        return this.msesi;
    }

    /**
     * Gets the value of the msmaldi property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the msmaldi property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMSMALDI().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MSMALDI }
     * 
     * 
     */
    public List<MSMALDI> getMSMALDI() {
        if (msmaldi == null) {
            msmaldi = new ArrayList<MSMALDI>();
        }
        return this.msmaldi;
    }

    /**
     * Gets the value of the msOtherIonSource property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the msOtherIonSource property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMSOtherIonSource().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MSOtherIonSource }
     * 
     * 
     */
    public List<MSOtherIonSource> getMSOtherIonSource() {
        if (msOtherIonSource == null) {
            msOtherIonSource = new ArrayList<MSOtherIonSource>();
        }
        return this.msOtherIonSource;
    }

    /**
     * Gets the value of the msAnalyzer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the msAnalyzer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMSAnalyzer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MSAnalyzer }
     * 
     * 
     */
    public List<MSAnalyzer> getMSAnalyzer() {
        if (msAnalyzer == null) {
            msAnalyzer = new ArrayList<MSAnalyzer>();
        }
        return this.msAnalyzer;
    }

    /**
     * Gets the value of the msActivationDissociation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the msActivationDissociation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMSActivationDissociation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MSActivationDissociation }
     * 
     * 
     */
    public List<MSActivationDissociation> getMSActivationDissociation() {
        if (msActivationDissociation == null) {
            msActivationDissociation = new ArrayList<MSActivationDissociation>();
        }
        return this.msActivationDissociation;
    }

}
