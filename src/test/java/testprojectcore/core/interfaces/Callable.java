package testprojectcore.core.interfaces;


import org.openqa.selenium.WebElement;


/**
 * Interface to define methods to be passed as parameters to
 * other methods
 *
 * @author Eren Demirel <eren.demirel@asseco-see.com.tr>
 */
public interface Callable {

    boolean execute(String nameOfOperationRoughly, WebElement webElement);

}
