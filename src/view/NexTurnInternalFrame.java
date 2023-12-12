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


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import society.Society;

public class NexTurnInternalFrame extends AbstractInternalFrame {
    private static final String TIME_TAKEN = "Time taken: ";

	private static final int xOffset = 700, yOffset = 595;

    private final PlayerPersonActionsContainer playerPersonActionsContainer;
    private JButton btnNextMultipleTurns;
    private Society society;
    
    private List<NextTurnListener> listeners = new ArrayList<>();
    private PersonDecisionsView personBehaviourView;

	private JLabel lblTimeTaken;
	    
    public NexTurnInternalFrame(Society society, PlayerPersonActionsContainer playerPersonActionsContainer) {
        super("Next Turn");
        this.society = society;
        this.playerPersonActionsContainer = playerPersonActionsContainer;
        this.personBehaviourView = new PersonDecisionsView(society);
        
        setBounds(xOffset, yOffset, 620, 80);

        JButton btnNextTurn = new JButton("Next Turn");
		btnNextTurn.addActionListener(e -> nextTurn(1));
		btnNextTurn.setBounds(251, 9, 125, 29);
		add(btnNextTurn);
		
		NumberFormat format = NumberFormat.getIntegerInstance();
		JFormattedTextField fldNumberOfTurns = new JFormattedTextField(format);
		fldNumberOfTurns.setValue(10);
		fldNumberOfTurns.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					nextTurn(getFieldValue(fldNumberOfTurns));
				}
			}
		});
		fldNumberOfTurns.setBounds(39, 12, 67, 24);
		add(fldNumberOfTurns);
		
		btnNextMultipleTurns = new JButton("Next Turns");
		btnNextMultipleTurns.addActionListener(e -> nextTurn(getFieldValue(fldNumberOfTurns)));
		btnNextMultipleTurns.setBounds(116, 9, 125, 29);
		add(btnNextMultipleTurns);
		
		lblTimeTaken = new JLabel(TIME_TAKEN);
		lblTimeTaken.setBounds(390, 9, 125, 29);
		add(lblTimeTaken);
    }
    
    private int getFieldValue(JFormattedTextField fldNumberOfTurns) {
		Number value = (Number) fldNumberOfTurns.getValue();
		return value.intValue();
	}
    
    private void nextTurn(int numberOfTurns) {
    	long startTime = System.currentTimeMillis();
		society.nextTurn(numberOfTurns, playerPersonActionsContainer.getPlayerPersonActions(), personBehaviourView);
		
		for(NextTurnListener listener : listeners) {
			if (listener.needsUpdating()) {
				listener.updateInfo();
			}
		}
		long endTime = System.currentTimeMillis();
		if (numberOfTurns > 1) {
			lblTimeTaken.setText(TIME_TAKEN + (endTime - startTime) + " ms");
		}
	}
    
    public void addNextTurnListener(NextTurnListener listener) {
    	listeners.add(listener);
    }

	@Override
	public void updateSociety(Society society) {
		this.society = society;
		this.personBehaviourView = new PersonDecisionsView(society);
	}
}