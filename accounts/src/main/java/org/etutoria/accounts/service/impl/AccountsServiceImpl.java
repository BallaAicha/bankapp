package org.etutoria.accounts.service.impl;
import lombok.AllArgsConstructor;
import org.etutoria.accounts.constants.AccountsConstants;
import org.etutoria.accounts.dto.AccountsDto;
import org.etutoria.accounts.dto.CustomerDto;
import org.etutoria.accounts.entity.Accounts;
import org.etutoria.accounts.entity.Customer;
import org.etutoria.accounts.exception.CustomerAlreadyExistsException;
import org.etutoria.accounts.exception.ResourceNotFoundException;
import org.etutoria.accounts.mapper.AccountsMapper;
import org.etutoria.accounts.mapper.CustomerMapper;
import org.etutoria.accounts.repository.AccountsRepository;
import org.etutoria.accounts.repository.CustomerRepository;
import org.etutoria.accounts.service.IAccountsService;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;
@Service
@AllArgsConstructor
public class AccountsServiceImpl  implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    /**
     * @param customerDto - CustomerDto Object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    +customerDto.getMobileNumber());
        }
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }

    /**
     * @param customer - Customer Object
     * @return the new account details
     */
    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        return newAccount;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        //Recherche du client par numéro de mobil
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(//verify if the customer exists
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow( //rechercher le compte par ID client
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        //Mapping des détails du client vers CustomerDto
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        //Mapping des détails du compte vers AccountsDto et ajout à CustomerDto
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));//map the account details to AccountsDto
        return customerDto;
    }




    /**
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;// variable ppour verifier si la mise à jour a été effectuée
        //extarire les détails du compte du client
        AccountsDto accountsDto = customerDto.getAccountsDto();
        //vérifier si les détails du compte ne sont pas nuls pour mettre à jour
        if(accountsDto !=null ){
            //rechercher le compte par numéro de compte
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            //mise à jour des détails du compte
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);
            //rechercher le client par ID client
            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }





}
