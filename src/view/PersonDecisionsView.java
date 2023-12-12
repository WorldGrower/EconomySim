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

import javax.swing.JOptionPane;

import organization.Organization;
import organization.OrganizationPolicy;
import person.Person;
import person.PersonDecisions;
import society.Society;

public class PersonDecisionsView implements PersonDecisions {

	private final Society society;
	
	public PersonDecisionsView(Society society) {
		super();
		this.society = society;
	}

	@Override
	public boolean wantsToPartnerUp(Person askedPerson, Person askingPerson) {
		if (askedPerson == society.getControlledPerson()) {
			return ask(askedPerson, askingPerson, "Person " + askingPerson.toString() + " wants to partner up, do you accept?", "Partner up");
		} else {
			return AI.wantsToPartnerUp(askedPerson, askingPerson);
		}
	}

	@Override
	public boolean wantsToSex(Person askedPerson, Person askingPerson) {
		if (askedPerson == society.getControlledPerson()) {
			return ask(askedPerson, askingPerson, "Person " + askingPerson.toString() + " wants to have sex, do you accept?", "Sex");
		} else {
			return AI.wantsToSex(askedPerson, askingPerson);
		}
	}

	@Override
	public boolean personWantsToJoin(Organization organization, Person askedPerson, Person askingPerson) {
		if (askedPerson == society.getControlledPerson()) {
			return ask(askedPerson, askingPerson, "Person " + askingPerson.toString() + " wants you to join " + organization + ", do you accept?", "Join");
		} else {
			return AI.personWantsToJoin(organization, askedPerson, askingPerson);
		}
	}
	
	private boolean ask(Person askedPerson, Person askingPerson, String question, String title) {
		int selectedOption = JOptionPane.showConfirmDialog(null, 
                question, 
                title, 
                JOptionPane.YES_NO_OPTION); 
		return (selectedOption == JOptionPane.YES_OPTION);
	}

	@Override
	public boolean approvePolicyChange(Person askedPerson, Person initiator, OrganizationPolicy oldOrganizationPolicy, OrganizationPolicy newOrganizationPolicy) {
		if (askedPerson == society.getControlledPerson()) {
			return ask(askedPerson, initiator, "Person " + initiator.toString() + " wants to change policy: " + oldOrganizationPolicy + " to " + newOrganizationPolicy + ", do you approve?", "Approve");
		} else {
			return AI.approvePolicyChange(askedPerson, initiator, oldOrganizationPolicy, newOrganizationPolicy);
		}
	}

	@Override
	public boolean acceptsJob(Person askedPerson, Person askingPerson) {
		if (askedPerson == society.getControlledPerson()) {
			return ask(askedPerson, askingPerson, "Person " + askingPerson.toString() + " offers you a job, do you Accept?", "Job offer");
		} else {
			return AI.acceptsJob(askedPerson, askingPerson);
		}
	}
}
