package com.simfony.mobility.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Subscriber entity.
 */
public class SubscriberDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 15, max = 15)
    private String imsi;

    @NotNull
    @Size(min = 15, max = 15)
    private String msisdn;

    @NotNull
    @Min(value = 0)
    @Max(value = 2)
    private Integer nam;

    @NotNull
    @Min(value = 1)
    @Max(value = 1)
    private Integer state;

    @NotNull
    @Min(value = 10)
    @Max(value = 10)
    private Integer category;

    @NotNull
    @Min(value = 4)
    private Integer subscriberChargingCharacteristics;

    @NotNull
    @Size(min = 19, max = 19)
    private String iccid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Integer getNam() {
        return nam;
    }

    public void setNam(Integer nam) {
        this.nam = nam;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getSubscriberChargingCharacteristics() {
        return subscriberChargingCharacteristics;
    }

    public void setSubscriberChargingCharacteristics(Integer subscriberChargingCharacteristics) {
        this.subscriberChargingCharacteristics = subscriberChargingCharacteristics;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SubscriberDTO subscriberDTO = (SubscriberDTO) o;
        if (subscriberDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subscriberDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubscriberDTO{" +
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
