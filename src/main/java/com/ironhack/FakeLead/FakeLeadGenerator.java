package com.ironhack.FakeLead;

public class FakeLeadGenerator {
    public static void main(String[] args) {
        net.datafaker.Faker faker = new net.datafaker.Faker();
        java.io.FileWriter writer;
        for (int j = 0; j < 5; j++) {
            try {
                writer = new java.io.FileWriter("import/rawLeadsData" + j + ".csv");
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }

            var sb = new StringBuilder();
            for (int i = 0; i < 30; i++) {
                var company = Math.random() * 10 > 5 ? faker.company().name().trim().split(",")[0] : faker.commerce().brand();
                sb.append(faker.name().fullName()).append(",")
                        .append(faker.phoneNumber().cellPhone().replace(" ", ""))
                        .append(",").append(faker.name().username()).append("@").append(company.replace(" ", "_").
                                replace("'", "")).append(".com").append(",").append(company).append("\n");
            }
            try {
                writer.write(sb.toString());
                writer.close();
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
