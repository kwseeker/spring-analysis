package top.kwseeker.spring.annotation.anno02;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class SomeBean {

    CommonBean commonBean;
}
