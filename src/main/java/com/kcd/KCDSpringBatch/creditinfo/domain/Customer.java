package com.kcd.KCDSpringBatch.creditinfo.domain;

import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CUSTOMER")
public class Customer {
    @Id @GeneratedValue
    @Column(name="customer_id")
    private Long id;

    private String customerNumber;

    @Column(name = "code_gender")
    private String codeGender;

    private Integer rating;

    @OneToMany(mappedBy = "customer")
    private List<FirmCustomer> firmCustomers = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    List<CardDetailInfo> cardDetailInfoList = new ArrayList<>();

    @LastModifiedDate
    @Column(updatable = false)
    private LocalDateTime lastModifiedTime;

    public Customer(String customerNumber, String codeGender) {
        this.customerNumber = customerNumber;
        this.codeGender = codeGender;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", customerNumber='" + customerNumber + '\'' +
                ", codeGender='" + codeGender + '\'' +
                ", rating=" + rating +
                '}';
    }
}
