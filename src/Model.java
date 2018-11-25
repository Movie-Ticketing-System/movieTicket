/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project3;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;


public class Model {
    private Connection con;
    private java.sql.Statement st;
    private ResultSet rs;
    private String  jdbc_drivers, url, user, password = "";
    private String current_user, current_native, current_lastConver;
    private String status;
    
    public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";
    
    private String sampleProperty;
    
    private PropertyChangeSupport propertySupport;
    
    public Model() {
        propertySupport = new PropertyChangeSupport(this);
    }
    
    public String getSampleProperty() {
        return sampleProperty;
    }
    
    public void setSampleProperty(String value) {
        String oldValue = sampleProperty;
        sampleProperty = value;
        propertySupport.firePropertyChange(PROP_SAMPLE_PROPERTY, oldValue, sampleProperty);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    public void database(){
        con = null;
        st = null;
        rs = null;
        
        jdbc_drivers = "com.mysql.jdbc.Driver";
        url = "jdbc:mysql://localhost:3306/movieticket";
        user = "root";
        password = "root";
        
    }
    
    public Connection getConnection(){
           // connect to databese and set up Statement
            Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieticket?autoReconnect=true&useSSL=false","root","root");
            st = conn.createStatement();

        }catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            //conn.close();
            System.exit(0);
        }
      
        return conn;
     }
    
    public int[][] getSeats(int show) {
        String s = "";
        int count = 0;
        int[][] seats = {
            new int[8],
            new int[8],
            new int[10],
            new int[10],
            new int[10],
            new int[10],
        };
        
        
        try {
//            System.setProperty("jdbc.drivers", jdbc_drivers);
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch(ClassNotFoundException e) {
                System.err.println("cant get DB Driver");
            }
 
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieticket", "root", "root");
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM seats WHERE time_id = '"+show+"'");

            if (rs.next()) {
                s = rs.getString(6);
            }

        } catch (SQLException ex) {
            //Logger lgr = Logger.getLogger(Version.class.getName());
            //lgr.log(Level.SEVERE, ex.getMessage(), ex);
               Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
               System.out.println("Exception Caught");
               
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
               // Logger lgr = Logger.getLogger(Version.class.getName());
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                //lgr.log(Level.WARNING, ex.getMessage(), ex);
                            }
        }
        
        
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < (i < 2? 8:10); j++) {
                seats[i][j] = Integer.parseInt(""+s.charAt(count));
            }
        }
        
        return seats;
    }
    
    
    public String getMovie(int mov) {
        String s = "";
        try {
            System.setProperty("jdbc.drivers", jdbc_drivers);
 
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieticket", "root", "root");
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM movies WHERE Movie_ID = "+mov+"");

            if (rs.next()) {
                s = rs.getString(2);
            }

        } catch (SQLException ex) {
            //Logger lgr = Logger.getLogger(Version.class.getName());
            //lgr.log(Level.SEVERE, ex.getMessage(), ex);
               Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
               System.out.println("Exception Caught");
               
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
               // Logger lgr = Logger.getLogger(Version.class.getName());
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                //lgr.log(Level.WARNING, ex.getMessage(), ex);
                            }
        }
        
        return s;
    }
    
    /**
     * @return String array of all movies stored in the database
     */
    public String[] getListings() {
        String s = "";
        String[] results = {};
        try {
            System.setProperty("jdbc.drivers", jdbc_drivers);
 
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieticket", "root", "root");
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM movies");
            
            while (rs.next()) {
                s += rs.getString(2)+"@";   //Creates a string of all movies and uses "@" as a delimiter
            }
            
            results = s.split("@");         //Segments the string using the delimiter and stores each segment as an element in an array

        } catch (SQLException ex) {
            //Logger lgr = Logger.getLogger(Version.class.getName());
            //lgr.log(Level.SEVERE, ex.getMessage(), ex);
               Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
               System.out.println("Exception Caught");
               
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
               // Logger lgr = Logger.getLogger(Version.class.getName());
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                //lgr.log(Level.WARNING, ex.getMessage(), ex);
                            }
        }
        
        return results;
    }
    
    public void createTicket(int sid, String seat, String name) {
        try {
            System.setProperty("jdbc.drivers", jdbc_drivers);
 
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieticket", "root", "");
            st = con.createStatement();
            st.executeUpdate("INSERT INTO tickets (Show_ID, Seat, Name) VALUES ('"+sid+"', '"+seat+"', '"+name+"')");
            

        } catch (SQLException ex) {
            //Logger lgr = Logger.getLogger(Version.class.getName());
            //lgr.log(Level.SEVERE, ex.getMessage(), ex);
               Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
               
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
               // Logger lgr = Logger.getLogger(Version.class.getName());
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                //lgr.log(Level.WARNING, ex.getMessage(), ex);
                            }
        }
    }
    
    public int insert(String Table, ArrayList input) {
        try {
            String query="insert into "+Table+" ";
            query+= "values (";
            query+="'"+input.get(0)+"'";
            for(int i=1; i<input.size() ;i++){
                query+=", '"+input.get(i)+"'";
            }
            query+= " )";
            st.executeUpdate(query);
        } catch(SQLException e) {
            System.err.println("SQL Error");
        }
        
        return 0;
    }
    
    public int delete(String Table, int input) {
        try {
            String query="DELETE FROM " + Table + " WHERE main_ID=\""+ input +"\";";
            st.executeUpdate(query);
        } catch(SQLException e) {
            System.err.println("SQL Error");
        }
        
        return 0;
    }
    
    public ResultSet select(String Table, String column, String value) {
        try {
            String query="SELECT FROM " + Table + " WHERE " + column + "=\""+ value +"\";";
            rs = st.executeQuery(query);
        } catch(SQLException e) {
            System.err.println("SQL Error");
        }
        
        return rs;
    }
    
    public int update(String Table, String col, String value, String rowID, String valueID) {
        try {
            String query="Update "+Table+" SET "+col+" = \'"+value+"\' WHERE "+rowID+" = "+valueID+"";
            st.executeUpdate(query);
        } catch(SQLException e) {
            System.err.println("SQL Error");
        }
        
        return 0;
    }
    
    public static void main(String[] args) {
        Model testModel = new Model();
        testModel.getConnection();
    }
}