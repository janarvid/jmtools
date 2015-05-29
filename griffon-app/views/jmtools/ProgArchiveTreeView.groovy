package jmtools
import javax.swing.tree.DefaultMutableTreeNode as TreeNode

import org.veggeberg.jmtools.progarchives.TopProgAlbumsReader;

mboxTree = null
mboxes = [
          [name: "root@example.com", folders: [[name: "Inbox"], [name: "Trash"]]],
          [name: "test@foo.com", folders: [[name: "Inbox"], [name: "Trash"]]]
]

panel = panel {
	scrollPane {
		mboxTree = tree()
	}
	final urls = []
	new File("../data/progarchives").eachFile { file ->
		if (file.name.startsWith("top-prog-albums-")) {
			urls << file.toString()
		}
	}
//	def urls = ['../test/resources/progarchives/top-prog-albums-2013.html']
//	urls << '../test/resources/progarchives/top-prog-albums-2012.html'
	TopProgAlbumsReader reader = new TopProgAlbumsReader()
	def albums = reader.getAlbums(urls)
	def level1Map = new TreeMap() //albums*.genre.unique().sort()
	def levels = ['genre', 'artist']
	for (album in albums) {
		//println album
		final level1 = album."${levels[0]}"
		final artists = level1Map.get(level1, new TreeMap()) 
		final level2Map = artists.get(album.artist, new TreeSet())
		level2Map << "${album.year} - ${album.title}"
	}
	mboxTree.model.root.removeAllChildren()
	level1Map.each {genre, artistList ->
		def node = new TreeNode(genre)
		artistList.each { artist, albumList ->
			final artistNode = new TreeNode(artist)  
			node.add(artistNode)
			albumList.each { album ->
				final albumNode = new TreeNode(album)
				artistNode.add(albumNode)
			}
		}
		mboxTree.model.root.add(node)
	}
	mboxTree.model.reload(mboxTree.model.root)
}

/*
def swing = new SwingBuilder()
JTree mboxTree
swing.frame(title: 'Mailer', defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE,
    size: [800, 600], show: true, locationRelativeTo: null) {
    lookAndFeel("system")
    menuBar() {
        menu(text: "File", mnemonic: 'F') {
            menuItem(text: "Exit", mnemonic: 'X', actionPerformed: {dispose() })
        }
    }
    splitPane {
        scrollPane(constraints: "left", preferredSize: [160, -1]) {
            mboxTree = tree(rootVisible: false)
        }
        splitPane(orientation:JSplitPane.VERTICAL_SPLIT, dividerLocation:280) {
            scrollPane(constraints: "top") { mailTable = table() }
            scrollPane(constraints: "bottom") { textArea() }
        }
    }
    ["From", "Date", "Subject"].each { mailTable.model.addColumn(it) }
}

mboxTree.model.root.removeAllChildren()
mboxes.each {mbox ->
    def node = new TreeNode(mbox.name)
    mbox.folders.each { folder -> node.add(new TreeNode(folder.name)) }
    mboxTree.model.root.add(node)
}
mboxTree.model.reload(mboxTree.model.root)
*/