/*******************************************************************************
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package view;

import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import society.PerformedTrade;
import society.Society;

public class TradeOverviewTableModel extends AbstractTableModel {

	private static final String[] COLUMN_NAMES = { "person", "person asset", "quantity", "target person", "target person asset", "quantity" }; 
	
	private List<PerformedTrade> performedTrades = Collections.emptyList();
	
	public TradeOverviewTableModel() {
		super();
	}

	public void updateTradeInfo(Society society) {
		this.performedTrades = society.getPerformedTrades();
		this.fireTableDataChanged();
	}
	
	@Override
	public int getRowCount() {
		return performedTrades.size();
	}

	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		PerformedTrade performedTrade = performedTrades.get(rowIndex);
		switch(columnIndex) {
		case 0:
			return performedTrade.getPersonName();
		case 1:
			return performedTrade.getAssetType().getDescription();
		case 2:
			return performedTrade.getQuantity();
		case 3:
			return performedTrade.getTargetPersonName();
		case 4:
			return performedTrade.getTargetAssetType().getDescription();
		case 5:
			return performedTrade.getTargetQuantity();
		}
		return null;
	}


}
