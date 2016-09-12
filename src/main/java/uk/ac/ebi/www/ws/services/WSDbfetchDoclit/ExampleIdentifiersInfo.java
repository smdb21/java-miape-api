/**
 * ExampleIdentifiersInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package uk.ac.ebi.www.ws.services.WSDbfetchDoclit;

public class ExampleIdentifiersInfo  implements java.io.Serializable {
    private java.lang.String[] accessionList;

    private java.lang.String[] entryVersionList;

    private java.lang.String[] idList;

    private java.lang.String[] nameList;

    private java.lang.String[] sequenceVersionList;

    public ExampleIdentifiersInfo() {
    }

    public ExampleIdentifiersInfo(
           java.lang.String[] accessionList,
           java.lang.String[] entryVersionList,
           java.lang.String[] idList,
           java.lang.String[] nameList,
           java.lang.String[] sequenceVersionList) {
           this.accessionList = accessionList;
           this.entryVersionList = entryVersionList;
           this.idList = idList;
           this.nameList = nameList;
           this.sequenceVersionList = sequenceVersionList;
    }


    /**
     * Gets the accessionList value for this ExampleIdentifiersInfo.
     * 
     * @return accessionList
     */
    public java.lang.String[] getAccessionList() {
        return accessionList;
    }


    /**
     * Sets the accessionList value for this ExampleIdentifiersInfo.
     * 
     * @param accessionList
     */
    public void setAccessionList(java.lang.String[] accessionList) {
        this.accessionList = accessionList;
    }


    /**
     * Gets the entryVersionList value for this ExampleIdentifiersInfo.
     * 
     * @return entryVersionList
     */
    public java.lang.String[] getEntryVersionList() {
        return entryVersionList;
    }


    /**
     * Sets the entryVersionList value for this ExampleIdentifiersInfo.
     * 
     * @param entryVersionList
     */
    public void setEntryVersionList(java.lang.String[] entryVersionList) {
        this.entryVersionList = entryVersionList;
    }


    /**
     * Gets the idList value for this ExampleIdentifiersInfo.
     * 
     * @return idList
     */
    public java.lang.String[] getIdList() {
        return idList;
    }


    /**
     * Sets the idList value for this ExampleIdentifiersInfo.
     * 
     * @param idList
     */
    public void setIdList(java.lang.String[] idList) {
        this.idList = idList;
    }


    /**
     * Gets the nameList value for this ExampleIdentifiersInfo.
     * 
     * @return nameList
     */
    public java.lang.String[] getNameList() {
        return nameList;
    }


    /**
     * Sets the nameList value for this ExampleIdentifiersInfo.
     * 
     * @param nameList
     */
    public void setNameList(java.lang.String[] nameList) {
        this.nameList = nameList;
    }


    /**
     * Gets the sequenceVersionList value for this ExampleIdentifiersInfo.
     * 
     * @return sequenceVersionList
     */
    public java.lang.String[] getSequenceVersionList() {
        return sequenceVersionList;
    }


    /**
     * Sets the sequenceVersionList value for this ExampleIdentifiersInfo.
     * 
     * @param sequenceVersionList
     */
    public void setSequenceVersionList(java.lang.String[] sequenceVersionList) {
        this.sequenceVersionList = sequenceVersionList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExampleIdentifiersInfo)) return false;
        ExampleIdentifiersInfo other = (ExampleIdentifiersInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.accessionList==null && other.getAccessionList()==null) || 
             (this.accessionList!=null &&
              java.util.Arrays.equals(this.accessionList, other.getAccessionList()))) &&
            ((this.entryVersionList==null && other.getEntryVersionList()==null) || 
             (this.entryVersionList!=null &&
              java.util.Arrays.equals(this.entryVersionList, other.getEntryVersionList()))) &&
            ((this.idList==null && other.getIdList()==null) || 
             (this.idList!=null &&
              java.util.Arrays.equals(this.idList, other.getIdList()))) &&
            ((this.nameList==null && other.getNameList()==null) || 
             (this.nameList!=null &&
              java.util.Arrays.equals(this.nameList, other.getNameList()))) &&
            ((this.sequenceVersionList==null && other.getSequenceVersionList()==null) || 
             (this.sequenceVersionList!=null &&
              java.util.Arrays.equals(this.sequenceVersionList, other.getSequenceVersionList())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getAccessionList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAccessionList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAccessionList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getEntryVersionList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEntryVersionList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEntryVersionList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIdList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNameList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNameList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getNameList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSequenceVersionList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSequenceVersionList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSequenceVersionList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExampleIdentifiersInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "ExampleIdentifiersInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accessionList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "accessionList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "accession"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entryVersionList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "entryVersionList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "entryVersion"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "idList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "id"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nameList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "nameList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "name"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sequenceVersionList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "sequenceVersionList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.ebi.ac.uk/ws/services/WSDbfetchDoclit", "sequenceVersion"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
