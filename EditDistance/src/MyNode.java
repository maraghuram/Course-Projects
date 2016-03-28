public class MyNode {
        int i,j;
        String c;
        boolean flag;
        boolean highlight;
        
        public MyNode(int i,int j) {
            this.i = i;
            this.j = j;
            highlight=flag=false;
        }
        public MyNode(int i,int j,String c) {
            this.i = i;
            this.j = j;
            this.c = c;
            highlight=flag=false;
        }
        
        public String toString() {
            if(!(i==0 || j==0)) return i+","+j+"("+DPEngine.GetCostMatrix()[i][j]+")";
            else return c+"("+DPEngine.GetCostMatrix()[i][j]+")";
            
        }        
    }