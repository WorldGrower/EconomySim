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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import action.OrganizationArgs;
import organization.AvailableOrganizationPolicies;
import organization.DecisionCriteria;
import organization.DecisionCriteriaFactory;
import organization.DecisionCriteriaType;
import organization.OrganizationPolicy;
import person.Person;
import person.Sex;

public class OrganizationDialog extends JDialog {

	private final OrganizationPanel contentPanel;

	private final Person person;
	private OrganizationArgs organizationArgs;

	private JButton okButton;

	private OrganizationDialog(Person person, DecisionCriteriaType oldDecisionCriteriaType, boolean selectDecisionCriteria, OrganizationPolicy oldOrganizationPolicy, AvailableOrganizationPolicies availableOrganizationPolicies) {
		this.person = person;
		this.contentPanel = new OrganizationPanel(oldDecisionCriteriaType, selectDecisionCriteria, oldOrganizationPolicy, availableOrganizationPolicies);
		
		setBounds(100, 100, 468, 376);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		okButton.setEnabled(false);
		okButton.addActionListener(this::ok);
	
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(this::cancel);
		buttonPane.add(cancelButton);
		
		contentPanel.addItemListener(this::radioButtonSelected);
	}
	
	private void radioButtonSelected(ItemEvent e) {
		okButton.setEnabled(true);
	}
	
	private void ok(ActionEvent e) {
		DecisionCriteriaType decisionCriteriaType = contentPanel.getDecisionCriteriaType();
		final DecisionCriteria decisionCriteria;
		if (decisionCriteriaType == DecisionCriteriaType.AUTOCRACY) {
			decisionCriteria = DecisionCriteriaFactory.createAutocracy(person);
		} else if (decisionCriteriaType == DecisionCriteriaType.OLIGARCHY) {
			decisionCriteria = DecisionCriteriaFactory.createOligarchy();
		} else if (decisionCriteriaType == DecisionCriteriaType.REPUBLIC) {
			decisionCriteria = DecisionCriteriaFactory.createRepublic();
		} else {
			decisionCriteria = null;
		}
		OrganizationPolicy organizationPolicy = contentPanel.getOrganizationPolicy();
		
		organizationArgs = new OrganizationArgs(decisionCriteria, organizationPolicy);
		this.dispose();
	}
	
	private void cancel(ActionEvent e) {
		organizationArgs = null;
		this.dispose();
	}
	
	public void showDialog() {
		setVisible(true);
	}
	
	public static OrganizationArgs createOrganizationArgs(Person person, DecisionCriteriaType oldDecisionCriteriaType, boolean selectDecisionCriteria, OrganizationPolicy oldOrganizationPolicy, AvailableOrganizationPolicies availableOrganizationPolicies) {
		if (!selectDecisionCriteria && oldDecisionCriteriaType == null) {
			throw new IllegalArgumentException("oldDecisionCriteriaType is null while selectDecisionCriteria is false");
		}
		
		OrganizationDialog organizationDialog = new OrganizationDialog(person, oldDecisionCriteriaType, selectDecisionCriteria, oldOrganizationPolicy, availableOrganizationPolicies);
		organizationDialog.showDialog();
		return organizationDialog.organizationArgs;
	}
	
	public static void main(String[] args) {
		Person person = new Person(18, Sex.MALE);
		DecisionCriteriaType oldDecisionCriteriaType = DecisionCriteriaType.AUTOCRACY;
		boolean selectDecisionCriteria = false;
		OrganizationPolicy oldOrganizationPolicy = OrganizationPolicy.DEFAULT;
		createOrganizationArgs(person, oldDecisionCriteriaType, selectDecisionCriteria, oldOrganizationPolicy, AvailableOrganizationPolicies.READONLY);
	}
}
