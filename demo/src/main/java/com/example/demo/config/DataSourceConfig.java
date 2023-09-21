package com.example.demo.config;

import javax.sql.DataSource;
import javax.swing.*;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


@Configuration
public class DataSourceConfig {
    //오메

	// Spring-jdbc DataSource
//	@Bean
//	public DataSource dataSource()
//	{
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//		dataSource.setUrl("jdbc:mysql://localhost:3306/bookdb");
//		dataSource.setUsername("root");
//		dataSource.setPassword("1234");
//
//		return dataSource;
//	}

	//	HikariCP DataSource
    @Bean
    public HikariDataSource dataSource()
    {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/sns");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");

        return dataSource;
    }


}
