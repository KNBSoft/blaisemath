/*
 * SimulationFactory.java
 * Created on Sep 18, 2007, 10:00:49 AM
 */

package utility;

import simulation.Team;
import behavior.Behavior;
import zDeprecated.Tasking;
import java.awt.Color;
import java.util.Vector;
import simulation.Simulation;
import valuation.Goal;
import valuation.Valuation;
import valuation.VictoryCondition;
import sequor.model.StringRangeModel;
import tasking.TaskGenerator;

/**
 * This class provides preset simulations to load into a program.
 * <br><br>
 * @author Elisha Peterson
 */
public class SimulationFactory {
    
    // CONSTANTS
    
    /** n-on-1 game */
    public static final int SIMPLE_PE=0;
    /** Specifies simple game with two teams, pursuers and evaders */
    public static final int TWOTEAM=1;
    /** Specifies simple game with three teams, pursuers, pursuers/evaders, and evaders */
    public static final int SIMPLE_PPE=2;
    /** Specifies game with two teams, pursuers, and evaders, with evaders seeking a goal */
    public static final int GOAL_PE=3;
    /** Lots of teams!! */
    public static final int LOTS_OF_FUN=4;
    /** For looking at lead factors. */
    public static final int LEAD_FACTOR=5;
    /** Custom */
    public static final int CUSTOM=6;
    public static final String[] GAME_STRINGS={"Follow the Light","Cops & Robbers","Antarctica","Sahara","Swallowed","Jurassic","Custom"};


    // STATIC FACTORY METHODS
    
    public static StringRangeModel comboBoxRangeModel(){return new StringRangeModel(GAME_STRINGS,SIMPLE_PE,0,6);}
    
    public static void setSimulation(Simulation sim,int simCode){
        String name;
        Vector<Team> teams;
        int primary;
        switch(simCode){
        case SIMPLE_PE:
            name="Follow the Light"; teams=lightSimulation(); primary=1; break;
        case TWOTEAM:      
            name="Cops & Robbers"; teams=twoTeamSimulation(); primary=1; break;
        case SIMPLE_PPE:    
            name="Antarctica"; teams=threeTeamSimulation(); primary=1; break;
        case GOAL_PE:       
            name="Sahara"; teams=twoPlusGoalSimulation(); primary=0; break;
        case LOTS_OF_FUN:   
            name="Swallowed"; teams=bigSimulation(); primary=0; break;
        case LEAD_FACTOR:   
            name="Jurassic"; teams=leadFactorSimulation(); primary=0; break;
        default:
            name="custom operation not supported!"; teams=null; primary=0; break;
        }
        sim.mainInitialize(name, teams.size(), teams, primary);
    }
    
    /** Simulation representing an n-on-1 scenario. */
    public static Vector<Team> lightSimulation(){
        Vector<Team> teams=new Vector<Team>();
        //                      NAME        #    STARTING POS        BEHAVIOR ALGORITHM          COLOR
        Team bugTeam=new Team(  "Bugs",     4,   Team.START_RANDOM,  Behavior.LEADING,   Color.DARK_GRAY);
        Team lightTeam=new Team("Light",    1,   Team.START_RANDOM,  Behavior.STRAIGHT,  Color.GREEN);
        
        Valuation val=new Valuation(bugTeam, lightTeam, Valuation.DIST_MIN);
        
        bugTeam.setVictoryCondition(new VictoryCondition.Basic(val,VictoryCondition.NEITHER,VictoryCondition.WON));        
        bugTeam.addCaptureCondition(lightTeam,1.0);
        
        bugTeam.addValuation(val);     
        bugTeam.addValuation(new Valuation(bugTeam, lightTeam, Valuation.NUM_OPPONENT));
        lightTeam.addValuation(new Valuation(lightTeam, bugTeam, Valuation.TIME_TOTAL));
        
        bugTeam.addAutoGoal(1.0,lightTeam,Goal.SEEK,TaskGenerator.AUTO_CLOSEST,1.0);
        lightTeam.addAutoGoal(1.0,bugTeam,Goal.FLEE,TaskGenerator.AUTO_GRADIENT,1.0);
        
        teams.add(bugTeam);
        teams.add(lightTeam);
        return teams;
    }
    
    public static Vector<Team> twoTeamSimulation(){
        Vector<Team> teams=new Vector<Team>();        
        //                                      #    STARTING POS        BEHAVIOR ALGORITHM          COLOR
        Team copTeam=new Team(      "Cops",     5,   Team.START_RANDOM,  Behavior.LEADING,   Color.BLUE);
        Team robberTeam=new Team(   "Robbers",  4,   Team.START_RANDOM,  Behavior.STRAIGHT,  Color.ORANGE);
        
        Valuation val=new Valuation(copTeam, robberTeam, Valuation.NUM_CAP);
        copTeam.setVictoryCondition(new VictoryCondition.Basic(val,VictoryCondition.NEITHER,VictoryCondition.WON));
        copTeam.addCaptureCondition(robberTeam,5.0);
        
        copTeam.addValuation(val);
        copTeam.addValuation(new Valuation(copTeam, robberTeam, Valuation.DIST_AVG));
        
        robberTeam.addAutoGoal(1.0, copTeam,   Goal.FLEE,  TaskGenerator.AUTO_GRADIENT,1.0);
        copTeam.addAutoGoal(   1.0, robberTeam,Goal.CAPTURE,  TaskGenerator.CONTROL_CLOSEST, 1.0);
        
        teams.add(copTeam);
        teams.add(robberTeam);
        return teams;
    }
    
    public static Vector<Team> threeTeamSimulation(){
        Vector<Team> teams=new Vector<Team>();
        //                      #    STARTING POS        BEHAVIOR ALGORITHM          COLOR
        Team dogTeam=new Team(  3,   Team.START_RANDOM,  Behavior.LEADING,   Color.BLUE);
        Team catTeam=new Team(  4,   Team.START_RANDOM,  Behavior.LEADING,   Color.BLACK);
        Team mouseTeam=new Team(5,   Team.START_RANDOM,  Behavior.STRAIGHT,  Color.GREEN);
        dogTeam.addAutoGoal(  1.0,catTeam,  Goal.CAPTURE,TaskGenerator.CONTROL_CLOSEST,1.0);
        catTeam.addAutoGoal(  0.5,dogTeam,  Goal.FLEE,TaskGenerator.AUTO_GRADIENT,  1.0);
        catTeam.addAutoGoal(  1.0,mouseTeam,Goal.CAPTURE,TaskGenerator.CONTROL_CLOSEST,1.0);
        mouseTeam.addAutoGoal(0.5,catTeam,  Goal.FLEE,TaskGenerator.AUTO_GRADIENT,  1.0);
        mouseTeam.addAutoGoal(  1.0,dogTeam,Goal.CAPTURE,TaskGenerator.CONTROL_CLOSEST,1.0);
        dogTeam.addAutoGoal(0.5,mouseTeam,  Goal.FLEE,TaskGenerator.AUTO_GRADIENT,  1.0);
        dogTeam.setString("Seals");
        catTeam.setString("Penguins");
        mouseTeam.setString("Fish");
        teams.add(dogTeam);
        teams.add(catTeam);
        teams.add(mouseTeam);
        return teams;
    }
    
    public static Vector<Team> twoPlusGoalSimulation(){
        Vector<Team> teams=new Vector<Team>();        
        //                      #    STARTING POS        BEHAVIOR ALGORITHM          COLOR
        Team dogTeam=new Team(  3,   Team.START_RANDOM,  Behavior.LEADING,   Color.ORANGE);
        Team catTeam=new Team(  4,   Team.START_RANDOM,  Behavior.STRAIGHT,              Color.GRAY);
        Team milk=new Team(     1,   Team.START_RANDOM,  Behavior.STATIONARY,        Color.BLUE);
        dogTeam.addAutoGoal(  1.0,catTeam,  Goal.CAPTURE,TaskGenerator.CONTROL_CLOSEST,1.0);
        catTeam.addAutoGoal(  0.5,dogTeam,  Goal.FLEE, TaskGenerator.AUTO_GRADIENT, 1.0);
        catTeam.addAutoGoal(  1.0,milk,     Goal.CAPTURE,TaskGenerator.AUTO_CLOSEST,   1.0);
        dogTeam.setString("Lions");
        catTeam.setString("Wildebeest");
        milk.setString("Watering Hole");
        teams.add(dogTeam);
        teams.add(catTeam);
        teams.add(milk);
        return teams;
    }
    
    public static Vector<Team> leadFactorSimulation(){
        Vector<Team> teams=new Vector<Team>();
        
        //                 #    STARTING POS        BEHAVIOR ALGORITHM          COLOR
        Team dogs=new Team(11,  Team.START_ZERO,    Behavior.LEADING,   Color.DARK_GRAY);
        Team cats=new Team(1,   Team.START_RANDOM,  Behavior.APPROACHPATH,         Color.GREEN);
        cats.setFixedPath("20cos(t/4)","20sin(t/2)");
        for(int i=0;i<dogs.size();i++){
            dogs.get(i).setColor(new Color(100+15*i,25*i,25*i));
            dogs.get(i).setLeadFactor(i/10.0);
        }
        dogs.get(0).setColor(Color.DARK_GRAY);
        dogs.get(dogs.size()-1).setColor(new Color(100,100,250));
        dogs.addAutoGoal(1.0, cats, Goal.SEEK,TaskGenerator.AUTO_CLOSEST, 1.0);
        dogs.setString("Velociraptors");
        cats.setString("Mathematicians");
        teams.add(dogs);
        teams.add(cats);
        return teams;
    }
    
    public static Vector<Team> bigSimulation(){
        Vector<Team> teams=new Vector<Team>();
        //                  #     STARTING POS        BEHAVIOR ALGORITHM          COLOR
        teams.add(new Team( 1,    Team.START_RANDOM,  Behavior.LEADING,   Color.RED));
        teams.add(new Team( 4,    Team.START_RANDOM,  Behavior.LEADING,   Color.ORANGE));
        teams.add(new Team( 3,    Team.START_LINE,    Behavior.LEADING,   Color.YELLOW));
        teams.add(new Team( 5,    Team.START_ARC,     Behavior.LEADING,   Color.GREEN));
        teams.add(new Team( 6,    Team.START_RANDOM,  Behavior.LEADING,   Color.CYAN));
        teams.add(new Team( 3,    Team.START_CIRCLE,  Behavior.LEADING,   Color.BLUE));
        teams.add(new Team( 2,    Team.START_RANDOM,  Behavior.LEADING,   Color.MAGENTA));
        teams.add(new Team( 4,    Team.START_CIRCLE,  Behavior.STRAIGHT,              Color.PINK));
        teams.add(new Team( 2,    Team.START_RANDOM,  Behavior.STRAIGHT,              Color.GRAY));
        teams.add(new Team( 2,    Team.START_RANDOM,  Behavior.STATIONARY,        Color.BLACK));
        teams.get(0).setString("Old Lady");
        teams.get(1).setString("Horses");
        teams.get(2).setString("Cows");
        teams.get(3).setString("Goats");
        teams.get(4).setString("Dogs");
        teams.get(5).setString("Cats");
        teams.get(6).setString("Birds");
        teams.get(7).setString("Spiders");
        teams.get(8).setString("Flies");
        teams.get(9).setString("Why");
        for(int i=0;i<9;i++){
            teams.get(i).addAutoGoal(1.0,teams.get(i+1),Goal.CAPTURE,TaskGenerator.CONTROL_CLOSEST,1.0);
            teams.get(i+1).addAutoGoal(0.5,teams.get(i),Goal.FLEE,TaskGenerator.AUTO_GRADIENT, 1.0);
        }
        return teams;
    }
}
