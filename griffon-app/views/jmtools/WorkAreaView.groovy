package jmtools

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import org.veggeberg.jmtools.Global;

collectionDirTable = null
workDirAlbumTable = null
tagTable = null

// Widget from other MVC maps
JTable getAlbumTable() {
	model.findExistingGroup(Global.MVC_COLLECTION_MANAGER).view.albumTable
}

workDirAlbumTablePopup = { e ->
	if (e.isPopupTrigger()) {
		popupMenu {
			menuItem(text: 'Rename', actionPerformed: controller.&renameWorkDirEnties)
			menuItem(text: 'Delete', actionPerformed: controller.&deleteWorkDirEnties)
		}.show(e.getComponent(), e.getX(), e.getY())
	}
}

collectionDirTablePopup = { e ->
	if (e.isPopupTrigger()) {
		popupMenu {
			menuItem(text: 'Rename', actionPerformed: controller.renameCollectionDirEnties)
		}.show(e.getComponent(), e.getX(), e.getY())
	}
}

tagTablePopup = { e ->
	if (e.isPopupTrigger()) {
		popupMenu {
			menuItem(text: 'Correct tags', actionPerformed: controller.&correctTags)
		}.show(e.getComponent(), e.getX(), e.getY())
	}
}

/*
tagTablePopup = { e ->
	if (e.isPopupTrigger()) {
		popupMenu {
			menuItem(text: 'Rename', actionPerformed: controller.renameCollectionDirEnties)
		}.show(e.getComponent(), e.getX(), e.getY())
	}
}
*/

panel = panel(layout: new BorderLayout()) {
	// Work and collection areas
	splitPane(orientation: JSplitPane.VERTICAL_SPLIT, resizeWeight:0.5) {
		splitPane(orientation: JSplitPane.HORIZONTAL_SPLIT, resizeWeight:0.5) {
			panel(layout: new BorderLayout(), border: new TitledBorder("Work Dir")) {
				scrollPane(constraints: BorderLayout.CENTER)
				{
					workDirAlbumTable = table(id: 'workDirAlbumTable',
					mousePressed: workDirAlbumTablePopup,
					mouseReleased: workDirAlbumTablePopup)
					{
						tableFormat = defaultTableFormat(columnNames: ['Name', 'Size', 'AlbumDir'])
						eventTableModel(source: model.findFilesModel.workDirAlbums, format: tableFormat)
						current.selectionModel.addListSelectionListener(controller.albumSelect)
						current.setPreferredScrollableViewportSize(new Dimension(300,200));
					}
				}
				/*
				 panel(constraints: BorderLayout.SOUTH) {
				 label("Album dir: ")
				 textField(text: bind('albumDir', source:model, mutual:true), columns: 20,
				 actionPerformed: controller.setAlbumDirInRow)
				 button(text: "Refresh Work", actionPerformed: controller.refreshWorkDirs)
				 }
				 */
			}
			panel(layout: new BorderLayout(), border: new TitledBorder("Collection Dir")) {
				scrollPane(constraints: BorderLayout.CENTER) {
					collectionDirTable = table(id: 'collectionDirTable',
					mousePressed: collectionDirTablePopup,
					mouseReleased: collectionDirTablePopup)
					{
						tableFormat = defaultTableFormat(columnNames: ['Name', 'Size'])
						eventTableModel(source: model.collectionDirs, format: tableFormat)
						current.setPreferredScrollableViewportSize(new Dimension(300,200))
					}
				}
				/*
				 panel(constraints: BorderLayout.SOUTH) {
				 button(' Move to collection', actionPerformed: controller.moveToCollectionDir)
				 button(text: "Refresh Collection", actionPerformed: controller.refreshCollectionDirs)
				 }
				 */
			}
		}
		panel(layout: new BorderLayout(), border: new TitledBorder("Tags")) {
			scrollPane(constraints: BorderLayout.CENTER) {
				tagTable = table(id: 'tagTable',
				mousePressed: tagTablePopup,
				mouseReleased: tagTablePopup
			)
				{
					tableFormat = defaultTableFormat(
						columnNames: ['Artist', 'Genre', 'Year', 'Album', 'Track', 'Title', 'File'])
					eventTableModel(source: model.tags, format: tableFormat)
					current.setPreferredScrollableViewportSize(new Dimension(600,200))
				}
			}
		}
	}
	panel(layout: new BorderLayout(), constraints: BorderLayout.SOUTH) {
		panel(layout: new FlowLayout(FlowLayout.LEFT), constraints: BorderLayout.WEST) {
			label("Album work dir: ")
			textField(text: bind('albumDir', source:model, mutual:true), columns: 20,
					actionPerformed: controller.setAlbumDirInRow)
					button(text: "Refresh Work", actionPerformed: controller.refreshWorkDirs)
					button('Unpack files', actionPerformed: controller.&unpackArchives)
					button('Launch tagger...', actionPerformed: controller.&launchTagger)
		}
		panel(layout: new FlowLayout(FlowLayout.RIGHT), constraints: BorderLayout.EAST) {
			button(' Move to collection', actionPerformed: controller.moveToCollectionDir)
			button(text: "Refresh Collection", actionPerformed: controller.refreshCollectionDirs)
		}
	}
}
