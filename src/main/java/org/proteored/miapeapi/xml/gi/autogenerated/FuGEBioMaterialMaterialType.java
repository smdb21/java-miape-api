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
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 *  Material represents any kind of substance used in an experimental workflow, such as whole organisms, cells, DNA, solutions, compounds and experimental substances (gels, arrays etc.). The Material class can be extended by adding subclasses to model domain specific properties, or the relationships to OntologyIndividual can be used to describe the characteristics and type of Material.   Materials can be related to other materials through a directed acyclic graph (represented by ProtocolApplication(s)). Sub-component materials can be represented by the self-association on Material (e.g. Wells within a array). These associations are abstract and should be extended to represent these semantics for extensions of ProtocolApplication and Material. 
 * 
 * <p>Java class for FuGE.Bio.Material.MaterialType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FuGE.Bio.Material.MaterialType">
 *   &lt;complexContent>
 *     &lt;extension base="{}FuGE.Common.IdentifiableType">
 *       &lt;sequence>
 *         &lt;element ref="{}ContactRole" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;group ref="{}ParamGroup" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FuGE.Bio.Material.MaterialType", propOrder = {
    "contactRole",
    "paramGroup"
})
@XmlSeeAlso({
    FuGEBioMaterialGenericMaterialType.class
})
public abstract class FuGEBioMaterialMaterialType
    extends FuGECommonIdentifiableType
{

    @XmlElement(name = "ContactRole")
    protected List<FuGECommonAuditContactRoleType> contactRole;
    @XmlElements({
        @XmlElement(name = "userParam", type = FuGECommonOntologyUserParamType.class),
        @XmlElement(name = "cvParam", type = FuGECommonOntologyCvParamType.class)
    })
    protected List<FuGECommonOntologyParamType> paramGroup;

    /**
     *  Contact details for the Material. The association to ContactRole could specify, for example, the creator or provider of the Material. Gets the value of the contactRole property.
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
     *  The characteristics of a Material. Gets the value of the paramGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the paramGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParamGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FuGECommonOntologyUserParamType }
     * {@link FuGECommonOntologyCvParamType }
     * 
     * 
     */
    public List<FuGECommonOntologyParamType> getParamGroup() {
        if (paramGroup == null) {
            paramGroup = new ArrayList<FuGECommonOntologyParamType>();
        }
        return this.paramGroup;
    }

}
