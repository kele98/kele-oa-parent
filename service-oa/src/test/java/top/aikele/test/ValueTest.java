package top.aikele.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ValueTest {
    @Value("${name}")
    private String name;
    @Test
    public void test(){
        System.out.println(name);
    }
}
