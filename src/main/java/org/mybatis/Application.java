package org.mybatis;

import org.mybatis.generator.MyGenerator;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mengtian on 2018-12-19
 */
public class Application {

    public static void main(String[] args) throws Exception {
        List<String> warnings = new LinkedList<>();

        ConfigurationParser parser = new ConfigurationParser(warnings);
        Configuration configuration = parser.parseConfiguration(new File("/Users/mengtian/Documents/code/stu/my-github/my-mybatis-generator/src/main/resources/generator.xml"));

        ShellCallback callback = new DefaultShellCallback(true);

        /*MyBatisGenerator generator = new MyBatisGenerator(configuration, callback, warnings);
        generator.generate(null);*/
        MyGenerator generator = new MyGenerator(configuration, callback, warnings, true);
        generator.generate(null);
    }
}
