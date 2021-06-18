package cn.com.project.mybatisGenerate;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 执行main方法，创建mybatis基础操作表实体类和接口等
 * mbg.xml 中调整库连接和配置要创建的表名
 * @author Administrator
 *
 */
public class MybatisGenerator {
	public static void main(String[] args) {
        MybatisGenerator generator = new MybatisGenerator();
        generator.run();
    }
    public void run() {
        try {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("cn/com/project/mybatisGenerate/mybatisGenerateConfig.xml");
            List<String> warnings = new ArrayList<>();
            ConfigurationParser parser = new ConfigurationParser(warnings);
            Configuration config = parser.parseConfiguration(resourceAsStream);
            DefaultShellCallback callback = new DefaultShellCallback(true);
            MyBatisGenerator generator = new MyBatisGenerator(config, callback, warnings);
            generator.generate(null);
            for (String warning : warnings) {
                System.err.println(">" + warning);
            }
            System.out.println("生成成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
