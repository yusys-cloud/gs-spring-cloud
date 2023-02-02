package com.example.seata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

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
    Long id;

    String name;
    Long money;
}
