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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import action.PersonAction;
import action.PersonActionFactory;
import action.PersonActions;
import action.PersonActionsByKnowledge;
import knowledge.Knowledge;
import knowledge.KnowledgePercentageKnown;
import knowledge.KnowledgeTree;
import person.Person;
import person.PersonKnowledgeUtils;
import person.Sex;
import society.Society;

public class TechnologyTreeInternalFrame extends AbstractInternalNextTurnFrame implements NextTurnListener {
    private static final int xOffset = 0, yOffset = 300;
    private static final int labelWidth = 125;
    private static final int labelHeight = 30;

    private final KnowledgePercentageKnown knowledgePercentageKnown;
    private final List<KnowledgeLabel> knowledgeLabels = new ArrayList<>();
    private Society society;
    
    public TechnologyTreeInternalFrame(Society society) {
        super("Technology Tree");
        this.society = society;
        this.knowledgePercentageKnown = new KnowledgePercentageKnown(society.getReadOnlyList());
        
        setBounds(xOffset, yOffset, 700, 450);
        this.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(null) {
        	@Override
        	public Dimension getPreferredSize() {
        		return new Dimension(this.getWidth(), this.getHeight());
        	}
        	
        	@Override
        	public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2 = (Graphics2D) g;
                paintConnectingLines(g2);
            }
        };
        
        createScrollPane(contentPanel);
        
        KnowledgeTree knowledgeTree = new KnowledgeTree(Knowledge.VALUES);
        
        PersonActionsByKnowledge personActionsByKnowledge = new PersonActionsByKnowledge(PersonActionFactory.createAllPersonActions());
        
        List<Point> usedLocations = new ArrayList<>();
        for(int index=0; index<knowledgeTree.size(); index++) {
	        for(Knowledge knowledge : knowledgeTree.getKnowledge(index)) {
	        	KnowledgeLabel knowledgeLabel = new KnowledgeLabel(knowledgePercentageKnown, knowledge, society.canObserveKnowledge(knowledge));
	        	
	        	createToolTipText(knowledgeLabel, knowledge, personActionsByKnowledge);
	        	int x = 25 + index * 200;
	        	int y = 25;
	        	while (usedLocations.contains(new Point(x, y)) || prerequisiteKnowledgeLabelAtSameHeight(knowledge, y)) {
	        		y += 100;
	        	}
				knowledgeLabel.setBounds(x, y, labelWidth, labelHeight);
				usedLocations.add(new Point(x, y));
				contentPanel.add(knowledgeLabel);
				knowledgeLabels.add(knowledgeLabel);
	        }
        }
        contentPanel.setBounds(createContentPanelBounds(usedLocations));
    }
    
    private void createToolTipText(KnowledgeLabel knowledgeLabel, Knowledge knowledge, PersonActionsByKnowledge personActionsByKnowledge) {
    	StringBuffer toolTipText = new StringBuffer("<html>");
    	toolTipText.append(knowledge.getDescription()).append("<br>");
    	
    	PersonActions personActions = personActionsByKnowledge.get(knowledge);
    	if (personActions.size() > 0) {
    		toolTipText.append("<br>Actions:<br>");
    		for(PersonAction personAction : personActions) {
    			toolTipText.append(personAction.getName()).append("<br>");
    		}
    	}
    	
    	toolTipText.append("<html>");
    	
    	knowledgeLabel.setToolTipText(toolTipText.toString());
		
	}

	private void createScrollPane(JPanel contentPanel) {
		JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBounds(0, 0, this.getWidth() - 20, this.getHeight() - 50);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
	}
    
    private Rectangle createContentPanelBounds(List<Point> usedLocations) {
    	int xMax = 0;
        int yMax = 0;
        for(Point usedLocation : usedLocations) {
        	if (usedLocation.getX() > xMax) {
        		xMax = (int) usedLocation.getX();
        	}
        	if (usedLocation.getY() > yMax) {
        		yMax = (int) usedLocation.getY();
        	}
        }
        return new Rectangle(0, 0, xMax + labelWidth + 25, yMax + labelHeight + 25);
    }
    
    private boolean prerequisiteKnowledgeLabelAtSameHeight(Knowledge knowledge, int y) {
    	if (knowledge.getPrerequisiteKnowledge() != null) {
    		KnowledgeLabel prerequisiteKnowledgeLabel = findKnowledgeLabel(knowledge.getPrerequisiteKnowledge());
    		return prerequisiteKnowledgeLabel.getY() == y;
    	} else {
    		return false;
    	}
    }
    
	private KnowledgeLabel findKnowledgeLabel(Knowledge Knowledge) {
		for(KnowledgeLabel knowledgeLabel : knowledgeLabels) {
			if (knowledgeLabel.getKnowledge() == Knowledge) {
				return knowledgeLabel;
			}
		}
		throw new IllegalStateException("No KnowledgeLabel was found for knowledge " + Knowledge.getName());
	}

	@Override
	public void updateInfo() {
		knowledgePercentageKnown.update(society.getReadOnlyList());
		for(KnowledgeLabel knowledgeLabel : knowledgeLabels) {
			knowledgeLabel.updateInfo(knowledgePercentageKnown, society.canObserveKnowledge(knowledgeLabel.getKnowledge()));
		}
	}
	
	private void paintConnectingLines(Graphics2D g2) {
		for(KnowledgeLabel knowledgeLabel : knowledgeLabels) {
			Knowledge knowledge = knowledgeLabel.getKnowledge();
			if (knowledge.getPrerequisiteKnowledge() != null) {
				drawConnectingLine(g2, knowledgeLabel, knowledge.getPrerequisiteKnowledge());
			}
			if (knowledge.getPrerequisiteKnowledge2() != null) {
				drawConnectingLine(g2, knowledgeLabel, knowledge.getPrerequisiteKnowledge2());
			}
		}
	}
	
	private void drawConnectingLine(Graphics2D g2, KnowledgeLabel knowledgeLabel, Knowledge prerequisiteKnowledge) {
		KnowledgeLabel prerequisiteKnowledgeLabel = findKnowledgeLabel(prerequisiteKnowledge);
		int x1 = knowledgeLabel.getX();
		int y1 = knowledgeLabel.getY() + knowledgeLabel.getHeight() / 2;
		int x2 = prerequisiteKnowledgeLabel.getX() + prerequisiteKnowledgeLabel.getWidth();
		int y2 = prerequisiteKnowledgeLabel.getY() + prerequisiteKnowledgeLabel.getHeight() / 2;
		Line2D line = new Line2D.Float(x1, y1, x2, y2);
        g2.draw(line);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
		      JFrame frame = new JFrame();
		      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		      JDesktopPane desktopPane = new JDesktopPane();
		      Society society = new Society();
		      Person person = new Person(18, Sex.FEMALE);
		      PersonKnowledgeUtils.learnKnowledge(person, Knowledge.STONE_TOOLING);
		      PersonKnowledgeUtils.learnPartialKnowledge(person, Knowledge.CLOTHING);
		      society.addPerson(person);
		      TechnologyTreeInternalFrame technologyTreeInternalFrame = new TechnologyTreeInternalFrame(society);
		      technologyTreeInternalFrame.setLocation(0, 0);
		      technologyTreeInternalFrame.setVisible(true);
		      desktopPane.add(technologyTreeInternalFrame);

		      frame.add(desktopPane, BorderLayout.CENTER);
		      frame.setSize(700, 500);
		      frame.setVisible(true);
		    });
	}
	
	private final static Color DARK_GREEN = new Color(38, 115, 38);
	
	private static class KnowledgeLabel extends JLabel {
		private final Knowledge knowledge;
		public KnowledgeLabel(KnowledgePercentageKnown knowledgePercentageKnown, Knowledge knowledge, boolean isKnownledgePublic) {
			super(createText(knowledgePercentageKnown, knowledge, isKnownledgePublic));
			this.knowledge = knowledge;
			setHorizontalAlignment(SwingConstants.CENTER);
			
			initializeLayout(knowledgePercentageKnown, knowledge);
		}

		private void initializeLayout(KnowledgePercentageKnown knowledgePercentageKnown, Knowledge knowledge) {
			int percentage = knowledgePercentageKnown.getKnowledgePercentageKnown(knowledge);
			Color color = knowledgePercentageKnown.isKnowledgeKnown(knowledge) ? Color.BLUE : Color.BLACK;
			if (isInprogress(percentage)) {
				color = DARK_GREEN;
			}
			Border border = BorderFactory.createLineBorder(color);
			setBorder(border);
        	
        	setForeground(color);
		}
		
		private static String createText(KnowledgePercentageKnown knowledgePercentageKnown, Knowledge knowledge, boolean isKnownledgePublic) {
			int percentage = knowledgePercentageKnown.getKnowledgePercentageKnown(knowledge);
			if (isInprogress(percentage)) {
        		return knowledge.getName() + " (" + percentage + "%)";
        	} else if (percentage >= 100 && !isKnownledgePublic) {
        		return knowledge.getName() + "*";
        	} else {
        		return knowledge.getName();
        	}
		}

		public void updateInfo(KnowledgePercentageKnown knowledgePercentageKnown, boolean isKnownledgePublic) {
			initializeLayout(knowledgePercentageKnown, knowledge);
			setText(createText(knowledgePercentageKnown, knowledge, isKnownledgePublic));
		}

		private static boolean isInprogress(int percentage) {
			return percentage != 0 && percentage != 100;
		}
		
		public Knowledge getKnowledge() {
			return knowledge;
		}
	}

	@Override
	public void updateSociety(Society society) {
		this.society = society;
		updateInfo();
	}
}