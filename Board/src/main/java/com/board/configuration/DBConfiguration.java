package com.board.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration //스프링은 @Configuration이 지정된 클래스를 자바 기반의 설정 파일로 인식
@PropertySource("classpath:/application.properties") // 해당 클래스에서 참조할 properties 파일 위치를 지정
@EnableTransactionManagement
public class DBConfiguration {

	@Autowired // 빈 주입 @Resource, @Inject 등이 존재합니다.
	private ApplicationContext applicationContext;
	/*	ApplicationContext는 스프링 컨테이너(Spring Container) 중 하나입니다.
		스프링 컨테이너는 빈(Bean)의 생성과 사용, 관계, 생명 주기 등을 관리합니다.
		빈(Bean)은 쉽게 이야기하면 객체입니다.

	 */

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
/*
 	트랜잭션 매니저 등록
 * */

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	/*	우리는 prefix에 spring.datasource.hikari를 지정하였는데요,
		쉽게 이야기하면 @PropertySource에 지정된 파일(application.properties)에서
		prefix에 해당하는 spring.datasource.hikari로 시작하는
		설정을 모두 읽어 들여 해당 메서드에 매핑(바인딩)합니다.
	 */
	public HikariConfig hikariConfig() {
		return new HikariConfig();

	}

	@Bean
	public DataSource dataSource() {
		return new HikariDataSource(hikariConfig());

	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource());
		factoryBean.setMapperLocations(applicationContext.getResources("classpath:/mappers/**/*Mapper.xml")); // ~ Mapper.xml 파일을 읽어들임
		factoryBean.setTypeAliasesPackage("com.board.*");//XML에서 parameterType과 resultType에 사용할 alias(별칭)지정
		factoryBean.setConfiguration(mybatisConfg());
		return factoryBean.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSession() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory());
	}

	@Bean
	@ConfigurationProperties(prefix = "mybatis.configuration")
	//application.properties에서 mybatis.configuration으로 시작하는 모든 설정을 읽어 들여 빈(Bean)으로 등록합니다.
	public org.apache.ibatis.session.Configuration mybatisConfg(){
		return new org.apache.ibatis.session.Configuration();
	}
}
