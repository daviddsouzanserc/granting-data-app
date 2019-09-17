package ca.gc.tri_agency.granting_data.model.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class CalendarGrid {
	private CalendarCell[][] dataGrid;
	final int COLS = 7;
	final int ROWS = 6;
	private YearMonth month;

	public CalendarGrid(long plusMinusMonth) {
		LocalDate startDate;
		if (plusMinusMonth == 0) {
			month = YearMonth.now();
		} else if (plusMinusMonth < 0) {
			month = YearMonth.now().minusMonths(plusMinusMonth);
		} else {
			month = YearMonth.now().plusMonths(plusMinusMonth);
		}
		startDate = month.atDay(1);
		int lastDay = YearMonth.from(startDate).atEndOfMonth().getDayOfMonth();
		int firstDayCol = startDate.withDayOfMonth(1).getDayOfWeek().getValue();

		int monthDay = 0;
		LocalDate tmpDate;
		dataGrid = new CalendarCell[ROWS][COLS];
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				if (row * COLS + col < firstDayCol) {
					tmpDate = startDate.minusDays(firstDayCol - (row * COLS + col));
				} else {
					tmpDate = startDate.plusDays(monthDay);
					monthDay++;
				}
				CalendarCell newCell = new CalendarCell();
				newCell.setCellDate(tmpDate.format(DateTimeFormatter.ISO_DATE));
				newCell.setDayOfMonth(tmpDate.getDayOfMonth());
				if (monthDay != 0 && monthDay <= lastDay) {
					newCell.setOfMonth(true);
				} else {
					newCell.setOfMonth(false);
				}
				dataGrid[row][col] = newCell;
			}

		}
		this.month = month;

	}

	public CalendarCell[][] getDataGrid() {
		return dataGrid;
	}

	public void setDataGrid(CalendarCell[][] dataGrid) {
		this.dataGrid = dataGrid;
	}

	public YearMonth getMonth() {
		return month;
	}

	public void setMonth(YearMonth month) {
		this.month = month;
	}

}
