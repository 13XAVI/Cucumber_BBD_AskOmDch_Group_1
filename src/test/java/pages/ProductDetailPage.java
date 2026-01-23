package Page;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductDetailPage {
    private final WebDriver driver;
    private final String productName;
    private WebDriverWait wait;

    public ProductDetailPage(WebDriver driver, WebDriverWait wait, String productName) {
        this.driver = driver;
        this.productName = productName;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    public boolean productNameCheck(){
        return productName.equals(driver.findElement(By.className("product_title")).getText());
    }
    public boolean addSelectedProductToCart(int quantity){
        driver.findElement(By.name("quantity")).clear();
        driver.findElement(By.name("quantity")).sendKeys(String.valueOf(quantity));
        driver.findElement(By.name("add-to-cart")).click();
        String returnMessage = driver.findElement(By.className("woocommerce-message")).getText();
        returnMessage = returnMessage.substring(returnMessage.indexOf("T") + 2);
        if(quantity > 1) return returnMessage.contains("“"+ productName +"” have been added to your cart.");
        else return returnMessage.contains("“"+ productName +"” has been added to your cart.");
    }
    public boolean additionalInformation(){
        WebElement additionalInformation = driver.findElement(By.className("additional_information_tab"));
        additionalInformation.click();
        return additionalInformation.getAttribute("class").contains("active");
    }
    public boolean review(){
        WebElement review = driver.findElement(By.className("reviews_tab"));
        review.click();
        return review.getAttribute("class").contains("active");
    }
    public void reviewComment(String comment){
        driver.findElement(By.name("comment")).sendKeys(comment);
    }
    public void name(String authorName){
        if(!driver.findElements(By.name("author")).isEmpty()) driver.findElement(By.name("author")).sendKeys(authorName);
    }
    public void email(String email){
        if(!driver.findElements(By.name("email")).isEmpty()) driver.findElement(By.name("email")).sendKeys(email);
    }
    public void starRating(int rating){
        driver.findElement(By.className("star-" + rating)).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.attributeContains(By.className("stars"), "class","selected"));
    }
    public void saveNameAndEmail(){
        driver.findElement(By.id("wp-comment-cookies-consent")).click();
    }
    public String submitComment(){
        driver.findElement(By.id("submit")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try{
            wait.until(ExpectedConditions.alertIsPresent());
            var alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.accept();
            return alertText;
        } catch (TimeoutException e0) {
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".description p")));
                return driver.findElement(By.cssSelector(".description p")).getText();
            } catch (TimeoutException e) {
                try {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".wp-die-message p")));
                    return driver.findElement(By.cssSelector(".wp-die-message p")).getText();
                } catch (TimeoutException e2) {
                    return e2.getMessage();
                }

            }
        }

    }

}
