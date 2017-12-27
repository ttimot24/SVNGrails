package grailsapplication1

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;


class RepoController {

    def index() {

        params.dump();
        
        def currentDirectory="";
        
            if (!params.dir.equals(null)) {
                currentDirectory = params.dir;
            }
        
            def url = "https://github.com/ttimot24/HorizontCMS/trunk/"+currentDirectory;
            def name = "";
            def password = "";
  
            SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));

        
            /* ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
             repository.setAuthenticationManager(authManager); */


            SVNNodeKind nodeKind = repository.checkPath("", -1);
            if (nodeKind == SVNNodeKind.NONE) {
                System.err.println("There is no entry at '" + url + "'.");
            } else if (nodeKind == SVNNodeKind.FILE) {
                System.err.println("The entry at '" + url + "' is a file while a directory was expected.");
            }

            System.out.println("Repository Root: " + repository.getRepositoryRoot(true));



         render(view: "tree",model: [
                                    repository: repository.getRepositoryRoot(true),
                                    latestRevision: repository.getLatestRevision(),
                                    entries: repository.getDir("", -1, null, (Collection) null),
                                    currentDirectory: currentDirectory+"/",
                                    ]);
    }
    
    
        public static Collection listEntries(SVNRepository repository, String path) throws SVNException {

        
        Collection entries = repository.getDir(path, -1, null,
                (Collection) null);
        Iterator iterator = entries.iterator();
        while (iterator.hasNext()) {
            SVNDirEntry entry = (SVNDirEntry) iterator.next();
            System.out.println("/" + (path.equals("") ? "" : path + "/")
                    + entry.getName() + " (author: '" + entry.getAuthor()
                    + "'; revision: " + entry.getRevision() + "; date: " + entry.getDate() + ")");
                
            if (entry.getKind() == SVNNodeKind.DIR) {
                listEntries(repository, (path.equals("")) ? entry.getName()
                        : path + "/" + entry.getName());
            }
        }
    
        return entries;
    }
    
}
