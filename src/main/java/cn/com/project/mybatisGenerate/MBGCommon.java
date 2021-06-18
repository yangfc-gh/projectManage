package cn.com.project.mybatisGenerate;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.springframework.util.StringUtils;

import java.util.Properties;
import java.util.Set;

public class MBGCommon implements CommentGenerator {

	@Override
	public void addClassAnnotation(InnerClass arg0, IntrospectedTable arg1,
			Set<FullyQualifiedJavaType> arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addClassComment(InnerClass arg0, IntrospectedTable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addClassComment(InnerClass arg0, IntrospectedTable arg1,
			boolean arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addComment(XmlElement arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addConfigurationProperties(Properties arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addEnumComment(InnerEnum arg0, IntrospectedTable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addFieldAnnotation(Field arg0, IntrospectedTable arg1,
			Set<FullyQualifiedJavaType> arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addFieldAnnotation(Field arg0, IntrospectedTable arg1,
			IntrospectedColumn arg2, Set<FullyQualifiedJavaType> arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addFieldComment(Field arg0, IntrospectedTable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addFieldComment(Field arg0, IntrospectedTable arg1,
			IntrospectedColumn arg2) {
		//判断数据库中该字段注释是否为空
        if(StringUtils.isEmpty(arg2.getRemarks()))
            return;
        arg0.addJavaDocLine("/**"+arg2.getRemarks()+"*/");
	}

	@Override
	public void addGeneralMethodAnnotation(Method arg0, IntrospectedTable arg1,
			Set<FullyQualifiedJavaType> arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addGeneralMethodAnnotation(Method arg0, IntrospectedTable arg1,
			IntrospectedColumn arg2, Set<FullyQualifiedJavaType> arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addGeneralMethodComment(Method arg0, IntrospectedTable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addGetterComment(Method arg0, IntrospectedTable arg1,
			IntrospectedColumn arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addJavaFileComment(CompilationUnit arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addModelClassComment(TopLevelClass arg0, IntrospectedTable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRootComment(XmlElement arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addSetterComment(Method arg0, IntrospectedTable arg1,
			IntrospectedColumn arg2) {
		// TODO Auto-generated method stub
	}

}
