
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.net.URL;
public class Test1 {

    public static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    public static final String AUTOMATE_KEY =System.getenv("BROWSERSTACK_ACCESS_KEY");
    public static final String buildname=System.getenv("BROWSERSTACK_BUILD_NAME");
  //  public static final String browserstackLocal = System.getenv("BROWSERSTACK_LOCAL");
    public static final String browserstackLocalIdentifier = System.getenv("BROWSERSTACK_LOCAL_IDENTIFIER");
    public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);  // A pool of 2 threads are being created here. You can change this as per your parallel limit
        Set<Callable<String>> callables = new HashSet<Callable<String>>();
        Test1 obj1 = new Test1();
        callables.add(new Callable<String>() {
            public String call() throws Exception {
                Hashtable<String, String> capsHashtable = new Hashtable<String, String>();
                capsHashtable.put("browser", "chrome");
                capsHashtable.put("browser_version", "latest");
                capsHashtable.put("os", "Windows");
                capsHashtable.put("os_version", "11");
                capsHashtable.put("build", buildname);
                capsHashtable.put("name", "Thread 1");
                obj1.executeTest(capsHashtable);
                return "Task 1 completed";


            }
        });
        callables.add(new Callable<String>() {
            public String call() throws Exception {
                Hashtable<String, String> capsHashtable = new Hashtable<String, String>();
                capsHashtable.put("browser", "Edge");
                capsHashtable.put("browser_version", "96.0");
                capsHashtable.put("os", "Windows");
                capsHashtable.put("os_version", "11");
                capsHashtable.put("build", "Amazon Webpage Test");
                capsHashtable.put("name", "Thread 2");
                obj1.executeTest(capsHashtable);
                return "Task 2 completed";
            }
        });
        callables.add(new Callable<String>() {
            public String call() throws Exception {
                Hashtable<String, String> capsHashtable = new Hashtable<String, String>();
                capsHashtable.put("browser", "safari");
                capsHashtable.put("browser_version", "latest");
                capsHashtable.put("os", "OS X");
                capsHashtable.put("os_version", "Big Sur");
                capsHashtable.put("build", "Amazon Webpage Test");
                capsHashtable.put("name", "Thread 3");
                obj1.executeTest(capsHashtable);
                return "Task 3 completed";
            }
        });
        List<Future<String>> futures;
        futures = executorService.invokeAll(callables);
        for(Future<String> future : futures){
            System.out.println("future.get = " + future.get());
        }
        executorService.shutdown();
    }
public void executeTest(Hashtable<String, String> capsHashtable) {
    String key;
    DesiredCapabilities caps = new DesiredCapabilities();
    // Iterate over the hashtable and set the capabilities
    Set<String> keys = capsHashtable.keySet();
    Iterator<String> keysIterator = keys.iterator();
    while (keysIterator.hasNext()) {
        key = keysIterator.next();
        caps.setCapability(key, capsHashtable.get(key));
    }
    WebDriver driver;
    try {
        driver = new RemoteWebDriver(new URL(URL), caps);
        final JavascriptExecutor jse = (JavascriptExecutor) driver;
        try {
        	 driver.get("https://www.amazon.in/");
             System.out.println("after launch");
             System.out.println(driver.getTitle());
             driver.manage().window().maximize();
             WebDriverWait w= new WebDriverWait(driver, 10);
               
             Thread.sleep(2000);
             driver.findElement(By.id("twotabsearchtextbox")).click();
             driver.findElement(By.id("twotabsearchtextbox")).sendKeys("Iphone x");
             driver.findElement(By.id("twotabsearchtextbox")).sendKeys(Keys.ENTER);
             System.out.println("page title = "+driver.getTitle());
             w.until(ExpectedConditions.titleContains("Iphone"));
             //w.until(ExpectedConditions.urlContains("iphone"));
             
             driver.findElement(By.id("a-autoid-0-announce")).click();
             driver.findElement(By.xpath("//div[@class='a-popover-inner']/ul/li[3]")).click();
            
             System.out.println("aa");
             WebElement element = driver.findElement(By.xpath("//span[@class='a-button-inner']/span/span[2]"));
             //w.until(ExpectedConditions.textToBePresentInElement(element, "High to Low"));
             Thread.sleep(2000);
             System.out.println("after");
             WebElement os=driver.findElement(By.xpath("//ul[@aria-labelledby='p_n_operating_system_browse-bin-title']/li[@aria-label='iOS']"));
            System.out.println("jjj");
             w.until(ExpectedConditions.visibilityOf(os));
             System.out.println("kk");
             driver.findElement(By.xpath("//ul[@aria-labelledby='p_n_operating_system_browse-bin-title']/li[@aria-label='iOS']"));
             System.out.println("b");
         
             //System.out.println(driver.getTitle());

             List<WebElement> name = driver.findElements(By.xpath("//div[@class='sg-row']/div[2]/div/div/div/h2/a"));
             System.out.println("name size= "+name.size());

             List<WebElement> price= driver.findElements(By.xpath("//span[@class='a-price-whole']"));
             System.out.println(price.size());

             List<WebElement> link = driver.findElements(By.xpath("//div[@class='sg-row']/div[2]/div/div/div/h2/a"));

             for (int i = 0; i < price.size(); i++) {
                 String link_text = link.get(i).getAttribute("href");
                 System.out.println(name.get(i).getText() + " " + price.get(i).getText() + " " + link_text);
             }
         } catch (Exception e) {
             jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Some elements failed to load..\"}}");
         }
         driver.quit();
    } catch (MalformedURLException e) {
        e.printStackTrace();
    }
}

}
