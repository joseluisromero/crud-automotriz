package com.automotriz.crud.service;

import com.automotriz.crud.dto.RequestCreditDTO;
import com.automotriz.crud.exception.ValidationServiceCustomer;

import java.util.List;

public interface RequestCreditService {
    RequestCreditDTO save(RequestCreditDTO requestCreditDTO) throws ValidationServiceCustomer;
    RequestCreditDTO update(RequestCreditDTO requestCreditDTO) throws ValidationServiceCustomer;
    List<RequestCreditDTO> getRequestCreditAll();
    List<RequestCreditDTO> getRequestByStatus(String status);
}
