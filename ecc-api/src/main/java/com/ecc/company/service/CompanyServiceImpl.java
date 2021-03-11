package com.ecc.company.service;

//import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ecc.ApplicationProperties;
import com.ecc.company.domain.Company;
import com.ecc.company.domain.LastUpdate;
import com.ecc.company.model.EventTypeEnum;
import com.ecc.company.model.Message;
import com.ecc.company.model.MessageTypeEnum;
import com.ecc.company.model.SseEvent;
import com.ecc.company.model.UpdateStatusEnum;
import com.ecc.company.repository.CompanyRepository;
import com.ecc.company.repository.LastUpdateRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {
	
	private final CompanyRepository companyRepo;
	private final LastUpdateRepository lastUpdateRepo;
	private final ApplicationProperties applicationProps;
	
	public Optional<LastUpdate> getLastUpdate() {
		
		return lastUpdateRepo.findFirstByOrderByLastUpdateDesc();
		
	}
	
	public SseEmitter updateCompaniesList(String companyMarket, String modUser) {
		
		SseEmitter emitter = new SseEmitter(36000000000L);
		SseEvent event = new SseEvent(emitter);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		executor.execute(() -> {
			
			int eventId = 0;
			try {
				
				if (companyMarket.equals("GPW") || companyMarket.equals("NC")) {
					
					event.sendEvent(String.valueOf(++eventId), EventTypeEnum.UPDATE_STATUS, UpdateStatusEnum.STARTING_WEBDRIVER.toString());
					
					System.setProperty(applicationProps.getSelenium().get("webdriver").getName(), applicationProps.getSelenium().get("webdriver").getUrl());
					
					ChromeOptions chromeOptions = new ChromeOptions();
					//chromeOptions.addArguments("--headless");
					chromeOptions.addArguments("--no-sandbox");
					chromeOptions.addArguments("--disable-gpu");
					chromeOptions.addArguments("--disable-extensions");
					chromeOptions.addArguments("--disable-dev-shm-usage");
					chromeOptions.addArguments("user-agent=Chrome/88.0.4324.150");
					
					WebDriver driver = new ChromeDriver(chromeOptions);
					WebDriverWait wait = new WebDriverWait(driver, 5);
					
					event.sendEvent(String.valueOf(++eventId), EventTypeEnum.UPDATE_STATUS, UpdateStatusEnum.COUNTING_NUMBER_OF_COMPANIES.toString());
					
					if (companyMarket.equals("GPW")) {
						
						Elements rows = null;
						
						try {
							
							driver.get(applicationProps.getSources().get("gpw").getUrl() + "akcje");
							
							WebElement anchor = driver.findElement(By.xpath("//a[@id='nj_fix2']"));
							anchor.click();
							
							anchor = driver.findElement(By.xpath("//a[@id='nj_fix1']"));
							anchor.click();
							
							wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//table[starts-with(@id, 'footable')]"), 2));
							
							Document doc = Jsoup.parse(driver.getPageSource());
							rows = doc.select("table[id^=footable] > tbody > tr");
							
							event.sendEvent(String.valueOf(++eventId), EventTypeEnum.NUMBER_OF_COMPANIES, rows.size());
							event.sendEvent(String.valueOf(++eventId), EventTypeEnum.UPDATE_STATUS, UpdateStatusEnum.UPDATING_COMPANIES.toString());
							
						} catch (Exception e) {
							event.sendEvent(
									String.valueOf(++eventId),
									EventTypeEnum.MESSAGE,
									new Message(MessageTypeEnum.DANGER.toString(), "Error", e.getMessage().toString().split("\n")[0]));
							event.sendEvent(String.valueOf(++eventId), EventTypeEnum.UPDATE_STATUS, UpdateStatusEnum.ERROR.toString());
							e.printStackTrace();
						} finally {
							driver.close();
							driver.quit();
						}
						
						if (!rows.isEmpty()) {
							
							String companyName = null;
							
							int progress = 0;
							for (Element row : rows) {
								
								progress++;
								try {
									
									companyName = row.child(2).text().split("/")[0].trim();
									String isin = row.child(2).child(0).attr("href").split("isin=")[1];
									String ticker = row.child(3).text();
									
									Company company = (companyRepo.findFirstByIsin(isin).isPresent() ? companyRepo.findFirstByIsin(isin).get() : new Company());
									
									company.setIsin(isin);
									company.setTicker(ticker);
									company.setCompanyName(companyName);
									company.setCompanyMarket(companyMarket);
									company.setWatchList(company.getWatchList() != null ? company.getWatchList() : false);
									company.setModUser(modUser);
									//company.setModDate(new Date());
									
									companyRepo.save(company);
									
									System.out.println(progress + " " + companyName + " " + isin + " " + ticker);
									
								} catch (Exception e) {
									event.sendEvent(
											String.valueOf(++eventId),
											EventTypeEnum.MESSAGE,
											new Message(MessageTypeEnum.WARNING.toString(), "Error - " + companyName, e.getMessage().toString().split("\n")[0]));
									e.printStackTrace();
								} finally {
									event.sendEvent(String.valueOf(++eventId), EventTypeEnum.PROGRESS, progress);
								}
								
								try {
									Thread.sleep(50);
								} catch(InterruptedException ex) {
									ex.printStackTrace();
								}
								
							}
							
							LastUpdate lastUpdate = new LastUpdate();
							lastUpdate.setModUser(modUser);
							//lastUpdate.setLastUpdate(new Date());
							lastUpdateRepo.save(lastUpdate);
							
							event.sendEvent(String.valueOf(++eventId), EventTypeEnum.UPDATE_STATUS, UpdateStatusEnum.COMPLETED.toString());
							
						}
						
					} else if (companyMarket.equals("NC")) {
						
						Elements rows = null;
						
						try {
							
							driver.get(applicationProps.getSources().get("nc").getUrl() + "notowania");
							
							wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//table[starts-with(@id, 'footable')]"), 0));
							
							Document doc = Jsoup.parse(driver.getPageSource());
							rows = doc.select("table[id^=footable]").first().select("tbody > tr");
							
							event.sendEvent(String.valueOf(++eventId), EventTypeEnum.NUMBER_OF_COMPANIES, rows.size());
							event.sendEvent(String.valueOf(++eventId), EventTypeEnum.UPDATE_STATUS, UpdateStatusEnum.UPDATING_COMPANIES.toString());
							
						} catch (Exception e) {
							event.sendEvent(
									String.valueOf(++eventId),
									EventTypeEnum.MESSAGE,
									new Message(MessageTypeEnum.DANGER.toString(), "Error", e.getMessage().toString().split("\n")[0]));
							event.sendEvent(String.valueOf(++eventId), EventTypeEnum.UPDATE_STATUS, UpdateStatusEnum.ERROR.toString());
							e.printStackTrace();
						} finally {
							driver.close();
							driver.quit();
						}
						
						if (!rows.isEmpty()) {
							
							String companyName = null;
							
							int progress = 0;
							for (Element row : rows) {
								
								progress++;
								try {
									
									companyName = row.child(1).text().split("/")[0].trim();
									String isin = row.child(1).child(0).attr("href").split("isin=")[1];
									String ticker = row.child(2).text();
									
									Company company = (companyRepo.findFirstByIsin(isin).isPresent() ? companyRepo.findFirstByIsin(isin).get() : new Company());
									
									company.setIsin(isin);
									company.setTicker(ticker);
									company.setCompanyName(companyName);
									company.setCompanyMarket(companyMarket);
									company.setWatchList(company.getWatchList() != null ? company.getWatchList() : false);
									company.setModUser(modUser);
									//company.setModDate(new Date());
									
									companyRepo.save(company);
									
									System.out.println(progress + " " + companyName + " " + isin + " " + ticker);
									
								} catch (Exception e) {
									event.sendEvent(
											String.valueOf(++eventId),
											EventTypeEnum.MESSAGE,
											new Message(MessageTypeEnum.WARNING.toString(), "Error - " + companyName, e.getMessage().toString().split("\n")[0]));
									e.printStackTrace();
								} finally {
									event.sendEvent(String.valueOf(++eventId), EventTypeEnum.PROGRESS, progress);
								}
								
								try {
									Thread.sleep(50);
								} catch(InterruptedException ex) {
									ex.printStackTrace();
								}
								
							}
							
							LastUpdate lastUpdate = new LastUpdate();
							lastUpdate.setModUser(modUser);
							//lastUpdate.setLastUpdate(new Date());
							lastUpdateRepo.save(lastUpdate);
							
							event.sendEvent(String.valueOf(++eventId), EventTypeEnum.UPDATE_STATUS, UpdateStatusEnum.COMPLETED.toString());
							
						}
						
					}
					
				} 
				
			} catch (Exception e) {
				event.sendEvent(
						String.valueOf(++eventId),
						EventTypeEnum.MESSAGE,
						new Message(MessageTypeEnum.DANGER.toString(), "Error", e.getMessage().toString()));
				event.sendEvent(String.valueOf(++eventId), EventTypeEnum.UPDATE_STATUS, UpdateStatusEnum.ERROR.toString());
				e.printStackTrace();
			} finally {
				emitter.complete();
			}
			
		});
		
		return emitter;
		
	}
	
}
