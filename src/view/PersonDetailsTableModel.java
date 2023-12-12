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

import person.Person;

public class PersonDetailsTableModel extends AbstractTableModel {

	private static final String[] COLUMN_NAMES = { "Name", "Value" }; 
	private static final String[] ROW_NAMES ={ "Name", "Age", "Sex", "Partner", "Children", "Parents", "Pregnancy", "Food", "Water", "Profession" };
	
	private Person person = null;
	
	public PersonDetailsTableModel() {
		super();
	}

	public void updatePerson(Person person) {
		this.person = person;
		this.fireTableDataChanged();
	}
	
	@Override
	public int getRowCount() {
		return (person != null ? ROW_NAMES.length : 0);
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
		if (columnIndex == 0) {
			return ROW_NAMES[rowIndex];
		} else {
			switch(rowIndex) {
			case 0:
				return person.toString();
			case 1:
				return person.getAge();
			case 2:
				return person.getSex();
			case 3:
				return person.getFamily().getPartner();
			case 4:
				return person.getFamily().getChildren();
			case 5:
				return person.getFamily().getParents();
			case 6:
				return person.isPregnant();
			case 7:
				return person.getFoodDescription();
			case 8:
				return person.getWaterDescription();
			case 9:
				return person.getProfession().getDescription();
			}
		}
		return null;
	}

}
