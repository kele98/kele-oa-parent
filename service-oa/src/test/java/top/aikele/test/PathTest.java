package top.aikele.test;

import java.io.File;
import java.nio.file.Path;

public class PathTest {
    public static void main(String[] args) {
        File file = new File("D:\\Project\\kele-oa-parent\\service-oa\\target\\classes\\");
        System.out.println(file.getPath());
    }
}
