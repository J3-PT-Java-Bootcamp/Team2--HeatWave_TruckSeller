package com.ironhack.CRMManager;

import com.ironhack.Commercial.Lead;
import com.ironhack.Commercial.Opportunity;
import com.ironhack.MenuUtilities.Menu;
import com.ironhack.MenuUtilities.MenuOption;

import java.util.Map;
import java.util.Scanner;

public class Screens {
    public static void mainMenu(User user){
        int selected;
        final MenuOption<Integer>[] mainMenu = new MenuOption[]{
                MenuOption.create("Check Leads", 1),
                MenuOption.create(" Check Opportunities", 2),
                MenuOption.create("See my Stats", 3),
                MenuOption.create("See Fast Command list", 4),
                MenuOption.create("See Users Manual", 5),
                MenuOption.create("Logout", 6)
        };
        var main_menu = new Menu<>(mainMenu, "Welcome back " + user.getName() + ", what do you want to do?", "You can also type fast commands if know them!");

        selected = main_menu.display();
        runSelected(selected, user);

    }
    public static void checkLeads(User user){
        for (Map.Entry<String, Lead> e : user.getLeadList().entrySet())
            System.out.println(e.getValue());
    }
    public static void checkOpportunities (User user){
        for (Map.Entry<String, Opportunity> e : user.getOpportunityList().entrySet())
            System.out.println(e.getValue());
    }
    public static void seeUserstats(User user){
        System.out.println("Pending leads: " + user.getLeadlistSize());
        System.out.println("Open opportunities: " + user.getOpportunityListSize());
    }
    public static void seeFastCommands (){}
    public static void seeUsersManual(){}
    public static void logOut(){}
    public static void runSelected(int selected, User user){
      switch (selected){
          case 1: checkLeads(user); break;
          case 2: checkOpportunities(user); break;
          case 3: seeUserstats(user); break;
          case 4: seeFastCommands(); break;
          case 5: seeUsersManual(); break;
          case 6: logOut(); break;
      }
    }

    public static void logIn (){
        boolean advance = false;
        do {
            Scanner user = new java.util.Scanner(System.in);
            System.out.println("User");
            String userName = user.nextLine();
            System.out.println("Password");
            String password = user.nextLine();
            if (CRMManager.checkCredentials(userName, password)){
                mainMenu(CRMManager.getUserList().get(userName));
                advance = true;
            } else System.out.println("Invalid credentials please try again");
        } while (advance = false);


    }

}
