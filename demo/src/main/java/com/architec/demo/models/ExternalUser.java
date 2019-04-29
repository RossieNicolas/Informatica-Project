package com.architec.demo.models;

import javax.persistence.*;

@Entity
@Table(name = "externalUsers")
public class ExternalUser {
    @Id
    @Column(name = "extern_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long externId;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "extra_info", nullable = false)
    private String extraInfo;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REMOVE })
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User userId;

    public ExternalUser() {
    }

    public ExternalUser(String company, String extraInfo, String password) {
        this.company = company;
        this.extraInfo = extraInfo;
        this.password = password;
    }
}
