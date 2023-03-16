package top.aikele.test;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class GeneratorCode {
    //代码生成器
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://192.168.3.41:3306/kele-oa", "kele", "kele1234")
                .globalConfig(builder -> {
                    builder.author("kele") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\Project\\kele-oa-parent\\service-oa\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("top.aikele") // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "D:\\Project\\kele-oa-parent\\service-oa\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("sys_menu") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
