import org.junit.After;
import org.junit.Before;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Hw {
    protected static WebDriver driver;
    private Logger logger = LogManager.getLogger(Hw.class);

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        logger.info("Драйвер поднят");
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    }

    @After
    public void setDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void checkOtus() {
        logger.info("Проверка checkOtus");
        driver.get("https://otus.ru/");
        logger.info("Сайт otus.ru открыт");
        driver.manage().window().fullscreen();
        //driver.manage().window().maximize();
        logger.info("Открыт сайт в полном окне");
        driver.findElement(By.xpath("//*[contains(text(), 'Контакты')]")).click();
        logger.info("Открыта страница Контакты");
        Assert.assertEquals("125167, г. Москва, Нарышкинская аллея., д. 5, стр. 2, тел. +7 499 938-92-02", driver.findElement(By.cssSelector("#__next > div.sc-2em8v9-0.etvoKo > div.sc-2em8v9-1.nmfgI > div.sc-1hmcglv-0.iiFycX > div:nth-child(3) > div.c0qfa0-5.cXQVNI")).getText());
        logger.info("Текст совпадает.Тест пройден");
        String actual = driver.getTitle();
        Assert.assertEquals("Контакты | OTUS", actual);
        logger.info("title проверен");
    }

    @Test
    public void checkTele() throws InterruptedException {
        logger.info("Проверка checkTele");
        driver.get("https://msk.tele2.ru/shop/number");
        logger.info("Сайт tele2.ru открыт");
        driver.findElement(By.cssSelector("#searchNumber")).sendKeys("97");
        Thread.sleep(2000);
        //не совсем понимаю, почему если делать так, не работает,а с слипом работает(
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[3]/div[1]/div[1]/div[2]"))).click();
        logger.info("Тест пройден");
    }

    @Test
    public void checkOtusFAQ() {
        logger.info("Проверка checkOtusFAQ");
        driver.get("https://otus.ru/");
        logger.info("Сайт otus.ru открыт");
        driver.findElement(By.xpath("//*[contains(text(), 'FAQ')]")).click();
        logger.info("Открыта страница FAQ");
        driver.findElement(By.xpath("//*[contains(text(), 'Где посмотреть программу интересующего курса?')]")).click();
        logger.info("Раскрыт вопрос");
        Assert.assertEquals("Программу курса в сжатом виде можно увидеть на странице курса после блока с преподавателями. Подробную программу курса можно скачать кликнув на “Скачать подробную программу курса”", driver.findElement(By.xpath("//div[@class='faq-question__answer js-faq-answer']")).getText());
        logger.info("Tекст совпадает. Тест пройден.");
    }

    @Test
    public void checkOtusSubscription() {
        logger.info("Проверка checkOtusSubscription");
        driver.get("https://otus.ru/");
        logger.info("Сайт otus.ru открыт");
        driver.findElement(By.cssSelector("body > div.body-wrapper > div > footer > div > div > div.footer2__content > div > div:nth-child(3) > form > input.input.footer2__subscribe-input")).sendKeys("test65464655@test.ru");
        logger.info("Введена почта");
        driver.findElement(By.xpath("/html/body/div[1]/div/footer/div/div/div[1]/div/div[3]/form/button")).click();
        logger.info("Нажата кнопка \"Подписаться\"");
        Assert.assertEquals("Вы успешно подписались", driver.findElement(By.cssSelector("body > div.body-wrapper > div > footer > div > div > div.footer2__content > div > div:nth-child(3) > p.subscribe-modal__success")).getText());
        logger.info("Проверка об успешной подписке");
    }
}



