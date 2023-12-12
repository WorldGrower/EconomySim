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


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import person.Person;
import society.Society;

public class SocietyInternalFrame extends AbstractInternalNextTurnFrame implements PersonListView, NextTurnListener {
    private static final int xOffset = 0, yOffset = 0;

    private final JTable tblPersons;
    private Society society;
    
    private JLabel lblTotalPopulation;
	private JLabel lblTotalPopulationValue;
	
	private JButton btnControlSelectedPerson;
	private JButton btnReleaseControlledPerson;
	private JLabel lblControlledPersonValue;
    
    private List<PersonListViewListener> listeners = new ArrayList<>();

	private PersonTableModel personTableModel;
    
    public SocietyInternalFrame(Society society) {
        super("Society");
        this.society = society;
        
        setBounds(xOffset, yOffset, 700, 300);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 450, 251);
		add(scrollPane);
		
		personTableModel = new PersonTableModel(society);
		tblPersons = new DefaultJTable(personTableModel);
		scrollPane.setViewportView(tblPersons);
		
		lblTotalPopulation = new JLabel("Total Population:");
		lblTotalPopulation.setBounds(470, 10, 125, 24);
		add(lblTotalPopulation);
		
		lblTotalPopulationValue = new JLabel(String.valueOf(society.size()));
		lblTotalPopulationValue.setBounds(650, 10, 67, 24);
		add(lblTotalPopulationValue);
		
		JLabel lblControlledPerson = new JLabel("Controlled Person:");
		lblControlledPerson.setBounds(470, 113, 125, 24);
		add(lblControlledPerson);
		
		lblControlledPersonValue = new JLabel("");
		lblControlledPersonValue.setBounds(470, 157, 149, 24);
		add(lblControlledPersonValue);
		
		lblControlledPersonValue.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		        if (mouseEvent.getClickCount() == 1) {
		        	Person controlledPerson = society.getControlledPerson();
		        	if (controlledPerson != null) {
		        		selectPerson(controlledPerson);
		        	}
		        }
		    }
		});
		
		btnControlSelectedPerson = new JButton("Control selected person");
		btnControlSelectedPerson.setEnabled(false);
		btnControlSelectedPerson.setBounds(470, 196, 185, 29);
		add(btnControlSelectedPerson);
		
		btnControlSelectedPerson.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tblPersons.getSelectedRow() != -1) {
					int selectedRow = tblPersons.getSelectedRow();
					Person person = getSelectedPerson(selectedRow);
					setControlledPerson(person);
				}
			}
		});
		
		btnReleaseControlledPerson = new JButton("Release controlled person");
		btnReleaseControlledPerson.setBounds(470, 235, 185, 29);
		btnReleaseControlledPerson.setEnabled(false);
		add(btnReleaseControlledPerson);
		
		btnReleaseControlledPerson.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setControlledPerson(null);
			}
		});
		
		tblPersons.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int selectedRow = tblPersons.getSelectedRow();
				Person person = getSelectedPerson(selectedRow);
				
				for(PersonListViewListener listener : listeners) {
					listener.personSelected(person);
				}
				btnControlSelectedPerson.setEnabled(selectedRow != -1);
			}
		});
    }
    
    public Society getSociety() {
		return society;
	}

	private Person getSelectedPerson(int selectedRow) {
    	return selectedRow != -1 ? society.getReadOnlyList().get(selectedRow) : null;
    }

	@Override
	public void selectPerson(Person person) {
		int index = society.getReadOnlyList().indexOf(person);
		tblPersons.setRowSelectionInterval(index, index);
	}

	@Override
	public int getSelectedPersonIndex() {
		return tblPersons.getSelectedRow();
	}
	
	public void addPersonListViewListener(PersonListViewListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void updateInfo() {
		lblTotalPopulationValue.setText(String.valueOf(society.size()));
		
		int oldSelectedIndex = tblPersons.getSelectedRow();
		((PersonTableModel)tblPersons.getModel()).fireTableDataChanged();
		tblPersons.repaint();
		boolean isOldSelectedIndexValid = oldSelectedIndex != -1 && oldSelectedIndex < tblPersons.getModel().getRowCount();
		if (isOldSelectedIndexValid) {
			tblPersons.setRowSelectionInterval(oldSelectedIndex, oldSelectedIndex);
		}
		
		btnReleaseControlledPerson.setEnabled(society.getControlledPerson() != null);
	}

	@Override
	public void setControlledPerson(Person person) {
		society.setControlledPerson(person);
		if (person != null) {
			lblControlledPersonValue.setText(society.getControlledPerson().toString());
		} else {
			lblControlledPersonValue.setText("");
		}
		btnControlSelectedPerson.setEnabled(false);
		btnReleaseControlledPerson.setEnabled(true);
	}

	@Override
	public void updateSociety(Society society) {
		this.society = society;
		this.personTableModel.updateSociety(society);
		updateInfo();
	}
}