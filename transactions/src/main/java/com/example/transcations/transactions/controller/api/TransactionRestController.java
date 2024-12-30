package com.example.transcations.transactions.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.transcations.transactions.model.dto.TransactionDTO;
import com.example.transcations.transactions.repository.TransactionRepository;

@RestController
@RequestMapping("api/transaction")
public class TransactionRestController {
    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionRestController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // @GetMapping
    // public ResponseEntity<Object> get() {
    // return CustomResponse.generate(HttpStatus.OK, "Data Found",
    // transactionRepository.findAll());
    // }

    // public ResponseEntity<Object> getByUsername(@RequestParam String search) {
    // return CustomResponse.generate(HttpStatus.OK, "Data Found",
    // transactionRepository.getByUsername(search));
    // }

    @GetMapping
    public Map<String, Object> getTransaction(@RequestParam("draw") int draw,
            @RequestParam("start") int start, @RequestParam("length") int length,
            @RequestParam(required = false, defaultValue = "") String search) {
        int page = start / length;

        Pageable pageable = PageRequest.of(page, length);
        Page<TransactionDTO> transactions = transactionRepository.findTransaction(search, pageable);

        long totalRecords = getTotalRecords();
        long filteredRecords = getFilteredRecordsCount(search);

        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw);
        response.put("recordsTotal", totalRecords);
        response.put("recordsFiltered", filteredRecords);
        response.put("data", transactions.getContent());
        return response;
    }

    private long getTotalRecords() {
        return transactionRepository.count();
    }

    private long getFilteredRecordsCount(String search) {
        return transactionRepository.countFiltered(search);
    }
}
