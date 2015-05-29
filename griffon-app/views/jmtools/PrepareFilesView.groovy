package jmtools

import java.awt.BorderLayout;

import javax.swing.border.TitledBorder;

collectionDirTable = null

/*
workDirAlbumTablePopup = { e ->
	if (e.isPopupTrigger()) {
		popupMenu {
			menuItem(text: 'Rename', actionPerformed: controller.renameWorkDirEnties)
			menuItem(text: 'Delete', actionPerformed: controller.deleteWorkDirEnties)
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
*/

panel = panel(layout: new BorderLayout()) {
	// Source archives
	/*
	splitPane(orientation: JSplitPane.HORIZONTAL_SPLIT, constraints: BorderLayout.CENTER, resizeWeight:0.33) {
		panel(layout: new BorderLayout()) {
			label(text: bind('currentArtist', source:model.topProgMissingModel,
			converter: { "<html>Albums of artist: <b>${it.name}</b></html>"}),
			constraints: BorderLayout.NORTH)
			scrollPane(constraints: BorderLayout.CENTER) {
				model.albumTable = table(id: 'albumTable') {
					tableFormat = defaultTableFormat(columnNames: ['Name', 'Year'])
					eventTableModel(source: model.topProgMissingModel.albums, format: tableFormat)
					current.selectionModel.addListSelectionListener(controller.albumSelect)
				}
			}
			panel(constraints: BorderLayout.SOUTH) {
				label(text: bind('currentAlbum', source:model,
				converter: { "<html>Current album: <b>${it?.name}</b></html>" }))
			}
		}
		
		// Work and collection areas
		splitPane(orientation: JSplitPane.HORIZONTAL_SPLIT, resizeWeight:0.5) {
			panel(layout: new BorderLayout()) {
				label(text: bind('currentArtistDir', source:model.topProgMissingModel,
					converter: { "<html>Work dir path: <b>${model.workDirPath}</b><html>"}),
					constraints: BorderLayout.NORTH)
				scrollPane(constraints: BorderLayout.CENTER)
				{
					model.workDirAlbumTable = table(id: 'workDirAlbumTable',
//					mousePressed: workDirAlbumTablePopup, mouseReleased: workDirAlbumTablePopup
				)
					{
						tableFormat = defaultTableFormat(columnNames: ['Name', 'Size', 'AlbumDir'])
						eventTableModel(source: model.workDirAlbums, format: tableFormat)
					}
				}
				panel(constraints: BorderLayout.SOUTH) {
					label("Album dir: ")
					textField(text: bind('albumDir', source:model, mutual:true), columns: 20,
					actionPerformed: controller.setAlbumDirInRow)
					button(text: "Refresh", actionPerformed: controller.refreshWorkDirs)
				}
			}
			panel(layout: new BorderLayout()) {
				label(text: bind('currentArtistDir', source:model.topProgMissingModel,
				converter: { "<html>Collection dir path: <b>${model.collectionDirPath}</b><html>"}),
				constraints: BorderLayout.NORTH)
				scrollPane(constraints: BorderLayout.CENTER) {
					collectionDirTable = table(id: 'collectionDirTable',
//					mousePressed: collectionDirTablePopup, mouseReleased: collectionDirTablePopup
				)
					{
						tableFormat = defaultTableFormat(columnNames: ['Name', 'Size'])
						eventTableModel(source: model.collectionDirs, format: tableFormat)
					}
				}
				panel(constraints: BorderLayout.SOUTH) {
					button(' Move to collection', actionPerformed: controller.moveToCollectionDir)
					button(text: "Refresh", actionPerformed: controller.refreshCollectionDirs)
				}
			}
		}
	}
	// Move from source archives to work area
	//		button('Move to work area...', actionPerformed: controller.moveToWorkArea, constraints: 'wrap')

	// Unpack any archives in work area
	 */
//	panel(constraints: BorderLayout.SOUTH) {
//		button('Unpack files'/*, actionPerformed: controller.unpackArchives*/)
//		button('Launch tagger...'/*, actionPerformed: controller.launchTagger*/)
//	}
	/*
	 panel(constraints: BorderLayout.CENTER) {
	 // Logging area
	 scrollPane {
	 textArea(rows: 4, columns:80, editable: false,
	 text: bind('logAreaText', source:model))
	 }
	 }
	 */
}
