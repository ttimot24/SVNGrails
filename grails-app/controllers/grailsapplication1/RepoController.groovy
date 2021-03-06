package grailsapplication1

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;


class RepoController {
    
    def baseUrl="https://github.com/ttimot24/HorizontCMS/";
    def name = "";
    def password = "";
   
    def index() {

            def currentDirectory="";
        
            if (!params.dir.equals(null)) {
                currentDirectory = params.dir;
            }
            
            def url = baseUrl+"trunk/"+currentDirectory;
  
            SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));

            /* ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
             repository.setAuthenticationManager(authManager); */


            SVNNodeKind nodeKind = repository.checkPath("", -1);
            if (nodeKind == SVNNodeKind.NONE) {
                System.err.println("There is no entry at '" + url + "'.");
            } else if (nodeKind == SVNNodeKind.FILE) {
                System.err.println("The entry at '" + url + "' is a file while a directory was expected.");
            }

            println("Repository Root: " + repository.getRepositoryRoot(true));

            def entries = repository.getDir("", -1, null, (Collection) null);
            
            Collections.sort(entries);

         render(view: "tree",model: [
                                    repository: repository,
                                    latestRevision: repository.getLatestRevision(),
                                    entries: entries,
                                    currentDirectory: currentDirectory+"/",
                                    ]);
    }
    
    
    
    def view(){
        
        
            def currentDirectory="";
        
            if (!params.file.equals(null)) {
                currentDirectory = params.file;
            }
            
            def url = baseUrl+"trunk/"+currentDirectory;
  
            SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
            
            SVNProperties properties = new SVNProperties();
            ByteArrayOutputStream content = new ByteArrayOutputStream();
            repository.getFile("", -1, properties, content);
                   
            render(view: "file",model: [
                    content: content.toString(),
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
