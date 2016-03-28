  
    public class MyLink {        
        String type;
        int id;
        boolean flag;
        
        public MyLink(int edgeCount,String type) {
            this.id = edgeCount;
            this.type = type;
            this.flag=false;
        } 

        public String toString() {
            return type+" "+id;
        }
        
    }