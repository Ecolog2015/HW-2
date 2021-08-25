import org.junit.After;
import org.junit.Before;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HwYandex {
    protected static WebDriver driver;
    private Logger logger = LogManager.getLogger(HwYandex.class);

    static By productCard = By.cssSelector("li.box-inline.v-top");
    //Локатор карточки продукта
    static By compareButton = By.cssSelector("li.box-inline.v-top [type = \"checkbox\"]");
    //Локатор кнопки сравнения
    static By alertWindow = By.xpath("//*[contains(text(), 'К сравнению был добавлен следующий товар')]");
    //Локатор всплывающего окна
    static By foundCards = By.xpath("//*[contains(text(), 'Перфоратор ')]");
    //Локатор для поиска карточек на странице сравнения


    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        logger.info("Драйвер поднят");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @After
    public void setDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void voltTest() throws InterruptedException {
        driver.get("https://www.220-volt.ru/catalog/perforatory/");
        logger.info("Сайт www.220-volt.ru открыт");

        driver.findElement(By.xpath("//input[@id='producer_16']")).click();
        logger.info("Нажат чекбокс Makita");

        driver.findElement(By.xpath("//input[@id='producer_473']")).click();
        logger.info("Нажат чекбокс Зубр");

        driver.findElement(By.xpath("//input[@id='filterSubm']")).click();
        logger.info("Нажата кнопка \"Подобрать модель\"");

        driver.findElement(By.cssSelector(".select2-selection__arrow")).click();
        logger.info("Нажата сортировка");

        driver.findElement(By.cssSelector(".listing-select-icon1")).click();
        logger.info("Выбрана по цене (min->max)");

        cardProducts("MAKITA");
        cardProducts("ЗУБР");

        driver.get("https://www.220-volt.ru/compare/");
        logger.info("Перешли на страницу сравнения");

        List<WebElement> cardsCompare = driver.findElements(foundCards);
        logger.info("Карточек в сравнении {}", cardsCompare.size());

        Assert.assertTrue(cardsCompare.size()==2);
        //Thread.sleep(3000);
    }

    public void cardProducts(String product) {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        List<WebElement> productLists = driver.findElements(productCard);
        logger.info("Получен лист карточек продуктов");

        for (WebElement productList : productLists) {

            if (productList.getText().toUpperCase().contains(product.toUpperCase())) {

                Actions actions = new Actions(driver);
                actions.moveToElement(productList);
                logger.info("Нашли карточку продукта {}",product);

                (productList).findElement(compareButton).click();
                logger.info("Нажали на кнопку сравнения");

                wait.until(ExpectedConditions.visibilityOfElementLocated(alertWindow));
                Assert.assertTrue(driver.findElement(alertWindow).isDisplayed());
                logger.info("Всплывающее окно отобразилось");

                driver.findElement(By.xpath("//a[contains(text(),'← Продолжить просмотр')]")).click();
                logger.info("Нажали кнопку Продолжить просмотр");
            }

            if (productList == productLists.get(productLists.size() - 1)) {
                logger.info("Не найдена карточка продукта ", product);
                Assert.assertTrue(false);
            }
        }
    }
}

