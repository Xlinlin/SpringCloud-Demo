**1 入口(位于spring-cloud-contenxt工程下)** <br>
```org.springframework.cloud.bootstrap.config.PropertySourceBootstrapConfiguration.initialize```  <br>
1.1 该类图：<br>
![PropertySourceBootstrapConfiguration类图](https://raw.githubusercontent.com/Xlinlin/spring-cloud-demo/master/SpringCloud-Demo-Doc/img/Spring-cloud/Config/PropertySourceBootstrapConfiguration.png)
ApplicationContextInitializer的子类，在spring boot 进行初始化的时候调用，将所有PropertySourceLocator类型的对象的locate方法都调用一遍，然后将各个渠道得到的属性值放到
composite中利用insertPropertySources(propertySources, composite)设置到environment中。<br>
1.2 代码片段
```
//关键看这个PropertySourceLocator
private List<PropertySourceLocator> propertySourceLocators = new ArrayList<>();

@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		CompositePropertySource composite = new CompositePropertySource(
				BOOTSTRAP_PROPERTY_SOURCE_NAME);
		AnnotationAwareOrderComparator.sort(this.propertySourceLocators);
		boolean empty = true;
		ConfigurableEnvironment environment = applicationContext.getEnvironment();
		//遍历所有PropertySourceLocator的实现
		for (PropertySourceLocator locator : this.propertySourceLocators) {
			PropertySource<?> source = null;
			// **重点关注这个方法**
			source = locator.locate(environment);
			if (source == null) {
				continue;
			}
			logger.info("Located property source: " + source);
			composite.addPropertySource(source);
			empty = false;
		}
		if (!empty) {
			MutablePropertySources propertySources = environment.getPropertySources();
			String logConfig = environment.resolvePlaceholders("${logging.config:}");
			LogFile logFile = LogFile.get(environment);
			if (propertySources.contains(BOOTSTRAP_PROPERTY_SOURCE_NAME)) {
				propertySources.remove(BOOTSTRAP_PROPERTY_SOURCE_NAME);
			}
			insertPropertySources(propertySources, composite);
			reinitializeLoggingSystem(environment, logConfig, logFile);
			setLogLevels(applicationContext, environment);
			handleIncludedProfiles(environment);
		}
	}
```
1.3 <label style="color:red">PropertySourceLocator接口的实现</label><br>
![PropertySourceBootstrapConfiguration类图](https://raw.githubusercontent.com/Xlinlin/spring-cloud-demo/master/SpringCloud-Demo-Doc/img/Spring-cloud/Config/PropertySourceLocator-Impl.png)
可以看到实现类有两个：config.client的实现和config.server的实现，本文仅关注client包下的实现<br>
**2 org.springframework.cloud.config.client.ConfigServicePropertySourceLocator类** <br>
2.1 ConfigServicePropertySourceLocator.locate()方法代码片段<br>
```$xslt
@Override
	@Retryable(interceptor = "configServerRetryInterceptor")
	public org.springframework.core.env.PropertySource<?> locate(
			org.springframework.core.env.Environment environment) {
		// copy一份ConfigClientProperties
		ConfigClientProperties properties = this.defaultProperties.override(environment);
		CompositePropertySource composite = new CompositePropertySource("configService");
		// 获取RestTemplate  getSecureRestTemplate方法获取
		RestTemplate restTemplate = this.restTemplate == null ? getSecureRestTemplate(properties)
				: this.restTemplate;
		Exception error = null;
		String errorBody = null;
		logger.info("Fetching config from server at: " + properties.getRawUri());
		try {
			String[] labels = new String[] { "" };
			if (StringUtils.hasText(properties.getLabel())) {
				labels = StringUtils.commaDelimitedListToStringArray(properties.getLabel());
			}

			String state = ConfigClientStateHolder.getState();

			// Try all the labels until one works
			for (String label : labels) {
			    //restTemplate远程获取配置
				Environment result = getRemoteEnvironment(restTemplate,
						properties, label.trim(), state);
				if (result != null) {
					logger.info(String.format("Located environment: name=%s, profiles=%s, label=%s, version=%s, state=%s",
							result.getName(),
							result.getProfiles() == null ? "" : Arrays.asList(result.getProfiles()),
							result.getLabel(), result.getVersion(), result.getState()));
                    // 将获取到的结果设置到spring的环境中
					if (result.getPropertySources() != null) { // result.getPropertySources() can be null if using xml
						for (PropertySource source : result.getPropertySources()) {
							@SuppressWarnings("unchecked")
							Map<String, Object> map = (Map<String, Object>) source
									.getSource();
							composite.addPropertySource(new MapPropertySource(source
									.getName(), map));
						}
					}

					if (StringUtils.hasText(result.getState()) || StringUtils.hasText(result.getVersion())) {
						HashMap<String, Object> map = new HashMap<>();
						putValue(map, "config.client.state", result.getState());
						putValue(map, "config.client.version", result.getVersion());
						composite.addFirstPropertySource(new MapPropertySource("configClient", map));
					}
					return composite;
				}
			}
		}
		catch (HttpServerErrorException e) {
			error = e;
			if (MediaType.APPLICATION_JSON.includes(e.getResponseHeaders()
					.getContentType())) {
				errorBody = e.getResponseBodyAsString();
			}
		}
		catch (Exception e) {
			error = e;
		}
		if (properties.isFailFast()) {
			throw new IllegalStateException(
					"Could not locate PropertySource and the fail fast property is set, failing",
					error);
		}
		logger.warn("Could not locate PropertySource: "
				+ (errorBody == null ? error==null ? "label not found" : error.getMessage() : errorBody));
		return null;

	}
```
2.2 RestTemplate获取，url、环境差异等配置从ConfigClientProperties中获取，而就是从spring上下文中获取对应的一些配置信息<br>
```$xslt
//这里，重点关注下这个ConfigClientProperties
private RestTemplate getSecureRestTemplate(ConfigClientProperties client) {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setReadTimeout((60 * 1000 * 3) + 5000); //TODO 3m5s, make configurable?
		RestTemplate template = new RestTemplate(requestFactory);
		String password = client.getPassword();
		String authorization = client.getAuthorization();

		if (password != null && authorization != null) {
			throw new IllegalStateException(
					"You must set either 'password' or 'authorization'");
		}

		if (password != null) {
			template.setInterceptors(Arrays.<ClientHttpRequestInterceptor> asList(
					new BasicAuthorizationInterceptor(client.getUsername(), password)));
		}
		else if (authorization != null) {
			template.setInterceptors(Arrays.<ClientHttpRequestInterceptor> asList(
					new GenericAuthorization(authorization)));
		}

		return template;
	}
```
2.3 远程请求获取配置文件<br>
```
private Environment getRemoteEnvironment(RestTemplate restTemplate, ConfigClientProperties properties,
											 String label, String state) {
		String path = "/{name}/{profile}";
		String name = properties.getName();
		String profile = properties.getProfile();
		String token = properties.getToken();
		String uri = properties.getRawUri();

		Object[] args = new String[] { name, profile };
		if (StringUtils.hasText(label)) {
			args = new String[] { name, profile, label };
			path = path + "/{label}";
		}
		ResponseEntity<Environment> response = null;

		try {
			HttpHeaders headers = new HttpHeaders();
			if (StringUtils.hasText(token)) {
				headers.add(TOKEN_HEADER, token);
			}
			if (StringUtils.hasText(state)) { //TODO: opt in to sending state?
				headers.add(STATE_HEADER, state);
			}
			final HttpEntity<Void> entity = new HttpEntity<>((Void) null, headers);
			// http get获取json结果转换成Environment类
			response = restTemplate.exchange(uri + path, HttpMethod.GET,
					entity, Environment.class, args);
		}
		catch (HttpClientErrorException e) {
			if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
				throw e;
			}
		}

		if (response == null || response.getStatusCode() != HttpStatus.OK) {
			return null;
		}
		Environment result = response.getBody();
		return result;
	}
```
**3 org.springframework.cloud.config.client.ConfigServiceBootstrapConfiguration** <br>
Spring-cloud-config client 装载ConfigClientProperties、ConfigServicePropertySourceLocator到spring容器中。<br>
``ConfigClientProperties用于ConfigServicePropertySourceLocator中``<br>
``ConfigServicePropertySourceLocator用于PropertySourceBootstrapConfiguration中``<br>





