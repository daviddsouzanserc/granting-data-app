package ca.gc.tri_agency.granting_data.model.util;

public class CalendarCell {
	private String cellDate;
	private int dayOfMonth;
	private boolean isOfMonth;

	public String getCellDate() {
		return cellDate;
	}

	public void setCellDate(String cellDate) {
		this.cellDate = cellDate;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public boolean isOfMonth() {
		return isOfMonth;
	}

	public void setOfMonth(boolean isOfMonth) {
		this.isOfMonth = isOfMonth;
	}

}
