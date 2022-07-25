package com.ironhack;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Commercial.Lead;
import com.ironhack.ScreenManager.Text.TextObject;

public class FakeLead {
    public static TextObject[] getRawLeads(int val) {
        net.datafaker.Faker faker = new net.datafaker.Faker();
        var resArr= new TextObject[val];
        for (int i = 0; i < val; i++) {
            resArr[i]=new TextObject();
            var company=faker.company().name().trim().split(",")[0];
            resArr[i].addText(CRMManager.crmData.getNextID(Lead.class));
            resArr[i].addText(faker.name().fullName());
            resArr[i].addText(faker.phoneNumber().cellPhone());
            var txt=faker.name().username().replace(".","_") + "@" + company.replace(" ","_") + ".com";
            resArr[i].addText(txt);
            resArr[i].addText(company);
        }
       return resArr;
    }
}
