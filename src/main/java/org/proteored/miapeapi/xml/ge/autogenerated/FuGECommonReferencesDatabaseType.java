//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-257 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.07.18 at 02:51:38 PM CEST 
//


package org.proteored.miapeapi.xml.ge.autogenerated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 *  An address to a repository. 
 * 
 * <p>Java class for FuGE.Common.References.DatabaseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FuGE.Common.References.DatabaseType">
 *   &lt;complexContent>
 *     &lt;extension base="{}FuGE.Common.IdentifiableType">
 *       &lt;sequence>
 *         &lt;element ref="{}ContactRole" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="URI" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FuGE.Common.References.DatabaseType", propOrder = {
    "contactRole"
})
public class FuGECommonReferencesDatabaseType
    extends FuGECommonIdentifiableType
{

    @XmlElement(name = "ContactRole")
    protected List<FuGECommonAuditContactRoleType> contactRole;
    @XmlAttribute
    protected String version;
    @XmlAttribute(name = "URI")
    @XmlSchemaType(name = "anyURI")
    protected String uri;

    /**
     *  Contacts associated with this database, such as hosting institution, database curators, manager etc. Gets the value of the contactRole property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contactRole property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContactRole().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FuGECommonAuditContactRoleType }
     * 
     * 
     */
    public List<FuGECommonAuditContactRoleType> getContactRole() {
        if (contactRole == null) {
            contactRole = new ArrayList<FuGECommonAuditContactRoleType>();
        }
        return this.contactRole;
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
     * Gets the value of the uri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getURI() {
        return uri;
    }

    /**
     * Sets the value of the uri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setURI(String value) {
        this.uri = value;
    }

}
