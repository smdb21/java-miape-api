//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.11 at 04:01:56 PM CEST 
//


package org.proteored.miapeapi.xml.msi.autogenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 *  Other classes in the model can be specified as sub-classes, inheriting from Identifiable. Identifiable gives classes a unique identifier within the scope and a name that need not be unique. Identifiable also provides a mechanism for annotating objects with BibliographicReference(s) and DatabaseEntry(s). 
 * 
 * <p>Java class for FuGE.Common.IdentifiableType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FuGE.Common.IdentifiableType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FuGE.Common.IdentifiableType")
@XmlSeeAlso({
    FuGECommonReferencesDatabaseType.class,
    FuGECommonReferencesBibliographicReferenceType.class,
    FuGECollectionProviderType.class,
    FuGEBioConceptualMoleculeConceptualMoleculeType.class,
    FuGECommonProtocolProtocolType.class,
    FuGECommonProtocolParameterType.class,
    FuGEBioDataDataType.class,
    FuGEBioMaterialMaterialType.class,
    FuGECommonAuditContactType.class,
    FuGECommonProtocolActionType.class,
    FuGECommonProtocolParameterizableApplicationType.class,
    FuGECommonProtocolSoftwareType.class,
    FuGECommonProtocolEquipmentType.class,
    FuGECommonProtocolProtocolApplicationType.class
})
public abstract class FuGECommonIdentifiableType {

    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
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

}
