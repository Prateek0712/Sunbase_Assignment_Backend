package com.sumbaseassignment.Controller;

import com.sumbaseassignment.Model.Customer;
import com.sumbaseassignment.Services.CustomerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/home")
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerServices customerServices;

    // Enpoint for creating a new customer

    @PostMapping("/api/create")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) throws Exception{
        try{
            Customer customerDetails = customerServices.addCustomer(customer);
            return ResponseEntity.ok(customerDetails);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // Endpoint for retrieving a paginated list of customers.
    @GetMapping("/api/getlist")
    public ResponseEntity<Page<Customer>> getList(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) throws Exception {
        try {
            Page<Customer> customerPage = customerServices.getAllCustomers(PageRequest.of(page, size));
            return ResponseEntity.ok(customerPage);
        } catch (Exception e) {
            throw new Exception("Internal Server Issue");
        }
    }


    // Endpoint for retrieving customer by ID.
    @GetMapping("/api/CustomerById/{customerId}")
    public ResponseEntity findById(@PathVariable int customerId) throws Exception{
        try{
            return ResponseEntity.ok(customerServices.getCustomerById(customerId));
        }
        catch (Exception e){
//            throw new Exception("Customer Not Found");
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint for deleting a customer by ID.
    @DeleteMapping("/api/deleteById/{customerId}")
    public ResponseEntity deleteById(@PathVariable int customerId) {
        try {
            customerServices.deleteCustomerById(customerId);
            return ResponseEntity.ok("Customer deleted from DB");
        } catch (Exception e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint for updating customer details by ID.
    @PutMapping("/api/updateDetails/{id}")
    public ResponseEntity<Customer> updateCustomerDetails(
            @PathVariable int id,
            @RequestBody Customer updateCustomer) {
        try {
            Customer updatedCustomer = customerServices.updateCustomerDetails(id, updateCustomer);

            if (updatedCustomer != null) {
                return ResponseEntity.ok(updatedCustomer);
            } else {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

     //Endpoint for searching customers by a searchTerm.
    @GetMapping("/api/customers/search")
    public List<Customer> searchCustomers(@RequestParam String searchTerm) {
        return customerServices.searchCustomers(searchTerm);
    }


    @GetMapping("/api/customers/search/sunbase")
    public List<Customer> searchCustomers() {
        return customerServices.addDataCustomer();
    }

    @PostMapping("/handlingExternal")
    public ResponseEntity<String> createEvent(@RequestBody Customer eventdata) {
        // the external URL
        String apiUrl = "https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=create";

        // Creating HttpHeaders and setting Authorization header
        HttpHeaders headers = new HttpHeaders();
        String bearerToken = "dGVzdEBzdW5iYXNlZGF0YS5jb206VGVzdEAxMjM=";
        headers.set("Authorization", "Bearer " + bearerToken);

        // RestTemplate created
        RestTemplate restTemplate = new RestTemplate();

        // Create HttpEntity with eventdata and headers
        HttpEntity<Customer> requestEntity = new HttpEntity<>(eventdata, headers);

        // Send POST request and getting response

       ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        // Return response body
        return response;

    }



}
