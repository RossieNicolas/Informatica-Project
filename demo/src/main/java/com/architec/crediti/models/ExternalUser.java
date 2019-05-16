package com.architec.crediti.models;

import javax.persistence.*;

@Entity
@Table(name = "external_users")
public class ExternalUser {
    @Id
    @Column(name = "extern_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long externId;

    @Column(name = "firstname", nullable = false)
    private String firstname;
    @Column(name = "lastname", nullable = false)
    private String lastname;
    @Column(name = "company", nullable = false)
    private String company;
    @Column(name = "phone", nullable = false)
    private String phone;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "postal", nullable = false)
    private String postal;
    @Column (name = "approved")
    private boolean approved;

    @Column(name = "password", nullable = false)
    private char[] password;

    @Column(name = "salt")
    private byte[] salt;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REMOVE})
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User userId;

    public ExternalUser() {
    }

    public ExternalUser(String firstname, String lastname, String company, String phone, String address, String city,
                        String postal, char[] password, byte[] salt) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.company = company;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.postal = postal;
        this.password = password;
        this.salt = salt;
        this.approved = false;
    }

    public ExternalUser(String firstname, String lastname, String company, String phone, String address, String city,
                        String postal) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.company = company;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.postal = postal;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public long getExternId() {
        return externId;
    }

    public void setExternId(long externId) {
        this.externId = externId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}
