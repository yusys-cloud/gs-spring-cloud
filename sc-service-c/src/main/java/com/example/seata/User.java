package com.example.seata;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author : yangzq80@gmail.com
 * @date: 2019-05-06
 */
@Getter
@Setter
@Entity
@Table(name="t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    Long money;
}
