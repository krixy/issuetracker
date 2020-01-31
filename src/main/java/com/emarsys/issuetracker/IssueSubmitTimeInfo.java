package com.emarsys.issuetracker;

import java.time.LocalDateTime;

public class IssueSubmitTimeInfo {
	
	private LocalDateTime submitTime;
	private int turnoverTimeInHours;
	
	public LocalDateTime getSubmitDate() {
		return submitTime;
	}
	public void setSubmitTime(LocalDateTime submitTime) {
		this.submitTime = submitTime;
	}
	public int getTurnoverTimeInHours() {
		return turnoverTimeInHours;
	}
	public void setTurnoverTimeInHours(int turnoverTimeInHours) {
		this.turnoverTimeInHours = turnoverTimeInHours;
	}
	
	
}
