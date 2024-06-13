package com.sumbaseassignment.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumbaseassignment.Model.Customer;
import com.sumbaseassignment.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerServices {

    // Autowired instance of CustomerRepository for accessing data layer.
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestTemplate restTemplate;

    /*@Value("${sunbasedata.api.auth.url}")
    private String authUrl;

    @Value("${sunbasedata.api.data.url}")
    private String dataUrl;*/


    public Customer addCustomer(Customer customer) throws Exception {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new Exception("Customer with the same email already exists in the database");
        }
        return customerRepository.save(customer);
    }


    // Retrieves a customer by ID.
    public Customer getCustomerById(int customerId) throws Exception {

        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isEmpty()) {
            throw new Exception("Customer not found");
        }
        return customer.get();
    }


    // Deletes a customer by ID.
    public void deleteCustomerById(int customerId) throws Exception {

        try {
            Optional<Customer> customer = customerRepository.findById(customerId);

            if (customer.isEmpty()) {
                throw new Exception("Customer not found");
            } else {
                customerRepository.deleteById(customerId);
            }

        } catch (EmptyResultDataAccessException e) {
            throw new Exception("Invalid id: " + customerId, e);
        }
    }

    // Updates details of an existing customer.
    public Customer updateCustomerDetails(int cutomerId, Customer updateCustomer) throws Exception {

        try {
            Customer existingCustomer = customerRepository.findById(cutomerId).orElse(null);

            if (existingCustomer != null) {
                existingCustomer.setFirst_name(updateCustomer.getFirst_name());
                existingCustomer.setLast_name(updateCustomer.getLast_name());
                existingCustomer.setCity(updateCustomer.getCity());
                existingCustomer.setEmail(updateCustomer.getEmail());
                existingCustomer.setAddress(updateCustomer.getAddress());
                existingCustomer.setState(updateCustomer.getState());
                existingCustomer.setStreet(updateCustomer.getStreet());
                existingCustomer.setPhone(updateCustomer.getPhone());

                return customerRepository.save(existingCustomer);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new Exception("Customer not found");
        }
    }

    // Retrieves all customers with pagination.
    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    // Searches for customers based on a search term.
    public List<Customer> searchCustomers(String searchTerm) {
        return customerRepository.searchCustomers(searchTerm);
    }


    //fetching data from remote api
    /*private List<Customer> fetchCustomerList(String bearerToken) {

    }*/

    // saving all the data which is coming from remote apis
    public List<Customer> addDataCustomer() {
        String apiUrl = "https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";
        HttpHeaders headers = new HttpHeaders();
        String bearerToken = "dGVzdEBzdW5iYXNlZGF0YS5jb206VGVzdEAxMjM=";
        headers.set("Authorization", "Bearer " + bearerToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Customer[]> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, Customer[].class);

        Customer[] objects = response.getBody();
        for (Customer cust : objects) {
            customerRepository.save(cust);
        }
        return List.of(objects);

    }

    // Helper method to convert a list of maps to a list of objects of the specified type
    private <T> List<T> convertToList(List<Map<String, Object>> list, Class<T> targetType) {
        return list.stream()
                .map(map -> new ObjectMapper().convertValue(map, targetType))
                .collect(Collectors.toList());
    }


    private void updateAndSaveCustomers(List<Customer> customers) {
        customers.forEach(customerRepository::save);
    }


}


