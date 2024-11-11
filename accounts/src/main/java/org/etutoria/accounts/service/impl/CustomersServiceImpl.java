package org.etutoria.accounts.service.impl;


import lombok.AllArgsConstructor;
import org.etutoria.accounts.dto.AccountsDto;
import org.etutoria.accounts.dto.CardsDto;
import org.etutoria.accounts.dto.CustomerDetailsDto;
import org.etutoria.accounts.dto.LoansDto;
import org.etutoria.accounts.entity.Accounts;
import org.etutoria.accounts.entity.Customer;
import org.etutoria.accounts.exception.ResourceNotFoundException;
import org.etutoria.accounts.mapper.AccountsMapper;
import org.etutoria.accounts.mapper.CustomerMapper;
import org.etutoria.accounts.repository.AccountsRepository;
import org.etutoria.accounts.repository.CustomerRepository;
import org.etutoria.accounts.service.ICustomersService;
import org.etutoria.accounts.service.client.CardsFeignClient;
import org.etutoria.accounts.service.client.LoansFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;


    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
        if(null != loansDtoResponseEntity) {
            customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
        }

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails( mobileNumber);
        if(null != cardsDtoResponseEntity) {
            customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        }


        return customerDetailsDto;

    }


}
