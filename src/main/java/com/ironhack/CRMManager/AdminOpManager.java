package com.ironhack.CRMManager;

import com.ironhack.CRMManager.Exceptions.CRMException;
import com.ironhack.CRMManager.ScreenManager.Screens.Commands;
import com.ironhack.CRMManager.ScreenManager.Screens.InputScreen;
import com.ironhack.CRMManager.ScreenManager.Screens.TableScreen;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Sales.Lead;
import lombok.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ironhack.CRMManager.CRMData.saveData;
import static com.ironhack.CRMManager.CRMManager.*;
import static com.ironhack.CRMManager.CRMManager.userOpManager;
import static com.ironhack.CRMManager.ScreenManager.InputReader.*;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.EXIT;
import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;
import static com.ironhack.Constants.Constants.*;
@Data
public class AdminOpManager {
    void loadLeadData(User currentUser) throws Exception {
        boolean stop = false;
        do {
            var txtObj = new TextObject("You can load lead data from any CSV file saved on root/import", LIMIT_X / 2, LIMIT_Y)
                    .addText(BLANK_SPACE).setBgcolor(MAIN_BG).setTxtColor(MAIN_TXT_COLOR);
            var rightCol = new TextObject("File names:");
            var leftCol = new TextObject("INDEX: ");
            var files = new ArrayList<>(List.of(Objects.requireNonNull(new File("import").listFiles())));
            for (int i = 0; i < Objects.requireNonNull(files).size(); i++) {
                File file = files.get(i);
                rightCol.addText(file.getName());
                leftCol.addText("-" + i + ": ");
            }
            txtObj.addGroupInColumns(2, txtObj.MAX_WIDTH, new TextObject[]{leftCol, rightCol});
            var inpScreen = new InputScreen(currentUser, "Select a File: ", txtObj,
                    new String[]{"File Index"}, INTEGER);
            var resVal = inpScreen.start();
            if (resVal.contains(":")) resVal = resVal.split(":")[1].trim();
            if (resVal.equals(EXIT.name())) stop = true;
            var index = Integer.parseInt(resVal);
            var currentFile=files.get(index);
            var leads = parseCSVLeads(currentFile);
            if (!leads.isEmpty()) {
                assignLeadsToUser(currentUser,leads);
            }
//            var toDelete = new File(files.remove(index).getAbsolutePath());//FIXME
            try{
                if(!currentFile.delete())
                    throw new RuntimeException("Delete=false!");
            } catch (Exception e) {
                throw new RuntimeException();
            }

            stop=true;



        } while (!stop);
    }
    private void assignLeadsToUser(User currentUser,ArrayList<Lead> leadList) throws Exception {
        TextObject txtObj;
        boolean stop = false;
        do {
            if (crmData.getUserList().isEmpty()) {
                break;
            } else {
                txtObj = new TextObject("-- USERS --").addText(BLANK_SPACE);
                for (User user : crmData.getUserList().values()) {
                    txtObj.addText(user.getName());
                }
                txtObj.addText(BLANK_SPACE)
                        .addText("Enter a user name to assign loaded Leads")
                        .addText("Or enter \"ALL\" to divide leads between all users")
                        .addText(BLANK_SPACE).addText(BLANK_SPACE);
            }
            var selectUserScreen = new InputScreen(currentUser,
                    "Select User:",
                    txtObj,
                    new String[]{"User Name"},
                    OPEN);
            var strRes = selectUserScreen.start();
            if (strRes.equalsIgnoreCase(EXIT.name())) break;
            var userVal = selectUserScreen.getValues();
            if (userVal != null && !userVal.isEmpty()) {
                var users = crmData.getUsers(false);
                int leadsPerUser = leadList.size() / users.size();
                int rest = leadList.size() % users.size();
                int counter = 0;
                if (userVal.get(0).equalsIgnoreCase("ALL")) {
                    for (int i = 0; i < users.size(); i++) {
                        for (int j = 0; j < leadsPerUser + (i == users.size() - 1 ? rest : 0); j++) {
                            String id = leadList.get(j + counter).getId();
                            users.get(i).addToLeadList(id);
                            crmData.addLead(leadList.get(j + counter));
                        }
                        counter += leadsPerUser;
                    }
                    if(!screenManager.isTest())saveData();
                    screenManager.confirming_screen(currentUser,"Leads were distributed among all users.","",false);
                    stop = true;
                } else if (crmData.getUserList().containsKey(userVal.get(0))) {
                    var user = crmData.getUserList().get(userVal.get(0));
                    for (Lead lead : leadList) {
                        user.getLeadList().add(lead.getId());
                        crmData.addLead(lead);
                    }
                    if(!screenManager.isTest())saveData();
                    screenManager.confirming_screen(currentUser,"All leads assigned to: " + userVal.get(0) + " correctly",
                            "",
                            false);
                    stop = true;
                }
            }
        } while (!stop);
    }
    private ArrayList<Lead> parseCSVLeads(File file) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(file));
        ArrayList<Lead> resVal = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            Lead lead = new Lead(values[0], crmData.getNextID(Lead.class), values[1], values[2], values[3]);
            resVal.add(lead);
        }
        if(!screenManager.isTest())saveData();
        return resVal;
    }
    void showStats_screen(User currentUser) {
        boolean stop = false;
        Commands comm = null;
        do {
            try {
                comm = Commands.valueOf(new TableScreen(currentUser,
                        "Users statistics:",
                        new java.util.ArrayList<User>(crmData.getUserList().values())).start());
                switch (comm) {
                    case MENU, BACK, LOGOUT -> stop = true;
                    case VIEW -> userOpManager.viewObject(currentUser, comm.getCaughtInput());
                }
            } catch (NullPointerException e) {
                break;
            } catch (CRMException e) {
                throw new RuntimeException(e);
            }
        } while (!stop);

    }
    void manageUsers_screen(User currentUser) {
        try {
            createNewUser(currentUser,false);
        } catch (CRMException ignored) {

            LogWriter.logError(getClass().getSimpleName(),
                    "manageUsers_screen","Received a unexpected exception.. "+ignored.getMessage());
        }
    }
    /**
     * Creates the newUser screen to create a new user,
     * if there is not previous user it makes admin user by default
     *
     * @param isAdmin true if must create an admin
     * @throws CRMException if Back/Exit/Logout/Menu commands are read
     */
    public void createNewUser(User currentUser,boolean isAdmin) throws CRMException {
        TextObject txtObj;
        if (crmData.getUserList().isEmpty()) {
            txtObj = new TextObject("Enter a Name and Password to create a New Admin")
                    .addText(BLANK_SPACE).addText(BLANK_SPACE);
        } else {
            txtObj = new TextObject("-- USERS --");
            for (User user : crmData.getUserList().values()) {
                txtObj.addText(user.getName());
            }
            txtObj.addText(BLANK_SPACE)
                    .addText("Enter a new user name and password to create new user.")
                    .addText("Or enter an existing user name and a new Password to change it. ")
                    .addText(BLANK_SPACE).addText(BLANK_SPACE);
        }
        var newUserScreen = new InputScreen(currentUser,
                "New User",
                txtObj,
                new String[]{"User Name", "Password", "Repeat Password"},
                OPEN,
                NEW_PASSWORD,
                NEW_PASSWORD);
        var strRes = newUserScreen.start();
        var userVal = newUserScreen.getValues();

        if (userVal != null && !userVal.isEmpty()) {
            if (crmData.getUserList().containsKey(userVal.get(0))) {
                if (userVal.get(1).equalsIgnoreCase(userVal.get(2))) {
                    crmData.getUserList().get(userVal.get(0)).setPassword(userVal.get(1));
                    try {
                        if(!screenManager.isTest())saveData();
                    } catch (Exception ignored) {
                        LogWriter.logError(getClass().getSimpleName(),
                                "createNewUser","Received a unexpected exception.. "+ignored.getMessage());
                    }
                    screenManager.confirming_screen(currentUser,"User " + userVal.get(0) + " password was properly updated.",
                            strRes,
                            true);
                }
            } else {
                if (userVal.size() == 3 && crmData.addToUserList(new User(userVal.get(0), userVal.get(1), isAdmin))) {
                   screenManager.confirming_screen(currentUser,"User " + userVal.get(0) + " was properly saved.",
                            strRes,
                            true);
                }
            }
        }
    }

}
