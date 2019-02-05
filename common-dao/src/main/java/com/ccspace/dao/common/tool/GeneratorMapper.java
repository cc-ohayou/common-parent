package com.ccspace.dao.common.tool;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *   使用Mybatis-generator时出现以下错误:

 Column name pattern can not be NULL or empty
 1
 错误产生的原因是因为使用了高版本的mysql驱动，当然你可以使用较低版本的mysql驱动，网上很多解决方案也是这样的。

 这里介绍根本的解决方案:

 connectionURL="jdbc:mysql://localhost:3306/shop？useSSL=false&amp;nullNamePatternMatchesAll=true"

 * Created by Administrator on 2015-8-10.
 */
public class GeneratorMapper {
    public static void main(String args[]) throws Exception {
        System.out.println("GeneratorMapper begin...");
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(GeneratorMapper.class.getResourceAsStream("/generate/generatorConfig.xml"));
//        Context context = config.getContext("Mysql");
        List<TableConfiguration> configs = config.getContext("Mysql").getTableConfigurations();
        for (TableConfiguration c : configs) {
            c.setCountByExampleStatementEnabled(false);
            c.setUpdateByExampleStatementEnabled(false);
            c.setUpdateByPrimaryKeyStatementEnabled(false);
            c.setDeleteByExampleStatementEnabled(false);
            c.setSelectByExampleStatementEnabled(false);
        }
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
        System.out.println("GeneratorMapper end...");
    }
}

