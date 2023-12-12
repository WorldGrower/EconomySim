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
import society.Society;

public class PersonTableModel extends AbstractTableModel {

	private static final String[] COLUMN_NAMES = { "Name", "Age", "Sex", "Cash", "Partner", "Pregnancy" };
	
	private Society society;

	public PersonTableModel(Society society) {
		super();
		this.society = society;
	}

	@Override
	public int getRowCount() {
		return society.size();
	}

	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}
	
	public void updateSociety(Society society) {
		this.society = society;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Person person = society.getReadOnlyList().get(rowIndex);
		switch(columnIndex) {
		case 0:
			return person.toString();
		case 1:
			return person.getAge();
		case 2:
			return person.getSex();
		case 3:
			return person.getCash();
		case 4:
			return person.getFamily().getPartner();
		case 5:
			return person.isPregnant();
		}
		return null;
	}

}
