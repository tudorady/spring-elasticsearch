package com.simfony.mobility.domain;


import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Subscriber.
 */
@Entity
@Table(name = "subscriber")
@Document(indexName = "subscriber")
public class Subscriber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUBSCRIBER_ID")
    private Long id;

    @NotNull
    @Size(min = 15, max = 15)
    @Column(name = "IMSI", length = 15, nullable = false)
    private String imsi;

    @NotNull
    @Column(name = "MSISDN", nullable = false)
    private String msisdn;

    @NotNull
    @Min(value = 0)
    @Max(value = 2)
    @Column(name = "NAM", nullable = false)
    private Integer nam;

    @NotNull
    @Min(value = 1)
    @Max(value = 1)
    @Column(name = "STATE", nullable = false)
    private Integer state;

    @NotNull
    @Min(value = 10)
    @Max(value = 10)
    @Column(name = "CATEGORY", nullable = false)
    private Integer category;

    @NotNull
    @Min(value = 4)
    @Column(name = "SUBSCRIBED_CHARGING_CHARACTERISTICS", nullable = false)
    private Integer subscriberChargingCharacteristics;

    @NotNull
    @Size(min = 19, max = 19)
    @Column(name = "ICCID", length = 19, nullable = false)
    private String iccid;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImsi() {
        return imsi;
    }

    public Subscriber imsi(String imsi) {
        this.imsi = imsi;
        return this;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public Subscriber msisdn(String msisdn) {
        this.msisdn = msisdn;
        return this;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Integer getNam() {
        return nam;
    }

    public Subscriber nam(Integer nam) {
        this.nam = nam;
        return this;
    }

    public void setNam(Integer nam) {
        this.nam = nam;
    }

    public Integer getState() {
        return state;
    }

    public Subscriber state(Integer state) {
        this.state = state;
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getCategory() {
        return category;
    }

    public Subscriber category(Integer category) {
        this.category = category;
        return this;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getSubscriberChargingCharacteristics() {
        return subscriberChargingCharacteristics;
    }

    public Subscriber subscriberChargingCharacteristics(Integer subscriberChargingCharacteristics) {
        this.subscriberChargingCharacteristics = subscriberChargingCharacteristics;
        return this;
    }

    public void setSubscriberChargingCharacteristics(Integer subscriberChargingCharacteristics) {
        this.subscriberChargingCharacteristics = subscriberChargingCharacteristics;
    }

    public String getIccid() {
        return iccid;
    }

    public Subscriber iccid(String iccid) {
        this.iccid = iccid;
        return this;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Subscriber subscriber = (Subscriber) o;
        if (subscriber.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subscriber.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Subscriber{" +
            "id=" + getId() +
            ", imsi='" + getImsi() + "'" +
            ", msisdn='" + getMsisdn() + "'" +
            ", nam=" + getNam() +
            ", state=" + getState() +
            ", category=" + getCategory() +
            ", subscriberChargingCharacteristics=" + getSubscriberChargingCharacteristics() +
            ", iccid='" + getIccid() + "'" +
            "}";
    }
}
