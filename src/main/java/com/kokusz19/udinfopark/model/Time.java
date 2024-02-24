package com.kokusz19.udinfopark.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Time {
    public int hour;
    public int minute;
}
