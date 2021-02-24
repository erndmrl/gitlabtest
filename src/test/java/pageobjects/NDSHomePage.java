package pageobjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import testprojectcore.core.Driver;
import testprojectcore.core.DriverUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NDSHomePage extends Driver {

    @FindBy(name = "login")
    WebElement usernameFormInput;

    @FindBy(name = "password")
    WebElement passwordFormInput;

    @FindBy(xpath = "//button[@type='submit'][contains(.,'Sign In')]")
    WebElement submitButton;

    @FindBy(css = ".v-data-table__wrapper")
    WebElement dataTable;

    @FindBy(css = "tbody")
    WebElement dataTableBody;

    @FindBy(css = ".v-data-table-header")
    WebElement dataTableHeader;

    @FindBy(xpath = "(//input[@type='checkbox'])[1]")
    List<WebElement> firstTableEntryCheckbox;

    @FindBy(css = "tr:nth-child(1) .status-color")
    List<WebElement> firstTableEntryStatusBox;

    @FindBy(css = ".fs-12 > strong")
    WebElement dateIndicator;

    @FindBy(xpath = "//i[@class='v-icon notranslate mr-2 fas fa-calendar-alt theme--light']")
    WebElement dateTimePicker;

    @FindBy(xpath = "(//i[contains(.,'chevron_right')])[5]")
    WebElement nextMonthButton;

    @FindBy(xpath = "(//div[contains(@class,'v-btn__content')])[114]")
    WebElement thirtiethDayOfMonth_1;

    @FindBy(xpath = "(//div[contains(.,'30')])[36]")
    WebElement thirtiethDayOfMonth_2;

    @FindBy(xpath = "//div[@class='v-btn__content'][contains(.,'30')]")
    WebElement thirtiethDayOfMonth_3;

    @FindBy(xpath = "(//div[@class='v-btn__content'][contains(.,'30')])[2]")
    WebElement thirtiethDayOfMonth_4;

    @FindBy(xpath = "(//div[@class='v-btn__content'][contains(.,'28')])[4]")
    WebElement twentyeighthDayOfMonth;

    @FindBy(css = ".v-card__actions > .primary > .v-btn__content")
    WebElement dateTimePickerSubmit;

    @FindBy(xpath = "//header/div[1]/button[3]")
    WebElement refreshContentButton;

    @FindBy(xpath = ".v-data-footer__icons-after .v-icon")
    WebElement nextPageButton;

    @FindBy(css = ".v-btn__loader")
    WebElement refreshContentButtonAnimated;


    public NDSHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void navigateToHomePage() {
        driver.get("https://nova2-test.noqu.delivery/Login");
    }

    public void login(String username, String password) {
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(usernameFormInput), 10, driver);
        usernameFormInput.sendKeys(username);
        passwordFormInput.sendKeys(password);
        submitButton.click();
    }

    public String getASpecificJobIdTextFromJobsTable(String jobId) {
        By jobIdText = By.xpath("//span[contains(.,'" + jobId + "')]");
        return driver.findElement(jobIdText).getText();
    }

    public void waitUntilTableHeaderIsVisible() {
        DriverUtils.waitUntil(ExpectedConditions.visibilityOf(dataTableHeader), 10, driver);
    }

    private int getTheSizeOfTheTable() {
        List<WebElement> rowsInDataTable = driver.findElements(By.tagName("tr"));   //There is only one table in the page
        return rowsInDataTable.size();
    }


    public int locateTheRowThatASpecificJobIdIsIn(String jobId) throws org.openqa.selenium.NoSuchElementException, StaleElementReferenceException, InterruptedException {
        DriverUtils.waitUntil(firstTableEntryCheckbox.size() > 0, 10, driver);
        DriverUtils.waitUntil(firstTableEntryStatusBox.size() > 0, 10, driver);
        int i = 1;
        boolean flag = false;
        By jobIdColumn = By.cssSelector("tr:nth-child(" + i + ")> .job-id-column span");
        for (i = 1; i < getTheSizeOfTheTable(); i++) {
            if (driver.findElement(jobIdColumn).getText().equals(jobId)) {
                flag = true;
                return i;
            }
        }
        if (!flag) {
            goToNextMonth();
            locateTheRowThatASpecificJobIdIsIn(jobId);
        }
        return i;
    }

    public String getJobStatusAccordingToRowNumber(int rowNum) throws InterruptedException {
        By jobStatus = By.cssSelector("tbody > tr:nth-child(" + rowNum + ") > .status-column");
        while (!driver.findElement(jobStatus).isDisplayed()){
            clickNextPage();
            Thread.sleep(2000);     //Unfortunately
        }
        return driver.findElement(jobStatus).getText();
    }

    public void clickNextPage(){
        nextPageButton.click();
    }

    public void refreshTableData() {
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(refreshContentButton), 10, driver);
        DriverUtils.getActions(driver).moveToElement(refreshContentButton).click().build().perform();
        DriverUtils.waitUntil(ExpectedConditions.visibilityOf(refreshContentButtonAnimated), 10, driver);
    }

    public void goToDateWhereJobSupposedToBeIn(Date jobDate) throws Exception {
        String uiFormat = new SimpleDateFormat("dd/MM/yyyy").format(jobDate);

        while (!dateIndicator.getText().contains(uiFormat)) {
            DriverUtils.waitUntil(getTheSizeOfTheTable() > 1, 10, driver);  //Wait until the table is populated
            DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(dateTimePicker), 5, driver);
            dateTimePicker.click();
            DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(nextMonthButton), 5, driver);
            nextMonthButton.click();
            if (dateIndicator.getText().matches(" to [0-9]{2}/02/2021")) {    //Then it is February
                DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(twentyeighthDayOfMonth), 5, driver);
                twentyeighthDayOfMonth.click();
            } else {
                try {
                    thirtiethDayOfMonth_1.click();
                } catch (NoSuchElementException se) {
                    thirtiethDayOfMonth_2.click();
                } catch (ElementNotInteractableException ene) {
                    thirtiethDayOfMonth_3.click();
                }
                catch (Exception e) {
                    thirtiethDayOfMonth_4.click();
                }
            }
            DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(dateTimePickerSubmit), 5, driver);
            dateTimePickerSubmit.click();
            Thread.sleep(500);  //Unfortunately
        }
    }


    public void goToNextMonth() throws InterruptedException {

        DriverUtils.waitUntil(getTheSizeOfTheTable() > 1, 10, driver);  //Wait until the table is populated
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(dateTimePicker), 5, driver);
        dateTimePicker.click();
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(nextMonthButton), 5, driver);
        nextMonthButton.click();
        if (dateIndicator.getText().matches(" to [0-9]{2}/02/2021")) {    //Then it is February
            DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(twentyeighthDayOfMonth), 5, driver);
            twentyeighthDayOfMonth.click();
        } else {
            try {
                thirtiethDayOfMonth_1.click();
            } catch (NoSuchElementException se) {
                thirtiethDayOfMonth_2.click();
            } catch (ElementNotInteractableException ene) {
                thirtiethDayOfMonth_3.click();
            }
            catch (Exception e) {
                thirtiethDayOfMonth_4.click();
            }
        }
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(dateTimePickerSubmit), 5, driver);
        dateTimePickerSubmit.click();
        Thread.sleep(500);  //Unfortunately
    }

}
