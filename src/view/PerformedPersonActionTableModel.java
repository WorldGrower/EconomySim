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

import javax.swing.table.AbstractTableModel;

import person.PlayerPersonActions;

public class PerformedPersonActionTableModel extends AbstractTableModel {

	private static final String[] COLUMN_NAMES = { "Action" }; 
	
	private PlayerPersonActions playerPersonActions;

	public void updatePlayerPersonActions(PlayerPersonActions playerPersonActions) {
		this.playerPersonActions = playerPersonActions;
		this.fireTableDataChanged();
	}
	
	@Override
	public int getRowCount() {
		return (playerPersonActions != null ? playerPersonActions.getPersonActions().size() : 0);
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
		switch(columnIndex) {
		case 0:
			return playerPersonActions.getPersonActions().get(rowIndex);
		}
		return null;
	}
}
