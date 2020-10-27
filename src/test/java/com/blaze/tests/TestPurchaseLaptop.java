package com.blaze.tests;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.blaze.pages.CartPage;
import com.blaze.pages.IndexPage;
import com.blaze.pages.PlaceOrderPage;
import com.blaze.pages.categories.LaptopsPage;
import com.blaze.utils.CommonUtilities;
import com.blaze.utils.Constants;

public class TestPurchaseLaptop extends BaseTest{

	private IndexPage indexPage = null;
	private CommonUtilities commonUtils = null;
	private LaptopsPage laptopsPage = null;
	private CartPage cartPage = null;
	private PlaceOrderPage placeOrderPage = null;
	private String totalBilledAmountOnCartPage = null;
	private String finalPurchaseDetails = null;
	private String totalAmountPaidByUser = null;
	
	@BeforeTest
	public void initConstr() {
		logger.info("************* Started Test case TestPurchaseLaptop ******************* ");
		indexPage = new IndexPage(driver);
		laptopsPage = new LaptopsPage(driver);
		cartPage = new CartPage(driver);
		commonUtils = new CommonUtilities(driver);
		placeOrderPage = new PlaceOrderPage(driver);
	}
	
	@Test(priority=1)
	public void accessDemoBlazeApp() {
		logger.info("************* Validating browser able to launch app url ******************* ");
		commonUtils.launchURL(Constants.webURL);
		driver.manage().window().maximize();
		logger.info("************* Validated browser launched app url ******************* ");
	}
	
	@Test(priority = 2)
	public void addingSonyVaioi5LaptopToCart() {
		logger.info("************* Validating user is able to add SonyVaioi5 Laptop in cart******************* ");
		indexPage.clickCategoryLaptops();
		commonUtils.waitForElement(Constants.SonyVaioI5LaptopElement, Constants.MAX_WAIT_TIME);
		laptopsPage.clickItem(laptopsPage.SonyVaioI5Laptop);
		commonUtils.waitForElement(Constants.addToCartByElement, Constants.MAX_WAIT_TIME);
		commonUtils.clickAddToCartButton();
		commonUtils.handleAlert();
		logger.info("************* Validated user added SonyVaioi5 Laptop in cart******************* ");
	}
	
	@Test(priority = 3)
	public void addingDelli78GBLatopToCart() {
		logger.info("************* Validating user is able to add Delli78GB Laptop in cart******************* ");
		commonUtils.clickHomeLinkButton();
		commonUtils.waitForElement(Constants.itemLaptopsElement, Constants.MAX_WAIT_TIME);
		indexPage.clickCategoryLaptops();
		commonUtils.waitForElement(Constants.Delli78GBLaptopElement, Constants.MAX_WAIT_TIME);
		laptopsPage.clickItem(laptopsPage.Delli78GBLaptop);
		commonUtils.waitForElement(Constants.addToCartByElement, Constants.MAX_WAIT_TIME);
		commonUtils.clickAddToCartButton();
		commonUtils.handleAlert();
		logger.info("************* Validated user added Delli78GB Laptop in cart******************* ");
	}
	
	@Test(priority = 4)
	public void deletingDelli78GBLaptopFromCart() throws InterruptedException {
		logger.info("************* Validating user is able to delete Delli78GB Laptop from cart******************* ");
		commonUtils.clickCartLinkButton();
		commonUtils.waitForElement(Constants.itemsAvailableInCartElement, Constants.MAX_WAIT_TIME);
		assertEquals(cartPage.getNumberOfItemsInCart(), 2);
		commonUtils.clickElement(Constants.deleteDelli78GBElement);
		Thread.sleep(5000);
		commonUtils.waitForElement(Constants.itemsAvailableInCartElement, Constants.MAX_WAIT_TIME);
		assertEquals(cartPage.getNumberOfItemsInCart(), 1);
		logger.info("************* Validated user deleted Delli78GB Laptop from cart******************* ");
	}
	
	@Test(priority = 5)
	public void clickPlaceOrder() {
		logger.info("************* Validating user is able to click on place order button ******************* ");
		totalBilledAmountOnCartPage = cartPage.getTotalBilledAmount();
		commonUtils.clickPlaceOrderButton();
		logger.info("************* Validated user clicked on place order button ******************* ");
	}
	
	@Test(priority = 6)
	public void fillPurchaseForm() {
		logger.info("************* Validating user is able to fill purchase form ******************* ");
		placeOrderPage.enterTextInElement(placeOrderPage.name, Constants.name);
		placeOrderPage.enterTextInElement(placeOrderPage.country, Constants.country);
		placeOrderPage.enterTextInElement(placeOrderPage.city, Constants.city);
		placeOrderPage.enterTextInElement(placeOrderPage.card, Constants.card);
		placeOrderPage.enterTextInElement(placeOrderPage.month, Constants.month);
		placeOrderPage.enterTextInElement(placeOrderPage.year, Constants.year);
		logger.info("************* Validated user filled purchase form ******************* ");
	}
	
	@Test(priority = 7)
	public void clickPurchaseForm() {
		logger.info("************* Validating user is able to click purchase button ******************* ");
		placeOrderPage.clickPurchaseButton();
		logger.info("************* Validating clicked purchase button ******************* ");
	}
	
	@Test(priority = 8)
	public void capturePurchaseIDAndAmount() {
		logger.info("************* Validating user is able to capture purchase id and amount ******************* ");
		finalPurchaseDetails = placeOrderPage.getPurchaseDetails(placeOrderPage.purchaseDetailsElement);
		logger.info("************* Validated user captured purchase id and amount ******************* ");
	}
	
	@Test(priority = 9)
	public void assertPurchaseAmountEqualsExpected() {
		logger.info("************* Validating purchase amount is equal to expected amount ******************* ");
		totalAmountPaidByUser = finalPurchaseDetails.split("Amount: ")[1].split(" USDCard")[0];
		assertEquals(totalAmountPaidByUser, totalBilledAmountOnCartPage);
		logger.info("************* Validated purchase amount is equal to expected amount ******************* ");
	}
	
	@Test(priority = 10)
	public void clickedOKButton() {
		logger.info("************* Validating user is able to click on OK button  ******************* ");
		placeOrderPage.clickOKButton();
		logger.info("************* Validated user clicked on OK button  ******************* ");
	}
	
}
