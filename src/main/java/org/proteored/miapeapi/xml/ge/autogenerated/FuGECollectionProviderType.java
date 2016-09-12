//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-257 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.07.18 at 02:51:38 PM CEST 
//


package org.proteored.miapeapi.xml.ge.autogenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 *  The provider of the document in terms of the Contact and the software the produced the document instance. 
 * 
 * <p>Java class for FuGE.Collection.ProviderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FuGE.Collection.ProviderType">
 *   &lt;complexContent>
 *     &lt;extension base="{}FuGE.Common.IdentifiableType">
 *       &lt;sequence>
 *         &lt;element ref="{}ContactRole" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Software_ref" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FuGE.Collection.ProviderType", propOrder = {
    "contactRole"
})
public class FuGECollectionProviderType
    extends FuGECommonIdentifiableType
{

    @XmlElement(name = "ContactRole")
    protected FuGECommonAuditContactRoleType contactRole;
    @XmlAttribute(name = "Software_ref")
    protected String softwareRef;

    /**
     *  The Contact that provided the document instance. 
     * 
     * @return
     *     possible object is
     *     {@link FuGECommonAuditContactRoleType }
     *     
     */
    public FuGECommonAuditContactRoleType getContactRole() {
        return contactRole;
    }

    /**
     *  The Contact that provided the document instance. 
     * 
     * @param value
     *     allowed object is
     *     {@link FuGECommonAuditContactRoleType }
     *     
     */
    public void setContactRole(FuGECommonAuditContactRoleType value) {
        this.contactRole = value;
    }

    /**
     * Gets the value of the softwareRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSoftwareRef() {
        return softwareRef;
    }

    /**
     * Sets the value of the softwareRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSoftwareRef(String value) {
        this.softwareRef = value;
    }

}
