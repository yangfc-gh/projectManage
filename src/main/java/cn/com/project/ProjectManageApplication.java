package cn.com.project;

import cn.com.project.data.model.sys.SysMenu;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("cn.com.project.data.dao")
@SpringBootApplication
public class ProjectManageApplication {

	public static void main(String[] args) {
		// 就是这么奇葩，阿里的druid，想要允许多条sql批量执行，还是在这地方加这环境变量......
		System.setProperty("druid.wall.multiStatementAllow", "true");
		SpringApplication.run(ProjectManageApplication.class, args);
	}

}
