# **Automated Integration Tests**


Project that includes automated user acceptance tests. The aim of this project is to decrease manuel testing that are being done, make use of mock servers and data driven tests.

For now only used for Delivery provider Integrations(DPI)

## Installation


It is ready to use once pulled. The suggested IDE is IntelliJ Idea. Before pulling the project make sure you have:

- OpenJDK 15 installed,
- Maven installed, 
- Annotation processors enabled since project uses Lombok,
- Browsers matching with the Selenium drivers inside the project source installed(_you may want to contact QA team for browser version info_), 
- Appium server installed **only if** you are going to run mobile tests.

## Executing the Tests

### From Your Local via IDE
You can use the plugins that IDEs provide to run Cucumber feature files and scenarios via mouse click. To pass command line parameters in this case, you should edit the configurations and add parameters to VM options for that test run, from your IDE. The required configuration parameters are:

- **Main Class**: io.cucumber.core.cli.Main
- **Glue**: stepdefs testprojectcore.stepdefs
- **Program arguments**: `IntelliJ Idea only`  --plugin org.jetbrains.plugins.cucumber.java.run.CucumberJvm5SMFormatter

When Cucumber tags isn't declared explicitly, only the tests annotated with the tag inside @CucumberOptions in src/test/java/testprojectcore/runners/RunCucumberTest.java will be run
### From Anywhere Else
The best and the universal way of executing the tests in the framework is running a **Maven command**. The required Maven command is below:


```bash
mvn test 
-Dcucumber.filter.tags=@<Cucumber Tags>
-Dconfigfilepath=<Path to Test Configuration File>
-Dbrowser=<Browser Name> 
-Denv=<Environment(e.g "Test", "Staging")>
-Dgrid=<Grid IP>
```

#### System Parameters:

- **configfilepath**: `Optional`. Parameter to declare the path of a [test configuration file](https://github.com/lineten/nova-cucumber-dpi-tests/blob/master/src/test/resources/config/config.txt). When not declared, it uses the file under _/src/test/resources/config/config.txt_ by default
- **browser**: `Optional`. When not declared, it will take the value from the [test configuration file](https://github.com/lineten/nova-cucumber-dpi-tests/blob/master/src/test/resources/config/config.txt). When it is not present in config file also, it will use the default browser declared in [Property file for web test configuration](https://github.com/lineten/nova-cucumber-dpi-tests/blob/master/src/test/resources/propertyfiles/webconfig.properties). And eventually if it is not present in property file either, it will use Chrome. Case insensitive but use one of browser names while declaring: _"Chrome", "Firefox", "Safari", "Edge", "Opera"_ 
- **env**: `Mandatory`. Environment. Case insensitive. Please be sure that the same name is used inside the test code so you may want to contact the QA team
- **cucumber.filter.tags**: `Optional`. When not declared explicitly, only the tests annotated with the tag inside @CucumberOptions in _src/test/java/testprojectcore/runners/RunCucumberTest.java_ will be run
- **grid**: `Optional`. Selenium Grid IP(if exists)


#### An Example Run Command:
```bash
mvn test -Dcucumber.filter.tags=@wip -Dbrowser=chrome -Denv=test 
```

---


# About the Project

The project consists of two parts, core and tests. Inside core, there are things related to framework. And inside tests, there are tests related to integration testing.

## About the Test Framework
The framework is written in **Java**. It uses **JUnit** and [Cucumber](https://cucumber.io/) as runners and is executed via [Maven Surefire plugin](https://maven.apache.org/surefire/maven-surefire-plugin/). Since Cucumber is in use, you may use feature files to organize the tests, design and write the tests with **BDD** in _Given-When-Then_ style, either with declarative or imperative programming


For API testing, you can use any kind of HTTP client but the ones that are already in use are [Apache Http Client](https://hc.apache.org/httpcomponents-client-4.5.x/index.html) and [Rest-assured](https://github.com/rest-assured/rest-assured)

For UI testing, **Selenium** and **Appium** are integrated. The framework encourages the usage of **Page Object Model** pattern as test design pattern and [PageFactory.initElements()](https://github.com/SeleniumHQ/selenium/wiki/PageFactory) to initialize the web elements inside page objects, so you may use POM pattern and initElements()



### Sharing the Scenario State
You can share the state. It is achieved by dependency injection of type constructor injection via [PicoContainer](http://picocontainer.com/). For further information, please see the notes in [TestContext](https://github.com/lineten/nova-cucumber-dpi-tests/blob/master/src/test/java/testprojectcore/testcontext/TestContext.java). You can also take a look at the [Example Test](https://github.com/lineten/nova-cucumber-dpi-tests/blob/master/src/test/resources/cucumber.features/Example.feature) to understand how it works

### Reporting
Integrated reporting tools are: [Allure](https://docs.qameta.io/allure/), [Cucumber Reports](https://reports.cucumber.io/), [Cucumber Reporting](https://github.com/damianszczepanik/cucumber-reporting)