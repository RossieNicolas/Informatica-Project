package com.architec.crediti.models;

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

    @Column(name = "adress", nullable = false)
    private String adress;

    @Column(name = "postalcode", nullable = false)
    private String postal;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REMOVE })
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User userId;

    public ExternalUser() {
    }

    public ExternalUser(String company, String adress, String postal, String city, String password) {
        this.company = company;
        this.adress = adress;
        this.postal = postal;
        this.city = city;
        this.password = password;
    }

    public long getExternId() {
        return externId;
    }

    public void setExternId(long externId) {
        this.externId = externId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

}
