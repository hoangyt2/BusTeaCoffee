package com.java04.wibucian.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAccount", nullable = false, length = 15)
    private String id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idEmployee", referencedColumnName = "idEmployee")
    private Employee employee;

    @Column(name = "password", length = 50)
    private String password;

    @Column(name = "role")
    private Integer role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

}