package scripts;
public class kuir {

    public static void main(String[] args) throws Exception {
        String command = args[0];   
        String path = args[1];
        
		if(command.equals("-c")) {
			makeCollection collection = new makeCollection(path);
			collection.makeXml();
		}
        else if (command.equals("-k")) {
            makeKeyword keyword = new makeKeyword(path);
            keyword.convertXml();
        }
        else if (command.equals("-i")) {
            indexer ix = new indexer(path);
            ix.processing();
        } else if (command.equals("-s")) {
            searcher sc = new searcher(path);
            String query = args[3];
            sc.CalcSim(query);
        } else if (command.equals("-m")) {
            String query = args[3];
            Midterm midterm = new Midterm(path);
            midterm.showSnippet(query);
            System.out.println("Do Something");
        }
    }
}
