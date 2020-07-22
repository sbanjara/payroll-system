
package Payrollfiles.model;

/**
 * The Badge class is an abstraction of an employee's badge. The employee's
 * badge contains badgeid and the employee's full name.
 * @author Sabin Banjara
 */
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