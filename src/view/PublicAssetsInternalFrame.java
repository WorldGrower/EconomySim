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


import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import society.PublicAssetsDescription;
import society.Society;

public class PublicAssetsInternalFrame extends AbstractInternalNextTurnFrame implements NextTurnListener {
	private static final int xOffset = 0, yOffset = 300;

    private Society society;
    
    private JLabel lblSocietyGold;
	private JLabel lblSocietyGoldValue;
	private JLabel lblSocietyLand;
	private JLabel lblSocietyLandValue;
	private JLabel lblRiverUsage;
	private JLabel lblRiverUsageValue;
	private JLabel lblIrrigationCanalOperational;
	private JLabel lblIrrigationCanalOperationalValue;

	private PublicAssetsTableModel publicAssetsTableModel;
	
    public PublicAssetsInternalFrame(Society society) {
        super("Public Assets");
        this.society = society;
        
        setBounds(xOffset, yOffset, 700, 450);

		lblSocietyGold = new JLabel("Unclaimed Cash:");
		lblSocietyGold.setBounds(10, 10, 125, 24);
		add(lblSocietyGold);
		
		lblSocietyGoldValue = new JLabel(String.valueOf(society.getCash()));
		lblSocietyGoldValue.setBounds(250, 10, 67, 24);
		add(lblSocietyGoldValue);
		
		lblSocietyLand = new JLabel("Unclaimed Land:");
		lblSocietyLand.setBounds(10, 45, 125, 24);
		add(lblSocietyLand);
		
		lblSocietyLandValue = new JLabel(String.valueOf(society.getUnclaimedLand()));
		lblSocietyLandValue.setBounds(250, 45, 67, 24);
		add(lblSocietyLandValue);
		
		lblRiverUsage = new JLabel("River Remaining Produce:");
		lblRiverUsage.setBounds(10, 80, 175, 24);
		add(lblRiverUsage);
		
		PublicAssetsDescription publicAssetsDescription = new PublicAssetsDescription(society);
		lblRiverUsageValue = new JLabel(publicAssetsDescription.getRiverRemainingProduceDescription());
		lblRiverUsageValue.setBounds(250, 80, 147, 24);
		add(lblRiverUsageValue);
		
		lblIrrigationCanalOperational = new JLabel("Irrigation Canal:");
		lblIrrigationCanalOperational.setBounds(10, 115, 175, 24);
		add(lblIrrigationCanalOperational);
		
		lblIrrigationCanalOperationalValue = new JLabel(publicAssetsDescription.getIrrigationCanalOperational());
		lblIrrigationCanalOperationalValue.setBounds(250, 115, 177, 24);
		add(lblIrrigationCanalOperationalValue);
		
		JScrollPane scrollPane_1_1 = new JScrollPane();
		scrollPane_1_1.setBounds(10, 150, 483, 230);
		getContentPane().add(scrollPane_1_1);
		
		publicAssetsTableModel = new PublicAssetsTableModel(society);
		JTable tblPublicAssets = new DefaultJTable(publicAssetsTableModel);
		scrollPane_1_1.setViewportView(tblPublicAssets);
    }
	
	@Override
	public void updateInfo() {
		lblSocietyGoldValue.setText(String.valueOf(society.getCash()));
		lblSocietyLandValue.setText(String.valueOf(society.getUnclaimedLand()));
		PublicAssetsDescription publicAssetsDescription = new PublicAssetsDescription(society);
		lblRiverUsageValue.setText(publicAssetsDescription.getRiverRemainingProduceDescription());
		lblIrrigationCanalOperationalValue.setText(publicAssetsDescription.getIrrigationCanalOperational());
		
		publicAssetsTableModel.updatePublicAssets(society);
	}

	@Override
	public void updateSociety(Society society) {
		this.society = society;
		updateInfo();
	}
}