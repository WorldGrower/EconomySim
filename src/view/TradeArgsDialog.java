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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import action.TradeArgs;
import asset.Asset;
import asset.AssetType;
import asset.SimpleAsset;
import person.Person;
import person.Sex;

public class TradeArgsDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private TradeArgs tradeArgs;
	private JList<AssetType> lstPersonAssets;
	private JList<AssetType> lstTargetPersonAssets;
	private JSpinner personSpinner;
	private JSpinner targetPersonSpinner;
	private JButton okButton;

	public static void main(String[] args) {
		try {
			Person person = new Person(18, Sex.MALE);
			person.getAssets().addAsset(new SimpleAsset(AssetType.FOOD, 100));
			person.getAssets().addAsset(new SimpleAsset(AssetType.CLAY_POT, 50));
			Person targetPerson = new Person(18, Sex.MALE);
			targetPerson.getAssets().addAsset(new SimpleAsset(AssetType.FOOD, 20));
			targetPerson.getAssets().addAsset(new SimpleAsset(AssetType.FLOUR, 50));
			TradeArgsDialog dialog = new TradeArgsDialog(person ,targetPerson);
			dialog.showDialog();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private TradeArgsDialog(Person person, Person targetPerson) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		setBounds(100, 100, 635, 433);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblPerson = new JLabel("Person:");
		lblPerson.setBounds(28, 26, 138, 23);
		contentPanel.add(lblPerson);
		
		JLabel lblPersonValue = new JLabel(person.toString());
		lblPersonValue.setBounds(28, 64, 138, 23);
		contentPanel.add(lblPersonValue);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(28, 112, 202, 129);
		contentPanel.add(scrollPane);
		
		lstPersonAssets = new JList(person.getAssets().keys().toArray());
		scrollPane.setViewportView(lstPersonAssets);
		
		JLabel lblTargetPerson = new JLabel("Target Person:");
		lblTargetPerson.setBounds(348, 26, 138, 23);
		contentPanel.add(lblTargetPerson);
		
		JLabel lblTargetPersonValue = new JLabel(targetPerson.toString());
		lblTargetPersonValue.setBounds(348, 64, 138, 23);
		contentPanel.add(lblTargetPersonValue);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(349, 113, 200, 127);
		contentPanel.add(scrollPane_1);
		
		lstTargetPersonAssets = new JList(targetPerson.getAssets().keys().toArray());
		scrollPane_1.setViewportView(lstTargetPersonAssets);
		
		personSpinner = createJSpinner(new SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
		personSpinner.setEnabled(false);
		personSpinner.setBounds(180, 267, 50, 20);
		contentPanel.add(personSpinner);
		
		targetPersonSpinner = createJSpinner(new SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
		targetPersonSpinner.setBounds(496, 267, 50, 20);
		targetPersonSpinner.setEnabled(false);
		contentPanel.add(targetPersonSpinner);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		okButton = new JButton("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		okButton.setEnabled(false);
		okButton.addActionListener(this::ok);
	
		JButton cancelButton = new JButton("Cancel");
		buttonPane.add(cancelButton);
		cancelButton.addActionListener(this::cancel);

		addListSelectionListener(lstPersonAssets, personSpinner, person);
		addListSelectionListener(lstTargetPersonAssets, targetPersonSpinner, targetPerson);
		
		lstPersonAssets.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				setOkButtonEnabled();
			}
		});
		lstTargetPersonAssets.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				setOkButtonEnabled();
			}
		});
	}
	
	private void setOkButtonEnabled() {
		okButton.setEnabled(personSpinner.isEnabled() && targetPersonSpinner.isEnabled());
	}
	
	private void ok(ActionEvent e) {
		tradeArgs = new TradeArgs(
				lstPersonAssets.getSelectedValue(), 
				(Integer) personSpinner.getValue(), 
				lstTargetPersonAssets.getSelectedValue(), 
				(Integer) targetPersonSpinner.getValue()
		);
		this.dispose();
	}
	
	private void cancel(ActionEvent e) {
		tradeArgs = null;
		this.dispose();
	}
	
	private static void addListSelectionListener(JList<AssetType> list, JSpinner spinner, Person person) {
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				AssetType assetType = list.getSelectedValue();
				Asset asset = person.getAssets().get(assetType);
				spinner.setEnabled(true);
				spinner.setModel(new SpinnerNumberModel(Integer.valueOf(asset.getQuantity()), Integer.valueOf(0), Integer.valueOf(asset.getQuantity()), Integer.valueOf(1)));
			}
		});
	}
	
	private JSpinner createJSpinner(SpinnerModel spinnerModel) {
		JSpinner spinner = new JSpinner(spinnerModel);
		((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().setEditable(false);
		return spinner;
	}
	
	public void showDialog() {
		setVisible(true);
	}

	public static TradeArgs choose(Person person, Person targetPerson) {
		TradeArgsDialog tradeArgsDialog = new TradeArgsDialog(person, targetPerson);
		tradeArgsDialog.showDialog();
		return tradeArgsDialog.tradeArgs;
	}
}
