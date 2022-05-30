package com.sling.test0520.domain;

import com.core.epril.entity.AuditEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "emailAndNum_UK", columnNames = {"eMail","phoneNum"})})
@Data
@EqualsAndHashCode(of = {}, callSuper = true)
@ToString(callSuper = true, of = {})
@NoArgsConstructor
@DynamicUpdate
public class User extends AuditEntity {


    @Column(length = 30, nullable = false)
    @NotEmpty
    @Email
    String email;


    @Column(length = 10, nullable = false)
    String name;

    @Column(length = 13, nullable = false)
    String phoneNum;

    @Column(length = 20, nullable = false)
    @NotEmpty
    String password;

    @Column(length = 40, nullable = false)
    String address;


    public User(long id) {
        this.id = id;
    }

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
