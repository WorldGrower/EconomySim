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
import java.awt.event.ItemListener;
import java.text.NumberFormat;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import asset.AssetType;
import organization.AvailableOrganizationPolicies;
import organization.DecisionCriteriaType;
import organization.OrganizationPolicy;
import society.TheftPunishment;

public class OrganizationPanel extends JPanel {

	private JRadioButton rbnAutocracy;
	private JRadioButton rbnOligarchy;
	private JRadioButton rbnRepublic;

	private JComboBox cmbThiefPunishment;
	private JLabel lblTaxesAssetType;
	private JComboBox cmbTaxesAssetType;
	private JFormattedTextField txtTaxesQuantity;
	private JComboBox cmbWageAssetType;
	private JFormattedTextField txtWageQuantity;
	private JFormattedTextField txtNumberOfHoursWorked;

	public OrganizationPanel(DecisionCriteriaType oldDecisionCriteriaType, boolean selectDecisionCriteria, OrganizationPolicy oldOrganizationPolicy, AvailableOrganizationPolicies availableOrganizationPolicies) {

		setSize(462, 306);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(null);

		ButtonGroup buttonGroup = new ButtonGroup();

		rbnAutocracy = new JRadioButton("Autocracy");
		rbnAutocracy.setBounds(6, 32, 103, 21);
		add(rbnAutocracy);
		buttonGroup.add(rbnAutocracy);

		rbnOligarchy = new JRadioButton("Oligarchy");
		rbnOligarchy.setBounds(6, 81, 103, 21);
		add(rbnOligarchy);
		buttonGroup.add(rbnOligarchy);

		rbnRepublic = new JRadioButton("Republic");
		rbnRepublic.setBounds(6, 131, 103, 21);
		add(rbnRepublic);
		buttonGroup.add(rbnRepublic);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Policy", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(146, 10, 306, 286);
		add(panel);
		panel.setLayout(null);

		JLabel lblTheftPunishment = new JLabel("Theft punishment");
		lblTheftPunishment.setBounds(10, 33, 118, 13);
		panel.add(lblTheftPunishment);

		cmbThiefPunishment = new JComboBox();
		cmbThiefPunishment.setModel(new DefaultComboBoxModel<>(TheftPunishment.VALUES));
		cmbThiefPunishment.setBounds(138, 33, 158, 21);
		panel.add(cmbThiefPunishment);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		add(buttonPane, BorderLayout.SOUTH);

		lblTaxesAssetType = new JLabel("Taxes Asset type:");
		lblTaxesAssetType.setBounds(10, 84, 118, 13);
		panel.add(lblTaxesAssetType);
		
		cmbTaxesAssetType = new JComboBox(getAssetTypes());
		cmbTaxesAssetType.setBounds(138, 84, 158, 21);
		panel.add(cmbTaxesAssetType);
		
		JLabel lblTaxesQuantity = new JLabel("Taxes Quantity:");
		lblTaxesQuantity.setBounds(10, 119, 118, 13);
		panel.add(lblTaxesQuantity);
		
		txtTaxesQuantity = new JFormattedTextField(NumberFormat.getIntegerInstance());
		txtTaxesQuantity.setBounds(138, 120, 158, 19);
		panel.add(txtTaxesQuantity);
		
		JLabel lblWageAssetType = new JLabel("Wage Asset type:");
		lblWageAssetType.setBounds(10, 174, 118, 13);
		panel.add(lblWageAssetType);
		
		cmbWageAssetType = new JComboBox(getAssetTypes());
		cmbWageAssetType.setBounds(138, 174, 158, 21);
		panel.add(cmbWageAssetType);
		
		JLabel lblWageQuantity = new JLabel("Wage Quantity:");
		lblWageQuantity.setBounds(10, 209, 118, 13);
		panel.add(lblWageQuantity);
		
		txtWageQuantity = new JFormattedTextField(NumberFormat.getIntegerInstance());
		txtWageQuantity.setBounds(138, 210, 158, 19);
		panel.add(txtWageQuantity);
		
		JLabel lblNumberOfHoursWorked = new JLabel("Hours worked:");
		lblNumberOfHoursWorked.setBounds(10, 242, 118, 13);
		panel.add(lblNumberOfHoursWorked);
		
		txtNumberOfHoursWorked = new JFormattedTextField(NumberFormat.getIntegerInstance());
		txtNumberOfHoursWorked.setBounds(138, 239, 158, 19);
		panel.add(txtNumberOfHoursWorked);

		setRadioButtonsEnabled(selectDecisionCriteria);
		cmbThiefPunishment.setEnabled(availableOrganizationPolicies.canChangeTheftPolicy());
		cmbTaxesAssetType.setEnabled(availableOrganizationPolicies.canChangeTaxesWages());
		txtTaxesQuantity.setEnabled(availableOrganizationPolicies.canChangeTaxesWages());
		cmbWageAssetType.setEnabled(availableOrganizationPolicies.canChangeTaxesWages());
		txtWageQuantity.setEnabled(availableOrganizationPolicies.canChangeTaxesWages());
		txtNumberOfHoursWorked.setEnabled(availableOrganizationPolicies.canChangeTaxesWages());
		
		updateInfo(oldDecisionCriteriaType, oldOrganizationPolicy);
	}

	private AssetType[] getAssetTypes() {
		return AssetType.VALUES;
	}

	public void addItemListener(ItemListener itemListener) {
		rbnAutocracy.addItemListener(itemListener);
		rbnOligarchy.addItemListener(itemListener);
		rbnRepublic.addItemListener(itemListener);
	}

	private void setRadioButtonsEnabled(boolean enabled) {
		rbnAutocracy.setEnabled(enabled);
		rbnOligarchy.setEnabled(enabled);
		rbnRepublic.setEnabled(enabled);
	}
	
	public DecisionCriteriaType getDecisionCriteriaType() {
		if (rbnAutocracy.isSelected()) {
			return DecisionCriteriaType.AUTOCRACY;
		} else if (rbnOligarchy.isSelected()) {
			return DecisionCriteriaType.OLIGARCHY;
		} else if (rbnRepublic.isSelected()) {
			return DecisionCriteriaType.REPUBLIC;
		} else {
			return null;
		}
	}
	
	public OrganizationPolicy getOrganizationPolicy() {
		return new OrganizationPolicy(
				(TheftPunishment) cmbThiefPunishment.getSelectedItem(),
				(AssetType) cmbTaxesAssetType.getSelectedItem(),
				Integer.parseInt(txtTaxesQuantity.getText()),
				(AssetType) cmbWageAssetType.getSelectedItem(),
				Integer.parseInt(txtWageQuantity.getText()),
				Integer.parseInt(txtNumberOfHoursWorked.getText())
		);
	}

	public void updateInfo(DecisionCriteriaType decisionCriteriaType, OrganizationPolicy organizationPolicy) {
		if (decisionCriteriaType == DecisionCriteriaType.AUTOCRACY) {
			rbnAutocracy.setSelected(true);
		} else if (decisionCriteriaType == DecisionCriteriaType.OLIGARCHY) {
			rbnOligarchy.setSelected(true);
		} else if (decisionCriteriaType == DecisionCriteriaType.REPUBLIC) {
			rbnRepublic.setSelected(true);
		}

		cmbThiefPunishment.setSelectedItem(organizationPolicy != null ? organizationPolicy.getTheftPunishment() : null);
		cmbTaxesAssetType.setSelectedItem(organizationPolicy != null ? organizationPolicy.getTaxes().getAssetType() : null);
		txtTaxesQuantity.setText(organizationPolicy != null ? Integer.toString(organizationPolicy.getTaxes().getQuantity()) : "0");
		cmbWageAssetType.setSelectedItem(organizationPolicy != null ? organizationPolicy.getSocialOrderMaintainerWage().getAssetType() : null);
		txtWageQuantity.setText(organizationPolicy != null ? Integer.toString(organizationPolicy.getSocialOrderMaintainerWage().getQuantity()) : "0");
		txtNumberOfHoursWorked.setText(organizationPolicy != null ? Integer.toString(organizationPolicy.getSocialOrderMaintainerHoursWorked()) : "0");
	}
}
