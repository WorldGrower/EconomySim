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

import java.awt.Panel;

import javax.swing.JLabel;

import action.PersonAction;
import environment.TimeRemaining;
import person.Person;

public class TimeRemainingPanel extends Panel {

	private JLabel lblTimeRemaining;
	private JLabel lblTimeRemainingValue;
	private TimeRemaining temporaryTimeRemaining = null;
	
	public TimeRemainingPanel() {
		this.setLayout(null);
		lblTimeRemaining = new JLabel("Time remaining:");
		lblTimeRemaining.setBounds(0, 5, 115, 24);
		add(lblTimeRemaining);
		
		lblTimeRemainingValue = new JLabel("");
		lblTimeRemainingValue.setBounds(115, 5, 125, 24);
		add(lblTimeRemainingValue);
	}
	
	public void updatePerson(Person person, TimeRemainingContainer timeRemainingContainer) {
		TimeRemaining timeRemaining = (person != null ? person.getTimeRemaining().copy() : null);
		if (timeRemaining != null) {
			temporaryTimeRemaining = timeRemaining;
			lblTimeRemainingValue.setText(temporaryTimeRemaining.toString());
		} else {
			temporaryTimeRemaining = null;
			lblTimeRemainingValue.setText("");
		}
		timeRemainingContainer.updateTimeRemaining(timeRemaining);
	}
	
	public void use(PersonAction personAction) {
		temporaryTimeRemaining.use(personAction.getTimeRequired());
		lblTimeRemainingValue.setText(temporaryTimeRemaining.toString());
	}
}
