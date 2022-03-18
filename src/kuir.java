public class kuir {

    public static void main(String[] args) throws Exception {
        makeCollection mc = new makeCollection();
        makeKeyword mk = new makeKeyword();
        mc.preprocessing();
        mk.analysis();
    }
}
