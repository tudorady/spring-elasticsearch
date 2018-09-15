package com.simfony.mobility.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Subscriber entity. This class is used in SubscriberResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /subscribers?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SubscriberCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter imsi;

    private StringFilter msisdn;

    private IntegerFilter nam;

    private IntegerFilter state;

    private IntegerFilter category;

    private IntegerFilter subscriberChargingCharacteristics;

    private StringFilter iccid;

    public SubscriberCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getImsi() {
        return imsi;
    }

    public void setImsi(StringFilter imsi) {
        this.imsi = imsi;
    }

    public StringFilter getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(StringFilter msisdn) {
        this.msisdn = msisdn;
    }

    public IntegerFilter getNam() {
        return nam;
    }

    public void setNam(IntegerFilter nam) {
        this.nam = nam;
    }

    public IntegerFilter getState() {
        return state;
    }

    public void setState(IntegerFilter state) {
        this.state = state;
    }

    public IntegerFilter getCategory() {
        return category;
    }

    public void setCategory(IntegerFilter category) {
        this.category = category;
    }

    public IntegerFilter getSubscriberChargingCharacteristics() {
        return subscriberChargingCharacteristics;
    }

    public void setSubscriberChargingCharacteristics(IntegerFilter subscriberChargingCharacteristics) {
        this.subscriberChargingCharacteristics = subscriberChargingCharacteristics;
    }

    public StringFilter getIccid() {
        return iccid;
    }

    public void setIccid(StringFilter iccid) {
        this.iccid = iccid;
    }

    @Override
    public String toString() {
        return "SubscriberCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (imsi != null ? "imsi=" + imsi + ", " : "") +
                (msisdn != null ? "msisdn=" + msisdn + ", " : "") +
                (nam != null ? "nam=" + nam + ", " : "") +
                (state != null ? "state=" + state + ", " : "") +
                (category != null ? "category=" + category + ", " : "") +
                (subscriberChargingCharacteristics != null ? "subscriberChargingCharacteristics=" + subscriberChargingCharacteristics + ", " : "") +
                (iccid != null ? "iccid=" + iccid + ", " : "") +
            "}";
    }

}
