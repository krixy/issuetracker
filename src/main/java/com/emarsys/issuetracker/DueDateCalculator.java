package com.emarsys.issuetracker;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

/**
 * This is a utility class for a hypothetical issue tracker service. Contains
 * logic for calculating due date for issues from given parameters.
 * 
 * @author Krisztian Szajko
 */
public class DueDateCalculator {

	private static int WORKING_HOURS_IN_A_WEEK = 40;
	private static int WORKING_HOURS_IN_A_DAY = 8;

	private static int WORKING_DAY_START_HOUR = 9;
	private static int WORKING_DAY_START_MINUTE = 0;
	private static int WORKING_DAY_END_TIME = 17;

	/**
	 * Calculates due dates for issues from {@link IssueSubmitTimeInfo} parameter.
	 * The idea of the algorithm is to analyze the turnoverTimeInHours information
	 * to determine how many calendar weeks adds up the given turnover, and reduce
	 * the actual turnover. Same procedure applied on the remainder days that are
	 * less then a week until the reduced turnoverTimeInHours will not able to turn
	 * the date. The method does not allow invalid parameters (see also
	 * {@link DueDateCalculator #validateInputParameters(IssueSubmitTimeInfo)}), and
	 * enables Issues to be created at non-working hours see also
	 * {@link DueDateCalculator #adjustIssueStartTimeToNextValidWorkingTime(LocalDateTime)}.
	 * 
	 * @param issueSubmitTimeInfo
	 * @return {@link LocalDateTime} due date for the issue to solve.
	 */
	public static LocalDateTime calculateDueDate(IssueSubmitTimeInfo issueSubmitTimeInfo) {

		validateInputParameters(issueSubmitTimeInfo);

		LocalDateTime result = issueSubmitTimeInfo.getSubmitDate();
		result = adjustIssueStartTimeToNextValidWorkingTime(result);

		int workingHoursPastThisDay = result.getHour() - WORKING_DAY_START_HOUR;
		int workingHoursPastThisWeek = (result.getDayOfWeek().getValue() - 1) * WORKING_HOURS_IN_A_DAY + workingHoursPastThisDay;
		int workingHourLeftFromStartWeek = WORKING_HOURS_IN_A_WEEK - workingHoursPastThisWeek;

		int turnoverTimeInHours = issueSubmitTimeInfo.getTurnoverTimeInHours();

		int weeksAhead = 0;
		int daysAhead = 0;
		if (turnoverTimeInHours >= workingHourLeftFromStartWeek) {
			turnoverTimeInHours = turnoverTimeInHours - workingHourLeftFromStartWeek;

			weeksAhead = turnoverTimeInHours / WORKING_HOURS_IN_A_WEEK;
			turnoverTimeInHours = turnoverTimeInHours % WORKING_HOURS_IN_A_WEEK;

			result = result.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).withHour(WORKING_DAY_START_HOUR);
			workingHoursPastThisDay = 0;
		}

		int workingHoursLeftThisDay = WORKING_HOURS_IN_A_DAY - workingHoursPastThisDay;

		if (turnoverTimeInHours >= workingHoursLeftThisDay) {
			turnoverTimeInHours = turnoverTimeInHours - workingHoursLeftThisDay;

			daysAhead = turnoverTimeInHours / WORKING_HOURS_IN_A_DAY;
			turnoverTimeInHours = turnoverTimeInHours % WORKING_HOURS_IN_A_DAY;

			result = result.withHour(WORKING_DAY_START_HOUR).plusDays(1);
		}

		int daysToAdd = weeksAhead * 7 + daysAhead;

		result = result.plusDays(daysToAdd).plusHours(turnoverTimeInHours);

		return result;
	}

	/**
	 * Validates IssueSubmitTimeInfo parameters.
	 * 
	 * @throws IllegalArgumentException if any of the parameters contains illegal
	 *                                  value.
	 * @param issueSubmitTimeInfo is the parameter, which is validated
	 * 
	 */
	private static void validateInputParameters(IssueSubmitTimeInfo issueSubmitTimeInfo) {

		if (issueSubmitTimeInfo == null)
			throw new IllegalArgumentException("Submit time information cannot be null!");

		if (issueSubmitTimeInfo.getTurnoverTimeInHours() <= 0)
			throw new IllegalArgumentException("Turnover cannot be negative or zero!");

	}

	/**
	 * Checks if the issue is created in a non-working hour and calculates the start
	 * of the next working day.
	 * 
	 * @param submitDate
	 * @return A {@link LocalDateTime} object holding the time infomation about the
	 *         next working day start time.
	 */
	private static LocalDateTime adjustIssueStartTimeToNextValidWorkingTime(LocalDateTime submitDate) {

		if (submitDate.getHour() < WORKING_DAY_START_HOUR && submitDate.getDayOfWeek().getValue() <= 5) {

			submitDate = submitDate.withHour(WORKING_DAY_START_HOUR).withMinute(WORKING_DAY_START_MINUTE);

		} else if (submitDate.getHour() > WORKING_DAY_END_TIME && submitDate.getDayOfWeek().getValue() < 5) {

			submitDate = submitDate.withHour(WORKING_DAY_START_HOUR).withMinute(WORKING_DAY_START_MINUTE).plusDays(1);

		} else if (submitDate.getHour() > WORKING_DAY_END_TIME && submitDate.getDayOfWeek() == DayOfWeek.FRIDAY) {

			submitDate = submitDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).withHour(WORKING_DAY_START_HOUR).withMinute(WORKING_DAY_START_MINUTE);

		} else if (submitDate.getDayOfWeek().getValue() > 5) {

			submitDate = submitDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).withHour(WORKING_DAY_START_HOUR).withMinute(WORKING_DAY_START_MINUTE);

		}
		return submitDate;
	}

}
