package jmtools

import griffon.core.GriffonClass;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JSplitPane;
import javax.swing.border.EtchedBorder;

import org.codehaus.griffon.runtime.core.DefaultGriffonViewClass;

public GriffonClass getGriffonClass() {
	GriffonClass gc = new DefaultGriffonViewClass(app, this.getClass())
	return gc
}

progressBar = null

messageLogPopup = { e ->
	if (e.isPopupTrigger()) {
		popupMenu {
			menuItem(text: 'Clear Message Log', actionPerformed: controller.clearMessageLog)
		}.show(e.getComponent(), e.getX(), e.getY())
	}
}

application(title: 'jmtools',
  preferredSize: [1200, 900],
  pack: true,
  //location: [50,50],
  locationByPlatform: true,
  iconImage:   imageIcon('/griffon-icon-48x48.png').image,
  iconImages: [imageIcon('/griffon-icon-48x48.png').image,
               imageIcon('/griffon-icon-32x32.png').image,
               imageIcon('/griffon-icon-16x16.png').image])
{
    // add content here
	splitPane(orientation: JSplitPane.VERTICAL_SPLIT, resizeWeight:0.75) {
		tabbedPane {
			final collectionManagerMVC = app.buildMVCGroup('collectionManager')
			widget(collectionManagerMVC.controller.view.panel, title: 'Collection Manager')
			
//			def paTreeMVC = buildMVCGroup('progArchiveTree')
//			widget(paTreeMVC.view.panel, title: 'Prog Archive Tree')
		/*
			final topProgMissingMVC = buildMVCGroup('topProgMissing')
			widget(topProgMissingMVC.view.panel, title: 'Top Prog Missing')
		
			def prepFiles = buildMVCGroup('prepareFiles')
			widget(prepFiles.view.panel, title: 'Prepare Files')
		
			def tagFiles = buildMVCGroup('fileTagger')
			widget(tagFiles.view.panel, title: 'Tag Files')
		
			def toCollMover = buildMVCGroup('toCollectionMover')
			widget(toCollMover.view.panel, title: 'Moved to Collection')
			*/
		}
		panel(layout: new BorderLayout()) {
			scrollPane(constraints: BorderLayout.CENTER) {
				textArea(editable: false, rows: 8,
					text: bind('messageLog', source:model),
					mousePressed: messageLogPopup,
					mouseReleased: messageLogPopup)
			}
			panel(constraints: BorderLayout.SOUTH, layout: new BorderLayout(), border: new EtchedBorder()) {
				panel(layout: new FlowLayout(FlowLayout.LEFT), constraints: BorderLayout.WEST) {
					label(text: "Status:")
					label(text: bind(source: model, sourceProperty: "statusMessage"))
				}
				progressBar = progressBar(constraints: BorderLayout.CENTER)
			}
		}
	}
}
