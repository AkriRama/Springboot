package com.example.transcations.transactions.model;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tb_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date transactionDate;

    private Float totalTransaction;

    private Float totalFee;

    private Float grandTotal;

    @ManyToOne
    @JoinColumn(name = "payment_status_id", referencedColumnName = "id")
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "payment_method_id", referencedColumnName = "id")
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "transaction")
    private List<TransactionDetail> transactionDetails;
}