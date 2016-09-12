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
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 *  A Protocol representing the creation of a mixture of substances, for example
 *     to be used for specifying the components of buffers, solutions etc. If the actionText attribute
 *     of SubtanceAction is not used, the SubstanceMixtureProtocol represents only the components of
 *     the mixture and not the processes applied to them. 
 * 
 * <p>Java class for GelML.SelectSubstance.SubstanceMixtureProtocolType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GelML.SelectSubstance.SubstanceMixtureProtocolType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.psidev.info/gelml/1_1candidate}FuGE.Common.Protocol.ProtocolType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.psidev.info/gelml/1_1candidate}SubstanceAction" maxOccurs="unbounded"/>
 *         &lt;element name="mixtureType" type="{http://www.psidev.info/gelml/1_1candidate}ParamType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="mixtureName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GelML.SelectSubstance.SubstanceMixtureProtocolType", propOrder = {
    "substanceAction",
    "mixtureType"
})
public class GelMLSelectSubstanceSubstanceMixtureProtocolType
    extends FuGECommonProtocolProtocolType
{

    @XmlElementRef(name = "SubstanceAction", namespace = "http://www.psidev.info/gelml/1_1candidate", type = JAXBElement.class)
    protected List<JAXBElement<? extends GelMLSelectSubstanceSubstanceActionType>> substanceAction;
    protected ParamType mixtureType;
    @XmlAttribute
    protected String mixtureName;

    /**
     * Gets the value of the substanceAction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the substanceAction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubstanceAction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link GelMLSelectSubstanceSubstanceActionType }{@code >}
     * {@link JAXBElement }{@code <}{@link GelMLSelectSubstanceTemporalSubstanceActionType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends GelMLSelectSubstanceSubstanceActionType>> getSubstanceAction() {
        if (substanceAction == null) {
            substanceAction = new ArrayList<JAXBElement<? extends GelMLSelectSubstanceSubstanceActionType>>();
        }
        return this.substanceAction;
    }

    /**
     * Gets the value of the mixtureType property.
     * 
     * @return
     *     possible object is
     *     {@link ParamType }
     *     
     */
    public ParamType getMixtureType() {
        return mixtureType;
    }

    /**
     * Sets the value of the mixtureType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParamType }
     *     
     */
    public void setMixtureType(ParamType value) {
        this.mixtureType = value;
    }

    /**
     * Gets the value of the mixtureName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMixtureName() {
        return mixtureName;
    }

    /**
     * Sets the value of the mixtureName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMixtureName(String value) {
        this.mixtureName = value;
    }

}
