package top.kwseeker.spring.myioc.beandefinition;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TypedStringValue {

    @NonNull private String value;
    private Class<?> targetType;
}
