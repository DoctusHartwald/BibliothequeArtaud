package mapping.municipale.paris;

import java.util.List;

public class EntryRowLocation {
	List<EntryRow> entryRows;
	List<HoldingRow> holdingRow;

	public List<EntryRow> getEntryRows() {
		return entryRows;
	}

	public void setEntryRows(List<EntryRow> entryRows) {
		this.entryRows = entryRows;
	}

	public List<HoldingRow> getHoldingRow() {
		return holdingRow;
	}

	public void setHoldingRow(List<HoldingRow> holdingRow) {
		this.holdingRow = holdingRow;
	}
}
