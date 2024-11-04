package com.sanctionloan.loan_sanction.contoller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.apiwiz.compliance.config.EnableCompliance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


@RestController
@EnableCompliance
@RequestMapping(value = {"/v1/acme-credit", "/v2/acme-credit"})
public class ConsumerCreditCheckService {
@Autowired
private RestTemplate restTemplate;
@Value("${approve.loan}")
private String approve;

@Value("${reject.loan}")
private String reject;
@Autowired
private ObjectMapper objectMapper;

@GetMapping("/v1/check-credit")
ResponseEntity<?> checkConsumerCredit(
        @RequestHeader Map<String, String> headers) throws URISyntaxException, InterruptedException, JsonProcessingException {
    boolean tracing = Boolean.parseBoolean(headers.get("tracing"));
    boolean deviateResponse = Boolean.parseBoolean(headers.get("deviateresponse"));
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
    if(deviateResponse){
        if(tracing){
            HttpHeaders heads = new HttpHeaders();
            heads.add("tracing",String.valueOf(Boolean.TRUE));
            heads.add("deviateresponse",String.valueOf(deviateResponse));
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(heads);
            Thread.sleep(10);
            restTemplate.exchange(new URI(reject), HttpMethod.POST, httpEntity, Object.class);
        }
        String res = """
              {
              "id": 12345,
              "creditScore": 400,
              "loanAmount": 50000,
              "loanProductId": "LP123456",
              "sourceBranchId": "BR001",
              "consumerId": "CUST7890",
              "status": "CreditScoreValidation",
              "loanType": "PL",
              "cts": "2024-11-03T10:15:30Z",
              "mts": "2024-11-03T10:15:30Z"
              }
                """;
        Map<String,Object> maps = objectMapper.readValue(res,Map.class);
        return new ResponseEntity<>(maps, HttpStatus.OK);
    }else{
        if(tracing){
            HttpHeaders heads = new HttpHeaders();
            heads.add("tracing",String.valueOf(Boolean.TRUE));
            heads.add("deviateresponse",String.valueOf(deviateResponse));
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(heads);
            Thread.sleep(10);
             restTemplate.exchange(new URI(approve), HttpMethod.POST, httpEntity, Object.class);
        }
        String res = """
              {
              "id": 12345,
              "creditScore": 700,
              "loanAmount": 50000,
              "loanProductId": "LP123456",
              "sourceBranchId": "BR001",
              "consumerId": "CUST7890",
              "status": "CreditScoreValidation",
              "loanType": "PL",
              "cts": "2024-11-03T10:15:30Z",
              "mts": "2024-11-03T10:15:30Z"
              }
                """;
        Map<String,Object> map = objectMapper.readValue(res,Map.class);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
}
