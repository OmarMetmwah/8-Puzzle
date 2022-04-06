package astar;
import java.util.*;

public class Astar {
    
    boolean existOnlyOnce(String str, char x)
    {
    int count = 0;
    for (int i = 0; i < str.length() ; i++)
        if (str.charAt(i) == x) count++;
    return count == 1 ; 
    }
    
    //Swap Function to move the board and change its state
    public String swap(String state, int n1, int n2){
        char[] c = state.toCharArray();
        char temp = c[n1];
        c[n1] = c[n2];
        c[n2] = temp;
        String newState = new String(c);
        return newState;
    }
    
    //Check if an array contains specific string -aka state- or not (used for explored array)
    public boolean searchArr(String searchedValue, String[] arr){
        for(String x : arr){
            if( x == null) break;
            if( x.equals(searchedValue) ) return true;
        }
        return false;
    }
    
    //Check if a queue contains specific string or not 
    public boolean searchQue(String searchedValue, Queue q){
            return q.contains(searchedValue);
    }
    
    //Calculate the hurestic of the state using Manhaten Distance
    public String mHeuristic(String state){
        int sum =0;
        sum += Math.abs( (state.indexOf("1")/3 )-0 ) + Math.abs( (state.indexOf("1")%3)-1 );
        sum += Math.abs( (state.indexOf("2")/3 )-0 ) + Math.abs( (state.indexOf("2")%3)-2 );
        sum += Math.abs( (state.indexOf("3")/3 )-1 ) + Math.abs( (state.indexOf("3")%3)-0 );
        sum += Math.abs( (state.indexOf("4")/3 )-1 ) + Math.abs( (state.indexOf("4")%3)-1 );
        sum += Math.abs( (state.indexOf("5")/3 )-1 ) + Math.abs( (state.indexOf("5")%3)-2 );
        sum += Math.abs( (state.indexOf("6")/3 )-2 ) + Math.abs( (state.indexOf("6")%3)-0 );
        sum += Math.abs( (state.indexOf("7")/3 )-2 ) + Math.abs( (state.indexOf("7")%3)-1 );
        sum += Math.abs( (state.indexOf("8")/3 )-2 ) + Math.abs( (state.indexOf("8")%3)-2 );
        return sum+"#"+state;
    }
    
    //Perform A* search on the intial state to reach the goalTest
    public String[] aStar(String intialState, String goalTest){
        PriorityQueue<String> frontier = new PriorityQueue();
        frontier.add(mHeuristic(intialState));
        String state, newState;
        String[] nodes = new String[10000];//nodes of the tree
        int j = 0;//nodes of the tree
        int index;//the position of zero in the state to determine which moves are allowed
        String[] explored = new String[10000];//explored nodes to avoid repeating and infinite loops
        int i = 0;//number of nodes explored
        int depth=0;//depth of search
        while (! frontier.isEmpty()){
            state = frontier.poll();
            state = state.substring(state.indexOf("#")+1);
            depth++;
            explored[i++] = state;
            if(state.equals(goalTest)){
                break;
            }
            index=state.indexOf("0");
            
            //left
            if(index-1 >= 0 && index!=0&& index!=3&& index!=6){
                
                newState = swap(state,index,index-1);
                nodes[j++] = newState + "left";
                newState = mHeuristic(newState);
                if(!(searchQue(newState,frontier)) && !(searchArr(newState.substring(state.indexOf("#")+1),explored)) ){
                    frontier.add(newState);
                }
            }
            //right
            if(index+1 <= 8 && index!=2&& index!=5&& index!=8){
                newState = swap(state,index,index+1);
                nodes[j++] = newState + "right";
                newState = mHeuristic(newState);
                if(!(searchQue(newState,frontier)) && !(searchArr(newState.substring(state.indexOf("#")+1),explored)) ){
                    frontier.add(newState);
                }
            }
            //up
            if(index-3 >= 0 && index!=0&& index!=1&& index!=2){
                newState = swap(state,index,index-3);
                nodes[j++] = newState + "up";
                newState = mHeuristic(newState);
                if(!(searchQue(newState,frontier)) && !(searchArr(newState.substring(state.indexOf("#")+1),explored)) ){
                    frontier.add(newState);
                }
            }
            //down
            if(index+3 <= 8 && index!=6&& index!=7&& index!=8){
                
                newState = swap(state,index,index+3);
                nodes[j++] = newState + "down";
                newState = mHeuristic(newState);
                if(!(searchQue(newState,frontier)) && !(searchArr(newState.substring(state.indexOf("#")+1),explored)) ){
                    frontier.add(newState);
                }
            }
        }
        System.out.println( "NODES EXPANDED:   " + (i-1) );
        System.out.println( "DEPTH OF SEARCH:   " + (depth-1) );
       return nodes;
    }
    
    //Go back on the history of the nodes to draw the path
    public String[] path(String intialState, String[] nodes ){
        int i = nodes.length-1;
        String opr, str, x;
        int index;
        String[] path = new String[100];
        int j = 0;
        while(nodes[i]==null) i--;
        while(!nodes[i].substring(0,9).equals("012345678")) i--;
        while(i>=0 && !(nodes[i].substring(0,9).equals(intialState)) ){
            opr = nodes[i].substring(9);
            str = nodes[i].substring(0,9);
            index =str.indexOf("0");
            switch (opr){
                case "right":
                    x = swap(str,index,index-1);
                    path[j] = x+"Right";j++;
                    while(i>=0 && ! nodes[i].substring(0,9).equals(x)){
                        i--;
                    }
                    break;
                case "left":
                    x = swap(str,index,index+1);
                    path[j] = x+"Left";j++;
                    while(i>=0 && ! nodes[i].substring(0,9).equals(x)){
                        i--;
                    }
                    break;
                case "up":
                     x = swap(str,index,index+3);
                    path[j] = x+"Up";j++;
                    while(i>=0 && ! nodes[i].substring(0,9).equals(x)){
                        i--;
                    }
                    break;
                case "down":
                     x = swap(str,index,index-3);
                    path[j] = x+"Down";j++;
                    while(i>=0 && ! nodes[i].substring(0,9).equals(x)){
                        i--;
                    }
                    break;
                default:
                    i--;
                    break;
            }         
        }
        return path;
    }
    
    //print the path with each state
    public void print(String[] path){
        int i =path.length-1;
        while(path[i]==null) i--;
        if(i!=0){System.out.println("COST Of Path:   "+(i+1));System.out.println("The PATH IS:");}
        for(; i>-1; i--){
            System.out.println("┌────┬─────┬─────┐");
            System.out.print("│   ");System.out.print(path[i].charAt(0));System.out.print("       ");System.out.print(path[i].charAt(1));System.out.print("        ");System.out.print(path[i].charAt(2));System.out.printf("    │\n");
            System.out.println("├────┼─────┼─────┤");
            System.out.print("│   ");System.out.print(path[i].charAt(3));System.out.print("       ");System.out.print(path[i].charAt(4));System.out.print("        ");System.out.print(path[i].charAt(5));System.out.printf("    │  **"+path[i].substring(9)+"**\n");
            System.out.println("├────┼─────┼─────┤");
            System.out.print("│   ");System.out.print(path[i].charAt(6));System.out.print("       ");System.out.print(path[i].charAt(7));System.out.print("        ");System.out.print(path[i].charAt(8));System.out.printf("    │\n");
            System.out.println("└────┴─────┴─────┘");
        }

    }
    
    
    public static void main(String[] args) {
        String goalTest = "012345678";
        Scanner read = new Scanner(System.in);
        String intialState = read.nextLine().replaceAll(",", "");
        Astar main = new Astar();
        if(!( main.existOnlyOnce(intialState,'0') && main.existOnlyOnce(intialState,'1')&&main.existOnlyOnce(intialState,'2')&&main.existOnlyOnce(intialState,'3')&&main.existOnlyOnce(intialState,'4')&&main.existOnlyOnce(intialState,'5')&&main.existOnlyOnce(intialState,'6')&&main.existOnlyOnce(intialState,'7')&&main.existOnlyOnce(intialState,'8'))){System.out.println("Invalid Input");System.exit(0);}
        if(intialState.equals(goalTest)){System.out.println("It's already solved");System.exit(0);}
        long startTime = System.nanoTime();
        String[] tree = main.aStar(intialState, goalTest);
        String[] path =  main.path(intialState ,tree);
        main.print(path);
        //printting the goal State
        String[] Done = new String[]{"012345678Done☺"};
        main.print(Done);
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("RUNNING TIME:   "+ totalTime +" nano sec" );
    }
    
}
