package com.example.KLF.server.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Report {
    @XmlAttribute
    String user_name;
    @XmlAttribute
    String activity_name;
    @XmlAttribute
    Integer amount;
    @XmlAttribute
    Timestamp first_occurrence;
    @XmlAttribute
    Timestamp last_occurrence;

    public Report(String user_name, String activity_name, Integer amount, Timestamp first_occurrence, Timestamp last_occurrence) {
        this.user_name = user_name;
        this.activity_name = activity_name;
        this.amount = amount;
        this.first_occurrence = first_occurrence;
        this.last_occurrence = last_occurrence;
    }

    public Report() {
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Timestamp getFirst_occurrence() {
        return first_occurrence;
    }

    public void setFirst_occurrence(Timestamp first_occurrence) {
        this.first_occurrence = first_occurrence;
    }

    public Timestamp getLast_occurrence() {
        return last_occurrence;
    }

    public void setLast_occurrence(Timestamp last_occurrence) {
        this.last_occurrence = last_occurrence;
    }

    @Override
    public String toString() {
        return "Report{" +
                "user_name='" + user_name + '\'' +
                ", activity_name='" + activity_name + '\'' +
                ", amount=" + amount +
                ", first_occurrence=" + first_occurrence +
                ", last_occurrence=" + last_occurrence +
                '}';
    }
}
