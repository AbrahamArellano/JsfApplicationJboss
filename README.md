# JsfApplicationJboss
JSF application prepared to be deployed on a Jboss 6 EAP

General Overview:
-----------------
- JSF application that provides multiple Java web functionalities:
+ Decorators
+ Interceptors
+ Events
+ Qualifiers
+ JMS
+ REST
+ JPA communication / full data layer
+ Rich faces
+ JAAS security
+ Multiple Resource Bundle handling
+ Expression Language usage
+ JSF validator / converters


Detail list:
------------
1.- Configure and create enterprise-ready web projects

	a.- Configure database connection and persistence details
		
		+ Define JNDI in Datasource:

			<datasources xmlns="http://www.jboss.org/ironjacamar/schema">
			 <datasource jndi-name="java:jboss/datasources/JBTravelDatasource" enabled="true" 
			     use-java-context="true" pool-name="JBTravelDatasource">
			  <connection-url>jdbc:postgresql://localhost:5432/postgres</connection-url>
			  <driver>postgresql-8.4-703.jdbc4.jar</driver>
			  <pool></pool>
			  <security>
			   <user-name>postgres</user-name>
			   <password>postgres</password>
			  </security>
			 </datasource>
			</datasources>

		+ Persistence.xml:

			<persistence xmlns="http://java.sun.com/xml/ns/persistence" 
				     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				     xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd" 
				     version="1.0">         
			   <persistence-unit name="JBTravel" transaction-type="JTA">
			      <provider>org.hibernate.ejb.HibernatePersistence</provider>
			      <jta-data-source>java:jboss/datasources/JBTravelDatasource</jta-data-source>
			      <properties>
				 <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQLDialect"/>
				 <property name="hibernate.hbm2ddl.auto" value="none"/>
				 <property name="hibernate.show_sql" value="true"/>
				 <property name="hibernate.format_sql" value="true"/>
				 <property name="hibernate.default_schema" value="TEST"/>
				 <property name="jboss.entity.manager.factory.jndi.name" value="java:jboss/JBTravelEntityManagerFactory"/>
			      </properties>
			   </persistence-unit>
			</persistence>

    b.- Include existing components and entities

		@Entity: specifies, at the class level, that this Java class represents a JPA entity.

		@Table: specifies, at the class level, what table this class maps to, along with the schema (for some datasources this is required) which stores the table.

		@Column: specifies, at either the field, or the bean getter, level, which column in the database the field maps to.

		@Id: specifies the field which is the unique ID for the entry in the database.

	c.- Generate views and view beans from entities

		a.- Right click on project -> Properties
		b.- Click JPA
		c.- JPA window:
			- Platform: Generic 2.0
			- Type: Disable Library Configuration
			- Persistent clas management: Discover anotated classes automatically
		d.- Add Connection:
			- Select PostgreSQL
			- Add new driver: "Use the .jar"
		e.- Define database DATA : user, pass, url, ...

	d.- Choose between war and ear applications

		WAR: package web apps
		EAR: package web apps + ejb

    e.- Generate the application scaffold

		- Using import maven
		- Using kickstarter

    f.- Handle web and enterprise application project structures, including possible configuration file locations

		- web.xml
		- faces-config.xml
		- jboss-web.xml


    g.- Generate and deploy the application using maven or JBoss Tools

		Maven:
			- mvn jboss-as:deploy


2.- Work with contexts (CDI)

        a.- Use the best context lifecycle for a given use case

			- @ApplicationScoped
			- @SessionScoped
			- @ConversationScoped
			- @RequestScoped
			- @Dependent

        b.- Make good use of Conversation by understanding:

            - Context order used by the container to resolve components and context variables:

		

            - Temporary versus long running

				+ Temporary/Transient: from one request to the other, while there is no REDIRECTION (ALMOST AS REQUEST)
				+ Long Running: usage of @ConversationalScoped and @Inject Conversation

				    - Conversation promotion:

				+ Annotate class @ConversationalScoped
				+ @Inject Conversation
				+ Check is not transient. conversation.isTransient() . To start a new conversation
				+ Invoke: conversation.begin()
				+ Invoke: conversation.end()

            - Conversation demarcation and propagation:

				+ Demarcation:

					- using "begin" and "end"

				+ Propagation:
		
					- A long-running conversation will automatically propagate with any JSF request. 
					- The usage of a link will break the conversation.
					  To keep it use the "cid" parameter

						<a href="/addCar.jsp?cid=#{conversation.id}">Add my car</a>

            - Relation between conversations, transactions, and persistence context flushmode

				????

            - Debugging a CDI application (potentially with Arquillian)
		
				+ @RunWith(Arquillian.class)
				+ public static method annotated with @Deployment that returns a ShrinkWrap archive
				+ One @Test method
				+ /src/test/resources/arquillian.xml

					(e.g.)

					a.-

						@Deployment
						public static JavaArchive createDeployment() {
							JavaArchive jar = ShrinkWrap
									.create(JavaArchive.class, "test.jar")
									.addPackages(true, "accounting")
									.addAsManifestResource(
											new File("src/main/webapp/WEB-INF", "beans.xml"))
									.addAsManifestResource(
											new File("src/main/resources/META-INF",
													"persistence.xml"));
							System.out.println(jar.toString(true)); // for debugging
							return jar;
						}

					b.-

						<arquillian xmlns="http://jboss.org/schema/arquillian"
							xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
							xsi:schemaLocation="http://jboss.org/schema/arquillian
							http://jboss.org/schema/arquillian/arquillian_1_0.xsd">

							<engine>
								<property name="deploymentExportPath">target/deployments</property>
							</engine>

						</arquillian>

					c.- Dependency:

						<dependency>
							<groupId>org.jboss.arquillian.junit</groupId>
							<artifactId>arquillian-junit-container</artifactId>
							<scope>test</scope>
						</dependency>

		c.- Manage context content

			Content using different contexts

3.- Work with POJO/JEE components

        a.- Differences between EJB3-based components and POJO-based components
		
			- A no-interface-view that makes local-business-view interfaces optional.
			- EJBs can now be packaged and deployed in a .war archive, and ejb-jar.xml is optional.
			- A portable, standard global JNDI name syntax for dynamic lookup.
			- Singleton session beans for shared state, with application start/stop callbacks.
			- Automatically created EJB timers and new calendar based expressions.
			- Asynchronous session bean invocations.


			CDI - vs - EJB:

				* @Stateless --> @Dependent (only)

				* @Singleton --> @ApplicationScoped (only)

				* @Stateful --> All scopes

        b.- Component definition
		        
			- Apply @Named:
			
				Expose the MANAGED BEAN to Facelets.

				    - Configure beans.xml

			+ Allows Decorators, Interceptors, Alternatives

        c.- Component lifecycle

			. Application
			- Session
			- Conversation
			- Request

			-->
			++ @ejb.Singleton
			++ @ejb.Startup

			- @PostConstruct
			- @PreDestroy

        d.- Component / contexts interaction:
	
            - Use of @Inject
	
				When the container scans the annotated class at deployment time, it will attempt to find a single bean that matches the bean
				type that is annotated.

		    - Understand interceptors (4 steps)

				* INTERCEPTOR:

					+ InterceptorBinding:

						@InterceptorBinding
						@Target({METHOD, TYPE})
						@Retention(RUNTIME)
						public @interface Logged { }

					+ Interceptor:

						@Logged 
						@Interceptor
						public class Logger {

							@AroundInvoke
							public Object logIt(InvocationContext context) throws Exception {
								...
						 	}
						}

					+ Usage:

						public class AccountManager {

							@Logged
							public void debitAccount(BigDecimal amount) { ... }

						}		

					+ Configuration:

						"beans.xml"

						  <interceptors>
							 <class>accounting.services.logging.Logger</class>
						  </interceptors>



		    - Understand events

				* EVENTS: (3 steps)

					+ Event definition:

						@Inject
						@Credit
						Event<PaymentEvent> creditEvent;

						@Inject
						@Debit
						Event<PaymentEvent> debitEvent;

					+ Trigger event:

						public void usage() {
	
							if (amount > 0) {
								PaymentEvent payment = new PaymentEvent(amount);
								creditEvent.fire(payment);
							}
						}

					+ Handler:

						public class MyEventHandler {

							public void anyPayment(@Observes @Any PaymentEvent event) {
								...
							}

							public void debitPayment(@Observes @Debit PaymentEvent event) {
								...
							}

							public void creditPayment(@Observes(notifyObserver=IF_EXISTS) @Debit PaymentEvent event) {

							}
						}


        e.- Use @Alternative (3 steps)

			+ Annotation:

				public class DecodeRing implements Decoder {

				}

				@Alternative
				public class SpecialDecoderRing implements Decoder {

				}

			+ Usage:

				public class MyClass {

					@Inject
					Decoder decoder;
		
				}

			+ beans.xml

				<alternative>
					<class>mypackage.SpecialDecoderRing</class>
				</alternative>



        f.- Use @Decorator (2 steps)

			+ Interface to Decorate

				public interface MyService {
					public void callMe();
					public void anotherMethod();
				}


			+ Implementation

				public class MyServiceImpl implements MyService {
					public void callMe() { ... }
					public void anotherMethod() { ... }
				}


			+ DECORATOR:

				@Decorator
				public abstract class MyServiceDecorator implements MyService {

					@Inject 
					@Delegate 
					@Any 
					MyService service;

					public void callMe() {
						// do whatever I like
						service.callMe();
					}
				}


			+ Configuration:

				"beans.xml"

				<decorators>
					<class>myPackage.MyServiceDecorator</class>
				</decorators>

        g.- Apply qualifier (3 steps)

			* QUALIFIER:

				1.- @Qualifier:

					@Qualifier
					@Target({Field, TYPE})
					@Retention(RUNTIME)
					@Documented
					public @interface Bad {
					}

				2.- Class to be qualified:

					@Bad
					public class BadMood implements MoodChecker {
		
						.....
					}

				3.- Usage:

					@Named
					@RequestScoped
					public class Mood {

						@Inject
						@Bad
						private MoodChecker moodChecker;
					}


        h.- Apply producer

			* PRODUCER: (2 types)

				(Used on:
					• primitives like int and long
					• parameterized types like Set<String>
					• raw types like Set)

				+ Producer Method:

					--> Method:

						@Produces
						@Preferred
						public PaymentStrategy getStrategicPaymentStrategy() {
							....
						} 

					--> Usage: !!! USUALLY on SINGLETONs

						@Inject
						@Preferred
						PaymentStrategy strategy;

				+ Producer Field:

					@Produces
					@AccountDB
					Connection connection = new Connection(id, pass);


        i.- Apply dispose

			+ Dispose Method

				@RequestScoped
				@Produces
				public Connection connect(User user) {
					return createConnection(user);
				}

				public void close(@Dispose Connection connection) {
					connection.close();
				}


        j.- Setting component and context variable in particular context using CDI APIs

			Using Inject and Scope

        k.- Implement the observer or observed pattern using CDI

			Use OBSERVERS!

        l.- JAAS security-related features, including (list not exhaustive)
            
			* AUTHENTICATION:
			* RESTRICTION:
            * SECURITY EVENTS:
	    	* BIND AUTHENTICATION METHODS: BASIC, FORM

			  - login.xhtml

				action="j_security_check"
				ID="j_username"
				ID="j_password"

			  - web.xml

				0.- login.xhtml

					<form method="post" action="j_security_check">

				1.- Login config:

					<login-config>
					   <auth-method>FORM</auth-method>
					   <form-login-config>
						<form-login-page>/faces/login.xhtml</form-login-page>
						<form-error-page>/faces/login.xhtml?failed=true</form-error-page>
					   </form-login-config>
					</login-config>

				2.- Add security constraint:

						IMPORTANT TO SECURE USING "/faces"

					<security-constraint> 
						<display-name>Full Application</display-name>

						<web-resource-collection> 
							 <web-resource-name>User Access</web-resource-name> 
							 <url-pattern>/faces/secure/*</url-pattern> 
						</web-resource-collection> 

						<auth-constraint> 
							 <role-name>RHLVL1</role-name> 
						</auth-constraint> 

					</security-constraint>

				3.- Add security domain:

					+ jboss-web.xml

						<security-domain>java:/jaas/JBTravel</security-domain>


4.- Access and manage identity information through API

	- Relevant methods from "HttpServletRequest":
		+ getRemoteUser
		+ isUserInRole
		+ getUserPrincipal


5.- Work with web/JSF components and web navigation

        a.- Understand Postback lifecycle

			An initial request occurs when a user makes a request for a page for the first time. 
			A postback request occurs when a user submits the form contained on a page that was previously loaded 
			into the browser as a result of executing an initial request.

			If the request for the page is a postback, a view corresponding to this page already exists in the FacesContext instance. 
			During this phase, the JavaServer Faces implementation restores the view by using the state information saved on the client or the server.

			- JSF rendering

			- JSF validation

			- JSF conversion

			- JSF error message

			- f:param:

				<h:outputFormat value="Hello, {0}! You are visitor number {1} to the page.">
					<f:param value="#{hello.name}" />
					<f:param value="#{bean.numVisitor}"/>
				</h:outputFormat>

        b.- Use exhaustive navigation capabilities using faces-config

			+ General structure "faces-config.xml" :

				<navigation-rule>
					<description></description
					<from-view-id></from-view-id>
					<navigation-case>
						<from-action></from-action>
						<from-outcome></from-outcome>
						<if></if>
						<to-view-id></to-view-id>
					</navigation-case>
				</navigation-rule>
		
			+ greetings.xhtml

				<h:commandButton id="submit" value="Submit" action="success" />

			+ faces-config.xml

				<navigation-rule>
					<from-view-id>/greetings.xhtml</from-view-id>
					<navigation-case>
						<from-action>success</from-action>
						<to-view-id>/response.xhtml</to-view-id>
					</navigation-case>
				</navigation-rule>


        c.- Handling of RESTFul approach with JSF2 (viewparam)


			+ Create a URL with parameters:

				
				<h:body>
					<h:form>
						<h:graphicImage url="#{resource['images:duke.waving.gif']}"
								        alt="Duke waving his hand"/>
						<h2>Hello, #{hello.name}!</h2>
						<p>I've made your
							<h:link outcome="personal" value="personal greeting" includeViewParams="true">
								<f:param name="Result" value="#{hello.name}" />
							</h:link>
						</p>
						<h:commandButton id="back" value="Back" action="index" />
					</h:form>
				</h:body>

				
			+ Receive a URL with parameters:

				<f:metadata>
					<f:viewParam name="Result" value="#{myBean.name}" />
				</f:metadata>

				Now can be use:

				<h:outputText value="Hello! #{myBean.name}" />


        d.- Use common Richfaces components

			+ <rich:panel>

				<rich:panel id="newMemberPanel" header="Register (Bean Validation example)"
					headerClass="header">
			
			+ <rich:collapsiblePanel>
			
			+ <rich:validator>, <rich:message>, <rich:messages>

				<h:outputLabel for="email" value="Email:"/>
				<h:inputText id="email" value="#{cc.attrs.member.email}">
					<rich:validator/>
				</h:inputText>
				<rich:message for="email" errorClass="invalid"/>

			+ <rich:dataTable>

				<rich:dataTable var="_member" value="#{members}" rendered="#{not empty members}"
						style="width : 100%">
										...
				</rich:dataTable>

			+ <a4j:commandButton>

				<a4j:commandButton id="register" action="#{memberRegistration.register}"
					value="Register"
					execute="newMemberPanel" render="newMemberPanel"/>

			+ <rich:popupPanel>
			
			+ <a4j:push>
			
			+ <rich:graphValidator>

				- JSF side:

					<rich:graphValidator value="#{invoice}" id="gv">
						<p>
							<rich:messages globalOnly="true" />
							<rich:messages for="gv"/>
						</p>
					</rich:graphValidator>


				- JAVA side:

					@AssertTrue(message = "You must specify a tax type when the invoice is taxable")
					public boolean isTaxTypeSpecified() {
						if (taxable && "".equals(taxType.trim()))
							return false;
						return true;
					}


        e.- Configure navigation based on application state

			"action" return new view.		

        f.- Set up JSF input validation
	
			* STANDARD VALIDATION:

				+ f:validateDoubleRange

				+ f:validateLength

				+ f:validateRegEx
	
				+ f:validateLongRange:

					<h:inputText id="quantity" size="4" value="#{item.quantity}"
						<f:validateLongRange minimum="1" />
					</h:inputText>
					<h:message for="quantity"/>

				+ f:validateRequired:

					Ensures the local value is not empty

				+ f:validateBean:
	
					Register a bean validator for the component

				+ f:validator:

					References a method that perform validation

					* JSF side:

						<h:inputText id="amount" value="#{myBean.amount}" validator="#{myBean.validateNonZero}" />

					* JAVA side:

						public void validateNonZero(FacesContext context, UIComponent component, Object componentValue) {
							if (componentValue != null && ((Long) componentValue) != 0L)
								return;

							UIInput inputComponent = (UIInput) component;
							inputComponent.setValid(false);

							context.addMessage(component.getClientId(context), new FacesMessage(
							"This number cannot be zero"));
						}


			* BEAN VALIDATION:

				+ @DecimalMax
				+ @DecimalMin
				+ @Max
				+ @Min
				+ @NotNull
				+ @Size
				+ @Digits
					@Digits(integer=6, fraction=2)
					BigDecimal price;
				+ @Pattern:
					@Pattern(regexp="\\(\\d{3}\\)\\d{3}-\\d{4}")
					String phoneNumber;

			* Null and Empty Strings

				To transform empty strings to NULL -->

					<context-param>
						<param-name>
						javax.faces.INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL
						</param-name>
						<param-value>true</param-value>
					</context-param>

        g.- Set up JSF input conversion

			+ f:converter:

				* Option 1:

					<h:inputText
						converter="javax.faces.convert.IntegerConverter" />
		
				* Option 2:

					<h:inputText value="#{loginBean.age}" />
						<f:converter converterId="Integer" />
					</h:inputText>
			
				* Option 3:

					<h:inputText value="#{loginBean.age}" >
						<f:converter binding="#{myBean.converter}" />
					</h:inputText>

			+ f:convertDateTime:

				<h:outputText value="#{myBean.myDate}">
					<f:convertDateTime pattern="dd/MM/yyyy" type="date" />
				</h:outputText>


				* Binding:

				+ JSF:			

					<h:inputText id="deliveryDate" value="#{myBean.deliveryDate}">
						<f:convertDateTime binding="#{myBean.dateConverter}" />
					</h:inputText>

				+ JAVA:

					private DateTimeConverter dateConverter;

					public setDateConverter(DateTimeConverter conv) {
						dateConverter = conv;
						dateConverter.setPattern("dd/MM/yyyy");
					}

					public getDateConverter() {
						return dateConverter;
					}

			+ f:convertNumber:

				<h:outputText value="#{myBean.myNumber}">
					<f:convertNumber type="currency" currencySymbol="€" minFractionDigits="2" maxFractionDigits="2" />
				</outputText><


        h.- Use Expression Language (EL)

		   * Reserved Words:	

				- and
				- or
				- not
				- eq
				- ne
				- lt
				- gt
				- le
				- ge
				- true
				- false
				- null
				- instanceof
				- empty
				- div
				- mod

		   * Getting values from REQUEST Params:

				${param['mycom.productId']}

		   * Getting values from REQUEST Scope:

				${requestScope['javax.servlet.servlet_path']}


        i.- Understand message bundle and locales

			+ SETTING RESOURCE BUNDLE:
			

				1.- DYNAMIC CONFIGURATION:

					f:loadBundle

						<f:loadBundle basename="dukesbookstore.web.messages.Messages" var="bundle" />				
			

				2.- MANUAL CONFIGURATION:

					2.1.- Configuring in faces-config.xml:

						<application>
							<resource-bundle>
								<base-name>com.text.locale</base-name>
								<var>greetings</var>
							</resource-bundle>
							<locale-config>
								<default-locale>en</default-locale>
							</locale-config>
						</application>


					2.2.- XHTML execution usage:

						<h:selectOneMenu value="#{login.currentLocale}"
										valueChangeListener="#{login.updateLocale}"
										onChange="submit()">

							<f:selectItems value="#{login.locales}" var="loc" />

						</h:selectOneMenu>


					2.3.- Java:

						public void updateLocale(ValueChangeEvent event) {
							
							String locale = event.getNewValue().toString();
							for (Entry<String, Locale> entry : locales) {
								if (entry.getValue().toString().equals(locale)) {

									FacesContext.getCurrentInstance().getViewRoot().setLocale(entry.getValue());

								}
							}
						}

					2.4.- In XHTML:

						<h:outputText value="#{greetings.greeting}" />



			+ RETRIEVING MESSAGES:

				- FROM SESSION:

					ResourceBundle bundle = (ResourceBundle)session.getAttribute("messages");
					bundle.getString("person.lastName");

				- JSF TAG:

					<h:outputLabel for="country" value="#{bundle["address.country"]}" />

        j.- Choose and configure JSF components

			* web.xml

				<servlet>
					<servlet-name>Faces Servlet</servlet-name>
					<servlet-class>javax.faces.webapp.FacesServlet</servlet-class>
					<load-on-startup>1</load-on-startup>
				</servlet>
				<servlet-mapping>
					<servlet-name>Faces Servlet</servlet-name>
					<url-pattern>/faces/*</url-pattern>
				</servlet-mapping>


			* SELECT_ONE_MENU:

				<h:selectOneMenu id="shippingOption"
						 required="true"
						 value="#{cashier.shippingOption}">
					<f:selectItem itemValue="2"
						  itemLabel="#{bundle.QuickShip}"/>
					<f:selectItem itemValue="5"
						  itemLabel="#{bundle.NormalShip}"/>
					<f:selectItem itemValue="7"
						  itemLabel="#{bundle.SaverShip}"/>
				 </h:selectOneMenu>


			* SELECT_MANY_CHECKBOX:

				<h:selectManyCheckbox id="newslettercheckbox"
							  layout="pageDirection"
							  value="#{cashier.newsletters}">
					<f:selectItems value="#{cashier.newsletterItems}"/>
				</h:selectManyCheckbox>


			* DATA_TABLE:

				<h:dataTable id="items"
						 columnClasses="list-column-center, list-column-left, list-column-right, list-column-center"
						 rowClasses="list-row-even, list-row-odd"
						 styleClass="list-background"
						 summary="#{bundle.ShoppingCart}"
						 value="#{cart.items}"
						 var="item">
					<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle.ItemQuantity}" />
					</f:facet>
					<h:inputText id="quantity" 
							 size="4"
							 value="#{item.quantity}"
							 title="#{bundle.ItemQuantity}">
						<f:validateLongRange minimum="1"/>
					</h:inputText>
					<h:message for="quantity"/>
					</h:column>
					<h:column>
					<f:facet name="header">
						<h:outputText value="#{bundle.ItemTitle}"/>
					</f:facet>
					<h:commandLink action="#{showcart.details}">
						<h:outputText value="#{item.item.title}"/>
					</h:commandLink>
					</h:column>
					...
					<f:facet name="footer"
					<h:panelGroup>
						<h:outputText value="#{bundle.Subtotal}"/>
						<h:outputText value="#{cart.total}" />
						<f:convertNumber currencySymbol="$" type="currency" />
						</h:outputText>
					</h:panelGroup>
					</f:facet>
					<f:facet name="caption">
					<h:outputText value="#{bundle.Caption}"/>
					</f:facet>
				</h:dataTable>


        k.- Efficiently handle exceptions:


					1.- Register a Exception Handler Factory class:

						<faces-config version="2.1"
							xmlns="http://java.sun.com/xml/ns/javaee"
							xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
							xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facesconfig_2_1.xsd">
		
							<factory>
							  <exception-handler-factory>
								com.wazi.guessnumber.exceptions.CustomExceptionHandlerFactory
							  </exception-handler-factory>
							</factory>
		
							<!—other configurations here -->
						</faces-config>


					2.- Create the Exception Handler Factory:

						import javax.faces.context.ExceptionHandler;
						import javax.faces.context.ExceptionHandlerFactory;

						public class CustomExceptionHandlerFactory extends ExceptionHandlerFactory {
						 
						  private ExceptionHandlerFactory parent;
						 
						  public CustomExceptionHandlerFactory(ExceptionHandlerFactory parent) {
							this.parent = parent;
						  }
						 
						  @Override
						  public ExceptionHandler getExceptionHandler() {
							ExceptionHandler result = new CustomExceptionHandler(parent.getExceptionHandler());
							return result;
						  }
						}


					3.- Create the Custom Exception Handler:

							public class CustomExceptionHandler extends ExceptionHandlerWrapper {
							  private ExceptionHandler wrapped;
							 
							  
							  public CustomExceptionHandler(ExceptionHandler wrapped) {
								this.wrapped = wrapped;
							  }
							 
							  @Override
							  public ExceptionHandler getWrapped() {
								return wrapped;
							  }

							  @Override
							  public void handle() throws FacesException {
								Iterator iterator = getUnhandledExceptionQueuedEvents().iterator();
		
								while (iterator.hasNext()) {
								  ExceptionQueuedEvent event = (ExceptionQueuedEvent) iterator.next();
								  ExceptionQueuedEventContext context = (ExceptionQueuedEventContext)event.getSource();
							 
								  Throwable throwable = context.getException();
								  
								  FacesContext fc = FacesContext.getCurrentInstance();
								  
								  try {
									  Flash flash = fc.getExternalContext().getFlash();
									  
									  // Put the exception in the flash scope to be displayed in the error 
									  // page if necessary ...
									  flash.put("errorDetails", throwable.getMessage());
									  
									  System.out.println("the error is put in the flash: " + throwable.getMessage());
									  
									  NavigationHandler navigationHandler = fc.getApplication().getNavigationHandler();
									  
									  navigationHandler.handleNavigation(fc, null, "error?faces-redirect=true");
									  
									  fc.renderResponse();
								  } finally {
									  iterator.remove();
								  }
								}
		
								// Let the parent handle the rest
								getWrapped().handle();
							  }
							}

        l.- Implement success and error messages

			* MESSAGE:

				+ JSF side:

					<h:inputText id="amount" value="#{myBean.amount}" validator="#{myBean.validateNonZero}" />

					<h:message for="amount" showDetail="true" showSummary="false"
						styleClass="error" />

				+ JAVA side:

					public void validateNonZero(FacesContext context, UIComponent component, Object componentValue) {
						if (componentValue != null && ((Long) componentValue) != 0L)
							return;

						UIInput inputComponent = (UIInput) component;
						inputComponent.setValid(false);

						context.addMessage(component.getClientId(context), new FacesMessage(
						"This number cannot be zero"));
					}

			* MESSAGES:

				+ JSF side:

					<h:messages globalOnly="true" styleClass="error"
						showSummary="true" showDetail="false" />

				+ JAVA side:

					FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("my summary error text", "my detail error text"));

6.- Expose components to external and legacy interfaces

        a.- Expose components as SOA-style web services

			@Stateless
			@WebService(
						name="ProfileMgmt",
						targetNamespace = "http://org.jboss.ws/samples/retail/profile",
						serviceName = "ProfileMgmtService")
			@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)

			public class ProfileMgmtBean {
				@WebMethod
				public @WebResult(name="discountResp") DiscountResponse getCustomerDiscount (
					@WebParam(name="discountReq") DiscountRequest request) {
					return new DiscountResponse(request.getCustomer(), 10.00);
				}
			}


        b.- Consume SOA-style web services

			Use Jboss Web Tool

		c.- Expose components as Restful services

			1.- Create subclass Application:

				@ApplicationPath("/rest")
				public class JaxRsActivator extends Application {
						/* class body intentionally left blank */
				}

			2.- Create REST service

				@Path("/library")
				public class Library {

					@GET
					@Path("/books")
					public String getBooks() {...}

					@GET
					@Path("/book/{isbn}")
					@Produces(MediaType.APPLICATION_XML)
					public Book getBook(@PathParam("isbn") String id) {
						// search my database and get a string representation and return it
					}

					@PUT
					@Path("/book/{isbn}")
					public void addBook(@PathParam("isbn") String id, @QueryParam("name") String name)
					{...}

					@POST
					@Consumes("application/x-www-form-urlencoded"  --- MediaType.APPLICATION_FORM_URLENCODED)
					public void save(@FormParam("managername") String managername) {
						// Store the value
						...
					}
				}

				** PUT - http://example.com/rs/library/book/9871234?name=JBossInAction



        d.- Consume external Restful services

			Use Jboss Web Tool


        e.- Consume external JMS-based messages

			HORNETQ:
			--------

				http://www.jboss.org/hornetq/docs

			1.- Change the standalone-full.xml

			2.- Adjust MDB properties:

				destination 
					Mandatory. The JNDI name - in the java:/ namespace.

				destinationType 
					javax.jms.Queue or javax.jms.Topic

				acknowledgeMode 
					Auto_acknowledge or Dups_ok_acknowledge

				subscriptionDurability 
					NonDurable or Durable

				subscriptionName 
					Subscription name of the topic

				clientID 
					The client id of the connection	

			3.- Update the WEB-INF/hornetq-jms.xml

				<jms-destination>
					<jms-queue name="GreetingsQ">
						<entry name="/queue/GreetingsQ" />
					</jms-queue>
				</jms-destination>



7.- Security (4 steps)

        Set up JAAS
        Secure methods
        Secure paths
        Create an authentication form
        Set up roles mapping


			  - web.xml

				0.- login.xhtml

					<form method="post" action="j_security_check">

						<h:inputText id="j_username" />
						<h:inputSecret id="j_password" />

						<h:commandButton value="Login" />

					</form>

				1.- Login config:

					<login-config>
					   <auth-method>FORM</auth-method>
					   <form-login-config>
						<form-login-page>/faces/login.xhtml</form-login-page>
						<form-error-page>/faces/login.xhtml?failed=true</form-error-page>
					   </form-login-config>
					</login-config>

				2.- Add security constraint:

						IMPORTANT TO SECURE USING "/faces"

					<security-constraint> 
						<display-name>Full Application</display-name>

						<web-resource-collection> 
							 <web-resource-name>User Access</web-resource-name> 
							 <url-pattern>/faces/secure/*</url-pattern> 
						</web-resource-collection> 

						<auth-constraint> 
							 <role-name>RHLVL1</role-name> 
						</auth-constraint> 

					</security-constraint>

				3.- Define security roles:

					<security-role>
						<description>LOW LEVEL ACCESS</description>
						<role-name>RHLVL1</role-name>
					</security-role>

				4.- Add security domain:

					+ jboss-web.xml

						<security-domain>java:/jaas/JBTravel</security-domain>



