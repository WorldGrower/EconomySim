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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.table.TableColumn;

public class SharedJButton extends JButton {
	private int row;

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
	
	public SharedJButton(TableColumn tableColumn, DisableableList disableableList) {
		tableColumn.setCellRenderer(new ButtonRenderer(disableableList));
		tableColumn.setCellEditor(new ButtonEditor(new JCheckBox(), this, disableableList));
	}
}
