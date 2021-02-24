package testprojectcore.core;

import testprojectcore.core.interfaces.Callable;
import org.openqa.selenium.WebElement;


/**
 * Invoker class to allow execution of different concrete commands to implement command pattern
 * while decoupling the client and the receiver
 *
 * Note that in this project concrete classes for commands will not be used since they are unnecessary
 *
 * @author Eren Demirel
 */
public class Invoker implements Callable {

    @Override
    public boolean execute(String nameOfOperationRoughly, WebElement webElement) {
        return DriverUtils.webElementoperations(nameOfOperationRoughly, webElement);
    }
}
