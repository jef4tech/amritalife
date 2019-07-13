package com.kraftlabs.crm_new.Models;

import java.util.Objects;

public class ExpenceConfig {
    private float jrTa;
    private float srTa;
    private  float jrDa;
    private float srDa;

    public float getJrTa() {
        return jrTa;
    }

    public void setJrTa(float jrTa) {
        this.jrTa = jrTa;
    }

    public float getSrTa() {
        return srTa;
    }

    public void setSrTa(float srTa) {
        this.srTa = srTa;
    }

    public float getJrDa() {
        return jrDa;
    }

    public void setJrDa(float jrDa) {
        this.jrDa = jrDa;
    }

    public float getSrDa() {
        return srDa;
    }

    public void setSrDa(float srDa) {
        this.srDa = srDa;
    }

    @Override
    public String toString() {
        return "ExpenceConfig{" +
                "jrTa=" + jrTa +
                ", srTa=" + srTa +
                ", jrDa=" + jrDa +
                ", srDa=" + srDa +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenceConfig that = (ExpenceConfig) o;
        return Float.compare(that.jrTa, jrTa) == 0 &&
                Float.compare(that.srTa, srTa) == 0 &&
                Float.compare(that.jrDa, jrDa) == 0 &&
                Float.compare(that.srDa, srDa) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jrTa, srTa, jrDa, srDa);
    }
}
