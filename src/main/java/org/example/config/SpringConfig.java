package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;

@Configuration
@ComponentScan(value = {"org.example"})
@EnableWebMvc
@PropertySource({"classpath:database.properties"})
public class SpringConfig implements WebMvcConfigurer
{

	private final ApplicationContext applicationContext;
	private final Environment environment;

	@Autowired
	public SpringConfig(ApplicationContext applicationContext, Environment environment)
	{
		this.applicationContext = applicationContext;
		this.environment = environment;
	}

	@Bean
	public SpringResourceTemplateResolver templateResolver()
	{
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(applicationContext);
		templateResolver.setPrefix("/WEB-INF/views");
		templateResolver.setSuffix(".html");
		templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine()
	{
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry)
	{
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		registry.viewResolver(resolver);
	}

	@Bean
	public DataSource dataSource()
	{
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("driver")));
		dataSource.setUrl(environment.getProperty("url"));
		dataSource.setUsername(environment.getProperty("user"));
		dataSource.setPassword(environment.getProperty("password"));

		return dataSource;
	}

	@Bean
	public JdbcTemplate jdbcTemplate()
	{
		return new JdbcTemplate(dataSource());
	}

	@Bean
	public MessageSource messageSource()
	{
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages/messages");
		messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
		return messageSource;
	}

	@Bean
	public LocalValidatorFactoryBean validatorFactoryBean()
	{
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(messageSource());
		return localValidatorFactoryBean;
	}

	@Override
	public Validator getValidator()
	{
		return validatorFactoryBean();
	}

	@Bean
	public LocaleResolver localeResolver()
	{
		FixedLocaleResolver localeResolver = new FixedLocaleResolver();
		localeResolver.setDefaultLocale(new Locale("ru", "RU"));
//		localeResolver.setDefaultLocale(Locale.ENGLISH);
		return localeResolver;
	}
}
