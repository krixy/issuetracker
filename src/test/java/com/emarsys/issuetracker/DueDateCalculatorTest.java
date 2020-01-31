package com.emarsys.issuetracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.Test;
public class DueDateCalculatorTest{
	
	@Test
	public void testWithinADay(){
		IssueSubmitTimeInfo issueSubmitTimeInfo = new IssueSubmitTimeInfo();
		
		LocalDateTime submitTime = LocalDateTime.of(2020, Month.JANUARY, 31, 10, 5);
		int turnoverTimeInHours = 3;
		LocalDateTime expectedTime = LocalDateTime.of(2020, Month.JANUARY, 31, 13, 5);
		
		issueSubmitTimeInfo.setSubmitTime(submitTime);
		issueSubmitTimeInfo.setTurnoverTimeInHours(turnoverTimeInHours);
		
		LocalDateTime actualTime = DueDateCalculator.calculateDueDate(issueSubmitTimeInfo);
		assertEquals(expectedTime, actualTime);
	}
	
	@Test
	public void testOverADay(){
		IssueSubmitTimeInfo issueSubmitTimeInfo = new IssueSubmitTimeInfo();
		
		LocalDateTime submitTime = LocalDateTime.of(2020, Month.JANUARY, 30, 10, 5);
		int turnoverTimeInHours = 9;
		LocalDateTime expectedTime = LocalDateTime.of(2020, Month.JANUARY, 31, 11, 5);
		
		issueSubmitTimeInfo.setSubmitTime(submitTime);
		issueSubmitTimeInfo.setTurnoverTimeInHours(turnoverTimeInHours);
		
		LocalDateTime actualTime = DueDateCalculator.calculateDueDate(issueSubmitTimeInfo);
		assertEquals(expectedTime, actualTime);
	}

	@Test
	public void testIncludingWeekend(){
		IssueSubmitTimeInfo issueSubmitTimeInfo = new IssueSubmitTimeInfo();
		
		LocalDateTime submitTime = LocalDateTime.of(2020, Month.JANUARY, 31, 10, 5);
		int turnoverTimeInHours = 9;
		LocalDateTime expectedTime = LocalDateTime.of(2020, Month.FEBRUARY, 3, 11, 5);
		
		issueSubmitTimeInfo.setSubmitTime(submitTime);
		issueSubmitTimeInfo.setTurnoverTimeInHours(turnoverTimeInHours);
		
		LocalDateTime actualTime = DueDateCalculator.calculateDueDate(issueSubmitTimeInfo);
		assertEquals(expectedTime, actualTime);
	}
	
	@Test
	public void testOverAWeekWithLessThanADayTurnover(){
		IssueSubmitTimeInfo issueSubmitTimeInfo = new IssueSubmitTimeInfo();
		
		LocalDateTime submitTime = LocalDateTime.of(2020, Month.JANUARY, 31, 16, 5);
		int turnoverTimeInHours = 1;
		LocalDateTime expectedTime = LocalDateTime.of(2020, Month.FEBRUARY, 3, 9, 5);
		
		issueSubmitTimeInfo.setSubmitTime(submitTime);
		issueSubmitTimeInfo.setTurnoverTimeInHours(turnoverTimeInHours);
		
		LocalDateTime actualTime = DueDateCalculator.calculateDueDate(issueSubmitTimeInfo);
		assertEquals(expectedTime, actualTime);
	}
	
	@Test
	public void testOverAWeek(){
		IssueSubmitTimeInfo issueSubmitTimeInfo = new IssueSubmitTimeInfo();
		
		LocalDateTime submitTime = LocalDateTime.of(2020, Month.JANUARY, 27, 15, 5);
		int turnoverTimeInHours = 40;
		LocalDateTime expectedTime = LocalDateTime.of(2020, Month.FEBRUARY, 3, 15, 5);
		
		issueSubmitTimeInfo.setSubmitTime(submitTime);
		issueSubmitTimeInfo.setTurnoverTimeInHours(turnoverTimeInHours);
		
		LocalDateTime actualTime = DueDateCalculator.calculateDueDate(issueSubmitTimeInfo);
		assertEquals(expectedTime, actualTime);
	}
	
	@Test
	public void testIsssueCreatedOnSaturday(){
		IssueSubmitTimeInfo issueSubmitTimeInfo = new IssueSubmitTimeInfo();
		
		LocalDateTime submitTime = LocalDateTime.of(2020, Month.FEBRUARY, 1, 10, 59);
		int turnoverTimeInHours = 9;
		LocalDateTime expectedTime = LocalDateTime.of(2020, Month.FEBRUARY, 4, 10, 0);
		
		issueSubmitTimeInfo.setSubmitTime(submitTime);
		issueSubmitTimeInfo.setTurnoverTimeInHours(turnoverTimeInHours);
		
		LocalDateTime actualTime = DueDateCalculator.calculateDueDate(issueSubmitTimeInfo);
		assertEquals(expectedTime, actualTime);
	}
	
	@Test
	public void testIsssueCreatedOnAWeekdayMorning(){
		IssueSubmitTimeInfo issueSubmitTimeInfo = new IssueSubmitTimeInfo();
		
		LocalDateTime submitTime = LocalDateTime.of(2020, Month.JANUARY, 31, 6, 30);
		int turnoverTimeInHours = 2;
		LocalDateTime expectedTime = LocalDateTime.of(2020, Month.JANUARY, 31, 11, 0);
		
		issueSubmitTimeInfo.setSubmitTime(submitTime);
		issueSubmitTimeInfo.setTurnoverTimeInHours(turnoverTimeInHours);
		
		LocalDateTime actualTime = DueDateCalculator.calculateDueDate(issueSubmitTimeInfo);
		assertEquals(expectedTime, actualTime);
	}
	
	@Test
	public void testIsssueCreatedOnAFridayEvening(){
		IssueSubmitTimeInfo issueSubmitTimeInfo = new IssueSubmitTimeInfo();
		
		LocalDateTime submitTime = LocalDateTime.of(2020, Month.JANUARY, 31, 22, 30);
		int turnoverTimeInHours = 2;
		LocalDateTime expectedTime = LocalDateTime.of(2020, Month.FEBRUARY, 3, 11, 0);
		
		issueSubmitTimeInfo.setSubmitTime(submitTime);
		issueSubmitTimeInfo.setTurnoverTimeInHours(turnoverTimeInHours);
		
		LocalDateTime actualTime = DueDateCalculator.calculateDueDate(issueSubmitTimeInfo);
		assertEquals(expectedTime, actualTime);
	}
	
	@Test()
	public void testSubmitTimeInfoIsNull(){
		
		Exception exception = assertThrows(IllegalArgumentException.class, () -> DueDateCalculator.calculateDueDate(null));
		String expectedMessage = "Submit time information cannot be null!";
		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}
	
	@Test()
	public void testTurnoverIsZero(){
		
		IssueSubmitTimeInfo issueSubmitTimeInfo = new IssueSubmitTimeInfo();
		issueSubmitTimeInfo.setSubmitTime(LocalDateTime.now());
		
		Exception exception = assertThrows(IllegalArgumentException.class, () -> DueDateCalculator.calculateDueDate(issueSubmitTimeInfo));
			
		String expectedMessage = "Turnover cannot be negative or zero!";
		String actualMessage = exception.getMessage();
		assertEquals(expectedMessage, actualMessage);
	}
	

}
