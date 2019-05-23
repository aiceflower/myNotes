### Spring 注解驱动开发

#### 一.注解驱动方式启动容器

1.创建主配置类

```Java
//配置类等同于配置文件
@Configuration //告诉Spring容器这是一个配置类,是一个组件
public class MainConfig {
	//给容器中注册一个bean，类型为返回值类型，id默认为方法名,也可在@Bean中指定id
	@Bean
	public Person person(){
		return new Person("zhangsan",22);
	}
}
```



2.创建容器

```Java
ApplicationContext applicationContext = 
    new AnnotationConfigApplicationContext(MainConfig.class);
```

#### 二、常用注解

@ComponentScan: 包扫描注解相当于之前的<context:component-scan>

```Java
@ComponentScan(value = "com.aiceflower", excludeFilters = {//根据规则排除一些扫描类
      @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class, Service.class})
})
//value: 指定要扫描的包
//excludeFilters=Filter[] ：指定扫描时排除的组件
//includeFilters=Filter[] ：指定扫描时只包含某些组件,使用该组件需要指定useDefaultFilters = false
//useDefaultFilters: 是否启动默认包扫描
//FilterType.ANNOTATION:按注解过滤
//ASSIGNABLE_TYPE:按指定类型
//FilterType.ASPECTJ:使用aspectj表达式
//FilterType.REGEX:使用正则
//FilterType.CUSTOM:使用自定义规则，需要实现TypeFilter接口,如下
public class MyFilter implements TypeFilter{
	/**
	 *
	 * @param metadataReader 读取到的当前正在扫描类的信息
	 * @param metadataReaderFactory 可以获取到其它任何类信息
	 */
	@Override
	public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
		//获取注解信息
		AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
		//获取类信息
		ClassMetadata classMetadata = metadataReader.getClassMetadata();
		//获取类的资源，如路径等
		Resource resource = metadataReader.getResource();
		String className = classMetadata.getClassName();
		if (className.equals("er")){
			return true;
		}
		return false;
	}
}
```

@ComponentScans: 包扫描，可以包含多个ComponentScan

@Scope: 指定作用域，默认为单例

- prototype:多例，使用时才创建对象

- singleton: 单例，容器启动创建对象

- request:同一次请求创建一个实例

- session:同一个session创建一个实例

@Lazy : 懒加载，针对单实例，在使用的时候才创建bean

@Conditional:按照条件给容器注册bean,可标于方法或类上

```Java
@Bean("qbs")
@Conditional({MacCondition.class})
public Person person01(){
    return new Person("乔布斯",58);
}
public class MacCondition implements Condition{
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		//获取Ioc使用的beanFactory
		context.getBeanFactory();
		//获取类加载器
		context.getClassLoader();
		//获取当前环境信息
		Environment environment = context.getEnvironment();
		//获取bean定义的注册类,如判断某个类未注册过才注册
		context.getRegistry();
		String osName = environment.getProperty("os.name");
		System.out.println(osName);
		if (osName.contains("Mac")){
			return true;
		}
		return false;
	}
}


```

@PropertySource：加载外部properties配置文件

@PropertySources

```Java
@PropertySource(value = "config.properties",encoding = "utf-8")//指定编码防止中文乱码
```

@Profile：spring为我们提供的可以根据当前环境，动态的激活和切换一系列组件功能。开发环境，测试环境，生产环境。

指定组件在哪个环境下才注册到容器中，不指定环境标识的bean，任何环境下都能注册这个组件。

没有标注

```Java
//标识bean
@profile("dev") 默认是default环境,也可标于类上
@Bean
public Red red(){
	return new Red();
}
//激活环境 
//1.命令行参数
-Dspring.profiles.active=dev
//2.代码，激活环境
public static void main( String[] args )
    {
        //1.创建容器
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //2.激活环境
        ConfigurableEnvironment environment = context.getEnvironment();
        environment.setActiveProfiles("dev");
        //3.注册主配置类
        context.register(MainConfig.class);
        //4.刷新容器
        context.refresh();

    }
```





#### 三、给容器中注册bean

- 包扫描 + 组件标注注解(@Controller/@Service/@Repository/@Component) 只能注册自己写的类
- @Bean 导入的三方包中的bean,方法参数默认从容器中查找对应的bean
- @Import 导入组件 

```Java
//1）@Import({Person.class}) id默认是组件的全类名
//2）@Import({MySelector.class}) 使用选择器
public class MySelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //不能返回null,值为组件全类名
        return new String[]{"com.aiceflower.entity.Color"};
    }
}
//3) @Import({MyImportBeanRegister.class}) 使用注册器
public class MyImportBeanRegister implements ImportBeanDefinitionRegistrar{
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, 				BeanDefinitionRegistry registry) {
        boolean isRegist = registry.containsBeanDefinition("com.aiceflower.entity.Red");
        if (!isRegist){
            RootBeanDefinition definition = new RootBeanDefinition(Red.class);
            //可指定bean的id
            registry.registerBeanDefinition("red",definition);
        }
    }
}   
```

- 使用Spring提供的BeanFactory（工厂bean）[默认获取到的是工厂bean创建的对象，加&获取工厂本身]

```Java
public class ColorFactory implements FactoryBean<Color>{
	//返回一个对象，这个对象会添加到容器中
	@Override
	public Color getObject() throws Exception {
		return new Color();
	}

	//返回的对象的类型
	@Override
	public Class<?> getObjectType() {
		return Color.class;
	}

	//是否单例
	@Override
	public boolean isSingleton() {
		return true;
	}
}
//注册这个bean 
@Bean
public ColorFactory colorFactory(){
    return new ColorFactory();
}
Object color = applicationContext.getBean("colorFactory");//类型为Color.class
Object colorFactory = applicationContext.getBean("&colorFactory");//ColorFactory.class
```

#### 四、Bean的生命周期

bean的创建	—	初始化	—	销毁的过程

创建：单实例在容器启动时创建，调用构造器，多实例在每一次使用时创建

初始化：对象创建完成，并赋值后，调用初始化

销毁：关闭容器时容器销毁单实例bean，多实例bean容器不销毁

- 指定初始化和销毁方法

  @Bean(initMethod = "init",destroyMethod = "destroy")相当于配置中的init-methon,destroy-method

- 实现InitializingBean和DisposableBean接口进行初始化和销毁
- 使用@PostConstructor和@PreDestroy进行初始化和销毁
- BeanPostProcessor bean的<strong><span style="color:red">后置处理器</span></strong>，在bean初始化前后进行一些操作

```Java
@Component
public class MyBeanPostProcessor implements BeanPostProcessor{
	//初始化之前调用 init之前
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("postProcessBeforeInitialization ... " + beanName);
		return bean;
	}

	//初始化之后调用 init之后
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("postProcessAfterInitialization ... " + beanName);
		return bean;
	}
}

```

BeanPostProcessor处理器运行原理:

```Java
populateBean(beanName, mbd, instanceWrapper);//给bean进行属性赋值
initializeBean{
	invokeAwareMethods(beanName, bean);//
    applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);//初始化之前
    invokeInitMethods(beanName, wrappedBean, mbd);//执行自定义初始化
    applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);//初始化之后
}
```

Spring底层对BeanPostProcessor的使用:

bean赋值、注入其它组件、@Autowired、生命周期注解功能、@Async、xxxBeanPostProcessor等。

#### 五、属性赋值

使用@Value赋值

```Java
//1.基本数值
@Value("张三")
private String name;
@Value("18")
private int age;
//2.可以写SpEL，#{},可以取小数，基本算术运算,或容器中的bean
@Value("#{15.2}")//这里只能用#{}直接赋值可使用${}赋值报类型转换异常
private long price;
@Value("#{18-2}")
private int count;
@Value("#{color}")//bean id
Color color;
private long price;
//3.可以写${}，取出配置文件中的值，(在运行的环境变量中的值)
@Value("${name}")
private Stirng name;
```

#### 六、获取配置文件属性值

```Java
//1. @Value("${key}")
@Value("${name}")
private String name;
//2. 使用环境获取
Environment environment = applicationContext.getEnvironment();
System.out.println(environment.getProperty("name"));
//3.通过表达式解析
@Component
public class ConfigAware implements EmbeddedValueResolverAware{
	StringValueResolver resolver;
	@Override
	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		this.resolver = resolver;
		System.out.println(resolver.resolveStringValue("${name}"));
	}
}
```



#### 七、自动装配

@Autowired  spring规范，优先按照类型去查找bean，如果有多个，则按属性名作为bean的id去查找，配合@Qualifier可手动指定bean的id，默认没有找到bean则报错,使用required = false指定未开到不报错，AutowiredAnnotationBeanPostProcessor后置处理器用于解析@Autowired

@Primary 使用@Autowired默认注入该注解指定的bean，可使用@Qualifier指定想要加载的bean

@Resource java规范,可指定bean的id，默认按属性名称装配bean，不支持@Primary 和required = false

#### 八、自定义组件使用spring底层组件

底层组件ApplicationContext、BeanFactory，Environment等，自定义组件实现xxxAware接口，在创建对象的时候会调用接口指定的方法注入相关组件。xxxAware功能使用xxxProcessor处理。如：

ApplicationContextAware ====》 

```Java
@Component
public class Red implements ApplicationContextAware,BeanNameAware,EmbeddedValueResolverAware,EnvironmentAware{
	private ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;//ioc容器
	}
	@Override
	public void setBeanName(String name) {
		System.out.println(name);//当前bean的名字
	}
	@Override
	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		//spring值解析器
		System.out.println(resolver.resolveStringValue("${name}"));
	}
	@Override
	public void setEnvironment(Environment environment) {
		//获取环境
		System.out.println(environment.getProperty("os.name"));
	}
}

```

#### 九、AOP

概念：指在程序运行期间，动态地将某段代码切入到指定方法指定位置进行运算的编程方式。底层是使用动态代理实现的。

导入依赖spring-aspects

通知类型:

- 前置通知(@Before),目标方法运行前运行
- 后置通知(@After),目标方法运行后运行
- 返回通知(@AfterReturning),目标方法正常返回之后运行
- 异常通知(@AfterThrowing),目标方法运行异常后运行
- 环绕通知(@Around),动态代理，手动推进目标方法运行。

需要将目标类和切面类都加入到容器中，并告诉spring哪个类是切面类,即加入@Aspect，并给配置类加入@EnableAspectJAutoProxy启用基于注解的aop

```Java
@Component
public class MathCalculator {//目标类

	public int div(int i, int j){
		System.out.println("div...");
		return i/j;
	}
}
@Aspect
@Component
public class LogAspect {//切面类
	//1.本类引用，"pointCut()"
	//2.外部类引用 全路径"com.aiceflower.aop.LogAspect.pointCut()"
	//抽取
	@Pointcut("execution(public int com.aiceflower.aop.MathCalculator.div(int,int))")
	public void pointCut(){}

	//该类的所有方法，任意参数
	//@Before("public int com.aiceflower.aop.MathCalculator.*(..)")
	@Before("pointCut()")
	public void before(JoinPoint point){
		Object[] args = point.getArgs();
		System.out.println(""+point.getSignature().getName()+"方法运行前，参数列表是{}"+ 
                           Arrays.toString(args));
	}
	@After("com.aiceflower.aop.LogAspect.pointCut()")
	public void after(JoinPoint point){//JoinPoint如果出现则必需在方法的第一个参数
		System.out.println(point.getSignature().getName()+"方法运行结束");
	}
	@AfterThrowing(value = "pointCut()",throwing = "e")
	public void throwE(Exception e){
		System.out.println("方法运行异常"+e.getMessage());
	}
	@AfterReturning(value="pointCut()",returning = "result")
	public void returnR(Object result){
		System.out.println("方法运行正常返回"+result);
	}
	public void around(){
		System.out.println("方法运行");
	}
}
```

AOP原理: [看给容器注册了什么组件，这个组件的功能是什么，这个组件什么时候工作]

1.EnableAspectJAutoProxy是什么？

```java
@Import(AspectJAutoProxyRegistrar.class)
public @interface EnableAspectJAutoProxy {}
```

给容器中导入AspectJAutoProxyRegistrar，利用它给容器中注册bean

```java 
//beanName=Class
internalAutoProxyCreator = AnnotationAwareAspectJAutoProxyCreator.class
```

给容器中注册一个切面代理创建器

2.AnnotationAwareAspectJAutoProxyCreator

继承关系及关键调用方法:

AnnotationAwareAspectJAutoProxyCreator

​	-->AspectJAwareAdvisorAutoProxyCreator

​		-->AbstractAdvisorAutoProxyCreator

​			-->AbstractAutoProxyCreator 

​				implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware 

​				关注bean后置处理器，在bean初始化前后做的事情，和Aware接口

​	1.AbstractAutoProxyCreator.setBeanFactory()

​	AbstractAutoProxyCreator.后置处理器相关方法

​	2.AspectJAwareAdvisorAutoProxyCreator.setBeanFactory() ==> initBeanFactory()

​	3.AnnotationAwareAspectJAutoProxyCreator.initBeanFactory()

3.流程

1）创建ioc容器，传入配置类

2）注册配置类，调用refresh()刷新容器

3）registerBeanPostProcessors(beanFactory);注册bean的后置处理器，来拦截bean的创建【断点在AbstractAdvisorAutoProxyCreator.setBeanFactory】

​	1）获取ioc容器中已经定义了的，需要创建对象的BeanPostProcessor

​	2）给容器中加别的BeanPostProcessor

​	3）优先注册实现了PriorityOrdered接口的BeanPostProcessor

​	4）再注册实现了Order接口的BeanPostProcessor

​	5）最后注册未实现优先级接口的BeanPostProcessor

​	6）注册BeanPostProcessor，实际上是创建BeanPostProcessor，保存在容器中

​		创建internalAutoProxyCreator的BeanPostProcessor【AnnotationAwareAspectJAutoProxyCreator】

​		1）创建Bean实例

​		2）populateBean() 给bean的各种属性赋值

​		3）initializeBean() 初始化bean

​			1）invokeAwareMethods 处理Aware接口的方法回调

​			2）applyBeanPostProcessorsBeforeInitialization应用所有后置处理器的postProcessBeforeInitialization方法

​			3）invokeInitMethods 执行初始化方法

​			4）applyBeanPostProcessorsAfterInitialization应用所有后置处理器的postProcessAfterInitialization方法

​		4）BeanPostProcessor[AnnotationAwareAspectJAutoProxyCreator]创建成功，==>aspectJAdvisorsBuilder

​	7）把BeanPostProcessor注册到BeanFactory中beanFactory.addBeanPostProcessor(postProcessor)

=====以上是创建和注册AnnotationAwareAspectJAutoProxyCreator的过程======

​	AnnotationAwareAspectJAutoProxyCreator --> InstantiationAwareBeanPostProcessor

4）finishBeanFactoryInitialization[断点在AbstractAutoProxyCreator.postProcessBeforeInstantiation]，完成BeanFactory初始化工作，创建剩下的单实例bean

​	1）遍历获取容器中所有的bean，你依次创建对象getBean(beanName)

​		getBean() -> doGetBean() -> getSingleton()

​	2）创建bean

​		【AnnotationAwareAspectJAutoProxyCreator在所有bean创建之前会有一个拦截，因为它是InstantiationAwareBeanPostProcessor，会调用postProcessBeforeInstantiation方法】

​		1）先从缓存中获取当前单实例bean，如果有获取到，说明之前创建过，直接使用，否则再创建bean，只要创建好的bean都会被缓存起来

​		2）createBean() 创建bean，AnnotationAwareAspectJAutoProxyCreator会在任何bean创建之前尝试返回bean的实例

​		【BeanPostProcessor是在bean对象创建完成初始化前后调用的】

​		【InstantiationAwareBeanPostProcessor是在创建bean实例之前先尝试用后置处理器返回的对象】

​			1）resolveBeforeInstantiation(beanName, mbdToUse)；解析BeforeInstantiation希望后置处理顺在此能返回一个代理对象，如果能返回代理对象就使用，如果不能就继续

​				1）后置处理器先尝试返回对象

​				bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);

​				拿到所有后置处理器，如果是InstantiationAwareBeanPostProcessor就执行					

​				postProcessBeforeInstantiation方法

​			2）doCreateBean(beanName, mbdToUse, args)；真正去创建一个bean实例，和3.6流程一样



AnnotationAwareAspectJAutoProxyCreator【InstantiationAwareBeanPostProcessor】的作用：

1）每一个bean创建之前会调用postProcessBeforeInstantiation方法

​	关心自定义切面类的创建

​	1）判断当前类是否在advisedBeans中（保存了所有需要增强的bean，切面类就是增强的类）

​	2）判断当前bean是否是基础类型的Advice、Pointcut、Advisor、AopInfrastructureBean

​		或者是否是切面(@Aspect)

​	3）是否需要跳过

​		1）获取后先的增强器(切面里的通知方法) 【List<Advisor> candidateAdvisors】

​			每一个封装通知方法的增强器是InstantiationModelAwarePointcutAdvisor，判断每个增强器是否是AspectJPointcutAdvisor，返回true

​		2）永远返回false

2）创建对象

​	postProcessAfterInitialization（）

​		return wrapIfNecessary(bean, beanName, cacheKey);//返回包装如果需要的话

​		1）获取当前bean的所有增强器（通知方法）【Object[]s pecificInterceptors】

​			1）找到所有后选的增强器 (找到哪些通知方法是能切入当前bean方法)

​			2）获取到能在当前bean使用的增强器(所有的通知方法)

​			3）给增强器排序

​		2）保存当前bean在advisedBeans，表示已经增强

​		3）如果当前bean需要增强，则创建代理对象

​			1）获取所有增强器(通知方法)

​			2）保存到proxyFactory

​			3)创建代理对象(spring自动决定使用哪个动态代理)

​				JdkDynamicAopProxy(config)；

​				ObjenesisCglibAopProxy(config)；

​		4）给容器返回使用cglib增强了的代理对象 

​		5）以后容器中获取到的就是就是这个组件的代理对象，执行目标方法的时候，代理对象就会执行通知方法的流程。

3）目标方法执行

​	容器中保留了组件的代理对象（cglib增强后的的对象），这个对象里面保存了详细信息（比如增强器，目标方法，xxx）

​	1）CglibAopProxy.intercept();拦截目标方法的执行

​	2）根据ProxyFactory对象获取将要执行的目标方法的拦截器链

List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);

​		1）创建一个List<Object> interceptorList对象保存所有拦截器5，一个默认的ExposeInvocationIntercept和4个增强器。

​		2）遍历所有的增强器，将其转为Intercept, registry.getInterceptors(advisor)

​		3）将增强器转换为List<MethodInterceptor> interceptors

​			如果是MethodInterceptor直接加到容器中，如果不是使用AdvisorAdapter将增强器转换为MethodInterceptor，然后转换为数组返回​

​	3）如果没有拦截器链直接执行目标方法

​		拦截器链（每一个通知方法，又被包装为方法拦截器，利用MethodInterceptor机制）

​	4）如果有拦截器链，把需要执行的目标对象，目标方法，拦截器链等信息，传入创建一个CglibMethodInvocation对象，并调用其proceed()方法

​	5）拦截器链的触发过程

​		1）如果没有拦截器，直接执行目标方法，或者拦截器

​		2）链式获取每一个拦截器，拦截器执行invoke方法，每一个拦截器等待下一个拦截器执行完成返回以后再来执行，只有异常通知过程有异常捕获，拦截器链的机制，保证通知方法与目标方法的执行顺序。



总结：

​	1）@EnableAspectJAutoProxy 开启AOP功能

​	2）@EnableAspectJAutoProxy会为容器注册一个组件AnnotationAwareAspectJAutoProxyCreator

​	3）AnnotationAwareAspectJAutoProxyCreator是一个后置处理器

​	4）容器创建流程

​		1）registerBeanPostProcessors 注册后置处理器，创建AnnotationAwareAspectJAutoProxyCreator

​		2）finishBeanFactoryInitialization初始化剩下的单实例bean

​			1）创建业务逻辑组件和切面组件

​			2）AnnotationAwareAspectJAutoProxyCreator拦截组件的创建过程

​			3）组件创建完成以后，判断组件是否需要增强

​				是：切面通知方法，包装增强器（Advisor），给业务逻辑组件创建一个代理对象

​	5）执行目标方法

​		1）代理对象执行目标方法

​		2）CglibAopProxy.tercept()

​			1）得到目标方法 的拦截器链（增强器包装成拦截器MethodInterceptor）

​			2）利用拦截器的链式机制，依次进入每个拦截器进行执行

​			3）效果：

​				正常执行：前置通知->目标方法->后置通知->返回通知

​				出现异常：前置通知->目标方法->后置通知->异常通知

执行流程：

![](https://raw.githubusercontent.com/aiceflower/assets/master/img/java/spring/aop/aop_interceptor_chain.png)

####  十、申明式事务

环境准备：

1）导入相关依赖

​	数据源、数据库驱动、spring-jdbc模块

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>4.3.10.RELEASE</version>
</dependency>
<dependency>
    <groupId>c3p0</groupId>
    <artifactId>c3p0</artifactId>
    <version>0.9.1.2</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.43</version>
</dependency>
```



2）配置数据源、JdbcTemplate (Spring提供，简化数据库操作) 操作数据库

```java
@Configuration
public class TxConfig {

	//数据源
	@Bean
	public DataSource dataSource(){
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setUser("root");
		dataSource.setPassword("root");
		try {
			dataSource.setDriverClass("com.mysql.jdbc.Driver");
			dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/xtjtest");
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}

		return dataSource;
	}
	@Bean
	public JdbcTemplate jdbcTemplate(/*DataSource dataSource*/){
		//spring对@Configuration进行特殊处理，给容器中添加组件的方法，多次调用只是从容器中查找组件
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
		return jdbcTemplate;
	}
}
```

3）给方法标注@Transactional表示是一个事务方法

4）@EnableTransactionManagement //开启基于注解的事务管理功能

5）配置事务管理器来控制事务

```java
@Bean
public PlatformTransactionManager platformTransactionManager(){
    return new DataSourceTransactionManager(dataSource());
}
```

原理：

1）@EnableTransactionManagement利用@TransactionManagementConfigurationSelector给容器导入组件

​	AutoProxyRegistrar和ProxyTransactionManagementConfiguration

2）AutoProxyRegistrar给容器中注册一个InfrastructureAdvisorAutoProxyCreator组件：利用重置处理器机制在对象创建以后，包装对象返回一个代理对象（增强器），代理对象执行方法，利用拦截器链进行调用。

3）ProxyTransactionManagementConfiguration

​	1）给容器中注册事务增强器

​		1）增强器用事务的注解信息，AnnotationTransactionAttributeSource解析事务注解

​		2）事务拦截器TransactionInterceptor，保存了事务属性信息和事务管理器，它是一个MethodInterceptor，在目标方法执行的 时候执行拦截器链

​		事务拦截器：

​			1）获取事务属性TransactionAttribute

​			2）获取事务管理器PlatformTransactionManager，如果没有添加指定管理器，会从容器中按类型PlatformTransactionManager.class获取事务管理器

​			3）执行目标方法

​				如果异常，获取事务管理器回滚操作

​				如果正常，获取事务管理器提交操作		

#### 十一、扩展原理

1）BeanPostProcessor，bean后置处理器，bean创建对象初始化前后进行拦截工作

2）BeanFactoryPostProcessor, beanFactory的后置处理器，在beanFactory标准初始化之后调用，所有的bean定义已经保存加载到beanFactory中，但是bean的实例还未创建。



```
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor{
   @Override
   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
      System.out.println("MyBeanFactoryPostProcessor ...");
      beanFactory.getBeanDefinitionCount();//定义bean个数
      beanFactory.getBeanDefinitionNames();//定义隱的names
   }
}
```

​	1）	创建ioc容器

​	2）invokeBeanFactoryPostProcessors(beanFactory)执行BeanFactoryPostProcessors

​		如何找到所有的BeanFactoryPostProcessors并执行它们的方法

​		1）直接在BeanFactory中找到所有类型是BeanFactoryPostProcessor的组件，并执行它们的方法。

​		2）在初始化创建其它组件之前执行

3）BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor

​	postProcessBeanDefinitionRegistry() 在所有bean定义信息将要被加载，bean实例还未创建

​	优先于BeanFactoryPostProcessor，利用BeanDefinitionRegistryPostProcessor给容器再额外添加一些组件

```java
@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor{

	//BeanDefinitionRegistry bean定义信息的存储中心，以后beanFactory就是按照
	//BeanDefinitionRegistry中保存的每一个bean定义信息创建bean实例
	@Override//先工作
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		System.out.println("registry..bean的数量："+registry.getBeanDefinitionCount());
		//RootBeanDefinition beanDefinition = new RootBeanDefinition(Color.class);
		AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(Color.class).getBeanDefinition();
		registry.registerBeanDefinition("color", beanDefinition);
		
	}
	@Override//后工作
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("beanFactory...bean的数量: "+beanFactory.getBeanDefinitionCount());
	}
}
```

原理：

​	1）创建ioc容器

​	2）refresh() ==> invokeBeanFactoryPostProcessors(beanFactory) 

​	3）从容器中获取所有的BeanDefinitionRegistryPostProcessor，依次

​		1. 触发它们的postProcessBeanDefinitionRegistry()方法

​		2. 再触发它们的postProcessBeanFactory()方法【BeanFactoryPostProcessor】

​	4）再从容器中找到所有的BeanFactoryPostProcessor，然后依次触发postProcessBeanFactory()方法

4）ApplicationListener：监听容器中发布的事件，事件驱动模型开发

```
public interface ApplicationListener<E extends ApplicationEvent> 
```

​	监听ApplicationEvent及其子类事件

```java
@Component
public class MyApplicationListener implements ApplicationListener<ApplicationEvent>{
	//当容器中发布些事件，方法会等到触发
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		System.out.println("收到事件：" + event);
	}
}
```

容器默认刷新完成(所有的bean都创建完成)发布ContextRefreshedEvent事件，

容器关闭发布ContextClosedEvent事件。

发布自定义事件：

1）写一个监听器(ApplicationListener实现类)来监听某个事件(ApplicationEvent及其子类)

2）把监听器加入到容器中

3）只要容器中有相应类开的事件发布，就能监听到这个事件

4）发布事件

```java
public static void main( String[] args )
{
    //1.创建容器
    AnnotationConfigApplicationContext context = new 
        AnnotationConfigApplicationContext(TxConfig.class);
    context.publishEvent(new ApplicationEvent("发布事件") {//匿名内部类
    });
}
```

原理：	

​	ContextRefreshedEvent、App$1、ContextClosedEvent

1）ContextRefreshedEvent事件

​	1）创建ioc容器 

​	2)   refresh()  ==>  finishRefresh(); 容器刷新完成

2）自己发布事件

3）容器关闭事件

[事件发布流程：] （都会执行的流程）

​	publishEvent(new ContextRefreshedEvent(this));

​		1）获取事件多播器（派发器）；getApplicationEventMulticaster()

​		2）multicastEvent派发事件

​		3）获取到所有的ApplicationListener

​			for (final ApplicationListener<?> listener : getApplicationListeners(event, type)) 

​			1）如果有Executor，可以支持使用Executor进行异步派发

​			2）如果没有，同步的方式执行invokeListener(listener, event)

​			3）拿到listener，回调listener.onApplicationEvent(event);方法。

【事件多播器（派发器）】：

​	1）创建ioc容器

​	2）refresh()  ==>  initApplicationEventMulticaster();

​		1）去容器中找有没有id="applicationEventMulticaster"这个组件，如果有就获取

​		2）如果没有就自己创建一个，注册到容器中.

【容器中有哪些监听器】

​	1）创建ioc容器

​	2）refresh()  ==> registerListeners();

​		从容器中拿到所有的监听器，

​		String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);

​		把它们注册到applicationEventMulticaster中

​		getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);

利用注解监听事件：

```java
@Component
public class AnnoListener {
	@EventListener(classes = {ApplicationEvent.class})
	public void listener(ApplicationEvent event){
		System.out.println("注解监听事件: " + event);
	}
}
```

@EventListener原理：

使用EventListenerMethodProcessor处理器，解析方法上的@EventListener

EventListenerMethodProcessor实现了SmartInitializingSingleton接口

1）创建ioc容器

2）refresh() ==>  finishBeanFactoryInitialization(beanFactory);//初始化剩下的单实例bean

​	1）先创建所有的单实例bean，getBean() 一次循环

​	2）获取所有的单实例bean，判断是否是SmartInitializingSingleton类型，如果是就调用

​		smartSingleton.afterSingletonsInstantiated()



####  十二、spring容器创建过程

Spring容器的refresh()【创建刷新】

1）prepareRefresh() 刷新前的预处理

​	1）initPropertySources()初始化一些属性设置；子类定义一些个性化的属性设置方法

​	2）getEnvironment().validateRequiredProperties()校验属性的合法等

​	3）this.earlyApplicationEvents = new LinkedHashSet<ApplicationEvent>()保存容器中的一些早期事件

2）obtainFreshBeanFactory()获取beanFactory

​	1）refreshBeanFactory()刷新beanFactory

​		创建了一个this.beanFactory = new DefaultListableBeanFactory();设置id

​	2）getBeanFactory()返回刚才GenericApplicationContext创建的beanFactory对象

​	3）将创建的beanFactory(DefaultListableBeanFactory)返回

3）prepareBeanFactory(beanFactory)BeanFactory的预准备工作【BeanFactory的一些设置】

​	1）设置类加载器、支持的解析器...

​	2）添加部分BeanPostProcessor【ApplicationContextAwareProcessor】

​	3）设置忽略的自动装配接口EnvironmentAware、ResourceLoaderAware

​	4）注册可以解析的自动装配，我们能在任何组件中自动注入BeanFactory、ResourceLoader、ApplicationEventPublisher、ApplicationContext

​	5）添加BeanPostProcessor【ApplicationListenerDetector】

​	6）添加编译时的AspectJ支持

​	7）给BeanFactory中注册一些能用的组件、

​		environment【ConfigurableEnvironment】、

​		systemProperties【Map<String, Object>】、

​		systemEnvironment【Map<String, Object>】

4）postProcessBeanFactory(beanFactory)BeanFactory准备工作完成后进行的后置处理工作

​	子类通过重写这个方法在BeanFactory创建并好预准备完成以后做进一步的设置。

========================BeanFactory的创建及预准备工作==================================

5）invokeBeanFactoryPostProcessors(beanFactory)执行BeanFactoryPostProcessor

​	BeanFactoryPostProcessor、BeanFactory的后置处理器在BeanFactory标准初始化之后执行

​	两个接口：

​	BeanFactoryPostProcessor、BeanDefinitionRegistryPostProcessor

​	1）invokeBeanFactoryPostProcessors

​		先执行BeanDefinitionRegistryPostProcessor的方法：

​		1）获取所有的BeanDefinitionRegistryPostProcessor

​		2）先执行实现了PriorityOrdered优先级接口的的BeanDefinitionRegistryPostProcesso

​			postProcessor.postProcessBeanDefinitionRegistry(registry)

​		3）再执行实现了Ordered顺序接口的BeanDefinitionRegistryPostProcesso

​			postProcessor.postProcessBeanDefinitionRegistry(registry);

​		4）最后执行没有实现优先级或是顺序接口的BeanDefinitionRegistryPostProcesso

​			postProcessor.postProcessBeanDefinitionRegistry(registry);

​		再执行BeanFactoryPostProcessor

​		1）获取所有BeanFactoryPostProcessor

​		2）先执行实现了PriorityOrdered优先级接口的的BeanFactoryPostProcessor

​			postProcessor.postProcessBeanFactory(beanFactory);

​		3）再执行实现了Ordered顺序接口的BeanFactoryPostProcessor

​			postProcessor.postProcessBeanFactory(beanFactory);

​		4）最后执行没有实现优先级或是顺序接口的BeanFactoryPostProcessor

​			postProcessor.postProcessBeanFactory(beanFactory);

6）registerBeanPostProcessors(beanFactory)注册BeanPostProcessors(bean的后置处理器)【拦截bean的创建过程】

​	不同类型的BeanPostProcessor，在bean创建前后的执行时机是不一样的

​	BeanPostProcessors、

​	DestructionAwareBeanPostProcessor、

​	InstantiationAwareBeanPostProcessor、

​	MergedBeanDefinitionPostProcessor【internalPostProcessors】、

​	SmartInstantiationAwareBeanPostProcessor

​	1）获取所有的BeanPostProcessor，后置处理器默认都可通过PriorityOrdered和Ordered指定优先级和顺序

​	2）先注册PriorityOrdered接口的BeanPostProcessors

​		把每一个BeanPostProcessors添加到BeanFactory中

​		beanFactory.addBeanPostProcessor(postProcessor)

​	3）再注册Ordered接口的BeanPostProcessors

​	4）最后执行未实现优先级和排序接口的BeanPostProcessors

​	5）最最后执行MergedBeanDefinitionPostProcessor(internalPostProcessors)

​	6）注册一个ApplicationListenerDetector，来在bean创建完成后检查是否是ApplicationListener如果是

​		this.applicationContext.addApplicationListener((ApplicationListener<?>) bean);

7）initMessageSource();初始化MessageSource组件【作国际化、消息绑定、消息解析】

​	1）获取BeanFactory

​	2）看容器中是否有id为messageSource的组件，类型是MessageSource的组件

​		有则赋值 给messageSource属性，没有则自己创建一个【DelegatingMessageSource】

​		MessageSource：取出国际化配置文件中的某个key的值，能按区域信息获取

​	3）按创建好的messageSource注册在容器中,当获取国际化值时，可自动注入messageSource

​		beanFactory.registerSingleton(MESSAGE_SOURCE_BEAN_NAME, this.messageSource);

​		MessageSource.getMessage(String code, Object[] args, String defaultMessage, Locale locale);

8）initApplicationEventMulticaster()初始化事件派发器

​	1）获取BeanFactory

​	2）从BeanFactory中获取applicationEventMulticaster组件【ApplicationEventMulticaster】

​	3）如果上一步没有配置，创建一个SimpleApplicationEventMulticaster

​	4）将创建的applicationEventMulticaster注册到容器中，以后使用可直接注入

9）onRefresh()留给子类

​	子类重写这个方法，在容器刷新时可自定义逻辑

10）registerListeners()将容器中所有的ApplicationListener注册

​	1）获取所有的ApplicationListener

​	2）将每个监听器添加到事件派发器中

​		getApplicationEventMulticaster().addApplicationListener(listener);

​	3）派发之前步骤产生的事件

11）finishBeanFactoryInitialization(beanFactory);初始化所有剩下的所有单实例bean

​	beanFactory.preInstantiateSingletons()初始化剩下的单实例bean

​		1）获取容器中所有的bean，依次进行初始化和创建对象

​		2）拿到bean的定义信息RootBeanDefinition

​		3）bean不是抽象的，是单实例的，不是懒加载的

​			1）判断是否是factoryBean，是否是实现FactoryBean接口

​			是：利用工厂方法创建对象

​			否：利用getBean(beanName)创建对象，ioc.getBean()

​				1)doGetBean()

​				2)先获取缓存中保存的单实例bean，说明之前被创建过(所有创建过的单实例bean都会被缓存起来)

```java
private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(256);//缓存所有单实例bean
```

​				3）缓存中获取不到，开始bean的创建流程

​				4）标记当前bean已经被创建

​				5）获取bean的定义信息

​				6）获取当前bean依赖的其它的bean，如果有依赖的bean，则按getBean()把依赖的bean创建

​				7）启动单实例bean的创建流程

​					1）createBean(beanName, mbd, rgs)

​					2）resolveBeforeInstantiation(beanName, mbdToUse)

​					让beanPostProcessor【InstantiationAwareBeanPostProcessor】拦截返回代理对象

​					InstantiationAwareBeanPostProcessor提前执行

​					先触发：postProcessorBeforeInstantiation

​					如果有返回值再触发：postProcessorAfterInitialization

​					3）如果前面的InstantiationAwareBeanPostProcessor没有返回代理对象

​					4）调用doGetBean创建bean

​						1）创建bean实例，createBeanInstance(beanName, mbd, args)

​							利用工厂方法或对象的构造器创建对象

​						2）applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);

​						遍历调用bdp.postProcessMergedBeanDefinition(mbd, beanType, beanName);

​						3）populateBean(beanName, mbd, instanceWrapper);bean属性赋值

​							赋值之前：

​							1）拿到InstantiationAwareBeanPostProcessor后置处理器

​								执行postProcessAfterInstantiation方法

​							2）拿到InstantiationAwareBeanPostProcessor后置处理器

​								执行postProcessPropertyValues

​							======赋值之前：========

​							3）applyPropertyValues应用bean属性赋值，选用setter方法等赋值

​						4）initializeBean(beanName, exposedObject, mbd)初始化bean

​							1）invokeAwareMethods 执行xxxAware接口方法

​							2）applyBeanPostProcessorsBeforeInitialization后置处理器初始化之前的

​								BeanPostProcessor.postProcessBeforeInitialization	

​							3）invokeInitMethods执行初始化方法

​								1）是否为InitializingBean接口，是执行接口方法

​								2）mbd.getInitMethodName()看是否自定义了初始化方法

​							4）applyBeanPostProcessorsAfterInitialization后置处理器初始化之后的

​								BeanPostProcessor.postProcessAfterInitialization

​						5）registerDisposableBeanIfNecessary注册bean的销毁方法【容器关闭以后等执行】

​					5）addSingletonBean将创建的bean添加到缓存中singletonObjects

​					ioc容器就是这些map，很多的map，里面保存了单实例bean，环境信息...

​		=====所有bean都利用doGetBean创建完成======

​		4）检查所有bean是否是SmartInitializingSingleton类型的，是则执行其afterSingletonsInstantiated()				

12）finishRefresh()完成BeanFactory的初始化创建工作，IOC容器创建完成

​	1）initLifecycleProcessor();初始化和生命周期有关的后置处理器LifecycleProcessor

​		默认从容器中找，是否有这个组件，如果没有new DefaultLifecycleProcessor()并添加到容器中

​		写一个LifecycleProcessor的实现类，可以在BeanFactory刷新完成和关闭的时候调用

​		void onRefresh();

​		void onClose();

​	2）getLifecycleProcessor().onRefresh()拿到生命周期处理器回调onRefresh()

​	3）publishEvent(new ContextRefreshedEvent(this))发布容器刷新完成事件

​	4）LiveBeansView.registerApplicationContext(this);

======总结====

1）spring容器在启动的时候，先会保存所有注册进来的bean的定义信息

​	1）xml注册bean <bean>

​	2）注解注册bean @Service、@Component、@Bean等

2）spring容器会在合适的时机创建这些bean

​	1）用到这个bean的时候；利用getBean创建bean，保存到容器中

​	2）统一创建剩下所有bean的时候finishBeanFactoryInitialization

3）后置处理器

​	每一个bean创建完成，都会使用各种后置处理器，来增强bean的功能

​	AutowiredAnnotationBeanPostProcessor处理自动注入等

4）spring的事件驱动模型

​	ApplicationListener事件监听

​	ApplicationEventMulticaster事件派发