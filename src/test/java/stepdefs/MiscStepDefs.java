package stepdefs;

import testprojectcore.driverutil.PageObjectFactory;
import io.cucumber.java.en.Given;
import pageobjects.MiscPO;
import testprojectcore.testcontext.TestContext;

public class MiscStepDefs {

    private TestContext scenarioContext;
    private MiscPO miscPO;


    public MiscStepDefs(TestContext scenarioContext) {
        this.scenarioContext = scenarioContext;
        miscPO = PageObjectFactory.createClass(MiscPO.class);
    }


    @Given("I test something")
    public void iTestAsdf() {
    }
}
