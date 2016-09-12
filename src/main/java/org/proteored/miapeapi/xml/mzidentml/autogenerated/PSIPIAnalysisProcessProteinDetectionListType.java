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
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 *  The protein list resulting from a protein
 * 				detection process. 
 * 
 * <p>Java class for PSI-PI.analysis.process.ProteinDetectionListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PSI-PI.analysis.process.ProteinDetectionListType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://psidev.info/psi/pi/mzIdentML/1.0}FuGE.Bio.Data.InternalDataType">
 *       &lt;sequence>
 *         &lt;group ref="{http://psidev.info/psi/pi/mzIdentML/1.0}ParamGroup" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ProteinAmbiguityGroup" type="{http://psidev.info/psi/pi/mzIdentML/1.0}PSI-PI.analysis.process.ProteinAmbiguityGroupType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PSI-PI.analysis.process.ProteinDetectionListType", propOrder = {
    "paramGroup",
    "proteinAmbiguityGroup"
})
public class PSIPIAnalysisProcessProteinDetectionListType
    extends FuGEBioDataInternalDataType
{

    @XmlElements({
        @XmlElement(name = "userParam", type = FuGECommonOntologyUserParamType.class),
        @XmlElement(name = "cvParam", type = FuGECommonOntologyCvParamType.class)
    })
    protected List<FuGECommonOntologyParamType> paramGroup;
    @XmlElement(name = "ProteinAmbiguityGroup")
    protected List<PSIPIAnalysisProcessProteinAmbiguityGroupType> proteinAmbiguityGroup;

    /**
     * Scores or output parameters associated with
     * 								the ProteinDetectionList Gets the value of the paramGroup property.
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

    /**
     * Gets the value of the proteinAmbiguityGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the proteinAmbiguityGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProteinAmbiguityGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PSIPIAnalysisProcessProteinAmbiguityGroupType }
     * 
     * 
     */
    public List<PSIPIAnalysisProcessProteinAmbiguityGroupType> getProteinAmbiguityGroup() {
        if (proteinAmbiguityGroup == null) {
            proteinAmbiguityGroup = new ArrayList<PSIPIAnalysisProcessProteinAmbiguityGroupType>();
        }
        return this.proteinAmbiguityGroup;
    }

}
