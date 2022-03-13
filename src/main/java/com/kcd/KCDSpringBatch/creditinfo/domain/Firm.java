package com.kcd.KCDSpringBatch.creditinfo.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FIRM")
public class Firm {
    @Id @GeneratedValue
    @Column(name = "firm_id")
    private Long id;

    private String firmCode;

    @OneToMany(mappedBy = "firm", cascade = CascadeType.ALL)
    private List<FirmCustomer> firmCustomers = new ArrayList<>();

    public Firm(Long id) {
        this.id = id;
    }
}
