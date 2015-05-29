package jmtools

import java.awt.BorderLayout;

import net.miginfocom.swing.MigLayout;

panel = panel(layout: new BorderLayout()) {
	/*
	panel(layout: new MigLayout(), constraints: BorderLayout.NORTH) {
		// The work area
		label('Work area: ')
		textField(text: bind('workAreaDir', source: model.prepareFilesMVC.model), editable:false,
			constraints: 'wrap')
	
		// Launch the tagger
		button('Launch tagger...', actionPerformed: controller.launchTagger)
	}
	
	panel(constraints: BorderLayout.CENTER) {
		// Logging area
		scrollPane {
			textArea(rows: 24, columns:80, editable: false,
				text: bind('logAreaText', source:model))
		}
	}
	*/
}
