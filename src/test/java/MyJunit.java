import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class MyJunit {
    WebDriver driver;
    WebDriverWait wait;

    @Before
    public void setup() {
        System.setProperty("webdriver.gecko.driver", "./src/test/resources/geckodriver.exe");
        FirefoxOptions ops = new FirefoxOptions();
        ops.addArguments("--headed");
        driver = new FirefoxDriver(ops);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

    }


    @Test
    public void getTitle() {
        driver.get("https://demoqa.com");
        String title = driver.getTitle();
        System.out.println(title);
        Assert.assertTrue(title.contains("ToolsQA"));
    }

    @Test
    public void checkifElementExists() throws InterruptedException {
        driver.get("https://demoqa.com");


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        Boolean status = wait.until(ExpectedConditions.elementToBeClickable(By.className("banner-image"))).isDisplayed();

        Assert.assertTrue(status);

    }

    @Test
    public void writeOnTextBox() throws InterruptedException {
        //Going to the Home page.
        driver.get("https://demoqa.com/elements");
        //Click menu
        driver.findElement(By.xpath("//span[contains(text(),'Text Box')]")).click();

        //Explicit wait class declear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        //Waiting for the textbox to be clickable.Then input name
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[id=userName]"))).sendKeys("Helal");
        //Scroll down
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,600)", "");
        //Waiting for the submit to be clickable.


        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='submit']"))).click();
        //Raking output
        String text = driver.findElement(By.cssSelector("[id=name]")).getText();
        // Assertion of the input and out put
        Assert.assertTrue(text.contains("Helal"));
    }

    @Test
    public void clickOnMultipleButon() {
        driver.get("https://demoqa.com/buttons");
        //
        Actions action = new Actions(driver);
        List<WebElement> list = driver.findElements(By.cssSelector("button"));
        action.doubleClick(list.get(1)).perform();

        String text = driver.findElement(By.id("doubleClickMessage")).getText();
        Assert.assertTrue(text.contains("You have done a double click"));

        action.contextClick(list.get(2)).perform();
        String text2 = driver.findElement(By.id("rightClickMessage")).getText();
        Assert.assertTrue(text2.contains("You have done a right click"));

        list.get(3).click();
        String text3 = driver.findElement(By.id("dynamicClickMessage")).getText();
        Assert.assertTrue(text3.contains("You have done a dynamic click"));

    }

    @Test
    public void fillupForm() {
        driver.get("https://demoqa.com/text-box");

        //Explicit wait class declear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));


        WebElement userName = driver.findElement(By.id("userName"));
        wait.until(ExpectedConditions.elementToBeClickable(userName)).sendKeys("Helal");

        driver.findElement(By.id("userEmail")).sendKeys("Helal@gmail.com");
        driver.findElement(By.id("currentAddress")).sendKeys("8/218 mirpur");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,600)", "");
        driver.findElement(By.id("submit")).click();

    }

    @Test
    public void handleAlerts() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");

        driver.findElement(By.id(("alertButton"))).click();
        driver.switchTo().alert().accept();
        //driver.switchTo().alert().dismiss();

        driver.findElement(By.id(("promtButton"))).click();
        driver.switchTo().alert().sendKeys("Fahim");
        Thread.sleep(2000);
        driver.switchTo().alert().accept();

        String text = driver.findElement(By.id("promptResult")).getText();
        Assert.assertTrue(text.contains("Fahim"));

    }

    @Test
    public void selectDate() throws InterruptedException {
        driver.get("https://demoqa.com/date-picker");
        Thread.sleep(1000);
        driver.findElement(By.id("datePickerMonthYearInput")).clear();
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys("05/08/1993");
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys(Keys.ENTER);

    }

    @Test
    public void selectDropdown() throws InterruptedException {
        driver.get("https://demoqa.com/select-menu");

        Thread.sleep(2000);
        Select color = new Select(driver.findElement(By.id("oldSelectMenu")));
        color.selectByValue("2");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,600)", "");

        Select cars = new Select(driver.findElement(By.id("cars")));
        if (cars.isMultiple()) {
            cars.selectByValue("volvo");
            cars.selectByValue("audi");
        }
    }

    @Test
    public void HandleMultipleTabs() throws InterruptedException {
        driver.get("https://demoqa.com/links");
        driver.findElement(By.id("simpleLink")).click();
        Thread.sleep(5000);

        ArrayList<String> w = new ArrayList<String>(driver.getWindowHandles());
        //switch to open tab
        driver.switchTo().window(w.get(1));
        System.out.println("New tab title: " + driver.getTitle());

//        for(String childTab:driver.getWindowHandles()){
//            driver.switchTo().window(childTab);
//        }
//        System.out.printf(driver.getTitle());
        Boolean status = driver.findElement(By.xpath("//img[@src='/images/Toolsqa.jpg']")).isDisplayed();
        Assert.assertEquals(true, status);
        driver.close();
        driver.switchTo().window(w.get(0));

    }

    @Test
    public void HandleChildWindows() throws InterruptedException {

        driver.get("https://demoqa.com/browser-windows");

        wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.id(("windowButton"))));

        button.click();
        String mainWindowHandle = driver.getWindowHandle();
        Set<String> allWindowHandles = driver.getWindowHandles();
        Iterator<String> iterator = allWindowHandles.iterator();


        while (iterator.hasNext()) {
            String ChildWindow = iterator.next();
            if (!mainWindowHandle.equalsIgnoreCase(ChildWindow)) {
                driver.switchTo().window(ChildWindow);
                String text = driver.findElement(By.id("sampleHeading")).getText();
                Assert.assertTrue(text.contains("This is a sample page"));
            }

        }


    }

    @Test
    public void modalDialog() throws InterruptedException {

        driver.get("https://demoqa.com/modal-dialogs");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("showSmallModal")));

        //driver.findElement(By.id("showSmallModal")).click();
        element.click();
        Thread.sleep(2000);
        driver.findElement(By.id("closeSmallModal")).click();

    }

    @Test
    public void webTables() throws InterruptedException {
        driver.get("https://demoqa.com/webtables");
        driver.findElement(By.xpath("//span[@id='edit-record-1']//*[@stroke='currentColor']")).click();
        driver.findElement(By.id("submit")).click();

    }

    @Test
    public void scrapData() {
        driver.get("https://demoqa.com/webtables");
        WebElement table = driver.findElement(By.className("rt-tbody"));
        List<WebElement> allRows = table.findElements(By.className("rt-tr"));
        int i = 0;
        for (WebElement row : allRows) {
            List<WebElement> cells = row.findElements(By.className("rt-td"));
            for (WebElement cell : cells) {
                i++;
                System.out.println("num[" + i + "] " + cell.getText());

            }
        }
    }

    @Test
    public void uploadImage() {
        driver.get("https://demoqa.com/upload-download");
        WebElement uploadElement = driver.findElement(By.id("uploadFile"));
        uploadElement.sendKeys("E:\\Picture\\2018\\Test.jpg");

        String text = driver.findElement(By.id("uploadedFilePath")).getText();
        Assert.assertTrue(text.contains("Test.jpg"));
    }

    @Test
    public void handleIframe() {
        driver.get("https://demoqa.com/frames");
        driver.switchTo().frame("frame2");
        String text = driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertTrue(text.contains("This is a sample page"));
        driver.switchTo().defaultContent();

    }

    @Test
    public void mouseHover() throws InterruptedException {
        driver.get("https://green.edu.bd/");
        WebElement mainMenu = driver.findElement(By.xpath("//a[@class='dropdown-toggle'][contains(text(),'About Us')]"));
        Actions actions = new Actions(driver);
        actions.moveToElement(mainMenu).perform();
        Thread.sleep(3000);
        WebElement subMenu = driver.findElement(By.xpath("//li[@id='menu-item-325']//a[contains(text(),'History')]"));
        actions.moveToElement(subMenu);
        actions.click().build().perform();
    }

    @Test
    public void keyboardEvents() throws InterruptedException {
        driver.get("https://www.google.com/");
        WebElement searchElement = driver.findElement(By.name("q"));
        Actions action = new Actions(driver);
        action.moveToElement(searchElement);
        action.keyDown(Keys.SHIFT);
        action.sendKeys("Selenium Webdriver")
                .keyUp(Keys.SHIFT)
                .doubleClick()
                .contextClick()
                .perform();

        Thread.sleep(5000);
    }

    @Test
    public void takeScreenShot() throws IOException {
        driver.get("https://demoqa.com");
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String time = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss-aa").format(new Date());
        String fileWithPath = "./src/test/resources/screenshots/" + time + ".png";
        File DestFile = new File(fileWithPath);
        FileUtils.copyFile(screenshotFile, DestFile);
    }


    @After
    public void finishTest() {
        //driver.close();
        driver.quit();
    }

    @Test
    public void readExcelFile() throws IOException {
        String filePath = ".\\src\\test\\resources";

        try {
            readFromExcel(filePath, "ExportExcel.xls", "Sheet1");
        } catch (Exception e) {

        }

    }

    public static void readFromExcel(String filePath, String fileName, String sheetName) throws IOException, InterruptedException {
        File file = new File(filePath + "\\" + fileName);
        FileInputStream inputStream = new FileInputStream(file);

        Workbook workbook = null;
        String fileExtensionName = fileName.substring(fileName.indexOf("."));
        if (fileExtensionName.equals(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        }
        Sheet sheet = workbook.getSheet(sheetName);

        int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
        for (int i = 0; i < rowCount + 1; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                DataFormatter formatter = new DataFormatter();
                System.out.print(formatter.formatCellValue((row.getCell(j))) + "|| ");
            }
            System.out.println();

        }

    }
}