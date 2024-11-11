package org.etutoria.accounts.service;

import org.etutoria.accounts.dto.CustomerDetailsDto;

public interface ICustomersService {
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber);
}
