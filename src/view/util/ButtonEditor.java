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
package view.util;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

public class ButtonEditor extends DefaultCellEditor {
	private final SharedJButton button;
	private String label;
	private DisableableList disableableList;

	public ButtonEditor(JCheckBox checkBox, SharedJButton button, DisableableList disableableList) {
		super(checkBox);
		this.button = button;
		this.disableableList = disableableList;
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		label = (value == null) ? "Modify" : value.toString();
		button.setText(label);
		button.setEnabled(disableableList.isEnabled(row));
		//workaround: selection (getSelectedRow) doesn't work if button is in JTable
		button.setRow(row);
		return button;
	}

	public Object getCellEditorValue() {
		return new String(label);
	}
}