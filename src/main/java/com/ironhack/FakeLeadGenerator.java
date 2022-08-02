package com.ironhack;

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
            for (int i = 0; i < 15; i++) {
                var company = Math.random() * 10 > 5 ? faker.company().name().trim().split(",")[0] : faker.commerce().brand();
                sb.append(faker.name().fullName());
                sb.append(",");
                sb.append(faker.phoneNumber().cellPhone().replace(" ", ""));
                sb.append(",");
                sb.append(faker.name().username() + "@" + company.replace(" ", "_").replace("'", "") + ".com");
                sb.append(",");
                sb.append(company);
                sb.append("\n");
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
