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
 *  A complex default value for the Parameter, such as a term from a controlled list or a function. 
 * 
 * <p>Java class for FuGE.Common.Measurement.ComplexValueType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FuGE.Common.Measurement.ComplexValueType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://psidev.info/psi/pi/mzIdentML/1.0}FuGE.Common.Measurement.MeasurementType">
 *       &lt;sequence>
 *         &lt;group ref="{http://psidev.info/psi/pi/mzIdentML/1.0}ParamGroup" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FuGE.Common.Measurement.ComplexValueType", propOrder = {
    "paramGroup"
})
public class FuGECommonMeasurementComplexValueType
    extends FuGECommonMeasurementMeasurementType
{

    @XmlElements({
        @XmlElement(name = "userParam", type = FuGECommonOntologyUserParamType.class),
        @XmlElement(name = "cvParam", type = FuGECommonOntologyCvParamType.class)
    })
    protected List<FuGECommonOntologyParamType> paramGroup;

    /**
     * Gets the value of the paramGroup property.
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
