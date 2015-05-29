package jmtools

import java.awt.BorderLayout

import javax.swing.JSplitPane
import javax.swing.ListSelectionModel
import javax.swing.border.TitledBorder

import ca.odell.glazedlists.SortedList

findResultsTablePopup = { e ->
	if (e.isPopupTrigger()) {
		popupMenu {
			menuItem(text: 'Delete', actionPerformed: controller.deleteFindResults)
		}.show(e.getComponent(), e.getX(), e.getY())
	}
}

panel = panel(layout: new BorderLayout()) {
	splitPane(orientation: JSplitPane.VERTICAL_SPLIT, constraints: BorderLayout.CENTER, resizeWeight:0.7) {
		splitPane(orientation: JSplitPane.HORIZONTAL_SPLIT) {
			panel(layout: new BorderLayout()) {
				scrollPane(border: new TitledBorder("Artists"), constraints: BorderLayout.CENTER) {
					model.artistTable = table(id: 'artistTable', selectionMode: ListSelectionModel.SINGLE_SELECTION) {
						current.selectionModel.addListSelectionListener(controller.artistSelect)
						current.addMouseListener(controller.doubleClickArtist)
						tableFormat = defaultTableFormat(columnNames: ['Name', 'Genre', 'Rank'])
						// tableFormat = defaultAdvancedTableFormat(columns: [[name:'Name'], [name: 'LastName']])
						eventTableModel(source: model.artists, format: tableFormat)
						if (model.artists instanceof SortedList) {
							installTableComparatorChooser(source: model.artists)
						}
					}
				}
				panel(constraints: BorderLayout.SOUTH) {
					button(text: "Refresh", actionPerformed: controller.refreshArtistTable)
				}
			}
			scrollPane(border: new TitledBorder("Albums")) {
				table(id: 'albumTable') {
					tableFormat = defaultTableFormat(columnNames: ['Name', 'Year', 'Rank'])
					eventTableModel(source: model.albums, format: tableFormat)
					installTableComparatorChooser(source: model.albums)
				}
			}
		}
		panel(layout: new BorderLayout(), constraints: BorderLayout.SOUTH) {
			panel(constraints: BorderLayout.NORTH) {
				label(text: bind(sourceProperty: 'currentArtist', source:model,
					converter: { "<html>Artist: <b>${it.name}</b></html>"}))
				label("   Search for: ")
				textField(text: bind(sourceProperty: 'searchPattern', source: model, mutual:true),
					actionPerformed: controller.findOnDisk,
					columns:20)
				button(text: "Find on disk", actionPerformed: controller.findOnDisk)
			}
			scrollPane(constraints: BorderLayout.CENTER) {
				model.findResultsTable = table(id: 'findResultsTable',
					mousePressed: findResultsTablePopup,
					mouseReleased: findResultsTablePopup)
				{
					tableFormat = defaultTableFormat(columnNames: ['Name', 'Size', 'Date'])
					eventTableModel(source: model.findResults, format: tableFormat)
					//installTableComparatorChooser(source: model.findResults)
				}
			}
			panel(constraints: BorderLayout.SOUTH) {
				label('Artist Directory: ')
				textField(text: bind(sourceProperty: 'currentArtistDir', source: model, mutual:true), columns:20)
//					actionPerformed: {
//						println "model.currentArtistDir = ${model.currentArtistDir}" 
//						model.setCurrentArtistDir(model.currentArtistDir) })
				button(' Move to work area '/*, actionPerformed: controller.moveToWorkDir*/)
				label(text: bind(sourceProperty: 'currentArtistDir', source: model, 
					converter: { model.workDirPath }))
			}
		}
	}
}