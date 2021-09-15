package top.kwseeker.spring.config.importAnno.service;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {

    private String name = "admin";
}
