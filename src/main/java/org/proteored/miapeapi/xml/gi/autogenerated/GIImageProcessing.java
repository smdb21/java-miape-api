//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-257 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.07.18 at 02:51:39 PM CEST 
//


package org.proteored.miapeapi.xml.gi.autogenerated;

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
 *         &lt;element ref="{}GI_Processing_Input_Image" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="GI_Image_Processing_Software" type="{}MIAPE_Software_Type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="GI_Image_Processing_Step" type="{}GI_Image_Processing_Algorithm_Type" maxOccurs="unbounded" minOccurs="0"/>
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
    "giProcessingInputImage",
    "giImageProcessingSoftware",
    "giImageProcessingStep"
})
@XmlRootElement(name = "GI_Image_Processing")
public class GIImageProcessing {

    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "GI_Processing_Input_Image")
    protected List<GIProcessingInputImage> giProcessingInputImage;
    @XmlElement(name = "GI_Image_Processing_Software")
    protected List<MIAPESoftwareType> giImageProcessingSoftware;
    @XmlElement(name = "GI_Image_Processing_Step")
    protected List<GIImageProcessingAlgorithmType> giImageProcessingStep;

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
     * Gets the value of the giProcessingInputImage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the giProcessingInputImage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGIProcessingInputImage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GIProcessingInputImage }
     * 
     * 
     */
    public List<GIProcessingInputImage> getGIProcessingInputImage() {
        if (giProcessingInputImage == null) {
            giProcessingInputImage = new ArrayList<GIProcessingInputImage>();
        }
        return this.giProcessingInputImage;
    }

    /**
     * Gets the value of the giImageProcessingSoftware property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the giImageProcessingSoftware property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGIImageProcessingSoftware().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MIAPESoftwareType }
     * 
     * 
     */
    public List<MIAPESoftwareType> getGIImageProcessingSoftware() {
        if (giImageProcessingSoftware == null) {
            giImageProcessingSoftware = new ArrayList<MIAPESoftwareType>();
        }
        return this.giImageProcessingSoftware;
    }

    /**
     * Gets the value of the giImageProcessingStep property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the giImageProcessingStep property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGIImageProcessingStep().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GIImageProcessingAlgorithmType }
     * 
     * 
     */
    public List<GIImageProcessingAlgorithmType> getGIImageProcessingStep() {
        if (giImageProcessingStep == null) {
            giImageProcessingStep = new ArrayList<GIImageProcessingAlgorithmType>();
        }
        return this.giImageProcessingStep;
    }

}
