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


import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JLabel;

import person.CauseOfDeath;
import society.DeathStatistics;
import society.Society;

public class PopulationGraphInternalFrame extends AbstractInternalNextTurnFrame implements NextTurnListener {
    private static final int xOffset = 0, yOffset = 300;

    private PopulationGraph populationGraph;
    private Society society;

    private final Map<CauseOfDeath, JLabel> causeOfDeathLabels = new EnumMap<>(CauseOfDeath.class);

	private JLabel lblNumberOfTheftsValue;
    
    public PopulationGraphInternalFrame(Society society) {
        super("Population Graphs");
        this.society = society;
        
        setBounds(xOffset, yOffset, 700, 450);

        populationGraph = new PopulationGraph(society.getPopulationStatistics());
		populationGraph.setBounds(10, 10, 590, 450);
		add(populationGraph);
		
		DeathStatistics deathStatistics = society.getDeathStatistics();
		CauseOfDeath[] causeOfDeathValues = CauseOfDeath.VALUES;
		for(int i=0; i<causeOfDeathValues.length; i++) {
			CauseOfDeath causeOfDeath = causeOfDeathValues[i];
			
			JLabel lblDescription = new JLabel("Deaths by " + causeOfDeath.getDescription() + ":");
			lblDescription.setBounds(610, 10 + i * 30, 135, 24);
			add(lblDescription);
			
			JLabel lblValue = new JLabel(Integer.toString(deathStatistics.getTotalNumberOfDeaths(causeOfDeath)));
			lblValue.setBounds(790, 10 + i * 30, 67, 24);
			add(lblValue);
			
			causeOfDeathLabels.put(causeOfDeath, lblValue);
		}
		
		int offset = 6;
		
		JLabel lblNumberOfThefts = new JLabel("Current thefts:");
		lblNumberOfThefts.setBounds(610, 10 + offset * 30, 135, 24);
		add(lblNumberOfThefts);
		
		lblNumberOfTheftsValue = new JLabel(Integer.toString(society.getLatestNumberOfThefts()));
		lblNumberOfTheftsValue.setBounds(790, 10 + offset * 30, 67, 24);
		add(lblNumberOfTheftsValue);
    }

	@Override
	public void updateInfo() {
		populationGraph.updateData(society.getPopulationStatistics());
		
		DeathStatistics deathStatistics = society.getDeathStatistics();
		for(Entry<CauseOfDeath, JLabel> entry : causeOfDeathLabels.entrySet()) {
			CauseOfDeath causeOfDeath = entry.getKey();
			entry.getValue().setText(Integer.toString(deathStatistics.getTotalNumberOfDeaths(causeOfDeath)));
		}
		
		lblNumberOfTheftsValue.setText(Integer.toString(society.getLatestNumberOfThefts()));
	}

	@Override
	public void updateSociety(Society society) {
		this.society = society;
		updateInfo();
	}
}