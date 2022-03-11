package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.services.TheCredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.TheEncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private  static WebDriver driver;

	private static String baseUrl;
	private final String  fname="King";
	private final String  lname="James";
	private final String  uname="lakers";
	private final String  pword="bball";

	//NoteModel
	private final String noteTitle_org="org note title";
	private final String noteTitle_upt="upt note title";
	private final String noteDes_org ="org note des";
	private final String noteDes_upt="upt note des";

	//CredentialsModel
	private final String[] urls= new String[]{"nfl.com", "ncaa.com", "nba.com"};
	private final String[] unames = new String[]{"brady","cane","james"};
	private final String[] pws = new String[]{"sb","nc","wc"};
	private final String[] urls_upt= new String[]{"nflball.com", "ncaaball.com", "nbaball.com"};
	private final String[] unames_upt = new String[]{"tombrady","hurricane","kingjames"};
	private final String[] pws_upt = new String[]{"sbfb","ncuni","wcbb"};

	@Autowired
	private TheEncryptionService theEncryptionService;
	@Autowired
	private TheCredentialService theCredentialService;

	private Logger logger= LoggerFactory.getLogger(CloudStorageApplicationTests.class);

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
	}

	@AfterAll
	 public static void afterAll() {
		if (driver != null) {
			driver.quit();
		}
	}

	@BeforeEach
	public void beforeEach() throws InterruptedException {
		//this.driver = new ChromeDriver();
		baseUrl="http://localhost:" + this.port;
		sleep(1000);
	}

	@AfterEach
	public void takeABreak() throws InterruptedException {
		sleep(2000);
		//driver.quit();
	}


	@Test
	@Order(1)
	//test that verifies that an unauthorized user can only access the login and signup pages
	public void a_getLoginPage() throws InterruptedException {

		logger.error("test 1 -accessibility and security");

		driver.get(baseUrl + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
		sleep(2000);


		driver.get(baseUrl+"/signup");
		Assertions.assertEquals("Sign Up",driver.getTitle());
		sleep(2000);

		driver.get(baseUrl+"/home");
        Assertions.assertNotEquals("Home",driver.getTitle());
	}

	@Test
	@Order(2)
    //Write a test that signs up a new user, logs in, verifies that the home page is accessible,
	// logs out, and verifies that the home page is no longer accessible
    public void b_signupSuccess() throws InterruptedException {
		logger.error("test 2 -signup");
		driver.get(baseUrl + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signUpNow(fname, lname, uname, pword);
		//now we should have been redirected to login page with successful msg
		//driver.navigate().to(baseUrl +"/login");
		//driver.switchTo().window("/login");
		sleep(4000);
		assertEquals("Login", driver.getTitle());
		//driver.findElement(By.id("signup-success-msg"));
		 //driver.get(baseUrl + "/login");
		//LoginPage loginPage=new LoginPage(driver);
		//sleep(5000);
        //Assertions.assertTrue(driver..sigupOkMsgDisplayed());
		//Assertions.assertTrue(signupPage.sigupOkMsgDisplayed());
	}

	//Write a test that signs up a new user, logs in, verifies that the home page is accessible,
	// logs out, and verifies that the home page is no longer accessible
	@Test
	@Order(3)
	public void c_loginSuccess() throws Exception{
		logger.error("test 3 -login-logout-login again");
        driver.get(baseUrl + "/login");
        LoginPage loginPage=new LoginPage(driver);
        loginPage.LoginNow(uname,pword);
		sleep(1000);
		//Assert page redirected to home so login is successful
		Assertions.assertEquals("Home",driver.getTitle());

		driver.get(baseUrl + "/home");
		HomePage homePage=new HomePage(driver);
		homePage.clickLogoutBtn();
		sleep(1000);
		//Assert home page is not accessible after logout
		Assertions.assertNotEquals("Home",driver.getTitle());

		//now it is on login page actually, login again for other tests to continue
		driver.get(baseUrl + "/login");
		loginPage.LoginNow(uname,pword);
		sleep(1000);
		//Assert page redirected to home so login is successful
		Assertions.assertEquals("Home",driver.getTitle());

	}



	public void waitForVisibility(String id){
		WebDriverWait wait = new WebDriverWait(driver, 4000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));

	}

	@Test
	@Order(4)
	public void testNotes() throws Exception{
		logger.error("test 4 -Notes");
		//c_loginSuccess();
		driver.get(baseUrl+"/home");
		NotePage notePage=new NotePage(driver);

		waitForVisibility(notePage.getNoteTabId());
		notePage.clickNoteTab();

		waitForVisibility(notePage.getAddNoteBtnId());
		notePage.clickAddNoteBtn();

		//Create New NoteModel and verify//
		//now the Modal is there, input values
		//waitForVisibility(notePage.getNoteSubmitBtnId());
		notePage.inputNoteTitle(noteTitle_org);
		notePage.inputNoteDescription(noteDes_org);
		sleep(2000);
		notePage.submitNote();
		//go back to notTab
		sleep(2000);
		waitForVisibility(notePage.getNoteTabId());
		notePage.clickNoteTab();
        //verify new note is added and displayed as expected
		Assertions.assertEquals(notePage.getNoteTitleDisplay(),noteTitle_org);
		Assertions.assertEquals(notePage.getNoteDesDisplay(),noteDes_org);

		sleep(3000);

		//Edit the newly created NoteModel and verify//
		notePage.clickNoteEditBtn();
		sleep(1000);
		//waitForVisibility(notePage.getNoteSubmitBtnId());
		//edit the note
		notePage.inputNoteTitle(noteTitle_upt);
		notePage.inputNoteDescription(noteDes_upt);
		sleep(2000);
		notePage.submitNote();
		//go back to notTab
		waitForVisibility(notePage.getNoteTabId());
		notePage.clickNoteTab();
		sleep(1000);
		//verify  note is edited and displayed as expected
		Assertions.assertEquals(notePage.getNoteTitleDisplay(),noteTitle_upt);
		Assertions.assertEquals(notePage.getNoteDesDisplay(),noteDes_upt);

		//Delete the newly edited NoteModel and verify//
		//go back to notTab
		waitForVisibility(notePage.getNoteTabId());
		notePage.clickNoteTab();
		//delete the NoteModel and verify it is not there
		notePage.clickNoteDeleteBtn();
		//Assertions.assertNull(notePage.getNoteTitleDisplay());
		//Assertions.assertThrows(Exception.class,null);

		sleep(2000);
		//go back to notTab, visually see note is deleted and let's assert it
		waitForVisibility(notePage.getNoteTabId());
		notePage.clickNoteTab();
		sleep(1000);
		Assertions.assertEquals(0,notePage.getNoteEditBtns().size());

	}

	@Test
	@Order(5)
	public void testCredentials() throws Exception {
		logger.error("test 5 - CredentialsModel");
		driver.get(baseUrl + "/home");
		CredentialPage credentialPage = new CredentialPage(driver);

		//////Create new CredentialsModel and verify//////
		int total=3;
		//create number of "total" credentials
		for(int pos=0;pos<total;pos++){
			//wait for Credential page is visible
			waitForVisibility(credentialPage.getCrenTabId());
			credentialPage.clickCrenTab();
			sleep(1000);
			//click add new credential button
			waitForVisibility(credentialPage.getAddCrenBtnId());
			credentialPage.clickAddCrenBtn();
			//now the modal is there, input values
			credentialPage.inputUrl(urls[pos]);
			credentialPage.inputUserName(unames[pos]);
			credentialPage.inputPasswd(pws[pos]);
			sleep(2000);
			credentialPage.clickCrenSubmitBtn();
			//this sleep is important, otherwise at the beginning of the next loop,
			//tab may not available when visibility is being checked which causing waiting forever
			sleep(3000);
		}

		//go back to Credential tab
		waitForVisibility(credentialPage.getCrenTabId());
		credentialPage.clickCrenTab();
		sleep(1000);
		//verify the displayed credentials are expected and their passwds are encrypted
		//encryptionService = new TheEncryptionService();
		for(int pos=0;pos<total;pos++){
           String displayedUrl=credentialPage.getUrl(pos);
           String displayedUname=credentialPage.getUname(pos);

           String displayedPwd = credentialPage.getPw(pos);
           //decrypt pwd
			String key= theCredentialService.getKeyById(pos+1);
			displayedPwd= theEncryptionService.decryptValue(displayedPwd,key);

			Assertions.assertEquals(displayedUrl,urls[pos]);
			Assertions.assertEquals(displayedUname,unames[pos]);
            Assertions.assertEquals(displayedPwd,pws[pos]);
		}


		////Edit CredentialsModel and Verify the updated results////
		////Same time verified in the Edit window the password is decrypted////
		for(int pos=0;pos<total;pos++) {
			//wait for Credential page is visible
			waitForVisibility(credentialPage.getCrenTabId());
			credentialPage.clickCrenTab();
			sleep(1000);
			credentialPage.clickEditCredBtn(pos);
			sleep(1000);
			credentialPage.inputUrl(urls_upt[pos]);
			credentialPage.inputUserName(unames_upt[pos]);
			//before update pwd, first verify it is decrypted
			Assertions.assertEquals(credentialPage.getPasswdInModal(),pws[pos]);
			credentialPage.inputPasswd(pws_upt[pos]);
			sleep(2000);
			credentialPage.clickCrenSubmitBtn();
			//this sleep is important, otherwise at the beginning of the next loop,
			//tab may not available when visibility is being checked which causing waiting forever
			sleep(3000);
		}

         //go back to Credential tab
		waitForVisibility(credentialPage.getCrenTabId());
		credentialPage.clickCrenTab();
		sleep(1000);
		//verify after update, the displayed credentials are expected and their passwds are encrypted
		for(int pos=0;pos<total;pos++){
			String displayedUrl=credentialPage.getUrl(pos);
			String displayedUname=credentialPage.getUname(pos);

			String displayedPwd = credentialPage.getPw(pos);
			//decrypt pwd
			String key= theCredentialService.getKeyById(pos+1);
			displayedPwd= theEncryptionService.decryptValue(displayedPwd,key);

			Assertions.assertEquals(displayedUrl,urls_upt[pos]);
			Assertions.assertEquals(displayedUname,unames_upt[pos]);
			Assertions.assertEquals(displayedPwd,pws_upt[pos]);
		}

		///Verify deleting credentials is working as expected
		for(int pos=0;pos<total;pos++){
			waitForVisibility(credentialPage.getCrenTabId());
			credentialPage.clickCrenTab();
			sleep(1000);

            //note after each deletion, the next one we want to delete is always at position 0
			credentialPage.clickDeleteCredBtn(0);
			sleep(3000);
		}

		//go back to Credential tab, visually all credentials are deleted and let's assert it
		waitForVisibility(credentialPage.getCrenTabId());
		credentialPage.clickCrenTab();
		Assertions.assertEquals(0,credentialPage.getEditBtns().size());
		sleep(2000);



	}
}
