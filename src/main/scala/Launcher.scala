import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.{ ChromeDriver, ChromeDriverService, ChromeOptions }
import org.scalatest.FunSuite
import org.scalatest.selenium.WebBrowser
import scala.sys.process._

/**
 *
 * @author <a href="mailto:hyysguyang@gmail.com">Young Gu</a>
 * @author <a href="mailto:guyang@lansent.com">Young Gu</a>
 */
object Launcher {

  def main(args: Array[String]) {
    val nodata = "没有查询到符合条件的记录！"
    val baseUrl: String = "http://xkczd.gz163.cn"
    import java.util.concurrent._
    val ex = new ScheduledThreadPoolExecutor(1)
    val task = new Runnable {
      def run() = if (nodata == new Query(baseUrl).fetchResult()) "audacious" !
    }
    ex.scheduleAtFixedRate(task, 1, 1, TimeUnit.MINUTES)

  }
}

class Query(baseUrl: String) extends FunSuite with WebBrowser {
  def createWebDriver = {
    if (Option(sys.props("webdriver.chrome.driver")).isEmpty) {
      System.setProperty("webdriver.chrome.driver", "/Develop/tools/chromedriver/chromedriver")
    }
    val builder: ChromeDriverService.Builder = new ChromeDriverService.Builder
    import scala.collection.JavaConversions._
    val service = builder.usingAnyFreePort.withEnvironment(seleniumEnv).build
    val options = new ChromeOptions
    options.addArguments("--start-maximized")
    val driver: ChromeDriver = new ChromeDriver(service, options)
    driver.manage.window.setSize(new org.openqa.selenium.Dimension(1920, 1080))
    driver
  }

  implicit val webDriver: WebDriver = createWebDriver

  def fetchResult() = {
    println("Starting fetching result....")
    goTo(s"$baseUrl/Proposer/MonthGagnerUnLotList.aspx")
    //    $(""" [name="ctl00$ContentPlaceHolder1$Month"] """).fillSelect().withIndex(1)
    telField("ctl00$ContentPlaceHolder1$txtCardCode").value = "520221198307082591"
    click on "ctl00$ContentPlaceHolder1$ibtnQuery"
    val result = find(cssSelector("#ContentPlaceHolder1_GridView1")).get.text
    webDriver.quit()
    println(s"Get Result: $result")
    result

  }

  def seleniumEnv: Map[String, String] = {
    val usePopupBrowser: Boolean = Option(sys.props("selenium.usePopupBrowser")).getOrElse("false").toBoolean
    if (usePopupBrowser) Map()
    else Map("DISPLAY" -> System.getProperty("lmportal.xvfb.id", ":1000"))
  }

}

