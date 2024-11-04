package com.sanctionloan.loan_sanction.contoller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanctionloan.loan_sanction.Models.ConsumerLoanRequest;
import com.sanctionloan.loan_sanction.Models.ConsumerResponse;
import io.apiwiz.compliance.config.EnableCompliance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Random;

@RestController
@EnableCompliance
@RequestMapping( value = {"/v1/acme-loans", "/v2/acme-loans"})
public class Consumer_Loan {

@Autowired
private RestTemplate restTemplate;

@Autowired
private ObjectMapper objectMapper;

@Value("${credit.check}")
private String creditCheck;
@PostMapping("/approve-loan-request")
ResponseEntity<?> approveLoanRequest(
        @RequestHeader Map<String, String> headers,
        @RequestBody(required = false) String body) throws JsonProcessingException {
    
    String res = """
              {
              "id": 12345,
              "creditScore": 700,
              "loanAmount": 50000,
              "loanProductId": "LP123456",
              "sourceBranchId": "BR001",
              "consumerId": "CUST7890",
              "status": "Approved",
              "loanType": "PL",
              "cts": "2024-11-03T10:15:30Z",
              "mts": "2024-11-03T10:15:30Z"}
                """;
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    Map<String,Object> maps = objectMapper.readValue(res,Map.class);
    return new ResponseEntity<>(maps, HttpStatus.OK);
}

@PostMapping("/loan-request")
ResponseEntity<?> createLoanRequest(
        @RequestHeader Map<String, String> headers,
        @RequestBody(required = false) ConsumerLoanRequest consumerLoanRequest,
        @RequestParam (required = false) Map<String, String> queries) throws URISyntaxException, JsonProcessingException, InterruptedException {
    boolean tracing = Boolean.parseBoolean(headers.get("tracing"));
    boolean deviateResponse = Boolean.parseBoolean(headers.get("deviateresponse"));
    boolean deviateCompliance = Boolean.parseBoolean(headers.get("compliance"));
    String ap = headers.get("application");
    if("API_MONITORING".equalsIgnoreCase(ap)){
        double probability = 0.1;
        Random random = new Random();
        if (random.nextDouble() <= probability) {
            try {
                int delayTime = 1000;
                System.out.println("Applying delay...");
                Thread.sleep(delayTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Delay was interrupted.");
            }
        } else {
            System.out.println("No delay applied.");
        }
    }
    if(tracing){
        HttpHeaders heads = new HttpHeaders();
        heads.add("tracing",String.valueOf(Boolean.TRUE));
        heads.add("deviateresponse",String.valueOf(deviateResponse));
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(heads);
        restTemplate.exchange(new URI(creditCheck), HttpMethod.GET, httpEntity, Object.class);
    }
    String res = """
            {
              "id": 12345,
              "loanAmount": 50000,
              "loanProductId": "LP123456",
              "sourceBranchId": "BR001",
              "consumerId": "CUST7890",
              "status": "Pending",
              "loanType": "PL",
              "cts": "2024-11-03T10:15:30Z",
              "mts": "2024-11-03T10:15:30Z"
            }
            """;
    String res2= """
            {
              "id": 12345,
              "loanAmount": "50000",
              "loanProductId": "LP123456",
              "sourceBranchId": "BR001",
              "consumerId": 121312412,
              "status": "Pending",
              "loanType": "BL",
              "cts": "2024-11-03T10:15:30Z",
              "mts": "2024-11-03T10:15:30Z",
              "secretCode":"1l#pL!982KN#$"
            }
            """;
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    Map<String,Object> mapss = objectMapper.readValue(res2,Map.class);
    if(deviateCompliance){
        return new ResponseEntity<>(mapss,HttpStatus.OK);
    }
    ConsumerResponse consumerResponse = objectMapper.readValue(res, ConsumerResponse.class);
    return new ResponseEntity<>(consumerResponse,HttpStatus.OK);
}

@PostMapping("/reject-loan-request")
ResponseEntity<?> rejectLoanRequest( @RequestHeader MultiValueMap<String, String> headers,
                                     @RequestBody(required = false) String body) throws JsonProcessingException {
    String res = """
              {
              "id": 12345,
              "creditScore": 400,
              "loanAmount": 50000,
              "loanProductId": "LP123456",
              "sourceBranchId": "BR001",
              "consumerId": "CUST7890",
              "status": "Rejected",
              "loanType": "PL",
              "cts": "2024-11-03T10:15:30Z",
              "mts": "2024-11-03T10:15:30Z"
              }
                """;
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    Map<String,Object> maps = objectMapper.readValue(res,Map.class);
    return new ResponseEntity<>(maps, HttpStatus.OK);
}


}
