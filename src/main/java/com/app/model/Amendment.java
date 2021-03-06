//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.04 at 12:37:11 AM CEST 
//


package com.app.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.app.model.Ref;


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
 *         &lt;element name="RefAkta" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Lista_predloga">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Predlog_amandmana" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element ref="{http://www.parlament.gov.rs/akti}ref"/>
 *                             &lt;element name="sadrzaj" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element ref="{http://www.parlament.gov.rs/akti}ref" maxOccurs="unbounded" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element ref="{http://www.parlament.gov.rs/amandmani}Cvor" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Obrazlozenje">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.parlament.gov.rs/akti}ref" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="tip" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="status" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="predlagac" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="naziv" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "refAkta",
    "listaPredloga",
    "obrazlozenje"
})
@XmlRootElement(name = "Amandman")
public class Amendment {

    @XmlElement(name = "RefAkta", required = true)
    protected String refAkta;
    @XmlElement(name = "Lista_predloga", required = true)
    protected Amendment.ListaPredloga listaPredloga;
    @XmlElement(name = "Obrazlozenje", required = true)
    protected Amendment.Obrazlozenje obrazlozenje;
    @XmlAttribute(name = "tip", required = true)
    protected String tip;
    @XmlAttribute(name = "status", required = true)
    protected String status;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "predlagac", required = true)
    protected String predlagac;
    @XmlAttribute(name = "naziv")
    protected String naziv;

    /**
     * Gets the value of the refAkta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefAkta() {
        return refAkta;
    }

    /**
     * Sets the value of the refAkta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefAkta(String value) {
        this.refAkta = value;
    }

    /**
     * Gets the value of the listaPredloga property.
     * 
     * @return
     *     possible object is
     *     {@link Amendment.ListaPredloga }
     *     
     */
    public Amendment.ListaPredloga getListaPredloga() {
        return listaPredloga;
    }

    /**
     * Sets the value of the listaPredloga property.
     * 
     * @param value
     *     allowed object is
     *     {@link Amendment.ListaPredloga }
     *     
     */
    public void setListaPredloga(Amendment.ListaPredloga value) {
        this.listaPredloga = value;
    }

    /**
     * Gets the value of the obrazlozenje property.
     * 
     * @return
     *     possible object is
     *     {@link Amendment.Obrazlozenje }
     *     
     */
    public Amendment.Obrazlozenje getObrazlozenje() {
        return obrazlozenje;
    }

    /**
     * Sets the value of the obrazlozenje property.
     * 
     * @param value
     *     allowed object is
     *     {@link Amendment.Obrazlozenje }
     *     
     */
    public void setObrazlozenje(Amendment.Obrazlozenje value) {
        this.obrazlozenje = value;
    }

    /**
     * Gets the value of the tip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTip() {
        return tip;
    }

    /**
     * Sets the value of the tip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTip(String value) {
        this.tip = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

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
     * Gets the value of the predlagac property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPredlagac() {
        return predlagac;
    }

    /**
     * Sets the value of the predlagac property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPredlagac(String value) {
        this.predlagac = value;
    }

    /**
     * Gets the value of the naziv property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNaziv() {
        return naziv;
    }

    /**
     * Sets the value of the naziv property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNaziv(String value) {
        this.naziv = value;
    }


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
     *         &lt;element name="Predlog_amandmana" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element ref="{http://www.parlament.gov.rs/akti}ref"/>
     *                   &lt;element name="sadrzaj" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element ref="{http://www.parlament.gov.rs/akti}ref" maxOccurs="unbounded" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element ref="{http://www.parlament.gov.rs/amandmani}Cvor" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "predlogAmandmana"
    })
    public static class ListaPredloga {

        @XmlElement(name = "Predlog_amandmana", required = true)
        protected List<Amendment.ListaPredloga.PredlogAmandmana> predlogAmandmana;

        /**
         * Gets the value of the predlogAmandmana property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the predlogAmandmana property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPredlogAmandmana().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Amendment.ListaPredloga.PredlogAmandmana }
         * 
         * 
         */
        public List<Amendment.ListaPredloga.PredlogAmandmana> getPredlogAmandmana() {
            if (predlogAmandmana == null) {
                predlogAmandmana = new ArrayList<Amendment.ListaPredloga.PredlogAmandmana>();
            }
            return this.predlogAmandmana;
        }
        
        public void setPredlogAmandmana(List<Amendment.ListaPredloga.PredlogAmandmana> predlog){
        	predlogAmandmana=predlog;
        }


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
         *         &lt;element ref="{http://www.parlament.gov.rs/akti}ref"/>
         *         &lt;element name="sadrzaj" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element ref="{http://www.parlament.gov.rs/akti}ref" maxOccurs="unbounded" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element ref="{http://www.parlament.gov.rs/amandmani}Cvor" minOccurs="0"/>
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
            "ref",
            "sadrzaj",
            "cvor"
        })
        public static class PredlogAmandmana {

            @XmlElement(namespace = "http://www.parlament.gov.rs/akti", required = true)
            protected Ref ref;
            protected Amendment.ListaPredloga.PredlogAmandmana.Sadrzaj sadrzaj;
            @XmlElement(name = "Cvor")
            protected Cvor cvor;

            /**
             * Gets the value of the ref property.
             * 
             * @return
             *     possible object is
             *     {@link Ref }
             *     
             */
            public Ref getRef() {
                return ref;
            }

            /**
             * Sets the value of the ref property.
             * 
             * @param value
             *     allowed object is
             *     {@link Ref }
             *     
             */
            public void setRef(Ref value) {
                this.ref = value;
            }

            /**
             * Gets the value of the sadrzaj property.
             * 
             * @return
             *     possible object is
             *     {@link Amendment.ListaPredloga.PredlogAmandmana.Sadrzaj }
             *     
             */
            public Amendment.ListaPredloga.PredlogAmandmana.Sadrzaj getSadrzaj() {
                return sadrzaj;
            }

            /**
             * Sets the value of the sadrzaj property.
             * 
             * @param value
             *     allowed object is
             *     {@link Amendment.ListaPredloga.PredlogAmandmana.Sadrzaj }
             *     
             */
            public void setSadrzaj(Amendment.ListaPredloga.PredlogAmandmana.Sadrzaj value) {
                this.sadrzaj = value;
            }

            /**
             * Gets the value of the cvor property.
             * 
             * @return
             *     possible object is
             *     {@link Cvor }
             *     
             */
            public Cvor getCvor() {
                return cvor;
            }

            /**
             * Sets the value of the cvor property.
             * 
             * @param value
             *     allowed object is
             *     {@link Cvor }
             *     
             */
            public void setCvor(Cvor value) {
                this.cvor = value;
            }


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
             *         &lt;element ref="{http://www.parlament.gov.rs/akti}ref" maxOccurs="unbounded" minOccurs="0"/>
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
                "content"
            })
            public static class Sadrzaj {

                @XmlElementRef(name = "ref", namespace = "http://www.parlament.gov.rs/akti", type = Ref.class, required = false)
                @XmlMixed
                protected List<Object> content;

                /**
                 * Gets the value of the content property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the content property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getContent().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Ref }
                 * {@link String }
                 * 
                 * 
                 */
                public List<Object> getContent() {
                    if (content == null) {
                        content = new ArrayList<Object>();
                    }
                    return this.content;
                }
                
                public void setContent(List<Object> content){
                	this.content=content;
                }

            }

        }

    }


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
     *         &lt;element ref="{http://www.parlament.gov.rs/akti}ref" maxOccurs="unbounded" minOccurs="0"/>
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
        "content"
    })
    public static class Obrazlozenje {

        @XmlElementRef(name = "ref", namespace = "http://www.parlament.gov.rs/akti", type = Ref.class, required = false)
        @XmlMixed
        protected List<Object> content;

        /**
         * Gets the value of the content property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the content property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getContent().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Ref }
         * {@link String }
         * 
         * 
         */
        public List<Object> getContent() {
            if (content == null) {
                content = new ArrayList<Object>();
            }
            return this.content;
        }
        
        public void setContent(List<Object> content){
        	this.content=content;
        }

    }

}
