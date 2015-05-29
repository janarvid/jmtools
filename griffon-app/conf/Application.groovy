application {
    title = 'Jmtools'
    startupGroups = ['jmtools']

    // Should Griffon exit when no Griffon created frames are showing?
    autoShutdown = true

    // If you want some non-standard application class, apply it here
    //frameClass = 'javax.swing.JFrame'
}
mvcGroups {
    // MVC Group for "findFiles"
    'findFiles' {
        model      = 'jmtools.FindFilesModel'
        controller = 'jmtools.FindFilesController'
        view       = 'jmtools.FindFilesView'
    }

    // MVC Group for "colllectionManager"
    'collectionManager' {
        model      = 'jmtools.CollectionManagerModel'
        controller = 'jmtools.CollectionManagerController'
        view       = 'jmtools.CollectionManagerView'
    }

    // MVC Group for "topProgMissing"
    'topProgMissing' {
        model      = 'jmtools.TopProgMissingModel'
        view       = 'jmtools.TopProgMissingView'
        controller = 'jmtools.TopProgMissingController'
    }

    // MVC Group for "workArea"
    'workArea' {
        model      = 'jmtools.WorkAreaModel'
        view       = 'jmtools.WorkAreaView'
        controller = 'jmtools.WorkAreaController'
    }

    // MVC Group for "fileTagger"
    'fileTagger' {
        model      = 'jmtools.FileTaggerModel'
        view       = 'jmtools.FileTaggerView'
        controller = 'jmtools.FileTaggerController'
    }

    // MVC Group for "prepareFiles"
    'prepareFiles' {
        model      = 'jmtools.PrepareFilesModel'
        view       = 'jmtools.PrepareFilesView'
        controller = 'jmtools.PrepareFilesController'
    }

    // MVC Group for "progArchiveTree"
    'progArchiveTree' {
        model      = 'jmtools.ProgArchiveTreeModel'
        view       = 'jmtools.ProgArchiveTreeView'
        controller = 'jmtools.ProgArchiveTreeController'
    }

    // MVC Group for "progRockTree"
    'progRockTree' {
        model      = 'jmtools.ProgRockTreeModel'
        view       = 'jmtools.ProgRockTreeView'
        controller = 'jmtools.ProgRockTreeController'
    }

    // MVC Group for "archiveUnpacker"
    'archiveUnpacker' {
        model      = 'jmtools.ArchiveUnpackerModel'
        view       = 'jmtools.ArchiveUnpackerView'
        controller = 'jmtools.ArchiveUnpackerController'
    }

    // MVC Group for "jmtools"
    'jmtools' {
        model      = 'jmtools.JmtoolsModel'
        view       = 'jmtools.JmtoolsView'
        controller = 'jmtools.JmtoolsController'
    }
}
