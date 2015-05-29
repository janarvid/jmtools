package jmtools

import griffon.core.GriffonClass;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import org.codehaus.griffon.runtime.core.DefaultGriffonViewClass;

workDirAlbumTable=null
findResultsTable=null

/*
findResultsTablePopup = { e ->
	if (e.isPopupTrigger()) {
		popupMenu {
			menuItem(text: 'Delete', actionPerformed: controller.deleteFindResults)
		}.show(e.getComponent(), e.getX(), e.getY())
	}
}
*/

public GriffonClass getGriffonClass() {
	GriffonClass gc = new DefaultGriffonViewClass(app, this.getClass())
	return gc
}

panel = panel(layout: new BorderLayout()) {
	splitPane(orientation: JSplitPane.HORIZONTAL_SPLIT, constraints: BorderLayout.NORTH, resizeWeight:0.5) {
		scrollPane(border: new TitledBorder("Find Results")) {
			findResultsTable = table(id: 'findResultsTable',
//			mousePressed: findResultsTablePopup, mouseReleased: findResultsTablePopup
		) {
				tableFormat = defaultTableFormat(columnNames: ['Name', 'Size', 'Date'])
				eventTableModel(source: model.findResults, format: tableFormat)
				//installTableComparatorChooser(source: model.findResults)
			}
			findResultsTable.setPreferredScrollableViewportSize(findResultsTable.getPreferredSize());
			findResultsTable.setPreferredScrollableViewportSize(new Dimension(300,200));
		}
		scrollPane(border: new TitledBorder("Work Dir")) {
			workDirAlbumTable = table(id: 'workDirAlbumTable', 
			//				mousePressed: workDirAlbumTablePopup, mouseReleased: workDirAlbumTablePopup
			)
			{
				tableFormat = defaultTableFormat(columnNames: ['Name', 'Size', 'AlbumDir'])
				eventTableModel(source: model.workDirAlbums, format: tableFormat)
			}
			workDirAlbumTable.setPreferredScrollableViewportSize(workDirAlbumTable.getPreferredSize());
			workDirAlbumTable.setPreferredScrollableViewportSize(new Dimension(300,200));
		}
	}
	panel(constraints: BorderLayout.CENTER) {
		label("   Search for: ")
		textField(text: bind(sourceProperty: 'searchPattern', source: model, mutual:true),
		actionPerformed: controller.findOnDisk,
		columns:20)
		button(text: "Find on disk", actionPerformed: controller.findOnDisk)
		button(' Move to work area ', actionPerformed: controller.&moveToWorkDir)
		label(text: bind(sourceProperty: 'currentArtistDir', source: model,
		converter: { model.workDirPath }))
		panel {
			button(text: "Refresh Work", actionPerformed: controller.refreshWorkDirs)
		}
	}
}
