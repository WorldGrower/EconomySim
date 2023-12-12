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

import action.PersonAction;
import action.PersonActionFactory;
import action.PersonActions;
import environment.TimeRemaining;
import person.Person;
import society.Society;
import view.util.DisableableList;

public class PersonActionTableModel extends AbstractTableModel implements DisableableList, TimeRemainingContainer {

	private static final String[] COLUMN_NAMES = { "Name", "Action" }; 
	
	private final Society society;
	private PersonActions personActions = PersonActions.NONE;
	private Person person;
	private TimeRemaining timeRemaining;
	
	public PersonActionTableModel(Society society) {
		super();
		this.society = society;
	}

	public void updatePerson(Person person) {
		if (person != null) {
			personActions = PersonActionFactory.createSortedPersonActions(person, society, society.getPublicLocations());
			
		} else {
			personActions = PersonActions.NONE;
		}
		this.person = person;
		this.fireTableDataChanged();
	}
	
	@Override
	public int getRowCount() {
		return personActions.size();
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
			return personActions.get(rowIndex).toString();
		case 1:
			return "execute";
		}
		return null;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 1;
	}

	@Override
	public boolean isEnabled(int rowIndex) {
		PersonAction personAction = personActions.get(rowIndex);
		return timeRemaining != null && personAction.canPerform(person, timeRemaining, society, society);
	}

	public PersonAction get(int rowIndex) {
		return personActions.get(rowIndex);
	}

	@Override
	public void updateTimeRemaining(TimeRemaining timeRemaining) {
		this.timeRemaining = timeRemaining;
	}
}
