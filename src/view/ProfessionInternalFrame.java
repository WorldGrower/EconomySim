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

import person.Profession;
import person.ProfessionStatistics;
import society.Society;

public class ProfessionInternalFrame extends AbstractInternalNextTurnFrame implements NextTurnListener {
    private static final int xOffset = 0, yOffset = 300;

    private Society society;
    private final ProfessionStatistics professionStatistics;

    private final Map<Profession, JLabel> professionLabels = new EnumMap<>(Profession.class);

    public ProfessionInternalFrame(Society society) {
        super("Profession Overview");
        this.society = society;
        this.professionStatistics = new ProfessionStatistics(society.getReadOnlyList());
        
        setBounds(xOffset, yOffset, 700, 450);

		Profession[] professionValues = Profession.VALUES;
		for(int i=0; i<professionValues.length; i++) {
			Profession profession = professionValues[i];
			
			JLabel lblDescription = new JLabel("Profession " + profession.getDescription() + ":");
			lblDescription.setBounds(10, 10 + i * 30, 135, 24);
			add(lblDescription);
			
			JLabel lblValue = new JLabel(Integer.toString(professionStatistics.getProfessionCount(profession)));
			lblValue.setBounds(190, 10 + i * 30, 67, 24);
			add(lblValue);
			
			professionLabels.put(profession, lblValue);
		}
    }

	@Override
	public void updateInfo() {
		professionStatistics.updateInfo(society.getReadOnlyList());
		
		for(Entry<Profession, JLabel> entry : professionLabels.entrySet()) {
			Profession profession = entry.getKey();
			entry.getValue().setText(Integer.toString(professionStatistics.getProfessionCount(profession)));
		}		
	}

	@Override
	public void updateSociety(Society society) {
		this.society = society;
		updateInfo();
	}
}