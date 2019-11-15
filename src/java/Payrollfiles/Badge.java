
package Payrollfiles;


public class Badge {
    
    private String id;
    private String name;
    
    public Badge(String id, String name) {
        
        this.id = id;
        this.name = name;
            
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("#").append(id).append(" (").append(name).append(")");
        return s.toString();
    }
    
    public String getBadgeid(){
        return id;
    }
    
    public String getName(){
        return name;
    }
}