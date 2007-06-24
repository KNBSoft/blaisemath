package scio.planar;

import java.awt.Adjustable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import scio.algebra.GroupElement;
import scio.algebra.GroupElementId;
import scio.algebra.PermutationElement;
import scio.graph.Edge;
import scio.graph.Graph;
import scio.graph.GraphGroupElement;

/**
 * <b>TemperleyLiebElement.java</b><br>
 * Author: <i>Elisha Peterson</i><br>
 * Created on <i>May 8, 2007, 11:51 AM</i><br><br>
 *
 * The TL algebra may be implemented as a graph... this class limits vertex valency
 * to 1-2 only! and all leafs are either inputs or outputs!<br><br>
 *
 * The main difference between this class and the planar graph classes is that the inputs
 * and outputs are STRONGLY controlled. The class encodes the arrays of inputs and outputs
 * with ALL actions.<br><br>
 *
 * In the standard case (default), the TL Element exists in TL_n, and the inputs are
 * [1,2,...,n]; outputs are [2n,2n-1,...,n+1]. The reverse order ensures that the vertices
 * proceed in order when read in clockwise fashion.<br><br>
 *
 * Additionally keeps track of the number of "kinks" in the element, and the "positivity" in the element.
 * This is specific to special kinds of trace diagrams, and will not extend well to more general cases.<br><br>
 *
 * Here are some default formatting options:
 *   1) Nested parentheses counting: e.g. ( (()()()) () ) () () becomes 53000100... numbers represent how many parentheses are inside
 *   2) Integer pairings... 
 */
public class TemperleyLiebElement extends PlanarGraphElement implements Iterator<TemperleyLiebElement> {
    
// Fields    
    
    ArrayList<Integer> inputs;
    ArrayList<Integer> outputs;
    int n=0;
    int kinks=0;
    boolean positive=true;
    
// Constructors    
    
    /** Default: set to identity, or each input is matched with each output */
    TemperleyLiebElement(){
        super();
        g.multiEdge=true;
        g.directed=false;
        initPuts(2);
        initId(2);
    }
    /** Default: sets up for a given n. */
    TemperleyLiebElement(int n){this();initPuts(n);initId(n);}
    /** Construct based on a list of pairings. */
    TemperleyLiebElement(int[][] pair){
        super();g.multiEdge=true;g.directed=false;
        inputs=new ArrayList<Integer>();
        outputs=new ArrayList<Integer>();
        /** pair should be an ?x2 array! */
        this.n=pair.length;
        for(int i=0;i<n;i++){addEdge(pair[i][0],pair[i][1]);}
    }
    /** Construct based on nesting information */
    TemperleyLiebElement(int[] paren){
        this(paren.length);
        g.clear();
        setToParen(paren);
    }
    /** Construct based on another TL Element */
    TemperleyLiebElement(TemperleyLiebElement e){
        super();
        inputs=(ArrayList<Integer>)e.inputs.clone();
        outputs=(ArrayList<Integer>)e.outputs.clone();
        n=e.n;
        kinks=e.kinks;
        positive=e.positive;
        g.addAll(e.getGraph());
    }
    
// Initializer methods
    
    /** Resets the underlying graph */
    public void clear(){g.clear();}
    
    /** Initializes inputs to 1->n and outputs to 2n->(n+1) */
    public void initPuts(int n){
        this.n=n;
        inputs=new ArrayList<Integer>();
        outputs=new ArrayList<Integer>();
        for(int i=1;i<=n;i++){inputs.add(i);outputs.add(2*n+1-i);}
    }
    
    /** Initializes to identity. */
    public void initId(int n){
        g.clear();
        for(int i=1;i<=n;i++){addEdge(i,2*n+1-i);}
    }
    
// Set methods    
    
    /** Adds to parenthetical elements from given start position. */
    public void setToParen(int i0,int[] paren,int ps,int pe){
        if(paren==null||pe<ps||ps>=paren.length||pe>=paren.length||i0>=2*paren.length){return;}
        /** Add first edge of paren */
        int inParen=paren[ps];
        //System.out.println("adding edge: "+i0+" to "+(i0+2*inParen+1));
        g.addEdge(i0,i0+2*inParen+1);
        /** Add edges inside paren 0 */
        setToParen(i0+1,paren,ps+1,ps+inParen);
        /** Add edges outside paren 0 */
        setToParen(i0+2*inParen+2,paren,ps+inParen+1,pe); 
    }
    /** Same method without bounds. */
    public void setToParen(int[] paren){setToParen(1,paren,0,paren.length-1);}
    
    /** Initializes to element represented by the string. Returns false if not valid. */
    public boolean setTo(String s){
        //TODO fill in the gaps here!!
        return true;
    }
    
// Get methods    
    
    /** Returns parenthetical notation, if possible... does not check validity! */
    public int[] getParen(){
        int[] result=new int[n];
        int i=0;
        for(Edge e:g){if(!e.isTrivial()){result[i]=(e.getSink()-e.getSource()-1)/2;i++;}}
        return result;
    }
    
    /** Returns paren string */
    public String getParenString(){
        int[] p=getParen();
        String s="";
        for(int i=0;i<p.length;i++){s+=p[i];}
        return s;
    }
    
    /** Returns whether this is indeed a basis element... */
    public boolean isBasisElement(){
        return g.getNumTrivialLoops()==0&&validParen(getParen())&&kinks==0;
    }
    
    /** Prints out the graph */
    public String toString(){return ((kinks%2==1)?"-":"")+g.toString()+" in TL("+n+")";}
        
    /** Validity checking redefined! Determines if it's "ok" to add the given edge.
     * No vertex can be used more than once!
     */
    public boolean validEdge(int a,int b, int w){
        if(a==b&&a==-1){return true;}
        if(a<1||b<1){return false;}
        if(a==b||w!=1){return false;}
        TreeSet<Integer> vxUsed=g.getVertices();
        return !(vxUsed.contains(a)||vxUsed.contains(b));
    }
    
    /** Reduces the vertex label set to remove gaps */
    private void relabelVertices(){g.relabelVertices();initPuts(g.getNumVertices()/2);}
    
    /** Determines if two edges incident to the given vertices are "crossed". Easy test given the cyclic ordering of vertices.
     * Uses a "cross" product to determine this!
     */
    public boolean crossed(int va,int vb){
        int va2=g.getAdjacency(va).first();
        int vb2=g.getAdjacency(vb).first();
        return (va-vb)*(va2-vb)*(va-vb2)*(va2-vb2)<0;
    }
    /** As above, but with edges */
    public boolean crossed(Edge a,Edge b){
        int va=a.getSource();
        int va2=a.getSink();
        int vb=b.getSource();
        int vb2=b.getSink();
        return (va-vb)*(va2-vb)*(va-vb2)*(va2-vb2)<0;
    }
    /** Returns array of two edges which are crossed, or null if there are none such! */
    public Edge[] getCrossedEdges(){
        for(Edge e1:g){for(Edge e2:g){
            if(e1==e2){continue;}
            if(crossed(e1,e2)){
                Edge[] result={e1,e2};
                return result;
            }
        }}
        return null;
    }
    
    /** Fundamental group action! No check yet on validity here...*/
    public GroupElementId actLeft(GroupElement x){
        // perform the gluing; result is contained in gTemp
        TemperleyLiebElement tlx=(TemperleyLiebElement)x;
        Graph gTemp=(Graph)tlx.getGraph().clone();
        int plus=gTemp.glueTo(this.getGraph(),this.outputs,tlx.inputs);
        //System.out.println("gtemp"+gTemp.toString());
        
        // populate a mapping of vertices to adjacent vertices
        TreeMap<Integer,TreeSet<Integer>> adj=gTemp.getAllAdjacencies();
        ArrayDeque<ArrayDeque<Integer>> strands=getAllStrandsFromAdjacency(gTemp,adj);
        //System.out.println("adj"+adj.toString());
        //System.out.println("strands"+strands.toString());
        
        // Construct and add an edge for each strand. Here's where some account
        // must be taken for signs/kinks/vertex removals.
        // TODO account for signs/kinks
        // TODO remove trivial loops
        Graph resultGraph=new Graph();resultGraph.directed=false;resultGraph.multiEdge=true;
        resultGraph.addEdge(-1,-1,gTemp.getLoopsAt(-1));
        int newKinks=0;
        for(ArrayDeque<Integer> strand:strands){
            //System.out.println("kinks in strand "+strand.toString()+" = "+kinkNumber(strand));
            newKinks+=kinkNumber(strand);
            if(strand.getFirst()==strand.getLast()){resultGraph.addTrivialLoop();} else{
                int start=strand.getFirst();
                int end=strand.getLast();
                resultGraph.addEdge(start,end);
                //check for crossings along first/last edge of the strand
                if(start>plus&&end>plus&&this.crossed(start-plus,end-plus)){newKinks++;}
                if(start<=plus&&end<=plus&&tlx.crossed(start,end)){newKinks++;}
            }
        }
        //System.out.println(resultGraph.toString());
        
        // Constructs the new graph based on the above computation.
        TemperleyLiebElement result=new TemperleyLiebElement();
        result.g.clear();
        result.inputs=tlx.inputs;
        result.outputs=this.outputs;
        result.getGraph().addAll(resultGraph);
        result.relabelVertices();
        result.kinks=tlx.kinks+this.kinks+newKinks;
        return result;
    }
    
// STATIC METHODS

    /** Returns whether a given integer list is a valid paren notation. */
    public static boolean validParen(int[] p,int ps,int pe){
        if(p==null||pe>=p.length){return false;}
        if(pe<ps){return true;}
        if(pe==ps){return p[ps]==0;}
        return validParen(p,ps+1,ps+p[ps])&&validParen(p,ps+p[ps]+1,pe);
    }
    /** Same method without bounds. */
    public static boolean validParen(int[] p){return validParen(p,0,p.length-1);}
    
    /** Returns a TL element corresponding to a given permutation. */
    public static TemperleyLiebElement getPermutationElement(PermutationElement p){
        TemperleyLiebElement result=new TemperleyLiebElement(p.getN());
        result.g.clear();
        for(int i=1;i<=p.getN();i++){
            result.addEdge(i,2*p.getN()+1-p.get(i));
        }
        return result;
    }
    
    /** Help method. Determines which of a given integer list is contained in an arraydeque. */
    private static Integer notContained(TreeSet<Integer> adj,ArrayDeque<Integer> deque){
        int v0=adj.first();
        if(!deque.contains(v0)){return v0;}
        int v1=adj.last();
        if(!deque.contains(v1)){return v1;}
        return null;
    }
    
    /** Returns a deque containing the given integer based on a list of adjacencies. */
    public static ArrayDeque<Integer> getStrandFromAdjacency(Graph g,TreeMap<Integer,TreeSet<Integer>> adj,Integer i){
        ArrayDeque<Integer> result=new ArrayDeque<Integer>();
        result.add(i);
        // add onto the beginning of the deque
        Integer newFirst;
        do{
            newFirst=notContained(adj.get(result.getFirst()),result);
            if(newFirst==null){break;}
            result.addFirst(newFirst);
        }while(true);
        // add onto the end of the deque
        Integer newLast;
        do{
            newLast=notContained(adj.get(result.getLast()),result);
            if(newLast==null){break;}
            result.addLast(newLast);
        }while(true);
        // if the result is a loop, add on another vertex to indicate this
        if(g.getValency(result.getFirst())==2){result.addLast(result.getFirst());}
        return result;
    }
    
    /** Returns an array of deques as given above, one for each connected component. */
    public static ArrayDeque<ArrayDeque<Integer>> getAllStrandsFromAdjacency(Graph g,TreeMap<Integer,TreeSet<Integer>> adj){
        ArrayDeque<ArrayDeque<Integer>> result=new ArrayDeque<ArrayDeque<Integer>>();
        TreeSet<Integer> vertices=new TreeSet<Integer>();
        vertices.addAll(adj.keySet());
        vertices.remove(-1);
        do{
            result.add(getStrandFromAdjacency(g,adj,vertices.first()));
            vertices.removeAll(result.getLast());
        }while(vertices.size()>0);
        return result;
    }
    
    /** Counts the number of kinks in an ArrayDeque. To have a kink, there must be at least 4 elements (if a loop) or 5 (if an edge).
     * In the case of an edge, the first and last elements are ignored. Otherwise, a kink exists when the list of integers increases or
     * decreases twice in a row... e.g. 2-3-4 represents a kink, while 2-4-3 does not. This simply counts the number of such!
     * The case with an X above a U (or upsidedown) is considered outside of this method.
     */
    public static int kinkNumber(ArrayDeque<Integer> strand){
        // requires at least 5 nodes to have a kink... unless its an "X" over a "U", which must be taken care of outside this method
        if(strand.size()<5){return 0;}
        
        // compute list of sign changes... open strands will ignore the first and last element
        int last=0;
        ArrayDeque<Integer> signArray=new ArrayDeque<Integer>();
        for(Integer current:strand){if(last!=0){signArray.add(current>last?1:-1);}last=current;}
        //System.out.println(signArray.toString());
        
        // in the case of open strands, delete the first and last sign
        if(strand.getFirst()!=strand.getLast()){signArray.removeFirst();signArray.removeLast();}
        
        // count the number of adjacent signs ++ and --
        int result=0;
        int lastSign=0;
        for(int sign:signArray){if(sign==lastSign){result++;lastSign=0;}else{lastSign=sign;}}
        return result;
    }
    
// Iterator Methods... basis elements only!
    
    /** Whether a parenthetical notation has a next value */
    public static boolean hasParenAfter(int[] p,int ps,int pe){
        if(!validParen(p,ps,pe)){return false;}
        for(int i=ps;i<=pe;i++){if(p[i]!=0)return true;}
        return false;
    }
    /** Same method without bounds */
    public static boolean hasParenAfter(int[] p){return hasParenAfter(p,0,p.length-1);}
    /** Determines the next element given parenthetical notation */
    public static int[] parenAfter(int[] p,int ps,int pe){
        //String s="1. ps="+ps+" pe="+pe+" p=[";for(int i=ps;i<=pe;i++){s+=p[i];}System.out.println(s+"]");
        if(ps==pe){int[] result={p[ps]-1};return result;} 
        if(!hasParenAfter(p,ps,pe)){return null;}
        int[] result=new int[pe-ps+1];
        for(int i=ps;i<=pe;i++){result[i-ps]=p[i];}
        /** Break p[ps..pe] down into 3 sets: p[ps], p[ps+1..ps+first], p[ps+first+1..pe] */
        int first=p[ps];
        /** Increment the last set first, if possible */
        if(hasParenAfter(p,ps+first+1,pe)){
            //System.out.println("replacing after");
            int[] pReplace=parenAfter(p,ps+first+1,pe);
            for(int i=ps+first+1;i<=pe;i++){result[i-ps]=pReplace[i-ps-first-1];}
        }
        else if(hasParenAfter(p,ps+1,ps+first)){
            //System.out.println("replacing inside");
            int[] pReplace=parenAfter(p,ps+1,ps+first);
            for(int i=ps+1;i<=ps+first;i++){result[i-ps]=pReplace[i-ps-1];}
        }
        else if(first<=0){System.out.println("error!");return null;}
        else{
            //System.out.println("replacing first");
            first--;
            for(int i=0;i<=first;i++){result[i]=first-i;}
            for(int i=first+1;i<pe-ps+1;i++){result[i]=pe-ps-i;}
        }
        //s="2. result=[";for(int i=0;i<pe-ps+1;i++){s+=result[i];}System.out.println(s+"]");
        return result;
    }
    /** Same method without bounds */
    public static int[] parenAfter(int[] p){return parenAfter(p,0,p.length-1);}
    
    /** Determines whether there is a next TL element */
    public boolean hasNext(){return g.getNumTrivialLoops()==0&&kinks==0&&hasParenAfter(getParen());}
    
    /** Determines the next TL element, if it exists */
    public TemperleyLiebElement next(){if(!hasNext()){return null;}return new TemperleyLiebElement(parenAfter(getParen()));}
    
    /** Required by Iterator interface */
    public void remove(){}
    
    /** Compare to another AddInt */
    public int compareTo(Object o){
        return Integer.valueOf(getParenString())-Integer.valueOf(((TemperleyLiebElement)o).getParenString());
    }
}
