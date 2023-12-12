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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import action.OrganizationArgs;
import action.PersonAction;
import action.PersonActionArgs;
import action.TradeArgs;
import knowledge.Knowledge;
import organization.AvailableOrganizationPoliciesImpl;
import organization.Organization;
import person.Person;
import person.PersonDecisions;
import person.PlayerPersonActions;
import society.Society;
import view.util.SharedJButton;

public class PersonDetailsInternalFrame extends AbstractInternalNextTurnFrame implements PersonListViewListener, NextTurnListener, PlayerPersonActionsContainer {
    private static final int xOffset = 700, yOffset = 0;
    private static final int TABLE_HEIGHT = 184;

    private Society society;
    
	private JTable tblPersonDetails;
	private JTable tblPersonAssets;
	private JTable tblPersonActions;
	private PlayerPersonActions playerPersonActions;
	private PersonActionTableModel personActionTableModel;
	private JTable tblPersonKnowledge;
	private TimeRemainingPanel timeRemainingPanel;

	private PersonKnowledgeTableModel personKnowledgeTableModel;

	private PersonDetailsTableModel personDetailsTableModel;

	private PersonAssetsTableModel personAssetsTableModel;

	private PerformedPersonActionTableModel performedPersonActionTableModel;
    private PersonSocialObligationsTableModel personSocialObligationsTableModel;
	
	private PersonDecisions personBehaviour;
	private JTable tblPersonSocialObligations;
	
    public PersonDetailsInternalFrame(Society society, PersonListView personListView) {
        super("Person Details");
        this.society = society;
        this.personBehaviour = new PersonDecisionsView(society);
        
        setBounds(xOffset, yOffset, 620, 542);

        JLabel lblPersonDetails = new JLabel("Person details:");
		lblPersonDetails.setBounds(7, 10, 125, 24);
		getContentPane().add(lblPersonDetails);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(7, 42, 283, TABLE_HEIGHT);
		getContentPane().add(scrollPane_1);
		
		personDetailsTableModel = new PersonDetailsTableModel();
		tblPersonDetails = new DefaultJTable(personDetailsTableModel);
		scrollPane_1.setViewportView(tblPersonDetails);
		
		tblPersonDetails.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		        JTable table = (JTable) mouseEvent.getSource();
		        Point point = mouseEvent.getPoint();
		        int row = table.rowAtPoint(point);
		        if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1 && row != -1) {
		        	Object value = table.getValueAt(row, 1);
		        	if (value instanceof Person) {
		        		Person person = (Person) value;
		        		selectPerson(person);
		        	}
		        	if (value instanceof List) {
		        		List objects = (List) value;
		        		if (objects.size() > 0) {
		        			if (objects.get(0) instanceof Person) {
		        				if (objects.size() == 1) {
		        					Person person = (Person) objects.get(0);
		        					selectPerson(person);
		        				} else {
			        				Person person = (Person) InputDialog.chooseFromList(objects, "Choose Person");
			        				if (person != null) {
			        					selectPerson(person);
			        				}
		        				}
		        			}
		        		}
		        	}
		        }
		    }
		    
		    private void selectPerson(Person person) {
		    	personListView.selectPerson(person);
		    }
		});
		
		JLabel lblPersonAssets = new JLabel("Person assets:");
		lblPersonAssets.setBounds(313, 10, 125, 24);
		getContentPane().add(lblPersonAssets);
		
		JScrollPane scrollPane_1_1 = new JScrollPane();
		scrollPane_1_1.setBounds(313, 42, 283, TABLE_HEIGHT);
		getContentPane().add(scrollPane_1_1);
		
		personAssetsTableModel = new PersonAssetsTableModel();
		tblPersonAssets = new DefaultJTable(personAssetsTableModel);
		scrollPane_1_1.setViewportView(tblPersonAssets);
		
		JLabel lblPersonActions = new JLabel("Person actions:");
		lblPersonActions.setBounds(7, 237, 125, 24);
		getContentPane().add(lblPersonActions);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(7, 275, 283, TABLE_HEIGHT);
		getContentPane().add(scrollPane_2);
		
		personActionTableModel = new PersonActionTableModel(society);
		tblPersonActions = new DefaultJTable(personActionTableModel);
		SharedJButton personActionButton = new SharedJButton(tblPersonActions.getColumn("Action"), personActionTableModel);
		int actionColumnWidth = 85;
		tblPersonActions.getColumnModel().getColumn(1).setPreferredWidth(actionColumnWidth);
		tblPersonActions.getColumnModel().getColumn(1).setMaxWidth(actionColumnWidth);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(313, 509, 283, 141);
		getContentPane().add(scrollPane_4);
		
		performedPersonActionTableModel = new PerformedPersonActionTableModel();
		JTable tblPerformedPersonActions = new DefaultJTable(performedPersonActionTableModel);
		scrollPane_4.setViewportView(tblPerformedPersonActions);
		
		personActionButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (personActionButton.getRow() != -1) {
					int selectedRow = personListView.getSelectedPersonIndex();
					Person person = getSelectedPerson(personListView);
					
					PersonAction personAction = personActionTableModel.get(personActionButton.getRow());
					
					if (playerPersonActions == null) {
						playerPersonActions = new PlayerPersonActions(person);
					}
					
					Person targetPerson = null;
					List<Person> targetPersons = personAction.getPossibleTargetPersons(person, society, society, Integer.MAX_VALUE);
					if (targetPersons.size() > 0) {
						targetPerson = (Person) InputDialog.chooseFromList(targetPersons, "Choose person");
						if (targetPerson == null) {
							return;
						}
					}
					
					Knowledge knowledge = null;
					List<Knowledge> knowledgeList = personAction.getPossibleKnowledge(person, targetPerson);
					if (knowledgeList.size() > 0) {
						knowledge = (Knowledge) InputDialog.chooseFromList(knowledgeList, "Choose knowledge"); 
						if (knowledge == null) {
							return;
						}
					}

					TradeArgs tradeArgs = null;
					if (personAction.requiresTradeArgs()) {
						tradeArgs = TradeArgsDialog.choose(person, targetPerson);
						if (tradeArgs == null) {
							return;
						}
					}
					
					OrganizationArgs organizationArgs = null;
					if (personAction.requiresDecisionCriteria() || personAction.requiresOrganizationPolicy()) {
						Organization organization = society.findOrganization();
						organizationArgs = OrganizationDialog.createOrganizationArgs(
								person,
								organization.getDecisionCriteriaType(),
								personAction.requiresDecisionCriteria(), 
								organization.getOrganizationPolicy(),
								new AvailableOrganizationPoliciesImpl(personAction, society)
						);
						if (organizationArgs == null) {
							return;
						}
					}

					PersonActionArgs args = new PersonActionArgs(targetPerson, knowledge, tradeArgs, organizationArgs, personBehaviour);
					playerPersonActions.addPersonAction(personAction, args);
					
					timeRemainingPanel.use(personAction);
					
					personListView.setControlledPerson(person);
					
					//workaround for fact that button doesn't get reloaded using editor
					personActionButton.setEnabled(personActionTableModel.isEnabled(personActionButton.getRow()));
					
					((PersonActionTableModel) tblPersonActions.getModel()).fireTableDataChanged();
					tblPersonActions.repaint();
					
					performedPersonActionTableModel.updatePlayerPersonActions(playerPersonActions);
				}
			}
		});		
		
		scrollPane_2.setViewportView(tblPersonActions);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(313, 275, 282, TABLE_HEIGHT);
		getContentPane().add(scrollPane_3);
		
		personKnowledgeTableModel = new PersonKnowledgeTableModel();
		tblPersonKnowledge = new DefaultJTable(personKnowledgeTableModel);
		scrollPane_3.setViewportView(tblPersonKnowledge);
		
		JLabel lblPersonKnowledge = new JLabel("Person knowledge:");
		lblPersonKnowledge.setBounds(313, 237, 125, 24);
		getContentPane().add(lblPersonKnowledge);
    
		timeRemainingPanel = new TimeRemainingPanel();
		timeRemainingPanel.setBounds(313, 457, 280, 37);
		this.getContentPane().add(timeRemainingPanel);
		
		JLabel lblPersonSocialObligations = new JLabel("Person social obligations:");
		lblPersonSocialObligations.setBounds(7, 466, 175, 24);
		getContentPane().add(lblPersonSocialObligations);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(7, 510, 282, 141);
		getContentPane().add(scrollPane);
		
		personSocialObligationsTableModel = new PersonSocialObligationsTableModel();
		tblPersonSocialObligations = new DefaultJTable(personSocialObligationsTableModel);
		scrollPane.setViewportView(tblPersonSocialObligations);
		
		
		SharedJButton personSocialObligationButton = new SharedJButton(tblPersonSocialObligations.getColumn("Action"), personSocialObligationsTableModel);
		personSocialObligationButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (personSocialObligationButton.getRow() != -1) {
					Person person = getSelectedPerson(personListView);
					
					int index = personSocialObligationButton.getRow();
					
					person.getSocialObligations().fullfill(index, person);
					
					//TODO: update & repaint
				}
			}
		});
    }
    
    private Person getSelectedPerson(PersonListView personListView) {
    	int selectedRow = personListView.getSelectedPersonIndex();
		return society.getReadOnlyList().get(selectedRow);
    }

	@Override
	public void personSelected(Person person) {
		personDetailsTableModel.updatePerson(person);
		personAssetsTableModel.updatePerson(person);
		personActionTableModel.updatePerson(person);
		personKnowledgeTableModel.updatePerson(person);
		personSocialObligationsTableModel.updatePerson(person);
		
		timeRemainingPanel.updatePerson(person, personActionTableModel);
		
		playerPersonActions = null;
		performedPersonActionTableModel.updatePlayerPersonActions(playerPersonActions);
		tblPersonActions.repaint();
	}

	@Override
	public void updateInfo() {
		Person selectedPerson = society.getControlledPerson(); 
		timeRemainingPanel.updatePerson(selectedPerson, personActionTableModel);
	}

	@Override
	public PlayerPersonActions getPlayerPersonActions() {
		Person selectedPerson = society.getControlledPerson();
		return playerPersonActions != null ? playerPersonActions : new PlayerPersonActions(selectedPerson);
	}

	@Override
	public void updateSociety(Society society) {
		this.society = society;
		this.personBehaviour = new PersonDecisionsView(society);
		updateInfo();
	}
}