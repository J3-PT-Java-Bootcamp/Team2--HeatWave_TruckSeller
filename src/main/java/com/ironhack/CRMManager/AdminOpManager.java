package com.ironhack.CRMManager;

import com.ironhack.Sales.Lead;
import com.ironhack.CRMManager.Exceptions.CRMException;
import com.ironhack.CRMManager.ScreenManager.ConsolePrinter;
import com.ironhack.CRMManager.ScreenManager.Screens.InputScreen;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import lombok.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;
import static com.ironhack.Constants.Constants.*;
import static com.ironhack.CRMManager.ScreenManager.InputReader.INTEGER;
import static com.ironhack.CRMManager.ScreenManager.InputReader.OPEN;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.EXIT;
@Data
public class AdminOpManager {
    private final CRMManager manager;
    private final ConsolePrinter printer;
    void loadLeadData() {
        boolean stop = false;
        do {
            var txtObj = new TextObject("You can load lead data from any CSV file saved on root/import", LIMIT_X / 2, LIMIT_Y)
                    .addText(BLANK_SPACE).setBgcolor(MAIN_BG).setTxtColor(MAIN_TXT_COLOR);
            var rightCol = new TextObject("File names:");
            var leftCol = new TextObject("INDEX: ");
            var files = new File("import").listFiles();
            for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
                File file = files[i];
                rightCol.addText(file.getName());
                leftCol.addText("-" + i + ": ");
            }
            txtObj.addGroupInColumns(2, txtObj.MAX_WIDTH, new TextObject[]{leftCol, rightCol});
            var inpScreen = new InputScreen(manager, printer, "Select a File: ", txtObj,
                    new String[]{"File Index"}, INTEGER);
            try {
                var resVal = inpScreen.start();
                if (resVal.contains(":")) resVal = resVal.split(":")[1].trim();
                if (resVal.equals(EXIT.name())) stop = true;
                var index = Integer.parseInt(resVal);
                var leads = parseCSVLeads(files[index]);
                if (!leads.isEmpty()) {
                    assignLeadsToUser(leads);
                }

            } catch (Exception ignored) {
            }


        } while (!stop);
    }

    private void assignLeadsToUser(ArrayList<Lead> leadList) throws Exception {
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
            var selectUserScreen = new InputScreen(manager,
                    printer,
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
                    manager.saveData();
                    manager.getScreenManager().confirming_screen("Whatever....","",false);
                    stop = true;
                } else if (crmData.getUserList().containsKey(userVal.get(0))) {
                    var user = crmData.getUserList().get(userVal.get(0));
                    for (Lead lead : leadList) {
                        user.getLeadList().add(lead.getId());
                        crmData.addLead(lead);
                    }
                    manager.saveData();
                    manager.getScreenManager().confirming_screen("All leads assigned to: " + userVal.get(0) + " correctly",
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
        manager.saveData();
        return resVal;
    }

    void showStats_screen() {
        //TODO
    }

    void manageUsers_screen() {
        try {
            manager.getScreenManager().newUser_screen(false);
        } catch (CRMException ignored) {

        }
    }
}
