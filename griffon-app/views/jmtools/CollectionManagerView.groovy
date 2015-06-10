package jmtools

import griffon.core.MVCGroup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import ca.odell.glazedlists.SortedList;

artistTable = null
albumTable = null

panel = panel(layout: new BorderLayout()) {
	splitPane(orientation: JSplitPane.VERTICAL_SPLIT, resizeWeight:0.8, constraints: BorderLayout.CENTER) {
		panel(layout: new BorderLayout()) {
			splitPane(orientation: JSplitPane.HORIZONTAL_SPLIT, resizeWeight:0.5, constraints: BorderLayout.CENTER) {
				panel(layout: new BorderLayout()) {
					scrollPane(border: new TitledBorder("Artists"), constraints: BorderLayout.CENTER) {
						artistTable = table(id: 'artistTable', selectionMode: ListSelectionModel.SINGLE_SELECTION) {
							current.selectionModel.addListSelectionListener(controller.artistSelect)
							current.addMouseListener(controller.doubleClickArtist)
							tableFormat = defaultTableFormat(columnNames: ['Name', 'Genre', 'Rank'])
							// tableFormat = defaultAdvancedTableFormat(columns: [[name:'Name'], [name: 'LastName']])
							eventTableModel(source: model.artists, format: tableFormat)
							if (model.artists instanceof SortedList) {
								installTableComparatorChooser(source: model.artists)
							}
							current.setPreferredScrollableViewportSize(new Dimension(300,200))
						}
					}
					/*
					panel(constraints: BorderLayout.SOUTH) {
						button(text: "Refresh Artists", actionPerformed: controller.refreshArtistTable)
					}
					*/
				}
				scrollPane(border: new TitledBorder("Albums")) {
					albumTable = table(id: 'albumTable') {
						current.selectionModel.addListSelectionListener(controller.albumSelect)
						current.addMouseListener(controller.doubleClickAlbum)
						tableFormat = defaultTableFormat(columnNames: ['Name', 'Year', 'Rank'])
						eventTableModel(source: model.albums, format: tableFormat)
						if (model.albums instanceof SortedList) {
							installTableComparatorChooser(source: model.albums)
						}
						current.setPreferredScrollableViewportSize(new Dimension(300,200))
					}
				}
			}
			panel(constraints: BorderLayout.SOUTH, layout: new FlowLayout(FlowLayout.LEFT)) {
				button(text: "Refresh Artists", actionPerformed: controller.refreshArtistTable)
				label("Genres:")
				comboBox(id:'genreCB', model: eventComboBoxModel(source: model.genres),
					actionPerformed: controller.selectGenre,
					selectedItem: bind(target:model, targetProperty:'selectedGenre')
				)
				label("Current artist:")
				textField(text: bind(sourceProperty: 'currentArtist', source:model,
					converter: { it.name}), editable:false, columns: 20)
				label("  Genre:")
				textField(text: bind(sourceProperty: 'currentArtist', source:model,
					converter: { it.genre}), editable:false, columns: 20)
				label("  Dir:")
				textField(text: bind(sourceProperty: 'currentArtistDir', source:model, mutual:true), columns: 20)
			}
		}
		tabbedPane {
//			final topProgMissingMVC = buildMVCGroup('topProgMissing')
//			widget(topProgMissingMVC.view.panel, title: 'Top Prog Missing')

			final findFilesMVC = app.buildMVCGroup('findFiles')
			widget(findFilesMVC.controller.view.panel, title: 'Find Files')
			
			final workAreaMVC = app.buildMVCGroup('workArea')
			widget(workAreaMVC.controller.view.panel, title: 'Work Area')
			
			/*
			def prepFiles = buildMVCGroup('prepareFiles')
			widget(prepFiles.view.panel, title: 'Prepare Files')

			def tagFiles = buildMVCGroup('fileTagger')
			widget(tagFiles.view.panel, title: 'Tag Files')

			def toCollMover = buildMVCGroup('toCollectionMover')
			widget(toCollMover.view.panel, title: 'Moved to Collection')
			*/
		}
	}
}