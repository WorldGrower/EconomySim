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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import knowledge.Knowledge;
import person.Person;

public class PersonKnowledgeTableModel extends AbstractTableModel {

	private static final String[] COLUMN_NAMES = { "Name", "Value" }; 
	
	private Map<Knowledge, String> personKnowledgeMap;
	private List<Knowledge> knowledgeList = new ArrayList<>();
	
	public PersonKnowledgeTableModel() {
		super();
	}

	public void updatePerson(Person person) {
		if (person != null) {
			Map<Knowledge, String> knowledgeDescription = person.getKnowledgeDescription();
			this.personKnowledgeMap = new LinkedHashMap<>(knowledgeDescription);
			this.knowledgeList = new ArrayList<>(knowledgeDescription.keySet());
			Knowledge.sortOnName(knowledgeList);
		} else {
			this.personKnowledgeMap = new LinkedHashMap<>();
			this.knowledgeList = Collections.emptyList();
		}
		this.fireTableDataChanged();
	}
	
	@Override
	public int getRowCount() {
		return knowledgeList.size();
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
		Knowledge knowledge = knowledgeList.get(rowIndex);
		String description = personKnowledgeMap.get(knowledge);
		switch(columnIndex) {
		case 0:
			return knowledge.toString();
		case 1:
			return description;
		}
		return null;
	}
}
