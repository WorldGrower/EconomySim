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

import organization.SocialObligations;
import person.Person;
import view.util.DisableableList;

public class PersonSocialObligationsTableModel extends AbstractTableModel implements DisableableList {

	private static final String[] COLUMN_NAMES = { "Name", "Action" }; 
	
	private Person person;
	
	public PersonSocialObligationsTableModel() {
		super();
	}

	public void updatePerson(Person person) {
		if (person != null) {
			this.person = person;
		} else {
			this.person = null;
		}
		this.fireTableDataChanged();
	}
	
	@Override
	public int getRowCount() {
		if (person != null) {
			return person.getSocialObligations().size();
		} else {
			return 0;
		}
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
		SocialObligations socialObligations = person.getSocialObligations();
		switch(columnIndex) {
		case 0:
			return socialObligations.getDescription(rowIndex);
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
		SocialObligations socialObligations = person.getSocialObligations();
		return socialObligations.canFullfill(rowIndex, person);
	}
}
