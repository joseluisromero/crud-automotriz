package com.automotriz.crud.enums;

import lombok.Getter;

@Getter
public enum SujectCreditType {
    SujectCredit("Sujeto de crédito"),
    NotSujectCredit("No sujeto de crédito");
    private String name;

    SujectCreditType(String name) {
        this.name = name;
    }
    private String getName(SujectCreditType sujectCreditType) {
        return this.name = sujectCreditType.getName();
    }
}
